package commands;

import managers.CollectionManager;
import network.Request;
import network.Response;

/**
 * Команда для удаления из коллекции всех элементов, превышающих заданный.
 * Сравнение элементов производится на основе логики метода compareTo в классе StudyGroup.
 * Команда реализует двухэтапную проверку: если объект для сравнения не прикреплен к запросу,
 * сервер отправляет сигнал клиенту о необходимости ввода данных.
 */
public class RemoveGreater extends Command {
    /**
     * Менеджер коллекции, в которой производится поиск и удаление элементов.
     */
    private final CollectionManager collectionManager;

    /**
     * Конструктор, инициализирующий команду удаления превышающих элементов.
     *
     * @param collectionManager Менеджер коллекции для выполнения операций над данными.
     */
    public RemoveGreater(CollectionManager collectionManager) {
        super(Commands.remove_greater, "удалить из коллекции все элементы, превышающие заданный");
        this.collectionManager = collectionManager;
    }

    /**
     * Исполняет логику удаления элементов, которые больше переданного объекта.
     * Если в запросе отсутствует прикрепленный объект StudyGroup, возвращается ответ с требованием ввода.
     *
     * @param request Объект сетевого запроса, содержащий (при наличии) объект для сравнения.
     * @return Объект ответа с результатом операции (элементы удалены или не найдены).
     */
    @Override
    public Response execute(Request request) {
        if (request.getAttachedObject() == null) {
            return new Response("Для сравнения необходимо ввести данные объекта:", true, null, true);
        }

        boolean isRemoved = collectionManager.removeGreater(request.getAttachedObject());
        return new Response(isRemoved ? "Элементы удалены." : "Подходящих элементов не найдено.", true, null);
    }
}