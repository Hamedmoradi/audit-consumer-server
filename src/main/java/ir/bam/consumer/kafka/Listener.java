package ir.bam.consumer.kafka;

import ir.bam.consumer.service.ConsumeWebService;
import java.util.concurrent.CountDownLatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class Listener {

  @Value("${spring.kafka.template.default-topic}")
  private String topicName;
  @Value("${spring.kafka.consumer.group-id}")
  private String groupId;

  @Autowired
  private ConsumeWebService consumeWebService;

  public CountDownLatch countDownLatch = new CountDownLatch(300);

  @KafkaListener(topics = "${spring.kafka.template.default-topic}", groupId = "${spring.kafka.consumer.group-id}")
  public void consume(ConsumerRecord<?, ?> record) {
    log.info("Tópico: {}", topicName);
    log.info("Listener Id2, Thread ID: " + Thread.currentThread().getId());
    log.info("Received: " + record);
    consumeWebService.sendMessageToLogServer(record.value().toString());
    countDownLatch.countDown();
  }

}