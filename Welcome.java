import java.util.Scanner;

public class Welcome                    // Welcoming the user to the program
{
    private boolean next;
    Welcome()
    {
        next = false;
    }

    public boolean isNext()
    {
        return next;
    }

    public void display()
    {
        Scanner sc = new Scanner(System.in);

        while (!next)
        {
            System.out.println("Welcome to the TransLink bus route planner! Please press 1 to continue");

            try
            {
                String input = sc.nextLine();

                if (input.equals("1"));
                {
                    next = true;
                }
            }

            catch (NullPointerException e)
            {
            }

        }
    }
}
