package src.server.commands;

import src.managers.CollectionManager;
import src.network.Request;
import src.network.Response;

/**
 * Команда для полной очистки коллекции.
 * Удаляет все элементы из текущей коллекции учебных групп через менеджер коллекции.
 */
public class Clear extends Command {
    /**
     * Менеджер коллекции, над которой будет выполнена операция очистки.
     */
    private final CollectionManager collectionManager;

    /**
     * Конструктор, инициализирующий команду очистки.
     * Устанавливает системное имя команды clear и описание для формирования справки.
     *
     * @param collectionManager Менеджер коллекции, который будет очищен.
     */
    public Clear(CollectionManager collectionManager) {
        super(Commands.clear, "очистить коллекцию");
        this.collectionManager = collectionManager;
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
        collectionManager.clear();
        return new Response("Коллекция успешно очищена.", true, null);
    }
}