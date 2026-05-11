package src.server.commands;

import src.managers.CollectionManager;
import src.models.StudyGroup;
import src.network.Request;
import src.network.Response;

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

    /**
     * Конструктор, инициализирующий команду и привязывающий её к менеджеру коллекции.
     * Устанавливает системное имя команды "add" и её описание для вывода в справке.
     *
     * @param collectionManager Менеджер коллекции, через который осуществляется добавление.
     */
    public Add(CollectionManager collectionManager) {
        super(Commands.add, "добавить элемент в коллекцию");
        this.collectionManager = collectionManager;
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
        collectionManager.addNewGroup(newGroup);
        return new Response("Группа успешно добавлена", true, null);
    }
}