package src;

public class Distance
{
    private String arrivalTime, departureTime;
    private int tripID, distance;

    Distance(String arrivalTime, String departureTime, int tripID, int distance)
    {
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.tripID = tripID;
        this.distance = distance;
    }

    public String getArrivalTime()
    {
        return arrivalTime;
    }

    public String getDepartureTime()
    {
        return departureTime;
    }

    public int getTripID()
    {
        return tripID;
    }

    public int getDistance()
    {
        return distance;
    }
}
