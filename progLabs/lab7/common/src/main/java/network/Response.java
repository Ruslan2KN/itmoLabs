package network;

import models.StudyGroup;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;

/**
 * Класс, представляющий ответ от сервера клиенту.
 * Является контейнером, который передает результат выполнения команды,
 * текстовое сообщение, коллекцию данных (если это необходимо) и
 * специальные инструкции для клиента (флаг запроса объекта).
 */
public class Response implements Serializable {
    /**
     * Уникальный идентификатор версии сериализованного класса.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Текстовое сообщение с результатом операции или описанием ошибки.
     */
    private final String message;

    /**
     * Статус выполнения запроса (true - успешно, false - ошибка).
     */
    private final boolean success;

    /**
     * Коллекция объектов StudyGroup, возвращаемая сервером (например, для команды show).
     */
    private final Collection<StudyGroup> collection;

    /**
     * Флаг, указывающий клиенту на необходимость ввода данных сложного объекта.
     * Используется в двухшаговом протоколе взаимодействия.
     */
    private final boolean requiresObject;

    /**
     * Полный конструктор для создания объекта ответа.
     *
     * @param message        текстовый результат выполнения.
     * @param success        статус успеха операции.
     * @param collection     коллекция элементов (может быть null).
     * @param requiresObject флаг необходимости ввода объекта пользователем.
     */
    public Response(String message, boolean success, Collection<StudyGroup> collection, boolean requiresObject) {
        this.message = message;
        this.success = success;
        this.collection = collection;
        this.requiresObject = requiresObject;
    }

    /**
     * Упрощенный конструктор для стандартных ответов.
     * Флаг requiresObject автоматически устанавливается в значение false.
     *
     * @param message    текстовый результат выполнения.
     * @param success    статус успеха операции.
     * @param collection коллекция элементов (может быть null).
     */
    public Response(String message, boolean success, Collection<StudyGroup> collection) {
        this(message, success, collection, false);
    }

    /**
     * Возвращает текстовое сообщение сервера.
     *
     * @return строка с сообщением.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Проверяет, была ли команда выполнена успешно.
     *
     * @return true если успешно, иначе false.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Возвращает коллекцию, переданную сервером.
     *
     * @return коллекция объектов StudyGroup.
     */
    public Collection<StudyGroup> getCollection() {
        return collection;
    }

    /**
     * Проверяет, требуется ли от клиента ввод данных для создания объекта.
     *
     * @return true если требуется ввод, иначе false.
     */
    public boolean isRequiresObject() {
        return requiresObject;
    }
}