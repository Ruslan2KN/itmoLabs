package src.server;

import src.server.commands.*;
import src.managers.CollectionManager;
import src.managers.CommandManager;
import src.managers.FileManager;
import src.models.StudyGroup;

import java.util.ArrayDeque;

/**
 * Класс, отвечающий за конфигурацию и запуск серверного приложения.
 * Данный класс выполняет роль фасадного инициализатора: создает необходимые менеджеры,
 * обеспечивает загрузку данных из внешних источников и регистрирует все
 * доступные серверные команды перед запуском сетевого модуля.
 */
public class ServerApp {

    /**
     * Статический метод, с которого начинается выполнение серверной части программы.
     * Вызывает метод старта в ServerApp, который подготавливает окружение и
     * запускает бесконечный цикл ожидания клиентских подключений.
     *
     * @param args Аргументы командной строки. Первый аргумент (опционально) используется для задания порта сервера.
     */
    public static void start(String[] args) {
        int port = 8080;

        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка, порт должен быть числом. Используется порт по умолчанию: " + port);
            }
        }

        System.out.println("Инициализация сервера на порту "+port);

        FileManager fileManager = new FileManager();
        ArrayDeque<StudyGroup> loadedCollection = fileManager.loadCollection();
        CollectionManager collectionManager = new CollectionManager(loadedCollection);
        CommandManager commandManager = new CommandManager();

        commandManager.register(new Add(collectionManager));
        commandManager.register(new Clear(collectionManager));
        commandManager.register(new CountByStudentsCount(collectionManager));
        commandManager.register(new Help(commandManager));
        commandManager.register(new Info(collectionManager));
        commandManager.register(new PrintDescending(collectionManager));
        commandManager.register(new PrintFieldDescendingGroupAdmin(collectionManager));
        commandManager.register(new RemoveById(collectionManager));
        commandManager.register(new RemoveFirst(collectionManager));
        commandManager.register(new RemoveGreater(collectionManager));
        commandManager.register(new RemoveLower(collectionManager));
        commandManager.register(new Show(collectionManager));
        commandManager.register(new UpdateId(collectionManager));
        commandManager.register(new CheckId(collectionManager));

        Server server = new Server(port, commandManager, fileManager, collectionManager);
        server.start();
    }
}