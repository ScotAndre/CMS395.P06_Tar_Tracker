package edu.rollins.cms395.tartracker;

import android.content.Context;

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
    private Context mContext;
    private DatabaseManager db;

    private int mDrinkCount = 0;

    public BacCalculator(Context context){
        mContext = context;
        db = new DatabaseManager(mContext);
    }

    public int getmDrinkCount(){
        return db.countDrinks();
    }

    public void addDrink(){
        long timeStamp = System.currentTimeMillis();
        db.insertDrink(timeStamp);
    }

    public void resetDrinkCounter(){
        db.clearDrinks();
    }
}
