package lab5.src.managers;

import lab5.src.models.*;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Scanner;

/**
 * Класс для чтения XML-файла с данными коллекции учебных групп.
 * Считывание файла происходит с использованием Scanner (по требованиям ТЗ),
 * а десериализация структуры XML выполняется через встроенный DOM-парсер Java.
 */
public class XMLReader {

    /**
     * Считывает коллекцию объектов StudyGroup из указанного XML-файла.
     * В случае, если отдельная группа содержит ошибки (неверный формат данных,
     * отсутствие обязательных полей или провал валидации), она пропускается,
     * а чтение остальных элементов продолжается.
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
        StringBuilder xmlContent = new StringBuilder();

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                xmlContent.append(scanner.nextLine()).append("\n");
            }
        }
        if (xmlContent.toString().trim().isEmpty()) {
            return collection;
        }
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            ByteArrayInputStream input = new ByteArrayInputStream(xmlContent.toString().getBytes(StandardCharsets.UTF_8));
            Document doc = builder.parse(input);
            doc.getDocumentElement().normalize();

            NodeList groupNodes = doc.getElementsByTagName("studyGroup");

            for (int i = 0; i < groupNodes.getLength(); i++) {
                Node node = groupNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    try {
                        collection.add(parseGroup((Element) node));
                    } catch (Exception e) {
                        System.out.println("Ошибка, группа пропущена " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка XML " + e.getMessage());
        }
        return collection;

    }

    /**
     * Выполняет разбор отдельного XML-элемента (тега <studyGroup>) и собирает из него объект StudyGroup.
     *
     * @param element корневой XML-элемент группы
     * @return полностью сформированный и провалидированный объект StudyGroup
     * @throws IllegalArgumentException если отсутствуют обязательные поля или данные не проходят валидацию
     */
    private StudyGroup parseGroup(Element element) {
        StudyGroup group = new StudyGroup();

        group.setId(Long.parseLong(getRequiredValue(element, "id")));
        group.setName(getRequiredValue(element, "name"));
        group.setCreationDate(LocalDateTime.parse(getRequiredValue(element, "creationDate")));
        group.setStudentsCount(Integer.parseInt(getRequiredValue(element, "studentsCount")));

        String transferredStr = getDirectChildValue(element, "transferredStudents");
        if (transferredStr != null) {
            group.setTransferredStudents(Integer.parseInt(transferredStr));
        }
        String avgMarkStr = getDirectChildValue(element, "averageMark");
        if (avgMarkStr != null) {
            group.setAverageMark(Double.parseDouble(avgMarkStr));
        }

        String formStr = getDirectChildValue(element, "formOfEducation");
        if (formStr != null) {
            group.setFormOfEducation(FormOfEducation.valueOf(formStr.toUpperCase()));
        }

        Element coordinateEl = getRequiredElement(element, "coordinates");
        Coordinates coordinates = new Coordinates();
        coordinates.setX(Float.parseFloat(getRequiredValue(coordinateEl, "x")));
        coordinates.setY(Double.parseDouble(getRequiredValue(coordinateEl, "y")));
        group.setCoordinates(coordinates);

        Element adminEl = getDirectChildElement(element, "groupAdmin");
        if (adminEl != null) {
            Person admin = new Person();
            admin.setName(getRequiredValue(adminEl, "name"));
            admin.setEyeColor(Color.valueOf(getRequiredValue(adminEl, "eyeColor").toUpperCase()));
            admin.setNationality(Country.valueOf(getRequiredValue(adminEl, "nationality").toUpperCase()));

            String height = getDirectChildValue(adminEl, "height");
            if (height != null) admin.setHeight(Double.parseDouble(height));

            Element locEl = getDirectChildElement(adminEl, "location");
            if (locEl != null) {
                Location location = new Location();
                location.setX(Double.parseDouble(getRequiredValue(locEl, "x")));
                location.setY(Long.parseLong(getRequiredValue(locEl, "y")));
                location.setName(getRequiredValue(locEl, "name"));
                admin.setLocation(location);
            }
            group.setGroupAdmin(admin);
        }

        group.validate();
        return group;
    }

    /**
     * Вспомогательный метод для извлечения обязательного текстового значения из дочернего тега.
     *
     * @param parent родительский XML-элемент
     * @param tag    имя искомого дочернего тега
     * @return строковое значение тега
     * @throws IllegalArgumentException если искомый тег отсутствует или пуст
     */
    private String getRequiredValue(Element parent, String tag) {
        String val = getDirectChildValue(parent, tag);
        if (val == null || val.isEmpty()) {
            throw new IllegalArgumentException("Ошибка, отсутствует обязательное поле " + tag);
        }
        return val;
    }

    /**
     * Вспомогательный метод для получения обязательного дочернего XML-элемента.
     *
     * @param parent родительский XML-элемент
     * @param tag    имя искомого дочернего элемента
     * @return найденный дочерний элемент
     * @throws IllegalArgumentException если искомый элемент отсутствует
     */
    private Element getRequiredElement(Element parent, String tag) {
        Element element = getDirectChildElement(parent, tag);
        if (element == null) {
            throw new IllegalArgumentException("Ошибка, отсутствует обязательный класс " + tag);
        }
        return element;
    }

    /**
     * Ищет дочерний тег с указанным именем только на один уровень вложенности и возвращает его текст.
     * Помогает избежать захвата одноименных тегов из глубоких вложений.
     *
     * @param parent  родительский XML-элемент
     * @param tagName имя искомого тега
     * @return строковое значение тега или null, если тег не найден
     */
    private String getDirectChildValue(Element parent, String tagName) {
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals(tagName)) {
                return node.getTextContent().trim();
            }
        }
        return null;
    }

    /**
     * Ищет дочерний XML-элемент с указанным именем только на один уровень вложенности.
     *
     * @param parent  родительский XML-элемент
     * @param tagName имя искомого тега
     * @return объект Element или null, если элемент не найден
     */
    private Element getDirectChildElement(Element parent, String tagName) {
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals(tagName)) {
                return (Element) node;
            }
        }
        return null;
    }


}