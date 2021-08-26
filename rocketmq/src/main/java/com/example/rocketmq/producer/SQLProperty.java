package com.example.rocketmq.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.stereotype.Component;


@Component("proPro")
public class SQLProperty {

    public void run() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("proGroup");
        producer.setNamesrvAddr("10.190.90.240:9876");
        producer.setSendMsgTimeout(10000);
        producer.start();
        for (int i = 0; i < 100; i++) {
            Message message = new Message("topic", "tag", String.valueOf(i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            message.putUserProperty("key", String.valueOf(i));
            SendResult sendResult = producer.send(message);
            System.out.println(sendResult.toString());
            Thread.sleep(1000);
        }
        producer.shutdown();
    }

}
