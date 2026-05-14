package commands;

import managers.CollectionManager;
import models.StudyGroup;
import network.Request;
import network.Response;

/**
 * Команда для обновления данных существующего элемента коллекции по его ID.
 * При обновлении сохраняются оригинальный идентификатор и дата создания элемента.
 * Команда требует передачи одного аргумента (ID) и прикрепленного объекта с новыми данными.
 */
public class UpdateId extends Command {
    /**
     * Менеджер коллекции для поиска и замены элементов.
     */
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды обновления по идентификатору.
     *
     * @param collectionManager Менеджер коллекции.
     */
    public UpdateId(CollectionManager collectionManager) {
        super(Commands.update_id, "обновить значение элемента коллекции по id");
        this.collectionManager = collectionManager;
    }

    /**
     * Указывает количество обязательных строковых аргументов.
     *
     * @return Возвращает 1 (необходим ID обновляемого элемента).
     */
    @Override
    public int getRequiredArgsCount() {
        return 1;
    }

    /**
     * Выполняет процедуру обновления объекта.
     * Сначала проверяет существование объекта с данным ID, затем переносит
     * служебные поля (ID и дата создания) в новый объект и замещает старый элемент.
     *
     * @param request Запрос, содержащий ID в аргументах и новые данные в прикрепленном объекте.
     * @return Ответ со статусом успеха или описанием ошибки.
     */
    @Override
    public Response execute(Request request) {
        try {
            long id = Long.parseLong(request.getArgs()[0]);
            StudyGroup oldGroup = collectionManager.getById(id);

            if (oldGroup == null) {
                return new Response("Ошибка: элемент c id " + id + " не найден в коллекции.", false, null);
            }

            StudyGroup updatedGroup = request.getAttachedObject();
            if (updatedGroup == null) {
                return new Response("Ошибка: новые данные для обновления не переданы.", false, null);
            }

            updatedGroup.setId(oldGroup.getId());
            updatedGroup.setCreationDate(oldGroup.getCreationDate());

            collectionManager.removeById(id);
            collectionManager.addUpdatedGroup(updatedGroup);

            return new Response("Элемент с id " + id + " успешно обновлен.", true, null);
        } catch (NumberFormatException e) {
            return new Response("Ошибка: id должен быть целым числом.", false, null);
        }
    }
}