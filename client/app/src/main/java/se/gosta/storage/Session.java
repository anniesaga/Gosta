package se.gosta.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the current Session
 */

public class Session {

    private static Session instance;

    public List<Company> companies;

    public static String currentCompanyName;

    /**
     * Private constructor to prevent instantiation.
     */
    private Session() {}

    private static Map<String, Company> map = new HashMap<>();

    /**
     * @return the one and only instance of Session.
     */
    public static Session getSession() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    /**
     * Puts current company into map
     * @param name Name of company
     * @param company Current Company
     */
    public static void put(String name, Company company) {
        map.put(name, company);
    }


    /**
     * Get current Company based on name
     * @return current Company
     */
    public static Company get(String name) {
        return map.get(name);
    }

    /**
     * Sets name of current Company for current Session.
     * @param companyName Name of the company.
     */
    public static void setCurrentCompanyName(String companyName) {
        currentCompanyName = companyName;
    }

    // I t ex main activity:
    // Session.getSession().put(companyName, company);
    // intent... bla bla gå till X

    // från aktivitet X:
    // Company company = Session.getSession().get(companyName);
}

