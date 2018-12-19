package se.gosta.storage;

/**
 * Represents an Event at the fair.
 */

public class Event implements Comparable<Event> {

    // String variables for Event-objects
    private String startTime;
    private String eventName;
    private String eventInfo;

    /**
     * Constructor for Event
     * @param startTime Start time of the event
     * @param eventName Name of the event
     * @param eventInfo Detailed description of the event
     * @throws NullPointerException
     */
    public Event(String startTime, String eventName, String eventInfo){
        if ( eventName == null ){
            throw new NullPointerException("name cannot be null");
        }
        this.startTime = startTime;
        this.eventName = eventName;
        this.eventInfo = eventInfo;
    }

    /**
     * Returns the start time of an event
     * @return the start time of an event
     */
    public String startTime(){
        return startTime;
    }
    /**
     * Returns the name of an event
     * @return the name of an event
     */
    public String eventName(){
        return eventName;
    }
    /**
     * Returns the detailed description of an event
     * @return the detailed description of an event
     */
    public String eventInfo(){
        return eventInfo;
    }

    /**
     * Returns a String representation of the Event
     * @return a String representation of the Event
     */
    @Override
    public String toString(){
        return startTime + " " + eventName;
    }

    /**
     * Check if this Event is bigger/smaller than other
     * @return int value of which is bigger/smaller
     * @param event The Event to compare this Event to
     */
    public int compareTo(Event event){
        return this.startTime.compareTo(event.startTime);
    }

}
