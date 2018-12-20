package se.gosta.storage;

/**
 * Represents a Company at the fair.
 */

public class Company implements Comparable<Company> {

    // String variables for Company-objects
    private String name;
    private String contact;
    private String email;
    private String info;
    private String fileName;
    private String website;

    //Boolean variables for Company-objects
    private boolean recruiting;
    private boolean partTime;
    private boolean thesis;

    //Int variable for Company-objetcts
    private int caseNo;

    /**
     * Constructor for Company
     * @param name Company name
     * @param contact Name of contact person
     * @param email Email address to contact person
     * @param info Information text about Company
     * @param recruiting If the company is recruiting
     * @param partTime If the company recruits part time workers
     * @param thesis If the company offers thesis subjects
     * @param fileName Filename of logo on server
     * @param caseNo Specifies at which case the company is located
     * @param website URL to website
     */
    public Company(String name, String contact, String email, String info,
                   int recruiting, int partTime, int thesis, String fileName, int caseNo, String website) {

        this.name = name;

        if(contact == null) {
            this.contact = "Kontakt saknas";
        }  else {
            this.contact = contact;
        }

        if(email == null) {
            this.email = "Mailadress saknas";
        } else {
            this.email = email;
        }

        if (info == null) {
            this.info = "Mer info kommer inom kort.";
        } else {
            this.info = info;
        }

        this.recruiting = (recruiting != 0);

        this.partTime = (partTime != 0);

        this.thesis = (thesis != 0);

        if (fileName == null) {
            this.fileName = "deafultlogo.png";
        } else {
            this.fileName = fileName;
        }

        if (caseNo != 0) {
            this.caseNo = caseNo;
        }

        if (website == null) {
            this.website = "URL saknas";
        } else {
            this.website = website;
        }
    }


    /**
     * Method that returns a company's name
     * @return Name of company
     */
    public String name(){
        return name;
    }

    /**
     * Method that returns the name of a company's contact person
     * @return Name of contactperson
     */
    public String contact(){
        return contact;
    }

    /**
     * Method that returns the email address of a company's contact person
     * @return Email address of contact person
     */
    public String email(){
        return email;
    }

    /**
     * Method that returns the information text of a company
     * @return Information text of company
     */
    public String info(){
        return info;
    }

    /**
     * Method that returns if the company is recruiting
     * @return True/false if the company is recruiting
     */
    public boolean isRecruiting(){
        return recruiting;
    }

    /**
     * Method that returns if the company has available part time jobs
     * @return True/false if the company has available part time jobs
     */
    public boolean hasPartTime(){
        return partTime;
    }

    /**
     * Method that returns if the company offers thesis subjects for students
     * @return True/false if the company offers thesis subjects
     */
    public boolean hasThesis(){
        return thesis;
    }

    /**
     * Method that returns the filename of a company's logo on the server
     * @return Filename of company logo
     */
    public String fileName(){
        return fileName;
    }

    /**
     * Method that returns a company's case number
     * @return A company's case number
     */
    public int caseNo(){
        return caseNo;
    }

    /**
     * Method that returns the URL to a company's website
     * @return URL to company website
     */
    public String wesbite() {
        return website;
    }

    /**
     * Returns a String representation of the Company
     * @return a String representation of the Company
     */
    public String toString(){
        return name;
    }

    /**
     * Check if this Company equals other
     * @return true if this Company equals other
     * @param o The Object to check if this Company is equal to
     */
    @Override
    public boolean equals(Object o){
        if(!(o instanceof Company))
            return false;
        Company other = (Company)o;
        return other.name().equals(this.name);
    }

    /**
     * Check if this Company is bigger/smaller than other
     * @return int value of which is bigger/smaller
     * @param c The Company to compare this Company to
     */
    @Override
    public int compareTo(Company c) {
        return this.name.compareTo(c.name());
    }

}

