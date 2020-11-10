package com.example.cleandata.task;

import com.example.cleandata.pojo.IpSegment;
import com.example.cleandata.pojo.Location;
import com.example.cleandata.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 读取csv文件数据存入PostgreSQL数据库
 *
 * @author QiuHongLong
 */
@Component
@Slf4j
public class CsvToPg {

    @Autowired
    @Qualifier("jdbcTemplateOne")
    JdbcTemplate jdbc;

    @Autowired
    @Qualifier("transactionTemplateOne")
    private TransactionTemplate tx;

    private ArrayList<String> readCsv(String filePath) {
        ArrayList<String> data = new ArrayList<>();
        try (FileReader fr = new FileReader(filePath)) {
            try (BufferedReader br = new BufferedReader(fr)) {
                String line;
                while ((line = br.readLine()) != null) {
                    data.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }


    private void txSavePg(String sql, List<Object[]> batchArgs) {
        tx.execute((txStatus) -> {
            try {
                jdbc.batchUpdate(sql, batchArgs);
                return true;
            } catch (Exception e) {
                txStatus.setRollbackOnly();
                log.error(e.getMessage());
                return false;
            }
        });
    }


    public void action01() {
        // read csv
        String filePath = "C:\\Users\\QiuHongLong\\Desktop\\ip_area_isp.csv";
        ArrayList<String> data = readCsv(filePath);
        log.info("read csv length: " + data.size());
        // clean data
        ArrayList<IpSegment> ips = cleanData01(data);
        log.info("ok ips length: " + ips.size());
        // operate pg
        savePg01(ips);
    }

    Pattern pattern = Pattern.compile("([^,]*),([^,]*),([^,]*),([^,]*),([^,]*),(.*)");

    private ArrayList<IpSegment> cleanData01(ArrayList<String> data) {
        ArrayList<IpSegment> ipSegments = new ArrayList<>();
        ArrayList<String> errorIps = new ArrayList<>();
        long seqNumber = 340758767710949376L;
        int blankCnt = 0;
        int ipv6Cnt = 0;
        for (String ip : data) {
            String[] columns = ip.split(",");
            if (columns.length >= 2) {
                String cIdr = columns[0];
                String location = columns[1];
                if (cIdr.contains(":")) {
                    ipv6Cnt++;
                }
                if (location.contains("\" \"")) {
                    blankCnt++;
                }
                if (!cIdr.contains(":")
                        && !"\" \"".equals(location)) {
                    int locationId = Integer.parseInt(location);
                    IpSegment ipSegment = IpUtils.getcidrstartandend(cIdr);
                    if (ipSegment != null) {
                        ipSegment.setSegmentId(seqNumber++);
                        ipSegment.setLocationId(locationId);
                        Matcher matcher = pattern.matcher(ip);
                        try {
                            String isp = null;
                            if (matcher.find()) {
                                // 只有先 find 置状态位后才能 group, 不然报错
                                isp = matcher.group(5);
                            }
                            if (StringUtils.isNotBlank(isp)
                                    && !"\" \"".equals(isp)) {
                                ipSegment.setIsp(isp);
                            } else {
                                ipSegment.setIsp("default");
                            }
                        } catch (Exception e) {
                            log.info("String isp = matcher.group(5) error : " + ip);
                            ipSegment.setIsp("default");
                        }
                        ipSegments.add(ipSegment);
                    } else {
                        errorIps.add(cIdr);
                    }
                }
            }
        }
        log.info("blank Cnt : " + blankCnt);
        log.info("ipv6 Cnt : " + ipv6Cnt);

        log.info("error ips Cnt : " + errorIps.size());
        StringBuilder error = new StringBuilder();
        error.append("error ips : ");
        errorIps.forEach(v -> error.append(v).append("; "));
        log.info(error.toString());

        return ipSegments;
    }

    private void savePg01(ArrayList<IpSegment> ips) {
        int totalSize = ips.size();
        int batchSize = 100000;
        int currentSize = 0;
        String sql = "insert into util_ip_segment " +
                "(segment_id, start_id, end_id, cidr, start_ip, end_ip, location_id, create_date, isp) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        List<Object[]> batchArgs = new ArrayList<>();
        while (currentSize < totalSize) {
            int nextSize = currentSize + batchSize;
            if (nextSize < totalSize) {
                while (currentSize < nextSize) {
                    IpSegment ip = ips.get(currentSize);
                    batchArgs.add(new Object[]{ip.getSegmentId(), ip.getStartIdLong(), ip.getEndIdLong(),
                            ip.getCIdr(), ip.getStartIdStr(), ip.getEndIdStr(), ip.getLocationId(), new Date(), ip.getIsp()});
                    currentSize++;
                    if (currentSize == nextSize) {
                        jdbc.batchUpdate(sql, batchArgs);
                        log.info("operate pg currentSize: " + currentSize);
                        batchArgs.clear();
                    }
                }
            } else {
                while (currentSize < totalSize) {
                    IpSegment ip = ips.get(currentSize);
                    batchArgs.add(new Object[]{ip.getSegmentId(), ip.getStartIdLong(), ip.getEndIdLong(),
                            ip.getCIdr(), ip.getStartIdStr(), ip.getEndIdStr(), ip.getLocationId(), new Date(), ip.getIsp()});
                    currentSize++;
                    if (currentSize == totalSize) {
                        jdbc.batchUpdate(sql, batchArgs);
                        log.info("operate pg currentSize: " + currentSize);
                        batchArgs.clear();
                    }
                }
            }
        }
    }


    public void action02() {
        // read csv
        String filePath = "C:\\Users\\QiuHongLong\\Desktop\\area_lng_lat.csv";
        ArrayList<String> data = readCsv(filePath);
        log.info("read csv length: " + data.size());
        // clean data
        ArrayList<Location> locations = cleanData02(data);
        // update postgresql
        updatePg02(locations);

    }


    private ArrayList<Location> cleanData02(ArrayList<String> data) {
        ArrayList<Location> locations = new ArrayList<>();
        data.forEach(s -> {
            String[] columns = s.split(";");
            int exceptedCnt = 4;
            if (columns.length == exceptedCnt) {
                Location location = Location.builder()
                        .locationId(Integer.valueOf(columns[0]))
                        .locationNote(columns[1])
                        .longitude(Double.valueOf(columns[2]))
                        .latitude(Double.valueOf(columns[3]))
                        .build();
                locations.add(location);
            } else {
                log.error("location data error : " + s);
                throw new RuntimeException("location data error : " + s);
            }
        });
        return locations;
    }

    private void updatePg02(ArrayList<Location> locations) {

        String sql = "update util_location set longitude = ?, latitude = ?, create_date = ? where location_id = ?";
        int clock = 0;
        for (Location location : locations) {
            jdbc.update(sql, location.getLongitude(), location.getLatitude(), new Date(), location.getLocationId());
            if (++clock % 500 == 0) {
                log.info("update postgresql data cnt : " + clock);
            }

        }

    }


}
