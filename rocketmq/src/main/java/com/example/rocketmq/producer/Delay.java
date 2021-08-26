package com.example.rocketmq.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.stereotype.Component;


@Component
public class Delay {

    public void run() throws Exception {

        DefaultMQProducer producer = new DefaultMQProducer("group");
        producer.setNamesrvAddr("10.190.90.240:9876");
        producer.start();

        Message message = new Message("topic", "tag", "A".getBytes(RemotingHelper.DEFAULT_CHARSET));
        // org/apache/rocketmq/store/config/MessageStoreConfig.java
        // private String messageDelayLevel = "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";
        message.setDelayTimeLevel(3);
        SendResult result = producer.send(message);
        System.out.println(result.toString());

        producer.shutdown();
        Thread.sleep(120 * 1000);

    }


}
