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

        for (String dir : listDirectories) {
            createDirectory(dir);
        }

        createFile("C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\src\\", "Main.java");
        createFile("C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\src\\", "Util.java");
        createFile("C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\src\\", "temp.txt");

        File gamesFolder = new File("Games");
        File srcFolder = new File(gamesFolder, "src");
        File resFolder = new File(gamesFolder, "res");
        File savegamesFolder = new File(gamesFolder, "savegames");
        File tempFolder = new File(gamesFolder, "temp");

        boolean allDirectoriesCreated = checkIsCreateDirectory(gamesFolder) &&
                checkIsCreateDirectory(srcFolder) &&
                checkIsCreateDirectory(resFolder) &&
                checkIsCreateDirectory(savegamesFolder) &&
                checkIsCreateDirectory(tempFolder);
        if (allDirectoriesCreated) {
            File mainFolder = new File(srcFolder, "main");
            mainFolder.mkdirs();
            try {
                createFile(mainFolder.getAbsolutePath(), "Main.java");
                createFile(mainFolder.getAbsolutePath(), "Utils.java");
                createFile(tempFolder.getAbsolutePath(), "temp.txt");
            } catch (IOException e) {
                System.err.println("Не удалось создать файл: " + e.getMessage());
            }

            new File(resFolder, "drawables").mkdirs();
            new File(resFolder, "vectors").mkdirs();
            new File(resFolder, "icons").mkdirs();

        } else {
            System.err.println("Не удалось создать одну или несколько директорий");
        }


        GameProgress game1 = new GameProgress(100, 3, 10, 150.5);
        GameProgress game2 = new GameProgress(90, 4, 15, 200.0);
        GameProgress game3 = new GameProgress(80, 5, 20, 250.75);

        saveGame("C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\savegames\\game1.dat", game1);
        saveGame("C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\savegames\\game2.dat", game2);
        saveGame("C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\savegames\\game3.dat", game3);

        String zipFilePath = "C:\\Users\\HP\\Desktop\\savegames.zip";
        createZipArchive("C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\savegames\\", zipFilePath);

        deleteOriginalSaveFiles(savegamesFolder.getAbsolutePath());
        writeLogToFile(stringBuilder.toString(), new File("C:\\Users\\HP\\IdeaProjects\\Installation\\temp.txt"));


        File file = new File("C:\\Users\\HP\\IdeaProjects\\Installation\\Games\\src\\Main.java");
        if (file.canRead() && file.canWrite()) {
            System.out.println("Файл доступен для чтения и записи.");
        } else {
            System.out.println("Невозможно прочитать или записать файл.");
        }
    }

    private static void createDirectory(String path) {
        File f = new File(path);
        if (!f.exists()) {
            if (f.mkdirs()) {
                stringBuilder.append("Директория " + f + "успешно создана: ");
            } else {
                stringBuilder.append("Ошибка при создании: " + f.getName());
            }
        } else stringBuilder.append("Директория " + path + "уже создана.");
    }

    private static boolean checkIsCreateDirectory(File directory) {
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                stringBuilder.append("Директория" + directory.getName() + "успешно создана\n");
                return true;
            } else {
                stringBuilder.append("Ошибка при создании директории " + directory.getName() + "\n");
                return false;
            }
        } else {
            stringBuilder.append("Директория " + directory.getName() + "уже существует\n");
            return true;
        }
    }

    private static boolean checkAndCreateFile(File file) {
        try {
            if (file.createNewFile()) {
                System.out.println("Файл " + file.getName() + "успешно создан");
                return true;
            } else {
                System.err.println("Ошибка при создании файла " + file.getName() + "Фвйлл уже существует");
                return false;
            }
        } catch (IOException e) {
            System.err.println("Ошибка при создании файла" + file.getName() + ": " + e.getMessage());
            return false;
        }
    }

    private static void createFile(String path, String fileName) throws FileNotFoundException {
        File f = new File(path, fileName);
        try {
            if (!f.exists()) {
                if (f.createNewFile()) {
                    stringBuilder.append("Создан файл: ").append(fileName).append("\n");
                } else {
                    stringBuilder.append("Ошибка при создании файла ").append(f.getName()).append("\n");
                }
            } else stringBuilder.append("Файл " + fileName + "уже был создан ");
        } catch (IOException e) {
            stringBuilder.append("Ошибка при создании файла ").append(f.getName()).append(": ").append(e.getMessage()).append("\n");
            throw new FileNotFoundException();
        }
    }

    private static void createFileOrDirectory(String path) {
        File f = new File(path);
        if (!f.exists()) {
            if (f.mkdirs()) {
                stringBuilder.append("Директория " + f + "успешно создана: ");
            } else {
                stringBuilder.append("Ошибка при создании: " + f.getName());
            }
        } else {
            stringBuilder.append("Директория уже существует: " + f.getName());
        }
    }


    private static void saveGame(String filePath, GameProgress game) {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(game);
            System.out.println("Игра успешно сохранена в " + filePath);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении игры: " + e.getMessage());
        }
    }

    private static void writeLogToFile(String log, File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(stringBuilder.toString());
            System.out.println("Лог успешно записан в файл " + file.getName());
        } catch (IOException e) {
            System.err.println("Ошибка при записи лога в файл " + file.getName() + ": " + e.getMessage());
        }
    }


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
                    try (FileOutputStream fos = new FileOutputStream("C:\\Users\\HP\\IdeaProjects\\installation\\Games\\sro\\temp.txt")) {
                        fos.write(stringBuilder.toString().getBytes());
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        }
    }
}


