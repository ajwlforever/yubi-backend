package com.yupi.springbootinit.manager;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

import static com.yupi.springbootinit.config.RabbitMqConfig.AI_CHAT_QUEUE;
import static com.yupi.springbootinit.config.RabbitMqConfig.DATA_ANALYSIS_QUEUE;

/*
    接受业务消息并处理
 */
@Slf4j
@Component
public class ServiceMessageReceiver {

    // 监听数据处理消息
    @RabbitListener(queues =  DATA_ANALYSIS_QUEUE)
    public void receiveDataAnalysisMessage(Message message, Channel channel) throws UnsupportedEncodingException {
        String msg = new String(message.getBody(), "UTF-8");
        log.info("接收到数据分析消息：{}", msg);
        boolean ack = true;
        //
        Exception exception =   null;


    }

    // 监听AI对话服务
    @RabbitListener(queues =  AI_CHAT_QUEUE)
    public void receiveAiChatMessage(Message message, Channel channel) throws UnsupportedEncodingException {
        String msg = new String(message.getBody(), "UTF-8");
        log.info("接收AI对话消息：{}", msg);
        boolean ack = true;
        //
        Exception exception =   null;


    }



}

