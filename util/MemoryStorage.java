package filereader.util;
import java.util.*;

/**
 * MemoryStorage is a centralized repository where stores all valid data
 * It acts similar to a database, where it has 3 objects (tables) including covidObjects, propertiesObjects, and populationObjects and Zip_code is a Primary/Foreign key
 * It implements Singleton pattern to ensure only one database is instantiated
 */
public class MemoryStorage {
    private static MemoryStorage instance;
    private Map<String, Map<String, CovidObject>> storageCovidData;
    private Map<String, Integer> storagePopulationData;
    private Map<String, List<PropertiesObject>> storagePropertyData;


    /**
     * MemoryStorage constructor set to private
     */
    private MemoryStorage() {
        this.storageCovidData = new HashMap<>();
        this.storagePropertyData = new HashMap<>();
        this.storagePopulationData = new HashMap<>();
    }


    /**
     * Only getInstance is public
     * @return MemoryStorage instance
     */

    public static MemoryStorage getInstance() {
        if (instance == null) {
            synchronized (MemoryStorage.class) {
                if (instance == null) {
                    instance = new MemoryStorage();
                }
            }
        }
        return instance;
    }



    /**
     * Add methods for storageCovidData
     * @param covidObject
     */
    public void addCovidObjectToStorage(CovidObject covidObject) {

        String zip = covidObject.getCovidZipCode();
        Map<String, CovidObject> covidObjectsAndDates = storageCovidData.get(zip);

        // Check the (inner) Date map existence
        if (covidObjectsAndDates == null) {
            covidObjectsAndDates = new HashMap<>();
            // create a new one for the first time, add to the storageCovidData
            storageCovidData.put(zip, covidObjectsAndDates);
        }

        String date = covidObject.getCovidDate();
        // add covidObject to the inner date map directly
        covidObjectsAndDates.put(date, covidObject);


        // Debug added and updated values
        //System.out.println("Added CovidObject: " + covidObject + " for ZIP: " + zip + " and Date: " + date);
        //System.out.println("Updated ZipCodes: " + storageCovidData.keySet() + " \n" + storageCovidData.values() );

    }



    /**
     * Add methods for storagePopulationData
     * @param populationObject
     */
    public void addPopulationObjectToStorage(PopulationObject populationObject) {
        String zip = populationObject.getPopulationZipCode();
        int population = populationObject.getPopulation();

        // Check if the population data for the zip code already exists
        if (!storagePopulationData.containsKey(zip)) {
            storagePopulationData.put(zip, population);
        }

        // Debug
        //System.out.println("Updated ZIP code keyset: " + storagePopulationData.keySet() + " \n and values: " + storagePopulationData.values());
    }



    /**
     * Add methods for storagePropertyData
     * @param propertiesObject
     */
    public void addPropertyObjectToStorage(PropertiesObject propertiesObject) {

        String zip = propertiesObject.getPropertiesZipCode();
        List<PropertiesObject> propertyDetailList = storagePropertyData.get(zip);
        if(propertyDetailList == null || propertyDetailList.isEmpty()) {
            propertyDetailList = new ArrayList<>();
            storagePropertyData.put(zip, propertyDetailList);
        }
        propertyDetailList.add(propertiesObject);

        // Debug
        //System.out.println("Updated ZIP codes " + storagePropertyData.keySet() + " \nand values: " + storagePropertyData.values());
    }




    // Getter methods for CovidObjects, return an empty Map if no Covid data was successfully loaded into MemoryStorage
    public Map<String, Map<String, CovidObject>> getAllCovidObjects() {
        return storageCovidData != null ? storageCovidData : Collections.emptyMap();
    }

    public Map<String, CovidObject> getDateAndCovidObjectsByZip(String zip) {
        return storageCovidData != null ? storageCovidData.get(zip) : Collections.emptyMap();
    }

    public CovidObject getCovidObjectsDetail(String zip, String date) {
        Map<String, CovidObject> dateCovidObjectsMap = getDateAndCovidObjectsByZip(zip);

        // Check if the ZIP code map is null
        if (dateCovidObjectsMap == null) {
            return null;
        }

        // get the CovidObject based on the date
        return dateCovidObjectsMap.get(date);
    }

    public Set<String> getAllZipsFromCovidData () {
        return storageCovidData != null ? storageCovidData.keySet() : Collections.emptySet();
    }



    // Getter methods for PopulationObjects, return an empty Map if no Population data was successfully loaded into MemoryStorage
    public Map<String, Integer> getAllPopulationObjects() {
        return storagePopulationData != null ? storagePopulationData : Collections.emptyMap();
    }

    public Integer getPopulationByZip (String zipCode) {
        return storagePopulationData != null ? getAllPopulationObjects().get(zipCode) : null;
    }

    public Set<String> getAllZipsFromPopulationData () {
        return storagePopulationData != null ? storagePopulationData.keySet() : Collections.emptySet();
    }





    // Getter methods for PropertyObjects, return an empty Map if no Properties data was successfully loaded into MemoryStorage
    public Map<String, List<PropertiesObject>> getAllPropertyObjects() {
        return storagePropertyData != null ? storagePropertyData : Collections.emptyMap();
    }

    public Set<String> getAllZipsFromPropertyData () {
        return storagePropertyData != null ? storagePropertyData.keySet() : Collections.emptySet();
    }

    public List<PropertiesObject> getPropertyObjectsByZip(String zipCode) {
        return storagePropertyData != null ? storagePropertyData.get(zipCode) : Collections.emptyList();
    }

    public int getTotalMarketValueByZip(String zipCode) {
        List<PropertiesObject> propertyObjectsList = getPropertyObjectsByZip(zipCode);
        int totalMarketValue = 0;

        // Ensure propertyObjectsList is not an empty List
        if(!propertyObjectsList.isEmpty()) {
            for (PropertiesObject propertyObject : propertyObjectsList ) {

                // If propertyObject null return 0
                int marketValue = propertyObject != null ? propertyObject.getPropertiesMarketValue() : 0;
                totalMarketValue += marketValue;
            }
        }
        return totalMarketValue;
    }

    public int getTotalLivableAreaByZip(String zipCode) {
        List<PropertiesObject> propertyObjectsList = getPropertyObjectsByZip(zipCode);
        int totalLivableArea = 0;

        // Ensure propertyObjectsList is not an empty List
        if(propertyObjectsList != null && !propertyObjectsList.isEmpty()) {
            for (int i = 0; i < propertyObjectsList.size(); i++) {

                // Ensure propertyObject is not Null
                if (propertyObjectsList.get(i) != null) {
                    totalLivableArea += propertyObjectsList.get(i).getPropertiesLivableArea();
                }
            }
        }
        return totalLivableArea;
    }

    public void closeMemory() {
        instance = null;
    }
}
