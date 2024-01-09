package filereader.datamanagement;
import filereader.util.CovidObject;
import filereader.util.MemoryStorage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class JsonCovidReader implements FileParser{
    private String fileName;
    private MemoryStorage memoryStorage;

    /**
     * JsonCovid constructor
     * @param covidFile
     */
    protected JsonCovidReader(String covidFile) {
        this.fileName = covidFile;
    }


    /**
     * Parses the JsonCode file and stores specific valid fields in MemoryStorage
     * @param memory
     * @throws IOException
     */
    @Override
    public void parserAndStoreInMemory(MemoryStorage memory) throws IOException {
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(fileName)) {
            JSONArray jsonArray = (JSONArray) parser.parse(reader);

            // Iterate over each JSON object in the array
            for (Object obj : jsonArray) {
                JSONObject jsonObj = (JSONObject) obj;

                // Extracting required fields to create covidObject
                Object zipCodeObj = jsonObj.get("zip_code");
                Object timestampObj = jsonObj.get("etl_timestamp");

                if (zipCodeObj != null && timestampObj != null) {
                    String zipCodeStr = zipCodeObj.toString();
                    String timestamp = timestampObj.toString();
                    Long fullyVaccinated = jsonObj.get("fully_vaccinated") != null ? (Long) jsonObj.get("fully_vaccinated") : 0;
                    Long partiallyVaccinated = jsonObj.get("partially_vaccinated") != null ? (Long) jsonObj.get("partially_vaccinated") : 0;
                    Long deaths = jsonObj.get("deaths") != null ? (Long) jsonObj.get("deaths") : 0;

                // Validate ZIP and date, skip the line if either field is not valid
                    if (isValidZipCode(zipCodeStr) && isValidDateFormat(timestamp)) {
                        // try catch is handled in formatTimestamp and getValueAtIndexOrDefault methods
                        String date = formatTimestamp(timestamp);
                        int fullyVaccinatedInt = fullyVaccinated != null ? fullyVaccinated.intValue() : 0;
                        int partiallyVaccinatedInt = partiallyVaccinated != null ? partiallyVaccinated.intValue() : 0;
                        int deathsInt = deaths != null ? deaths.intValue() : 0;

                        // Create a CovidObject and store it in CovidObjects in MemoryStorage
                        CovidObject covidObject = new CovidObject();
                        covidObject.setCovidObject(zipCodeStr, date, fullyVaccinatedInt, partiallyVaccinatedInt, deathsInt);
                        memory.addCovidObjectToStorage(covidObject);
                    }
                }
            }

        }  catch (FileNotFoundException | ParseException e) {
            System.err.println("Error JsonCovidFile input not found " + e);
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
        // Define the expected date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            // Try to parse the input date
            LocalDateTime.parse(date, formatter);
            return true; // If parsing successful, the date is valid
        } catch (DateTimeParseException e) {
            // If parsing fails, the date is not in the correct format
            return false;
        }
    }


    private String formatTimestamp(String timestamp) {
        DateTimeFormatter timeStampInput = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // MM for month HH for 24 hrs
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
