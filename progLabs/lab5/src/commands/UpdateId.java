package lab5.src.commands;

import lab5.src.managers.CollectionManager;
import lab5.src.managers.ConsoleInput;
import lab5.src.models.StudyGroup;

/**
 * Команда для обновления полей существующего элемента коллекции по его уникальному идентификатору (ID).
 * Позволяет пользователю интерактивно выбрать и изменить конкретные значения объекта StudyGroup.
 */
public class UpdateId extends Command{
    private final CollectionManager collectionManager;
    private final ConsoleInput consoleInput;

    /**
     * Конструктор, инициализирующий команду обновления.
     * Устанавливает имя команды "update_id" и её описание.
     *
     * @param collectionManager менеджер коллекции для поиска существующего элемента по ID
     * @param consoleInput   объект для интерактивного взаимодействия с пользователем при обновлении данных
     */
    public UpdateId(CollectionManager collectionManager, ConsoleInput consoleInput){
        super("update_id", "обновить значение элемента коллекции, по его id");
        this.collectionManager=collectionManager;
        this.consoleInput=consoleInput;
    }

    /**
     * Выполняет логику обновления элемента.
     * Проверяет наличие ID в аргументах, ищет соответствующую группу в коллекции
     * и, если элемент найден, запускает процесс пошагового редактирования полей.
     *
     * @param args массив строковых аргументов (ожидается ровно один аргумент — ID обновляемого элемента)
     * @throws IllegalArgumentException если ID не указан, элемент с таким ID не найден в коллекции или аргумент не является числом
     */
    @Override
    public void execute(String[] args){
        if (args.length !=1){
            throw new IllegalArgumentException("Ошибка, команда update требует ровно один элемент.");
        }

        try {
            long id = Long.parseLong(args[0]);

            StudyGroup oldGroup= collectionManager.getById(id);
            if (oldGroup == null){
                throw new IllegalArgumentException("Ошибка, элемент c id "+id+" не найден в коллекции");
            }
            consoleInput.updateStudyGroup(oldGroup);
            System.out.println(id + " элемент успешно обновлен");
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("Ошибка, id должны быть цельым числом");
        }
    }
}
