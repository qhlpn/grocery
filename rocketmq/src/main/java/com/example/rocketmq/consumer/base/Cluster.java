package com.example.rocketmq.consumer.base;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.stereotype.Component;


@Component
public class Cluster {

    public void run() throws MQClientException, InterruptedException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("group1");
        consumer.setInstanceName("consumerA");
        consumer.setNamesrvAddr("10.190.90.240:9876");
        consumer.subscribe("topic", "tag");
        consumer.setMessageModel(MessageModel.CLUSTERING);
        DefaultMQPushConsumer finalConsumerA = consumer;
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) ->{
            System.out.println(finalConsumerA.getInstanceName() + "\t" + Thread.currentThread().getName() + "\t" + msgs);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } );
        consumer.start();
        System.out.println("consumerA start.");

        consumer = new DefaultMQPushConsumer("group1");
        consumer.setInstanceName("consumerB");
        consumer.setNamesrvAddr("10.190.90.240:9876");
        consumer.subscribe("topic", "tag");
        consumer.setMessageModel(MessageModel.CLUSTERING);
        DefaultMQPushConsumer finalConsumerB = consumer;
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) ->{
            System.out.println(finalConsumerB.getInstanceName() + "\t" + Thread.currentThread().getName() + "\t" + msgs);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } );
        consumer.start();
        System.out.println("consumerB start.");

        Thread.sleep(120 * 1000);
    }

}
