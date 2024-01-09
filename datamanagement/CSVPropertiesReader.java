package edu.upenn.cit594.datamanagement;
import edu.upenn.cit594.util.MemoryStorage;
import edu.upenn.cit594.util.PropertiesObject;
import java.io.IOException;
import java.util.List;


/**
 * Concrete class parsing Csv property file
 */
public class CSVPropertiesReader extends CSVFileReader {

    public CSVPropertiesReader(String propertyFile) {
        super(propertyFile);
    }




    /**
     * Parses the CsvCode file and stores specific valid fields in MemoryStorage
     * @param memory
     * @throws IOException
     */
    /*
    public void parserAndStoreInMemory(MemoryStorage memory) throws IOException {
        // Get a list of lines
        List<String> lines = identifyLines(fileName);

        // Hw instruction mentions if a csv file is valid, it always has a header
        // Read the first line to get column names and its index
        List<String> headerColumns = processFields(lines.get(0));

        int zipCodeIndex = findColumnIndex(headerColumns, "zip_code");
        int marketValueIndex = findColumnIndex(headerColumns, "market_value");
        int livableAreaIndex = findColumnIndex(headerColumns, "total_livable_area");

        if (zipCodeIndex == -1 || marketValueIndex == -1 || livableAreaIndex == -1) {
            System.err.println("Required columns not found in the header in Property csv file.");
        } else {


            // Iterate through the list of lines
            for (int i = 1; i < lines.size(); i++) {

                // Process a line and returns a list of fields
                List<String> values = processFields(lines.get(i));

                String zipCodeString = values.get(zipCodeIndex).trim();
                // load only the 1st 5 digits into storage
                zipCodeString = zipCodeString.substring(0, Math.min(5, values.get(zipCodeIndex).trim().length()));
                // Check if zipCode is a valid integer or can be parsed as an integer
                if (isValidInteger(zipCodeString)) {
                    int zipCode = Integer.parseInt(zipCodeString);

                    // Validate Zip
                    if (isValidZipCode(zipCode)) {
                        // Extract values based on its index, set to 0 as default value as required
                        int marketValue = getValueAtIndexOrDefault(values, marketValueIndex, 0);
                        int livableArea = getValueAtIndexOrDefault(values, livableAreaIndex, 0);

                        // Add the data to MemoryStorage
                        PropertiesObject propertiesObject = new PropertiesObject();
                        propertiesObject.setPropertyObject(zipCode, marketValue, livableArea);
                        memory.addPropertyObjectToStorage(propertiesObject);
                    }
                }
            }
        }
    }

     */

    public void parserAndStoreInMemory(MemoryStorage memory) throws IOException {
        // Get the whole list of lines
        List<String> lines = identifyLines(fileName);

        // Hw instruction mentions if a csv file is valid, the first row is always has a header
        List<String> headerColumns = processFields(lines.get(0));

        int zipCodeIndex = findColumnIndex(headerColumns, "zip_code");
        int marketValueIndex = findColumnIndex(headerColumns, "market_value");
        int livableAreaIndex = findColumnIndex(headerColumns, "total_livable_area");

        // Ensure required fields are provided
        if (zipCodeIndex == -1 || marketValueIndex == -1 || livableAreaIndex == -1) {
            System.err.println("Required columns not found in the header in Property csv file.");
        } else {

            // Iterate through the list of lines
            for (int i = 1; i < lines.size(); i++) {
                // Process a line and returns a list of fields
                List<String> fields = processFields(lines.get(i));

                // Extract zip based on its index
                String zipCodeStr = fields.get(zipCodeIndex).trim();
                zipCodeStr = zipCodeStr.substring(0, Math.min(5, fields.get(zipCodeIndex).trim().length())); // extract only the 1st 5 digits to load in storage

                // Validate Zip
                if (isValidZipCode(zipCodeStr)) {
                    // Extract values based on its index, set to 0 as default value as required. Try catch is handled in getValueAtIndexOrDefault
                    int marketValue = getValueAtIndexOrDefault(fields, marketValueIndex, 0);
                    int livableArea = getValueAtIndexOrDefault(fields, livableAreaIndex, 0);

                    // Add the data to MemoryStorage
                    PropertiesObject propertiesObject = new PropertiesObject();
                    propertiesObject.setPropertyObject(zipCodeStr, marketValue, livableArea);
                    memory.addPropertyObjectToStorage(propertiesObject);
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
        // Check if zipCode starts with 5 digits, do not check anything after
        return zipCode.matches("^\\d{5}.*");
    }

}
