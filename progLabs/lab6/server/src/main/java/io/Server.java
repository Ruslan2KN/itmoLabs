package io;

import exceptions.FileSaveException;
import managers.CollectionManager;
import managers.CommandManager;
import managers.FileManager;
import network.Request;
import network.Response;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Основной класс серверного приложения, отвечающий за сетевое взаимодействие.
 * Сервер работает в однопоточном режиме и использует блокирующий ввод-вывод
 * с установленным тайм-аутом для одновременной проверки команд в консоли сервера.
 * Обеспечивает прием подключений, чтение запросов, их обработку через менеджер
 * команд и отправку ответов клиентам.
 */
public class Server {
    /**
     * Порт, на котором будет запущен сервер.
     */
    private final int port;

    /**
     * Менеджер для поиска и исполнения серверных команд.
     */
    private final CommandManager commandManager;

    /**
     * Менеджер для работы с файловой системой (сохранение и загрузка).
     */
    private final FileManager fileManager;

    /**
     * Менеджер, управляющий текущим состоянием коллекции объектов.
     */
    private final CollectionManager collectionManager;

    /**
     * Флаг активности сервера. Когда становится false, сервер завершает работу.
     */
    private boolean isRunning;

    /**
     * Создает новый объект сервера с необходимыми зависимостями.
     *
     * @param port              порт для прослушивания входящих соединений.
     * @param commandManager    менеджер для обработки бизнес-логики команд.
     * @param fileManager       менеджер для автоматического сохранения данных.
     * @param collectionManager менеджер для доступа к коллекции StudyGroup.
     */
    public Server(int port, CommandManager commandManager, FileManager fileManager, CollectionManager collectionManager) {
        this.port = port;
        this.commandManager = commandManager;
        this.fileManager = fileManager;
        this.collectionManager = collectionManager;
    }

    /**
     * Запускает основной цикл работы сервера.
     * Метод открывает ServerSocket, устанавливает небольшой тайм-аут ожидания
     * (SoTimeout), чтобы иметь возможность периодически проверять ввод команд
     * в консоли сервера. В блоке finally гарантируется сохранение коллекции
     * в файл перед полным выключением программы.
     */
    public void start() {
        isRunning = true;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setSoTimeout(50);
            System.out.println("Сервер успешно запущен на порту " + port);
            System.out.println("Доступные серверные команды: save, exit");

            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            while (isRunning) {
                checkServerConsole(consoleReader);

                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Подключился новый клиент " + clientSocket.getInetAddress());

                    handleClient(clientSocket);

                } catch (SocketTimeoutException e) {
                    // Тайм-аут вышел, новых подключений нет, переходим к следующей итерации цикла
                } catch (IOException e) {
                    System.out.println("Ошибка при ожидании подключения " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Не удалось запустить сервер " + e.getMessage());
        } finally {
            fileManager.saveCollection(collectionManager.getCollection());
            System.out.println("Работа сервера завершена.");
        }
    }

    /**
     * Проверяет наличие введенных строк в консоли сервера без блокировки потока.
     * Поддерживает команды "save" для принудительного сохранения и "exit"
     * для остановки сервера.
     *
     * @param consoleReader ридер для чтения из стандартного потока ввода System.in.
     */
    private void checkServerConsole(BufferedReader consoleReader) {
        try {
            if (consoleReader.ready()) {
                String line = consoleReader.readLine().trim();
                if (line.equalsIgnoreCase("save")) {
                    try {
                        fileManager.saveCollection(collectionManager.getCollection());
                    } catch (FileSaveException e) {
                        System.out.println(e.getMessage());
                    }
                } else if (line.equalsIgnoreCase("exit")) {
                    System.out.println("Выключение сервера");
                    isRunning = false;
                } else {
                    System.out.println("Неизвестная команда. Доступны только 'save' и 'exit'");
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения консоли сервера " + e.getMessage());
        }
    }

    /**
     * Обрабатывает жизненный цикл взаимодействия с одним клиентом.
     * Считывает запрос, исполняет его и отправляет результат обратно.
     * По завершении закрывает соединение с клиентом.
     *
     * @param clientSocket сокет подключенного клиента.
     */
    private void handleClient(Socket clientSocket) {
        try {
            Request request = readRequest(clientSocket);

            if (request != null) {
                System.out.println("Получена команда от клиента " + request.getCommandsName());

                Response response = commandManager.exeCommand(request);

                sendResponse(clientSocket, response);
                System.out.println("Ответ успешно отправлен клиенту.");
            }
        } catch (Exception e) {
            System.out.println("Ошибка при обработке запроса клиента " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Ошибка при закрытии сокета клиента " + e.getMessage());
            }
        }
    }

    /**
     * Десериализует объект запроса из входного потока сокета.
     *
     * @param clientSocket сокет клиента.
     * @return объект Request, присланный клиентом.
     * @throws IOException            при ошибках чтения данных.
     * @throws ClassNotFoundException если класс Request не найден в classpath.
     */
    private Request readRequest(Socket clientSocket) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
        return (Request) ois.readObject();
    }

    /**
     * Сериализует и отправляет объект ответа в выходной поток сокета.
     *
     * @param clientSocket сокет клиента.
     * @param response     объект ответа для отправки.
     * @throws IOException при ошибках записи данных.
     */
    private void sendResponse(Socket clientSocket, Response response) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
        oos.writeObject(response);
        oos.flush();
    }
}