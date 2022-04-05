/**
 * Runs the program.
 *
 * @author: Elliot Lyons, 2022
 */

package src;

import java.util.Scanner;

public class TransitLink
{
    public static final Scanner s = new Scanner(System.in);

    public static void main(String[] args)
    {
        Welcome welcome = new Welcome(s);
        MainMenu mainMenu = new MainMenu(s);

        while (!welcome.isNext() && !welcome.isQuit())
        {
            welcome.display();
        }

        if (!welcome.isQuit()) {
            while (!mainMenu.isQuit()) {
                mainMenu.display();
            }
        }

        System.out.println("Thank you for using the TransLink bus planner!");
    }
}