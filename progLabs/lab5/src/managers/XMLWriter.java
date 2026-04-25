package lab5.src.managers;

import lab5.src.models.StudyGroup;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayDeque;

/**
 * Класс, отвечающий за сохранение (сериализацию) коллекции в файл формата XML.
 */
public class XMLWriter {

    /**
     * Записывает переданную коллекцию элементов в указанный XML-файл.
     * Перед записью производит проверки на наличие файла и прав доступа.
     *
     * @param collection коллекция объектов StudyGroup, которую необходимо сохранить
     * @param filePath   путь к целевому файлу
     * @throws IOException           если возникает ошибка ввода-вывода
     * @throws FileNotFoundException если путь пустой, является директорией или нет прав на запись
     */
    public void writeCollection(ArrayDeque<StudyGroup> collection, String filePath ) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new FileNotFoundException("Путь к файлу не указан или указан неправильно. Укажите правильный путь.");
        }
        File file = new File(filePath);

        if (file.isDirectory()) {
            throw new FileNotFoundException("Укзанный путь до файла, является директорией");
        }
        if (file.exists() && !file.canWrite()) {
            throw new FileNotFoundException("Нет прав на запись в файл " + filePath);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<studyGroups>\n");

            for (StudyGroup group : collection) {
                writer.write(getIndent(4)+"<studyGroup>\n");
                writeTag(writer,8, "id", String.valueOf(group.getId()));
                writeTag(writer, 8, "name", group.getName());
                writeTag(writer, 8, "creationDate", group.getCreationDate().toString());
                writeTag(writer, 8, "studentsCount", String.valueOf(group.getStudentsCount()));

                if (group.getTransferredStudents() != null) {
                    writeTag(writer, 8, "transferredStudents", String.valueOf(group.getTransferredStudents()));
                }
                if (group.getAverageMark() != null) {
                    writeTag(writer,8, "averageMark", String.valueOf(group.getAverageMark()));
                }
                if (group.getFormOfEducation() != null) {
                    writeTag(writer,  8, "formOfEducation", group.getFormOfEducation().name());
                }

                writer.write(getIndent(8)+"<coordinates>\n");
                writeTag(writer, 12, "x", String.valueOf(group.getCoordinates().getX()));
                writeTag(writer, 12, "y", String.valueOf(group.getCoordinates().getY()));
                writer.write(getIndent(8)+"</coordinates>\n");

                if (group.getGroupAdmin() != null) {
                    writer.write(getIndent(8) +"<groupAdmin>\n");
                    writeTag(writer,12,"name", group.getGroupAdmin().getName());
                    if (group.getGroupAdmin().getHeight()!=null) {
                        writeTag(writer, 12, "height", String.valueOf(group.getGroupAdmin().getHeight()));
                    }

                    writeTag(writer, 12, "eyeColor", group.getGroupAdmin().getEyeColor().name());
                    writeTag(writer,12,"nationality", group.getGroupAdmin().getNationality().name());


                    if (group.getGroupAdmin().getLocation() != null) {
                        writer.write(getIndent(12)+ "<location>\n");
                        writeTag(writer,16,"x", String.valueOf(group.getGroupAdmin().getLocation().getX()));
                        writeTag(writer,16,"y", String.valueOf(group.getGroupAdmin().getLocation().getY()));
                        writeTag(writer,16,"name", group.getGroupAdmin().getLocation().getName());
                        writer.write(getIndent(12) +"</location>\n");
                    }
                    writer.write(getIndent(8)+"</groupAdmin>\n");
                }
                writer.write(getIndent(4) + "</studyGroup>\n");

            }
            writer.write("</studyGroups>\n");
        }
    }

    /**
     * Вспомогательный метод для записи тега с отступами.
     */
    private void writeTag(BufferedWriter writer, int indent, String tagName, String value) throws IOException{
        if (value == null) return;

        writer.write(getIndent(indent));
        writer.write("<" + tagName + ">");
        writer.write(escapeXml(value));
        writer.write("</" + tagName +">\n");

    }

    /**
     * Вспомогательный метод для генерации отступов (пробелов).
     * @param spaces количество пробелов
     * @return строка, состоящая из нужного количества пробелов
     */
    private String getIndent(int spaces){
        return " ".repeat(spaces);
    }

    /**
     * Экранирует специальные символы XML.
     */
    private String escapeXml(String str){
        if (str== null) return "";
        return str.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

}
