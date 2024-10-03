import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AirportLookup {

    private Map<String, String> iataMap; // Map IATA
    private Map<String, String> icaoMap; // Map ICAO
    private Map<String, String> iataCityMap; // Map of cities
    private Map<String, String> icaoCityMap; // Map of cities

    public int numberOfChanges = 0;

    public AirportLookup(String csvFilePath) throws IOException, IllegalArgumentException {
        iataMap = new HashMap<>();
        icaoMap = new HashMap<>();
        iataCityMap = new HashMap<>();
        icaoCityMap = new HashMap<>();
        loadAirports(csvFilePath); // Loading from CSV
    }

    public String replaceAirportsCodes(String line) {
 
        return convertCodes(line);
    }

    // Method for loading from CSV
    private void loadAirports(String csvFilePath) throws IOException, IllegalArgumentException{
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            String[] headers = br.readLine().split(","); // Reading headers
            int nameIndex = -1, iataIndex = -1, icaoIndex = -1, cityIndex = -1;

            // Determine the order of columns
            for (int i = 0; i < headers.length; i++) {
                switch (headers[i].trim().toLowerCase()) {
                    case "name":
                        nameIndex = i;
                        break;
                    case "iata_code":
                        iataIndex = i;
                        break;
                    case "icao_code":
                        icaoIndex = i;
                        break;
                    case "municipality":
                        cityIndex = i;
                        break;
                }
            }

            // Checking the correctness of column indices
            if (nameIndex == -1 || iataIndex == -1 || icaoIndex == -1 || cityIndex == -1) {
                throw new IllegalArgumentException("Airport lookup malformed: missing columns.");
            }

            // Reading data lines
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length <= Math.max(nameIndex, Math.max(iataIndex, icaoIndex))) {
                    throw new IllegalArgumentException("Airport lookup malformed: incomplete row.");
                }

                String name = columns[nameIndex].trim();
                String iataCode = columns[iataIndex].trim();
                String icaoCode = columns[icaoIndex].trim();
                String city = columns[cityIndex].trim();

                // Add airport to maps if codes are not empty
                if (!iataCode.isEmpty()) {
                    iataMap.put(iataCode, name);
                }
                if (!icaoCode.isEmpty()) {
                    icaoMap.put(icaoCode, name);
                }
                if (!city.isEmpty()) {
                    iataCityMap.put(iataCode, city);
                    icaoCityMap.put(icaoCode, city);
                }

            }
        }
    }

    private String getAirportAndCount (String code, String mask) {
        String replacement = null;
        switch (mask) {
            case "##":
                replacement = icaoMap.getOrDefault(code, null);
                break;
            case "#":
                replacement = iataMap.getOrDefault(code, null);
                break;
            case "*##":
                replacement = icaoCityMap.getOrDefault(code, null);
                break;
            case "*#":
                replacement = iataCityMap.getOrDefault(code, null);
            default:
                break;
        }

        if (replacement!=null) {
            numberOfChanges++;
            return replacement;            
        } else {
            return (mask+code);
        }
        
        //return null;
    }

    public String convertCodes(String input) {
        StringBuilder result = new StringBuilder();
        int length = input.length();
        
        for (int i = 0; i < length; ) {
            if (input.charAt(i) == '#') {
                if (i + 1 < length && input.charAt(i + 1) == '#') {
                    // ## - ICAO
                    i += 2;
                    String code = extractCode(input, i);
                    //result.append(icaoMap.getOrDefault(code, "##"+code));
                    //String replacement = icaoMap.getOrDefault(code, null);
                    result.append(getAirportAndCount(code, "##"));
                    i += code.length();
                } else {
                    // # - IATA
                    i++;
                    String code = extractCode(input, i);
                    //result.append(iataMap.getOrDefault(code, "#"+code));
                    result.append(getAirportAndCount(code, "#"));
                    i += code.length();
                }
            } else if (input.charAt(i) == '*' && i + 1 < length && input.charAt(i + 1) == '#') {
                if (i + 2 < length && input.charAt(i + 2) == '#') {
                    // *## - City ICAO
                    i += 3;
                    String code = extractCode(input, i);
                    //result.append(icaoCityMap.getOrDefault(code, "*##"+code));
                    result.append(getAirportAndCount(code, "*##"));
                    i += code.length();
                } else {
                    // *# - City IATA
                    i += 2;
                    String code = extractCode(input, i);
                    //result.append(iataCityMap.getOrDefault(code, "*#"+code));
                    result.append(getAirportAndCount(code, "*#"));
                    i += code.length();
                }
            } else {
                // Add symbol to the result
                result.append(input.charAt(i));
                i++;
            }
        }

        return result.toString();
    }

    // Helper method to extract airport code from string
    private static String extractCode(String input, int startIndex) {
        StringBuilder code = new StringBuilder();
        while (startIndex < input.length() && Character.isLetterOrDigit(input.charAt(startIndex))) {
            code.append(input.charAt(startIndex));
            startIndex++;
        }
        return code.toString();
    }

}