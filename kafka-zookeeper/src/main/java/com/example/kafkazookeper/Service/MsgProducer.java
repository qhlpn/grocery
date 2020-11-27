package com.example.kafkazookeper.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * @author QiuHongLong
 */

@Slf4j
@Component
public class MsgProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, Object o) {

        String os = JSON.toJSONString(o);

        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, os);

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.error("生产者发送消息：{} 失败，原因：{}", o.toString(), throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, String> sendResult) {
                log.info("生产者成功发送消息到" + topic + "-> " + sendResult.getProducerRecord().value());
            }
        });
    }


}
