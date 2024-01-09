package edu.upenn.cit594.datamanagement;
import edu.upenn.cit594.util.CovidObject;
import edu.upenn.cit594.util.MemoryStorage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;


/**
 * Concrete class parsing Csv covid file
 */
public class CSVCovidReader extends CSVFileReader{

    public CSVCovidReader(String covidFile) {
        super(covidFile);
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
        int timestampIndex = findColumnIndex(headerColumns, "etl_timestamp");
        int partiallyVaccinatedIndex = findColumnIndex(headerColumns, "partially_vaccinated");
        int fullyVaccinatedIndex = findColumnIndex(headerColumns, "fully_vaccinated");
        int deathsIndex = findColumnIndex(headerColumns, "deaths");

        // Only execute when required fields are provided
        if (zipCodeIndex == -1 || timestampIndex == -1 || partiallyVaccinatedIndex == -1
                || fullyVaccinatedIndex == -1) {
            System.err.println("Required columns not found in the header in Covid csv file.");
        } else {

            // Iterate through the list of lines, skip the 1st row (header)
            for (int i = 1; i < lines.size(); i++) {

                // Process a line and returns a list of fields
                List<String> fields = processFields(lines.get(i));

                // Extract fields by indexes
                String zipCodeStr = fields.get(zipCodeIndex).trim();
                String timestamp = fields.get(timestampIndex).trim();

                // Validate Zip and Date format
                if (isValidZipCode(zipCodeStr) && isValidDateFormat(timestamp)) {
                    // try catch is handled in formatTimestamp and getValueAtIndexOrDefault methods
                    String date = formatTimestamp(timestamp);
                    int partiallyVaccinated = getValueAtIndexOrDefault(fields, partiallyVaccinatedIndex, 0);
                    int fullyVaccinated = getValueAtIndexOrDefault(fields, fullyVaccinatedIndex, 0);
                    int deaths = getValueAtIndexOrDefault(fields, deathsIndex, 0); // partial field is optional, it might or might not exist

                    // Add the data to MemoryStorage
                    CovidObject covidObject = new CovidObject();
                    covidObject.setCovidObject(zipCodeStr, date, fullyVaccinated, partiallyVaccinated, deaths);
                    memory.addCovidObjectToStorage(covidObject);

                    // DEBUG values ready to pass into storage
                    //System.out.println("zipCode: " + zipCode + " date " + date + " partiallyVaccinated " + partiallyVaccinated +
                    //" fullyVaccinated " + fullyVaccinated + " deaths " + deaths);
                    //System.out.println(memory.getInstance().getCovidObjectsDetail(19111, "2021-03-25"));
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


    private static boolean isValidDateFormat(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            // Try to parse the input date, return true if success
            LocalDateTime.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private String formatTimestamp(String timestamp) {
        DateTimeFormatter timeStampInput = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // MM for month
        DateTimeFormatter dateOutput = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDateTime dateTime = LocalDateTime.parse(timestamp, timeStampInput);
            return dateTime.format(dateOutput);
        } catch (DateTimeParseException e) {
            System.err.println("Error formatting timestamp. Error message: " + e.getMessage());
            return null;
        }
    }
}
