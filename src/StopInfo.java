package src;

import java.util.ArrayList;
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

            System.out.println("Please enter a stop name you wish to find or 'back' to return:");
            String input = s.nextLine();

            if (input.equals(null))
            {
                valid = false;
            }

            else
            {
                if (input.equals("back"))
                {
                    return;
                }

                else
                {
                    Iterable<String> x = tst.keysWithPrefix(input);
                    if (!x.equals(null)) {
                        String out = "";
                        String y = x.toString();
                        String[] keys = y.split(input, -1);
                        ArrayList<String> info = new ArrayList<String>();

                        for (int i = 0; i < keys.length; i++)
                        {
                            keys[i] = input + keys[i];
                            out += "Stop name: " + keys[i] +"x"+ tst.get(keys[i]) + "\n";
                        }

                        System.out.println(out);
                    }

                    else
                    {
                        valid = false;
                    }
                }
            }
        }
    }
}