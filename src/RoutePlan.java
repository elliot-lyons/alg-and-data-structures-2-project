package src;

import java.util.ArrayList;
import java.util.Scanner;

public class RoutePlan
{
    private boolean back;
    private Scanner s;
    private double[][] distances;
    private ArrayList<Stop> theStops;

    RoutePlan(Scanner s, ArrayList<Stop> theStops)
    {
        this.s = s;
        this.theStops = theStops;
        back = false;
    }

    public void display()
    {
        System.out.println(theStops.size());
    }
}