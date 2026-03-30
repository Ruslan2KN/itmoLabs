package lab5.src.commands;

import lab5.src.managers.CollectionManager;

/**
 * Команда для удаления первого элемента коллекции.
 * Поскольку в качестве коллекции используется очередь (ArrayDeque), команда удаляет
 * элемент, находящийся в самом начале (голову очереди).
 */
public class RemoveFirst extends Command{
    private final CollectionManager collectionManager;

    /**
     * Конструктор, инициализирующий команду удаления первого элемента.
     * Устанавливает имя команды "remove_first" и её описание.
     *
     * @param collectionManager менеджер коллекции, из которой будет удален первый элемент
     */
    public RemoveFirst(CollectionManager collectionManager){
        super("remove_first", "удалить первый элемент из коллекции");
        this.collectionManager=collectionManager;
    }

    /**
     * Выполняет логику удаления первого элемента.
     * Проверяет отсутствие лишних аргументов и запрашивает удаление у менеджера коллекции.
     * Если коллекция пуста, выводит информационное сообщение об этом.
     *
     * @param args массив строковых аргументов, переданных с командой (ожидается пустой массив)
     * @throws IllegalArgumentException если команде переданы какие-либо дополнительные параметры
     */
    @Override
    public void execute(String[] args){
        if (args.length >0){
            throw new IllegalArgumentException("Ошибка, команда remove_first не принимает параметры");
        }
        boolean success = collectionManager.removeFirst();

        if(success){
            System.out.println("Первый элемент удален из коллекции");
        } else {
            System.out.println("Коллекция пуста, удалять нечего");
        }
    }
}
