package com.example.rocketmq.producer.base;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.stereotype.Component;

@Component
public class OneWay {

    public void run() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("groupA");
        producer.setNamesrvAddr("10.190.90.240:9876");
        producer.start();
        for (int i = 0; i < 100; i++) {
            Message message = new Message("topic", "tag", "message content".getBytes(RemotingHelper.DEFAULT_CHARSET));
            producer.sendOneway(message);
            Thread.sleep(1000);
        }
        producer.shutdown();
    }



}
