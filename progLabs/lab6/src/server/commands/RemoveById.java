package src.server.commands;

import src.managers.CollectionManager;
import src.network.Request;
import src.network.Response;

/**
 * Команда для удаления элемента из коллекции по его уникальному идентификатору (ID).
 * Данная команда требует передачи одного строкового аргумента, который будет
 * преобразован в число типа long.
 */
public class RemoveById extends Command {
    /**
     * Менеджер коллекции, в которой производится поиск и удаление элемента.
     */
    private final CollectionManager collectionManager;

    /**
     * Конструктор, инициализирующий команду удаления по ID.
     * Устанавливает системное имя команды remove_by_id и описание для формирования справки.
     *
     * @param collectionManager Менеджер коллекции для выполнения операции удаления.
     */
    public RemoveById(CollectionManager collectionManager) {
        super(Commands.remove_by_id, "удалить элемент из коллекции по id");
        this.collectionManager = collectionManager;
    }

    /**
     * Возвращает количество обязательных строковых аргументов для выполнения команды.
     *
     * @return Возвращает 1 (команде необходим идентификатор объекта).
     */
    @Override
    public int getRequiredArgsCount(){
        return 1;
    }

    /**
     * Выполняет логику удаления элемента по ID.
     * Метод извлекает идентификатор из аргументов запроса, пытается найти
     * соответствующий элемент в коллекции и удалить его. В случае успеха возвращает
     * подтверждение, если ID не найден или формат аргумента неверен — сообщает об ошибке.
     *
     * @param request Объект сетевого запроса, содержащий массив аргументов.
     * @return Объект ответа со статусом выполнения и текстовым уведомлением.
     */
    @Override
    public Response execute(Request request) {
        try {
            long id = Long.parseLong(request.getArgs()[0]);
            boolean isRemoved = collectionManager.removeById(id);
            if (isRemoved) {
                return new Response("Элемент с id " + id + " успешно удален.", true, null);
            } else {
                return new Response("Элемент с id " + id + " не найден.", false, null);
            }
        } catch (NumberFormatException e) {
            return new Response("Ошибка: id должен быть целым числом.", false, null);
        }
    }
}