package lab5.src.commands;

import lab5.src.managers.CollectionManager;
import lab5.src.managers.FileManager;

/**
 * Команда для сохранения текущего состояния коллекции в файл.
 * Выступает связующим звеном: получает актуальные данные из менеджера коллекции
 * и передает их менеджеру файлов для записи на диск (в XML-файл).
 */
public class Save extends Command {
    private final CollectionManager collectionManager;
    private final FileManager fileManager;

    /**
     * Конструктор, инициализирующий команду сохранения.
     * Устанавливает имя команды "save" и её описание.
     *
     * @param collectionManager менеджер коллекции, предоставляющий данные (элементы) для сохранения
     * @param fileManager       менеджер файлов, отвечающий за физическую запись коллекции на диск
     */
    public Save(CollectionManager collectionManager, FileManager fileManager) {
        super(Commands.save, "сохранить коллекцию в файл");
        this.collectionManager = collectionManager;
        this.fileManager = fileManager;
    }

    /**
     * Выполняет логику сохранения коллекции.
     * Проверяет отсутствие лишних аргументов, после чего запрашивает коллекцию
     * у CollectionManager и передает её в FileManager для сохранения.
     *
     * @param args массив строковых аргументов, переданных вместе с командой (ожидается пустой массив)
     * @throws IllegalArgumentException если команде переданы какие-либо параметры
     */
    @Override
    public void execute(String[] args) {
        if (args.length > 0) {
            throw new IllegalArgumentException("Комманда " + getName() + " не принимает в себя параметров");
        }
        fileManager.saveCollection(collectionManager.getCollection());
    }
}
