package src.server.commands;

import src.managers.CollectionManager;
import src.network.Request;
import src.network.Response;
import src.models.StudyGroup;

/**
 * Внутренняя команда для проверки существования элемента коллекции по его идентификатору.
 * Данная команда используется клиентским приложением (например, перед выполнением
 * команды update), чтобы убедиться, что объект с заданным ID присутствует в коллекции
 * на стороне сервера.
 */
public class CheckId extends Command {
    /**
     * Менеджер коллекции, в котором производится поиск объекта.
     */
    private final CollectionManager collectionManager;

    /**
     * Конструктор, инициализирующий команду проверки ID.
     * Устанавливает системное имя команды check_id и описание для внутреннего использования.
     *
     * @param collectionManager Менеджер коллекции для доступа к базе объектов.
     */
    public CheckId(CollectionManager collectionManager) {
        super(Commands.check_id, "внутренняя команда проверки существования id");
        this.collectionManager = collectionManager;
    }

    /**
     * Возвращает количество обязательных аргументов для выполнения команды.
     *
     * @return Возвращает 1 (для проверки требуется только один аргумент — id).
     */
    @Override
    public int getRequiredArgsCount(){
        return 1;
    }

    /**
     * Выполняет поиск объекта в коллекции по переданному идентификатору.
     * Извлекает ID из первого аргумента запроса. При нахождении объекта возвращает
     * успешный ответ, содержащий найденную группу в коллекции ответа.
     * В случае отсутствия ID или неверного формата возвращает отрицательный статус.
     *
     * @param request Объект сетевого запроса, содержащий массив аргументов.
     * @return Объект ответа с результатом проверки и найденным объектом при успехе.
     */
    @Override
    public Response execute(Request request) {
        try {
            long id = Long.parseLong(request.getArgs()[0]);
            StudyGroup group = collectionManager.getById(id);
            if (group != null) {
                return new Response("id найден", true, java.util.Collections.singletonList(group));
            } else {
                return new Response("Ошибка, Элемент с id " + id + " не найден.", false, null);
            }
        } catch (Exception e) {
            return new Response("Ошибка, неверный формат id.", false, null);
        }
    }
}