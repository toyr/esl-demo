package com.example.freeswitch.esl.demo.service.impl;

import com.example.freeswitch.esl.demo.service.TestService;
import com.sun.xml.internal.ws.api.ha.StickyFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author unisk1123
 * @Description
 * @create 2020-09-01 5:06 PM
 */
@Slf4j
@Service
public class TestServiceImpl implements TestService {
    @Override
    public void test(String type) {
        log.info(type);
    }
}
