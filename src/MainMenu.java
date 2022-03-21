import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainMenu
{
    private boolean quit, first;
    private Scanner s;
    private ArrayList<Stop> stops;
    private ArrayList<Trip> trips;

    public MainMenu(Scanner s)
    {
        this.s = s;
        quit = false;
        first = true;
        stops = stopList();
        trips = tripList();
    }

    public boolean isQuit()
    {
        return quit;
    }

    public ArrayList<Stop> stopList()
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

                Stop aStop = new Stop(id, name);
                result.add(aStop);
            }

        }

        catch (IOException e)
        {
            System.out.println("stops.txt file not found");
        }

        return result;
    }

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

            while ((current = br.readLine()) != null)
            {
                ArrayList<String> lines = new ArrayList<String>();

                while (line[0].equals(previous[0]))
                {
                    if (first)
                    {
                        first = false;
                    }


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
                        RoutePlan routePlan = new RoutePlan(s, stops);
                        routePlan.display();
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
}
