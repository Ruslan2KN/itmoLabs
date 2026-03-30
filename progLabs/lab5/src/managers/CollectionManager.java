package lab5.src.managers;

import lab5.src.exceptions.EmptyCollectionException;
import lab5.src.models.StudyGroup;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Arrays;

/**
 * Класс для управления коллекцией объектов StudyGroup.
 * Реализует всю основную бизнес-логику приложения: добавление, удаление, сортировку,
 * фильтрацию элементов, а также сохранение и загрузку коллекции из XML-файлов.
 */
public class CollectionManager {
    private ArrayDeque<StudyGroup> collection= new ArrayDeque<>();
    private LocalDateTime initializationDate=LocalDateTime.now();
    private final String nameOfFile="Study_groups";
    private XMLWriter xmlWriter= new XMLWriter();
    private XMLReader xmlReader=new XMLReader();

    /**
     * Конструктор по умолчанию.
     * При создании объекта автоматически пытается загрузить данные коллекции из файлов,
     * пути к которым указаны в переменной окружения.
     */
    public CollectionManager(){
        super();loadCollection();
    }

    /**
     * Конструктор, создающий коллекцию на основе переданного массива групп.
     *
     * @param collection массив объектов StudyGroup для инициализации коллекции
     * @throws IllegalArgumentException если переданный массив равен null
     */
    public CollectionManager(StudyGroup... collection){
        if (collection==null){ throw new IllegalArgumentException("Массив коллекции не может быть null");}
        this.collection.addAll(Arrays.asList(collection));
    }

    /**
     * Добавляет новую учебную группу в коллекцию.
     *
     * @param group объект StudyGroup для добавления
     * @throws IllegalArgumentException если переданный объект равен null
     */
    public void add(StudyGroup group){
        if (group==null ){ throw new IllegalArgumentException("Элемент не может быть null");}
        collection.add(group);
    }

    /**
     * Полностью очищает коллекцию, удаляя все элементы.
     */
    public void clear(){
        collection.clear();
    }

    /**
     * Выводит в стандартный поток вывода информацию о коллекции:
     * тип коллекции, дату инициализации и текущее количество элементов.
     */
    public void info(){
        System.out.println("Тип коллекции: " + collection.getClass().getSimpleName());
        System.out.println("Даты иницилизации коллекции: " + initializationDate);
        System.out.println("Количество элементов в коллекции: " + collection.size());
    }

    /**
     * Выводит в стандартный поток вывода строковое представление всех элементов коллекции.
     * Если коллекция пуста, выводит соответствующее сообщение.
     */
    public void show(){
        if (collection.isEmpty()){
            System.out.println("Коллекция пуста");}
        else { for (StudyGroup g : collection) {
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
    public boolean removeById(long id){
        return collection.removeIf(group -> group.getId() == id);
    }

    /**
     * Ищет и возвращает объект учебной группы по ее ID.
     *
     * @param id уникальный идентификатор искомой группы
     * @return объект StudyGroup, если он найден, или null в противном случае
     */
    public StudyGroup getById(long id){
        for (StudyGroup group : collection){
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
    public boolean removeFirst(){
        if (collection.isEmpty()){
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
    public long countByStudentsCount(int count){
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
    public boolean removeGreater (StudyGroup groupToCompare){
        return collection.removeIf(group -> group.compareTo(groupToCompare) > 0);
    }

    /**
     * Удаляет из коллекции все элементы, которые меньше заданного объекта
     * (согласно логике сравнения метода compareTo в StudyGroup).
     *
     * @param groupToCompare объект для сравнения
     * @return true, если хотя бы один элемент был удален; иначе false
     */
    public boolean removeLower (StudyGroup groupToCompare){
        return collection.removeIf(group -> group.compareTo(groupToCompare) < 0);
    }

    /**
     * Выводит элементы коллекции в порядке убывания.
     * Если коллекция пуста, выводит соответствующее сообщение.
     */
    public void printDescending(){
        if (collection.isEmpty()){
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
    public void printFieldDescendingGroupAdmin(){
        if (collection.isEmpty()){
            throw new EmptyCollectionException("Коллекция пуста");

        }
        System.out.println(" Админы групп по убыванию");
        collection.stream()
                .map(group -> group.getGroupAdmin())
                .filter(admin -> admin!=null)
                .sorted((admin1, admin2) -> admin2.getName().compareTo(admin1.getName()))
                .forEach(System.out:: println);
    }

    /**
     * Сохраняет текущее состояние коллекции в XML-файл.
     * Путь для сохранения берется из первого значения системной переменной окружения.
     *
     * @throws IllegalArgumentException если переменная окружения с путем сохранения не задана
     */
    public void save(){
        String filePath= System.getenv(nameOfFile);

        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Путь сохранения " + nameOfFile + " не указан.");
        }
        String ourFile =filePath.split(";")[0].trim();

        try {
            xmlWriter.writeCollection(collection, ourFile);
            System.out.println("Коллекция успешно сохранена в файл "+ ourFile);
        } catch (IllegalArgumentException | IOException e){
            System.out.println("Ошибка, при сохранении коллекции "+ e.getMessage() );
        }
    }

    /**
     * Приватный метод для первоначальной загрузки коллекции из XML-файлов при запуске программы.
     * Читает пути из переменной окружения (разделенные точкой с запятой).
     * Игнорирует элементы с дублирующимися ID и обновляет счетчик генерации ID в классе StudyGroup.
     */
    private void loadCollection() {
        String filePaths = System.getenv(nameOfFile);

        if (filePaths == null || filePaths.trim().isEmpty()) {
            System.out.println("Файл " + nameOfFile + " не указан.\nПрограмма запущена с пустой коллекцией");
            return;
        }
        String[] filePath = filePaths.split(";");
        for (String path : filePath) {
            try {
                ArrayDeque<StudyGroup> loadedGroup = xmlReader.readCollection(path.trim());
                for (StudyGroup newGroup : loadedGroup){
                    boolean isExist = false;
                    for ( StudyGroup existingGroup : this.collection){
                        if (existingGroup.getId()==newGroup.getId() ){
                            isExist =true;
                            break;
                        }
                    }
                    if (!isExist){
                        this.collection.add(newGroup);
                    } else {
                        System.out.println("Группа '"+newGroup.getName() + "' пропущена, так как элемент с id= "+newGroup.getId()+ " уже существует");
                    }
                }
                System.out.println("Файл "+ path.trim() + " успешно считан");
            } catch (FileNotFoundException e) {
                System.out.println("Ошибка при чтение файла "+path.trim()+": "+e.getMessage());

            }
        }
        long maxId = 0;
        for (StudyGroup group : collection) {
            if (group.getId() > maxId) {
                maxId = group.getId();
            }
        }
        StudyGroup.updateNextId(maxId);
        System.out.println("Загрузка элементов завершена, всего элементов в коллекции "+collection.size());
    }
}
