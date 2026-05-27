package commands;

import exceptions.EmptyCollectionException;
import managers.CollectionManager;
import network.Request;
import network.Response;

/**
 * Команда для вывода значений поля groupAdmin всех элементов коллекции в порядке убывания.
 * Делегирует логику фильтрации, сортировки и форматирования классу CollectionManager,
 * упаковывая полученный текстовый результат в сетевой ответ для клиента.
 */
public class PrintFieldDescendingGroupAdmin extends Command {
    /**
     * Менеджер коллекции, предоставляющий доступ к отсортированным данным администраторов.
     */
    private final CollectionManager collectionManager;

    /**
     * Конструктор, инициализирующий команду вывода администраторов по убыванию.
     * Устанавливает системное имя команды print_field_descending_group_admin и описание для справки.
     *
     * @param collectionManager Менеджер коллекции для получения отформатированных данных.
     */
    public PrintFieldDescendingGroupAdmin(CollectionManager collectionManager) {
        super(Commands.print_field_descending_group_admin, "вывести элементы поля groupAdmin в порядке убывания");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет логику команды вывода администраторов.
     * Метод запрашивает у менеджера коллекции готовую отформатированную строку
     * со списком администраторов. Если коллекция пуста, перехватывает
     * исключение EmptyCollectionException и возвращает клиенту соответствующее уведомление.
     *
     * @param request Объект сетевого запроса от клиента.
     * @return Объект ответа, содержащий текстовый список администраторов или сообщение о пустой коллекции.
     */
    @Override
    public Response execute(Request request) {
        try {
            String adminsList = collectionManager.getFieldDescendingGroupAdmin();
            return new Response("Админы групп по убыванию:\n" + adminsList, true, null);

        } catch (EmptyCollectionException e) {
            return new Response(e.getMessage(), true, null);
        }
    }
}