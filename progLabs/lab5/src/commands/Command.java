package lab5.src.commands;

/**
 * Абстрактный базовый класс для всех команд в приложении.
 * Определяет общую структуру команды, включая ее уникальное имя, текстовое описание
 * и абстрактный метод для выполнения бизнес-логики.
 * Служит основой для реализации паттерна проектирования Command.
 */
public abstract class Command {
    private final String name;
    private final String description;

    /**
     * Конструктор для инициализации базовых свойств команды.
     *
     * @param name        строковое имя команды, по которому она будет вызываться из консоли
     * @param description краткое описание того, что делает команда (используется для команды help)
     */
    public Command(String name, String description){
        this.name=name;
        this.description=description;
    }

    public String getName() {
        return name;
    }

    /**
     * Возвращает описание команды.
     *
     * @return строка с описанием действия команды
     */
    public String getDescription() {
        return description;
    }

    /**
     * Абстрактный метод, содержащий основную логику выполнения команды.
     * Должен быть обязательно переопределен во всех классах-наследниках.
     *
     * @param args массив строковых аргументов, переданных пользователем вместе с командой
     * (может быть пустым, если команда не требует дополнительных параметров)
     */
    public abstract void execute(String[] args);
}
