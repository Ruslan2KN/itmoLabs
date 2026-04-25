package lab5.src.managers;

import lab5.src.exceptions.EmptyCollectionException;
import lab5.src.models.StudyGroup;


import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Objects;

/**
 * Класс для управления коллекцией объектов StudyGroup.
 * Реализует бизнес-логику приложения по работе с данными: добавление, удаление,
 * сортировку, фильтрацию и предоставление информации об элементах коллекции.
 */
public class CollectionManager {
    private ArrayDeque<StudyGroup> collection = new ArrayDeque<>();
    private final LocalDateTime initializationDate = LocalDateTime.now();


    /**
     * Конструктор по умолчанию.
     * Создает пустую коллекцию и устанавливает время ее инициализации.
     */
    public CollectionManager() {
        super();
    }

    /**
     * Конструктор, инициализирующий менеджер уже существующей коллекцией.
     *
     * @param collection двусторонняя очередь (ArrayDeque) объектов StudyGroup
     * для инициализации менеджера. Если передано значение null,
     * будет использоваться пустая коллекция по умолчанию.
     */
    public CollectionManager(ArrayDeque<StudyGroup> collection) {
        if (collection != null) {
            this.collection = collection;
        }
    }

    /**
     * Добавляет новую учебную группу в коллекцию.
     *
     * @param group объект StudyGroup для добавления
     * @throws IllegalArgumentException если переданный объект равен null
     */
    public void add(StudyGroup group) {
        if (group == null) {
            throw new IllegalArgumentException("Элемент не может быть null");
        }
        collection.add(group);
    }

    /**
     * Полностью очищает коллекцию, удаляя все элементы.
     */
    public void clear() {
        collection.clear();
    }

    /**
     * Выводит в стандартный поток вывода информацию о коллекции:
     * тип коллекции, дату инициализации и текущее количество элементов.
     */
    public void info() {
        System.out.println("Тип коллекции: " + collection.getClass().getSimpleName());
        System.out.println("Даты инициализации коллекции: " + initializationDate);
        System.out.println("Количество элементов в коллекции: " + collection.size());
    }

    /**
     * Выводит в стандартный поток вывода строковое представление всех элементов коллекции.
     * Если коллекция пуста, выводит соответствующее сообщение.
     */
    public void show() {
        if (collection.isEmpty()) {
            System.out.println("Коллекция пуста");
        } else {
            for (StudyGroup g : collection) {
                System.out.println(g);
            }
        }
    }

    /**
     * Удаляет элемент из коллекции по его уникальному ID.
     *
     * @param id уникальный идентификатор группы для удаления
     * @return true, если элемент был успешно удален; false, если элемент с таким ID не найден
     */
    public boolean removeById(long id) {
        return collection.removeIf(group -> group.getId() == id);
    }

    /**
     * Ищет и возвращает объект учебной группы по ее ID.
     *
     * @param id уникальный идентификатор искомой группы
     * @return объект StudyGroup, если он найден, или null в противном случае
     */
    public StudyGroup getById(long id) {
        for (StudyGroup group : collection) {
            if (group.getId() == id) {
                return group;
            }
        }
        return null;
    }

    /**
     * Удаляет первый элемент из коллекции (голову очереди).
     *
     * @return true, если элемент был удален; false, если коллекция пуста
     */
    public boolean removeFirst() {
        if (collection.isEmpty()) {
            return false;
        }
        collection.removeFirst();
        return true;
    }

    /**
     * Подсчитывает количество групп в коллекции, у которых число студентов совпадает с заданным.
     *
     * @param count количество студентов для фильтрации
     * @return количество групп, удовлетворяющих условию
     */
    public long countByStudentsCount(int count) {
        return collection.stream()
                .filter(group -> group.getStudentsCount() == count)
                .count();
    }

    /**
     * Удаляет из коллекции все элементы, которые превышают заданный объект
     * (согласно логике сравнения метода compareTo в StudyGroup).
     *
     * @param groupToCompare объект для сравнения
     * @return true, если хотя бы один элемент был удален; иначе false
     */
    public boolean removeGreater(StudyGroup groupToCompare) {
        return collection.removeIf(group -> group.compareTo(groupToCompare) > 0);
    }

    /**
     * Удаляет из коллекции все элементы, которые меньше заданного объекта
     * (согласно логике сравнения метода compareTo в StudyGroup).
     *
     * @param groupToCompare объект для сравнения
     * @return true, если хотя бы один элемент был удален; иначе false
     */
    public boolean removeLower(StudyGroup groupToCompare) {
        return collection.removeIf(group -> group.compareTo(groupToCompare) < 0);
    }

    /**
     * Выводит элементы коллекции в порядке убывания.
     * Если коллекция пуста, выводит соответствующее сообщение.
     */
    public void printDescending() {
        if (collection.isEmpty()) {
            System.out.println("Коллекция пуста");
            return;
        }
        System.out.println("Элементы коллекции по убыванию");
        collection.stream().sorted((g1, g2) -> g2.compareTo(g1))
                .forEach(System.out::println);
    }

    /**
     * Выводит список администраторов всех групп в порядке убывания (сортировка по имени).
     * Группы без администратора (null) игнорируются.
     *
     * @throws EmptyCollectionException если коллекция пуста
     */
    public void printFieldDescendingGroupAdmin() {
        if (collection.isEmpty()) {
            throw new EmptyCollectionException("Коллекция пуста");

        }
        System.out.println(" Админы групп по убыванию");
        collection.stream()
                .map(StudyGroup::getGroupAdmin)
                .filter(Objects::nonNull)
                .sorted((admin1, admin2) -> admin2.getName().compareToIgnoreCase(admin1.getName()))
                .forEach(System.out::println);
    }

    /**
     * Возвращает текущую коллекцию учебных групп.
     *
     * @return двусторонняя очередь (ArrayDeque), содержащая все объекты StudyGroup
     */
    public ArrayDeque<StudyGroup> getCollection() {
        return collection;
    }


}
