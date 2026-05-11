package src.server.commands;

import src.managers.CollectionManager;
import src.network.Request;
import src.network.Response;
import src.models.StudyGroup;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Команда для вывода всех элементов коллекции в порядке убывания.
 * Порядок определяется методом compareTo класса StudyGroup, но инвертируется
 * для достижения сортировки от большего к меньшему.
 */
public class PrintDescending extends Command {
    /**
     * Менеджер коллекции, предоставляющий доступ к элементам для сортировки.
     */
    private final CollectionManager collectionManager;

    /**
     * Конструктор, инициализирующий команду вывода по убыванию.
     * Устанавливает системное имя команды print_descending и описание для справки.
     *
     * @param collectionManager Менеджер коллекции для получения данных.
     */
    public PrintDescending(CollectionManager collectionManager) {
        super(Commands.print_descending, "вывести элементы коллекции в порядке убывания");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет логику сортировки и формирования ответа.
     * Метод использует Stream API для преобразования текущей коллекции в
     * отсортированный список. Если коллекция пуста, возвращается
     * соответствующее уведомление.
     *
     * @param request Объект сетевого запроса от клиента.
     * @return Объект ответа, содержащий отсортированный список учебных групп.
     */
    @Override
    public Response execute(Request request) {

        if (collectionManager.getCollection().isEmpty()) {
            return new Response("Коллекция пуста.", true, null);
        }

        List<StudyGroup> descendingCollection = collectionManager.getDescending();

        return new Response("Элементы коллекции по убыванию:", true, descendingCollection);
    }
}