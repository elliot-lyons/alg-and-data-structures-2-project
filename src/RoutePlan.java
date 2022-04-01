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
                    System.out.print("Invalid output. ");
                }

                System.out.print("Please enter a source stop ID or 'back' to return to main menu: \n");

                input = s.nextLine();

                switch (input)
                {
                    case ("back"):
                    {
                        return;
                    }

                    default:
                    {
                        valid = false;

                        for (int i = 0; i < stops.size() && !valid; i++)
                        {
                            if (input.equals(stops.get(i).getStopID()))
                            {
                                valid = true;
                                source = i;
                                sourceName = input;
                            }
                        }
                    }
                }

            } while (!valid);

            valid = true;

            if (!back) {
                do {
                    if (!valid) {
                        System.out.print("Invalid output. ");
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
                                    destName = input;
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

        String res = "Take ";

        if (tripIDs[source][dest].contains(","))
        {
            String[] t = tripIDs[source][dest].split(",", -1);
            String[] s = sources[source][dest].split(",", -1);
            String[] d = dests[source][dest].split(",", -1);

            for (int i = 0; i < tripIDs.length; i++)
            {
                if (i != 0)
                {
                    res += "\nThen take ";
                }

                if (!t[i].equals("a transfer"))
                {
                    res += " trip ID ";
                }

                res += t[i] + " from " + s[i] + " to " + d[i] + ".";

                if (!t[i].equals("a transfer")) {
                    res = res + "\n" + enRoute(t[i], s[i], d[i]);
                }
            }
        }

        else
        {
            res += tripIDs[source][dest] + " from " + sources[source][dest] + " to " + dests[source][dest] + ".";

            if (!tripIDs[source][dest].equals("a transfer"))
            {
                res = res + "\n" + enRoute(tripIDs[source][dest], sources[source][dest], dests[source][dest]);
            }
        }

        if (res.equals("Take "))
        {
            return "Error";
        }

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

    public String enRoute(String tripID, String source, String dest)
    {
        String e = "En route you will pass stops: ";
        Trip current = getTripByID(tripID);
        boolean finish = false;
        boolean first = false;
        String[] theStops = current.getStopIDs();
        ArrayList<String> enRoute = new ArrayList<String>();

        for (int j = 0; j < theStops.length && !finish; j++) {
            if (!first && theStops[j].equals(sourceName)) {
                first = true;
            }

            if (first) {
                enRoute.add(theStops[j]);

                if (theStops[j].equals(destName)) {
                    finish = true;
                }
            }
        }

        for (int k = 0; k < enRoute.size() - 1; k++)
        {
            e = e + enRoute.get(k) + ", ";
        }

        e = e + enRoute.get(enRoute.size() - 1) + ".";

        return e;
    }
}