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
                System.out.println("Stop name not found.");
                System.out.println("***YOU MUST ONLY CAPITALISE ALL FIRST LETTERS***: eg: 'Maple Drive', " +
                        "not 'maple drive'");
            }

            System.out.println("Please enter a stop name, or the start of a stop name," +
                    " you wish to find or 'back' to return to main menu:");
            String input = s.nextLine();

            if (input.equals(null))
            {
                valid = false;
            }

            else
            {
                if (input.equalsIgnoreCase("back"))
                {
                    return;
                }

                else
                {
                    Iterable<String> x = tst.keysWithPrefix(input);
                    if (!x.equals(null)) {
                        String out = "";
                        String y = x.toString();        // string with all stops beginning with input
                        String[] keys = y.split(input, -1);     // separating the stops
                        int count = 0;

                        for (int i = 0; i < keys.length; i++)
                        {
                            keys[i] = input + keys[i];          // have to put input back into the stop name
                            String current = keys[i];

                            while (current.charAt(current.length() - 1) ==  ' ')    // taking any whitespace out of end
                            {                                                       // of string
                                current = current.substring(0, current.length() - 1);
                            }

                            keys[i] = current;

                            if (tst.contains(keys[i]) && !out.contains(keys[i])) {
                                out += "Stop name: " + keys[i] + ". " + tst.get(keys[i]) + "\n";
                                count++;
                            }
                        }

                        if (count > 0)
                        {
                            System.out.println("There " + (count > 1 ? "are " + count + " stops " :
                            "is 1 stop ") + "containing the current query." );
                        }

                        if (out.equals(""))
                        {
                            valid = false;
                        }

                        else {
                            valid = true;
                            System.out.println(out);
                        }
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