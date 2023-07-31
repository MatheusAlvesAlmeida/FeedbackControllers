package com.matheus.rabbitmq.subscriber;

import com.matheus.controllers.def.ops.IController;
import com.matheus.shared.Shared;
import com.matheus.util.SaveOutput;
import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class Subscriber {

    private static final String QUEUE_NAME = "NUMBERS";

    private static ConnectionFactory createConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
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
                count.incrementAndGet();
            }
        };
    }

    public static void main(String[] args) {
        int prefetchCount = 1, setPointIndex = 0, sample = 0;;
        int[] desiredArrivalRate = {17000, 10000, 20000, 5000, 8000};
        AtomicInteger messageCount = new AtomicInteger(0);
        double arrivalRate = 0;
        long startTime = 0;

        // RootLocus: {'kp': -8.157596087708224e-05, 'ki': 0.0008493198021217253, 'kd': 0.00019751623305156406}
        // Ziegler: {'kp': 0.0002485638532920902, 'ki': 0.0001242819266460451, 'kd': 0.0001242819266460451}
        // AMIGO: {'kp': 5.074845338046842e-05, 'ki': 0.00010678320398806896, 'kd': 6.343556672558552e-06}
        // Cohen: {'kp': 7.82976137870084e-05, 'ki': 7.412174105170129e-05, 'kd': 9.989695552135559e-06}

        double kp = 7.82976137870084e-05, ki = 7.412174105170129e-05, kd = 9.989695552135559e-06;

        //IController basicPID = IController.createController(Shared.BASIC_PID, desiredArrivalRate[0], 1, 1, 10, prefetchCount, kp, ki, kd);
        //IController deadzonePID = IController.createController(Shared.DEADZONE_PID, desiredArrivalRate[0], 1, 1, 10, prefetchCount, kp, ki, kd, 1000);
        //IController errorsquarePID = IController.createController(Shared.ERROR_SQUARE_PID, desiredArrivalRate[0], 1, 1, 10, prefetchCount, kp, ki, kd);
        IController incrementalPID = IController.createController(Shared.INCREMENTAL_PID, desiredArrivalRate[0], 1, 1, 10, prefetchCount, kp, ki, kd);

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
                            // Stop the consumer
                            channel.basicCancel(consumerTag);
                            // Calculate the arrival rate
                            double interval = (currentTime - startTime) / 1000.0;
                            arrivalRate = messageCount.get() / interval;
                            // Print the results and save them to a file
                            System.out.printf("%d, %.2f, %d\n", prefetchCount, arrivalRate, desiredArrivalRate[setPointIndex]);
                            SaveOutput.saveBasicOnOffResult(prefetchCount, arrivalRate, desiredArrivalRate[setPointIndex]);
                            // Update the prefetch count
                            sample += 1;
                            messageCount.set(0);
                            prefetchCount = (int) incrementalPID.update(arrivalRate);
                            channel.basicQos(prefetchCount, true);
                            // Change the set point every 60 samples
                            if (sample == 60) {
                                if (setPointIndex == desiredArrivalRate.length - 1) {
                                    System.out.println("Finished the simulation!");
                                    break;
                                }
                                setPointIndex += 1;
                                sample = 0;
                                System.out.println("Changing set point to " + desiredArrivalRate[setPointIndex]);
                                incrementalPID.updateSetPoint(desiredArrivalRate[setPointIndex]);
                            }
                            // Restart the consumer
                            consumerTag = channel.basicConsume(QUEUE_NAME, false, consumer);
                            startTime = System.currentTimeMillis();
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
