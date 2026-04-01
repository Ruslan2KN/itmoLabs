package lab5.src.models;


import java.util.Objects;

/**
 * Класс, представляющий человека (например, администратора группы).
 * Содержит данные о физических параметрах, национальности и местоположении.
 * Реализует интерфейс {@link Validator} для проверки корректности полей.
 */
public class Person implements Validator {

    /**
     * Имя человека.
     * Поле не может быть null, строка не может быть пустой.
     */
    private String name;

    /**
     * Рост человека.
     * Значение поля должно быть больше 0. Поле не может быть null.
     */
    private Double height;

    /**
     * Цвет глаз человека.
     * Поле не может быть null.
     */
    private Color eyeColor;

    /**
     * Национальность (страна) человека.
     * Поле не может быть null.
     */
    private Country nationality;

    /**
     * Местоположение человека.
     * Поле может быть null.
     */
    private Location location;

    /**
     * Конструктор по умолчанию.
     */
    public Person() {
        super();
    }

    /**
     * Конструктор, создающий человека с заданными параметрами.
     *
     * @param name        имя (не может быть null или пустым)
     * @param height      рост (должен быть больше 0)
     * @param eyeColor    цвет глаз (не может быть null)
     * @param nationality национальность (не может быть null)
     * @param location    местоположение
     */
    public Person(String name, Double height, Color eyeColor, Country nationality, Location location) {
        setName(name);
        setHeight(height);
        setEyeColor(eyeColor);
        setNationality(nationality);
        this.location = location;
    }

    /**
     * Устанавливает цвет глаз человека.
     *
     * @param eyeColor новый цвет глаз
     * @throws IllegalArgumentException если передано значение null
     */
    public void setEyeColor(Color eyeColor) {
        if (eyeColor == null) {
            throw new IllegalArgumentException("Цвет глаз не может быть null.");
        }
        this.eyeColor = eyeColor;
    }

    /**
     * Устанавливает рост человека.
     *
     * @param height новый рост
     * @throws IllegalArgumentException если переданный рост меньше или равен 0
     */
    public void setHeight(Double height) {
        if (height != null && height <= 0) {
            throw new IllegalArgumentException("Рост должен быть больше 0.");
        }
        this.height = height;
    }

    /**
     * Устанавливает местоположение человека.
     *
     * @param location новое местоположение (может быть null)
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Устанавливает имя человека.
     *
     * @param name новое имя
     * @throws IllegalArgumentException если строка равна null или состоит только из пробелов
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть null или пустой строкой.");
        }
        this.name = name;
    }

    /**
     * Устанавливает национальность человека.
     *
     * @param nationality новая национальность
     * @throws IllegalArgumentException если передано значение null
     */
    public void setNationality(Country nationality) {
        if (nationality == null) {
            throw new IllegalArgumentException("Национальность не может быть null.");
        }
        this.nationality = nationality;
    }

    public String getName() {
        return name;
    }

    public Color getEyeColor() {
        return eyeColor;
    }

    public Country getNationality() {
        return nationality;
    }

    public Double getHeight() {
        return height;
    }

    public Location getLocation() {
        return location;
    }

    /**
     * Проверяет валидность полей объекта в соответствии с требованиями ТЗ.
     * Дополнительно вызывает валидацию для поля location, если оно не равно null.
     *
     * @throws IllegalArgumentException если имя null или пустое, рост null или {@code <=} 0,
     *                                  цвет глаз или национальность равны null
     */
    @Override
    public void validate() throws IllegalArgumentException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Поле Person, name не может быть null или пустым");
        }
        if (height == null || height <= 0) {
            throw new IllegalArgumentException("Поле Person, height не может быть null или меньше 1");
        }
        if (eyeColor == null) {
            throw new IllegalArgumentException("Поле Person, eyeColor не может быть null");
        }
        if (nationality == null) {
            throw new IllegalArgumentException("Поле Person, nationality не может быть null");
        }
        if (location != null) {
            location.validate();
        }
    }

    /**
     * Проверяет равенство данного объекта с другим объектом.
     *
     * @param o объект для сравнения
     * @return true, если объекты идентичны, иначе false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        return Objects.equals(name, person.name) &&
                Objects.equals(height, person.height) &&
                Objects.equals(eyeColor, person.eyeColor) &&
                Objects.equals(nationality, person.nationality) &&
                Objects.equals(location, person.location);
    }

    /**
     * Возвращает хэш-код объекта.
     *
     * @return числовое значение хэш-кода, основанное на полях объекта
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, height, eyeColor, nationality, location);
    }

    /**
     * Возвращает строковое представление данных человека.
     *
     * @return строка с перечислением всех полей объекта
     */
    @Override
    public String toString() {
        return " [ name= " + name + ", height= " + height + ", eyeColor= " +
                eyeColor + ", nationality= " + nationality + ", location= " + location + " ]";
    }
}