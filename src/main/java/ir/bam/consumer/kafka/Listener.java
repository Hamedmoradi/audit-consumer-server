package ir.bam.consumer.kafka;

import ir.bam.consumer.service.ConsumeWebService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;

import java.util.concurrent.CountDownLatch;



@ConfigurationProperties(prefix = "spring.kafka.template.default-topic")
public class Listener {

    private static final org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(Listener.class);

    @Autowired
    private ConsumeWebService consumeWebService;

    public CountDownLatch countDownLatch0 = new CountDownLatch(300);
    public CountDownLatch countDownLatch1 = new CountDownLatch(300);
    public CountDownLatch countDownLatch2 = new CountDownLatch(300);

    @KafkaListener(id = "bmi_audit-0", topicPartitions = {@TopicPartition(topic = "bmi_audit", partitions = {"0"})})
    public void listenPartition0(ConsumerRecord<?, ?> record) {
        log.info("Listener Id0, Thread ID: " + Thread.currentThread().getId());
        log.info("Received: " + record);

        consumeWebService.sendMessageToLogServer(record.value().toString());
        countDownLatch0.countDown();

    }

    @KafkaListener(id = "bmi_audit-1", topicPartitions = {@TopicPartition(topic = "bmi_audit", partitions = {"1"})})
    public void listenPartition1(ConsumerRecord<?, ?> record) {
        log.info("Listener Id1, Thread ID: " + Thread.currentThread().getId());
        log.info("Received: " + record);

        consumeWebService.sendMessageToLogServer(record.value().toString());
        countDownLatch1.countDown();
//                Acknowledgment acknowledgment = record.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);
//        if (acknowledgment != null) {
//            System.out.println("Acknowledgment provided");
//            acknowledgment.acknowledge();
//        }
    }

    @KafkaListener(id = "bmi_audit-2", topicPartitions = {@TopicPartition(topic = "bmi_audit", partitions = {"2"})})
    public void listenPartition2(ConsumerRecord<?, ?> record) {
        log.info("Listener Id2, Thread ID: " + Thread.currentThread().getId());
        log.info("Received: " + record);
        consumeWebService.sendMessageToLogServer(record.value().toString());
        countDownLatch2.countDown();

    }

}