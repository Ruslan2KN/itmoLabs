package managers;

import exceptions.EmptyCollectionException;
import models.StudyGroup;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * Класс менеджер, отвечающий за управление коллекцией учебных групп.
 * Синхронизирован с помощью ReadWriteLock для обеспечения многопоточной безопасности.
 */
public class CollectionManager {
    /**
     * Коллекция, в которой хранятся объекты учебных групп.
     */
    private ArrayDeque<StudyGroup> collection = new ArrayDeque<>();

    /**
     * Дата и время инициализации объекта менеджера коллекции.
     */
    private final LocalDateTime initializationDate = LocalDateTime.now();

    /**
     * Блокировка для синхронизации потоков чтения и записи.
     */
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public CollectionManager() {
        super();
    }

    public CollectionManager(ArrayDeque<StudyGroup> collection) {
        if (collection != null) {
            this.collection = collection;
        }
    }

    /**
     * Добавляет в коллекцию уже готовый объект группы.
     * Используется при загрузке данных или успешном добавлении в БД.
     */
    public void addUpdatedGroup(StudyGroup group) {
        if (group == null) {
            throw new IllegalArgumentException("Элемент не может быть null");
        }
        lock.writeLock().lock();
        try {
            collection.add(group);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Удаляет все элементы из текущей коллекции.
     */
    public void clear(String username) {
        lock.writeLock().lock();
        try {
            collection.removeIf(group -> group.getOwner().equals(username));
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Возвращает форматированную строку с информацией о коллекции.
     */
    public String getInfoString() {
        lock.readLock().lock();
        try {
            return "Тип коллекции: " + collection.getClass().getSimpleName() + "\n" +
                    "Даты инициализации коллекции: " + initializationDate + "\n" +
                    "Количество элементов в коллекции: " + collection.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Возвращает копию коллекции для безопасной передачи по сети и вывода (вместо старого show).
     */
    public Collection<StudyGroup> getCollectionCopy() {
        lock.readLock().lock();
        try {

            return new ArrayList<>(collection);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Пытается удалить элемент из коллекции, сравнивая его ID.
     */
    public void removeById(long id, String username) {
        lock.writeLock().lock();
        try {
             collection.removeIf(group -> group.getId() == id && group.getOwner().equals(username));
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Ищет элемент в коллекции по его идентификатору.
     */
    public StudyGroup getById(long id) {
        lock.readLock().lock();
        try {
            for (StudyGroup group : collection) {
                if (group.getId() == id) {
                    return group;
                }
            }
            return null;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Получаем первый элемент коллекции
     * @return StudyGroup
     */
    public StudyGroup getAbsoluteFirst() {
        lock.readLock().lock();
        try {
            return collection.peekFirst();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Считает количество элементов, поле studentsCount которых равно заданному значению.
     */
    public long countByStudentsCount(int count) {
        lock.readLock().lock();
        try {
            return collection.stream()
                    .filter(group -> group.getStudentsCount() == count)
                    .count();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Удаляет из коллекции все элементы, которые больше переданного.
     */
    public boolean removeGreater(StudyGroup groupToCompare, String username) {
        lock.writeLock().lock();
        try {
            return collection.removeIf(group -> group.compareTo(groupToCompare) > 0 && group.getOwner().equals(username));
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Удаляет из коллекции все элементы, которые меньше переданного.
     */
    public boolean removeLower(StudyGroup groupToCompare, String username) {
        lock.writeLock().lock();
        try {
            return collection.removeIf(group -> group.compareTo(groupToCompare) < 0 && group.getOwner().equals(username));
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Возвращает список элементов коллекции, отсортированный по убыванию.
     */
    public List<StudyGroup> getDescending() {
        lock.readLock().lock();
        try {
            return collection.stream()
                    .sorted((g1, g2) -> g2.compareTo(g1))
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Возвращает строковое представление администраторов всех групп, отсортированное по убыванию.
     */
    public String getFieldDescendingGroupAdmin() {
        lock.readLock().lock();
        try {
            if (collection.isEmpty()) {
                throw new EmptyCollectionException("Коллекция пуста");
            }

            String result = collection.stream()
                    .map(StudyGroup::getGroupAdmin)
                    .filter(Objects::nonNull)
                    .sorted((admin1, admin2) -> admin2.getName().compareToIgnoreCase(admin1.getName()))
                    .map(Object::toString)
                    .collect(Collectors.joining("\n"));

            return result.isEmpty() ? "В текущих группах нет назначенных администраторов." : result;
        } finally {
            lock.readLock().unlock();
        }
    }


    public List<StudyGroup> show(){
        lock.readLock().lock();
        try {
            return collection.stream()
                    .sorted()
                    .toList();
            } finally {
            lock.readLock().unlock();
        }
    }
}