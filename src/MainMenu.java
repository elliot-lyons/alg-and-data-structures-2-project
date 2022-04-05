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
    private DijkstraAllPairsSP dijk;

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
        System.out.println("Done stops");
        trips = new ArrayList<Trip>();
        ewd = new EdgeWeightedDigraph(stops.size());
        createDigraph();
        System.out.println("Done graph");
        dijk = new DijkstraAllPairsSP(ewd);

        firstRoute = true;
        firstStop = true;
        firstArrive = true;
    }

    public boolean isQuit() {
        return quit;
    }


    /**
     *
     * @return an array list of stops. A stop object contains the id and the name of the stop. The index of these stops
     * is important in the creation of the distances[][] and tripIDs[][] arrays, as explained below
     */

    public void createStops() {
        int count = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader("transit_files//smaller_stops.txt"));
            String current = br.readLine();

            while ((current = br.readLine()) != null) {
                String[] line = current.split(",", -1);

                String id = line[0];
                String name = meaningfulName(line[2]);

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

    public void createDigraph()
    {
        try {
            BufferedReader br = new BufferedReader(new FileReader("transit_files//smaller_stop_times.txt"));
            String current = br.readLine();
            String[] line = current.split(",", -1);
            String previous = line[0];
            previous = "n/a";
            boolean first = true;
            boolean time = true;

            while (current != null) {
                if (first) {        // if it's the first line of file, we ignore it as it's just a line of column headers
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
                int maxIndex = 0;                   // well as the stops

                do {
                    try {
                        time = validTime(arrival);          // make sure time < 23:59:59

                        if (time) {
                            if (sequence > maxIndex) {      // try to find the last stop in the sequence, this tells
                                maxIndex = sequence;        // us how long the array of stopIds need to be
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

                trip.setStops(tripStops);
                trip.setArrivalTimes(arrivalTimes);
                trips.add(trip);

                for (int i = 0; i < tripStops.length - 1; i++)                      // adding in the shortest direct paths
                {                                                               // between nodes
//                    System.out.println("i: " + tripStops[i]);
//                    System.out.println("i++ " + tripStops[i+1]);
                    DirectedEdge edge = new DirectedEdge(findIndex(tripStops[i]), findIndex(tripStops[i+1]), 1);
                    ewd.addEdge(edge);
                }
            }
        } catch (IOException e) {
            System.out.println("stop_times.txt not found");
        }


        // getting shortest costs from transfers.txt, same approach as above for stop_times.txt
        try {
            BufferedReader br = new BufferedReader(new FileReader("transit_files//smaller_transfers.txt"));
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
     * @return the index of that stop within the stops list
     *
     * This is a really important method for the program. The program reads in the stops first and adds them to a list
     * of stops, storing their stopID and their name. We use the index of these stops in other elements. Ie, if we read
     * a stop with id "111" in first, we add it to the stops ArrayList. This means its index will be 0. Then if we read
     * in stop with id "212" as the fourth entry in stops.txt, its index in the stops ArrayList will be 3.
     *
     * This is really important when we go to create our distances[][] and tripIds[][] arrays. To store the distance
     * between stops '111' and '212' and then the tripId of this distance, we will need to know where to store them
     * within those arrays. When we call findIndex(111) we will get returned 0. When we call findIndex(212) we will
     * get 3. We can then store the distance between these two nodes in distances[0][3] and the id(s) of this trip
     * in tripIDs[0][3]. It doesn't make sense to store them in distances[111][212] as the program only uses the indexes
     * in the list of stops to identify the stops, not the ids when storing information about them.
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