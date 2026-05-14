package io;

import managers.CollectionManager;
import managers.CommandManager;
import managers.FileManager;
import models.StudyGroup;
import commands.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Properties;

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
     */
    public static void start() {
        int port = 8080;
        Properties appProps = new Properties();
        System.out.println("Чтение настроек server.properties");
        try (FileInputStream fis =new FileInputStream("properties/server.properties") ) {
            appProps.load(fis);
        String portString = appProps.getProperty("port", "8080");
        port = Integer.parseInt(portString);
        } catch (IOException e){
            System.out.println("Файл properties/server.properties не найдет. Будут использован порт по умолчанию "+ port);
        } catch (NumberFormatException e){
            System.out.println("Ошибка, порт в файле настроек не является числом. Используется порт по умолчанию "+port);
        }

        System.out.println("Инициализация сервера на порту " + port);

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