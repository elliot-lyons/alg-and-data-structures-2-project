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

            System.out.println("Please input a certain time (in the format HH:MM:SS (please include colons)):");
            String input = s.nextLine();

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
                    ArrayList<Trip> chosen = null;

                    for (int i = 0; i < trips.size(); i++)
                    {


                    }
                }
            }

            else
            {
                valid = false;
            }
        }

    }
}
