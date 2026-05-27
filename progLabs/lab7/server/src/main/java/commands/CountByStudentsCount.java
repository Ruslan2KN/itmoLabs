package commands;

import managers.CollectionManager;
import network.Request;
import network.Response;

/**
 * Команда для подсчета количества элементов коллекции, у которых значение поля studentsCount
 * совпадает с заданным пользователем.
 * Данная команда требует передачи одного числового аргумента.
 */
public class CountByStudentsCount extends Command {
    /**
     * Менеджер коллекции, в которой производится поиск и подсчет элементов.
     */
    private final CollectionManager collectionManager;

    /**
     * Конструктор, инициализирующий команду подсчета по числу студентов.
     * Устанавливает системное имя команды count_by_students_count и описание для справки.
     *
     * @param collectionManager Менеджер коллекции для выполнения операций поиска.
     */
    public CountByStudentsCount(CollectionManager collectionManager) {
        super(Commands.count_by_students_count, "вывести количество элементов с заданным числом студентов");
        this.collectionManager = collectionManager;
    }

    /**
     * Возвращает количество обязательных строковых аргументов для данной команды.
     *
     * @return Возвращает 1 (команде требуется одно число — количество студентов).
     */
    @Override
    public int getRequiredArgsCount() {
        return 1;
    }

    /**
     * Выполняет логику подсчета элементов с заданным количеством студентов.
     * Метод извлекает число из аргументов запроса, валидирует его и запрашивает
     * данные у менеджера коллекции. В случае успешного выполнения возвращает
     * количество найденных элементов, при ошибке парсинга — сообщение о неверном формате.
     *
     * @param request Объект сетевого запроса, содержащий массив аргументов.
     * @return Объект ответа с результатом подсчета или сообщением об ошибке.
     */
    @Override
    public Response execute(Request request) {
        try {
            int count = Integer.parseInt(request.getArgs()[0]);
            long result = collectionManager.countByStudentsCount(count);
            return new Response("Найдено элементов с количеством студентов " + count + ": " + result, true, null);
        } catch (NumberFormatException e) {
            return new Response("Ошибка: количество студентов должно быть целым числом.", false, null);
        }
    }
}