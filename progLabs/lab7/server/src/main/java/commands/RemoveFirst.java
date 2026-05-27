package commands;

import dao.StudyGroupDAO;
import managers.CollectionManager;
import models.StudyGroup;
import network.Request;
import network.Response;

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
    private final StudyGroupDAO studyGroupDAO;

    /**
     * Конструктор, инициализирующий команду удаления первого элемента.
     * Устанавливает системное имя команды remove_first и описание для формирования справки.
     *
     * @param collectionManager Менеджер коллекции для выполнения операции.
     */
    public RemoveFirst(CollectionManager collectionManager, StudyGroupDAO studyGroupDAO) {
        super(Commands.remove_first, "удалить первый элемент из коллекции");
        this.collectionManager = collectionManager;
        this.studyGroupDAO=studyGroupDAO;
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
            StudyGroup firstElement = collectionManager.getAbsoluteFirst();

            if (firstElement == null) {
                return new Response("Коллекция пуста, удалять нечего.", false, null);
            }

            if (!firstElement.getOwner().equals(request.getUsername())) {
                return new Response("Ошибка: первый элемент принадлежит другому пользователю, вы не можете его удалить.", false, null);
            }

            boolean isDeletedInDb = studyGroupDAO.deleteStudyGroupById(firstElement.getId(), request.getUsername());

            if (isDeletedInDb) {
                collectionManager.removeById(firstElement.getId(), request.getUsername());
                return new Response("Первый элемент успешно удален.", true, null);
            } else {
                return new Response("Произошла ошибка: не удалось удалить элемент из базы данных.", false, null);
            }

        } catch (Exception e) {
            return new Response("Ошибка при выполнении команды: " + e.getMessage(), false, null);
        }
    }
}