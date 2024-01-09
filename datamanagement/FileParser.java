package fileReader.datamanagement;
import fileReader.util.MemoryStorage;
import java.io.IOException;

/**
 * Apply Factory pattern to parser different files and load data into MemoryStorage
 */
public interface FileParser {

        /**
         * Parser files and load all valid data into MemoryStorage
         * @param memoryStorage object
         * @throws IOException
         */
        void parserAndStoreInMemory(MemoryStorage memoryStorage) throws IOException;

        String getFileName();

        boolean isValidZipCode(String zipCode);
}
