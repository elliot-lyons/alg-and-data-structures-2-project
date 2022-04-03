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
    private double[][] distances;
    private String[][] tripIDs, sources, dests;
    private RoutePlan routePlan;
    private ArrivalTime arrivalTime;

    public MainMenu(Scanner s) {
        this.s = s;
        quit = false;
        first = true;

        stops = createStops();
        trips = new ArrayList<Trip>();
        tripIDs = new String[stops.size()][stops.size()];
        sources = new String[stops.size()][stops.size()];
        dests = new String[stops.size()][stops.size()];
        distances = createDistances();
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

    public ArrayList<Stop> createStops() {
        ArrayList<Stop> result = new ArrayList<Stop>();

        try {
            BufferedReader br = new BufferedReader(new FileReader("transit_files//smaller_stops.txt"));
            String current = br.readLine();

            while ((current = br.readLine()) != null) {
                String[] line = current.split(",", -1);

                String id = line[0];
                String name = meaningfulName(line[2]);

                Stop s = new Stop(id, name);
                result.add(s);
            }

        } catch (IOException e) {
            System.out.println("stops.txt file not found");
        }

        return result;
    }

    /**
     *
     * @param stopName: A 'raw' name read in from stops.txt
     * @return that stopName with the appropriate modifications (moving 'North/South/East/Westbound to end of name)
     */
    public String meaningfulName(String stopName)
    {
        String result = "";

        // if (stopName.contains("N"))
        // {}

        return result;
    }


    /**
     *
     * @return: a 2D double array with all the direct paths from one stop to another stop. This method also updates
     * a 2D String array of tripIDs. The tripID array represents the trip you take for that quickest path.
     * i.e. if the cost from a -> b was 3 and the trip you took for that cost was 112, distances[a][b] would be 3 and
     * tripIDs[a][b] 112. (Slightly different how indexes are calculated, explained below
     */

    public double[][] createDistances() {
        double[][] result = new double[stops.size()][stops.size()];

        // initially we just set the distances between stops to be infinity or if they're the same stop; 0.

        for (int i = 0; i < stops.size(); i++) {
            for (int j = 0; j < stops.size(); j++) {
                if (i == j) {
                    result[i][j] = 0;
                    tripIDs[i][j] = "Same stop";
                } else {
                    result[i][j] = Double.POSITIVE_INFINITY;
                    tripIDs[i][j] = "n/a";
                }
            }
        }

        // first we get the costs from stop_times.txt

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
                String prevTripID = "";
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
                            prevTripID = tripID;
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

                for (int i = 0; i < tripStops.length; i++)                      // adding in the shortest direct paths
                {                                                               // between nodes
                    for (int j = i + 1; j < tripStops.length; j++) {
                        double dis = (double) j - i;

                        // as per project spec, we know that consecutive stops have a cost of 1. This means the
                        // difference in sequence between stops, is their cost. I.e. if stop a is two stops after
                        // stop b, the cost of going from b -> a = 2

                        int indexI = findIndex(tripStops[i]);       // finding what indexes represent our current stops
                        int indexJ = findIndex(tripStops[j]);       // important step, more about below

                        if (indexI < 0 || indexJ < 0) {
                            System.out.println("Error: indexI: " + indexI + " indexJ " + indexJ);
                        } else {

                            // for now we are just creating a 2d array that stores the costs between stops
                            // we will store the distances if the current distance is shorter than the one already there
                            // if no route between them, remains as infinity

                            if (dis < result[indexI][indexJ]) {
                                result[indexI][indexJ] = dis;
                                tripIDs[indexI][indexJ] = prevTripID;
                                sources[indexI][indexJ] = tripStops[i];
                                dests[indexI][indexJ] = tripStops[j];
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("stop_times.txt not found");
        }

        // getting shortest costs from transfers.txt, same approach as above for stop_times.txt
        try {
            BufferedReader br = new BufferedReader(new FileReader("transit_files//smaller_transfers.txt"));
            String current = br.readLine();
            int count = 1;                  // used for letting user know the line no. of error if there is one in the file

            while ((current = br.readLine()) != null) {
                String[] line = current.split(",", -1);
                count++;

                String source = line[0];
                String destination = line[1];
                int type = Integer.parseInt(line[2]);
                double dist = -1;

                if (type == 2) {
                    dist = Double.parseDouble(line[3]) / 100;
                } else if (type == 0) {
                    dist = 2;
                } else {
                    System.out.println("Error, invalid transfer type in transfers.txt for journey from stop: " + source
                            + " to stop " + destination + " on line " + count + ".");
                }

                int indexI = findIndex(source);
                int indexJ = findIndex(destination);

                if (dist < result[indexI][indexJ]) {
                    result[indexI][indexJ] = dist;
                    tripIDs[indexI][indexJ] = "a transfer";
                    sources[indexI][indexJ] = source;
                    dests[indexI][indexJ] = destination;
                }
            }
        } catch (IOException e) {
            System.out.println("transfers.txt not found");
        }

        return result;
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

    public String[][] getTripIDs() {
        return tripIDs;
    }


    /**
     * This is doing Floyd Warshall algorithm to find shortest path between stops
     */

    public void floydWarshall()
    {
        for (int i = 0; i < distances.length; i++)
        {
            for (int j = 0; j < distances.length; j++)
            {
                for (int k = 0; k < distances.length; k++)
                {
                    if (distances[j][i] + distances[i][k] < distances[j][k])
                    {
                        distances[j][k] = distances[j][i] + distances[i][k];

                        // should algorithm reach here, it suggests you need to take multiple busses
                        // we make a note of the tripIds of these busses, as well as the stopIds that you need to
                        // transfer from.

                        // eg if a->c then c->b is shorter than a->b, we record the tripId of the trips from a->c then
                        // c->b. We add a and c as sources, as this is where you catch the busses from and add c and b as
                        // destinations as this is where you get off the busses

                        tripIDs[j][k] = tripIDs[j][i] + "," + tripIDs[i][k];
                        sources[j][k] = sources[j][i] + "," + sources[i][k];
                        dests[j][k] = dests[j][i] + "," + dests[i][k];
                    }
                }
            }
        }
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
                            floydWarshall();
                            routePlan = new RoutePlan(s, distances, tripIDs, stops, trips, sources, dests);
                        }

                        routePlan.display();
                        break;
                    }

                    case "2": {
                        StopInfo stopInfo = new StopInfo(s);
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
                        first = true;
                    }
                }
            } catch (Exception e) {
            }
        }
    }
}