package com.example.cleandata.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author QiuHongLong
 */
@Data
@Builder
@Accessors(chain = true)
public class IpSegment {
    private long segmentId;
    private long startIdLong;
    private long endIdLong;
    private String cIdr;
    private String startIdStr;
    private String endIdStr;
    private String isp;
    private int locationId;
    private Date createDate;
    private Date updateDate;

}
