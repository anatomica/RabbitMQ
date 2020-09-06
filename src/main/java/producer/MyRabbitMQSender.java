package producer;

import java.util.Scanner;

public class MyRabbitMQSender {

    private final static String FIRST_CH_NAME = "firstCH";
    private final static String SECOND_CH_NAME = "secondCH";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите название канала: \"firstCH\" или \"secondCH\"");
        System.out.println("Для смены канала введите: \"/change 'и имя канала'\"");
        String chanelName = scanner.nextLine();
        while (true){
            String msg = scanner.nextLine();
            if (msg.equals("exit") || msg.equals("quit")) break;
            if (msg.startsWith("/change ")){
                chanelName = msg.split(" ", 2)[1];
                //change chanel
            }
            //sending messages
        }
        scanner.close();
    }

}
