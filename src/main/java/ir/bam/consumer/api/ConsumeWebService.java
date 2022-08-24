package ir.bam.consumer.api;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ConsumeWebService {


    @Value("${audit.server.request.url}")
    private String requstUrl;

    @Value("${audit.server.response.url}")
    private String responseUrl;

    @Value("${audit.server.methodCall.url}")
    private String methodCallUrl;

    private final RestTemplate restTemplate = new RestTemplate();


    @RequestMapping(value = "/consumer")
    public ResponseEntity<String> sendToLogService(@RequestBody String json) {

        if (getType(json).equals("auditRequest")) {
            auditApiCall(separateMessage(json), getToken(json), requstUrl);
        } else if (getType(json).equals("auditResponse")) {
            auditApiCall(separateMessage(json), getToken(json), responseUrl);
        } else if (getType(json).equals("auditMethodCall")) {
            auditApiCall(separateMessage(json), getToken(json), methodCallUrl);
        }
        return null;
    }

    public String getType(String json) {
        JSONObject jObject = new JSONObject(json);
        String type = jObject.get("type").toString();
        return type;
    }

    private void auditApiCall(String message, String token, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(message, headers);
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

