package lab5.src.commands;

import lab5.src.managers.CollectionManager;

/**
 * Команда для подсчета элементов коллекции с заданным количеством студентов.
 * Ищет и выводит количество учебных групп, у которых значение поля studentsCount
 * строго совпадает с переданным числом.
 */
public class CountByStudentsCount extends Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор, инициализирующий команду.
     * Устанавливает имя команды "count_by_students_count" и её описание для справки.
     *
     * @param collectionManager менеджер коллекции, в котором будет производиться поиск и подсчет элементов
     */
    public CountByStudentsCount(CollectionManager collectionManager) {
        super("count_by_students_count", " вывести количество элементов, значения поля studentsCount которое равно заданному числу");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет логику команды подсчета.
     * Проверяет наличие ровно одного аргумента, преобразует его в целое число
     * и запрашивает у менеджера коллекции количество совпадений для вывода на экран.
     *
     * @param args массив строковых аргументов (ожидается ровно один аргумент — искомое количество студентов)
     * @throws IllegalArgumentException если передано неверное количество аргументов или переданный аргумент невозможно преобразовать в целое число
     */
    @Override
    public void execute(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException(" Ошибка, команда требует ровно один параметр");
        }
        try {
            int count = Integer.parseInt(args[0]);
            long result = collectionManager.countByStudentsCount(count);

            System.out.println("Найдено элементов с количеством студентов" + count + ": " + result);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ошибка, количество студентов должно быть целым числом");
        }
    }
}
