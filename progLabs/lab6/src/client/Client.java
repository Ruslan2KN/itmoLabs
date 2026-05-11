package src.client;

import src.exceptions.ConnectionException;
import src.network.Request;
import src.network.Response;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Класс, отвечающий за сетевое взаимодействие с сервером.
 * Осуществляет передачу запросов и получение ответов по протоколу TCP.
 * Использует средства пакета java.nio для работы в неблокирующем режиме (non-blocking IO).
 */
public class Client {
    /**
     * Хост (адрес) сервера.
     */
    private final String host;

    /**
     * Порт, на котором сервер принимает подключения.
     */
    private final int port;

    /**
     * Сетевой канал для обмена данными с сервером.
     */
    private SocketChannel socketChannel;

    /**
     * Создает новый объект клиента с заданными параметрами адреса.
     *
     * @param host Адрес сервера для подключения.
     * @param port Порт сервера для подключения.
     */
    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Устанавливает неблокирующее соединение с сервером.
     * Метод открывает канал, переводит его в неблокирующий режим и пытается
     * завершить подключение. В случае превышения времени ожидания или физической
     * недоступности сервера выбрасывается исключение.
     *
     * @throws ConnectionException Если не удалось подключиться к серверу за отведенное время.
     */
    public void connect() {
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress(host, port));

            int retries = 0;
            while (!socketChannel.finishConnect()) {
                Thread.sleep(50);
                retries++;
                if (retries > 60) {
                    throw new ConnectionException("Ошибка, превышено время ожидания подключения к серверу.");
                }
            }
        } catch (ConnectionException e) {
            throw e;
        } catch (Exception e) {
            throw new ConnectionException("Сервер временно недоступен: " + e.getMessage());
        }
    }

    /**
     * Сериализует запрос, отправляет его на сервер и ожидает получение ответа.
     * Метод автоматически вызывает connect() перед каждой операцией. Запрос сериализуется
     * в массив байтов, передается через ByteBuffer. Полученный ответ десериализуется
     * обратно в объект Response. После выполнения операции соединение закрывается.
     *
     * @param request Объект запроса, содержащий команду, аргументы и прикрепленный объект.
     * @return Объект ответа, полученный от сервера.
     * @throws ConnectionException При разрыве соединения, тайм-ауте ожидания данных или ошибках сети.
     */
    public Response sendAndReceive(Request request) {
        connect();

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(request);
            oos.flush();

            ByteBuffer writeBuffer = ByteBuffer.wrap(baos.toByteArray());
            while (writeBuffer.hasRemaining()) {
                socketChannel.write(writeBuffer);
            }

            ByteBuffer readBuffer = ByteBuffer.allocate(1024 * 1024);
            int bytesRead;
            int retries = 0;

            while ((bytesRead = socketChannel.read(readBuffer)) == 0) {
                Thread.sleep(50);
                retries++;
                if (retries > 100) {
                    throw new ConnectionException("Ошибка, сервер не отвечает (тайм-аут).");
                }
            }

            if (bytesRead == -1) {
                throw new ConnectionException("Соединение с сервером было разорвано.");
            }

            ByteArrayInputStream bais = new ByteArrayInputStream(readBuffer.array(), 0, bytesRead);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Response) ois.readObject();

        } catch (ConnectionException e) {
            throw e;
        } catch (Exception e) {
            throw new ConnectionException("Ошибка сети: не удалось отправить или получить данные. " + e.getMessage());
        } finally {
            try {
                if (socketChannel != null && socketChannel.isOpen()) {
                    socketChannel.close();
                }
            } catch (Exception ignored) {}
        }
    }
}