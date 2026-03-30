package lab5.src.commands;

import lab5.src.managers.CollectionManager;

/**
 * Команда для вывода значений поля groupAdmin всех элементов коллекции в порядке убывания.
 * Порядок убывания обычно определяется алфавитным порядком имен администраторов или иным критерием,
 * реализованным в методе сравнения менеджера коллекции.
 */
public class PrintFieldDescendingGroupAdmin extends Command{
    private final CollectionManager collectionManager;

    /**
     * Конструктор, инициализирующий команду вывода администраторов.
     * Устанавливает системное имя команды и её краткое описание.
     *
     * @param collectionManager менеджер коллекции, из которого будут извлечены и отсортированы данные об администраторах
     */
    public PrintFieldDescendingGroupAdmin(CollectionManager collectionManager){
        super("print_field_descending_group_admin", "вывести элементы поля groupAdmin в порядке убывания");
        this.collectionManager= collectionManager;
    }

    /**
     * Выполняет логику команды.
     * Проверяет, что аргументы отсутствуют, и делегирует выполнение задачи менеджеру коллекции.
     *
     * @param args массив строковых аргументов, переданных с командой (ожидается пустой массив)
     * @throws IllegalArgumentException если команде переданы какие-либо дополнительные параметры
     */
    @Override
    public void execute(String[] args){
        if (args.length > 0){
            throw new IllegalArgumentException("Ошибка, команда print_field_descending_group_admin не принимает параметры");
        }
        collectionManager.printFieldDescendingGroupAdmin();
    }

}
