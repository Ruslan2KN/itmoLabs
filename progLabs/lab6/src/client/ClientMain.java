package src.client;

import src.managers.ConsoleInput;

import java.util.Scanner;

/**
 * Главный класс клиентского приложения.
 * Данный класс служит точкой входа в программу. Он отвечает за инициализацию
 * основных компонентов: сканера для чтения ввода, менеджера консольного ввода
 * и сетевого клиента, после чего передает управление основному приложению.
 */
public class ClientMain {

    /**
     * Статический метод, с которого начинается выполнение программы.
     * Настраивает параметры подключения к серверу (хост и порт), создает
     * необходимые зависимости и запускает жизненный цикл клиентского приложения.
     *
     * @param args Аргументы командной строки (в данной реализации не используются).
     */
    public static void main(String[] args) {
        String host = "localhost";
        int port = 8080;

        if (args.length == 2) {
            host = args[0];
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка, порт должен быть числом. Используется порт по умолчанию: " + port);
            }
        } else if (args.length == 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка, порт должен быть числом. Используется порт по умолчанию: " + port);
            }
        }
        System.out.println("Попытка подключения к серверу по адресу " + host + ":" + port);

        Scanner scanner = new Scanner(System.in);
        ConsoleInput consoleInput = new ConsoleInput(scanner);
        Client client = new Client(host, port);

        ClientApp app = new ClientApp(client, consoleInput, scanner);
        app.start();
    }

}