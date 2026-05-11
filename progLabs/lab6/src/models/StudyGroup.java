package src.models;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Главный класс, представляющий учебную группу.
 * Является основным элементом коллекции, которой управляет программа.
 * Реализует интерфейс Comparable для сортировки по умолчанию по количеству студентов
 * и интерфейс Validator для проверки целостности данных.
 */
public class StudyGroup implements Comparable<StudyGroup>, Validator, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Уникальный идентификатор группы.
     * Значение поля должно быть больше 0, значение должно быть уникальным.
     * Генерируется автоматически менеджером коллекции.
     */
    private long id;

    /**
     * Имя учебной группы.
     * Поле не может быть null, строка не может быть пустой.
     */
    private String name;

    /**
     * Координаты местоположения группы.
     * Поле не может быть null.
     */
    private Coordinates coordinates;

    /**
     * Дата и время создания объекта.
     * Поле не может быть null, значение генерируется автоматически при создании.
     */
    private LocalDateTime creationDate;

    /**
     * Количество студентов в учебной группе.
     * Значение поля должно быть больше 0.
     */
    private int studentsCount;

    /**
     * Количество студентов, переведенных в группу из других заведений.
     * Значение поля должно быть больше 0, может принимать значение null.
     */
    private Integer transferredStudents;

    /**
     * Средний балл студентов группы.
     * Значение поля должно быть больше 0, может принимать значение null.
     */
    private Double averageMark;

    /**
     * Форма обучения в данной группе.
     * Может принимать значение null.
     */
    private FormOfEducation formOfEducation;

    /**
     * Администратор (ответственное лицо) учебной группы.
     * Может принимать значение null.
     */
    private Person groupAdmin;

    /**
     * Конструктор по умолчанию.
     * Используется для создания пустого объекта с последующим заполнением полей.
     */
    public StudyGroup() {
        super();
    }

    /**
     * Конструктор с параметрами для создания учебной группы.
     * Идентификатор и дата создания устанавливаются отдельно менеджером коллекции.
     *
     * @param name                имя группы
     * @param coordinates         объект координат
     * @param studentsCount       количество студентов
     * @param averageMark         средний балл группы
     * @param formOfEducation     форма обучения
     * @param groupAdmin          объект администратора группы
     * @param transferredStudents количество переведенных студентов
     */
    public StudyGroup(String name, Coordinates coordinates, int studentsCount, Double averageMark, FormOfEducation formOfEducation, Person groupAdmin, Integer transferredStudents) {
        setName(name);
        setCoordinates(coordinates);
        setStudentsCount(studentsCount);
        setAverageMark(averageMark);
        this.formOfEducation = formOfEducation;
        this.groupAdmin = groupAdmin;
        setTransferredStudents(transferredStudents);
    }

    /**
     * Устанавливает имя учебной группы.
     *
     * @param name новое имя группы
     * @throws IllegalArgumentException если имя равно null или является пустой строкой
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя группы не может быть null или пустым.");
        }
        this.name = name;
    }

    /**
     * Возвращает имя учебной группы.
     *
     * @return имя группы
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает объект координат группы.
     *
     * @return координаты группы
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Возвращает средний балл группы.
     *
     * @return средний балл (может быть null)
     */
    public Double getAverageMark() {
        return averageMark;
    }

    /**
     * Возвращает форму обучения в группе.
     *
     * @return форма обучения (может быть null)
     */
    public FormOfEducation getFormOfEducation() {
        return formOfEducation;
    }

    /**
     * Возвращает общее количество студентов в группе.
     *
     * @return количество студентов
     */
    public int getStudentsCount() {
        return studentsCount;
    }

    /**
     * Возвращает количество переведенных студентов.
     *
     * @return количество переведенных студентов (может быть null)
     */
    public Integer getTransferredStudents() {
        return transferredStudents;
    }

    /**
     * Возвращает уникальный идентификатор группы.
     *
     * @return ID группы
     */
    public long getId() {
        return id;
    }

    /**
     * Возвращает объект администратора группы.
     *
     * @return администратор группы (может быть null)
     */
    public Person getGroupAdmin() {
        return groupAdmin;
    }

    /**
     * Возвращает дату и время создания объекта.
     *
     * @return дата создания
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Устанавливает средний балл учебной группы.
     *
     * @param averageMark новый средний балл
     * @throws IllegalArgumentException если балл не равен null и меньше или равен 0
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
     * @param coordinates новый объект координат
     * @throws IllegalArgumentException если передано значение null
     */
    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Координаты не могут быть null.");
        }
        this.coordinates = coordinates;
    }

    /**
     * Устанавливает форму обучения в группе.
     *
     * @param formOfEducation новая форма обучения (может быть null)
     */
    public void setFormOfEducation(FormOfEducation formOfEducation) {
        this.formOfEducation = formOfEducation;
    }

    /**
     * Устанавливает администратора учебной группы.
     *
     * @param groupAdmin новый объект администратора (может быть null)
     */
    public void setGroupAdmin(Person groupAdmin) {
        this.groupAdmin = groupAdmin;
    }

    /**
     * Устанавливает количество студентов в группе.
     *
     * @param studentsCount новое количество студентов
     * @throws IllegalArgumentException если значение меньше или равно 0
     */
    public void setStudentsCount(int studentsCount) {
        if (studentsCount <= 0) {
            throw new IllegalArgumentException("Количество студентов должно быть больше 0.");
        }
        this.studentsCount = studentsCount;
    }

    /**
     * Устанавливает количество переведенных студентов.
     *
     * @param transferredStudents новое количество переведенных студентов
     * @throws IllegalArgumentException если значение не равно null и меньше или равно 0
     */
    public void setTransferredStudents(Integer transferredStudents) {
        if (transferredStudents != null && transferredStudents <= 0) {
            throw new IllegalArgumentException("Количество переведенных студентов должно быть больше 0.");
        }
        this.transferredStudents = transferredStudents;
    }

    /**
     * Устанавливает идентификатор группы вручную.
     * Обычно используется при загрузке данных из файла сохранения.
     *
     * @param id новый уникальный идентификатор
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Устанавливает дату создания объекта вручную.
     * Обычно используется при восстановлении коллекции из файла.
     *
     * @param creationDate объект даты и времени создания
     */
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Проверяет корректность заполнения всех полей объекта согласно ТЗ.
     * Также запускает валидацию вложенных объектов координат и администратора.
     *
     * @throws IllegalArgumentException если какое-либо поле нарушает установленные ограничения
     */
    @Override
    public void validate() throws IllegalArgumentException {
        if (id <= 0) {
            throw new IllegalArgumentException("Поле StudyGroup, id должно быть больше 0");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Поле StudyGroup, name не может быть null или пустым.");
        }
        if (coordinates == null) {
            throw new IllegalArgumentException("Поле StudyGroup, coordinates не может быть null.");
        }
        coordinates.validate();

        if (creationDate == null) {
            throw new IllegalArgumentException("Поле StudyGroup, creationDate не может быть null.");
        }
        if (studentsCount <= 0) {
            throw new IllegalArgumentException("Поле StudyGroup, studentsCount не может быть меньше 1.");
        }
        if (transferredStudents != null && transferredStudents <= 0) {
            throw new IllegalArgumentException("Поле StudyGroup, transferredStudents не может быть меньше 1.");
        }
        if (averageMark != null && averageMark <= 0) {
            throw new IllegalArgumentException("Поле StudyGroup, averageMark не может быть меньше 1.");
        }
        if (groupAdmin != null) {
            groupAdmin.validate();
        }
    }

    /**
     * Сравнивает текущую группу с другой на основе количества студентов.
     * Используется для сортировки коллекции по умолчанию.
     *
     * @param other объект для сравнения
     * @return отрицательное целое число, ноль или положительное число, если количество студентов
     * данной группы меньше, равно или больше чем у сравниваемой соответственно.
     */
    @Override
    public int compareTo(StudyGroup other) {
        if (other == null) return 1;
        return Integer.compare(this.studentsCount, other.studentsCount);
    }

    /**
     * Проверяет равенство текущей группы с другим объектом.
     * Две группы считаются равными, если их уникальные идентификаторы (id) совпадают.
     *
     * @param o объект для сравнения
     * @return true если объекты равны, иначе false
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
        return id == studyGroup.id;
    }

    /**
     * Возвращает хэш-код объекта учебной группы.
     * Хэш-код генерируется на основе уникального идентификатора.
     *
     * @return значение хэш-кода
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Возвращает подробное строковое представление объекта учебной группы.
     * Включает в себя значения всех полей, включая вложенные объекты.
     *
     * @return строка с данными о группе
     */
    @Override
    public String toString() {
        return "StudyGroup [ id= " + id + ", name= " + name + ", coordinates= " + coordinates +
                ", creationDate= " + creationDate + ", studentsCount= " + studentsCount +
                ", transferredStudents= " + transferredStudents + ", averageMark= " + averageMark +
                ", formOfEducation= " + formOfEducation + ", groupAdmin=" + groupAdmin + " ]";
    }
}