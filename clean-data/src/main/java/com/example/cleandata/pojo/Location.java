package com.example.cleandata.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author QiuHongLong
 */
@Data
@Builder
@Accessors(chain = true)
public class Location {

    /**
     * 地点ID
     */
    private int locationId;
    /**
     * 地点信息
     */
    private String locationNote;
    /**
     * 经度
     */
    private double longitude;
    /**
     * 维度
     */
    private double latitude;

}
