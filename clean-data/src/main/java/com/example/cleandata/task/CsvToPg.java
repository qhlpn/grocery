package com.example.cleandata.task;

import com.example.cleandata.pojo.IpSegment;
import com.example.cleandata.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


/**
 * @author QiuHongLong
 * @description 读取CSV文件数据存入PG数据库
 */
@Component
@Slf4j
public class CsvToPg {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private TransactionTemplate tx;

    public void action() {
        // read csv
        String filePath = "C:\\Users\\QiuHongLong\\Desktop\\ip_area_isp.csv";
        ArrayList<String> data = readCsv(filePath);
        log.info("read csv length: " + data.size());
        // clean data
        ArrayList<IpSegment> ips = cleanData(data);
        log.info("ok ips length: " + ips.size());
        // operate pg
        savePg(ips);
    }

    private ArrayList<String> readCsv(String filePath) {
        ArrayList<String> data = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(new File(filePath))) {
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private ArrayList<IpSegment> cleanData(ArrayList<String> data) {
        ArrayList<IpSegment> ipSegments = new ArrayList<>();
        ArrayList<String> errorIps = new ArrayList<>();
        HashSet<Long> first = new HashSet<>();
        HashSet<String> dupIds = new HashSet<>();
        long seqNumber = 5000000000L;
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
                        if (first.contains(ipSegment.getSegmentId())) {
                            dupIds.add(cIdr.split("/")[0]);
                            ipSegment.setSegmentId(seqNumber);
                            first.add(seqNumber);
                            seqNumber++;
                        } else {
                            first.add(ipSegment.getSegmentId());
                        }
                        ipSegment.setLocationId(locationId);
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

        log.info("dupIds ips Cnt : " + dupIds.size());
        StringBuilder dups = new StringBuilder();
        dups.append("dupIds ips : ");
        dupIds.forEach(v -> dups.append(v).append("; "));
        log.info(dups.toString());

        return ipSegments;
    }

    private void savePg(ArrayList<IpSegment> ips) {
        int totalSize = ips.size();
        int batchSize = 100000;
        int currentSize = 0;
        String sql = "insert into util_ip_segment " +
                "(segment_id, start_id, end_id, cidr, start_ip, end_ip, location_id, create_date) values (?, ?, ?, ?, ?, ?, ?, ?)";
        List<Object[]> batchArgs = new ArrayList<>();
        while (currentSize < totalSize) {
            int nextSize = currentSize + batchSize;
            if (nextSize < totalSize) {
                while (currentSize < nextSize) {
                    IpSegment ip = ips.get(currentSize);
                    batchArgs.add(new Object[]{ip.getSegmentId(), ip.getStartIdLong(), ip.getEndIdLong(),
                            ip.getCIdr(), ip.getStartIdStr(), ip.getEndIdStr(), ip.getLocationId(), new Date()});
                    currentSize++;
                    if (currentSize == nextSize) {
                        txSavePg(sql, batchArgs);
                        log.info("operate pg currentSize: " + currentSize);
                        batchArgs.clear();
                    }
                }
            } else {
                while (currentSize < totalSize) {
                    IpSegment ip = ips.get(currentSize);
                    batchArgs.add(new Object[]{ip.getSegmentId(), ip.getStartIdLong(), ip.getEndIdLong(),
                            ip.getCIdr(), ip.getStartIdStr(), ip.getEndIdStr(), ip.getLocationId(), new Date()});
                    currentSize++;
                    if (currentSize == totalSize) {
                        txSavePg(sql, batchArgs);
                        log.info("operate pg currentSize: " + currentSize);
                        batchArgs.clear();
                    }
                }
            }
        }
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


}
