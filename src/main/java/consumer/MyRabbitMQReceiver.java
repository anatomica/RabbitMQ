package consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class MyRabbitMQReceiver {

    private final static String PRACTICE = "practice.java";
    private final static String THEORY = "theory.java";

    public static void main(String[] args) throws IOException, TimeoutException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите название желаемого канала: \"practice.java\" или \"theory.java\"");
        System.out.println("Для смены канала введите: \"/change 'и имя канала'\"");
        String chanelName = scanner.nextLine();

        while (true){
            String msg = scanner.nextLine();
            if (msg.equals("exit") || msg.equals("quit")) break;
            if (msg.startsWith("/change ")){
                chanelName = msg.split(" ", 2)[1]; // change chanel
            }

            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(chanelName, BuiltinExchangeType.TOPIC);
            // String queueName = channel.queueDeclare().getQueue();
            String queueName = chanelName;

            String routingKey = "";
            if (chanelName.equals("practice.java")) routingKey = "[practice.java]";
            if (chanelName.equals("theory.java")) routingKey = "[theory.java]";

            channel.queueBind(queueName, chanelName, routingKey);
            System.out.println(" [*] Waiting for messages with routing key (" + routingKey + "):");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });
        }
        scanner.close();
    }
}
