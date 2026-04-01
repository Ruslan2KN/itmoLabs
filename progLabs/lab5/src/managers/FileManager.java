package lab5.src.managers;

import lab5.src.models.StudyGroup;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayDeque;

/**
 * Менеджер файлов, отвечающий за операции ввода-вывода коллекции.
 * Данный класс изолирует логику работы с файловой системой и форматом XML от основной
 * бизнес-логики приложения. Он управляет процессами чтения и записи коллекции
 * StudyGroup, используя специализированные классы XMLReader и XMLWriter.
 * * Все пути к файлам определяются через системные переменные окружения.
 */
public class FileManager {
    private XMLReader xmlReader = new XMLReader();
    private XMLWriter xmlWriter = new XMLWriter();
    private final String nameOfFile = "Study_groups";

    /**
     * Конструктор по умолчанию.
     * Инициализирует объекты для чтения и записи XML-данных.
     */

    public FileManager() {
        super();
    }

    /**
     * Сохраняет текущее состояние коллекции в XML-файл.
     * Путь для сохранения извлекается из переменной окружения. Если переменная
     * содержит список путей, разделенных точкой с запятой, для сохранения
     * используется первый из них.
     *
     * @param collection коллекция объектов StudyGroup, которую необходимо сохранить.
     * @throws IllegalArgumentException если переменная окружения с путем сохранения не задана или пуста.
     */
    public void saveCollection(ArrayDeque<StudyGroup> collection) {
        String filePath = System.getenv(nameOfFile);

        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Путь сохранения " + nameOfFile + " не указан.");
        }
        String ourFile = filePath.split(";")[0].trim();

        try {
            xmlWriter.writeCollection(collection, ourFile);
            System.out.println("Коллекция успешно сохранена в файл " + ourFile);
        } catch (IllegalArgumentException | IOException e) {
            System.out.println("Ошибка, при сохранении коллекции " + e.getMessage());
        }
    }


    /**
     * Загружает коллекцию из XML-файлов при запуске программы.
     * Считывает список путей из переменной окружения. Метод автоматически:
     * - Игнорирует элементы с уже существующими в текущей сессии ID.
     * - Находит максимальный ID среди всех загруженных объектов.
     * - Обновляет статический счетчик ID в классе StudyGroup для обеспечения
     * корректной генерации последующих идентификаторов.
     * * @return объект ArrayDeque, заполненный данными из файлов. Если пути не заданы
     * или файлы пусты, возвращается пустая очередь.
     */
    public ArrayDeque<StudyGroup> loadCollection() {
        ArrayDeque<StudyGroup> collection = new ArrayDeque<>();

        String filePaths = System.getenv(nameOfFile);

        if (filePaths == null || filePaths.trim().isEmpty()) {
            System.out.println("Файл " + nameOfFile + " не указан.\nПрограмма запущена с пустой коллекцией");
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
                        System.out.println("Группа '" + newGroup.getName() + "' пропущена, так как элемент с id= " + newGroup.getId() + " уже существует");
                    }
                }
                System.out.println("Файл " + path.trim() + " успешно считан");
            } catch (FileNotFoundException e) {
                System.out.println("Ошибка при чтение файла " + path.trim() + ": " + e.getMessage());

            }
        }
        long maxId = 0;
        for (StudyGroup group : collection) {
            if (group.getId() > maxId) {
                maxId = group.getId();
            }
        }
        StudyGroup.updateNextId(maxId);
        System.out.println("Загрузка элементов завершена, всего элементов в коллекции " + collection.size());
        return collection;

    }
}
