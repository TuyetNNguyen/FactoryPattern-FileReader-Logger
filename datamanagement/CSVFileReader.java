package edu.upenn.cit594.datamanagement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * CSVFile abstract class handles csv file only
 * It identify lines and process fields
 */
public abstract class CSVFileReader implements FileParser{
    protected String fileName;

    /**
     * CSVFileReader constructor
     * @param fileName
     */
    public CSVFileReader(String fileName) {
        this.fileName = fileName;
    }


    /**
     * This method is step 1 of parsing a CSV file process
     * it takes in the valid input file, identify lines and returns a list of lines
     * @param fileName
     * @return a list of lines
     * @throws IOException
     */
    protected List<String> identifyLines(String fileName) throws IOException {
        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();
        boolean inQuotes = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            int currentChar;
            while ((currentChar = reader.read()) != -1) {
                char c = (char) currentChar;

                if (c == '"') {
                    inQuotes = !inQuotes;
                }

                currentLine.append(c);

                if (c == '\n' && !inQuotes) {
                    lines.add(currentLine.toString());
                    currentLine.setLength(0);
                }
            }

            if (currentLine.length() > 0) {
                lines.add(currentLine.toString());
            }
        }

        return lines;
    }


    /**
     * This method is step 2 in the parsing CSV file
     * It takes in a line, identify specific fields and returns a list of fields
     * @param line
     * @return a List of fields
     */
    protected List<String> processFields(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                //  checks if currently inside a quoted field, at least one more character in the line, and the next character is a double quote
                if (inQuotes && i < line.length() - 1 && line.charAt(i + 1) == '"') {

                    // Handle double quote in the middle of an escaped field
                    currentField.append('"');

                    // Skip the next double quote
                    i++;

                } else {
                    // Set inQuotes flag if any of the conditions above do not meet
                    inQuotes = !inQuotes;
                }

                // When a char is not inQuote and is a comma, this is a field ending delimiter
            } else if (c == ',' && !inQuotes) {
                // Add the field to the list and reset currentField
                fields.add(currentField.toString().trim());
                currentField.setLength(0);

            } else {
                // Append character to the currentField
                currentField.append(c);
            }
        }

        // Add the last field
        fields.add(currentField.toString().trim());

        return fields;
    }


    /**
     * Find column index based on the field name that is identified from the header
     * @param fields
     * @param fieldName
     * @return the column index
     */
    protected int findColumnIndex(List<String> fields, String fieldName) {

        for (int i = 0; i < fields.size(); i++) {
            if (fields.get(i).trim().equalsIgnoreCase(fieldName.trim())) {
                return i;
            }
        }
        // Set index to -1 when a column is not found
        return -1;
    }


    /**
     * Get value of associated fieldName (header) based on fieldName index
     * return default value if index is out of bounds
     * @param fields
     * @param index of fieldName
     * @param defaultValue
     * @return
     */
    protected int getValueAtIndexOrDefault(List<String> fields, int index, int defaultValue) {
        if (index >= 0 && index < fields.size() && fields.get(index).trim() != null && !fields.get(index).trim().isEmpty()) {
            try{
                // handle floating-point numbers
                double valueInDouble = Double.parseDouble(fields.get(index).trim());
                return (int) valueInDouble;
            } catch (NumberFormatException e) {
                System.out.println("Error parsing value at index " + index + ". Error message: " + e.getMessage());
            }
        }
        return defaultValue;
    }




}



