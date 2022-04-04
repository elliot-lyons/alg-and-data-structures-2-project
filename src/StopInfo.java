package src;

import java.util.Scanner;

public class StopInfo
{
    private boolean back, valid;
    private Scanner s;
    private TST<String> tst;

    StopInfo(Scanner s, TST<String> tst)
    {
        this.s = s;
        this.tst = tst;
        back = false;
        valid = true;
    }

    public void display()
    {
        while (!back)
        {
            if (!valid)
            {
                System.out.println("Stop name not found...");
            }

            System.out.println("Please enter a stop name you wish to find!");

        }
    }
}