package com.yupi.springbootinit.config;



import lombok.Data;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
@Data
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitMqConfig {
    public static final String  MAIN_DIRECT_EXCHANGE =  "main_direct_exchange";
    public static final String  DATA_ANALYSIS_QUEUE = "data_analysis_queue";
    public static final String  AI_CHAT_QUEUE = "ai_chat_queue";

    public static final String DATA_ANALYSIS_ROUTE_KEY = "data_analysis_route_key";
    public static final String AI_CHAT_ROUTE_KEY = "ai_chat_route_key";
    // public static final String DEAD_LETTER_EXCHANGE= "dead_letter_exchange";

     @Resource
     private RabbitTemplate rabbitTemplate;



    @Bean("mainExchange")
    public DirectExchange mainExchange(){
        return new DirectExchange(MAIN_DIRECT_EXCHANGE);
    }

    @Bean("dataAnalysisQueue")
    public Queue dataAnalysisQueue(){
        return new Queue(DATA_ANALYSIS_QUEUE);
    }

    @Bean("aiChatQueue")
    public Queue aiChatQueue(){
        return new Queue(AI_CHAT_QUEUE);
    }

    // 数据分析业务绑定交换机
    @Bean
    public Binding dataAnalysisBinding(@Qualifier("dataAnalysisQueue") Queue queue,
                                       @Qualifier("mainExchange") DirectExchange exchange){
            return BindingBuilder.bind(queue).to(exchange).with(DATA_ANALYSIS_ROUTE_KEY);
    }

    // AI对话业务绑定交换机
    @Bean
    public Binding aiChatBinding(@Qualifier("aiChatQueue") Queue queue,
                                   @Qualifier("mainExchange") DirectExchange exchange){
            return BindingBuilder.bind(queue).to(exchange).with(AI_CHAT_ROUTE_KEY);
    }




}
