package com.matheus.subscriber;

import com.matheus.controllers.def.ops.IController;
import com.matheus.shared.Shared;
import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class Subscriber {

    private static final String QUEUE_NAME = "NUMBERS";

    private static ConnectionFactory createConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setHost("localhost");
        return factory;
    }

    private static Channel createChannel(Connection connection) throws IOException {
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        return channel;
    }

    private static DefaultConsumer createConsumer(Channel channel, AtomicInteger count) {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                    byte[] body) throws IOException {
                channel.basicAck(envelope.getDeliveryTag(), false);
                // System.out.println(" [x] Received '" + new String(body, "UTF-8") + "'");
                count.incrementAndGet();
            }
        };
    }

    public static void main(String[] args) {
        int prefetchCount = 0;
        AtomicInteger messageCount = new AtomicInteger(0);
        double arrivalRate = 0;
        long startTime = 0;

        IController controller = IController.createController(Shared.BASIC_ONOFF, 10000, 1, 5);
        System.out.println("Test for " + Shared.BASIC_ONOFF + " controller");

        try {
            Connection connection = createConnectionFactory().newConnection();
            try {
                Channel channel = createChannel(connection);
                try {
                    channel.basicQos(0, prefetchCount, true);
                    DefaultConsumer consumer = createConsumer(channel, messageCount);
                    String consumerTag = channel.basicConsume(QUEUE_NAME, false, consumer);

                    while (true) {
                        if (startTime == 0) {
                            startTime = System.currentTimeMillis();
                            messageCount.set(0);
                        }

                        long currentTime = System.currentTimeMillis();
                        if (currentTime - startTime >= 5000) {
                            channel.basicCancel(consumerTag);
                            double interval = (currentTime - startTime) / 1000.0;
                            arrivalRate = messageCount.get() / interval;
                            System.out.printf("%d, %.2f, 10000\n", prefetchCount, arrivalRate);
                            startTime = 0;
                            messageCount.set(0);
                            prefetchCount = (int) controller.update(arrivalRate);
                            channel.basicQos(prefetchCount, true);
                            consumerTag = channel.basicConsume(QUEUE_NAME, false, consumer);
                        }
                    }
                } finally {
                    channel.close();
                }
            } finally {
                connection.close();
            }
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
