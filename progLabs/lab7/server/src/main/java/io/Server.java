package io;

import dao.DatabaseConnection;
import dao.UserDAO;
import managers.CommandManager;
import network.Request;
import network.Response;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Многопоточный сервер, обрабатывающий запросы клиентов.
 * Реализует конвейерную архитектуру с использованием пулов потоков.
 */
public class Server {
    private final int port;
    private final CommandManager commandManager;
    private final UserDAO userDAO; // Только для аутентификации
    private final DatabaseConnection dbConnection;
    private boolean isRunning;


    private final ExecutorService readPool = Executors.newCachedThreadPool();
    private final ExecutorService processPool = Executors.newFixedThreadPool(10); // Максимум пулов
    private final ExecutorService sendPool = Executors.newCachedThreadPool();

    public Server(int port, CommandManager commandManager, UserDAO userDAO, DatabaseConnection dbConnection) {
        this.port = port;
        this.commandManager = commandManager;
        this.userDAO = userDAO;
        this.dbConnection = dbConnection;
    }

    public void start() {
        isRunning = true;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setSoTimeout(50);
            System.out.println("Сервер успешно запущен на порту " + port);
            System.out.println("Для выключения сервера введите команду 'exit'");

            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            while (isRunning) {
                checkServerConsole(consoleReader);

                try {
                    Socket clientSocket = serverSocket.accept();
                    // Главный поток не блокируется, сразу отдаем чтение в Cached pool
                    readPool.submit(() -> handleRead(clientSocket));
                } catch (SocketTimeoutException e) {
                    // Тайм-аут вышел, новых подключений нет, переходим к следующей итерации, проверяя exit раз в 50мс
                } catch (IOException e) {
                    System.out.println("Ошибка при ожидании подключения: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Не удалось запустить сервер: " + e.getMessage());
        } finally {
            shutdownServer();
        }
    }

    /**
     * Чтение запроса (Cached Thread Pool)
     */
    private void handleRead(Socket clientSocket) {
        try {
            ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
            Request request = (Request) ois.readObject();

            // Если чтение прошло успешно, передаем задачу на обработку в Fixed pool
            processPool.submit(() -> handleProcess(clientSocket, request));

        } catch (EOFException e) {
            closeSocket(clientSocket);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка при чтении запроса от клиента: " + e.getMessage());
            closeSocket(clientSocket);
        }
    }

    /**
     * Обработка запроса (Fixed Thread Pool)
     */
    private void handleProcess(Socket clientSocket, Request request) {
        Response response;
        try {
            boolean isAuthenticated = userDAO.authenticateUser(request.getUsername(), request.getPassword());

            if (!isAuthenticated) {
                response = new Response("Ошибка авторизации: неверный пароль.", false, null);
            } else {
                response = commandManager.exeCommand(request);
            }
        } catch (Exception e) {
            response = new Response("Внутренняя ошибка сервера при обработке команды: " + e.getMessage(), false, null);
        }
        // Ответ передаем в Cached pool
        final Response finalResponse = response;
        sendPool.submit(() -> handleSend(clientSocket, finalResponse));
    }

    /**
     * Отправка ответа (Cached Thread Pool)
     */
    private void handleSend(Socket clientSocket, Response response) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
            oos.writeObject(response);
            oos.flush();
        } catch (IOException e) {
            System.out.println("Ошибка при отправке ответа клиенту: " + e.getMessage());
        } finally {
            closeSocket(clientSocket);
        }
    }

    /**
     * Закрытие сокета без выброса исключений.
     */
    private void closeSocket(Socket socket) {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Ошибка при закрытии сокета: " + e.getMessage());
        }
    }

    /**
     * Чтение команд администратора сервера.
     */
    private void checkServerConsole(BufferedReader consoleReader) {
        try {
            if (consoleReader.ready()) {
                String line = consoleReader.readLine().trim();
                if (line.equalsIgnoreCase("exit")) {
                    System.out.println("Завершение работы сервера");
                    isRunning = false;
                } else {
                    System.out.println("Неизвестная команда. Доступна только 'exit'");
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения консоли сервера: " + e.getMessage());
        }
    }

    /**
     * Корректное завершение работы пулов и базы данных.
     */
    private void shutdownServer() {
        System.out.println("Остановка пулов потоков");
        readPool.shutdown();
        processPool.shutdown();
        sendPool.shutdown();
        try {
            if (!readPool.awaitTermination(2, TimeUnit.SECONDS)) readPool.shutdownNow();
            if (!processPool.awaitTermination(2, TimeUnit.SECONDS)) processPool.shutdownNow();
            if (!sendPool.awaitTermination(2, TimeUnit.SECONDS)) sendPool.shutdownNow();
        } catch (InterruptedException e) {
            readPool.shutdownNow();
            processPool.shutdownNow();
            sendPool.shutdownNow();
        }
        if (dbConnection !=null){
            dbConnection.close();
        }
        System.out.println("Работа сервера успешно завершена.");
    }
}