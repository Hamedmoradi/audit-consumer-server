package ir.bam.consumer.kafka;

import java.util.concurrent.CountDownLatch;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.web.client.RestTemplate;


@Slf4j
public class Listener {

    private final RestTemplate restTemplate = new RestTemplate();
//    @Value("${spring.kafka.template.default-topic}")
//    private String topic;

    public CountDownLatch countDownLatch0 = new CountDownLatch(3);
    public CountDownLatch countDownLatch1 = new CountDownLatch(3);
    public CountDownLatch countDownLatch2 = new CountDownLatch(3);

    @KafkaListener(id = "id0", topicPartitions = {@TopicPartition(topic = "bmi_audit", partitions = {"0"})})
    public void listenPartition0(ConsumerRecord<?, ?> record) {
//        log.info("Listener Id0, Thread ID: " + Thread.currentThread().getId());
//        log.info("Received: " + record);
        saveRequest(separateMessage((String) record.value()));
        countDownLatch0.countDown();
//        System.out.println(topic);
    }

    @KafkaListener(id = "id1", topicPartitions = {@TopicPartition(topic = "bmi_audit", partitions = {"1"})})
    public void listenPartition1(ConsumerRecord<?, ?> record) {
//        log.info("Listener Id1, Thread ID: " + Thread.currentThread().getId());
//        log.info("Received: " + record);
        saveResponse(separateMessage((String) record.value()));
        countDownLatch1.countDown();
    }

    @KafkaListener(id = "id2", topicPartitions = {@TopicPartition(topic = "bmi_audit", partitions = {"2"})})
    public void listenPartition2(ConsumerRecord<?, ?> record) {
//        log.info("Listener Id2, Thread ID: " + Thread.currentThread().getId());
//        log.info("Received: " + record);
        saveMethodCall(separateMessage((String) record.value()));
        countDownLatch2.countDown();
    }

    private void saveRequest(String message) {
        String url = "http://localhost:9081/audit/audit/request";
        auditApiCall(message, url);

    }

    private void saveResponse(String response) {
        String url = "http://localhost:9081/audit/audit/response";
        auditApiCall(response, url);
    }

    private void saveMethodCall(String response) {
        String url = "http://localhost:9081/audit/audit/methodCall";
        auditApiCall(response, url);
    }

    public String separateMessage(String json) {
        JSONObject jObject = new JSONObject(json);
        JSONObject messagePart = jObject.getJSONObject("message");
        return String.valueOf(messagePart);
    }

    private void auditApiCall(String message, String url) {
        String body = message;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJncmFudCI6IlBBU1NXT1JEIiwiaXNzIjoiaHR0cDovL2FwaS5ibWkuaXIvc2VjdXJpdHkiLCJhdWQiOiJrZXkiLCJleHAiOjE2Mzc2NzIzNTk5NjgsIm5iZiI6MTYzNzU4NTk1OTk2OCwicm9sZSI6ImludGVybmV0IGJhbmstY3VzdG9tZXIiLCJzZXJpYWwiOiJkNDc4NGI5ZC02ZjVkLTNhNGMtYTAwZi04ODlkOTNmN2I5NGUiLCJzc24iOiIzOTIwMTgzNTg0IiwiY2xpZW50X2lkIjoiMTIzIiwic2NvcGVzIjpbImFjY291bnQtc3VwZXIiLCJzc28tbWFuYWdlci1jdXN0b21lciIsInNzby1tYW5hZ2VyLWVucm9sbG1lbnQiLCJjdXN0b21lci1zdXBlciJdfQ==.4_58-CWXtWAEUwDV4XcHU10TWoouZxNVTjOD_k_6g-g");
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }
}