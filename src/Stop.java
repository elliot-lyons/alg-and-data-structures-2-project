package src;

public class Stop
{
    private String stopID;
    private String stopName, arrivalTime, departureTime;

    Stop (String stopID, String stopName)
    {
        this.stopID = stopID;
        this.stopName = stopName;
    }

    public String getStopID()
    {
        return stopID;
    }

    public String getStopName()
    {
        return stopName;
    }

    public String getArrivalTime()
    {
        return arrivalTime;
    }

    public String getDepartureTime()
    {
        return departureTime;
    }

    public void setStopName(String stopName)
    {
        this.stopName = stopName;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setStopID(String stopID) {
        this.stopID = stopID;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }
}