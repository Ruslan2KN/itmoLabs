package lab5.src.commands;

import lab5.src.managers.CollectionManager;

/**
 * Команда для вывода всех элементов коллекции в порядке убывания.
 * Порядок определяется методом сравнения, реализованным в классе StudyGroup.
 */
public class PrintDescending extends Command{
    private final CollectionManager collectionManager;

    /**
     * Конструктор, инициализирующий команду вывода по убыванию.
     * Устанавливает имя команды "print_descending" и её описание.
     *
     * @param collectionManager менеджер коллекции, элементы которой необходимо отсортировать и вывести
     */
    public PrintDescending(CollectionManager collectionManager){
        super("print_descending", "вывести элементы коллекции в порядке убывания");
        this.collectionManager= collectionManager;
    }

    /**
     * Выполняет логику команды вывода по убыванию.
     * Проверяет отсутствие лишних аргументов и вызывает соответствующий метод в менеджере коллекции.
     *
     * @param args массив строковых аргументов, переданных с командой (ожидается пустой массив)
     * @throws IllegalArgumentException если команде переданы какие-либо дополнительные параметры
     */
    @Override
    public void execute(String[] args){
        if (args.length > 0){
            throw new IllegalArgumentException("Ошибка, команда print_descending не принимает параметры");
        }
        collectionManager.printDescending();
    }
}
