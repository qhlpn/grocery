package com.example.rocketmq.producer.base;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.stereotype.Component;

@Component
public class Async {

    public void run() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("groupA");
        producer.setNamesrvAddr("10.190.90.240:9876");
        producer.start();
        for (int i = 0; i < 100; i++) {
            Message message = new Message("topic", "tag", "message content".getBytes(RemotingHelper.DEFAULT_CHARSET));
            int temp = i;
            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println(temp + "\t" + sendResult.toString());
                }

                @Override
                public void onException(Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
            Thread.sleep(1000);
        }
        producer.shutdown();
    }


}
