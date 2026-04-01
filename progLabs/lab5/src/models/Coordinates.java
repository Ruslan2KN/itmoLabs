package lab5.src.models;

/**
 * Хранит и управляет координатоми х, у
 * Реализует интерфейс {@link Validator} для проверки корректности данных
 */
public class Coordinates implements Validator {

    /**
     * Координата x
     * не может быть null
     */
    private Float x;

    /**
     * Координата y.
     * Максимальное значение поля 689. Не может быть null.
     */
    private Double y;

    /**
     * Конструктор по умолчанию.
     */
    public Coordinates() {
        super();
    }

    /**
     * Конструктор, создающий координаты с заданными значениями x и y.
     *
     * @param x координата x
     * @param y координата y
     */
    public Coordinates(Float x, Double y) {
        setX(x);
        setY(y);
    }

    /**
     * Устанавливает значение координаты x.
     *
     * @param x новое значение координаты x
     * @throws NullPointerException если передано значение null
     */
    public void setX(Float x) {
        if (x == null) {
            throw new NullPointerException("Х не может быть null.");
        }
        this.x = x;
    }

    /**
     * Устанавливает значение координаты y.
     *
     * @param y новое значение координаты y
     * @throws IllegalArgumentException если передано значение null или оно превышает 689
     */
    public void setY(Double y) {
        if (y == null || y > 689) {
            throw new IllegalArgumentException("Y не может быть null и максимальное значение только 689.");
        }
        this.y = y;
    }

    /**
     * Возвращает значение координаты X.
     *
     * @return текущее значение X
     */
    public Float getX() {
        return x;
    }

    /**
     * Возвращает значение координаты Y.
     *
     * @return текущее значение Y
     */
    public Double getY() {
        return y;
    }

    /**
     * Проверяет валидность полей объекта в соответствии с требованиями ТЗ.
     * * @throws IllegalArgumentException если поле x равно null,
     * либо поле y равно null или больше 689
     */
    @Override
    public void validate() throws IllegalArgumentException {
        if (x == null) {
            throw new IllegalArgumentException("Поле Coordinates, x не может быть null");
        }
        if (y == null || y > 689) {
            throw new IllegalArgumentException("Поле Coordinates, y не может быть null и максимальное значение только 689");
        }
    }

    /**
     * Возвращает строковое представление координат.
     *
     * @return строка в формате "[ x= ..., y=... ]"
     */
    @Override
    public String toString() {
        return "[" +
                " x= " + x +
                ", y=" + y +
                " ]";
    }

}
