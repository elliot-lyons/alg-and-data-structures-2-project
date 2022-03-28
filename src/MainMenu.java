package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainMenu
{
    private boolean quit, first, firstRoute, firstStop, firstArrive;
    private Scanner s;
    private ArrayList<Stop> stops;
    private ArrayList<Trip> trips;
    private Distance[][] dists;

    public MainMenu(Scanner s)
    {
        this.s = s;
        quit = false;
        first = true;
        stops = createStops();
        //dists = createDists();
        trips = tripList();
        firstRoute = true;
        firstStop = true;
        firstArrive = true;
    }

    public boolean isQuit()
    {
        return quit;
    }

//    public ArrayList<Stop> stopList()
//    {
//        ArrayList<Stop> result = new ArrayList<Stop>();
//
//        try
//        {
//            BufferedReader br = new BufferedReader(new FileReader("transit_files//stops.txt"));
//            String current = br.readLine();
//
//            while ((current = br.readLine()) != null)
//            {
//                String[] line = current.split(",", -1);
//
//                int id = Integer.parseInt(line[0]);
//                String name = line[2];
//
//                Stop aStop = new Stop(id, name);
//                result.add(aStop);
//            }
//
//        }
//
//        catch (IOException e)
//        {
//            System.out.println("stops.txt file not found");
//        }
//
//        return result;
//    }

    public ArrayList<Stop> createStops()
    {
        ArrayList<Stop> result = new ArrayList<Stop>();

        try
        {
            BufferedReader br = new BufferedReader(new FileReader("transit_files//stops.txt"));
            String current = br.readLine();

            while ((current = br.readLine()) != null)
            {
                String[] line = current.split(",", -1);

                int id = Integer.parseInt(line[0]);
                String name = line[2];

                Stop s = new Stop(id, name);
                result.add(s);
            }

        }

        catch (IOException e)
        {
            System.out.println("stops.txt file not found");
        }

        return result;
    }

//    public Distance[][] createDists()
//    {
//        Distance[][] result = new Distance[trips.size()][trips.size()];
//
//        for (int i = 0; i < result.length; i++)
//        {
//            for (int j = 0; j < result[0].length; j++)
//            {
//                if (i == j)
//                {
//                    result[i][j] = new Distance("0", "0", 0, 0);
//                }
//
//                else
//                {
//                    result[i][j] = new Distance("0", "0", 0, (int) Double.POSITIVE_INFINITY);
//                }
//            }
//        }
//
//        for (int i = 0; i < result.length; i++)
//        {
//            for (int j = 0; j < result[0].length; j++)
//            {
//
//            }
//        }
//
//
//    }
//
//    public int getTripIndex(int tripID)
//    {
//        for (int i = 0; i < stops.size(); i++)
//        {
//            if (tripID == stops.get(i))
//            {
//                return i;
//            }
//        }
//
//        return -1;
//    }


    public ArrayList<Trip> tripList()
    {
        ArrayList<Trip> result = new ArrayList<Trip>();

        try
        {
            BufferedReader br = new BufferedReader(new FileReader("transit_files//stop_times.txt"));
            String current = br.readLine();
            String[] line = current.split(",", -1);
            String[] previous = line;
            previous[0] = "n/a";

            Trip t = null;
            Stop s = null;

            while ((current = br.readLine()) != null)
            {
                line = current.split(",", -1);

                if (!line[0].equals(previous[0]))
                {
                    t = new Trip(Integer.parseInt(line[0]));
                }

                int sID = Integer.parseInt(line[3]);

                for (int i = 0; i < stops.size(); i++)
                {
                    if (sID == (stops.get(i).getStopID()))
                    {
                        s = stops.get(i);
                        break;
                    }
                }

                if (s != null)
                {
                    s.setArrivalTime(line[1]);
                    s.setDepartureTime(line[2]);
                }


            }
        }

        catch (IOException e)
        {
            System.out.println("stop_times.txt file not found");
        }

        return result;
    }

    public void display()
    {
        while (!quit)
        {
            if (first)
            {
                System.out.println("Please choose from one of the following options by pressing the corresponding key:");
                System.out.println("1: Plan a route");
                System.out.println("2: See stop info");
                System.out.println("3: Search for all trips with a given arrival time");
                System.out.println("4: Quit");
                first = false;
            }

            try
            {
                String input = s.nextLine();

                switch (input)
                {
                    case "1":
                    {
                        if (firstRoute)
                        {
                            firstRoute = false;
                            stops = createStops();
                            //distances = createDistances(stops);
                        }
                        //RoutePlan routePlan = new RoutePlan(s, stops);
                        //routePlan.display();
                        break;
                    }

                    case "2":
                    {
                        StopInfo stopInfo = new StopInfo(s);
                        stopInfo.display();
                        break;
                    }

                    case "3":
                    {
                        ArrivalTime arrivalTime = new ArrivalTime(s);
                        arrivalTime.display();
                        break;
                    }

                    case "4":
                    {
                        quit = true;
                        break;
                    }

                    default:
                    {
                        System.out.println("Invalid input");
                        first = true;
                    }
                }
            }

            catch (Exception e)
            {}
        }
    }

    public int findDistance(String[] arrive, String[] depart)
    {
        int hours = Integer.parseInt(arrive[0]) - Integer.parseInt(depart[0]);
        int mins = Integer.parseInt(arrive[1]) - Integer.parseInt(depart[1]);
        int secs = Integer.parseInt(arrive[2]) - Integer.parseInt(depart[2]);

        hours = hours * 60 * 60;                        // converting into seconds
        mins *= mins;

        return hours + mins + secs;
    }
}
