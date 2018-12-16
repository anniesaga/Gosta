package se.gosta.storage;

import java.util.List;

public class Sponsor {

    private String sponsorName;
    private String sponsorWebsite;
    private String sponsorInfo;
    private String sponsorFileName;

    public static List<Sponsor> sponsors;

    public Sponsor(String sponsorName, String sponsorWebsite, String sponsorInfo, String sponsorFileName){

        this.sponsorName = sponsorName;
        this.sponsorWebsite = sponsorWebsite;
        this.sponsorInfo = sponsorInfo;
        this. sponsorFileName = sponsorFileName;
    }


    public String sponsorName(){
        return sponsorName;
    }

    public String sponsorWebsite(){
        return sponsorWebsite;
    }

    public String sponsorInfo(){
        return sponsorInfo;
    }

    public String sponsorFileName(){
        return sponsorFileName;
    }

    public String toString(){
        return sponsorName;
    }


}
