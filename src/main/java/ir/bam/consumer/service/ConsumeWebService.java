package ir.bam.consumer.service;

public interface ConsumeWebService {

    String getType(String json);

    String getToken(String json);

    String separateMessage(String json);

    void getRequestMessageFromKafka(String message, String token);

    void getResponseMessageFromKafka(String response, String token);

    void getMethodCallMessageFromKafka(String response, String token);

    void sendMessageToLogServer(String message);
}
