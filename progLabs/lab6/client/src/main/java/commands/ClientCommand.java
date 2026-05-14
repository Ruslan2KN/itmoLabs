package commands;

/**
 * Абстрактный базовый класс для всех клиентских команд в приложении.
 * Этот класс определяет общую структуру команды, включая ее уникальное имя и описание.
 * Служит основой для реализации паттерна проектирования Command на стороне клиента.
 */
public abstract class ClientCommand {
    /**
     * Уникальный идентификатор команды из перечисления ClientCommands.
     */
    private final ClientCommands name;

    /**
     * Текстовое описание того, что делает данная команда.
     */
    private final String description;

    /**
     * Конструктор для инициализации базовых свойств клиентской команды.
     *
     * @param name        Название команды, соответствующее перечислению ClientCommands.
     * @param description Краткое описание функционала команды для вывода в справке.
     */
    public ClientCommand(ClientCommands name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Возвращает имя команды в строковом представлении.
     *
     * @return Строковое имя команды.
     */
    public String getName() {
        return name.name();
    }

    /**
     * Возвращает текстовое описание функционала команды.
     *
     * @return Строка с описанием действия команды.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Абстрактный метод, содержащий основную логику выполнения команды.
     * Этот метод должен быть обязательно реализован во всех конкретных классах-командах.
     *
     * @param args Массив строковых аргументов, переданных пользователем вместе с командой.
     */
    public abstract void execute(String[] args);
}