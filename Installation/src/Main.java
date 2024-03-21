import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.*;
import java.nio.file.*;
import java.io.Serializable;
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


        createFile("C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\src\\main", "Main.java");
        createFile("C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\src\\main", "Util.java");
        createFile("C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\temp", "temp.txt");

        try (FileOutputStream fos = new FileOutputStream("C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\temp\\temp.txt")) {
            fos.write(stringBuilder.toString().getBytes());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        GameProgress game1 = new GameProgress(100, 3, 10, 150.5);
        GameProgress game2 = new GameProgress(90, 4, 15, 200.0);
        GameProgress game3 = new GameProgress(80, 5, 20, 250.75);

        saveGame("C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\savegames\\game1.dat", game1);
        saveGame("C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\savegames\\game2.dat", game2);
        saveGame("C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\savegames\\game3.dat", game3);

        createZipArchive("C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\savegames", "C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\savegames\\savegames.zip");


    }

    private static void createDirectory(String path) {
        File file = new File(path);
        if (file.exists()) {
            stringBuilder.append("Директория " + path + " успешно создана");
        } else stringBuilder.append("Директория " + path + " не создана");
    }

    //Создает файл по указанному пути и имени
    private static void createFile(String directoryPath, String fileName) {
        File directory = new File(directoryPath, fileName);
        if (directory.exists()) {
            stringBuilder.append("Файл " + fileName + " успешно создан");
        } else stringBuilder.append("Файл " + fileName + " не создан");
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


    //Создает zip-архив из указанной директории и сохраняет его по указанному пути
    private static void createZipArchive(String sourceFolderPath, String zipFilePath) {
        File sourceFolder = new File(sourceFolderPath);
        if (!sourceFolder.exists() || !sourceFolder.isDirectory()) {
            System.err.println("Ошибка: указанный путь не является директорией или не существует.");
            return;
        }

        File[] files = sourceFolder.listFiles();
        List<File> sourceFiles = new ArrayList<>(Arrays.asList(files));

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
}



