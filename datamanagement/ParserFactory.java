package filereader.datamanagement;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class ParserFactory {
    private static Set<String> usedPrefixes = new HashSet<>();
    /**
     * This method is the initial step in of parsing file process
     * The expected input format is --name=fileName
     * Return a fileParser object if it valid, otherwise throw error
     * @param input as “--name=value”
     * @return a FileParser object
     * @throws IOException
     */
    public static FileParser createParser(String input) throws IOException {

        String[] parts = input.split("=");

        // Argument input must have 2 elements to match the form “--name=value”, otherwise throw an error
        if (parts.length == 2) {
            String prefix = parts[0].substring(2).toLowerCase();
            String fileName = parts[1].toLowerCase();

            // Check if the file exists and is readable, otherwise throw an error
            if (!isFileReadable(fileName)) {
                throw new IOException("File " + fileName + " does not exist or cannot be opened for reading.");
            }


            // Filename extension must be either “csv” or “json” and case-insensitive, otherwise throw an error
            if (isCsvFile(fileName)) {

                // Prefix of the argument must be one of these covid, population, or properties, otherwise throw an error
                switch (prefix) {
                    case "covid":
                        return new CSVCovidReader(fileName);
                    case "population":
                        return new CSVPopulationReader(fileName);
                    case "properties":
                        return new CSVPropertiesReader(fileName);
                    default:
                        throw new IOException("Prefix of the argument must be one of these covid, population, or properties, you entered: " + prefix);
                }
            } else if (prefix.equals("covid") && isJsonFile(fileName)) {
                return new JsonCovidReader(fileName);
            } else {
                throw new IOException("Filename extension must be either “csv” or “json”, you entered: " + fileName);
            }
        }
        throw new IOException("Argument input must have 2 elements to match the form `--name=value`, you entered: " + input);
    }



    // Check if the file exists and is readable
    private static boolean isFileReadable(String fileName) {
        Path filePath = Paths.get(fileName);
        return Files.exists(filePath) && Files.isReadable(filePath);
    }

    private static boolean isJsonFile(String fileName) {
        return fileName.toLowerCase().endsWith(".json");
    }

    private static boolean isCsvFile(String fileName) {
        return fileName.toLowerCase().endsWith(".csv");
    }

}
