import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;


public class Utils {
    // Reads the contents of a text file and returns a list of lines.
    public static List<String> readFile(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }

    //Writes a list of lines to a file.
    public static void writeFile(String filePath, List<String> lines) throws IOException {
        Files.write(Paths.get(filePath), lines);
    }

    public static void printToConsole(List<String> lines) {
        for (String s : lines) {
            System.out.println(s);
        }
    }

    //Reads a CSV file and returns the data as a list of string arrays.
    public static List<String[]> readCSV(String csvPath) throws IOException {
        List<String> lines = readFile(csvPath);
        List<String[]> csvData = new ArrayList<>();
        for (String line : lines) {
            csvData.add(line.split(", "));
        }
        return csvData;
    }

    // Removes extra spaces and empty lines from a list of strings.
    public static List<String> cleanUpWhitespace(List<String> lines) {
        List<String> cleaned = new ArrayList<>();
        boolean lastLineWasBlank = false;

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                if (!lastLineWasBlank) {
                    cleaned.add("");
                    lastLineWasBlank = true;
                }
            } else {
                cleaned.add(line);
                lastLineWasBlank = false;
            }
        }
        

        

        return cleaned;
    }
}
