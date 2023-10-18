package com.yupi.springbootinit.RQTest;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;



public class DlxDirectConsumer {

    private  static final  String DLX_QUEUE = "dlx_queue";
    private static final String exchange_queue = "direct-exchange-queue";


    public static void main(String[] aaa) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        // 设置 rabbitmq 对应的信息
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(exchange_queue, "direct");


        // 指定死信队列参数
        Map<String, Object> args = new HashMap<>();
        // 指定死信队列绑定到哪个交换机
        args.put("x-dead-letter-exchange", DLX_QUEUE);
        args.put("x-dead-letter-routing-key", "boss");
        // 创建队列，分配一个队列名称：小紫
        String queueName = "xiaozi_queue";
        channel.queueDeclare(queueName, true, false, false, args);
        channel.queueBind(queueName, exchange_queue, "xiaozi");

        // 创建队列，分配一个队列名称：小黑

        // 指定死信队列参数
        Map<String, Object> args2 = new HashMap<>();
        // 指定死信队列绑定到哪个交换机
        args2.put("x-dead-letter-exchange", DLX_QUEUE);
        args2.put("x-dead-letter-routing-key", "od");  // 经过死信队列到哪个交换机去哪里

        String queueName2 = "xiaohei_queue";
        channel.queueDeclare(queueName2, true, false, false, args2);
        channel.queueBind(queueName2, exchange_queue, "xiaohei");
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");


// 小紫队列监听机制
        DeliverCallback xiaoziDeliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            // 拒绝消息 去死信队列
            channel.basicNack(delivery.getEnvelope().getDeliveryTag(),false,false);
            System.out.println(" [xiaozi] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message
                    + "'");
        };
// 小黑队列监听机制
        DeliverCallback xiaoheiDeliverCallback = (consumerTag, delivery) ->
        {
            String message = new String(delivery.getBody(), "UTF-8");
            // 拒绝消息 去死信队列
             channel.basicNack(delivery.getEnvelope().getDeliveryTag(),false, false);

            System.out.println(" [xiaohei] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message
                    + "'");
        };
        channel.basicConsume(queueName, true, xiaoziDeliverCallback,
                consumerTag -> {
                });
        channel.basicConsume(queueName2, true, xiaoheiDeliverCallback,
                consumerTag -> {
                });


    }
}
