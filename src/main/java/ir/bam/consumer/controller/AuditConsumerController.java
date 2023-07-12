package ir.bam.consumer.controller;

import ir.bam.consumer.service.ConsumeWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuditConsumerController {

    @Autowired
    private ConsumeWebService consumeWebService;

    @RequestMapping(value = "/consumer")
    public void sendToLogService(@RequestBody String message) {consumeWebService.sendMessageToLogServer(message);}
}
