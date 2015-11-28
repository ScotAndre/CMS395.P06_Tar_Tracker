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
 *  The BAC decreases by 0.00125 for every 5 minutes that pass without drinking
 */
public class BacCalculator {
    private static final double MALE_DIST_RATE = 0.73;
    private static final double FEMALE_DIST_RATE = 0.66;
    private static final double BAC_DISSIPATION_RATE = 0.00125;
    private int mDrinkCount = 0;

    public void addDrink(){
        mDrinkCount++;
        // TODO: add drink and timestamp to database
    }

    public void resetDrinkCounter(){
        mDrinkCount = 0;
        // do we want to remove the drinks from the database?
    }

    public void getDrinkCount(){
        // Get this out of the database
    }


}
