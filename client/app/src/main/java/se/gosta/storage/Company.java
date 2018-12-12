package se.gosta.storage;

import java.util.List;

public class Company implements Comparable<Company> {


    private String name;
    private String email;
    private String info;
    private String fileName;
    private int caseNo;

    public static List<Company> companies;

    public Company(String name, String email, String info, String fileName, int caseNo) {

        this.name = name;

        if(email != null && email.contains("@")) {
            this.email = email;
        }
        if (info != null) {
            this.info = info;
        }
        if (fileName != null) {
            this.fileName = fileName;
        }
        if (caseNo != 0) {
            this.caseNo = caseNo;
        }
    }


    public String name(){
        return name;
    }

    public String email(){
        return email;
    }

    public String info(){
        return info;
    }

    public String fileName(){
        return fileName;
    }

    public int caseNo(){
        return caseNo;
    }

    public String toString(){
        return name;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Company))
            return false;
        Company other = (Company)o;
        return other.name().equals(this.name);
    }

    @Override
    public int compareTo(Company c) {
        return this.name.compareTo(c.name());
    }

}

