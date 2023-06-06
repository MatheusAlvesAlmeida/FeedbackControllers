package com.matheus.subscriber;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class Subscriber {

    private static final String QUEUE_NAME = "NUMBERS";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        int prefetchCount = 0;

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.basicQos(0, prefetchCount, true);

        AtomicInteger count = new AtomicInteger(0);
        double arrivalRate = 0;
        long t1 = 0;

        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                    byte[] body) throws IOException {
                channel.basicAck(envelope.getDeliveryTag(), false);
                //System.out.println(" [x] Received '" + new String(body, "UTF-8") + "'");
                count.incrementAndGet();
            }
        };

        String consumerTag = channel.basicConsume(QUEUE_NAME, false, consumer);

        while (true) {
            if (t1 == 0) {
                t1 = System.currentTimeMillis();
                count.set(0);
            }

            long t2 = System.currentTimeMillis();
            if (t2 - t1 >= 5000) {
                channel.basicCancel(consumerTag);
                double interval = (t2 - t1) / 1000.0;
                arrivalRate = count.get() / interval;
                System.out.printf("%d, %.2f\n", prefetchCount, arrivalRate);
                t1 = 0;
                count.set(0);
                prefetchCount += 10;
                channel.basicQos(prefetchCount, true);
                consumerTag = channel.basicConsume(QUEUE_NAME, false, consumer);
            }
        }
    }
}
