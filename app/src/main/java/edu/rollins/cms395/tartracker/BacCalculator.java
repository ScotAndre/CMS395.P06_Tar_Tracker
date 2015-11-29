package edu.rollins.cms395.tartracker;

import android.content.Context;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

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

    private DatabaseManager db;
    private int mDrinkCount = 0;
    private Context mContext;
    private boolean isMale = true;

    // Default Values:
    public static final double BAC_PER_SE_LIMIT_DEFAULT = 0.08;
    public static final double BAC_UNDERAGE_LIMIT_DEFAULT = 0.02;
    public static final double BAC_ENHANCED_LIMIT_DEFAULT = 0.15;
    public static final int LEGAL_AGE_DEFAULT = 21;
    public static final int AVERAGE_MALE_WEIGHT = 196;
    public static final int AVERAGE_FEMALE_WEIGHT = 166;

    public BacCalculator(Context context){
        mContext = context;
        db = new DatabaseManager(mContext);
    }

    public int getDrinkCount(){
        return db.countDrinks();
    }

    public void addDrink(){
        long timeStamp = System.currentTimeMillis();
        db.insertDrink(timeStamp);
    }

    public void resetDrinkCounter(){
        db.clearDrinks();
    }

    public double getBac(){
        // 5 minutes is 300,000 milliseconds
        // 50 minutes is 3,000,000 milliseconds
        // 60 minutes is 3,600,000 milliseconds
        final int HOUR = 3600000;
        // 150 minutes is 6,600,000 milliseconds
        long timeStamp = System.currentTimeMillis();
        double bac = 0.00;
        int weight = 0;
        double distRate = 0.0;
        ArrayList<Long> drinks = new ArrayList<Long>();
        drinks = db.getDrinks();

        //TODO: Will need to get rid of this when preferences are implemented
        if(isMale){
            weight = AVERAGE_MALE_WEIGHT;
            distRate = MALE_DIST_RATE;
        } else {
            weight = AVERAGE_FEMALE_WEIGHT;
            distRate = FEMALE_DIST_RATE;
        }

        // BAC = (A * 5.14 / W * r) - 0.015 * H
        for(int i = 0; i < drinks.size(); i++){
            long drink = drinks.get(i);
            if(timeStamp - drink <= 6600000) {
                bac += (1.5 * 5.14 / weight * distRate) - 0.015 * ((timeStamp - drink) / HOUR);
            }
        }
        Log.d("BAC Calc ", "" + bac);

        DecimalFormat df = new DecimalFormat("0.000");
        String bacString = df.format(bac);

        return Double.parseDouble(bacString);
    }
}
