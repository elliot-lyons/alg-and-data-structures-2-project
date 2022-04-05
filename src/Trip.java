/**
 * Stores a trip that contains where the bus stops at on said trip and at what time it does so for each stop.
 */

package src;

public class Trip
{
    private String tripID;
    private String[] stops, arrivalTimes;

    Trip(String tripID)
    {
        this.tripID = tripID;
    }

    public String getTripID()
    {
        return tripID;
    }

    public void setStops(String[] stops)
    {
       this.stops = stops;
    }

    public String[] getStopIDs() {
        return stops;
    }

    public void setArrivalTimes(String[] arrivalTimes)
    {
        this.arrivalTimes = arrivalTimes;
    }

    public String[] getArrivalTimes() {
        return arrivalTimes;
    }
}