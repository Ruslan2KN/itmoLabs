package lab5.src.commands;

import lab5.src.managers.CollectionManager;

/**
 * Команда для сохранения текущего состояния коллекции в файл.
 * Вызывает механизм записи данных, который обычно использует путь из переменной окружения.
 */
public class Save extends Command{
    private final CollectionManager collectionManager;

    /**
     * Конструктор, инициализирующий команду сохранения.
     * Устанавливает имя команды "save" и её описание.
     *
     * @param collectionManager менеджер коллекции, метод сохранения которого будет вызван
     */
    public Save(CollectionManager collectionManager){
        super("save","сохранить коллекцию в файл");
        this.collectionManager=collectionManager;
    }

    /**
     * Выполняет логику сохранения коллекции.
     * Проверяет отсутствие аргументов и делегирует задачу менеджеру коллекции.
     *
     * @param args массив строковых аргументов, переданных с командой (ожидается пустой массив)
     * @throws IllegalArgumentException если команде переданы какие-либо параметры
     */
    @Override
    public void execute(String[] args){
        if (args.length>0){
            throw new IllegalArgumentException("Комманда "+getName()+ " не принимает в себя параметров");
        }
        collectionManager.save();
    }
}
