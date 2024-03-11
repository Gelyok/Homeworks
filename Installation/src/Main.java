import java.io.*;

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

        saveGame("C:\\Users\\HP\\IdeaProjects\\Installation\\Games", game1);
        saveGame("C:\\Users\\HP\\IdeaProjects\\Installation\\Games", game2);
        saveGame("C:\\Users\\HP\\IdeaProjects\\Installation\\Games", game3);

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
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(game);
            System.out.println("Game saved successfully to " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
        }
    }
    private static void writeLogToFile(String log, File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(log);
            System.out.println("Лог успешно записан в файл " + file.getName());
        } catch (IOException e) {
            System.err.println("Ошибка при записи лога в файл " + file.getName() + ": " + e.getMessage());
        }
    }
}
