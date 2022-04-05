/**
 * Main menu of program where user decides what it is they wish to do.
 */

package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainMenu {
    private boolean quit, first, firstRoute, firstStop, firstArrive;
    private Scanner s;

    private ArrayList<Stop> stops;
    private ArrayList<Trip> trips;

    private TST<String> tst;
    private EdgeWeightedDigraph ewd;

    private RoutePlan routePlan;
    private ArrivalTime arrivalTime;
    private StopInfo stopInfo;

    public MainMenu(Scanner s) {
        this.s = s;
        quit = false;
        first = true;

        tst = new TST<String>();
        stops = new ArrayList<Stop>();
        createStops();
        trips = new ArrayList<Trip>();
        ewd = new EdgeWeightedDigraph(stops.size());
        createDigraph();

        firstRoute = true;
        firstStop = true;
        firstArrive = true;
    }

    public boolean isQuit() {
        return quit;
    }


    /**
     * This method reads in the stops.txt file. It creates an array list of stops objects. The index of these stop
     * objects are the vertex for which that stop is stored in the EdgeWeightDigraph
     *
     * Also a TST is created from the stops.txt file. We use the stop name as the key and store the data of the
     * associated stop that we then relay back to the user should they search for that specific stop for part 2 of
     * assignment.
     */
    public void createStops() {
        int count = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader("transit_files//stops.txt"));
            String current = br.readLine();

            while ((current = br.readLine()) != null) {
                String[] line = current.split(",", -1);

                String id = line[0];
                String name = meaningfulName(line[2]);  // storing NB/SB/EB/WB at the end of name, as per spec

                Stop s = new Stop(id, name);

                String data = "";
                data += "Stop ID: " + id;
                data += ". Stop code: " + line[1];
                data += ". Stop latitude: " + line[4];
                data += ". Stop longitude: " + line[5] + ".";

                tst.put(name, data);
                stops.add(s);
            }

        } catch (IOException e) {
            System.out.println("stops.txt file not found");
        }
    }


    /**
     * To give part 2 of assignment more utility, as per project spec
     *
     * @param stopName: A 'raw' name read in from stops.txt
     * @return that stopName with the appropriate modifications (moving 'North/South/East/Westbound to end of name)
     */
    public String meaningfulName(String stopName)
    {

        if (stopName.lastIndexOf("Northbound") == 0) {
            stopName = stopName.replaceAll("Northbound ", "");
            return stopName + " Northbound";
        }

        else if (stopName.lastIndexOf("Southbound") == 0) {
            stopName = stopName.replaceAll("Southbound ", "");
            return stopName + " Southbound";
        }

        else if (stopName.lastIndexOf("Eastbound") == 0) {
            stopName = stopName.replaceAll("Eastbound ", "");
            return stopName + " Eastbound";
        }

        else if (stopName.lastIndexOf("Westbound") == 0) {
            stopName = stopName.replaceAll("Westbound ", "");
            return stopName + " Westbound";
        }

        return stopName;
    }


    /**
     * Creating a graph of directed edges from one stop to another. This is used for part one of assignment
     */
    public void createDigraph()
    {
        try {               // first we store vertexes from stop_times.txt
            BufferedReader br = new BufferedReader(new FileReader("transit_files//stop_times.txt"));
            String current = br.readLine();
            String[] line = current.split(",", -1);
            String previous = line[0];
            previous = "n/a";
            boolean first = true;
            boolean time = true;

            while (current != null) {
                if (first) {       // if it's the first line of file, we ignore it as it's just a line of column headers
                    current = br.readLine();
                    line = current.split(",", -1);
                    first = false;
                }

                ArrayList<String> stopIDs = new ArrayList<String>();
                ArrayList<Integer> sequences = new ArrayList<Integer>();
                ArrayList<String> arrivals = new ArrayList<String>();

                String tripID = line[0];
                String stopID = line[3];
                String arrival = line[1];
                int sequence = Integer.parseInt(line[4]);

                Trip trip = new Trip(tripID);       // creating a trip object which stores the tripId of said trip as
                int maxIndex = 0;                   // well as where bus stops on trip and the times it does so

                do {
                    try {
                        time = validTime(arrival);          // make sure time < 23:59:59

                        if (time) {
                            if (sequence > maxIndex) {      // try to find the last stop in the sequence, this tells
                                maxIndex = sequence;        // us how long the arrays of data need to be
                            }
                            stopIDs.add(stopID);            // for time being we add the data associated with this stop
                            sequences.add(sequence);        // to lists of those pieces of data
                            arrivals.add(arrival);
                        }

                        previous = line[0];
                        current = br.readLine();

                        if (current != null) {          // move onto next line
                            line = current.split(",", -1);
                            tripID = line[0];
                            stopID = line[3];
                            arrival = line[1].replaceAll(" ", "0");
                            time = validTime(arrival);
                            sequence = Integer.parseInt(line[4]);
                        } else {
                            break;
                        }
                    } catch (NullPointerException e) {
                        System.out.println("All ok!");
                    }

                } while (tripID.equals(previous));          // while we're dealing with same tripID

                // once we reach here, we know we have all the data we need about a trip

                String[] tripStops = new String[maxIndex];
                String[] arrivalTimes = new String[maxIndex];

                // this creates ordered arrays of the stopIDs. The arrival times are also stored in the correct indexes
                // we use the sequences we gathered to tell us the index of these pieces of data

                for (int i = 0; i < tripStops.length; i++) {
                    int index = sequences.get(i) - 1;
                    String id = stopIDs.get(i);
                    String tiempo = arrivals.get(i);
                    tripStops[index] = id;
                    arrivalTimes[index] = tiempo;
                }

                // once the data for said trip is in correct order we store it in the current trip object

                trip.setStops(tripStops);
                trip.setArrivalTimes(arrivalTimes);

                // we have all data we need now in the trip object, so store it in the ArrayList of those trip objects

                trips.add(trip);

                // adding in directed edges between stops. If stops are consecutive, their weight is 1. Again the place
                // these stops are in the ArrayList of stops, is their vertex. So we find the two indexes of those
                // specific stops before we store the distance between them in an edge and consequently the graph

                for (int i = 0; i < tripStops.length - 1; i++)
                {
                    DirectedEdge edge = new DirectedEdge(findIndex(tripStops[i]), findIndex(tripStops[i+1]), 1);
                    ewd.addEdge(edge);
                }
            }
        } catch (IOException e) {
            System.out.println("stop_times.txt not found");
        }


        // getting shortest costs from transfers.txt, same approach as above for stop_times.txt
        try {
            BufferedReader br = new BufferedReader(new FileReader("transit_files//transfers.txt"));
            String current = br.readLine();

            while ((current = br.readLine()) != null) {
                String[] line = current.split(",", -1);

                String source = line[0];
                String destination = line[1];
                int type = Integer.parseInt(line[2]);
                double dist = -1;

                if (type == 2) {
                    dist = Double.parseDouble(line[3]) / 100;
                } else if (type == 0) {
                    dist = 2;
                } else {
                    System.out.println("Error, invalid transfer type in transfers.txt.");
                    return;
                }

                int sIndex = findIndex(source);
                int dIndex = findIndex(destination);

                // Same approach as above, except we don't store the data as a trip because it's a transfer

                DirectedEdge edge = new DirectedEdge(sIndex, dIndex, dist);
                ewd.addEdge(edge);
            }
        } catch (IOException e) {
            System.out.println("transfers.txt not found");
        }

    }


    /**
     *
     * @param stopID: passes in a stopId that we wish to know index of
     * @return the index of that stop within the stops list (-1 if said stop doesn't exist)
     *
     * As above the vertexes in the graph aren't represented by the stopIds themselves. Instead they're represented by
     * the index of that stopId within the ArrayList of stops. Because of this we need a method to find said index so
     * we can create DirectedEdges. The below method allows us to do so.
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
     *
     * @param time: a 'raw' time read in from stop_times.txt
     * @return boolean stating whether time is valid (i.e. < 23:59:59, as per project specifications
     */
    public boolean validTime(String time)
    {
        try
        {
            String[] times = time.split(":", 3);
            String hour = times[0].replaceAll(" ", "");

            if (Integer.parseInt(hour) > 23)
            {
                return false;
            }
        }

        catch (Exception e)
        {
            return false;
        }

        return true;
    }


    /**
     * The UI of the program
     */
    public void display() {
        while (!quit) {
            System.out.println("Please choose from one of the following options by pressing the corresponding key:");
            System.out.println("1: Plan a route");
            System.out.println("2: See stop info");
            System.out.println("3: Search for all trips with a given arrival time");
            System.out.println("4: Quit");

            try {
                String input = s.nextLine();

                switch (input) {
                    case "1": {

                        // Below only done if user's first time pressing 1 in main menu. Similar if statements for
                        // other two inputs as well

                        if (firstRoute) {
                            System.out.println("This will tell you the quickest path from one stop to another!");
                            firstRoute = false;
                            routePlan = new RoutePlan(s, ewd, stops);
                        }

                        routePlan.display();
                        break;
                    }

                    case "2": {
                        if (firstStop)
                        {
                            System.out.println("This will display information of a certain stop!");
                            stopInfo = new StopInfo(s, tst);
                        }

                        stopInfo.display();
                        break;
                    }

                    case "3": {
                        if (firstArrive) {
                            System.out.println("This will tell you all trips that arrive at a stop at a specific time!");
                            arrivalTime = new ArrivalTime(s, trips);
                            firstArrive = false;
                        }
                        arrivalTime.display();
                        break;
                    }

                    case "4": {
                        quit = true;
                        break;
                    }

                    default: {
                        System.out.println("Invalid input");
                    }
                }
            } catch (Exception e) {
            }
        }
    }
}