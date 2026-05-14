package commands;

import managers.CollectionManager;
import network.Request;
import network.Response;

/**
 * Команда для удаления из коллекции всех элементов, которые меньше заданного.
 * Логика сравнения базируется на реализации интерфейса Comparable в модели StudyGroup.
 * Аналогично команде добавления, требует наличия прикрепленного объекта в запросе для выполнения сравнения.
 */
public class RemoveLower extends Command {
    /**
     * Менеджер коллекции для управления элементами очереди.
     */
    private final CollectionManager collectionManager;

    /**
     * Конструктор для создания экземпляра команды remove_lower.
     *
     * @param collectionManager Менеджер коллекции, предоставляющий методы фильтрации.
     */
    public RemoveLower(CollectionManager collectionManager) {
        super(Commands.remove_lower, "удалить из коллекции все элементы, меньшие заданного");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет операцию удаления элементов, значение которых меньше переданного объекта.
     *
     * @param request Сетевой запрос, из которого извлекается объект для сравнения.
     * @return Ответ со статусом завершения операции.
     */
    @Override
    public Response execute(Request request) {
        if (request.getAttachedObject() == null) {
            return new Response("Для сравнения необходимо ввести данные объекта:", true, null, true);
        }

        boolean isRemoved = collectionManager.removeLower(request.getAttachedObject());
        return new Response(isRemoved ? "Элементы удалены." : "Подходящих элементов не найдено.", true, null);
    }
}