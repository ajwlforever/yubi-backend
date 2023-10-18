package com.yupi.springbootinit.RQTest;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class DlxDirectProducer {

    private  static final  String DLX_QUEUE = "dlx_queue";
    private static final String exchange_queue = "direct-exchange-queue";

    public static void main(String[] aaaags) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try(Connection connection = factory.newConnection()){
            Channel channel = connection.createChannel();

            // 私信交换机
            channel.exchangeDeclare(DLX_QUEUE, "direct");

            // 老板的死信队列
            String queueName = "boss_dlx_queue";
            channel.queueDeclare(queueName,true,false ,false, null);
            channel.queueBind(queueName,  DLX_QUEUE, "boss");

            // 外包的私信队列
            String queueName1 = "od_dlx_queue";
            channel.queueDeclare(queueName1,true,false ,false, null);
            channel.queueBind(queueName1,  DLX_QUEUE, "od");

            //老板监听机制
            DeliverCallback deliverCallback1 = (consumerTag, delivery)->{
                String msg= new String(delivery.getBody(), "UTF-8");
                channel.basicNack(delivery.getEnvelope().getDeliveryTag(),false,false);
                System.out.println("[boss] recieved "+delivery.getEnvelope().getRoutingKey()+":"+msg);};

            DeliverCallback deliverCallback2 = (consumerTag, delivery) ->{
                String msg= new String(delivery.getBody(), "UTF-8");
                channel.basicNack(delivery.getEnvelope().getDeliveryTag(),false,false);
                System.out.println("[od] recieved "+delivery.getEnvelope().getRoutingKey()+":"+msg);};

            // 开启监听
            channel.basicConsume(queueName, true, deliverCallback1, consumerTag->{});
            channel.basicConsume(queueName1,true,  deliverCallback2, consumerTag ->{});

            //  正常发送消息

            Scanner scanner = new Scanner(System.in);
            while(scanner.hasNext()) {
                String msg = scanner.nextLine();
                //屈辱分
                String[] s = msg.split(" ");
                System.out.println(s.length);
                if(s.length<1) continue;  /// 此次输入无效

                String content = s[0];
                String routeKey = s[1];
                // 发送消息
                channel.basicPublish(exchange_queue, routeKey, null, msg.getBytes(StandardCharsets.UTF_8) );
            }

      } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (TimeoutException ex) {
            throw new RuntimeException(ex);
        }
    }
}
