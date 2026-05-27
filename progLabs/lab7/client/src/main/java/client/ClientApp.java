package client;

import commands.ClientCommand;
import commands.ClientCommands;
import commands.ExecuteScript;
import commands.Exit;
import exceptions.ConnectionException;
import utils.ConsoleInput;
import models.StudyGroup;
import network.Request;
import network.Response;

import java.io.Console;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Основной класс управления клиентским приложением.
 */
public class ClientApp {
    private final Client client;
    private final ConsoleInput consoleInput;
    private final Scanner defaultScanner;
    private final Map<String, ClientCommand> clientCommands = new HashMap<>();

    private String username;
    private String password;

    public ClientApp(Client client, ConsoleInput consoleInput, Scanner scanner) {
        this.client = client;
        this.consoleInput = consoleInput;
        this.defaultScanner = scanner;

        clientCommands.put(ClientCommands.exit.name(), new Exit());
    }

    public void start() {
        System.out.println("Клиентское приложение запущено");

        authenticate();

        System.out.println("Введите 'help' для получения списка команд.");
        runLoop(defaultScanner, false);
    }

    /**
     * Запрашивает логин и пароль у пользователя.
     */
    private void authenticate() {
        while (true) {
            System.out.print("Введите логин (или 'регистрация' для нового пользователя, сервер сам создаст аккаунт): ");
            username = defaultScanner.nextLine().trim();
            if (username.isEmpty()) {
                System.out.println("Логин не может быть пустым.");
                continue;
            }


            Console console = System.console();
            if (console != null) {
                char[] passChars = console.readPassword("Введите пароль: ");
                password = new String(passChars);
            } else {
                System.out.print("Введите пароль: ");
                password = defaultScanner.nextLine().trim();
            }
            break;
        }

        clientCommands.put(ClientCommands.execute_script.name(), new ExecuteScript(client, consoleInput, username, password));
    }

    private void runLoop(Scanner scanner, boolean isScriptMode) {
        while (true) {
            if (!isScriptMode) System.out.print(">");
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

    private void processServerCommand(String commandName, String[] args) {
        if (commandName.equals("update_id")) {
            handleUpdateId(args);
            return;
        }
        try {
            Request request = new Request(commandName, args, null, username, password);
            Response response = client.sendAndReceive(request);

            if (response != null && response.isRequiresObject()) {
                System.out.println(response.getMessage());
                try {
                    StudyGroup attachedObject = consoleInput.askStudyGroup();
                    request = new Request(commandName, args, attachedObject, username, password);
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

    private void handleUpdateId(String[] args) {
        if (args.length != 1) {
            System.out.println("Ошибка команда требует ровно один аргумент id");
            return;
        }
        try {
            Request checkReq = new Request("check_id", args, null, username, password);
            Response checkRes = client.sendAndReceive(checkReq);

            if (checkRes != null && checkRes.isSuccess() && checkRes.getCollection() != null) {
                StudyGroup groupToUpdate = checkRes.getCollection().iterator().next();

                if (groupToUpdate.getOwner() != null && !groupToUpdate.getOwner().equals(username)) {
                    System.out.println("Ошибка: Вы не можете изменить этот объект, так как он принадлежит другому пользователю (" + groupToUpdate.getOwner() + ").");
                    return;
                }

                consoleInput.updateStudyGroup(groupToUpdate);

                Request updateReq = new Request("update_id", args, groupToUpdate, username, password);
                Response updateRes = client.sendAndReceive(updateReq);
                printResponse(updateRes);
            } else {
                if (checkRes != null) System.out.println(checkRes.getMessage());
            }
        } catch (ConnectionException e) {
            System.out.println(e.getMessage());
        }
    }

    private void printResponse(Response response) {
        if (response != null) {
            System.out.println(response.getMessage());
            if (response.getCollection() != null && !response.getCollection().isEmpty()) {
                response.getCollection().forEach(System.out::println);
            }
        }
    }
}