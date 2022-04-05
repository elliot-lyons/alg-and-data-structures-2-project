package src;

import java.util.ArrayList;
import java.util.Scanner;

public class RoutePlan
{
    private boolean back, valid, route;
    private String input, source, dest;
    private Scanner s;
    private ArrayList<Stop> stops;
    private EdgeWeightedDigraph weightedDigraph;
    private DijkstraSP dijk;

    RoutePlan(Scanner s, EdgeWeightedDigraph weightedDigraph, ArrayList<Stop> stops)
    {
        this.s = s;
        this.weightedDigraph = weightedDigraph;
        this.stops = stops;
    }

    public void display()
    {
        back = false;
        valid = true;
        route = true;

        do
        {
            do {
                if (!valid)
                {
                    System.out.print("Invalid input. ");
                }

                if (!route)
                {
                    System.out.print("No route between specified stops. ");
                    route = true;
                }

                System.out.println("Please enter a source stop ID or 'back' to return to main menu:");

                input = s.nextLine();

                if (input.equals(null))
                {
                    valid = false;
                }

                else if (input.equalsIgnoreCase("back")) {
                    return;
                }

                else
                {
                    valid = false;

                    for (int i = 0; i < stops.size() && !valid; i++) {
                        if (input.equals(stops.get(i).getStopID())) { // confirming stop is in list
                            valid = true;
                            source = input;
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

                    System.out.println("Please enter a destination stop ID or 'back' to return to main menu:");

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
                                    dest = input;
                                }
                            }
                        }
                    }

                } while (!valid);

             tripInstructions();

            }
        } while (!back);
    }

    public void tripInstructions() {
        String res = "";

        dijk = new DijkstraSP(weightedDigraph, findIndex(source));

        if (dijk.hasPathTo(findIndex(dest))) {
            String x = dijk.pathTo(findIndex(dest)).toString();
            String stopsArray[] = x.split("->", -1);

            ArrayList<Integer> theStops = new ArrayList<Integer>();

            for (int i = 0; i < stopsArray.length; i++) {
                if (i != 0) {
                    String[] y = stopsArray[i].split(" ", -1);
                    theStops.add(Integer.parseInt(y[0]));
                }
            }

            ArrayList<String> s = new ArrayList<String>();

            s.add(source);

            for (int i = theStops.size() - 1; i >= 0; i--) {
                s.add(findStop(theStops.get(i)));
            }

            for (int i = 0; i < s.size(); i++) {
                res += s.get(i) + "\n";
            }

            System.out.println("Stops passed:");
            System.out.println(res);
            System.out.println("Cost: " + dijk.distTo(findIndex(dest)));
        }

        else
        {
            System.out.print("No trip between these two stops. ");
        }
    }

    public int findIndex(String stopID) {
        for (int i = 0; i < stops.size(); i++) {
            if (stops.get(i).getStopID().equals(stopID)) {
                return i;
            }
        }
        return -1;
    }

    public String findStop(int index)
    {
        return stops.get(index).getStopID();
    }
}