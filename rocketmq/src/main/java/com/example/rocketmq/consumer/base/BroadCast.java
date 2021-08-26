package com.example.rocketmq.consumer.base;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.stereotype.Component;


@Component
public class BroadCast {

    public void run() throws MQClientException, InterruptedException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("group1");
        consumer.setInstanceName("consumerA");
        consumer.setNamesrvAddr("10.190.90.240:9876");
        consumer.subscribe("topic", "tag");
        consumer.setMessageModel(MessageModel.BROADCASTING);
        // 如果查询不到消息消费进度时（即初次启动时），从什么地方开始消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
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
        consumer.setMessageModel(MessageModel.BROADCASTING);
        DefaultMQPushConsumer finalConsumerB = consumer;
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) ->{
            System.out.println(finalConsumerB.getInstanceName() + "\t" + Thread.currentThread().getName() + "\t" + msgs);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } );
        consumer.start();
        System.out.println("consumerB start.");

        Thread.sleep(60 * 1000);
    }

}
