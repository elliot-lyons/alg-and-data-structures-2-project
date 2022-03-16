public class TransitLink                    // the main class where the program is run
{
    public static void main(String[] args)
    {
        Welcome welcome = new Welcome();
        MainMenu mainMenu = new MainMenu();

        while (!welcome.isNext())
        {
            welcome.display();
        }

        while (!mainMenu.isQuit())
        {
            mainMenu.display();
        }
    }
}