package se.gosta.storage;

import java.util.List;

public class Company implements Comparable<Company> {

    private String name;
    private String contact;
    private String email;
    private String info;
    private boolean recruiting;
    private boolean partTime;
    private boolean thesis;
    private String fileName;
    private int caseNo;
    private String website;

    public static List<Company> companies;

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


    public String name(){
        return name;
    }

    public String contact(){
        return contact;
    }

    public String email(){
        return email;
    }

    public String info(){
        return info;
    }

    public boolean isRecruiting(){
        return recruiting;
    }

    public boolean hasPartTime(){
        return partTime;
    }

    public boolean hasThesis(){
        return thesis;
    }

    public String fileName(){
        return fileName;
    }

    public int caseNo(){
        return caseNo;
    }

    public String wesbite() {
        return website;
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

