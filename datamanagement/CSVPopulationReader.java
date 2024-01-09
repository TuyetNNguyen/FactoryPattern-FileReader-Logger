package edu.upenn.cit594.datamanagement;
import edu.upenn.cit594.util.MemoryStorage;
import edu.upenn.cit594.util.PopulationObject;
import java.io.IOException;
import java.util.List;


/**
 * Concrete class parsing Csv population file
 */
public class CSVPopulationReader extends CSVFileReader {

    public CSVPopulationReader(String populationFile) {
        super(populationFile);
    }

    /**
     * Parses the CsvCode file and stores specific valid fields in MemoryStorage
     * @param memory
     * @throws IOException
     */
    public void parserAndStoreInMemory(MemoryStorage memory) throws IOException {
        // Get the whole list of lines
        List<String> lines = identifyLines(fileName);

        // Hw instruction mentions if a csv file is valid, the first row is always has a header
        List<String> headerColumns = processFields(lines.get(0));

        int zipCodeIndex = findColumnIndex(headerColumns, "zip_code");
        int populationIndex = findColumnIndex(headerColumns, "population");

        if (zipCodeIndex == -1 || populationIndex == -1) {
            System.err.println("Required columns not found in the header in Population csv file.");
        } else {

            // Iterate through the list of lines
            for (int i = 1; i < lines.size(); i++) {
                // Process a line and returns a list of fields
                List<String> fields = processFields(lines.get(i));

                // Extract zip from indexes
                String zipCodeStr = fields.get(zipCodeIndex).trim();
                // Validate Zip
                if (isValidZipCode(zipCodeStr)) {
                    // get population, try catch is handled in getValueAtIndexOrDefault
                   int population = getValueAtIndexOrDefault(fields, populationIndex, 0);

                    // Add the data to MemoryStorage
                    PopulationObject populationObject = new PopulationObject();
                    populationObject.setPopulationObject(zipCodeStr, population);
                    memory.addPopulationObjectToStorage(populationObject);
                }
            }
        }
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public boolean isValidZipCode(String zipCode) {
        // Check if zipCode has exactly 5 digits
        return zipCode.matches("^\\d{5}$");
    }
}
