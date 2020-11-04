package com.example.cleandata.utils;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author QiuHongLong
 */
@Component
@Slf4j
public class SshUtils {

    public SshUtils() {
        try {
            Session session = new JSch().getSession("ctgtest", "203.55.10.34", 22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword("ctrQ@4etWPbn6u");
            session.connect();
            session.setPortForwardingL("127.0.0.1", 18921, "10.150.59.56", 18921);
            log.info("postgresql connect go through ssh channel !");
        } catch (Exception e) {
            log.error("ssh settings is failed. skip!", e);
        }
    }


}
