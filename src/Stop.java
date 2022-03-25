package src;

public class Stop
{
    private int stopID;
    private String stopName;

    Stop(int stopID, String stopName)
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
}