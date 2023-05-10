package com.matheus.subscriber;

import com.matheus.controllers.def.ops.IController;
import com.matheus.shared.Shared;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Subscriber {

    private static final String QUEUE_NAME = "NUMBERS_STREAM";
    private static final int QUEUE_SLEEP_TIME = 2 * 60 * 1000;
    //private static final int QUEUE_WAIT_TIME = 1000;

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        IController controller = IController.createController(Shared.BASIC_ONOFF, 1.0, 100.0);
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
                GetResponse response = channel.basicGet(QUEUE_NAME, false); // #TO-DO: colocar aqui em espera e dar o ack logo assim que receber
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
                // Print queue size and arrival rate
                System.out.printf("Queue size: %d, Arrival rate: %.2f\n", queueSize, arrivalRate);
                // #TO-DO: fazer com que a fila esteja sempre cheia de mensagens. Pode ser usando mais de um publisher (100).
                // if (queueSize < arrivalRate) {
                //     System.out.println("Queue size is less than arrival rate, sleeping for 2 minutes...");
                //     Thread.sleep(QUEUE_SLEEP_TIME);
                // }
                // Compute new value for prefetch count using controller
                int newPC = (int) controller.update(queueSize, arrivalRate);
                System.out.printf("Updated prefetch count: %d\n\n", newPC);
                // Set new prefetch count
                // #TO-DO: Analisar o desempenho do sistema com o PC 1 e 10. Verificar se o PC 1 não é muito baixo e o PC 10 não é muito alto.
                channel.basicQos(newPC);
            }
        }
    }
}
