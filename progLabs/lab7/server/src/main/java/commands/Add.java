package commands;

import dao.StudyGroupDAO;
import managers.CollectionManager;
import models.StudyGroup;
import network.Request;
import network.Response;

/**
 * Команда для добавления нового элемента (учебной группы) в коллекцию.
 * Данная команда реализует двухшаговый протокол: если в запросе отсутствует
 * готовый объект, она отправляет клиенту требование о необходимости
 * интерактивного ввода данных.
 */
public class Add extends Command {
    /**
     * Менеджер коллекции, в которую будет добавлен новый элемент.
     */
    private final CollectionManager collectionManager;

    private final StudyGroupDAO studyGroupDAO;
    /**
     * Конструктор, инициализирующий команду и привязывающий её к менеджеру коллекции.
     * Устанавливает системное имя команды "add" и её описание для вывода в справке.
     *
     * @param collectionManager Менеджер коллекции, через который осуществляется добавление.
     */
    public Add(CollectionManager collectionManager, StudyGroupDAO studyGroupDAO) {
        super(Commands.add, "добавить элемент в коллекцию");
        this.collectionManager = collectionManager;
        this.studyGroupDAO=studyGroupDAO;
    }

    /**
     * Выполняет бизнес-логику добавления новой группы.
     * Метод проверяет наличие прикрепленного объекта StudyGroup в запросе.
     * Если объект прислан (второй шаг протокола), он регистрируется в коллекции.
     * Если объект равен null (первый шаг), метод формирует ответ с флагом
     * requiresObject, чтобы клиент запросил ввод у пользователя.
     *
     * @param request Объект сетевого запроса, содержащий аргументы и (опционально) объект группы.
     * @return Объект ответа с текстовым сообщением и результатом выполнения операции.
     */
    @Override
    public Response execute(Request request) {
        if (request.getAttachedObject() == null) {
            return new Response("Для команды add требуется ввести данные объекта:", true, null, true);
        }

        StudyGroup newGroup = request.getAttachedObject();
        newGroup.setOwner(request.getUsername());
        try{
            long generatedId= studyGroupDAO.insertStudyGroup(newGroup,request.getUsername());
            newGroup.setId(generatedId);
            collectionManager.addUpdatedGroup(newGroup);
            return new Response("Группа успешно добавлена с ID: " + generatedId, true, null);
        } catch (Exception e){
            return new Response("Ошибка сохранения в базу данных: "+e.getMessage(), false, null);
        }
    }
}