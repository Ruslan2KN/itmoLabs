
import client.*;
import utils.ConsoleInput;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
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
        String host = null;
        int port =0;

        Properties appProps = new Properties();
        System.out.println("Чтение настроек client.properties");
        try (FileInputStream fis = new FileInputStream("properties/client.properties")) {
            appProps.load(fis);
            host = appProps.getProperty("host");
            String portString = appProps.getProperty("port");
            port = Integer.parseInt(portString);
        } catch (IOException e){
            System.err.println("Файл properties/client.properties не найдет");
            System.exit(1);
        } catch (NumberFormatException e){
            System.err.println("Ошибка, порт в файле настроек не является числом");
            System.exit(1);
        }
        System.out.println("Попытка подключения к серверу по адресу " + host + ":" + port);

        Scanner scanner = new Scanner(System.in);
        ConsoleInput consoleInput = new ConsoleInput(scanner);
        Client client = new Client(host, port);

        ClientApp app = new ClientApp(client, consoleInput, scanner);
        app.start();
    }

}