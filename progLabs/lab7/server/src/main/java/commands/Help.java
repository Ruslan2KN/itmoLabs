package commands;

import managers.CommandManager;
import network.Request;
import network.Response;

/**
 * Команда для получения справочной информации о доступных серверных командах.
 * Данная команда обращается к менеджеру команд, итерируется по всем зарегистрированным
 * в нем объектам и формирует форматированную строку с их именами и описаниями.
 */
public class Help extends Command {
    /**
     * Менеджер команд, предоставляющий список всех зарегистрированных команд.
     */
    private final CommandManager commandManager;

    /**
     * Конструктор, инициализирующий команду помощи.
     * Устанавливает системное имя команды help и описание для справки.
     *
     * @param commandManager Менеджер команд, из которого будет извлечен список доступных действий.
     */
    public Help(CommandManager commandManager) {
        super(Commands.help, "вывести справку по доступным командам");
        this.commandManager = commandManager;
    }

    /**
     * Выполняет логику формирования справочного сообщения.
     * Метод извлекает все команды из CommandManager, выравнивает их по столбцам
     * с помощью StringBuilder и возвращает итоговую строку в объекте ответа.
     *
     * @param request Объект сетевого запроса от клиента.
     * @return Объект ответа, содержащий полный список команд и их описаний.
     */
    @Override
    public Response execute(Request request) {
        StringBuilder sb = new StringBuilder("Доступные команды:\n");

        for (Command command : commandManager.getCommands().values()) {
            sb.append(String.format("%-38s : %s\n", command.getName(), command.getDescription()));
        }

        return new Response(sb.toString(), true, null);
    }
}