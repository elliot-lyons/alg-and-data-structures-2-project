package src;

public class Stop
{
    private int stopID;
    private String stopName, arrivalTime, departureTime;

    Stop(int stopID, String arrivalTime, String departureTime)
    {
        this.stopID = stopID;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
    }

    Stop (int stopID, String stopName)
    {
        this.stopID = stopID;
        this.stopName = stopName;
    }

    public int getStopID()
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

    public void setStopID(int stopID) {
        this.stopID = stopID;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }
}