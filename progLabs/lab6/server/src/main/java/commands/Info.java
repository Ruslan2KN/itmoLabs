package commands;

import managers.CollectionManager;
import network.Request;
import network.Response;

/**
 * Команда для получения краткой сводной информации о текущем состоянии коллекции.
 * Выводит тип реализации коллекции и текущее количество содержащихся в ней элементов.
 */
public class Info extends Command {
    /**
     * Менеджер коллекции, из которого извлекаются метаданные.
     */
    private final CollectionManager collectionManager;

    /**
     * Конструктор, инициализирующий команду получения информации.
     * Устанавливает системное имя команды "info" и описание для формирования справки.
     *
     * @param collectionManager Менеджер коллекции для доступа к метаданным очереди.
     */
    public Info(CollectionManager collectionManager) {
        super(Commands.info, "информация о коллекции");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет логику формирования информационного сообщения.
     * Метод обращается к менеджеру коллекции, получает имя класса реализации
     * и количество элементов, после чего формирует итоговую строку для отправки клиенту.
     *
     * @param request Объект сетевого запроса от клиента.
     * @return Объект ответа, содержащий текстовое описание параметров коллекции.
     */
    @Override
    public Response execute(Request request) {
        String info = "Тип коллекции: " + collectionManager.getCollection().getClass().getSimpleName() + "\n" +
                "Количество элементов: " + collectionManager.getCollection().size();
        return new Response(info, true, null);
    }
}