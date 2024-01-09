package edu.upenn.cit594.util;


/**
 * Property schema class
 */
public class PropertiesObject {

    private String zipCode;
    private int marketValue;
    private int livableArea;


    /**
     * PropertiesObject constructor
     */
    public PropertiesObject() {}


    // Create ONE setter method for updating the entire propertyObject since those fields are required and not planning for update the program
    // hence, avoid creating separated setter methods
    public void setPropertyObject(String zipCode, int marketValue, int livableArea) {
        this.zipCode = zipCode;
        this.marketValue = marketValue;
        this.livableArea = livableArea;
    }



    // Getter methods

    public String getPropertiesZipCode() {
        return zipCode;
    }

    public int getPropertiesMarketValue() {
        return marketValue;
    }

    public int getPropertiesLivableArea() {
        return livableArea;
    }

}
