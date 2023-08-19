package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class TaskManager {
    static final String FILE_NAME = "tasks.csv";
    static final String[] OPTIONS = {"add", "remove", "list", "exit"};
    static String[][] tasks;

    public static void main(String[] args) {
        tasks = loadDataToTab(FILE_NAME);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printOptions(OPTIONS);
            String input = scanner.nextLine();
            switch (input) {
                case "exit":
                    saveAndExit(FILE_NAME, tasks);
                    System.out.println(ConsoleColors.RED + "bye bye" + ConsoleColors.RESET);
                    System.exit(0);
                    break;
                case "add":
                    addTask();
                    break;
                case "remove":
                    removeTask();
                    break;
                case "list":
                    printTab(tasks);
                    break;
                default:
                    System.out.println("Please select a correct option");
            }
        }
    }

    public static void printOptions(String[] tab) {
        System.out.println(ConsoleColors.BLUE);
        System.out.println("Please select an option" + ConsoleColors.RESET);
        for (String option : tab) {
            System.out.println(option);
        }
    }

    public static String[][] loadDataToTab(String fileName) {
        Path path = Paths.get(fileName);
        if (!Files.exists(path)) {
            System.out.println("File not exist");
            System.exit(0);
        }
        String[][] tab = null;
        try {
            List<String> strings = Files.readAllLines(path);
            tab = new String[strings.size()][strings.get(0).split(",").length];

            for (int i = 0; i < strings.size(); i++) {
                String[] split = strings.get(i).split(",");
                for (int j = 0; j < split.length; j++) {
                    tab[i][j] = split[j];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tab;
    }

    public static void printTab(String[][] tab) {
        for (int i = 0; i < tab.length; i++) {
            System.out.print((ConsoleColors.CYAN + (i + 1) + ": " + ConsoleColors.RESET));
            for (int j = 0; j < tab[i].length; j++) {
                System.out.print(ConsoleColors.CYAN + tab[i][j] + " " + ConsoleColors.RESET);
            }
            System.out.println();
        }
    }

    public static void addTask() {

        Scanner scanner = new Scanner(System.in);
        String description = "";
        System.out.println("Please add task description");
        description = scanner.nextLine();

        String date = "";
        //System.out.println("Please add due date format yyyy-mm-dd");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        boolean validDate = false;
        while (!validDate) {
            System.out.println("Please add due date format yyyy-mm-dd");
            date = scanner.nextLine();
            try {
                dateFormat.parse(date);
                if (date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    validDate = true;
                } else {
                    System.out.println("invalid date format, add again in format yyyy-mm-dd");
                }
            } catch (ParseException e) {
                System.out.println("invalid date format, add again in format yyyy-mm-dd");
            }
        }


        try {
            Date dateCheck = dateFormat.parse(date);
        } catch (ParseException e) {
            System.out.println("invalid date format");
        }

        String important = "";
        System.out.println("Is this task important: true/false");
        important = scanner.nextLine();

        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length - 1] = new String[3];
        tasks[tasks.length - 1][0] = description;
        tasks[tasks.length - 1][1] = date;
        tasks[tasks.length - 1][2] = important;
    }

    public static void removeTask() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please type number of line to remove");
        int indexToRemove = Integer.parseInt(scanner.nextLine());

        if (indexToRemove >= 0 && indexToRemove < tasks.length) {
            tasks = ArrayUtils.remove(tasks, indexToRemove);
            System.out.println("task removed");
        } else {
            System.out.println("invalid index");
        }
    }

    public static void saveAndExit(String fileName, String[][] tab) {
        Path path = Paths.get(fileName);
        String[] lines = new String[tab.length];

        for (int i = 0; i < tab.length; i++) {
            lines[i] = String.join(",", tab[i]);
        }
        try {
            Files.write(path, Arrays.asList(lines));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}