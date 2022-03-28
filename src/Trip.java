package src;

import java.util.ArrayList;

public class Trip
{
    private int tripID;
    private ArrayList<Stop> stops;

    Trip(int tripID)
    {
        this.tripID = tripID;
        stops = new ArrayList<>();
    }

    public void addStop(Stop aStop)
    {
        stops.add(aStop);
    }

    public int getTripID()
    {
        return tripID;
    }

    public Stop getStopByID(int id)
    {
        for (int i = 0; i < stops.size(); i++)
        {
            Stop current = stops.get(i);

            if (current.getStopID() == id)
            {
                return current;
            }
        }

        return null;
    }
}
