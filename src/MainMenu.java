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
    RoutePlan routePlan;

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

    public ArrayList<Stop> createStops() {
        ArrayList<Stop> result = new ArrayList<Stop>();

        try {
            BufferedReader br = new BufferedReader(new FileReader("transit_files//smaller_stops.txt"));
            String current = br.readLine();

            while ((current = br.readLine()) != null) {
                String[] line = current.split(",", -1);

                String id = line[0];
                String name = line[2];

                Stop s = new Stop(id, name);
                result.add(s);
            }

        } catch (IOException e) {
            System.out.println("stops.txt file not found");
        }

        return result;
    }

    public double[][] createDistances() {
        double[][] result = new double[stops.size()][stops.size()];

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

        try {
            BufferedReader br = new BufferedReader(new FileReader("transit_files//smaller_stop_times.txt"));
            String current = br.readLine();
            String[] line = current.split(",", -1);
            String previous = line[0];
            previous = "n/a";
            boolean first = true;
            boolean time = true;

            while (current != null) {
                if (first) {
                    current = br.readLine();
                    line = current.split(",", -1);
                    first = false;
                }

                ArrayList<String> stopIDs = new ArrayList<String>();
                ArrayList<Integer> sequences = new ArrayList<Integer>();

                String tripID = line[0];
                String prevTripID = "";
                String stopID = line[3];
                String arrival = line[1];
                int sequence = Integer.parseInt(line[4]);

                Trip trip = new Trip(tripID);
                int maxIndex = 0;

                do {
                    try {
                        time = validTime(arrival);

                        if (time) {
                            if (sequence > maxIndex) {
                                maxIndex = sequence;
                            }
                            stopIDs.add(stopID);
                            sequences.add(sequence);
                        }

                        previous = line[0];
                        current = br.readLine();

                        if (current != null) {
                            line = current.split(",", -1);
                            prevTripID = tripID;
                            tripID = line[0];
                            stopID = line[3];
                            arrival = line[1];
                            time = validTime(arrival);
                            sequence = Integer.parseInt(line[4]);
                        } else {
                            break;
                        }
                    } catch (NullPointerException e) {
                        System.out.println("All ok!");
                    }

                } while (tripID.equals(previous));

                String[] tripStops = new String[maxIndex];

                for (int i = 0; i < tripStops.length; i++) {
                    int index = sequences.get(i) - 1;
                    String id = stopIDs.get(i);
                    tripStops[index] = id;
                }

                trip.setStops(tripStops);
                trips.add(trip);


                for (int i = 0; i < tripStops.length; i++)                      // adding in the shortest direct paths
                {                                                               // between nodes
                    for (int j = i + 1; j < tripStops.length; j++) {
                        double dis = (double) j - i;

                        String one = tripStops[i];
                        String two = tripStops[j];

                        int indexI = findIndex(tripStops[i]);
                        int indexJ = findIndex(tripStops[j]);

                        double dist = result[indexI][indexJ];
                        String id = tripIDs[indexI][indexJ];

                        if (indexI < 0 || indexJ < 0) {
                            System.out.println("Error: indexI: " + indexI + " indexJ " + indexJ);
                        } else {
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

        // getting shortest costs from transfers.txt
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
                        tripIDs[j][k] = tripIDs[j][i] + "," + tripIDs[i][k];
                        sources[j][k] = sources[j][i] + "," + sources[i][k];
                        dests[j][k] = dests[j][i] + "," + dests[i][k];
                    }
                }
            }
        }
    }

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
                        ArrivalTime arrivalTime = new ArrivalTime(s, trips);
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