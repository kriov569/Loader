import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class FileLoader {
    public static void main(String[] args) {
        String filename = "example.txt";
        String directory = "/path/to/directory/";
        String text = "Hello, world!";

        if (filename == null || filename.isEmpty() || directory == null || directory.isEmpty() || text == null || text.isEmpty()) {
            System.out.println("Некорректные входные данные.");
            return;
        }

        String filePath = directory + filename;

        try {
            saveFile(filePath, text);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении файла: " + e.getMessage());
            return;
        }

        try {
            String fileContent = findAndReadFile(filePath);
            System.out.println("Содержимое файла " + filename + ":");
            System.out.println(fileContent);
        } catch (IOException e) {
            System.out.println("Ошибка при поиске и чтении файла: " + e.getMessage());
        }
    }

    private static void saveFile(String filePath, String text) throws IOException {
        Path path = Paths.get(filePath);

        Path directoryPath = Paths.get(path.getParent().toString());
        Files.createDirectories(directoryPath);

        if (Files.exists(path)) {
            Files.delete(path);
        }

        Files.write(path, text.getBytes(StandardCharsets.UTF_8));

        BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
        long fileSize = attr.size();
        LocalDateTime fileTime = LocalDateTime.ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault());

        System.out.println("Файл " + path.getFileName() + " успешно сохранен.");
        System.out.println("Размер файла: " + fileSize + " байт");
        System.out.println("Время записи: " + fileTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    private static String findAndReadFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            throw new FileNotFoundException("Файл не найден: " + filePath);
        }

        byte[] fileBytes = Files.readAllBytes(path);
        return new String(fileBytes, StandardCharsets.UTF_8);
    }
}