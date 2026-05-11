package src.managers;

import src.server.commands.Command;
import src.network.Request;
import src.network.Response;

import java.util.HashMap;

/**
 * Класс менеджер, отвечающий за управление серверными командами.
 * Данный класс реализует логику регистрации команд, их хранения в оперативной памяти
 * и централизованного исполнения на основе входящих сетевых запросов.
 */
public class CommandManager {
    /**
     * Словарь, где ключом является строковое имя команды, а значением — объект этой команды.
     */
    private final HashMap<String, Command> commands = new HashMap<>();

    /**
     * Регистрирует новый объект команды в системе.
     * После регистрации команда становится доступна для вызова клиентом по её имени.
     *
     * @param command Объект команды, реализующий абстрактный класс Command.
     */
    public void register(Command command) {
        commands.put(command.getName(), command);
    }

    /**
     * Предоставляет доступ ко всем зарегистрированным командам.
     * Чаще всего используется командой help для формирования списка доступных функций.
     *
     * @return Объект HashMap, содержащий все доступные серверные команды.
     */
    public HashMap<String, Command> getCommands() {
        return commands;
    }

    /**
     * Основной метод для обработки сетевого запроса и выполнения команды.
     * Метод выполняет поиск команды по имени (регистр не важен), проверяет
     * количество переданных аргументов на соответствие требованиям конкретной
     * команды и запускает её выполнение.
     * В случае отсутствия команды, неверного количества аргументов или
     * внутренних ошибок выполнения метод возвращает объект Response с описанием проблемы.
     *
     * @param request Объект сетевого запроса, содержащий имя команды, аргументы и прикрепленные данные.
     * @return Объект сетевого ответа с результатом выполнения или сообщением об ошибке.
     */
    public Response exeCommand(Request request) {
        Command command = commands.get(request.getCommandsName().toLowerCase());

        if (command == null) {
            return new Response("Команда '" + request.getCommandsName() + "' не найдена. Введите help для вывода доступных команд.", false, null);
        }

        int expectedArgs = command.getRequiredArgsCount();
        int actualArgs = (request.getArgs() == null) ? 0 : request.getArgs().length;

        if (expectedArgs != actualArgs) {
            return new Response("Ошибка, команда '" + command.getName() + "' требует ровно " + expectedArgs + " аргументов (передано: " + actualArgs + ").", false, null);
        }

        try {
            return command.execute(request);
        } catch (Exception e) {
            return new Response("Ошибка выполнения: " + e.getMessage(), false, null);
        }
    }
}