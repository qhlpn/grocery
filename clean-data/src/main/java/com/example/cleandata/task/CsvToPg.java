package com.example.cleandata.task;

import com.example.cleandata.pojo.IpSegment;
import com.example.cleandata.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;


/**
 * @author QiuHongLong
 * @description 读取CSV文件数据存入PG数据库
 */
@Component
@Slf4j
public class CsvToPg {

    @Autowired
    private JdbcTemplate jdbc;

    public void action01() {
        // read csv
        String filePath = "C:\\Users\\QiuHongLong\\Desktop\\ip_area_isp.csv";
        ArrayList<String> data = readCsv01(filePath);
        log.info("read csv length: " + data.size());
        // clean data
        ArrayList<IpSegment> ips = cleanData01(data);
        log.info("ok ips length: " + ips.size());
        // operate pg
        // savePg01(ips);
    }

    private ArrayList<String> readCsv01(String filePath) {
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

    private ArrayList<IpSegment> cleanData01(ArrayList<String> data) {
        ArrayList<IpSegment> ipSegments = new ArrayList<>();
        ArrayList<String> errorIps = new ArrayList<>();
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
        StringBuilder sb = new StringBuilder();
        sb.append("error ips : ");
        errorIps.forEach(v -> sb.append(v).append("; "));
        log.info(sb.toString());
        return ipSegments;
    }

    private void savePg01(ArrayList<IpSegment> ips) {
        int totalSize = ips.size();
        int batchSize = 100000;
        int currentSize = 0;
        String sql = "insert into util_ip_segment (segment_id, start_id, end_id, cidr, start_ip, end_ip, location_id, create_date) values (?, ?, ?, ?, ?, ?, ?, ?)";
        List<Object[]> batchArgs = new ArrayList<>();
        while (currentSize < totalSize) {
            int nextSize = currentSize + batchSize;
            if (nextSize < totalSize) {
                while (currentSize < nextSize) {
                    IpSegment ip = ips.get(currentSize);
                    batchArgs.add(new Object[]{ip.getSegmentId(), ip.getStartIdLong(), ip.getEndIdLong(), ip.getCIdr(), ip.getStartIdStr(), ip.getEndIdStr(), ip.getLocationId(), new Date()});
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
                    batchArgs.add(new Object[]{ip.getSegmentId(), ip.getStartIdLong(), ip.getEndIdLong(), ip.getCIdr(), ip.getStartIdStr(), ip.getEndIdStr(), ip.getLocationId(), new Date()});
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


}
