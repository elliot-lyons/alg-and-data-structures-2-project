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
    private int[][] distances;
    private String[][] tripIDs;
    //private Distance[][] dists;

    public MainMenu(Scanner s)
    {
        this.s = s;
        quit = false;
        first = true;
        stops = createStops();
        distances = createDistances(stops);
        //dists = createDists();
        //trips = tripList();
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

                String id = line[0];
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

    public int[][] createDistances(ArrayList<Stop> stops)
    {
        int[][] result = new int[stops.size()][stops.size()];
        tripIDs = new String[stops.size()][stops.size()];

        for (int i = 0; i < result.length; i++)
        {
            for (int j = 0; j < result.length; j++)
            {
                if (i == j)
                {
                    result[i][j] = 0;
                    tripIDs[i][j] = "Same stop";
                }

                else
                {
                    result[i][j] = (int) Double.POSITIVE_INFINITY;
                    tripIDs[i][j] = "n/a";
                }
            }
        }

        try
        {
            BufferedReader br = new BufferedReader(new FileReader("transit_files//small_stop_times.txt"));
            String current = br.readLine();
            String[] line = current.split(",", -1);
            String previous = line[0];
            previous = "n/a";
            boolean first = true;

            while(current != null)
            {
                if (first)
                {
                    current = br.readLine();
                    line = current.split(",", -1);
                }

                ArrayList<String> stopIDs = new ArrayList<String>();
                ArrayList<Integer> sequences = new ArrayList<Integer>();

                String tripID = line[0];
                String stopID = line[3];
                int sequence = Integer.parseInt(line[4]);

                Trip trip = new Trip(tripID);
                int maxIndex = 0;

                do
                {
                    if (tripID.equals(previous) || first)
                    {
                        if (first)
                        {
                            first = false;
                        }

                        if (sequence > maxIndex)
                        {
                            maxIndex = sequence;
                        }

                        stopIDs.add(stopID);
                        sequences.add(sequence);

                        previous = line[0];
                        current = br.readLine();
                        line = current.split(",", -1);
                        tripID = line[0];
                        stopID = line[3];
                        System.out.println("Stop: " + stopID);
                        sequence = Integer.parseInt(line[4]);
                    }
                } while (tripID.equals(previous));

                String[] tripStops = new String[maxIndex];

                for (int i = 0; i < tripStops.length; i++)
                {
                    int index = sequences.get(i) - 1;
                    String id = stopIDs.get(i);
                    tripStops[index] = id;
                }

                trip.setStops(tripStops);

                for (int i = 0; i < tripStops.length; i++)                      // adding in the shortest direct paths
                {                                                               // between nodes
                    for (int j = i + 1; j < tripStops.length; j++)
                    {
                        int dis = j - 1;
                        int indexI = findIndex(tripStops[i]);
                        int indexJ = findIndex(tripStops[j]);

                        if (indexI < 0 || indexJ < 0)
                        {
                            System.out.println("Error: indexI: " + indexI + " indexJ " + indexJ);
                        }

                        else
                        {
                            if (dis < result[indexI][indexJ])
                            {
                                result[indexI][indexJ] = dis;
                                tripIDs[indexI][indexJ] = tripID;
                            }
                        }
                    }
                }
            }
        }

        catch (IOException e)
        {
            System.out.println("stop_times.txt not found");
        }

        return result;
    }

    public int findIndex(String stopID)
    {
        for (int i = 0; i < stops.size(); i++)
        {
            if (stops.get(i).getStopID().equals(stopID))
            {
                return i;
            }
        }

        return -1;
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

//    public ArrayList<Stop> makeStops()
//    {
//        ArrayList<String> result = new ArrayList<String>();
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
//                Stop s = new Stop(id, name);
//                result.add(s);
//            }
//
//        }
//
//        catch (IOException e)
//        {
//            System.out.println("stops.txt file not found");
//        }
//    }


//    public ArrayList<Trip> tripList()
//    {
//        ArrayList<Trip> result = new ArrayList<Trip>();
//
//        try
//        {
//            BufferedReader br = new BufferedReader(new FileReader("transit_files//stop_times.txt"));
//            String current = br.readLine();
//            String[] line = current.split(",", -1);
//            String[] previous = line;
//            previous[0] = "n/a";
//            boolean f = true;
//
//            System.out.println("Yes");
//
//            Trip t = null;
//            Stop s = null;
//
//            while ((current = br.readLine()) != null)
//            {
//                line = current.split(",", -1);
//
//                if (!line[0].equals(previous[0]))
//                {
//                    if (!f)
//                    {
//                        result.add(t);
//                    }
//
//                    else
//                    {
//                        f = false;
//                    }
//                    t = new Trip(Integer.parseInt(line[0]));
//                }
//
//                int sID = Integer.parseInt(line[3]);
//                //System.out.println(sID);
//
//                for (int i = 0; i < stops.size(); i++)
//                {
//                    if (sID == (stops.get(i).getStopID()))
//                    {
//                        s = stops.get(i);
//                        break;
//                    }
//                }
//
//                String[] a = line[1].split(":", -1);
//                a[0] = a[0].replaceAll("\\s","0");
//                int time = Integer.parseInt(a[0]);
//
//                if (time < 25)                // (Error handling by not looking at stops w arrival
//                {                                               // time greater than 24 (This may need to be moved if
//                    if (s != null)                              // RoutePlan can have stops with values greater than
//                    {                                           // 24
//                        s.setArrivalTime(line[1]);
//                        s.setDepartureTime(line[2]);
//                    }
//                }
//
//            }
//        }
//
//        catch (IOException e)
//        {
//            System.out.println("stop_times.txt file not found");
//        }
//
//        return result;
//    }

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

                            Trip t = trips.get(1);

                            System.out.println(t.getTripID());
                            stops = createStops();
                            distances = createDistances(stops);

                            System.out.println("Distance: " + distances[11][11] + " Trip: " + tripIDs[11][11]);
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
                        ArrivalTime arrivalTime = new ArrivalTime(s,trips);
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
