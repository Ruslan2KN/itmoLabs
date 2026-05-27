package commands;

import dao.StudyGroupDAO;
import managers.CollectionManager;
import network.Request;
import network.Response;

/**
 * Команда для полной очистки коллекции.
 * Удаляет все элементы из текущей коллекции учебных групп через менеджер коллекции.
 */
public class Clear extends Command {
    /**
     * Менеджер коллекции, над которой будет выполнена операция очистки.
     */
    private final CollectionManager collectionManager;
    private final StudyGroupDAO studyGroupDAO;

    /**
     * Конструктор, инициализирующий команду очистки.
     * Устанавливает системное имя команды clear и описание для формирования справки.
     *
     * @param collectionManager Менеджер коллекции, который будет очищен.
     */
    public Clear(CollectionManager collectionManager, StudyGroupDAO studyGroupDAO) {
        super(Commands.clear, "очистить коллекцию");
        this.collectionManager = collectionManager;
        this.studyGroupDAO=studyGroupDAO;
    }

    /**
     * Выполняет логику очистки коллекции.
     * Вызывает метод clear у менеджера коллекции и формирует ответ об успешном завершении операции.
     *
     * @param request Объект сетевого запроса от клиента.
     * @return Объект ответа с подтверждением того, что коллекция была очищена.
     */
    @Override
    public Response execute(Request request) {
        try {
            studyGroupDAO.clearStudyGroups(request.getUsername());
            collectionManager.clear(request.getUsername());
            return new Response("Все ваши элементы успешно удалены из коллекции.", true, null);
        } catch (Exception e){
            return new Response("Ошибка при очистке в БД: " + e.getMessage(), false, null);
        }
    }
}