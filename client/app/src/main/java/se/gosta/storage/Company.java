package se.gosta.storage;

import java.util.List;

public class Company {


    private String name;
    private String email;
    private String info;
    private String fileName;
    private int caseNo;

    public static List<Company> companies;

    public Company(String name, String email, String info, String fileName, int caseNo){
        this.name = name;
        this.email = email;
        this.info = info;
        this.fileName = fileName;
        this.caseNo = caseNo;
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

}

