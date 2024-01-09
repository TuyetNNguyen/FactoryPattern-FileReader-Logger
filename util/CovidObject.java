
package edu.upenn.cit594.util;


/**
 * Covid data schema class
 */
public class CovidObject {
    private String zipCode;
    private String date;
    private int deathRate;
    private int partiallyVaccinated;
    private int fullyVaccinated;


    /**
     * CovidObject constructor
     */
    public CovidObject () {}


    // Create ONE setter method for updating the entire CovidObject since those fields are required and not planning for update the program
    // hence, avoid creating separated setter methods
    public void setCovidObject(String zipCode, String date, int partiallyVaccinated, int fullyVaccinated, int deathRate) {
        this.zipCode = zipCode;
        this.date = date;
        this.partiallyVaccinated = partiallyVaccinated;
        this.fullyVaccinated = fullyVaccinated;
        this.deathRate = deathRate;
    }

    // Getter methods
    public String getCovidZipCode() {
        return zipCode;
    }

    public String getCovidDate() {
        return date;
    }

    public int getCovidPartiallyVaccinated() {
        return partiallyVaccinated;
    }

    public int getCovidFullyVaccinated() {
        return fullyVaccinated;
    }

    public int getCovidDeathRate() {
        return deathRate;
    }

}
