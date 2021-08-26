package com.example.rocketmq.producer;

import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.stereotype.Component;


@Component
public class Transaction {

    public void run() throws Exception {

        TransactionMQProducer producer = new TransactionMQProducer("group");
        producer.setNamesrvAddr("10.190.90.240:9876");
        producer.setSendMsgTimeout(10000);
        producer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
                // 执行本地事务
                // 根据事务状态返回结果
                System.out.println("local transaction: " + arg);
                return LocalTransactionState.COMMIT_MESSAGE;
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt msg) {
                // 事务补偿回查
                // 检查本地事务执行状态，返回结果
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        });

        producer.start();

        Message message = new Message("topic", "tag", "A".getBytes(RemotingHelper.DEFAULT_CHARSET));
        System.out.println("before send");
        SendResult result = producer.sendMessageInTransaction(message, "arg");
        System.out.println("after send");
        System.out.println(result.toString());

        producer.shutdown();
        Thread.sleep(10 * 1000);

    }


}
