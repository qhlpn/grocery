package com.example.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.stereotype.Component;

@Component("proCon")
public class SQLProperty {

    public void run() throws MQClientException, InterruptedException {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("conGroup");
        consumer.setInstanceName("consumer");
        consumer.setNamesrvAddr("10.190.90.240:9876");
        consumer.subscribe("topic", MessageSelector.bySql("key between 15 and 20"));
        consumer.setMessageModel(MessageModel.CLUSTERING);
        DefaultMQPushConsumer finalConsumer = consumer;
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) ->{
            System.out.println(finalConsumer.getInstanceName() + "\t" + Thread.currentThread().getName() + "\t" + msgs);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } );
        consumer.start();
        System.out.println("consumer start.");
        Thread.sleep(60 * 1000);
    }

}
