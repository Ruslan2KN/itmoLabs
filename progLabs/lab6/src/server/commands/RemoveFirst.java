package src.server.commands;

import src.exceptions.EmptyCollectionException;
import src.managers.CollectionManager;
import src.network.Request;
import src.network.Response;

/**
 * Команда для удаления самого первого элемента из коллекции (головы очереди).
 * Данная команда не требует дополнительных аргументов и работает непосредственно
 * с текущим состоянием очереди в менеджере коллекции.
 */
public class RemoveFirst extends Command {
    /**
     * Менеджер коллекции, в котором будет произведено удаление первого элемента.
     */
    private final CollectionManager collectionManager;

    /**
     * Конструктор, инициализирующий команду удаления первого элемента.
     * Устанавливает системное имя команды remove_first и описание для формирования справки.
     *
     * @param collectionManager Менеджер коллекции для выполнения операции.
     */
    public RemoveFirst(CollectionManager collectionManager) {
        super(Commands.remove_first, "удалить первый элемент из коллекции");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет логику удаления первого элемента.
     * Метод обращается к менеджеру коллекции для извлечения первого элемента.
     * Если коллекция оказывается пуста, перехватывается специализированное исключение
     * EmptyCollectionException, и клиенту отправляется сообщение об ошибке.
     *
     * @param request Объект сетевого запроса от клиента.
     * @return Объект ответа со статусом выполнения и текстовым результатом.
     */
    @Override
    public Response execute(Request request) {
        try {
            collectionManager.removeFirst();
            return new Response("Первый элемент успешно удален.", true, null);
        } catch (EmptyCollectionException e){
            return new Response(e.getMessage(), false, null);
        }
    }
}