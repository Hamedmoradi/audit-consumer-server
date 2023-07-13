package ir.bam.consumer.service.impl;

import ir.bam.consumer.service.ConsumeWebService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static ir.bam.consumer.enumeration.MessageTypeEnum.*;

@Service
@Async
public class ConsumeWebServiceImpl implements ConsumeWebService {


    @Value("${audit.server.request.url}")
    private String requestUrl;

    @Value("${audit.server.response.url}")
    private String responseUrl;

    @Value("${audit.server.methodCall.url}")
    private String methodCallUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(ConsumeWebServiceImpl.class);


    @Override
    public String separateMessage(String json) {
        JSONObject jObject = new JSONObject(json);
        JSONObject messagePart = jObject.getJSONObject("message");
        return String.valueOf(messagePart);
    }

    @Override
    public String getToken(String json) {
        JSONObject jObject = new JSONObject(json);
        String tokenPart = jObject.get("token").toString();
        return tokenPart;
    }


    public String getType(String json) {
        JSONObject jObject = new JSONObject(json);
        String type = jObject.get("type").toString();
        return type;
    }

    @Override
    public void getRequestMessageFromKafka(String message, String token) {
        auditApiCall(message, token, requestUrl);
    }

    @Override
    public void getResponseMessageFromKafka(String response, String token) {
        auditApiCall(response, token, responseUrl);
    }

    @Override
    public void getMethodCallMessageFromKafka(String response, String token) {
        auditApiCall(response, token, methodCallUrl);
    }

    private void auditApiCall(String message, String token, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(message, headers);
        try {
            restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            log.info("sent to url " + url + "   **** to log server    ");
        } catch (Exception exception) {
            log.error(" error in call " + url + "   **** exception:    " + exception);
        }
    }


    @Override
    public void sendMessageToLogServer(String record) {
        String message = separateMessage(record);
        String type = getType(record);
        String token = getToken(record);
        if (REQUEST_URL.getMessageType().equals(type)) {
            getRequestMessageFromKafka(message, token);
        }
        if (RESPONSE_URL.getMessageType().equals(type)) {
            getResponseMessageFromKafka(message, token);
        }
        if (METHOD_CALL.getMessageType().equals(type)) {
            getMethodCallMessageFromKafka(message, token);
        }
    }
}


