package lab5.src.managers;

import lab5.src.models.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Scanner;

/**
 * Класс для чтения и ручного парсинга XML-файла с данными коллекции учебных групп.
 * Чтение документа происходит построчно с использованием Scanner и отслеживанием текущего состояния (тегов).
 */
public class XMLReader {

    /**
     * Считывает коллекцию объектов StudyGroup из указанного XML-файла.
     * Метод построчно анализирует файл, извлекая значения тегов и формируя объекты.
     * Если данные конкретной группы содержат ошибки парсинга или не проходят валидацию,
     * такая группа пропускается с выводом сообщения в консоль, а чтение файла продолжается.
     *
     * @param filePath путь к XML-файлу для чтения
     * @return коллекция ArrayDeque, содержащая успешно считанные и валидные объекты StudyGroup
     * @throws FileNotFoundException если путь к файлу не указан, файл не найден или отсутствуют права на чтение
     */
    public ArrayDeque<StudyGroup> readCollection(String filePath) throws FileNotFoundException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new FileNotFoundException("Путь к файлу не указан");
        }
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("Файл не найден " + filePath);
        }
        if (!file.canRead()) {
            throw new FileNotFoundException("Нет прав на чтение файла " + filePath);
        }

        ArrayDeque<StudyGroup> collection = new ArrayDeque<>();

        try (Scanner scanner = new Scanner(file)) {

            StudyGroup currentGroup = null;
            Coordinates currentCoord = null;
            Person currentAdmin = null;
            Location currentLocation = null;

            boolean isCoord = false;
            boolean isGroupAdmin = false;
            boolean isLocation = false;
            boolean isCurrentGroupValid = true;

            int lineNumber = 0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                lineNumber++;

                if (line.isEmpty() || line.startsWith("<?xml") || line.equals("<studyGroups>") || line.equals("</studyGroups>")) {
                    continue;
                }

                try {
                    if (line.equals("<studyGroup>")) {
                        currentGroup = new StudyGroup();
                        isCurrentGroupValid = true;

                    } else if (line.equals("</studyGroup>")) {
                        if (currentGroup != null) {

                            try {
                                currentGroup.validate();
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage());
                                isCurrentGroupValid = false;
                            }
                            if (isCurrentGroupValid) {
                                collection.add(currentGroup);
                            } else {
                                System.out.println("Группа пропущена из за ошибок в данных");
                            }
                            currentGroup = null;
                        }
                    } else if (line.equals("<coordinates>")) {
                        isCoord = true;
                        currentCoord = new Coordinates();
                    } else if (line.equals("</coordinates>")) {
                        isCoord = false;
                        if (currentGroup != null) {
                            currentGroup.setCoordinates(currentCoord);
                        }

                    } else if (line.equals("<groupAdmin>")) {
                        isGroupAdmin = true;
                        currentAdmin = new Person();

                    } else if (line.equals("</groupAdmin>")) {
                        isGroupAdmin = false;
                        if (currentGroup != null) {
                            currentGroup.setGroupAdmin(currentAdmin);
                        }

                    } else if (line.equals("<location>")) {
                        isLocation = true;
                        currentLocation = new Location();

                    } else if (line.equals("</location>")) {
                        isLocation = false;
                        if (currentAdmin != null) {
                            currentAdmin.setLocation(currentLocation);
                        }
                    } else if (currentGroup != null) {

                        if (line.startsWith("<id>")) {
                            currentGroup.setId(Long.parseLong(extractValue(line, "id")));


                        } else if (line.startsWith("<name>")) {
                            String nameValue = extractValue(line, "name");
                            if (isLocation && currentLocation != null) {
                                currentLocation.setName(nameValue);
                            } else if (isGroupAdmin && currentAdmin != null) {
                                currentAdmin.setName(nameValue);

                            } else {
                                currentGroup.setName(nameValue);
                            }
                        } else if (line.startsWith("<x>")) {
                            String xValue = extractValue(line, "x");
                            if (isLocation && currentLocation != null) {
                                currentLocation.setX(Float.parseFloat(xValue));
                            } else if (isCoord && currentCoord != null) {
                                currentCoord.setX(Float.parseFloat(xValue));

                            }
                        } else if (line.startsWith("<y>")) {
                            String yValue = extractValue(line, "y");

                            if (isLocation && currentLocation != null) {
                                currentLocation.setY(Long.parseLong(yValue));
                            } else if (isCoord && currentCoord != null) {
                                currentCoord.setY(Double.parseDouble(yValue));

                            }
                        } else if (line.startsWith("<creationDate>")) {
                            currentGroup.setCreationDate(LocalDateTime.parse(extractValue(line, "creationDate")));

                        } else if (line.startsWith("<studentsCount>")) {
                            currentGroup.setStudentsCount(Integer.parseInt(extractValue(line, "studentsCount")));

                        } else if (line.startsWith("<transferredStudents>")) {
                            currentGroup.setTransferredStudents(Integer.parseInt(extractValue(line, "transferredStudents")));

                        } else if (line.startsWith("<averageMark>")) {
                            currentGroup.setAverageMark(Double.parseDouble(extractValue(line, "averageMark")));

                        } else if (line.startsWith("<formOfEducation>")) {
                            currentGroup.setFormOfEducation(FormOfEducation.valueOf(extractValue(line, "formOfEducation").toUpperCase()));

                        } else if (line.startsWith("<height>")) {
                            if (currentAdmin != null) {
                                currentAdmin.setHeight(Double.parseDouble(extractValue(line, "height")));
                            }

                        } else if (line.startsWith("<eyeColor>")) {
                            if (currentAdmin != null) {
                                currentAdmin.setEyeColor(Color.valueOf(extractValue(line, "eyeColor").toUpperCase()));
                            }

                        } else if (line.startsWith("<nationality>")) {
                            if (currentAdmin != null) {
                                currentAdmin.setNationality(Country.valueOf(extractValue(line, "nationality").toUpperCase()));
                            }

                        }
                    } else {
                        if (line.startsWith("<")) {
                            System.out.println("Найден тег вне <studyGroup> " + lineNumber);
                            isCurrentGroupValid = false;
                        }
                    }


                } catch (Exception e) {
                    System.out.println("Ошибка парсинга в строке " + lineNumber + ": " + e.getMessage());
                    isCurrentGroupValid = false;
                }
            }
        }
        return collection;
    }

    /**
     * Вспомогательный метод для извлечения текстового значения между открывающим и закрывающим тегами.
     *
     * @param line строка, содержащая XML-тег с данными
     * @param tag  имя тега, значение которого необходимо извлечь
     * @return строка со значением тега или пустая строка, если тег не найден или пуст
     */
    private String extractValue(String line, String tag) {
        String openTag = "<" + tag + ">";
        String closeTag = "</" + tag + ">";
        int startIndex = line.indexOf(openTag) + openTag.length();
        int endIndex = line.indexOf(closeTag);

        if (startIndex >= openTag.length() && endIndex > startIndex) {
            return line.substring(startIndex, endIndex);
        }
        return "";
    }
}
