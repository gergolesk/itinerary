//import java.io.FileNotFoundException;
import java.io.IOException;



public class Prettifier {

    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public static void main(String[] args) {
        //System.out.println("Current working directory: " + System.getProperty("user.dir"));

        String inputPath;
        String outputPath;
        String airportsPath;

        boolean needToPrint = false;
        
        if (args.length == 4 && args[0].equals("-o")){
            needToPrint = true;
            inputPath = args[1];
            outputPath = args[2];
            airportsPath = args[3];
        } else if ((args.length == 1 && args[0]=="-h") || args.length!=3) {
            printHelp();
            return;
        } else {
            inputPath = args[0];
            outputPath = args[1];
            airportsPath = args[2];
        }   
     
       
       
        try {
            // Try to create an object AirportLookup
            AirportLookup airportLookup = new AirportLookup(airportsPath);
            
            try {
                // Try to create and use ItineraryProcessor
                ItineraryProcessor processor = new ItineraryProcessor(airportLookup);
                processor.processItinerary(inputPath, outputPath);
                if (needToPrint) {
                    Utils.printToConsole(processor.dataLines);
                }
                System.out.println(GREEN + "File processed successfully. Number of corrections: " + CYAN + processor.numberOfChanges + RESET);
                
                
            } catch (IOException e) {
                // Processing IOException from ItineraryProcessor
                System.err.println(RED+"Input not found"+RESET);
            }
        
        } catch (IOException e) {
            // Processing IOException from AirportLookup
            System.err.println(RED + "Airport lookup not found" + RESET);
        } catch (IllegalArgumentException e) {
            // Processing IllegalArgumentException
            System.err.println(RED+"Airport lookup malformed"+RESET);
        }     
    }

    private static void printHelp() {
        //System.out.println("Itenerary usage:");
        //System.out.println("java Prettifier.java ./input.txt ./output.txt ./airport-lookup.csv");

        System.out.println(BLUE+"Itinerary usage:"+RESET);
        System.out.println("1. Basic usage:");
        System.out.println(YELLOW+"   java Prettifier.java ./input.txt ./output.txt ./airport-lookup.csv"+RESET);
        System.out.println("2. Output to console:");
        System.out.println(YELLOW+"   java Prettifier.java -o ./input.txt ./output.txt ./airport-lookup.csv"+RESET);
        System.out.println("3. Show help:");
        System.out.println(YELLOW+"   java Prettifier.java -h"+RESET);
    }
}