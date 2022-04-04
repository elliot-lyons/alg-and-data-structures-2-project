package src;

import java.util.ArrayList;
import java.util.Scanner;

public class RoutePlan
{
    private boolean back, valid;
    private String input, sourceName, destName;
    private int source, dest;
    private Scanner s;
    private double[][] distances;
    private String[][] tripIDs, sources, dests;
    private ArrayList<Stop> stops;
    private ArrayList<Trip> trips;

    RoutePlan(Scanner s, double[][] distances, String[][] tripIDs, ArrayList<Stop> stops, ArrayList<Trip> trips,
            String[][] sources, String[][] dests)
    {
        this.s = s;
        this.distances = distances;
        this.tripIDs = tripIDs;
        this.stops = stops;
        this.trips = trips;
        this.sources = sources;
        this.dests = dests;
    }

    public void display()
    {
        back = false;
        valid = true;

        do
        {
            do {
                if (!valid)
                {
                    System.out.print("Invalid input. ");
                }

                System.out.print("Please enter a source stop ID or 'back' to return to main menu: \n");

                input = s.nextLine();

                if (input.equals(null))
                {
                    valid = false;
                }

                else
                {
                    switch (input)
                    {
                        case ("back"):
                        {
                            return;
                        }

                        default: {
                            valid = false;

                            for (int i = 0; i < stops.size() && !valid; i++) {
                                if (input.equals(stops.get(i).getStopID())) // confirming stop is in list
                                {
                                    valid = true;
                                    source = i;
                                    sourceName = input;
                                }
                            }
                        }
                    }
                }

            } while (!valid);

            valid = true;

            if (!back) {
                do {
                    if (!valid) {
                        System.out.print("Invalid input. ");
                    }

                    System.out.print("Please enter a destination stop ID or 'back' to return to main menu: \n");

                    input = s.nextLine();

                    switch (input) {
                        case ("back"): {
                            return;
                        }

                        default: {
                            valid = false;

                            for (int i = 0; i < stops.size() && !valid; i++) {
                                if (input.equals(stops.get(i).getStopID())) {
                                    valid = true;
                                    dest = i;
                                }
                            }
                        }
                    }

                } while (!valid);

                System.out.println(tripInstructions());

            }
        } while (!back);
    }

    public String tripInstructions()
    {
        if (distances[source][dest] == Double.POSITIVE_INFINITY)
        {
            return "There is no route from this source to this destination";
        }

        if (source == dest)
        {
            return "These stops are the same, the associated cost is 0.";
        }

        String res = "Take ";

        if (tripIDs[source][dest].contains(","))        // implies that you need to take more than one route
        {
            String[] t = tripIDs[source][dest].split(",", -1);
            String[] s = sources[source][dest].split(",", -1);
            String[] d = dests[source][dest].split(",", -1);

            for (int i = 0; i < t.length; i++)
            {
                if (i != 0)
                {
                    res += "\nThen take ";
                }

                if (!t[i].equals("a transfer"))
                {
                    res += "trip ID ";
                }

                res += t[i] + " from " + s[i] + " to " + d[i] + ".";        // displays where we take the trip to and from

                if (!t[i].equals("a transfer")) {       // if it's not a transfer, we list off the stops you pass on
                    res = res + "\n" + enRoute(t[i], s[i], d[i]);   // a trip, using enRoute method
                }
            }
        }

        else
        {
            res +=  tripIDs[source][dest] + " from " + sources[source][dest] +
                    " to " + dests[source][dest] + ".";

            if (!tripIDs[source][dest].equals("a transfer"))
            {
                res = res + "\n" + enRoute(tripIDs[source][dest], sources[source][dest], dests[source][dest]);
            }
        }

        if (res.equals("Take "))
        {
            return "Error";
        }

        res += "\nThe associated cost with this trip is: " + distances[source][dest] + ".";

        return res;
    }

    public Trip getTripByID(String tripID)
    {
        for (int i = 0; i < trips.size(); i++)
        {
            Trip current = trips.get(i);

            if (current.getTripID().equals(tripID))
            {
                return current;
            }
        }

        return null;
    }


    /**
     *
     * @param tripID: the current trip ID
     * @param source: where you get on this trip ID
     * @param dest: where you get off
     *
     * @return a string that details all the stops you pass (including the ones where you embark and disembark)
     */

    public String enRoute(String tripID, String source, String dest)
    {
        String e = "En route you will pass stops: ";
        Trip current = getTripByID(tripID);
        boolean finish = false;
        boolean first = false;
        String[] theStops = current.getStopIDs();
        ArrayList<String> enRoute = new ArrayList<String>();

        for (int j = 0; j < theStops.length && !finish; j++) {
            if (!first && theStops[j].equals(source)) {     // don't display stops that bus has already passed before
                first = true;                               // you get on
            }

            if (first) {
                enRoute.add(theStops[j]);

                if (theStops[j].equals(dest)) {             // don't display stops that bus will pass after you get off
                    finish = true;
                }
            }
        }

        // Tidying of output

        for (int k = 0; k < enRoute.size() - 1; k++)
        {
            e = e + enRoute.get(k) + ", ";
        }

        e = e + enRoute.get(enRoute.size() - 1) + ".";

        return e;
    }
}