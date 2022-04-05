/**
 * Deals with part 1 of assignment
 */

package src;

import java.util.ArrayList;
import java.util.Scanner;

public class RoutePlan
{
    private boolean back, valid;
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

        do
        {
            do {
                if (!valid)
                {
                    System.out.print("Invalid input. ");
                }

                System.out.println("Please enter a source stop ID or 'back' to return to main menu:");

                input = s.nextLine();

                if (input.equals(null) || input.length() == 0)
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

             tripDetails();

            }
        } while (!back);
    }


    /**
     * Method that performs Dijkstra on two passed stopIds. This method is only called if the stopIDs exist, so no
     * risk of potential errors related to a stopId not existing arise.
     *
     * Outputs the details of quickest route between two stops (ie the stops passed from one stop to another and the
     * cost of said trip). If no route between the two exist we output that to the user. As above all other errors
     * are taken care of before we reach this method.
     */
    public void tripDetails() {
        String res = "";

        dijk = new DijkstraSP(weightedDigraph, findIndex(source));

        if (dijk.hasPathTo(findIndex(dest))) {                     // if there is a path between nodes
            String x = dijk.pathTo(findIndex(dest)).toString();    // store path in string
            String stopsArray[] = x.split("->", -1);    // breaks the path up to try separate stops

            // next few lines of code deal with breaking up the path we got from Dijkstra and storing it in a String
            // that the user will be able to understand

            ArrayList<Integer> theStops = new ArrayList<Integer>();

            for (int i = 0; i < stopsArray.length; i++) {
                if (i != 0) {
                    String[] y = stopsArray[i].split(" ", -1);
                    theStops.add(Integer.parseInt(y[0]));
                }
            }

            ArrayList<String> s = new ArrayList<String>();

            s.add(source);

            for (int i = theStops.size() - 1; i >= 0; i--) {        // path has stops in reverse order (destination 1st)
                s.add(findStop(theStops.get(i)));                   // this loop undoes that
            }

            for (int i = 0; i < s.size(); i++) {        // once stops are legible and in order, add them to a String
                res += s.get(i) + "\n";
            }

            System.out.println("Stops passed:");            // Outputting these stops and the cost of trip
            System.out.println(res);
            System.out.println("Cost: " + dijk.distTo(findIndex(dest)));
        }

        else
        {
            System.out.print("No trip between these two stops. ");
        }
    }


    /**
     * Same method as found in MainMenu
     * @param stopID: the stop we wish to find the index of
     * @return: the index (vertex number) for the requested stop (-1 if stop doesn't exist)
     */
    public int findIndex(String stopID) {
        for (int i = 0; i < stops.size(); i++) {
            if (stops.get(i).getStopID().equals(stopID)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Reverse of findIndex; tells us the stopId at vertex 'index' in EdgeWeightedDigraph / at index 'index' in stops
     * ArrayList
     * @param index: index of stop we want
     * @return: the associated stopID. ("n/a" if none exist, which should never arise in program as user never calls
     * this method and because findIndex has already been called before where this method is called, the index
     * will be within the appropriate bounds. ie all error handling has been handled outside this method)
     */
    public String findStop(int index)
    {
        if (index < stops.size() && index >= 0) {
            return stops.get(index).getStopID();
        }

        return "n/a";
    }
}