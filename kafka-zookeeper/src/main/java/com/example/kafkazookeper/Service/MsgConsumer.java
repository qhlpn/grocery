package com.example.kafkazookeper.Service;


import com.example.kafkazookeper.Pojo.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author QiuHongLong
 */
@Slf4j
@Component
public class MsgConsumer {


    @KafkaListener(topics = {"topic1"}, groupId = "group1")
    public void receiveMsg(ConsumerRecord<String, String> consumerRecord) {

        String record = consumerRecord.value();

        log.info("消费者消费topic:{} partition:{}的消息 -> {}", consumerRecord.topic(), consumerRecord.partition(), record);

    }


    @KafkaListener(topics = {"topic2"}, groupId = "group2")
    public void consumeMessage2(Message msg) {

        log.info("消费者消费{}的消息 -> {}", "topic2", msg.toString());

    }

}
