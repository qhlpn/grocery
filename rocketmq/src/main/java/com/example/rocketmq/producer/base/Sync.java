package com.example.rocketmq.producer.base;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.stereotype.Component;

@Component
public class Sync {

    public void run() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("groupA");
        producer.setNamesrvAddr("10.190.90.240:9876");
        producer.setSendMsgTimeout(10000);
        producer.start();
        Message message = new Message("topic", "tag", "message content".getBytes(RemotingHelper.DEFAULT_CHARSET));
        SendResult result = producer.send(message);
        System.out.println(result.toString());
        result = producer.send(message);
        System.out.println(result.toString());
        producer.shutdown();
        Thread.sleep(10 * 1000);
    }



}
