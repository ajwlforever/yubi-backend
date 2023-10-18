package com.yupi.springbootinit.RQTest;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class FaoutSender {
    private static final String exchange_name = "queue1";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try(Connection connection = factory.newConnection()){
            Channel channel = connection.createChannel();
            channel.queueDeclare(exchange_name,false,false,false,null);
            channel.exchangeDeclare(exchange_name,"fanout");
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()){
                String msg = scanner.nextLine();
                //s1 是队列名称 不声明， 即转发给所有队列
                channel.basicPublish(exchange_name, "", null, msg.getBytes());

                System.out.println("[x]send:  " + msg);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
