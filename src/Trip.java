package src;

public class Trip
{
    private String tripID;
    private String[] stops;

    Trip(String tripID)
    {
        this.tripID = tripID;
    }

//    public void addStop(Stop aStop)
//    {
//        stops.add(aStop);
//    }

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
}
