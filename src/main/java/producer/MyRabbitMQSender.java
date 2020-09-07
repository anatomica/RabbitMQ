package producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class MyRabbitMQSender {

    private final static String PRACTICE = "practice.java";
    private final static String THEORY = "theory.java";

    public static void main(String[] args) throws IOException, TimeoutException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите название публикуемого канала: \"practice.java\" или \"theory.java\"");
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
            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {
                channel.exchangeDeclare(chanelName, BuiltinExchangeType.TOPIC);
                channel.queueDeclare(chanelName, false, false, false, null);

                String routingKey = "";
                if (chanelName.equals("practice.java")) routingKey = "[practice.java]";
                if (chanelName.equals("theory.java")) routingKey = "[theory.java]";

                StringBuilder message = new StringBuilder("message");
                for (int i = 0; i < 10; i++) {
                    message.append(" ").append(i);
                    channel.basicPublish(chanelName, routingKey, null, message.toString().getBytes("UTF-8"));
                    System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");
                    message.delete(0, message.length()).append("message");
                }

                System.out.println("Введите название публикуемого канала: \"practice.java\" или \"theory.java\"");
                System.out.println("Для смены канала введите: \"/change 'и имя канала'\"");
            }
        }
        scanner.close();
    }
}
