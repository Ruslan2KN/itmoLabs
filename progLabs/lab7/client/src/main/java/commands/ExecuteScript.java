package commands;

import client.*;
import utils.ConsoleInput;
import exceptions.ScriptRecursionException;
import models.StudyGroup;
import network.Request;
import network.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ExecuteScript extends ClientCommand {
    private final Client client;
    private final ConsoleInput consoleInput;
    private final Set<String> activeScripts = new HashSet<>();

    private final String username;
    private final String password;

    public ExecuteScript(Client client, ConsoleInput consoleInput, String username, String password) {
        super(ClientCommands.execute_script, "считать и исполнить скрипт из указанного файла");
        this.client = client;
        this.consoleInput = consoleInput;
        this.username = username;
        this.password = password;
    }

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

                        Request checkReq = new Request("check_id", commandArgs, null, username, password);
                        Response checkRes = client.sendAndReceive(checkReq);

                        if (checkRes != null && checkRes.isSuccess() && checkRes.getCollection() != null) {
                            StudyGroup attachedObject = checkRes.getCollection().iterator().next();

                            if (attachedObject.getOwner() != null && !attachedObject.getOwner().equals(username)) {
                                System.out.println("Ошибка скрипта: объект с id " + commandArgs[0] + " принадлежит другому пользователю.");
                                continue;
                            }

                            consoleInput.updateStudyGroup(attachedObject);

                            Request updateReq = new Request("update_id", commandArgs, attachedObject, username, password);
                            Response updateRes = client.sendAndReceive(updateReq);
                            if (updateRes != null) System.out.println(updateRes.getMessage());
                        } else {
                            if (checkRes != null) System.out.println(checkRes.getMessage());
                        }
                        continue;
                    }

                    Request request = new Request(commandName, commandArgs, null, username, password);
                    Response response = client.sendAndReceive(request);

                    if (response != null && response.isRequiresObject()) {
                        StudyGroup attachedObject = consoleInput.askStudyGroup();
                        request = new Request(commandName, commandArgs, attachedObject, username, password);
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