import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItineraryProcessor {

    public int numberOfChanges = 0;
    public List<String> dataLines;

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
        processedLines = Utils.cleanUpWhitespace(processedLines);

        // Write processed lines to a file at the specified path
        Utils.writeFile(outputPath, processedLines);
        dataLines = processedLines;
    }

    private String processLine(String line) {
        line = line.replace('\b', '\n').replace('\f', '\n').replace('\r', '\n');
        // Convert airport codes to names
        line = airportLookup.replaceAirportsCodes(line);
        // Date and time formatting
        line = DateAndTimeFormatter.formatDateTime(line);
        numberOfChanges = airportLookup.numberOfChanges + DateAndTimeFormatter.numberOfChanges;
        return line;
    }
}
