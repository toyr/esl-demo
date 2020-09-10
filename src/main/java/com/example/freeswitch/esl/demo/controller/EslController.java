package com.example.freeswitch.esl.demo.controller;

import com.example.freeswitch.esl.demo.freeswitch.esl.client.inbound.Client;
import com.example.freeswitch.esl.demo.freeswitch.esl.client.transport.message.EslMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author unisk1123
 * @Description
 * @create 2020-09-01 4:51 PM
 */
@Slf4j
@RestController
public class EslController {

    @Autowired
    private Client client;

    @GetMapping("/call/{userId}")
    public String call(@PathVariable("userId") String userId) {
        EslMessage eslMessage = client.sendSyncApiCommand("originate", "user/" + userId + " &echo");
        log.info("originate, eslMessage: {}", eslMessage.toString());
        return "success";
    }
}
