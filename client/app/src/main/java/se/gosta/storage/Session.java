package se.gosta.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Session {
    private static Session instance;

    public List<Company> companies;
    public static String currentCompanyName;
    private Session() {}

    private static Map<String, Company> map = new HashMap<>();
    public static Session getSession() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }
    public static void put(String name, Company company) {
        map.put(name, company);
    }

    public static Company get(String name) {
        return map.get(name);
    }

    public static void setCurrentCompanyName(String companyName) {
        currentCompanyName = companyName;
    }

    // I t ex main activity:
    // Session.getSession().put(companyName, company);
    // intent... bla bla gå till X

    // från aktivitet X:
    // Company company = Session.getSession().get(companyName);
}

