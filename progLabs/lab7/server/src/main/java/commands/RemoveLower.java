package commands;

import dao.StudyGroupDAO;
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
    private final StudyGroupDAO studyGroupDAO;

    /**
     * Конструктор для создания экземпляра команды remove_lower.
     *
     * @param collectionManager Менеджер коллекции, предоставляющий методы фильтрации.
     */
    public RemoveLower(CollectionManager collectionManager, StudyGroupDAO studyGroupDAO) {
        super(Commands.remove_lower, "удалить из коллекции все элементы, меньшие заданного");
        this.collectionManager = collectionManager;
        this.studyGroupDAO=studyGroupDAO;
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

        try {
            int studentsCountToCompare = request.getAttachedObject().getStudentsCount();

            int deletedCount = studyGroupDAO.deleteLowerStudyGroups(studentsCountToCompare, request.getUsername());

            if (deletedCount > 0) {
                collectionManager.removeLower(request.getAttachedObject(), request.getUsername());
                return new Response("Успешно удалено элементов: " + deletedCount, true, null);
            } else {
                return new Response("У вас нет элементов, меньших заданного (или коллекция пуста).", true, null);
            }
        } catch (Exception e) {
            return new Response("Ошибка при удалении из базы данных: " + e.getMessage(), false, null);
        }
    }
}