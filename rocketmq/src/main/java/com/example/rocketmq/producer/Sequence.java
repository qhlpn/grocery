package com.example.rocketmq.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.stereotype.Component;


@Component("seqPro")
public class Sequence {

    public void run() throws Exception {

        DefaultMQProducer producer = new DefaultMQProducer("group");
        producer.setNamesrvAddr("10.190.90.240:9876");
        producer.start();

        Message message = new Message("topic", "tag", "A".getBytes(RemotingHelper.DEFAULT_CHARSET));
        SendResult result = producer.send(
                message,
                // MessageQueueSelector
                (mqs, msg, arg) -> {
                    System.out.println("arg is " + arg);
                    return mqs.get(Integer.parseInt((String) arg));
                },
                "1");
        System.out.println(result.toString());

        result = producer.send(message, (mqs, msg, arg) -> {
            System.out.println("arg is " + arg);
            return mqs.get(Integer.parseInt((String) arg));
        }, "2");
        System.out.println(result.toString());

        producer.shutdown();
        Thread.sleep(120 * 1000);

    }


}
