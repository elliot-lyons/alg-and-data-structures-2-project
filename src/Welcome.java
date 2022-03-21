import java.util.Scanner;

public class Welcome                    // Welcoming the user to the program
{
    private boolean next, first;
    private Scanner s;

    Welcome(Scanner s)
    {
        this.s = s;
        first = true;
        next = false;
    }

    public boolean isNext()
    {
        return next;
    }

    public void display()
    {

        while (!next) {
            if (first) {
                System.out.println("Welcome to the TransLink bus route planner! Please press 1 to continue");
                first = false;
            }

            try {
                String input = s.nextLine();

                if (input.equals("1")) {
                    next = true;
                } else {
                    first = true;
                    System.out.println("Invalid input");
                }
            } catch (Exception e) {
            }
        }
    }
}
