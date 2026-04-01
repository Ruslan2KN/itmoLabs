package lab5.src.main;

import lab5.src.commands.*;
import lab5.src.exceptions.CommandNotFoundException;
import lab5.src.exceptions.EmptyCollectionException;
import lab5.src.managers.CollectionManager;
import lab5.src.managers.CommandManager;
import lab5.src.managers.ConsoleInput;
import lab5.src.managers.FileManager;
import lab5.src.models.StudyGroup;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Основной класс приложения, управляющий жизненным циклом программы.
 * Выступает в роли главного связующего звена архитектуры (оркестратора).
 * Отвечает за инициализацию всех менеджеров, загрузку начальных данных,
 * внедрение зависимостей в команды и запуск интерактивного цикла обработки ввода.
 */
public class App {

    /**
     * Запускает приложение и переводит его в интерактивный режим.
     * * Метод выполняет следующие шаги:
     * - Инициализирует поток ввода данных (сканер) и обработчик консольного ввода (ConsoleInput).
     * - Создает менеджер файлов (FileManager) и загружает сохраненную коллекцию с диска.
     * - Создает менеджер коллекции (CollectionManager), передавая ему загруженные данные.
     * - Настраивает менеджер команд (CommandManager) и регистрирует в нем все доступные команды,
     * распределяя между ними необходимые менеджеры.
     * * После успешной настройки программа входит в бесконечный цикл, считывая ввод пользователя
     * до вызова команды выхода (exit) или принудительного закрытия потока (например, Ctrl+D).
     * В цикле перехватываются и обрабатываются пользовательские и системные исключения,
     * предотвращая аварийное завершение программы.
     */
    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            ConsoleInput consoleInput = new ConsoleInput(scanner);
            CommandManager commandManager = new CommandManager();
            FileManager fileManager = new FileManager();
            ArrayDeque<StudyGroup> loadedCollection = fileManager.loadCollection();
            CollectionManager collectionManager = new CollectionManager(loadedCollection);


            commandManager.register(new Add(collectionManager, consoleInput));
            commandManager.register(new Save(collectionManager, fileManager));
            commandManager.register(new Clear(collectionManager));
            commandManager.register(new CountByStudentsCount(collectionManager));
            commandManager.register(new ExecuteScript(commandManager, consoleInput));
            commandManager.register(new Exit());
            commandManager.register(new Help(commandManager));
            commandManager.register(new Info(collectionManager));
            commandManager.register(new PrintDescending(collectionManager));
            commandManager.register(new PrintFieldDescendingGroupAdmin(collectionManager));
            commandManager.register(new RemoveById(collectionManager));
            commandManager.register(new RemoveFirst(collectionManager));
            commandManager.register(new RemoveGreater(collectionManager, consoleInput));
            commandManager.register(new RemoveLower(collectionManager, consoleInput));
            commandManager.register(new Show(collectionManager));
            commandManager.register(new UpdateId(collectionManager, consoleInput));

            System.out.println("Введите 'help' для получения списка доступных команд");

            while (true) {
                System.out.print(">");

                if (!scanner.hasNextLine()) {
                    System.out.println("Поток ввода закрыт.\n Завершение работы");
                    break;
                }

                String line = scanner.nextLine().trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] tokens = line.split("\\s+");
                String commandName = tokens[0];
                String[] commandArgs = Arrays.copyOfRange(tokens, 1, tokens.length);

                try {
                    commandManager.exeCommand(commandName, commandArgs);
                } catch (CommandNotFoundException | EmptyCollectionException e) {
                    System.out.println(e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.out.println("Ошибка " + e.getMessage());
                } catch (RuntimeException e) {
                    System.out.println("Ошибка выполнения " + e.getMessage());
                }


            }
        } catch (NoSuchElementException e) {
            System.out.println("Прерывание работы программы");
        }
    }
}
