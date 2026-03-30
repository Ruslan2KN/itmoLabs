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
     * Перед записью производит проверки на корректность пути, отсутствие директории с таким именем
     * и наличие прав на запись.
     *
     * @param collection коллекция объектов {@link StudyGroup}, которую необходимо сохранить
     * @param filePath   строковый путь к целевому файлу для сохранения
     * @throws IOException           если возникает непредвиденная ошибка ввода-вывода при работе с файлом
     * @throws FileNotFoundException если путь пустой, указывает на директорию или к файлу нет прав доступа на запись
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
                writer.write("  <studyGroup>\n");
                writer.write("    <id>" + group.getId() + "</id>\n");
                writer.write("    <name>" + group.getName() + "</name>\n");

                writer.write("    <coordinates>\n");
                writer.write("    <x>" + group.getCoordinates().getX() + "</x>\n");
                writer.write("    <y>" + group.getCoordinates().getY() + "</y>\n");
                writer.write("    </coordinates>\n");

                writer.write("    <creationDate>" + group.getCreationDate().toString() + "</creationDate>\n");
                writer.write("    <studentsCount>" + group.getStudentsCount() + "</studentsCount>\n");

                if (group.getTransferredStudents() != null) {
                    writer.write("    <transferredStudents>" + group.getTransferredStudents() + "</transferredStudents>\n");

                }
                if (group.getAverageMark() != null) {
                    writer.write("    <averageMark>" + group.getAverageMark() + "</averageMark>\n");
                }
                if (group.getFormOfEducation() != null) {
                    writer.write("    <formOfEducation>" + group.getFormOfEducation().name() + "</formOfEducation>\n");
                }

                if (group.getGroupAdmin() != null) {
                    writer.write("    <groupAdmin>\n");
                    writer.write("       <name>" + group.getGroupAdmin().getName() + "</name>\n");
                    writer.write("       <height>" + group.getGroupAdmin().getHeight() + "</height>\n");

                    if (group.getGroupAdmin().getEyeColor() != null) {
                        writer.write("       <eyeColor>" + group.getGroupAdmin().getEyeColor().name() + "</eyeColor>\n");
                    }
                    if (group.getGroupAdmin().getNationality() != null) {
                        writer.write("       <nationality>" + group.getGroupAdmin().getNationality().name() + "</nationality>\n");
                    }
                    if (group.getGroupAdmin().getLocation() != null) {
                        writer.write("       <location>\n");
                        writer.write("          <x>" + group.getGroupAdmin().getLocation().getX() + "</x>\n");
                        writer.write("          <y>" + group.getGroupAdmin().getLocation().getY() + "</y>\n");
                        writer.write("          <name>" + group.getGroupAdmin().getLocation().getName() + "</name>\n");
                        writer.write("       </location>\n");


                    }
                    writer.write("     </groupAdmin>\n");
                }
                writer.write("  </studyGroup>\n");

            }
            writer.write("</studyGroups>\n");
        }
    }
}
