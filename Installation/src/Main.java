import java.io.*;
import java.util.zip.*;

public class Main {
    public static void main(String[] args) {

        File gamesFolder = new File("Games");
        File srcFolder = new File(gamesFolder, "src");
        File resFolder = new File(gamesFolder, "res");
        File savegamesFolder = new File(gamesFolder, "savegames");
        File tempFolder = new File(gamesFolder, "temp");

        createDirectory(gamesFolder);
        createDirectory(srcFolder);
        createDirectory(resFolder);
        createDirectory(savegamesFolder);
        createDirectory(tempFolder);

        File mainFolder = new File(srcFolder, "main");
        createDirectory(mainFolder);
        createFile(new File(mainFolder, "Main.java"));
        createFile(new File(mainFolder, "Utils.java"));

        createDirectory(new File(resFolder, "drawables"));
        createDirectory(new File(resFolder, "vectors"));
        createDirectory(new File(resFolder, "icons"));

        createFile(new File(tempFolder, "temp.txt"));

        GameProgress game1 = new GameProgress(100, 3, 10, 150.5);
        GameProgress game2 = new GameProgress(90, 4, 15, 200.0);
        GameProgress game3 = new GameProgress(80, 5, 20, 250.75);

        saveGame("C:\\Users\\HP\\Desktop", game1);
        saveGame("C:\\Users\\HP\\Desktop", game2);
        saveGame("C:\\Users\\HP\\Desktop", game3);

        String zipFilePath = "C:\\Users\\HP\\Desktop\\savegames.zip";
        createZipArchive(savegamesFolder.getAbsolutePath(), zipFilePath);

        deleteOriginalSaveFiles(savegamesFolder.getAbsolutePath());

    }

    private static void createDirectory(File directory) {
        if (!directory.exists()) {
            if (directory.mkdir()) {
                System.out.println("Директория " + directory.getName() + " успешно создана.");
            } else {
                System.err.println("Ошибка при создании директории " + directory.getName());
            }
        }
    }

    private static void createFile(File file) {
        try {
            if (file.createNewFile()) {
                System.out.println("Файл " + file.getName() + " успешно создан.");
            } else {
                System.err.println("Ошибка при создании файла " + file.getName());
            }
        } catch (IOException e) {
            System.err.println("Ошибка при создании файла " + file.getName() + ": " + e.getMessage());
        }
    }

    private static void saveGame(String filePath, GameProgress game) {
        try (FileOutputStream fos = new FileOutputStream(filePath + File.separator + "game.dat");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(game);
            System.out.println("Игра успешно сохранена в " + filePath);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении игры: " + e.getMessage());
        }
    }

    private static void writeLogFile(String log, File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(log);
            System.out.println("Лог успешно записан в файл " + file.getName());
        } catch (IOException e) {
            System.err.println("Ошибка при записи лога в файл " + file.getName() + ": " + e.getMessage());
        }
    }

    private static void createZipArchive(String sourceFolderPath, String zipFilePath) {
        try {
            FileOutputStream fos = new FileOutputStream(zipFilePath);
            ZipOutputStream zos = new ZipOutputStream(fos);
            File sourceFolder = new File(sourceFolderPath);
            addFilesToZip(sourceFolder, sourceFolder.getName(), zos);
            zos.close();
            fos.close();

            System.out.println("Архив успешно создан: " + zipFilePath);
        } catch (IOException e) {
            System.err.println("Ошибка создания архива zip:" + e.getMessage());
        }
    }

    private static void addFilesToZip(File sourceFile, String parentPath, ZipOutputStream zos) throws IOException {
        if (sourceFile.isDirectory()) {
            for (File file : sourceFile.listFiles()) {
                addFilesToZip(file, parentPath + File.separator + file.getName(), zos);
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

    private static void deleteOriginalSaveFiles(String folderPath) {
        File folder = new File(folderPath);
        for (File file : folder.listFiles()) {
            if (file.isFile()) {
                if (!file.getName().equals("savegames.zip")) {
                    if (file.delete()) {
                        System.out.println("Удален файл: " + file.getName());
                    } else {
                        System.err.println("Не удалось удалить файл: " + file.getName());
                    }
                }
            }
        }
    }
}
