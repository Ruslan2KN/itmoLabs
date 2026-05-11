package src.server.commands;

import src.managers.CollectionManager;
import src.network.Request;
import src.network.Response;
import src.models.StudyGroup;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Команда для отображения всех элементов коллекции в строковом представлении.
 * Перед отправкой клиенту элементы сортируются в их естественном порядке (natural ordering).
 */
public class Show extends Command {
    /**
     * Менеджер коллекции, хранящий объекты StudyGroup.
     */
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды вывода элементов.
     *
     * @param collectionManager Менеджер коллекции для доступа к ArrayDeque.
     */
    public Show(CollectionManager collectionManager) {
        super(Commands.show, "вывести элементы коллекции");
        this.collectionManager = collectionManager;
    }

    /**
     * Формирует список всех элементов коллекции.
     * Если коллекция не пуста, возвращает отсортированный список в объекте Response.
     *
     * @param request Запрос от клиента.
     * @return Ответ, содержащий текстовое сообщение и коллекцию объектов для отображения.
     */
    @Override
    public Response execute(Request request) {
        if (collectionManager.getCollection().isEmpty()) {
            return new Response("Коллекция пуста.", true, null);
        }

        List<StudyGroup> sortedCollection = collectionManager.getCollection().stream()
                .sorted()
                .collect(Collectors.toList());

        return new Response("Элементы коллекции:", true, sortedCollection);
    }
}