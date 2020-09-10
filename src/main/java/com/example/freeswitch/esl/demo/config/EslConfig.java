package com.example.freeswitch.esl.demo.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.freeswitch.esl.demo.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.freeswitch.esl.client.IEslEventListener;
import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.inbound.InboundConnectionFailure;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author unisk1123
 * @Description esl 客户端配置
 * @create 2020-09-01 4:43 PM
 */
@Slf4j
@Configuration
public class EslConfig {

    @Value("${freeswitch.esl.host}")
    private String host;

    @Value("${freeswitch.esl.port}")
    private int port;

    @Value("${freeswitch.esl.password}")
    private String password;

    @Autowired
    private TestService testService;

    @Bean
    public Client client() {
        Client client = new Client();

        try {
            client.connect(host, port, password, 10);
        } catch (InboundConnectionFailure e) {
            log.error("Connect failed", e);
        }
        // 注册事件处理程序
        client.addEventListener(new IEslEventListener() {
            @Override
            public void eventReceived(EslEvent event) {

                String type = event.getEventName();
                Map<String, String> map = event.getEventHeaders();
                String call_uuid = map.get("Unique-ID");
                String caller_id_name = map.get("Caller-Caller-ID-Number");
                String caller_id_number = map.get("Caller-Caller-ID-Name");
                String destination_number = map.get("Caller-Destination-Number");
                JSONObject json = JSONObject.parseObject(JSON.toJSONString(map));
                switch (type) {
                    case EventConstant.RECORD_START:
                        String filepath = map.get("Record-File-Path");
                        testService.test(type);
                        break;
                    case EventConstant.RECORD_STOP:
                        break;
                    case EventConstant.CHANNEL_ANSWER:
                        log.info("event listener, type: {}, event headers: {}.", type, json.toJSONString());
                        testService.test(type);
                        break;
                    case EventConstant.CHANNEL_BRIDGE:
                        long startTime = Long.parseLong(map.get("Caller-Channel-Created-Time"));
                        long answerTime = Long.parseLong(map.get("Caller-Channel-Answered-Time"));
                        break;
                    case EventConstant.CHANNEL_DESTROY:
                        break;
                    case EventConstant.CHANNEL_HANGUP_COMPLETE:
                        log.info("event listener, type: {}, event headers: {}.", type, json.toJSONString());
                        testService.test(type);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void backgroundJobResultReceived(EslEvent event) {
                log.info("Background job result received+:" + event.getEventName() + "/" + event.getEventHeaders());
            }
        });
        client.setEventSubscriptions("plain", "all");
        return client;
    }

}
