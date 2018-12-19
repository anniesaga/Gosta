package se.gosta.storage;

/**
 * Represents a Sponsor for the Fair
 * NOTE: This classed is not used in this sprint but will be implemented
 * for when sponsors will be fetched from the server
 */


public class Sponsor {

    // String variables of a Sponsor
    private String sponsorName;
    private String sponsorWebsite;
    private String sponsorInfo;
    private String sponsorFileName;


    /**
     * Constructor for Sponsor
     * @param sponsorName Sponsor name
     * @param sponsorWebsite URL of website to sponsor
     * @param sponsorInfo Description about the sponsor
     * @param sponsorFileName Filename of the logo on the server
     */

    public Sponsor(String sponsorName, String sponsorWebsite, String sponsorInfo, String sponsorFileName){

        // Will have to check for null here before usage.
        this.sponsorName = sponsorName;
        this.sponsorWebsite = sponsorWebsite;
        this.sponsorInfo = sponsorInfo;
        this.sponsorFileName = sponsorFileName;
    }

    /**
     * Returns the name of a sponsor
     * @return the name of a sponsor
     */
    public String sponsorName(){
        return sponsorName;
    }

    /**
     * Returns the website URL of a sponsor
     * @return the website URL of a sponsor
     */
    public String sponsorWebsite(){
        return sponsorWebsite;
    }

    /**
     * Returns the description of a sponsor
     * @return the description of a sponsor
     */
    public String sponsorInfo(){
        return sponsorInfo;
    }

    /**
     * Returns the filename of logo on server
     * @return the filename of logo on server
     */
    public String sponsorFileName(){
        return sponsorFileName;
    }

    public String toString(){
        return sponsorName;
    }


}
