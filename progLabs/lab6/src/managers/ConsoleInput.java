package src.managers;

import src.models.*;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

/**
 * Класс для интерактивного считывания данных из консоли или файла (скрипта).
 * Отвечает за пошаговое создание и обновление объектов коллекции (учебных групп),
 * а также за валидацию пользовательского ввода. Имеет два режима работы:
 * - Консольный режим: В случае ошибки запрашивает ввод повторно.
 * - Режим скрипта: При обнаружении невалидных данных немедленно прерывает
 * выполнение текущей команды, выбрасывая исключение, и не выводит подсказки.
 */
public class ConsoleInput {

    /**
     * Сканер для чтения пользовательского ввода или строк из файла скрипта.
     */
    private Scanner scanner;

    /**
     * Флаг, указывающий на текущий режим работы менеджера ввода.
     * Если установлен в true, ввод осуществляется из скрипта (без вывода подсказок).
     */
    private Boolean scriptMode = false;

    /**
     * Конструктор, принимающий сканер для чтения данных.
     *
     * @param scanner объект Scanner, настроенный на нужный поток ввода (например, System.in)
     */
    public ConsoleInput(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Запускает пошаговый интерактивный процесс создания новой учебной группы.
     * Вызывает вспомогательные методы для заполнения всех полей объекта.
     *
     * @return полностью сформированный и валидный объект StudyGroup
     */
    public StudyGroup askStudyGroup() {
        StudyGroup group = new StudyGroup();

        askNameGroups(group);
        group.setCoordinates(askCoordinates());
        askStudentsCount(group);
        askTransferredStudents(group);
        askAvgMark(group);
        group.setFormOfEducation(askFormOfEducation());
        group.setGroupAdmin(askPerson());
        return group;
    }

    /**
     * Запрашивает у пользователя ввод имени группы и устанавливает его в переданный объект.
     *
     * @param group объект учебной группы, для которого задается имя
     */
    private void askNameGroups(StudyGroup group) {
        while (true) {
            printPrompt(" Введите имя группы, имя группы не может быть пустым ");
            try {
                group.setName(scanner.nextLine());
                break;
            } catch (IllegalArgumentException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Скрипт ошибка имени группы: " + e.getMessage());
                }
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Запрашивает количество переведенных студентов и сохраняет в объект группы.
     * Данное поле не является обязательным и может быть пропущено пользователем.
     *
     * @param group объект учебной группы, для которого задается количество переведенных студентов
     */
    private void askTransferredStudents(StudyGroup group) {
        while (true) {
            printPrompt("Введите количество переведенных студентов, можно пропустить нажав enter");

            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                group.setTransferredStudents(null);
                break;
            }
            try {
                int count = Integer.parseInt(input);
                group.setTransferredStudents(count);
                break;
            } catch (NumberFormatException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Скрипт: количество переведенных студентов должно быть целым числом");
                }
                System.out.println(" Ошибка, введите целое число. ");
            } catch (IllegalArgumentException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Скрипт: " + e.getMessage());
                }
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Запрашивает и устанавливает количество студентов в группе.
     * Поле является обязательным и должно быть представлено целым числом больше нуля.
     *
     * @param group объект учебной группы, для которого задается количество студентов
     */
    private void askStudentsCount(StudyGroup group) {
        while (true) {
            printPrompt("Введите количество студентов, целое число, не может быть пустым ");
            try {
                int count = Integer.parseInt(scanner.nextLine().trim());
                group.setStudentsCount(count);
                break;
            } catch (NumberFormatException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Ошибка скрипта: кол-во студентов должно быть целым числом");
                }
                System.out.println(" Ошибка, введите целое число. ");
            } catch (IllegalArgumentException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Скрипт: " + e.getMessage());
                }
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Запрашивает и устанавливает средний балл группы.
     * Поле может быть пропущено при вводе (будет сохранено как null).
     *
     * @param group объект учебной группы, для которого задается средний балл
     */
    private void askAvgMark(StudyGroup group) {
        while (true) {
            printPrompt("Введите средний балл (дробное число) или пропустите ввод");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                group.setAverageMark(null);
                break;
            }
            try {
                Double avg = Double.parseDouble(input);
                group.setAverageMark(avg);
                break;
            } catch (NumberFormatException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Ошибка скрипта: средний балл должен быть дробным числом");
                }
                System.out.println(" Ошибка, введите дробное число. ");
            } catch (IllegalArgumentException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Ошибка скрипта: " + e.getMessage());
                }
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Интерактивно собирает данные для создания объекта администратора группы.
     * Предлагает пользователю выбор: создать администратора или пропустить этот шаг.
     *
     * @return полностью сформированный объект Person или null, если создание пропущено
     */
    private Person askPerson() {
        Person admin = new Person();

        printPrompt("Хотите ли вы добавить админа группы? впишите любой символ, если хотите или нажмите enter чтобы не добавлять админа.");

        String adminInput = scanner.nextLine().trim();
        if (adminInput.isEmpty()) {
            admin = null;
            printPrompt("Группа создана без админа");
        } else {
            while (true) {
                printPrompt(" Введите имя админа, не может быть пустой строкой ");
                try {
                    admin.setName(scanner.nextLine());
                    break;
                } catch (IllegalArgumentException e) {
                    if (scriptMode) {
                        throw new IllegalArgumentException("Ошибка скрипта: имя админа должно быть string. " + e.getMessage());
                    }
                    System.out.println(e.getMessage());
                }
            }
            while (true) {
                printPrompt("Введите рост админа, дробное число, не может быть пустым");
                try {
                    Double height = Double.parseDouble(scanner.nextLine().trim());
                    admin.setHeight(height);
                    break;
                } catch (NumberFormatException e) {
                    if (scriptMode) {
                        throw new IllegalArgumentException("Ошибка скрипта: рост админа должен быть числом");
                    }
                    System.out.println(" Ошибка, введите корректное число. ");
                } catch (IllegalArgumentException e) {
                    if (scriptMode) {
                        throw new IllegalArgumentException("Ошибка скрипта: " + e.getMessage());
                    }
                    System.out.println(e.getMessage());
                }
            }
            admin.setNationality(askCountry());
            admin.setEyeColor(askEyeColor());
            admin.setLocation(askLocation());
        }

        if (admin != null) {
            printPrompt("Группа создана с админом");
        }

        return admin;
    }

    /**
     * Интерактивно запрашивает и проверяет данные для создания объекта координат.
     *
     * @return сформированный объект Coordinates с заданными параметрами X и Y
     */
    private Coordinates askCoordinates() {
        Coordinates coords = new Coordinates();

        while (true) {
            printPrompt(" Введите координату X. дробное число, не может быть null ");
            try {
                Float x = Float.parseFloat(scanner.nextLine().trim());
                coords.setX(x);
                break;
            } catch (NumberFormatException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Ошибка скрипта: координата х должна быть числом");
                }
                System.out.println("Ошибка, введите корректное дробное число.");
            } catch (NullPointerException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Ошибка: " + e.getMessage());
                }
                System.out.println(e.getMessage());
            }
        }
        while (true) {
            printPrompt("Введите координату Y, максимум значение 689 и дробное число ");
            try {
                Double y = Double.parseDouble(scanner.nextLine().trim());
                coords.setY(y);
                break;
            } catch (NumberFormatException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Ошибка скрипта: координата у должна быть числом не большим 689");
                }
                System.out.println("Ошибка, введите корректное число.");
            } catch (IllegalArgumentException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Ошибка: " + e.getMessage());
                }
                System.out.println(e.getMessage());
            }
        }
        return coords;
    }

    /**
     * Запрашивает выбор формы обучения из доступных вариантов перечисления FormOfEducation.
     *
     * @return выбранная форма обучения или null, если ввод пропущен
     */
    private FormOfEducation askFormOfEducation() {
        while (true) {
            printPrompt("Выберите форму обучения: " + Arrays.toString(FormOfEducation.values()) + " или нажмите enter что бы пропустить выбор");
            String formOfEducation = scanner.nextLine().trim().toUpperCase();
            if (formOfEducation.isEmpty()) return null;
            try {
                return FormOfEducation.valueOf(formOfEducation);
            } catch (IllegalArgumentException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Ошибка скрипта: выбрана неправильная форма обучения. " + e.getMessage());
                }
                System.out.println("Ошибка, такой формы обучения нет.");
            }
        }
    }

    /**
     * Запрашивает выбор цвета глаз из доступных вариантов перечисления Color.
     *
     * @return выбранное значение цвета глаз
     */
    private Color askEyeColor() {
        while (true) {
            printPrompt("Введите цвет глаз: " + Arrays.toString(Color.values()));
            try {
                return Color.valueOf(scanner.nextLine().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Ошибка скрипта: неверный цвет. " + e.getMessage());
                }
                System.out.println("Ошибка, такого цвета нет.");
            }
        }
    }

    /**
     * Запрашивает выбор страны происхождения из вариантов перечисления Country.
     *
     * @return выбранное значение страны
     */
    private Country askCountry() {
        while (true) {
            printPrompt("Введите страну рождения: " + Arrays.toString(Country.values()));
            try {
                return Country.valueOf(scanner.nextLine().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Ошибка скрипта: " + e.getMessage());
                }
                System.out.println("Ошибка, такой страны нет");
            }
        }
    }

    /**
     * Интерактивно собирает данные для создания объекта локации администратора.
     * Запрашивает координаты X, Y и название места.
     *
     * @return сформированный объект Location
     */
    private Location askLocation() {
        printPrompt("Введите локацию");
        Location location = new Location();

        while (true) {
            printPrompt("Введите координату X для локации, дробное число, не может быть пустым");
            String input = scanner.nextLine().trim();
            try {
                double x = Double.parseDouble(input);
                location.setX(x);
                break;
            } catch (NumberFormatException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Ошибка скрипта: координата х должна быть числом. " + e.getMessage());
                }
                System.out.println("Ошибка, введите корректное дробное число");
            }
        }
        while (true) {
            printPrompt("Введите координату Y для локации, целое число, не может быть пустым");
            String input = scanner.nextLine().trim();
            try {
                Long y = Long.parseLong(input);
                location.setY(y);
                break;
            } catch (NumberFormatException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Ошибка скрипта: координата y должна быть числом. " + e.getMessage());
                }
                System.out.println("Ошибка, введите корректное целое число");
            }
        }
        while (true) {
            printPrompt("Введите имя локации, не может быть пустой строкой");
            String input = scanner.nextLine().trim();
            try {
                location.setName(input);
                break;
            } catch (IllegalArgumentException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Ошибка скрипта: " + e.getMessage());
                }
                System.out.println(e.getMessage());
            }
        }
        return location;
    }

    /**
     * Устанавливает новый объект Scanner для считывания данных.
     * Используется для переключения ввода с консоли на файл (скрипт).
     *
     * @param scanner новый экземпляр Scanner
     */
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Возвращает текущий используемый объект Scanner.
     *
     * @return текущий экземпляр Scanner
     */
    public Scanner getScanner() {
        return scanner;
    }

    /**
     * Запускает интерактивное меню для обновления полей существующей группы.
     * Позволяет выбрать конкретное поле для изменения или обновить весь объект целиком.
     * Содержит возможность отмены внесенных изменений.
     *
     * @param group объект учебной группы, который необходимо обновить
     */
    public void updateStudyGroup(StudyGroup group) {
        boolean isUpd = true;
        StudyGroup oldGroup = new StudyGroup(group.getName(), group.getCoordinates(), group.getStudentsCount(), group.getAverageMark(), group.getFormOfEducation(), group.getGroupAdmin(), group.getTransferredStudents());

        while (isUpd) {
            if (!scriptMode) {
                System.out.println(" Выберите элемент который хотите обновить");
                System.out.println("1 - Имя (сейчас: " + group.getName() + ")");
                System.out.println("2 - Координаты");
                System.out.println("3 - Количество студентов (сейчас: " + group.getStudentsCount() + ")");
                System.out.println("4 - Количество переведенных студентов (сейчас: " + group.getTransferredStudents() + ")");
                System.out.println("5 - Средний балл (сейчас: " + group.getAverageMark() + ")");
                System.out.println("6 - Форма обучения (сейчас: " + group.getFormOfEducation() + ")");
                System.out.println("7 - Админ группы");
                System.out.println("8 - Всю группу");
                System.out.println("9 - Отменить все изменения");
                System.out.println("0 - Сохранить изменения и выйти");
                System.out.print(" Выберите поле для обновления 0-9: ");
            }

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": {
                    askNameGroups(group);
                    printPrompt("Имя обновлено");
                    break;
                }
                case "2": {
                    group.setCoordinates(askCoordinates());
                    printPrompt("Координаты обновлены");
                    break;
                }
                case "3": {
                    askStudentsCount(group);
                    printPrompt("Количество студентов обновлено");
                    break;
                }
                case "4": {
                    askTransferredStudents(group);
                    printPrompt("Количество переведенных студентов обновлено");
                    break;
                }
                case "5": {
                    askAvgMark(group);
                    printPrompt("Средний балл обновлен");
                    break;
                }
                case "6": {
                    group.setFormOfEducation(askFormOfEducation());
                    printPrompt("Форма обучения обновлена");
                    break;
                }
                case "7": {
                    printPrompt("Введите новые данные для админа");
                    group.setGroupAdmin(askPerson());
                    break;
                }
                case "8": {
                    printPrompt("Введите новые данные для группы");
                    askNameGroups(group);
                    group.setCoordinates(askCoordinates());
                    askStudentsCount(group);
                    askTransferredStudents(group);
                    askAvgMark(group);
                    group.setFormOfEducation(askFormOfEducation());
                    group.setGroupAdmin(askPerson());
                    printPrompt("Данные группы были обновлены");
                    break;
                }
                case "9": {
                    if (!Objects.equals(group.getName(), (oldGroup.getName()))) {
                        printPrompt("Название группы стало прежним");
                        group.setName(oldGroup.getName());
                    }
                    if (group.getStudentsCount() != oldGroup.getStudentsCount()) {
                        printPrompt("Количество студентов стало прежним");
                        group.setStudentsCount(oldGroup.getStudentsCount());
                    }
                    if (!Objects.equals(group.getTransferredStudents(), (oldGroup.getTransferredStudents()))) {
                        printPrompt("Количество переведенных студентов стало прежним");
                        group.setTransferredStudents(oldGroup.getTransferredStudents());
                    }
                    if (group.getCoordinates() != oldGroup.getCoordinates()) {
                        printPrompt("Координаты стали прежними");
                        group.setCoordinates(oldGroup.getCoordinates());
                    }
                    if (!Objects.equals(group.getAverageMark(), (oldGroup.getAverageMark()))) {
                        printPrompt("Средний балл группы стал прежним");
                        group.setAverageMark(oldGroup.getAverageMark());
                    }
                    if (group.getFormOfEducation() != oldGroup.getFormOfEducation()) {
                        printPrompt("Форма обучения стала прежней");
                        group.setFormOfEducation(oldGroup.getFormOfEducation());
                    }
                    if (!Objects.equals(group.getGroupAdmin(), (oldGroup.getGroupAdmin()))) {
                        printPrompt("Админ группы, стал прежним");
                        group.setGroupAdmin(oldGroup.getGroupAdmin());
                    }
                    printPrompt("Все данные группы стали прежними");
                    break;
                }
                case "0": {
                    isUpd = false;
                    break;
                }
                default:
                    if (scriptMode) {
                        throw new IllegalArgumentException("Ошибка скрипта: неверный пункт меню обновления " + choice);
                    }
                    System.out.println("Ошибка, неверный пункт. Введите цифру от 0 до 9");
            }
        }
    }

    /**
     * Изменяет режим работы менеджера ввода (консоль или скрипт).
     *
     * @param scriptMode true для включения режима скрипта, false для возврата к консоли
     */
    public void setScriptMode(Boolean scriptMode) {
        this.scriptMode = scriptMode;
    }

    /**
     * Выводит текстовую подсказку в стандартный поток вывода,
     * но только в том случае, если программа НЕ находится в режиме скрипта.
     *
     * @param message текст сообщения для вывода
     */
    private void printPrompt(String message) {
        if (!scriptMode) {
            System.out.println(message);
        }
    }

    /**
     * Проверяет, включен ли в данный момент режим исполнения скрипта.
     *
     * @return true, если режим скрипта активен, иначе false
     */
    public Boolean isScriptMode() {
        return scriptMode;
    }
}