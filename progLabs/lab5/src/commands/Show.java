package lab5.src.commands;

import lab5.src.managers.CollectionManager;

/**
 * Команда для вывода всех элементов коллекции в строковом представлении.
 * Поочередно отображает каждый объект, хранящийся в коллекции, используя его метод toString.
 */
public class Show extends Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор, инициализирующий команду вывода.
     * Устанавливает имя команды "show" и её описание.
     *
     * @param collectionManager менеджер коллекции, содержимое которой необходимо отобразить
     */
    public Show(CollectionManager collectionManager) {
        super(Commands.show, "выводит элементы коллекции");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет логику команды вывода элементов.
     * Проверяет отсутствие лишних аргументов и делегирует задачу вывода менеджеру коллекции.
     *
     * @param args массив строковых аргументов, переданных с командой (ожидается пустой массив)
     * @throws IllegalArgumentException если команде переданы какие-либо дополнительные параметры
     */
    @Override
    public void execute(String[] args) {
        if (args.length > 0) {
            throw new IllegalArgumentException("Команда add не принимает параметров.");
        }
        System.out.println("Элементы коллекции: ");
        collectionManager.show();
    }
}
