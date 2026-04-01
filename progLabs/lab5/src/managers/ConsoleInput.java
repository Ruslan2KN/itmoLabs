package lab5.src.managers;

import lab5.src.models.*;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Класс для интерактивного считывания данных из консоли или файла (скрипта).
 * * Отвечает за пошаговое создание и обновление объектов коллекции (учебных групп),
 * а также за валидацию пользовательского ввода. Имеет два режима работы:
 * * - Консольный режим: В случае ошибки (неверный тип данных, нарушение ограничений)
 * запрашивает ввод повторно до тех пор, пока не будут получены корректные данные.
 * - Режим скрипта: При обнаружении невалидных данных немедленно прерывает
 * выполнение текущей команды, выбрасывая исключение.
 */
public class ConsoleInput {

    private Scanner scanner;
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
     * * Последовательно вызывает методы для запроса всех необходимых полей объекта.
     * Если включен режим консоли, пользователю выводятся подсказки.
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
        if (!scriptMode) {
            System.out.println("Введите данные админа группы или нажмите enter чтобы не добавлять админа.");
        }
        String adminInput = scanner.nextLine().trim();
        if (adminInput.isEmpty()) {
            group.setGroupAdmin(null);
            if (!scriptMode) {
                System.out.println("Группа создана без админа");
            }
        } else {
            group.setGroupAdmin(askPerson());
            if (!scriptMode) {
                System.out.println("Группа создана с админом");
            }
        }
        return group;
    }

    /**
     * Запрашивает и устанавливает имя группы.
     *
     * @param group группа, для которой задается имя
     * @throws IllegalArgumentException в режиме скрипта, если введено некорректное имя
     */
    private void askNameGroups(StudyGroup group) {
        while (true) {
            if (!scriptMode) {
                System.out.println(" Введите имя группы ");
            }
            try {
                group.setName(scanner.nextLine());
                break;
            } catch (IllegalArgumentException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Скрипт ошибка имени группы " + e.getMessage());
                }
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Запрашивает и устанавливает количество переведенных студентов (может быть пропущено).
     *
     * @param group группа, для которой задается значение
     * @throws IllegalArgumentException в режиме скрипта при ошибке парсинга числа
     */
    private void askTransferredStudents(StudyGroup group) {
        while (true) {
            if (!scriptMode) {
                System.out.println("Введите количество переведенных студентов");
            }

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
                    throw new IllegalArgumentException("Скрипт количество переведенных студентов должно быть целым число");
                }
                System.out.println(" Ошибка, введите целое число. ");
            } catch (IllegalArgumentException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Скрипт " + e.getMessage());
                }
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Запрашивает и устанавливает количество студентов в группе.
     *
     * @param group группа, для которой задается значение
     * @throws IllegalArgumentException в режиме скрипта при ошибке ввода
     */
    private void askStudentsCount(StudyGroup group) {
        while (true) {
            if (!scriptMode) {
                System.out.println("Введите количество студентов");
            }
            try {
                int count = Integer.parseInt(scanner.nextLine().trim());
                group.setStudentsCount(count);
                break;
            } catch (NumberFormatException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Ошибка скрипта, кол-во студентов должно быть целым числом");
                }
                System.out.println(" Ошибка, введите целое число. ");
            } catch (IllegalArgumentException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Скрипт " + e.getMessage());
                }
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Запрашивает и устанавливает средний балл (может быть пропущено).
     *
     * @param group группа, для которой задается значение
     * @throws IllegalArgumentException в режиме скрипта при вводе нечислового значения
     */
    private void askAvgMark(StudyGroup group) {
        while (true) {
            if (!scriptMode) {
                System.out.println("Введите средний балл (дробное число) или пропустите ввод");
            }
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
                    throw new IllegalArgumentException("Ошибка скрипта средний балл должен быть дробным числом");
                }
                System.out.println(" Ошибка, введите дробное число. ");
            } catch (IllegalArgumentException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Ошибка скрипта " + e.getMessage());
                }
                System.out.println(e.getMessage());
            }
        }

    }

    /**
     * Интерактивно собирает данные для создания объекта администратора (Person).
     *
     * @return сформированный объект Person
     * @throws IllegalArgumentException в режиме скрипта при ошибке валидации полей
     */
    private Person askPerson() {
        Person admin = new Person();

        while (true) {
            if (!scriptMode) {
                System.out.println(" Введите имя админа ");
            }
            try {
                admin.setName(scanner.nextLine());
                break;
            } catch (IllegalArgumentException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Ошибка скрипта имя админа должно быть string" + e.getMessage());
                }
                System.out.println(e.getMessage());
            }
        }
        while (true) {
            System.out.println("Введите рост админа, дробное число");
            try {
                Double height = Double.parseDouble(scanner.nextLine().trim());
                admin.setHeight(height);
                break;
            } catch (NumberFormatException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Ошибка скрипта, рост админа должен быть числом");
                }
                System.out.println(" Ошибка, введите корректное число. ");
            } catch (IllegalArgumentException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Ошибка скрипта " + e.getMessage());
                }
                System.out.println(e.getMessage());
            }
        }
        admin.setNationality(askCountry());
        admin.setEyeColor(askEyeColor());
        admin.setLocation(askLocation());

        return admin;
    }

    /**
     * Интерактивно собирает данные для создания объекта координат.
     *
     * @return сформированный объект Coordinates
     * @throws IllegalArgumentException в режиме скрипта при ошибке формата чисел
     */
    private Coordinates askCoordinates() {
        Coordinates coords = new Coordinates();

        while (true) {
            if (!scriptMode) {
                System.out.println(" Введите координату X ");
            }
            try {
                Float x = Float.parseFloat(scanner.nextLine().trim());
                coords.setX(x);
                break;
            } catch (NumberFormatException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Ошибка скрипта координата х должна быть числом");
                }
                System.out.println("Ошибка, введите корректное дробное число.");
            } catch (NullPointerException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Ошибка " + e.getMessage());
                }
                System.out.println(e.getMessage());
            }
        }
        while (true) {
            if (!scriptMode) {
                System.out.println("Введите координату Y, максимум значение 689 ");
            }
            try {
                Double y = Double.parseDouble(scanner.nextLine().trim());
                coords.setY(y);
                break;
            } catch (NumberFormatException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Ошибка скрипта координата у должна быть числом не большим 689");
                }
                System.out.println("Ошибка, введите корректное число.");
            } catch (IllegalArgumentException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Ошибка " + e.getMessage());
                }
                System.out.println(e.getMessage());
            }
        }
        return coords;
    }

    /**
     * Запрашивает выбор формы обучения из доступных вариантов перечисления.
     *
     * @return выбранная форма обучения
     * @throws IllegalArgumentException в режиме скрипта, если введено несуществующее значение
     */
    private FormOfEducation askFormOfEducation() {
        while (true) {
            if (!scriptMode) {
                System.out.println("Выберите форму обучения: " + Arrays.toString(FormOfEducation.values()));
            }
            try {
                return FormOfEducation.valueOf(scanner.nextLine().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Ошибка скрипта выбрана неправильная форма обучения " + e.getMessage());
                }
                System.out.println("Ошибка, такой формы обучения нет.");
            }
        }
    }

    /**
     * Запрашивает выбор цвета глаз из доступных вариантов перечисления.
     *
     * @return выбранный цвет глаз
     * @throws IllegalArgumentException в режиме скрипта, если введено несуществующее значение
     */
    private Color askEyeColor() {
        while (true) {
            if (!scriptMode) {
                System.out.println("Введите цвет глаз: " + Arrays.toString(Color.values()));
            }
            try {
                return Color.valueOf(scanner.nextLine().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Ошибка скрипта неверный цвет " + e.getMessage());
                }
                System.out.println("Ошибка, такого цвета нет.");
            }
        }
    }

    /**
     * Запрашивает выбор страны из доступных вариантов перечисления.
     *
     * @return выбранная страна
     * @throws IllegalArgumentException в режиме скрипта, если введено несуществующее значение
     */
    private Country askCountry() {
        while (true) {
            if (!scriptMode) {
                System.out.println("Введите страну рождения: " + Arrays.toString(Country.values()));
            }
            try {
                return Country.valueOf(scanner.nextLine().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Ошибка скрипта " + e.getMessage());
                }
                System.out.println("Ошибка, такой страны нет");
            }
        }
    }

    /**
     * Интерактивно собирает данные для создания объекта локации.
     *
     * @return сформированный объект Location
     * @throws IllegalArgumentException в режиме скрипта при ошибке валидации данных
     */
    private Location askLocation() {
        if (!scriptMode) {
            System.out.println("Введите локацию");
        }
        Location location = new Location();

        while (true) {
            if (!scriptMode) {
                System.out.println("Введите координату X для локации, дробное число");
            }
            String input = scanner.nextLine().trim();
            try {
                double x = Double.parseDouble(input);
                location.setX(x);
                break;
            } catch (NumberFormatException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Ошибка скрипта, координата х должна быть числом " + e.getMessage());
                }
                System.out.println("Ошибка, введите корректное дробное число");
            }
        }
        while (true) {
            if (!scriptMode) {
                System.out.println("Введите координату Y для локации, целое число");
            }
            String input = scanner.nextLine().trim();
            try {
                Long y = Long.parseLong(input);
                location.setY(y);
                break;
            } catch (NumberFormatException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Ошибка скрипта координата y должна быть числом " + e.getMessage());
                }
                System.out.println("Ошибка, введите корректное целое число");
            }
        }
        while (true) {
            if (!scriptMode) {
                System.out.println("Введите имя локации");
            }
            String input = scanner.nextLine().trim();
            try {
                location.setName(input);
                break;
            } catch (IllegalArgumentException e) {
                if (scriptMode) {
                    throw new IllegalArgumentException("Ошибка скрипта " + e.getMessage());
                }
                System.out.println(e.getMessage());
            }
        }
        return location;
    }

    /**
     * Устанавливает новый объект Scanner для считывания данных.
     *
     * @param scanner объект Scanner
     */
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Возвращает текущий используемый объект Scanner.
     *
     * @return текущий объект Scanner
     */
    public Scanner getScanner() {
        return scanner;
    }

    /**
     * Запускает интерактивное меню для обновления полей существующей группы.
     * * Позволяет пользователю выбрать конкретное поле для изменения и сохраняет результат.
     * Доступные опции:
     * * - 1 - Имя
     * - 2 - Координаты
     * - 3 - Количество студентов
     * - 4 - Количество переведенных студентов
     * - 5 - Средний балл
     * - 6 - Форма обучения
     * - 7 - Админ группы
     * - 0 - Выход
     *
     * @param group объект StudyGroup, данные которого необходимо обновить
     * @throws IllegalArgumentException в режиме скрипта при выборе несуществующего пункта меню
     */
    public void updateStudyGroup(StudyGroup group) {
        boolean isUpd = true;

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
                System.out.println("0 - Сохранить изменения и выйти");
                System.out.print(" Выберите поле для обновления 0-7: ");
            }
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": {
                    askNameGroups(group);
                    System.out.println("Имя обновлено");
                    break;
                }
                case "2": {
                    group.setCoordinates(askCoordinates());
                    System.out.println("Координаты обновлены");
                    break;
                }
                case "3": {
                    askStudentsCount(group);
                    System.out.println("Количество студентов обновлено");
                    break;
                }
                case "4": {
                    askTransferredStudents(group);
                    System.out.println("Количество переведенных студентов обновлено");
                    break;
                }
                case "5": {
                    askAvgMark(group);
                    System.out.println("Средний балл обновлен");
                    break;
                }
                case "6": {
                    group.setFormOfEducation(askFormOfEducation());
                    System.out.println("Форма обучения обновлена");
                    break;
                }
                case "7": {
                    System.out.println("Введите новые данные для админа");
                    group.setGroupAdmin(askPerson());
                    System.out.println("Данные админа обновлены");
                    break;
                }
                case "0": {
                    isUpd = false;
                    break;
                }
                default:
                    if (scriptMode) {
                        throw new IllegalArgumentException("Ошибка скрипта неверный пункт меню обновления " + choice);
                    }
                    System.out.println("Ошибка, неверный пункт. Введите цифру от 0 до 7");
            }
        }
    }

    public void setScriptMode(Boolean scriptMode) {
        this.scriptMode = scriptMode;
    }


}
