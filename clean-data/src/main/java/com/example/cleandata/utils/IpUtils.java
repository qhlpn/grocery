package com.example.cleandata.utils;

import com.example.cleandata.pojo.IpSegment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


/**
 * @author QiuHongLong
 */
@Slf4j
public class IpUtils {

    public static long ipv4ToLong(String ip) {
        byte[] b = new byte[4];
        String[] ip_arr = ip.split("\\.");
        for (int i = ip_arr.length - 1; i > -1; i--) {
            b[i] = new Integer(ip_arr[i]).byteValue();
        }
        return (b[3] & 0xFFL) | ((b[2] << 8) & 0xFF00L) | ((b[1] << 16) & 0xFF0000L) | ((b[0] << 24) & 0xFF000000L);
    }

    public static String ipv4ToStr(long ip) {
        byte[] b = new byte[4];
        for (int i = b.length - 1; i > -1; i--) {
            b[i] = (byte) (ip >>> (i * 8));
        }

        StringBuilder str = new StringBuilder();
        for (int i = b.length - 1; i > 0; i--) {
            str.append(b[i] & 0xFF);
            str.append(".");
        }
        str.append(b[0] & 0xFF);

        return str.toString();
    }

    public static IpSegment getcidrstartandend(String cIdr) {

        String[] splits = cIdr.split("/");

        if (splits.length == 2) {
            String ipStr = splits[0];
            String maskBitStr = splits[1];
            if (StringUtils.isNotBlank(ipStr)
                    && StringUtils.isNotBlank(maskBitStr)) {
                long ip = ipv4ToLong(ipStr);
                int maskBit = 32 - Integer.parseInt(maskBitStr);

                if (ip % (1L << maskBit) != 0
                        || ip % (1L << (maskBit + 1)) == 0) {
                    return null;
                }

                long ipStartLong = (ip >> maskBit) << maskBit;
                long ipEndLong = ipStartLong + ((1L << maskBit) - 1);

                String ipStartStr = ipv4ToStr(ipStartLong);
                String ipEndStr = ipv4ToStr(ipEndLong);

                return IpSegment.builder().segmentId(ip).cIdr(cIdr)
                        .startIdLong(ipStartLong).endIdLong(ipEndLong)
                        .startIdStr(ipStartStr).endIdStr(ipEndStr).build();

            }
        }
        return null;

    }


}
