package com.matheus.subscriber;

import com.matheus.controllers.def.ops.IController;
import com.matheus.shared.Shared;
import com.matheus.util.SaveOutput;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Subscriber {

    private static final String QUEUE_NAME = "NUMBERS";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //IController controller = IController.createController(Shared.BASIC_ONOFF, 1.0, 100.0);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.basicQos(1000);

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
                GetResponse response = channel.basicGet(QUEUE_NAME, false);
                if (response != null) {
                    channel.basicAck(response.getEnvelope().getDeliveryTag(), false); 
                    count++;
                    String message = new String(response.getBody(), "UTF-8");
                    System.out.println("Received message: " + message);
                }
            } else {
                System.out.println("Queue is empty, stopping subscriber...");
                break;
            }

            long t2 = System.currentTimeMillis();
            if (t2 - t1 >= 1000) {
                double interval = (t2 - t1) / 1000;
                arrivalRate = count / interval;
                t1 = 0;
                count = 0;
                System.out.printf("Queue size: %d, Arrival rate: %.2f\n", queueSize, arrivalRate);
                // Compute new value for prefetch count using controller
                //int newPC = (int) controller.update(queueSize, arrivalRate);
                //System.out.printf("Updated prefetch count: %d\n\n", newPC);
                //channel.basicQos(newPC);
                // Save queue size, arrival rate and prefetch count to CSV file
                SaveOutput.saveToFile("prefetchcount_1000.csv", String.format("%d,%.2f\n", queueSize, arrivalRate));
            }
        }
    }
}
