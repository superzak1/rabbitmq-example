package com.zivs.rqexample.workfair;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.zivs.rqexample.utils.MyConnectionFactory;

public class Send {
    // 队列名称
    private final static String QUEUE_NAME = "fair_queue";

    public static void main(String[] argv) throws Exception {
        // 获取连接
        Connection connection = MyConnectionFactory.getConnection();
        Channel channel = connection.createChannel();

        /**
         * prefetchCount:会告诉RabbitMQ不要同时给一个消费者推送多于N个消息，即一旦有N个消息还没有ack，则该consumer将block掉，直到有消息ack
         * global:true\false 是否将上面设置应用于channel，简单点说，就是上面限制是channel级别的还是consumer级别
         */
        channel.basicQos(1);
        // 队列声明
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        for (int i = 0; i < 50; i++) {
            // message
            String message = "User " + i + " Say Hello!";
            // 发送message
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

            System.out.println(" [" + i + "] Sent msg: '" + message + "'");
        }

        channel.close();
        connection.close();
    }

}