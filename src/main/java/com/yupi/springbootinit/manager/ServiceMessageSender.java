package com.yupi.springbootinit.manager;


import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.yupi.springbootinit.config.RabbitMqConfig.DATA_ANALYSIS_ROUTE_KEY;
import static com.yupi.springbootinit.config.RabbitMqConfig.MAIN_DIRECT_EXCHANGE;

@Slf4j
@Component
public class ServiceMessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String message){
        rabbitTemplate.convertAndSend(MAIN_DIRECT_EXCHANGE, DATA_ANALYSIS_ROUTE_KEY, "lalal" );
    }
}
