package lab5.src.commands;

import lab5.src.managers.CollectionManager;

/**
 * Команда для полного удаления всех элементов из коллекции.
 * После выполнения этой команды коллекция становится пустой.
 */
public class Clear extends Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор, инициализирующий команду очистки.
     * Устанавливает имя команды "clear" и её описание для справки.
     *
     * @param collectionManager менеджер коллекции, над которым будет выполнена операция очистки
     */
    public Clear(CollectionManager collectionManager){
        super("clear","очищает коллекцию");
        this.collectionManager=collectionManager;
    }

    /**
     * Выполняет логику команды очистки коллекции.
     * Проверяет, что команде не переданы дополнительные аргументы, после чего
     * вызывает метод очистки у менеджера коллекции и выводит сообщение об успехе.
     *
     * @param args массив строковых аргументов, переданных с командой (ожидается пустой массив)
     * @throws IllegalArgumentException если команде переданы какие-либо дополнительные параметры
     */
    @Override
    public void execute(String[] args){
        if (args.length > 0){
            throw new IllegalArgumentException("Команда add не принимает параметров.");
        }
        collectionManager.clear();
        System.out.println("Коллекция очищена");
    }
}
