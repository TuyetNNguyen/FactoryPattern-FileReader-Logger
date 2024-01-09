package edu.upenn.cit594.util;


/**
 * Population schema class
 */
public class PopulationObject {
    private String zipCode;
    private int population;

    /**
     * populationObject constructor
     */
    public PopulationObject() {}



    // Create ONE setter method for updating the entire populationObject since those fields are required and not planning for update the program
    // hence, avoid creating separated setter methods
    public void setPopulationObject(String zipCode, int population) {
        this.zipCode = zipCode;
        this.population = population;
    }


    // Getter methods
    public int getPopulation() {
        return population;
    }
    public String getPopulationZipCode() {
        return zipCode;
    }


}
