import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Scanner;

public class TextSorter {

    public static void main(String[] args) {
        String inputFilePath = "src/main/resources/input.txt";  // Входной файл
        String outputFilePath = "src/main/resources/output.txt"; // Выходной файл

        Scanner scanner = new Scanner(System.in);

        // Запрос типа сортировки у пользователя
        System.out.println("Выберите тип сортировки:");
        System.out.println("1. По алфавиту");
        System.out.println("2. По длине строк");
        System.out.println("3. По слову");

        String sortOption = scanner.nextLine().trim();

        int wordIndex = -1;
        if ("3".equals(sortOption)) {
            System.out.print("Введите номер слова (начиная с 1): ");
            wordIndex = scanner.nextInt() - 1;
        }

        // Чтение строк из файла и обработка ошибок
        try {
            File inputFile = new File(inputFilePath);
            if (!inputFile.exists()) {
                System.err.println("Входной файл не найден: " + inputFilePath);
                return;
            }

            List<String> lines = Files.readAllLines(Paths.get(inputFilePath));
            Map<String, Integer> lineCounts = countLineOccurrences(lines);
            List<String> linesWithCounts = lines.stream()
                    .map(line -> line + " " + lineCounts.get(line))
                    .collect(Collectors.toList());

            Path outputPath = Paths.get(outputFilePath);
            Files.createDirectories(outputPath.getParent());

            // Сортировка в зависимости от выбранной опции
            switch (sortOption) {
                case "1":
                    linesWithCounts.sort(Comparator.comparing(line -> line.split(" ")[0]));
                    break;
                case "2":
                    linesWithCounts.sort(Comparator.comparingInt(String::length));
                    break;
                case "3":
                    if (wordIndex < 0) {
                        System.err.println("Номер слова должен быть положительным.");
                        return;
                    }
                    final int wordIndexFinal = wordIndex; // Делает wordIndex эффективно финальным
                    linesWithCounts.sort(Comparator.comparing(line -> getWord(line, wordIndexFinal)));
                    break;
                default:
                    System.err.println("Неверная опция сортировки.");
                    return;
            }

            // Запись отсортированных данных в выходной файл
            Files.write(Paths.get(outputFilePath), linesWithCounts);
            System.out.println("Файл отсортирован и сохранен в: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("Ошибка обработки файла: " + e.getMessage());
        }
    }

    private static Map<String, Integer> countLineOccurrences(List<String> lines) {
        Map<String, Integer> lineCounts = new HashMap<>();
        for (String line : lines) {
            lineCounts.put(line, lineCounts.getOrDefault(line, 0) + 1);
        }
        return lineCounts;
    }

    private static String getWord(String line, int index) {
        String[] words = line.split("\\s+");
        return index >= 0 && index < words.length ? words[index] : "";
    }
}
