package lab5.src.commands;

import lab5.src.managers.CollectionManager;
import lab5.src.managers.ConsoleInput;
import lab5.src.models.StudyGroup;

/**
 * Команда для удаления из коллекции всех элементов, которые превышают заданный.
 * Сравнение производится на основе логики, заложенной в методе compareTo класса StudyGroup.
 * Для определения эталонного значения пользователю предлагается интерактивно создать объект группы.
 */
public class RemoveGreater extends Command {
    private final CollectionManager collectionManager;
    private final ConsoleInput consoleInput;

    /**
     * Конструктор, инициализирующий команду удаления превышающих элементов.
     * Устанавливает имя команды "remove_greater" и её описание.
     *
     * @param collectionManager менеджер коллекции, из которой будут удаляться элементы
     * @param consoleInput      объект для интерактивного создания объекта-шаблона для сравнения
     */
    public RemoveGreater(CollectionManager collectionManager, ConsoleInput consoleInput) {
        super(Commands.remove_greater, "удалить из коллекции все элементы, превышающие заданный");
        this.collectionManager = collectionManager;
        this.consoleInput = consoleInput;
    }

    /**
     * Выполняет логику команды.
     * Проверяет отсутствие аргументов в командной строке, запускает процесс создания
     * объекта для сравнения и удаляет из коллекции все элементы, которые больше созданного.
     *
     * @param args массив строковых аргументов, переданных с командой (ожидается пустой массив)
     * @throws IllegalArgumentException если команде переданы какие-либо дополнительные параметры
     */
    @Override
    public void execute(String[] args) {
        if (args.length > 0) {
            throw new IllegalArgumentException("Ошибка, команда remove_greater не принемает параметров");
        }
        System.out.println("Создание элемента шаблона для сравнения");

        StudyGroup groupToCompare = consoleInput.askStudyGroup();
        boolean isRemoved = collectionManager.removeGreater(groupToCompare);

        if (isRemoved) {
            System.out.println("Элементы, превышающие заданный удалены");
        } else {
            System.out.println("В коллекции не нашлось элементов, превышающих заданный");
        }
    }


}
