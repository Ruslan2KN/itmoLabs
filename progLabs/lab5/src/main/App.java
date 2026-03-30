package lab5.src.main;

import lab5.src.commands.*;
import lab5.src.exceptions.CommandNotFoundException;
import lab5.src.exceptions.EmptyCollectionException;
import lab5.src.managers.CollectionManager;
import lab5.src.managers.CommandManager;
import lab5.src.managers.ConsoleInput;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Основной класс приложения, управляющий жизненным циклом программы.
 * Отвечает за инициализацию менеджеров, регистрацию всех доступных команд
 * и запуск цикла обработки пользовательского ввода.
 */
public class App {

    /**
     * Запускает приложение и переводит его в интерактивный режим.
     * Метод инициализирует:
     * - Поток ввода данных (сканер)
     * - Менеджер коллекции для хранения объектов StudyGroup
     * - Менеджер команд для управления списком доступных действий
     * * После настройки регистрирует все команды и входит в бесконечный цикл,
     * считывая ввод пользователя до команды выхода или закрытия потока.
     */
    public void start(){
        try(Scanner scanner= new Scanner(System.in)){
            ConsoleInput consoleInput= new ConsoleInput(scanner);
            CollectionManager collectionManager= new CollectionManager();
            CommandManager commandManager= new CommandManager();

            commandManager.register(new Add(collectionManager, consoleInput));
            commandManager.register(new Save(collectionManager));
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
            commandManager.register(new RemoveLower(collectionManager,consoleInput));
            commandManager.register(new Show(collectionManager));
            commandManager.register(new UpdateId(collectionManager,consoleInput));

            System.out.println("Введите 'help' для получения списка доступных команд");

            while(true){
                System.out.print(">");

                if (!scanner.hasNextLine()){
                    System.out.println("Поток ввода закрыт.\n Завершение работы");
                    break;
                }

                String line = scanner.nextLine().trim();

                if (line.isEmpty()){
                    continue;
                }

                String[] tokens = line.split("\\s+");
                String commandName= tokens[0];
                String[] commandArgs= Arrays.copyOfRange(tokens,1,tokens.length);

                try{
                    commandManager.exeCommand(commandName, commandArgs);
                } catch (CommandNotFoundException | EmptyCollectionException e){
                    System.out.println(e.getMessage());
                } catch (IllegalArgumentException e){
                    System.out.println("Ошибка "+e.getMessage());
                } catch (RuntimeException e){
                    System.out.println("Ошибка выполнения "+ e.getMessage());
                }


            }
        } catch (NoSuchElementException e){
            System.out.println("Прерывание работы программы");
        }
    }
}
