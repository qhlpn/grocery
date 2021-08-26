package com.example.rocketmq.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class Batch {

    public void run() throws Exception {

        DefaultMQProducer producer = new DefaultMQProducer("groupA");
        producer.setNamesrvAddr("10.190.90.240:9876");
        producer.start();

        List<Message> messages = new LinkedList<>();
        messages.add(new Message("topic", "tag", "A".getBytes(RemotingHelper.DEFAULT_CHARSET)));
        messages.add(new Message("topic", "tag", "B".getBytes(RemotingHelper.DEFAULT_CHARSET)));
        messages.add(new Message("topic", "tag", "C".getBytes(RemotingHelper.DEFAULT_CHARSET)));
        SendResult result = producer.send(messages);
        System.out.println(result.toString());

        producer.shutdown();
        Thread.sleep(120 * 1000);

    }
}
