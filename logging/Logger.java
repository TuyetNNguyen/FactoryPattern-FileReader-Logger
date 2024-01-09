package filereader.logging;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class Logger{
    private static volatile Logger instance;
    private PrintWriter writer;
    private String logFilePath;

    private Logger() {
    }

    public static Logger getInstance() {
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null) {
                    instance = new Logger();
                }
            }
        }
        return instance;
    }

    public void logEvent(String message) {
        String timestamp = getTimestamp();
        String formattedMessage = timestamp + " " + message;

        if (writer != null) {
            writer.println(formattedMessage);
            writer.flush();
        } else {
            // log to System.err console
            System.err.println(formattedMessage);
        }
    }



    /* WORKING VERSION
 public void logEvent(String message) {
     String timestamp = getTimestamp();
     String formattedMessage = timestamp + " " + message;

     // Check if logFilePath is not set, and attempt to set it
     if (logFilePath == null || logFilePath.isEmpty()) {
         setLogFilePath("src/default_log.txt"); // Set a default log file path or adjust accordingly
     }

     if (writer != null) {
         writer.println(formattedMessage);
         writer.flush();
     } else {
         System.err.println(formattedMessage);
     }
 }

     */


    private String getTimestamp() {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date timestamp = new Date(currentTimeMillis);
        return sdf.format(timestamp);
    }


    public void setOutputDestination(String fileName) {
        closeLogger();
        if (fileName != null && !fileName.isEmpty()) {
            try {
                if (fileName.equals("System.err")) {
                    writer = null;
                } else {
                    writer = new PrintWriter(new FileOutputStream(fileName, true), true);
                    logFilePath = fileName;
                }
            } catch (FileNotFoundException e) {
                System.err.println("Error opening log file: " + e.getMessage());
            }
        } else {
            // use System.err instead
            writer = null;
        }
    }


    /* WORKING VERSION
    public void setLogFilePath(String loggingFileName) {
        File logFile = new File(loggingFileName);
        File logDirectory = logFile.getParentFile();

        // Check if the directory structure exists, create if not
        if (!logDirectory.exists()) {
            if (!logDirectory.mkdirs()) {
                logEvent("Error creating log directory: " + logDirectory.getAbsolutePath());
                return;
            }
        }

        try {
            writer = new PrintWriter(new FileWriter(loggingFileName, true));
            logFilePath = loggingFileName;
            logEvent("Log file path set to: " + logFilePath);
        } catch (IOException e) {
            System.err.println("Error opening log file in logEvent method: " + e.getMessage());
        }

        // DEBUG
        //if (logFilePath == null || logFilePath.isEmpty()) {
            //logEvent("Error: Log file path is still empty or null.");}

    }

     */

    public void closeLogger() {
        if (writer != null && !System.err.equals(writer)) {
            writer.close();
        }
    }

}
