package src.network;

import src.models.StudyGroup;

import java.io.Serial;
import java.io.Serializable;

/**
 * Класс, представляющий сетевой запрос от клиента к серверу.
 * Данный класс служит контейнером для передачи всех данных, необходимых для
 * выполнения команды на стороне сервера: имени команды, её аргументов и
 * прикрепленного объекта (если он требуется).
 */
public class Request implements Serializable {

    /**
     * Уникальный идентификатор версии сериализованного класса.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Строковое имя команды, которую клиент просит выполнить.
     */
    private final String commandsName;

    /**
     * Массив строковых аргументов команды (например, ID или числовые значения).
     */
    private final String[] args;

    /**
     * Сложный объект (учебная группа), передаваемый вместе с запросом.
     * Используется для команд добавления (add) или обновления (update).
     */
    private final StudyGroup attachedObject;

    /**
     * Создает новый объект запроса для отправки на сервер.
     *
     * @param commandsName   имя вызываемой команды.
     * @param args           массив дополнительных строковых аргументов.
     * @param attachedObject объект StudyGroup для выполнения команды (может быть null).
     */
    public Request(String commandsName, String[] args, StudyGroup attachedObject) {
        this.commandsName = commandsName;
        this.args = args;
        this.attachedObject = attachedObject;
    }

    /**
     * Возвращает имя команды из запроса.
     *
     * @return строковое название команды.
     */
    public String getCommandsName() {
        return commandsName;
    }

    /**
     * Возвращает массив аргументов команды.
     *
     * @return массив строк-аргументов.
     */
    public String[] getArgs() {
        return args;
    }

    /**
     * Возвращает прикрепленный к запросу объект StudyGroup.
     *
     * @return объект учебной группы или null, если объект не требовался.
     */
    public StudyGroup getAttachedObject() {
        return attachedObject;
    }
}