package src;

import java.util.ArrayList;
import java.util.Scanner;

public class ArrivalTime
{
    private Scanner s;
    private boolean back;
    private ArrayList<Trip> trips;
    private boolean valid, quit;

    ArrivalTime(Scanner s, ArrayList<Trip> trips)
    {
        this.s = s;
        back = false;
        this.trips = trips;
        valid = true;
        boolean quit = false;
    }

    public void display()
    {
        while (!quit || !valid)
        {

            if (!valid)
            {
                System.out.println("Invalid input. Please enter in the specified format below:");
                valid = true;
            }

            System.out.println("Please input a certain time (in the format HH:MM:SS (please include colons))" +
                    " or 'back' to return to main menu.:");
            String input = s.nextLine();

            if (input.equals(null))
            {
                valid = false;
            }

            else if (input.equalsIgnoreCase("back"))
            {
                return;
            }

            String[] inputs = input.split(":", -1);

            // below is all error handling of user input

            if (inputs.length == 3)     // if not the case, user didn't put in 2 ':'. This means input = invalid
            {
                for (int i = 0; i < inputs.length; i++)
                {
                    inputs[i] = inputs[i].replaceAll(" ", ""); // if user decided to add spaces, eliminate them
                    String current = inputs[i];

                    if (current.length() > 2 || current.length() == 0)        // no time value is greater than 2 digits
                    {                                                         // or less than 1
                        valid = false;
                    }

                    if (current.length() < 2)               // person may have forgotten to add a zero in a place
                    {                                       // ie 7 instead of 07
                        inputs[i] = "0" + current;
                    }

                }

                if (valid)
                {
                    String time = "";               // storing the time back in a string now we know it's valid

                    for (int i = 0; i < inputs.length; i++)
                    {
                        time += inputs[i];

                        if (i < inputs.length - 1)
                        {
                            time += ":";
                        }
                    }

                    ArrayList<Trip> chosen = new ArrayList<Trip>();
                    ArrayList<Integer> here = new ArrayList<Integer>();

                    for (int i = 0; i < trips.size(); i++)          // go through all trips with times
                    {
                        String[] times = trips.get(i).getArrivalTimes();
                        int search = stringBinarySearch(times, time, 0, times.length - 1);  // search trip for
                        // this time

                        if (search > -1)         // if it's present in said trip
                        {
                            here.add(search);       // recording index of stop so we can point it out in output
                            chosen.add(trips.get(i));   // recording trip it's present in
                        }
                    }

                    if (chosen.size() > 0)      // if at least one trip exists with said arrival time
                    {
                        if (chosen.size() > 1) {
                            System.out.println("There are " + chosen.size() + " trips that have a stop with this " +
                                    "arrival time. They include:");
                        }

                        else {
                            System.out.println("There is one trip with this arrival time, Trip: " +
                                    chosen.get(0).getTripID() + ". Which stops at:");
                        }

                        for (int i = 0; i < chosen.size(); i++) {       // listing off those trips
                            System.out.println("Trip: " + chosen.get(i).getTripID() + ". Which stops at:");
                            String x = "";
                            String[] theStops = chosen.get(i).getStopIDs();
                            int theStop = here.get(i);

                            for (int j = 0; j < theStops.length; j++) {     // listing off the stops of that trip
                                if (j > 0)
                                {
                                    x += "\n";
                                }

                                x += theStops[j];

                                if (j == theStop)       // pointing out the stop at which a bus arrives at that
                                {                       // time
                                    x += " - The bus arrives at this stop at: " + time + ".";
                                }
                            }

                            x += "\n----------------------------------\n";
                            System.out.println(x);
                        }
                    }
                    else
                    {
                        System.out.println("There are no trips with this arrival time.");
                    }
                }
            }

            else
            {
                valid = false;
            }
        }

    }

    /**
     *
     * @param theArray: array of arrival times
     * @param theString: the arrival time we're looking for
     * @param min: min and max used for keepin track of where we're searching in array
     * @param max
     *
     * @return: the index that the time is stored at if it's in array, if not -1
     */
    public int stringBinarySearch(String[] theArray, String theString, int min, int max)
    {
        while (min <= max)
        {
            int mid = (min + max) / 2;

            if (theArray[mid].compareTo(theString) < 0)
            {
                min = mid + 1;
            }

            else if (theArray[mid].compareTo(theString) > 0)
            {
                max = mid - 1;
            }

            else
            {
                return mid;
            }
        }

        return -1;
    }
}