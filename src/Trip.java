package src;

import java.util.ArrayList;

public class Trip
{
    private int tripID;
    private ArrayList<Stop> stops;
    private ArrayList<Double> times;

    Trip(int tripID)
    {
        this.tripID = tripID;
        stops = new ArrayList<>();
    }

    public void addTime(String one, String two)
    {

    }
}
