package lab5.src.commands;

import lab5.src.managers.CollectionManager;
import lab5.src.managers.ConsoleInput;
import lab5.src.models.StudyGroup;

/**
 * Команда для интерактивного добавления нового элемента (учебной группы) в коллекцию.
 * Взаимодействует с пользователем через консольный ввод для пошагового создания объекта.
 */
public class Add extends Command {
    private final CollectionManager collectionManager;
    private final ConsoleInput consoleInput;

    /**
     * Конструктор, инициализирующий команду и привязывающий её к менеджерам приложения.
     * Устанавливает имя команды "add" и её описание для справки.
     *
     * @param collectionManager менеджер коллекции, в который будет сохранен новый элемент
     * @param consoleInput      объект для считывания и валидации данных пользователя из консоли
     */
    public Add(CollectionManager collectionManager, ConsoleInput consoleInput) {
        super("add", "добавить элемент в коллекцию");
        this.collectionManager = collectionManager;
        this.consoleInput = consoleInput;
    }

    /**
     * Выполняет логику команды добавления.
     * Проверяет отсутствие лишних аргументов, запускает процесс интерактивного создания группы
     * и сохраняет успешный результат в коллекцию.
     *
     * @param args массив строковых аргументов, переданных с командой (ожидается пустой массив)
     * @throws IllegalArgumentException если команде переданы какие-либо дополнительные параметры
     */
    @Override
    public void execute(String[] args) {
        if (args.length > 0) {
            throw new IllegalArgumentException("Команда add не принимает параметров.");
        }
        System.out.println("Создание новой группы");

        StudyGroup newGroup = consoleInput.askStudyGroup();

        collectionManager.add(newGroup);

        System.out.println("Группа успешно добавлена в коллекцию");

    }

}
