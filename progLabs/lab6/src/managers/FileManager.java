package src.managers;

import src.exceptions.FileSaveException;
import src.models.StudyGroup;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayDeque;

/**
 * Менеджер файлов, отвечающий за операции ввода-вывода коллекции.
 * Данный класс изолирует логику работы с файловой системой и форматом XML от основной
 * бизнес-логики приложения. Он управляет процессами чтения и записи коллекции
 * StudyGroup, используя специализированные классы XMLReader и XMLWriter.
 * Все пути к файлам определяются через системную переменную окружения Study_groups.
 */
public class FileManager {
    /**
     * Объект для чтения и десериализации коллекции из XML-формата.
     */
    private final XMLReader xmlReader = new XMLReader();

    /**
     * Объект для сериализации и записи коллекции в XML-формат.
     */
    private final XMLWriter xmlWriter = new XMLWriter();

    /**
     * Название переменной окружения, в которой хранятся пути к файлам коллекции.
     */
    private final String nameOfFile = "Study_groups";

    /**
     * Конструктор по умолчанию.
     * Создает новый объект менеджера файлов для управления сохранением и загрузкой данных.
     */
    public FileManager() {
        super();
    }

    /**
     * Сохраняет текущее состояние коллекции в XML-файл.
     * Путь для сохранения извлекается из переменной окружения Study_groups. Если переменная
     * содержит список путей, разделенных точкой с запятой, для сохранения используется первый из них.
     * Метод обрабатывает ошибки доступа к файлу и выбрасывает специализированное исключение.
     *
     * @param collection коллекция объектов StudyGroup, которую необходимо сохранить.
     * @throws FileSaveException если путь сохранения не задан или возникла ошибка при записи в файл.
     */
    public void saveCollection(ArrayDeque<StudyGroup> collection) {
        String filePath = System.getenv(nameOfFile);

        if (filePath == null || filePath.trim().isEmpty()) {
            throw new FileSaveException("Путь сохранения " + nameOfFile + " не указан.");
        }
        String ourFile = filePath.split(";")[0].trim();

        try {
            xmlWriter.writeCollection(collection, ourFile);
            System.out.println("Коллекция успешно сохранена в файл " + ourFile);
        } catch (IllegalArgumentException | IOException e) {
            throw new FileSaveException("Ошибка при сохранении коллекции: " + e.getMessage());
        }
    }

    /**
     * Загружает коллекцию из XML-файлов при запуске серверного приложения.
     * Считывает список путей из переменной окружения Study_groups. Метод выполняет:
     * 1. Поочередное чтение всех указанных файлов.
     * 2. Проверку на уникальность ID: если элемент с таким ID уже загружен, новый пропускается.
     * 3. Формирование итоговой очереди объектов для работы программы.
     *
     * @return объект ArrayDeque, заполненный данными из файлов. Если переменная окружения не задана
     * или файлы пусты, возвращается пустая очередь.
     */
    public ArrayDeque<StudyGroup> loadCollection() {
        ArrayDeque<StudyGroup> collection = new ArrayDeque<>();

        String filePaths = System.getenv(nameOfFile);

        if (filePaths == null || filePaths.trim().isEmpty()) {
            System.out.println("Переменная окружения " + nameOfFile + " не найдена.\nПрограмма запущена с пустой коллекцией.");
            return collection;
        }

        String[] filePath = filePaths.split(";");
        for (String path : filePath) {
            try {
                ArrayDeque<StudyGroup> loadedGroup = xmlReader.readCollection(path.trim());
                for (StudyGroup newGroup : loadedGroup) {
                    boolean isExist = false;
                    for (StudyGroup existingGroup : collection) {
                        if (existingGroup.getId() == newGroup.getId()) {
                            isExist = true;
                            break;
                        }
                    }
                    if (!isExist) {
                        collection.add(newGroup);
                    } else {
                        System.out.println("Группа '" + newGroup.getName() + "' пропущена, так как элемент с id= " + newGroup.getId() + " уже существует.");
                    }
                }
                System.out.println("Файл " + path.trim() + " успешно считан.");
            } catch (FileNotFoundException e) {
                System.out.println("Файл не найден " + path.trim() + ": " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Ошибка при чтении файла " + path.trim() + ": " + e.getMessage());
            }
        }
        System.out.println("Загрузка элементов завершена. Всего элементов в коллекции: " + collection.size());
        return collection;
    }
}