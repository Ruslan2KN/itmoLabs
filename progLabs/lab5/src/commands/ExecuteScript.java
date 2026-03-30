package lab5.src.commands;

import lab5.src.managers.CommandManager;
import lab5.src.managers.ConsoleInput;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Команда для чтения и исполнения скрипта из указанного файла.
 * Скрипт содержит последовательность команд в том же формате, что и при интерактивном вводе в консоли.
 * Реализует защиту от бесконечной рекурсии (зацикливания), если скрипт пытается вызвать сам себя,
 * а также временно перенаправляет поток ввода программы на чтение из файла.
 */
public class ExecuteScript extends Command {
    private final CommandManager commandManager;
    private final ConsoleInput consoleInput;

    /**
     * Хранит пути к файлам скриптов, которые выполняются в данный момент,
     * для обнаружения и предотвращения рекурсивных вызовов.
     */
    private final HashSet<String> activePath= new HashSet<>();

    /**
     * Конструктор, инициализирующий команду выполнения скрипта.
     * Устанавливает имя команды "execute_script" и её описание для справки.
     *
     * @param commandManager менеджер команд для делегирования выполнения считанных из файла команд
     * @param consoleInput   объект консольного ввода для временной подмены сканера на файловый
     */
    public ExecuteScript (CommandManager commandManager, ConsoleInput consoleInput ){
        super ( "execute_script", "считать и исполнить скрипт из указанного файла");
        this.commandManager=commandManager;
        this.consoleInput=consoleInput;
    }

    /**
     * Выполняет логику чтения и запуска скрипта.
     * Проверяет наличие пути к файлу, валидность файла и отсутствие рекурсии.
     * Временно заменяет системный сканер в ConsoleInput на сканер файла, поочередно считывает
     * и исполняет команды, после чего гарантированно возвращает исходный сканер обратно в блоке finally.
     *
     * @param args массив строковых аргументов (ожидается ровно один аргумент — путь к файлу скрипта)
     * @throws IllegalArgumentException если не передан путь к файлу, файл не найден/нет прав на чтение или обнаружена рекурсия
     */
    @Override
    public void execute(String[] args)  {
        if (args.length== 0){
            throw new IllegalArgumentException("Комманда "+getName()+ " требует параметр - имя файла");
        }
        String filePath= args[0];

        if (activePath.contains(filePath)){
            throw new IllegalArgumentException("Этот файл уже был загружен, скрипт "+filePath+ " вызывает сам себя");

        }
        File file= new File(filePath);
        if (!file.exists() || !file.canRead()){
            throw new IllegalArgumentException("Файл скрипта "+filePath+" не найден или недостаточно прав для чтения файла");
        }
        activePath.add(filePath);
        System.out.println("Выполнение скрипта "+filePath);

        Scanner oldScanner= consoleInput.getScanner();

        try (Scanner scriptScanner = new Scanner(file)){
            consoleInput.setScanner(scriptScanner);

            while (scriptScanner.hasNextLine()){
                String line = scriptScanner.nextLine().trim();
                if (line.isEmpty()){
                    continue;
                }
                String[] tokens = line.split("\\s+");
                String commandName = tokens [0];
                String[] commandsArgs= Arrays.copyOfRange(tokens,1, tokens.length);

                try {
                    commandManager.exeCommand(commandName, commandsArgs);
            } catch (IllegalArgumentException e){
                    System.out.println(e.getMessage());
                } catch (NoSuchElementException e){
                    System.out.println(e.getMessage());
                    break;
                }
        }
    } catch (FileNotFoundException e){
            System.out.println("Ошибка при чтение файла скрипта " +e.getMessage());
        } finally {
            consoleInput.setScanner(oldScanner);
            activePath.remove(filePath);
            System.out.println("Выполнение скрипта "+filePath+ " завершено");
        }
    }
}


