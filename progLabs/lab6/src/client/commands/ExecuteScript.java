package src.client.commands;

import src.client.Client;
import src.exceptions.ScriptRecursionException;
import src.managers.ConsoleInput;
import src.models.StudyGroup;
import src.network.Request;
import src.network.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Команда для чтения и выполнения скриптов из указанного файла.
 * Скрипт представляет собой текстовый файл, где каждая строка содержит команду и ее аргументы.
 * Класс поддерживает обработку вложенных скриптов и содержит встроенную защиту от бесконечной рекурсии.
 * При выполнении скрипта временно перехватывает поток ввода, переключая его на чтение из файла.
 */
public class ExecuteScript extends ClientCommand {

    /**
     * Сетевой клиент для отправки команд на сервер.
     */
    private final Client client;

    /**
     * Менеджер ввода, состояние которого изменяется на время выполнения скрипта.
     */
    private final ConsoleInput consoleInput;

    /**
     * Множество путей к файлам скриптов, которые в данный момент выполняются.
     * Используется для отслеживания и предотвращения рекурсивного вызова скриптов.
     */
    private final Set<String> activeScripts = new HashSet<>();

    /**
     * Инициализирует команду выполнения скрипта.
     *
     * @param client сетевой клиент приложения.
     * @param consoleInput менеджер консольного ввода для перенаправления потока чтения.
     */
    public ExecuteScript(Client client, ConsoleInput consoleInput) {
        super(ClientCommands.execute_script, "считать и исполнить скрипт из указанного файла");
        this.client = client;
        this.consoleInput = consoleInput;
    }

    /**
     * Выполняет чтение и построчное исполнение команд из файла.
     * Метод подменяет сканер в менеджере ввода на сканер файла, включает режим скрипта,
     * а после завершения (или при ошибке) гарантированно восстанавливает исходное состояние.
     * Специально обрабатывает команды execute_script (для вложенности), exit (для прерывания)
     * и update_id (требующую предварительной проверки id).
     *
     * @param args массив аргументов, где первым и единственным элементом должен быть путь к файлу скрипта.
     * @throws ScriptRecursionException если обнаружена попытка рекурсивного вызова уже выполняющегося скрипта.
     */
    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            System.out.println("Ошибка, укажите путь к файлу.");
            return;
        }

        String filePath = args[0];
        if (activeScripts.contains(filePath)) {
            throw new ScriptRecursionException("Ошибка, обнаружена рекурсия в " + filePath);
        }

        File file = new File(filePath);

        Scanner oldScanner = consoleInput.getScanner();
        Boolean oldScriptMode = consoleInput.isScriptMode();

        try (Scanner scriptScanner = new Scanner(file)) {
            activeScripts.add(filePath);
            consoleInput.setScanner(scriptScanner);
            consoleInput.setScriptMode(true);
            System.out.println("Начало выполнения скрипта " + filePath);

            while (scriptScanner.hasNextLine()) {
                try {
                    String line = scriptScanner.nextLine().trim();
                    if (line.isEmpty()) continue;

                    String[] tokens = line.split("\\s+");
                    String commandName = tokens[0].toLowerCase();
                    String[] commandArgs = Arrays.copyOfRange(tokens, 1, tokens.length);

                    if (commandName.equals("execute_script")) {
                        if (commandArgs.length > 0) execute(commandArgs);
                        continue;
                    }
                    if (commandName.equals("exit")) {
                        System.out.println("Команда exit встречена в скрипте. Выполнение скрипта прервано.");
                        break;
                    }

                    if (commandName.equals("update_id")) {
                        if (commandArgs.length != 1) {
                            System.out.println("Ошибка скрипта, команда update_id требует ровно один аргумент id");
                            continue;
                        }
                        Request checkReq = new Request("check_id", commandArgs, null);
                        Response checkRes = client.sendAndReceive(checkReq);

                        if (checkRes != null && checkRes.isSuccess() && checkRes.getCollection() != null) {
                            StudyGroup attachedObject = checkRes.getCollection().iterator().next();
                            consoleInput.updateStudyGroup(attachedObject);

                            Request updateReq = new Request("update_id", commandArgs, attachedObject);
                            Response updateRes = client.sendAndReceive(updateReq);
                            if (updateRes != null) System.out.println(updateRes.getMessage());
                        } else {
                            if (checkRes != null) System.out.println(checkRes.getMessage());
                        }
                        continue;
                    }

                    Request request = new Request(commandName, commandArgs, null);
                    Response response = client.sendAndReceive(request);

                    if (response != null && response.isRequiresObject()) {
                        StudyGroup attachedObject = consoleInput.askStudyGroup();
                        request = new Request(commandName, commandArgs, attachedObject);
                        response = client.sendAndReceive(request);
                    }

                    if (response != null) {
                        System.out.println(response.getMessage());
                        if (response.getCollection() != null && !response.getCollection().isEmpty()) {
                            response.getCollection().forEach(System.out::println);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Ошибка в строке скрипта: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден: " + filePath);
        } finally {
            activeScripts.remove(filePath);

            consoleInput.setScanner(oldScanner);
            consoleInput.setScriptMode(oldScriptMode);

            System.out.println("Выполнение скрипта " + filePath + " завершено.");
        }
    }
}