/**
 * Stop object that stores the id and name of a stop.
 */

package src;

public class Stop
{
    private String stopID;
    private String stopName;

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

    public void setStopName(String stopName)
    {
        this.stopName = stopName;
    }

    public void setStopID(String stopID) {
        this.stopID = stopID;
    }
}