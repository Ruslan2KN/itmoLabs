package lab5.src.models;

import java.time.LocalDateTime;
import java.lang.Object;
import java.util.Objects;

/**
 * Главный класс, представляющий учебную группу.
 * Является основным элементом коллекции, которой управляет программа.
 * Реализует интерфейс {@link Comparable} для сортировки по умолчанию (по количеству студентов)
 * и интерфейс {@link Validator} для проверки целостности данных.
 */
public class StudyGroup implements Comparable<StudyGroup>, Validator {

    /**
     * Уникальный идентификатор группы.
     * Значение поля должно быть больше 0, значение должно быть уникальным,
     * генерируется автоматически.
     */
    private long id;

    /**
     * Имя группы.
     * Поле не может быть null, строка не может быть пустой.
     */
    private String name;

    /**
     * Координаты группы.
     * Поле не может быть null.
     */
    private Coordinates coordinates;

    /**
     * Дата и время создания объекта.
     * Поле не может быть null, значение генерируется автоматически.
     */
    private LocalDateTime creationDate;

    /**
     * Количество студентов в группе.
     * Значение поля должно быть больше 0.
     */
    private int studentsCount;

    /**
     * Количество переведенных студентов.
     * Значение поля должно быть больше 0, может быть null.
     */
    private Integer transferredStudents;

    /**
     * Средний балл группы.
     * Значение поля должно быть больше 0, может быть null.
     */
    private Double averageMark;

    /**
     * Форма обучения.
     * Может быть null.
     */
    private FormOfEducation formOfEducation;

    /**
     * Администратор группы.
     * Может быть null.
     */
    private Person groupAdmin;

    /**
     * Статическое поле для генерации уникальных ID интерактивно.
     */
    private static Long nextId = 1L;

    /**
     * Конструктор по умолчанию.
     * Автоматически генерирует уникальный id и устанавливает текущую дату создания.
     */
    public StudyGroup() {
        super();
        this.id = nextId++;
        this.creationDate = LocalDateTime.now();
    }

    /**
     * Конструктор с параметрами для создания учебной группы.
     * ID и дата создания генерируются автоматически.
     *
     * @param name                имя группы
     * @param coordinates         координаты
     * @param studentsCount       количество студентов
     * @param averageMark         средний балл
     * @param formOfEducation     форма обучения
     * @param groupAdmin          администратор группы
     * @param transferredStudents количество переведенных студентов
     */
    public StudyGroup(String name, Coordinates coordinates, int studentsCount, Double averageMark, FormOfEducation formOfEducation, Person groupAdmin, int transferredStudents) {
        setName(name);
        setCoordinates(coordinates);
        setStudentsCount(studentsCount);
        setAverageMark(averageMark);
        this.formOfEducation = formOfEducation;
        this.groupAdmin = groupAdmin;
        this.id = nextId++;
        this.creationDate = LocalDateTime.now();
        setTransferredStudents(transferredStudents);

    }

    /**
     * Устанавливает имя группы.
     *
     * @param name новое имя
     * @throws IllegalArgumentException если имя равно null или состоит из пробелов
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя группы не может быть null или пустым. ");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Double getAverageMark() {
        return averageMark;
    }

    public FormOfEducation getFormOfEducation() {
        return formOfEducation;
    }

    public int getStudentsCount() {
        return studentsCount;
    }

    public Integer getTransferredStudents() {
        return transferredStudents;
    }

    public long getId() {
        return id;
    }

    public Person getGroupAdmin() {
        return groupAdmin;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Устанавливает средний балл группы.
     *
     * @param averageMark новый средний балл
     * @throws IllegalArgumentException если балл меньше или равен 0
     */
    public void setAverageMark(Double averageMark) {
        if (averageMark != null && averageMark <= 0) {
            throw new IllegalArgumentException("Средний балл должен быть больше 0.");
        }
        this.averageMark = averageMark;
    }

    /**
     * Устанавливает координаты группы.
     *
     * @param coordinates новые координаты
     * @throws IllegalArgumentException если передан null
     */
    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Координаты не могут быть null.");
        }
        this.coordinates = coordinates;
    }

    /**
     * Устанавливает форму обучения.
     *
     * @param formOfEducation новая форма обучения (может быть null)
     */
    public void setFormOfEducation(FormOfEducation formOfEducation) {
        this.formOfEducation = formOfEducation;
    }

    /**
     * Устанавливает администратора группы.
     *
     * @param groupAdmin новый администратор (может быть null)
     */
    public void setGroupAdmin(Person groupAdmin) {
        this.groupAdmin = groupAdmin;
    }

    /**
     * Устанавливает количество студентов.
     *
     * @param studentsCount новое количество студентов
     * @throws IllegalArgumentException если количество меньше или равно 0
     */
    public void setStudentsCount(int studentsCount) {
        if (studentsCount <= 0) {
            throw new IllegalArgumentException("Количество студентов должно быть больше 0. ");
        }
        this.studentsCount = studentsCount;
    }

    /**
     * Устанавливает количество переведенных студентов.
     *
     * @param transferredStudents новое количество переведенных студентов
     * @throws IllegalArgumentException если количество меньше или равно 0
     */
    public void setTransferredStudents(Integer transferredStudents) {
        if (transferredStudents != null && transferredStudents <= 0) {
            throw new IllegalArgumentException("Количество переведенных студентов должно быть больше 0.");
        }
        this.transferredStudents = transferredStudents;
    }

    /**
     * Устанавливает ID группы вручную (используется при загрузке из файла).
     *
     * @param id новый ID
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Устанавливает дату создания вручную (используется при загрузке из файла).
     *
     * @param creationDate новая дата создания
     */
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Обновляет счетчик генерации ID.
     * Вызывается после загрузки коллекции из файла, чтобы избежать дублирования ID.
     *
     * @param maxId максимальный ID, найденный в загруженном файле
     */
    public static void updateNextId(long maxId) {
        if (maxId >= nextId) {
            nextId = maxId + 1;
        }
    }

    /**
     * Проверяет валидность всех полей объекта.
     * Также вызывает валидацию вложенных объектов (coordinates, groupAdmin).
     *
     * @throws IllegalArgumentException при нарушении ограничений любого из полей
     */
    @Override
    public void validate() throws IllegalArgumentException {
        if (id <= 0) {
            throw new IllegalArgumentException("Поле StudyGroup, id должно быть больше 0");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Поле StudyGroup, name не может быть null или пустым. ");
        }
        if (coordinates == null) {
            throw new IllegalArgumentException("Поле StudyGroup, coordinates не может быть null. ");
        }
        coordinates.validate();

        if (creationDate == null) {
            throw new IllegalArgumentException("Поле StudyGroup, creationDate не может быть null. ");

        }
        if (studentsCount <= 0) {
            throw new IllegalArgumentException("Поле StudyGroup, studentsCount не может быть меньше 1. ");
        }
        if (transferredStudents != null && transferredStudents <= 0) {
            throw new IllegalArgumentException("Поле StudyGroup, transferredStudents не может быть  меньше 1. ");

        }
        if (averageMark != null && averageMark <= 0) {
            throw new IllegalArgumentException("Поле StudyGroup, averageMark не может быть  меньше 1. ");

        }
        if (groupAdmin != null) {
            groupAdmin.validate();
        }

    }

    /**
     * Сравнивает данную группу с другой для сортировки.
     * По умолчанию сортировка производится по количеству студентов (studentsCount).
     *
     * @param other другая группа для сравнения
     * @return результат сравнения количеств студентов
     */
    @Override
    public int compareTo(StudyGroup other) {
        if (other == null) return 1;
        return Integer.compare(this.studentsCount, other.studentsCount);
    }

    /**
     * Сравнивает текущую группу с другим объектом на равенство.
     * Равенство групп определяется исключительно по их уникальному ID.
     *
     * @param o объект для сравнения
     * @return true, если ID совпадают, иначе false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StudyGroup studyGroup = (StudyGroup) o;
        return Objects.equals(id, studyGroup.id);
    }

    /**
     * Возвращает хэш-код группы на основе её ID.
     *
     * @return хэш-код
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Возвращает строковое представление всех данных группы.
     *
     * @return строка с перечислением значений всех полей
     */
    @Override
    public String toString() {
        return "StudyGroup [ id= " + id + ", name= " + name + ", coordinates= " + coordinates +
                ", creationDate= " + creationDate + ", studentsCount= " + studentsCount +
                ", transferredStudents= " + transferredStudents + ", averageMark= " + averageMark +
                ", formOfEducation= " + formOfEducation + ", groupAdmin=" + groupAdmin + " ]";
    }

}
