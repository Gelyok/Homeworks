import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.zip.*;
import java.nio.file.*;
import static java.nio.file.Files.createDirectory;
import static java.nio.file.Files.createFile;

public class Main {
    private static StringBuilder stringBuilder;

    public static void main(String[] args) throws FileNotFoundException {
        stringBuilder = new StringBuilder();
        List<String> listDirectories = Arrays.asList("C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\src",
                "C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\src\\main",
                "C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\src\\test",
                "C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\res",
                "C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\res\\drawables",
                "C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\res\\vectors",
                "C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\res\\icons",
                "C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\savegames",
                "C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\temp");

        for (String dir : listDirectories) {
            createDirectory(dir);
        }

        System.out.println(stringBuilder.toString());

        createFile("C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\src\\main", "Main.java");
        createFile("C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\src\\main", "Util.java");
        createFile("C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\temp", "temp.txt");

        writeLogToFile("Директория C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\src успешно создана\n" +
                        "Директория C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\src\\main успешно создана\n" +
                        "Директория C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\src\\test успешно создана\n" +
                        "Директория C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\res успешно создана\n" +
                        "Директория C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\res\\drawables успешно создана\n" +
                        "Директория C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\res\\vectors успешно создана\n" +
                        "Директория C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\res\\icons успешно создана\n" +
                        "Директория C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\savegames успешно создана\n" +
                        "Директория C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\temp успешно создана\n" +
                        "Файл Main.java был создан\n" +
                        "Файл Util.java был создан\n" +
                        "Файл temp.txt был создан",
                new File("C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\temp\\temp.txt"));

        createZipArchive("C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\savegames", "C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\savegames\\savegames.zip");

        GameProgress game1 = new GameProgress(100, 3, 10, 150.5);
        GameProgress game2 = new GameProgress(90, 4, 15, 200.0);
        GameProgress game3 = new GameProgress(80, 5, 20, 250.75);

        saveGame("C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\savegames\\game1.dat", game1);
        saveGame("C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\savegames\\game2.dat", game2);
        saveGame("C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\savegames\\game3.dat", game3);

    }

    private static void createDirectory(String path) {
        Path directoryPath = Paths.get(path);
        try {
            Files.createDirectories(directoryPath);
            stringBuilder.append("Директория " + directoryPath + " успешно создана\n");
        } catch (IOException e) {
            stringBuilder.append("Ошибка при создании директории " + directoryPath + ": " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }


    //Создает файл по указанному пути и имени
    private static void createFile(String directoryPath, String fileName) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(directory, fileName);
        try {
            if (file.createNewFile()) {
                stringBuilder.append("Создан файл: ").append(fileName).append("\n");
            } else {
                stringBuilder.append("Ошибка при создании файла ").append(fileName).append("\n");
            }
        } catch (IOException e) {
            stringBuilder.append("Ошибка при создании файла ").append(fileName).append(": ").append(e.getMessage()).append("\n");
            e.printStackTrace();
        }
    }

    //Сохраняет игровой процесс в файле
    private static void saveGame(String filePath, GameProgress game) {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(game);
            System.out.println("Игра успешно сохранена в " + filePath);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении игры: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Записывает лог в файл
    private static void writeLogToFile (String log, File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(log);
            System.out.println("Лог успешно записан в файл " + file.getName());
        } catch (IOException e) {
            System.err.println("Ошибка при записи лога в файл " + file.getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Создает zip-архив из указанной директории и сохраняет его по указанному пути
    private static void createZipArchive(String sourceFolderPath, String zipFilePath) {
        File sourceFolder = new File(sourceFolderPath);
        if (!sourceFolder.exists() || !sourceFolder.isDirectory()) {
            System.err.println("Ошибка: указанный путь не является директорией или не существует.");
            return;
        }

        List<File> sourceFiles = Arrays.asList(sourceFolder.listFiles());

        try {
            FileOutputStream fos = new FileOutputStream(zipFilePath);
            ZipOutputStream zos = new ZipOutputStream(fos);

            for (File sourceFile : sourceFiles) {
                addFilesToZip(sourceFile, sourceFile.getName(), zos);
            }

            zos.close();
            fos.close();

            System.out.println("Архив успешно создан: " + zipFilePath);
        } catch (IOException e) {
            System.err.println("Ошибка при создании архива zip: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Добавляет файлы в zip-архив
    private static void addFilesToZip(File sourceFile, String parentPath, ZipOutputStream zos) throws IOException {
        if (sourceFile.isDirectory()) {
            File[] files = sourceFile.listFiles();
            if (files != null) {
                for (File file : files) {
                    addFilesToZip(file, parentPath + File.separator + file.getName(), zos);
                }
            }
        } else {
            byte[] buffer = new byte[1024];
            FileInputStream fis = new FileInputStream(sourceFile);
            zos.putNextEntry(new ZipEntry(parentPath));
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
            fis.close();
        }
    }

    //Удаляет оригинальные файлы сохранения
    private static void deleteOriginalSaveFiles(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Ошибка: указанный путь не является директорией или не существует.");
            return;
        }

        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && !file.getName().equals("savegames.zip")) {
                    if (file.delete()) {
                        System.out.println("Удален файл: " + file.getName());
                    } else {
                        System.err.println("Не удалось удалить файл: " + file.getName());
                    }
                }

                    writeLogToFile(stringBuilder.toString(), new File(folderPath + "\\temp.txt"));
                }
            }
        }
    }





