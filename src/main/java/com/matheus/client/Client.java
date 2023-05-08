package com.matheus.client;

import com.matheus.controllers.def.ops.IController;
import com.matheus.shared.Shared;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Client {

    private static final String QUEUE_NAME = "TEST_QUEUE";
    private static final int QUEUE_SLEEP_TIME = 2 * 60 * 1000;
    private static final int QUEUE_WAIT_TIME = 1000;

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        IController controller = IController.createController(Shared.BASIC_ONOFF, 0.0, 10.0);
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // Set up the queue
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // Set up the message rate monitoring loop
        int count = 0;
        double arrivalRate = 0;
        long t1 = 0;
        while (true) {
            if (t1 == 0) {
                t1 = System.currentTimeMillis();
                count = 0;
            }

            int queueSize = (int) channel.messageCount(QUEUE_NAME);

            if (queueSize > 0) {
                // Receive a message and ack it
                GetResponse response = channel.basicGet(QUEUE_NAME, false);
                if (response != null) {
                    count++;
                    String message = new String(response.getBody(), "UTF-8");
                    System.out.println("Received message: " + message);
                    channel.basicAck(response.getEnvelope().getDeliveryTag(), false);
                }
            }

            // Stop timer and update arrival rate
            long t2 = System.currentTimeMillis();
            if (t2 - t1 >= 1000) {
                double interval = (t2 - t1) / 1000;
                arrivalRate = count / interval;
                t1 = 0;
                count = 0;

                // Log information
                System.out.printf("Queue size: %d, Arrival rate: %.2f\n", queueSize, arrivalRate);

                if (queueSize < arrivalRate) {
                    System.out.println("Queue size is less than arrival rate, sleeping for 2 minutes...");
                    Thread.sleep(QUEUE_SLEEP_TIME);
                }

                // Compute new value for prefetch count using controller
                int newPC = (int) controller.update(queueSize, arrivalRate);
                System.out.printf("Updated prefetch count: %d\n\n", newPC);

                // Set new prefetch count
                channel.basicQos(newPC);
            }

            // Wait for a bit before checking again
            Thread.sleep(QUEUE_WAIT_TIME);
        }
    }
}
