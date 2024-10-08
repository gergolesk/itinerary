import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItineraryProcessor {

    public int numberOfChanges = 0;
    public List<String> dataLines;
    private int changes; 

    // Object for identifying airports by their code
    private AirportLookup airportLookup;

    // Class constructor, accepts an AirportLookup object to convert airport codes
    public ItineraryProcessor(AirportLookup airportLookup) {
        this.airportLookup = airportLookup;
    }

    public void processItinerary(String inputPath, String outputPath) throws IOException {
        // Read all lines from the input file
        List<String> lines = Utils.readFile(inputPath);
        List<String> processedLines = new ArrayList<>();

        // Process each line from the file
        for (String line : lines) {
            String processedLine = processLine(line);
            processedLines.add(processedLine);
        }

        // Cleaning up extra spaces and empty lines (for example, if there are several in a row)
        //processedLines = Utils.cleanUpWhitespace(processedLines);
        processedLines = cleanUpWhitespace(processedLines);

        // Write processed lines to a file at the specified path
        Utils.writeFile(outputPath, processedLines);
        dataLines = processedLines;
    }

    private String processLine(String line) {
        //line = line.replace('\b', '\n').replace('\f', '\n').replace('\r', '\n').replace('\u000B', '\n');
        line = trimVerticalWhiteSpace(line);
        
        // Convert airport codes to names
        line = airportLookup.replaceAirportsCodes(line);
        // Date and time formatting
        line = DateAndTimeFormatter.formatDateTime(line);
        numberOfChanges = changes + airportLookup.numberOfChanges + DateAndTimeFormatter.numberOfChanges;
        return line;
    }

    private String trimVerticalWhiteSpace(String input) {
        //int changes = 0;

        //  Replacing \v, \f и \r на \n
        int countV = countOccurrences(input, "\u000B");
        int countF = countOccurrences(input, "\f");
        int countR = countOccurrences(input, "\r");

        String result = input.replace("\u000B", "\n")
                             .replace("\f", "\n")
                             .replace("\r", "\n");

        // increase counter of changes
        changes += countV + countF + countR;
        return result;
    }

    // Method for counting the number of occurrences of a substring in a string
    private static int countOccurrences(String text, String substring) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(substring, index)) != -1) {
            count++;
            index += substring.length();
        }
        return count;
    }

    private List<String> cleanUpWhitespace(List<String> lines) {
        List<String> cleaned = new ArrayList<>();
        boolean lastLineWasBlank = false;
        boolean sec = true;

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                if (!lastLineWasBlank) {
                    cleaned.add("");
                    lastLineWasBlank = true;
                    //numberOfChanges++;
                } else {
                    if (sec){
                        numberOfChanges++;
                        sec = false;
                    }
                    
                }
            } else {
                cleaned.add(line);
                lastLineWasBlank = false;
                sec = true;
            }
        }  
        /*
        if (cleaned.size() > 0 && cleaned.get(cleaned.size() - 1).trim().isEmpty()) {
            cleaned.remove(cleaned.size() - 1); // Удаляем последнюю пустую строку
            numberOfChanges--; // Уменьшаем счетчик изменений
        }
        */
        return cleaned;
    }
}
