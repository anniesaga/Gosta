package se.gosta.storage;

public class Event {

    private String startTime;
    private String eventName;
    private String eventInfo;

    public Event(String startTime, String eventName, String eventInfo){
        if ( eventName == null ){
            throw new NullPointerException("name cannot be null");
        }
        this.startTime = startTime;
        this.eventName = eventName;
        this.eventInfo = eventInfo;
    }

    public String startTime(){
        return startTime;
    }

    public String eventName(){
        return eventName;
    }

    public String eventInfo(){
        return eventInfo;
    }

    @Override
    public String toString(){
        return startTime + " " + eventName;
    }
}
