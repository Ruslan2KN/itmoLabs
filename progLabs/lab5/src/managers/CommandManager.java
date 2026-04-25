package lab5.src.managers;

import java.util.HashMap;

import lab5.src.commands.Command;
import lab5.src.exceptions.CommandNotFoundException;

/**
 * Класс для управления командами приложения.
 * Отвечает за регистрацию доступных команд, их хранение и делегирование выполнения
 * конкретному объекту команды по ее строковому имени.
 */
public class CommandManager {
    private final HashMap<String, Command> commands = new HashMap<>();

    /**
     * Регистрирует новую команду в менеджере.
     * Команда сохраняется в коллекцию, где ключом выступает ее имя.
     *
     * @param command объект команды, который необходимо зарегистрировать
     */
    public void register(Command command) {
        commands.put(command.getName(), command);
    }

    /**
     * Возвращает коллекцию всех зарегистрированных команд.
     * Используется для команд вроде "help", которым нужен доступ к списку всех доступных действий.
     *
     * @return словарь HashMap, где ключ — имя команды, а значение — объект команды
     */
    public HashMap<String, Command> getCommands() {
        return commands;
    }

    /**
     * Ищет команду по имени и запускает ее выполнение с переданными аргументами.
     * Имя команды автоматически приводится к нижнему регистру перед поиском.
     *
     * @param commandName имя вызываемой команды
     * @param args        массив строковых аргументов, передаваемых команде для выполнения
     * @throws CommandNotFoundException если команда с таким именем не найдена в словаре зарегистрированных команд
     */
    public void exeCommand(String commandName, String[] args) {
        Command command = commands.get(commandName.toLowerCase());
        if (command == null) {
            throw new CommandNotFoundException("Команда '" + commandName + "' не найдена. Введите help что бы посмотреть весь перечень комманд");
        }
        command.execute(args);

    }
}
