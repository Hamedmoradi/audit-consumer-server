package ir.bam.consumer.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CountDownLatch;


@Slf4j
@ConfigurationProperties(prefix = "spring.kafka.template.default-topic")
public class Listener {

    @Value("${audit.server.request.url}")
    private String requstUrl;

    @Value("${audit.server.response.url}")
    private String responseUrl;

    @Value("${audit.server.methodCall.url}")
    private String methodCallUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public CountDownLatch countDownLatch0 = new CountDownLatch(3);
    public CountDownLatch countDownLatch1 = new CountDownLatch(3);
    public CountDownLatch countDownLatch2 = new CountDownLatch(3);

    @KafkaListener(id = "bmi_audit-0", topicPartitions = {@TopicPartition(topic = "bmi_audit", partitions = {"0"})})
    public void listenPartition0(ConsumerRecord<?, ?> record) {
//        log.info("Listener Id0, Thread ID: " + Thread.currentThread().getId());
//        log.info("Received: " + record);
        String message = separateMessage((String) record.value());
        String token = getToken((String) record.value());
        saveRequest(message, token);
        countDownLatch0.countDown();
    }

    @KafkaListener(id = "bmi_audit-1", topicPartitions = {@TopicPartition(topic = "bmi_audit", partitions = {"1"})})
    public void listenPartition1(ConsumerRecord<?, ?> record) {
//        log.info("Listener Id1, Thread ID: " + Thread.currentThread().getId());
//        log.info("Received: " + record);
        String message = separateMessage((String) record.value());
        String token = getToken((String) record.value());
        saveResponse(message, token);
        countDownLatch1.countDown();
    }

    @KafkaListener(id = "bmi_audit-2", topicPartitions = {@TopicPartition(topic = "bmi_audit", partitions = {"2"})})
    public void listenPartition2(ConsumerRecord<?, ?> record) {
//        log.info("Listener Id2, Thread ID: " + Thread.currentThread().getId());
//        log.info("Received: " + record);
        String message = separateMessage((String) record.value());
        String token = getToken((String) record.value());
        saveMethodCall(message, token);
        countDownLatch2.countDown();
    }

    private void saveRequest(String message, String token) {
        auditApiCall(message, token, requstUrl);
    }

    private void saveResponse(String response, String token) {
        auditApiCall(response, token, responseUrl);
    }

    private void saveMethodCall(String response, String token) {
        auditApiCall(response, token, methodCallUrl);
    }

    private void auditApiCall(String message, String token, String url) {
        String body = message;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }

    private String separateMessage(String json) {
        JSONObject jObject = new JSONObject(json);
        JSONObject messagePart = jObject.getJSONObject("message");
        return String.valueOf(messagePart);
    }

    private String getToken(String json) {
        JSONObject jObject = new JSONObject(json);
        String tokenPart = jObject.get("token").toString();
        return tokenPart;
    }
}