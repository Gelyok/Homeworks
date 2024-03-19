import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.zip.*;
import java.nio.file.*;
import java.io.File;

import static java.nio.file.Files.createDirectory;
import static java.nio.file.Files.createFile;

public class Main {
    private static StringWriter stringBuilder;

    public static void main(String[] args) throws FileNotFoundException {
        stringBuilder = new StringWriter();
        List<String> listDirectories = Arrays.asList("C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\src",
                "C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\src\\main",
                "C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\src\\test",
                "C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\res",
                "C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\res\\drawables",
                "C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\res\\vectors",
                "C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\res\\icons",
                "C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\savegames",
                "C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\temp");


        createFile("C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\src\\", "Main.java");
        createFile("C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\src\\", "Util.java");
        createFile("C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\src\\", "temp.txt");


        GameProgress game1 = new GameProgress(100, 3, 10, 150.5);
        GameProgress game2 = new GameProgress(90, 4, 15, 200.0);
        GameProgress game3 = new GameProgress(80, 5, 20, 250.75);

        saveGame("C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\savegames\\game1.dat", game1);
        saveGame("C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\savegames\\game2.dat", game2);
        saveGame("C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\savegames\\game3.dat", game3);

        for (String dir : listDirectories) {
            createDirectory(dir);
        }
    }

    private static void createDirectory(String path) {
        Path directoryPath = Paths.get(path);
        try {
            Files.createDirectories(directoryPath);
            stringBuilder.append("Директория " + directoryPath + " успешно создана\n");
        } catch (IOException e) {
            stringBuilder.append("Ошибка при создании директории " + directoryPath + ": " + e.getMessage() + "\n");
        }
    }

    //Проверяет существование и создает файл, если его нет
    private static boolean checkAndCreateFile(File file) {
        try {
            if (file.createNewFile()) {
                System.out.println("Файл " + file.getName() + " успешно создан");
                return true;
            } else {
                System.err.println("Ошибка при создании файла " + file.getName() + " Файл уже существует");
                return false;
            }
        } catch (IOException e) {
            System.err.println("Ошибка при создании файла" + file.getName() + ": " + e.getMessage());
            return false;
        }
    }

    //Создает файл по указанному пути и имени
    private static void createFile(String path, String fileName) throws FileNotFoundException {
        File f = new File(path, fileName);
        try {
            if (!f.exists()) {
                if (f.createNewFile()) {
                    stringBuilder.append("Создан файл: ").append(fileName).append("\n");
                } else {
                    stringBuilder.append("Ошибка при создании файла ").append(f.getName()).append("\n");
                }
            } else stringBuilder.append("Файл " + fileName + " уже был создан ");
        } catch (IOException e) {
            stringBuilder.append("Ошибка при создании файла ").append(f.getName()).append(": ").append(e.getMessage()).append("\n");
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
    private static void writeLogToFile(String log, File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(stringBuilder.toString());
            System.out.println("Лог успешно записан в файл " + file.getName());
        } catch (IOException e) {
            System.err.println("Ошибка при записи лога в файл " + file.getName() + ": " + e.getMessage());
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
            }
        }

        writeLogToFile(stringBuilder.toString(), new File("C:\\Users\\HP\\IdeaProjects\\Installation\\temp.txt"));
    }
}


