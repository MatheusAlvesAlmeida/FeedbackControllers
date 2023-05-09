package com.matheus.server;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Server {
    private final static String QUEUE_NAME = "NUMBERS_STREAM";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            int x = 0;
            while (true) {
                String message = "Hello World! " + x;
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println(" [x] Sent '" + message + "'");
                x++;

                int interval = new Random().nextInt(1000) + 100;
                Thread.sleep(interval);
            }
        }
    }
}
