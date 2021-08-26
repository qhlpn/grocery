package com.example.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.stereotype.Component;

@Component("seqCon")
public class Sequence {

    public void run() throws MQClientException, InterruptedException {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("group1");
        consumer.setInstanceName("consumerA");
        consumer.setNamesrvAddr("10.190.90.240:9876");
        consumer.subscribe("topic", "tag");
        consumer.setMessageModel(MessageModel.BROADCASTING);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        DefaultMQPushConsumer finalConsumer = consumer;
        // MessageListenerOrderly
        consumer.registerMessageListener((MessageListenerOrderly) (msgs, context) -> {
            System.out.println(finalConsumer.getInstanceName() + "\t" + Thread.currentThread().getName() + "\t" + msgs);
            return ConsumeOrderlyStatus.SUCCESS;
        });
        consumer.start();

    }
}
