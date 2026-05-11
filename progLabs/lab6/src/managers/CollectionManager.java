package src.managers;

import src.exceptions.EmptyCollectionException;
import src.models.StudyGroup;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Класс менеджер, отвечающий за управление коллекцией учебных групп.
 * Содержит коллекцию и предоставляет методы для работы с ней, включая
 * добавление, удаление, поиск, сортировку и генерацию идентификаторов.
 */
public class CollectionManager {
    /**
     * Коллекция, в которой хранятся объекты учебных групп.
     * Используется тип ArrayDeque для обеспечения работы в формате двусторонней очереди.
     */
    private ArrayDeque<StudyGroup> collection = new ArrayDeque<>();

    /**
     * Дата и время инициализации объекта менеджера коллекции.
     */
    private final LocalDateTime initializationDate = LocalDateTime.now();


    /**
     * Конструктор для создания нового пустого менеджера коллекции.
     * Устанавливает начальную дату инициализации.
     */
    public CollectionManager() {
        super();
    }

    /**
     * Конструктор для создания менеджера с уже существующей коллекцией.
     *
     * @param collection Коллекция StudyGroup для начальной инициализации.
     * Если передано значение null, будет создана пустая коллекция.
     */
    public CollectionManager(ArrayDeque<StudyGroup> collection) {
        if (collection != null) {
            this.collection = collection;
        }
    }

    /**
     * Добавляет в коллекцию уже готовый объект группы.
     * Используется при загрузке данных или обновлении существующих элементов.
     *
     * @param group Объект учебной группы для добавления.
     * @throws IllegalArgumentException Если передано значение null.
     */
    public void addUpdatedGroup(StudyGroup group) {
        if (group == null) {
            throw new IllegalArgumentException("Элемент не может быть null");
        }
        collection.add(group);
    }

    /**
     * Удаляет все элементы из текущей коллекции.
     */
    public void clear() {
        collection.clear();
    }

    /**
     * Выводит информацию о коллекции в стандартный поток вывода.
     * Включает тип коллекции, дату инициализации и общее количество элементов.
     */
    public void info() {
        System.out.println("Тип коллекции: " + collection.getClass().getSimpleName());
        System.out.println("Даты инициализации коллекции: " + initializationDate);
        System.out.println("Количество элементов в коллекции: " + collection.size());
    }

    /**
     * Выводит все элементы коллекции в их строковом представлении.
     * Если элементов нет, выводит сообщение о том, что коллекция пуста.
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
     * Пытается удалить элемент из коллекции, сравнивая его ID.
     *
     * @param id Уникальный номер удаляемой группы.
     * @return true, если элемент был найден и удален, иначе false.
     */
    public boolean removeById(long id) {
        return collection.removeIf(group -> group.getId() == id);
    }

    /**
     * Ищет элемент в коллекции по его идентификатору.
     *
     * @param id Идентификатор искомой учебной группы.
     * @return Найденный объект StudyGroup или null, если ничего не найдено.
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
     * Удаляет самый первый элемент коллекции.
     *
     * @throws EmptyCollectionException Если в коллекции нет элементов для удаления.
     */
    public void removeFirst() {
        if (collection.isEmpty()) {
            throw new EmptyCollectionException("Коллекция пуста, удалять нечего");
        }
        collection.removeFirst();
    }

    /**
     * Считает количество элементов, поле studentsCount которых равно заданному значению.
     *
     * @param count Число студентов для фильтрации.
     * @return Количество найденных элементов.
     */
    public long countByStudentsCount(int count) {
        return collection.stream()
                .filter(group -> group.getStudentsCount() == count)
                .count();
    }

    /**
     * Удаляет из коллекции все элементы, которые больше переданного.
     * Сравнение происходит на основе логики метода compareTo класса StudyGroup.
     *
     * @param groupToCompare Группа для сравнения.
     * @return true, если хотя бы один элемент был удален.
     */
    public boolean removeGreater(StudyGroup groupToCompare) {
        return collection.removeIf(group -> group.compareTo(groupToCompare) > 0);
    }

    /**
     * Удаляет из коллекции все элементы, которые меньше переданного.
     * Сравнение происходит на основе логики метода compareTo класса StudyGroup.
     *
     * @param groupToCompare Группа для сравнения.
     * @return true, если хотя бы один элемент был удален.
     */
    public boolean removeLower(StudyGroup groupToCompare) {
        return collection.removeIf(group -> group.compareTo(groupToCompare) < 0);
    }

    /**
     * Возвращает список элементов коллекции, отсортированный по убыванию.
     * Порядок определяется методом compareTo класса StudyGroup.
     *
     * @return отсортированный список объектов StudyGroup.
     */
    public List<StudyGroup> getDescending() {
        return collection.stream()
                .sorted((g1, g2) -> g2.compareTo(g1))
                .collect(Collectors.toList());
    }

    /**
     * Возвращает строковое представление администраторов всех групп,
     * отсортированное в порядке убывания (по имени).
     * Группы без администратора (null) игнорируются.
     *
     * @return отформатированная строка со списком администраторов.
     * @throws EmptyCollectionException если коллекция пуста.
     */
    public String getFieldDescendingGroupAdmin() {
        if (collection.isEmpty()) {
            throw new EmptyCollectionException("Коллекция пуста");
        }

        String result = collection.stream()
                .map(StudyGroup::getGroupAdmin)
                .filter(Objects::nonNull)
                .sorted((admin1, admin2) -> admin2.getName().compareToIgnoreCase(admin1.getName()))
                .map(Object::toString)
                .collect(Collectors.joining("\n"));

        return result.isEmpty() ? "В текущих группах нет назначенных администраторов." : result;
    }

    /**
     * Предоставляет доступ к объекту коллекции.
     *
     * @return Объект ArrayDeque с текущими элементами.
     */
    public ArrayDeque<StudyGroup> getCollection() {
        return collection;
    }

    /**
     * Генерирует новый уникальный идентификатор для элемента коллекции.
     * Ищет максимальный текущий ID и прибавляет к нему единицу.
     *
     * @return Новый свободный идентификатор.
     */
    public long generateNextId(){
        if (collection.isEmpty()){
            return 1L;
        }
        return collection.stream()
                .mapToLong(StudyGroup::getId)
                .max().orElse(0L)+1;
    }

    /**
     * Подготавливает и добавляет новую группу в коллекцию.
     * Автоматически присваивает группе уникальный ID и дату создания.
     *
     * @param group Объект группы для добавления.
     * @throws IllegalArgumentException Если переданное значение равно null.
     */
    public void addNewGroup(StudyGroup group){
        if (group == null){
            throw new IllegalArgumentException("Элемнт не может быть null");
        }
        group.setId(generateNextId());
        group.setCreationDate(LocalDateTime.now());
        collection.add(group);
    }


}