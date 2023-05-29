package com.matheus.publisher;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Publisher {
    private final static String QUEUE_NAME = "NUMBERS";
    private final static int NUM_PUBLISHERS = 10;
    private final static int DURATION = 5; // in minutes

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        ExecutorService executorService = Executors.newFixedThreadPool(NUM_PUBLISHERS);
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        for (int i = 0; i < NUM_PUBLISHERS; i++) {
            executorService.execute(() -> {
                try (Connection connection = factory.newConnection();
                        Channel channel = connection.createChannel()) {
                    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                    int x = 0;
                    while (true) {
                        String message = "Hello World! " + x;
                        channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
                        System.out.println(" [x] Sent '" + message + "'");
                        x++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        scheduledExecutorService.schedule(() -> {
            executorService.shutdownNow();
            scheduledExecutorService.shutdown();
        }, DURATION, TimeUnit.MINUTES);
    }
}
