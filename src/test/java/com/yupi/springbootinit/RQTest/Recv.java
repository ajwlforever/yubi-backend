package com.yupi.springbootinit.RQTest;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Recv {
    private static  String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();){

            Channel channel = connection.createChannel();

            DeliverCallback deliverCallback= (consumerTag, delivery) ->{
              String msg = new String(delivery.getBody(), "UTF-8");
                System.out.println("[x]recieve: "+ msg);

            };

            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag ->{});
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }


    }
}
