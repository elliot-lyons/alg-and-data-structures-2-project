/**
 * Simple class that welcomes user to program.
 */

package src;

import java.util.Scanner;

public class Welcome
{
    private boolean next, first, quit;
    private Scanner s;

    Welcome(Scanner s)
    {
        this.s = s;
        first = true;
        next = false;
        quit = false;
    }

    public boolean isNext()
    {
        return next;
    }

    public boolean isQuit() {
        return quit;
    }

    public void display()
    {

        while (!next && !quit) {
            if (first) {
                System.out.print("Welcome to the TransLink bus route planner! ");
                first = false;
            }

            System.out.println("Please press 1 to continue or 'quit' to quit.");

            try {
                String input = s.nextLine();

                if (input.equals("1")) {
                    next = true;
                }

                else if(input.equalsIgnoreCase("quit"))
                {
                    quit = true;
                }

                else {
                    System.out.print("Invalid input. ");
                }
            } catch (Exception e) {
            }
        }
    }
}