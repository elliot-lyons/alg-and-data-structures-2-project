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
                    " or 'back' to return home:");
            String input = s.nextLine();

            if (input.equals("back"))
            {
                return;
            }

            String[] inputs = input.split(":", -1);

            if (inputs.length == 3)
            {
                for (int i = 0; i < inputs.length; i++)
                {
                    String current = inputs[i];

                    if (current.length() > 2)
                    {
                        valid = false;
                    }

                    if (current.length() < 2)
                    {
                        inputs[i] = "0" + inputs[i];
                    }
                }

                if (valid)
                {
                    String time = "";

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

                    for (int i = 0; i < trips.size(); i++)
                    {
                        String[] times = trips.get(i).getArrivalTimes();
                        int search = stringBinarySearch(times, time, 0, times.length - 1);

                        if (search > -1)
                        {
                            here.add(search);
                            chosen.add(trips.get(i));
                        }
                    }

                    if (chosen.size() > 0)
                    {
                        if (chosen.size() > 1) {
                            System.out.println("There are " + chosen.size() + " trips that have a stop with this " +
                                    "arrival time. They include:");
                        }

                        else {
                            System.out.println("There is one trip with this arrival time, Trip: " +
                                    chosen.get(0).getTripID() + ". Which stops at:");
                        }

                            for (int i = 0; i < chosen.size(); i++) {
                                System.out.println("Trip: " + chosen.get(i).getTripID() + ". Which stops at:");
                                String x = "";
                                String[] theStops = chosen.get(i).getStopIDs();
                                int theStop = here.get(i);

                                for (int j = 0; j < theStops.length; j++) {
                                    if (j > 0)
                                    {
                                        x += "\n";
                                    }

                                    x += theStops[j];

                                    if (j == theStop)
                                    {
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
