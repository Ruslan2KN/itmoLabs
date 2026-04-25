package lab5.src.commands;

import lab5.src.managers.CollectionManager;

/**
 * Команда для удаления элемента из коллекции по его уникальному идентификатору (ID).
 * Осуществляет поиск элемента в коллекции и, если он найден, производит его удаление.
 */
public class RemoveById extends Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор, инициализирующий команду удаления по ID.
     * Устанавливает имя команды "remove_by_id" и её описание.
     *
     * @param collectionManager менеджер коллекции, из которой будет удален элемент
     */
    public RemoveById(CollectionManager collectionManager) {
        super(Commands.remove_by_id, "удалить элемент из коллекции по id");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет логику удаления элемента по ID.
     * Проверяет наличие ровно одного аргумента и пытается преобразовать его в число типа long.
     * Если элемент с таким ID существует, он удаляется; в противном случае выводится уведомление о неудаче.
     *
     * @param args массив строковых аргументов (ожидается ровно один аргумент — ID элемента)
     * @throws IllegalArgumentException если количество аргументов не равно одному или если переданный ID не является числом
     */
    @Override
    public void execute(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Ошибка, команда remove_by_id требует только один эелемент");

        }
        try {
            long id = Long.parseLong(args[0]);

            boolean isRemoved = collectionManager.removeById(id);

            if (isRemoved) {
                System.out.println(id + " элемент удален из коллекции ");
            } else {
                System.out.println(id + " элемент не найден в коллекции, удаление не было");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ошибка, id должен быть целым числом.");
        }
    }
}
