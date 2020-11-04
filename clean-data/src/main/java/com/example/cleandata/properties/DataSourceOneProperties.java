package com.example.cleandata.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author QiuHongLong
 */
@Component
@ConfigurationProperties(prefix = "spring.datasource.one")
@Data
public class DataSourceOneProperties {

    private String driverClassName;
    private String url;
    private String username;
    private String password;

}
