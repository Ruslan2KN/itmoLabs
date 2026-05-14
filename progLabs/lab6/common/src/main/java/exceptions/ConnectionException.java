package exceptions;

/**
 * Исключение, выбрасываемое при критических ошибках сетевого соединения.
 * Используется при недоступности сервера или разрыве связи в процессе обмена данными.
 */
public class ConnectionException extends RuntimeException {
    /**
     * @param message описание сетевой ошибки.
     */
    public ConnectionException(String message) {
        super(message);
    }
}
