package filereader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import filereader.datamanagement.FileParser;
import filereader.datamanagement.ParserFactory;
import filereader.logging.Logger;
import filereader.userinterface.UI;
import filereader.util.MemoryStorage;
import java.util.Comparator;



public class Main {
    private static Set<String> usedPrefixes = new HashSet<>();
    public static void main(String[] args) throws IOException {
        usedPrefixes.clear(); // clear the set
        Logger logger = Logger.getInstance();
        MemoryStorage memoryStorage = MemoryStorage.getInstance();
        ParserFactory parserFactory = new ParserFactory();
        UI userInterface = new UI();


        if (args.length == 0) {
            System.out.println("No command input arguments were provided.");
        } else {
            // Sort the args array, log file return as the first index
            Arrays.sort(args, Comparator.comparing(arg -> arg.toLowerCase().startsWith("--log=") ? 0 : 1));

            // Process runtime arguments
            for (String arg : args) {
                try {

                    // Check if the prefix is already used
                    String prefix = arg.split("=")[0].substring(2).toLowerCase();
                    if (!usedPrefixes.add(prefix)) {
                        throw new IOException("This argument is provided more than once: " + prefix + ". The completed file input is: " + arg);
                    }


                    // Process log file first as the 1st index to ensure logging the command input
                    if (arg.toLowerCase().startsWith("--log=")) {
                        String logFileName = arg.substring(6);
                        logger.setOutputDestination(logFileName);
                        logger.logEvent("Command input arguments: " + String.join(" ", args));  //logger input args
                        logger.logEvent("Opening log file: " + logFileName);
                    } else {
                        // Process data file
                        FileParser inputFile = parserFactory.createParser(arg);
                        inputFile.parserAndStoreInMemory(memoryStorage);
                        logger.logEvent("Command input arguments: " + String.join(" ", args));  //No logger, print to console
                        logger.logEvent("Opening data file: " + inputFile.getFileName());
                    }
                } catch (IOException e) {
                    logger.logEvent("Error processing argument: " + arg + ". " + e.getMessage());
                    throw new IOException(e.getMessage());
                }

            }
        }

        // Start the application
            userInterface.start();

    }
}
