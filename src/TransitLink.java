import java.util.Scanner;

public class TransitLink                    // the main class where the program is run
{
    public static final Scanner s = new Scanner(System.in);


    public static void main(String[] args)
    {
        Welcome welcome = new Welcome(s);
        MainMenu mainMenu = new MainMenu(s);

        while (!welcome.isNext())
        {
            welcome.display();
        }

        while (!mainMenu.isQuit())
        {
            mainMenu.display();
        }

        System.out.println("Thank you for using the TransLink bus planner!");
    }
}