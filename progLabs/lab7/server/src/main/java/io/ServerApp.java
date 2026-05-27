package io;

import dao.DatabaseConnection;
import dao.StudyGroupDAO;
import dao.UserDAO;
import managers.CollectionManager;
import managers.CommandManager;
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
     */
    public static void start() {
        int port = 8080;
        Properties appProps = new Properties();

        String dbHost = "pg";
        int dbPort = 5432;
        String dbName = "studs";
        String dbUser = "";
        String dbPass = "";

        System.out.println("Чтение настроек server.properties");
        try (FileInputStream fis = new FileInputStream("properties/server.properties")) {
            appProps.load(fis);

            String portString = appProps.getProperty("server.port", "8080");
            port = Integer.parseInt(portString);

            dbHost = appProps.getProperty("db.host", "pg");
            dbPort = Integer.parseInt(appProps.getProperty("db.port", "5432"));
            dbName = appProps.getProperty("db.name", "studs");
            dbUser = appProps.getProperty("db.user", "");
            dbPass = appProps.getProperty("db.pass", "");

        } catch (IOException e) {
            System.out.println("Файл properties/server.properties не найден. Будут использованы дефолтные параметры.");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка парсинга числовых значений в файле настроек. Используются дефолтные параметры.");
        }

        System.out.println("Инициализация сервера на порту " + port);

        DatabaseConnection dbConnection = new DatabaseConnection(dbHost, dbPort, dbName, dbUser, dbPass);
        UserDAO userDAO = new UserDAO(dbConnection);
        StudyGroupDAO studyGroupDAO = new StudyGroupDAO(dbConnection, userDAO);

        ArrayDeque<StudyGroup> loadedCollection = studyGroupDAO.loadCollection();
        CollectionManager collectionManager = new CollectionManager(loadedCollection);
        CommandManager commandManager = new CommandManager();

        commandManager.register(new CountByStudentsCount(collectionManager));
        commandManager.register(new Help(commandManager));
        commandManager.register(new Info(collectionManager));
        commandManager.register(new PrintDescending(collectionManager));
        commandManager.register(new PrintFieldDescendingGroupAdmin(collectionManager));
        commandManager.register(new Show(collectionManager));
        commandManager.register(new CheckId(collectionManager));

        commandManager.register(new Add(collectionManager, studyGroupDAO));
        commandManager.register(new Clear(collectionManager, studyGroupDAO));
        commandManager.register(new RemoveById(collectionManager, studyGroupDAO));
        commandManager.register(new UpdateId(collectionManager, studyGroupDAO));
        commandManager.register(new RemoveFirst(collectionManager, studyGroupDAO));
        commandManager.register(new RemoveGreater(collectionManager, studyGroupDAO));
        commandManager.register(new RemoveLower(collectionManager, studyGroupDAO));

        Server server = new Server(port, commandManager, userDAO, dbConnection);
        server.start();
    }
}