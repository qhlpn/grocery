package com.example.cleandata.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;


/**
 * @author QiuHongLong
 */
@Slf4j
public class HttpUtils {

    private static CloseableHttpClient httpClient = HttpClientBuilder
            .create()
            .setMaxConnPerRoute(50)
            .setMaxConnTotal(100)
            .build();


    public static String doGet(String reqURL) {
        String resCxt = null;
        HttpGet httpGet = new HttpGet(reqURL);
        try (CloseableHttpResponse res = httpClient.execute(httpGet)) {
            HttpEntity entity = res.getEntity();
            if (null != entity) {
                resCxt = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        } finally {
            httpGet.releaseConnection();
        }
        return resCxt;
    }


}
