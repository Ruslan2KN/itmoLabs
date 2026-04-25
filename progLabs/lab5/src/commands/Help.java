package lab5.src.commands;

import lab5.src.managers.CommandManager;

/**
 * Команда для вывода справочной информации по всем доступным командам приложения.
 * Извлекает список всех зарегистрированных команд из менеджера и выводит их названия вместе с описаниями.
 */
public class Help extends Command {
    private final CommandManager commandManager;

    /**
     * Конструктор, инициализирующий команду справки.
     * Устанавливает имя команды "help" и её описание.
     *
     * @param commandManager менеджер команд, из которого будет получен список всех доступных действий
     */
    public Help(CommandManager commandManager) {
        super(Commands.help, "вывести справку по допступным командам");
        this.commandManager = commandManager;
    }

    /**
     * Выполняет логику вывода справки.
     * Проверяет отсутствие лишних аргументов и выводит в консоль список команд,
     * отформатированный в виде таблицы для удобства чтения.
     *
     * @param args массив строковых аргументов, переданных с командой (ожидается пустой массив)
     * @throws IllegalArgumentException если команде переданы какие-либо дополнительные параметры
     */
    @Override
    public void execute(String[] args) {
        if (args.length > 0) {
            throw new IllegalArgumentException("Команда help не приминаем параметров.");
        }
        System.out.println("Доступные команды");
        for (Command command : commandManager.getCommands().values()) {
            System.out.printf("%-38s : %s%n", command.getName(), command.getDescription());
        }
    }

}
