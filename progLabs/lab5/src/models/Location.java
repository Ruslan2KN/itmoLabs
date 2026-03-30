package lab5.src.models;

/**
 * Класс, представляющий локацию (местоположение) с координатами и названием.
 * Реализует интерфейс {@link Validator} для проверки корректности данных.
 */
public class Location implements Validator{

    /**
     * Координата X локации.
     */
    private double x;

    /**
     * Координата Y локации.
     * Значение этого поля не может быть null.
     */
    private Long y;

    /**
     * Название локации.
     * Строка не может быть пустой, значение этого поля не может быть null.
     */
    private String name;

    /**
     * Конструктор по умолчанию.
     */
    public Location(){
        super();
    }

    /**
     * Конструктор, создающий локацию с заданными координатами и названием.
     *
     * @param x    координата X
     * @param y    координата Y (не может быть null)
     * @param name название локации (не может быть null или пустым)
     */
    public Location( double x, Long y, String name){
        setX(x);
        setY(y);
        setName(name);
    }

    /**
     * Устанавливает значение координаты X.
     *
     * @param x новое значение координаты X
     */
    public void setX(double x){
        this.x=x;
    }

    /**
     * Устанавливает значение координаты Y.
     *
     * @param y новое значение координаты Y
     * @throws NullPointerException если передано значение null
     */
    public void setY(Long y){
        if (y== null){
            throw new NullPointerException("Y не может быть null.");
        }
        this.y=y;
    }

    /**
     * Устанавливает название локации.
     *
     * @param name новое название локации
     * @throws IllegalArgumentException если строка равна null или состоит только из пробелов
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()){
            throw new IllegalArgumentException("Имя не может быть null или быть пустой строкой.");
        }
        this.name=name;
    }


    public double getX() {
        return x;
    }

    public Long getY() {
        return y;
    }

    public String getName() {
        return name;
    }


    /**
     * Проверяет валидность полей объекта в соответствии с требованиями ТЗ.
     *
     * @throws IllegalArgumentException если поле y равно null,
     * либо поле name равно null или пустое
     */
    @Override
    public void validate() throws IllegalArgumentException {
        if (y== null){
            throw new IllegalArgumentException("Поле location, y не может быть null");
        }
        if (name == null || name.trim().isEmpty()){
            throw new IllegalArgumentException("Поле location, name не может быть null или пустым");
        }

    }

    /**
     * Возвращает строковое представление локации.
     *
     * @return строка в формате "[ x= ..., y=..., name='...' ]"
     */
    @Override
    public String toString(){
        return " ["+
                " x= "+x +
                ", y= "+y+
                ", name='"+name +"'"+
                " ]";
    }
}
