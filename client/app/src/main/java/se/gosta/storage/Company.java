package se.gosta.storage;

import java.util.List;

public class Company implements Comparable<Company> {

    private String name;
    private String contact;
    private String email;
    private String info;
    private int empSwe;
    private int empGlobal;
    private boolean recruiting;
    private boolean partTime;
    private boolean thesis;
    private String fileName;
    private int caseNo;

    public static List<Company> companies;

    public Company(String name, String contact, String email, String info, int empSwe, int empGlobal,
                   int recruiting, int partTime, int thesis, String fileName, int caseNo) {

        this.name = name;

        if(contact != null) {
            this.contact = contact;
        }

        if(email != null && email.contains("@")) {
            this.email = email;
        }

        if (info == null) {
            this.info = "Mer info kommer inom kort.";
        } else {
            this.info = info;
        }

        this.empSwe = empSwe;

        this.empGlobal = empGlobal;

        this.recruiting = (recruiting != 0);

        this.partTime = (partTime != 0);

        this.thesis = (thesis != 0);

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

    public String contact(){
        return contact;
    }

    public String email(){
        return email;
    }

    public String info(){
        return info;
    }

    public int empSwe(){
        return empSwe;
    }

    public int empGlobal(){
        return empGlobal;
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

