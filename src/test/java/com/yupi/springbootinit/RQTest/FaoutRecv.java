package com.yupi.springbootinit.RQTest;
// Fanout交换机将消息广播到其绑定的所有队列。当消息被发送
// 到Fanout交换机时，它会将消息复制到所有绑定的队列上，而不考虑路由键的值。因此，无论消息
// 的路由键是什么，都会被广播到所有队列。Fanout交换机主要用于广播消息给所有的消费者。


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.checkerframework.checker.units.qual.C;

import java.io.IOException;

import java.util.concurrent.TimeoutException;

public class FaoutRecv {

    private static final String exchange_name = "queue1";


    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        // 设置参数
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        // 创建连接
        Channel channel1 =  connection.createChannel();
        Channel channel2 = connection.createChannel();

        //声明交换机
        channel1.exchangeDeclare(exchange_name,"fanout");
        channel2.exchangeDeclare(exchange_name, "fanout");

        // 两个队列的消费者

        String queue1 = "consumer1";
        channel1.queueDeclare(queue1,true,false,false,null);
        channel1.queueBind(queue1,exchange_name,"");

        String queue2 = "consumer2";
        channel2.queueDeclare(queue2,true,false,false, null);
        channel2.queueBind(queue2,exchange_name,"");

        System.out.println("[*]========================================");

        DeliverCallback deliverCallback1 = (consumerTag, delivery) ->{
            String msg = new String(delivery.getBody(), "UTF-8");
            System.out.println("[consumer1]recieve: "+ msg);

        };
        DeliverCallback deliverCallback2 = (consumerTag, delivery)->{
            String msg = new String(delivery.getBody(), "UTF-8");
            System.out.println("[consumer2]recieve: "+ msg);
        };


        // 监听消息
        channel1.basicConsume(queue1, true, deliverCallback1, consumerTag ->{});
        channel2.basicConsume(queue2, true, deliverCallback2,  consumerTag ->{});

    }
}
