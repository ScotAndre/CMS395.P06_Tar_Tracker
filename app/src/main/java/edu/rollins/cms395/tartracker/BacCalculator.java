package edu.rollins.cms395.tartracker;

/*
 *  BAC = (A * 5.14 / W * r) - 0.015 * H
 *
 *  Where:
 *      A is total alcohol consumed, in ounces
 *      W is the person's body weight in pounds
 *      r is the alcohol distribution ratio:
 *          - 0.73 for men
 *          - 0.66 for women
 *      H is the number of hours since the last drink
 *
 *  The BAC decreases by 0.00125 every 5 minutes
 */
public class BacCalculator {
    private double ounces = 0.0;
    private int weight = 0;
    private static final double MALE_RATIO = 0.73;
    private static final double FEMALE_RATIO = 0.66;
    private double ratio = 0.0;
    private int minutesSinceLastDrink = 0;
    private boolean isMale = true;
    private double bac = 0.0;

    public BacCalculator(){

    }

    public double getBAC(){
        return bac;
    }
}
