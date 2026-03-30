package lab5.src.commands;

import lab5.src.managers.CollectionManager;
import lab5.src.managers.ConsoleInput;
import lab5.src.models.StudyGroup;

/**
 * Команда для удаления из коллекции всех элементов, которые меньше заданного.
 * Сравнение производится на основе логики, заложенной в методе compareTo класса StudyGroup.
 * Для определения эталонного значения пользователю предлагается интерактивно создать объект группы.
 */
public class RemoveLower extends Command{
    private final CollectionManager collectionManager;
    private final ConsoleInput consoleInput;

    /**
     * Конструктор, инициализирующий команду удаления меньших элементов.
     * Устанавливает имя команды "remove_lower" и её описание.
     *
     * @param collectionManager менеджер коллекции, из которой будут удаляться элементы
     * @param consoleInput   объект для интерактивного создания объекта-шаблона для сравнения
     */
    public RemoveLower(CollectionManager collectionManager, ConsoleInput consoleInput){
        super("remove_lower", "удалить из коллекции все элементы, меньшие заданного");
        this.collectionManager=collectionManager;
        this.consoleInput=consoleInput;
    }

    /**
     * Выполняет логику команды.
     * Проверяет отсутствие аргументов в командной строке, запускает процесс создания
     * объекта для сравнения и удаляет из коллекции все элементы, которые меньше созданного.
     *
     * @param args массив строковых аргументов, переданных с командой (ожидается пустой массив)
     * @throws IllegalArgumentException если команде переданы какие-либо дополнительные параметры
     */
    @Override
    public void execute(String[] args){
        if (args.length > 0){
            throw new IllegalArgumentException("Ошибка, команда remove_lower не принемает параметров");
        }
        System.out.println("Создание элемента шаблона для сравнения");

        StudyGroup groupToCompare= consoleInput.askStudyGroup();
        boolean isRemoved = collectionManager.removeLower(groupToCompare);

        if (isRemoved){
            System.out.println("Элементы, меньше заданного удалены");
        } else {
            System.out.println("В коллекции не нашлось элементов, меньших заданного");
        }
    }

}
