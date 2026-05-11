package src.client;

import src.client.commands.*;
import src.exceptions.ConnectionException;
import src.managers.ConsoleInput;
import src.models.StudyGroup;
import src.network.Request;
import src.network.Response;

import java.util.*;

/**
 * Основной класс управления клиентским приложением.
 * Отвечает за цикл чтения команд из консоли, первичную обработку ввода,
 * маршрутизацию между локальными и серверными командами, а также
 * реализацию протокола взаимодействия с сервером.
 */
public class ClientApp {
    /**
     * Сетевой клиент для отправки запросов и получения ответов.
     */
    private final Client client;

    /**
     * Менеджер интерактивного ввода сложных объектов и данных.
     */
    private final ConsoleInput consoleInput;

    /**
     * Сканер для чтения строк из стандартного потока ввода.
     */
    private final Scanner defaultScanner;

    /**
     * Карта локальных команд, которые выполняются полностью на стороне клиента.
     */
    private final Map<String, ClientCommand> clientCommands = new HashMap<>();

    /**
     * Создает объект приложения и регистрирует доступные локальные команды.
     *
     * @param client         объект сетевого клиента.
     * @param consoleInput   менеджер для ввода объектов StudyGroup.
     * @param scanner        основной сканер ввода.
     */
    public ClientApp(Client client, ConsoleInput consoleInput, Scanner scanner) {
        this.client = client;
        this.consoleInput = consoleInput;
        this.defaultScanner = scanner;

        clientCommands.put(ClientCommands.exit.name(), new Exit());
        clientCommands.put(ClientCommands.execute_script.name(), new ExecuteScript(client, consoleInput));
    }

    /**
     * Запускает выполнение клиентского приложения.
     * Выводит приветственное сообщение и входит в основной цикл обработки.
     */
    public void start() {
        System.out.println("Клиентское приложение запущено");
        System.out.println("Введите 'help' для получения списка команд.");
        runLoop(defaultScanner, false);
    }

    /**
     * Основной цикл обработки входящего потока данных (консоли или файла).
     * Разбирает введенную строку на имя команды и аргументы, после чего
     * определяет способ обработки (локальный или серверный).
     *
     * @param scanner       сканер, из которого читаются строки.
     * @param isScriptMode  флаг, указывающий, выполняется ли сейчас скрипт.
     */
    private void runLoop(Scanner scanner, boolean isScriptMode) {
        while (true) {
            if (!isScriptMode) {
                System.out.print(">");
            }

            if (!scanner.hasNextLine()) break;

            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] tokens = line.split("\\s+");
            String commandName = tokens[0].toLowerCase();
            String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);

            if (clientCommands.containsKey(commandName)) {
                try {
                    clientCommands.get(commandName).execute(args);
                } catch (Exception e) {
                    System.out.println("Ошибка при выполнении клиентской команды: " + e.getMessage());
                }
                continue;
            }

            if (commandName.equals("help")) {
                if (args.length > 0) {
                    System.out.println("Ошибка, команда 'help' не принимает аргументов.");
                    continue;
                }
                System.out.println("Клиентские команды");
                for (ClientCommand cmd : clientCommands.values()) {
                    System.out.printf("%-38s : %s%n", cmd.getName(), cmd.getDescription());
                }
                System.out.println("\nСерверные команды");
            }

            if (commandName.equals("save")) {
                System.out.println("Ошибка, Команда 'save' доступна только в консоли сервера.");
                continue;
            }

            processServerCommand(commandName, args);
        }
    }

    /**
     * Реализует логику взаимодействия с сервером для выполнения команд.
     * Поддерживает двухшаговый протокол: если серверу для выполнения команды
     * требуется объект, клиент запрашивает его ввод у пользователя и
     * отправляет запрос повторно.
     *
     * @param commandName имя серверной команды.
     * @param args        массив аргументов команды.
     * @throws ConnectionException если возникли критические проблемы со связью.
     */
    private void processServerCommand(String commandName, String[] args) {
        if (commandName.equals("update_id")) {
            handleUpdateId(args);
            return;
        }
        try {
            Request request = new Request(commandName, args, null);
            Response response = client.sendAndReceive(request);

            if (response != null && response.isRequiresObject()) {
                System.out.println(response.getMessage());
                try {
                    StudyGroup attachedObject = consoleInput.askStudyGroup();
                    request = new Request(commandName, args, attachedObject);
                    response = client.sendAndReceive(request);
                } catch (Exception e) {
                    System.out.println("Ошибка ввода " + e.getMessage());
                    return;
                }
            }

            printResponse(response);
        } catch (ConnectionException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Обрабатывает специфическую логику команды обновления элемента.
     * Сначала проверяет существование ID на сервере, затем запрашивает
     * обновление полей и отправляет итоговый объект для сохранения.
     *
     * @param args массив аргументов, содержащий id.
     */
    private void handleUpdateId(String[] args) {
        if (args.length != 1) {
            System.out.println("Ошибка команда требует ровно один аргумент id");
            return;
        }
        try {
            Request checkReq = new Request("check_id", args, null);
            Response checkRes = client.sendAndReceive(checkReq);

            if (checkRes != null && checkRes.isSuccess() && checkRes.getCollection() != null) {
                StudyGroup groupToUpdate = checkRes.getCollection().iterator().next();

                consoleInput.updateStudyGroup(groupToUpdate);

                Request updateReq = new Request("update_id", args, groupToUpdate);
                Response updateRes = client.sendAndReceive(updateReq);
                printResponse(updateRes);
            } else {
                if (checkRes != null) System.out.println(checkRes.getMessage());
            }
        } catch (ConnectionException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Выводит содержимое ответа сервера в консоль.
     * Печатает текстовое сообщение и, если коллекция в ответе не пуста,
     * каждый её элемент построчно.
     *
     * @param response объект ответа, полученный от сервера.
     */
    private void printResponse(Response response) {
        if (response != null) {
            System.out.println(response.getMessage());
            if (response.getCollection() != null && !response.getCollection().isEmpty()) {
                response.getCollection().forEach(System.out::println);
            }
        }
    }
}