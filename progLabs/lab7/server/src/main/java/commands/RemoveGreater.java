package commands;

import dao.StudyGroupDAO;
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
    private final StudyGroupDAO studyGroupDAO;

    /**
     * Конструктор, инициализирующий команду удаления превышающих элементов.
     *
     * @param collectionManager Менеджер коллекции для выполнения операций над данными.
     */
    public RemoveGreater(CollectionManager collectionManager, StudyGroupDAO studyGroupDAO) {
        super(Commands.remove_greater, "удалить из коллекции все элементы, превышающие заданный");
        this.collectionManager = collectionManager;
        this.studyGroupDAO=studyGroupDAO;
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
        try {
            int studentsCountToCompare = request.getAttachedObject().getStudentsCount();

            int deletedCount = studyGroupDAO.deleteGreaterStudyGroups(studentsCountToCompare, request.getUsername());

            if (deletedCount > 0) {
                collectionManager.removeGreater(request.getAttachedObject(), request.getUsername());
                return new Response("Успешно удалено элементов: " + deletedCount, true, null);
            } else {
                return new Response("У вас нет элементов, превышающих заданный (или коллекция пуста).", true, null);
            }
        } catch (Exception e) {
            return new Response("Ошибка при удалении из базы данных: " + e.getMessage(), false, null);
        }
    }
}