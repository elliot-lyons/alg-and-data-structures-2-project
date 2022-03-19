import java.util.Scanner;

public class MainMenu
{
    private boolean quit, first;
    private Scanner s;

    public MainMenu(Scanner s)
    {
        this.s = s;
        quit = false;
        first = true;
    }

    public boolean isQuit()
    {
        return quit;
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
                        RoutePlan routePlan = new RoutePlan(s);
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
