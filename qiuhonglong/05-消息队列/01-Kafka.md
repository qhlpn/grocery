### 消息队列

+ **提高响应性能（异步、削峰）**
+ **降低系统耦合性（解耦）**



### Kafka 架构

<img src="E:\projects\grocery\qiuhonglong\05-消息队列\pictures\image-20201127160901921.png" alt="image-20201127160901921" style="zoom:50%;" />

+ **Producer **：生产者

+ **Consumer**：消费者

+ **Consumer Group**：消费者组，组内消费者不会消费同个消息，**用于实现广播和单播**

+ **Broker**：Kafka 服务器

+ **Topic**：主题（逻辑概念），用来区分不同类型的消息，类似于数据库的表

+ **Partition**：分区（物理存储），每个 Partition 对应一个目录，里面存储消息文件和索引文件，**分区可并发读写提速**

+ **Offset**：偏移量，作为消息在分区中的唯一标识，**保证分区有序性**，**不保证主题有序性**

+ **Replication**：副本，每个 Partition 数据可在其它 Broker 存有副本，**实现高可用，不支持读写分离**

+ **Record**：消息记录，包括 key value 和 timestamp 字段

  

### Producer

> 生产者发送消息（**推模式**），先后经过  `拦截器`  `序列化器`  `分区器`，最终由  `累加器`  批量发送到服务器。

<img src="E:\projects\grocery\qiuhonglong\05-消息队列\pictures\image-20201127163859254.png" alt="image-20201127163859254" style="zoom:70%;" />

+ **常见参数**

  ```properties
  bootstrap.server: 	broker ip + port
  key.serializer: 	key序列化器
  value.serializer: 	value序列化器
  batch.num.messages:	批量值（仅对 async 模式）
  request.required.acks: dft 0 无需等待leader确认 / 1 需leader确认写入本地log / -1 需所有备份都完成（仅对 async 模式）
  request.timeout.ms:	确认超时时间
  message.send.max.retries:	最大重试次数 dft 3
  retry.backoff.ms:	每次尝试增加的额外间隔时间 dft 300
  partitioner.class:	消息分区策略，dft kafka.producer.DefaultPartitioner，可自定义实现 kafka.producer.Partitioner
  producer.type:		消息发送模式（dft sync 同步 / async 异步）
  compression.topic:	压缩方式（dft none / gzip / snappy / lz4）
  compressed.topics:	要压缩的topic，dft null
  topic.metadata.refresh.interval.ms:	定期获取元数据时间 dft 600000（0则每次发送后获取，负数则只在发送失败获取），leader不可以时也会主动获取
  queue.buffering.max.ms:	缓存消息最大持续时间 dft 5000 （仅对 async 模式）
  queue.buffering.max.message:	缓存消息最大数量 dft 10000 （仅对 async 模式）
  queue.enqueue.timeout.ms:	dft -1 （0 缓存满则丢掉 / -1 满则阻塞 / 正值代表满则阻塞的时间 仅对 async 模式）
  ```



### Consumer / Group

> 消费者接收消息（**拉模式**），**将 offset 放在客户端管理** 

> Consumer / Group 结合可实现 **单播 和 广播**

<img src="E:\projects\grocery\qiuhonglong\05-消息队列\pictures\image-20201127170453911.png" alt="image-20201127170453911" style="zoom:70%;" />

+ **Java Comsumer** 是**线程不安全**的，不能数据访问保护。**多线程消费**方案：

  + **同个Group中建立多个Comsumer，消息获取和消息处理耦合**

    <img src="E:\projects\grocery\qiuhonglong\05-消息队列\pictures\image-20201130102545260.png" alt="image-20201130102545260" style="zoom:80%;" />

    ```java
    public class Consumer1 implements Runnable {
        private final KafkaConsumer consumer = ;
        public void run() {
            consumer.subscribe(Arrays.asList("topic"));
        }
    }
    executors.submit(consumer1);
    ...
    ```

  + **单个Comsumer，多个Worker，消息获取和消息处理解耦（即 Reactor模型 = IO多路复用 + 事件驱动）**

    <img src="E:\projects\grocery\qiuhonglong\05-消息队列\pictures\image-20201130174408275.png" alt="image-20201130174408275" style="zoom:80%;" />

    ```java
    private final KafkaConsumer<String, String> consumer;
    private ExecutorService executors;
    executors = new ThreadPoolExecutor(
                workerNum, workerNum, 0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(1000),
                new ThreadPoolExecutor.CallerRunsPolicy());
    while (true) {
    	ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
      	for (final ConsumerRecord record : records) {
        	executors.submit(new Worker(record));
      	}
    }
    ```



