package lab5.src.commands;

import lab5.src.managers.CollectionManager;

/**
 * Команда для вывода основной информации о коллекции.
 * Выводит тип коллекции, дату инициализации и текущее количество элементов.
 */
public class Info extends Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор, инициализирующий команду получения информации.
     * Устанавливает имя команды "info" и её описание.
     *
     * @param collectionManager менеджер коллекции, из которого будут извлечены метаданные
     */
    public Info(CollectionManager collectionManager) {
        super("info", "информация о коллекции");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет логику команды вывода информации.
     * Проверяет отсутствие лишних аргументов и делегирует запрос менеджеру коллекции.
     *
     * @param args массив строковых аргументов, переданных с командой (ожидается пустой массив)
     * @throws IllegalArgumentException если команде переданы какие-либо дополнительные параметры
     */
    @Override
    public void execute(String[] args) {
        if (args.length > 0) {
            throw new IllegalArgumentException("Команда add не принимает параметров.");
        }
        collectionManager.info();
    }
}
