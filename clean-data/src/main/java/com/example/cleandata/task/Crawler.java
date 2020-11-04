package com.example.cleandata.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.cleandata.pojo.Location;
import com.example.cleandata.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 爬虫
 *
 * @author QiuHongLong
 */

@Component
@Slf4j
public class Crawler {

    private static final String BAIDU_MAP_AK = "I4rbXkKpje3kfCYGM7sxl8wSN8eNVZyv";
    private static final String BAIDU_MAP_URL = "http://api.map.baidu.com/geocoding/v3?ak=" + BAIDU_MAP_AK + "&output=json" + "&ret_coordtype=gcj02ll";

    private static final String GAODE_MAP_KEY = "24bf4ebad16e00741f2affe6d4f1ff3d";
    private static final String GAODE_MAP_URL = "https://restapi.amap.com/v3/geocode/geo?key=" + GAODE_MAP_KEY;


    private static final String FILE_PATH = "C:\\Users\\QiuHongLong\\Desktop\\area_lng_lat.csv";

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void action01() {

        // query db --> get location_note
        ArrayList<Location> locations = queryDB01();
        // http request --> get lng lat
        log.info("locations total size : " + locations.size());
        int clock = 0;
        int recordBatch = locations.size() / 100;
        for (Location location : locations) {
            doGet01(location);
            // save csv
            saveCsv01(location);
            clock++;
            if (clock % recordBatch == 0) {
                log.info("run data cnt : " + clock);
            }
        }

    }


    private ArrayList<Location> queryDB01() {

        String sql = "select location_id locationId, note locationNote from util_location where location_id >= 0 order by location_id asc";
        List<Map<String, Object>> cxt = jdbcTemplate.queryForList(sql);
        ArrayList<Location> locations = new ArrayList<>();
        cxt.forEach(map -> {
            Location location = Location.builder()
                    .locationId((Integer) map.get("locationid"))
                    .locationNote((String) map.get("locationnote"))
                    .build();
            locations.add(location);
        });
        return locations;

    }

    private void doGet01(Location location) {
        String locationNote = location.getLocationNote();
        String url = GAODE_MAP_URL + "&address=" + locationNote;
        try {
            String res = HttpUtils.doGet(url);
            Thread.sleep(2000);
            JSONObject resObject = JSONObject.parseObject(res);
            JSONArray geocodes = resObject.getJSONArray("geocodes");
            String lnglat = geocodes.getJSONObject(0).getString("location");
            location.setLongitude(Double.parseDouble(lnglat.split(",")[0]))
                    .setLatitude(Double.parseDouble(lnglat.split(",")[1]));
        } catch (Exception e) {
            log.error("http get error, breakpoint location : " + location.toString());
            throw new RuntimeException(e.getMessage());
        }
    }

    private void saveCsv01(Location location) {
        String cxt = location.getLocationId() + ";"
                + location.getLocationNote() + ";"
                + location.getLongitude() + ";"
                + location.getLatitude();
        try (FileWriter fw = new FileWriter(FILE_PATH, true)) {
            try (BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(cxt);
                bw.newLine();
                bw.flush();
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

}
