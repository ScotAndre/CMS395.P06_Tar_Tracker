package edu.rollins.cms395.tartracker;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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
    private int mDrinkCount;
    private Context mContext;
    private boolean isMale = true;
    private boolean isUnderage = false;

    // Default Values:
    public static final double BAC_PER_SE_LIMIT_DEFAULT = 0.08;
    public static final double BAC_UNDERAGE_LIMIT_DEFAULT = 0.02;
    public static final double BAC_ENHANCED_LIMIT_DEFAULT = 0.15;
    public static final int LEGAL_AGE_DEFAULT = 21;
    public static final int AVERAGE_MALE_WEIGHT = 196;
    public static final int AVERAGE_FEMALE_WEIGHT = 166;

    // Override defaults?
    private double mPerSeBacLimit;
    private double mUnderageBacLimit;
    private double mEnhancedBacLimit;
    private int mWeight;
    private double mDistRate;
    private int mLegalDrinkingAge;

    public BacCalculator(Context context){
        mContext = context;
        db = new DatabaseManager(mContext);

        // set defaults
        mWeight = AVERAGE_MALE_WEIGHT;
        mDistRate = MALE_DIST_RATE;
        mLegalDrinkingAge = LEGAL_AGE_DEFAULT;
        mDrinkCount = 0;
        mPerSeBacLimit = BAC_PER_SE_LIMIT_DEFAULT;
        mUnderageBacLimit = BAC_UNDERAGE_LIMIT_DEFAULT;
        mEnhancedBacLimit = BAC_ENHANCED_LIMIT_DEFAULT;
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
        checkForPreferences();
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

        // BAC = (A * 5.14 / W * r) - 0.015 * H
        for(int i = 0; i < drinks.size(); i++){
            long drink = drinks.get(i);
            if(timeStamp - drink <= 6600000) {
                bac += (1.5 * 5.14 / mWeight * mDistRate) - 0.015 * ((timeStamp - drink) / HOUR);
            }
        }
        Log.d("BAC Calc ", "" + bac);

        DecimalFormat df = new DecimalFormat("0.000");
        String bacString = df.format(bac);

        return Double.parseDouble(bacString);
    }

    private void checkForPreferences(){
        if(!db.getName().equals("")){
            if(!Integer.toString(db.getAge()).equals("")){
                if(db.getAge() >= mLegalDrinkingAge){
                    isUnderage = false;
                } else {
                    isUnderage = true;
                }
            }
            if(!Integer.toString(db.getWeight()).equals("")){
                setWeight(db.getWeight());
            }

            if(!db.getGender().equals("")){
                if(db.getGender().equals("FEMALE")){
                    isMale = false;
                    mDistRate = MALE_DIST_RATE;
                } else {
                    isMale = true;
                    mDistRate = FEMALE_DIST_RATE;
                }
            }
        }
    }

    public int getSobrietyLevel(){
        int sobrietyLevel = 0;
        int age = db.getAge();
        double bac = getBac();

        // 0 Suprisingly Sober
        // 1 Sober
        // 2 Tipsy
        // 3 Drunk
        // 4 Dangerously Drunk
        // 5 Leathly Drunk
        // 6 error

        if ( getBac() == 0.0 && getDrinkCount() == 0 ){
            return 0;
        } else if ( getBac() >= mUnderageBacLimit && db.getAge() < mLegalDrinkingAge){
            // underage drunk
            return 3;
        } else if (db.getAge() >= mLegalDrinkingAge && getDrinkCount() > 0 && getBac() < mPerSeBacLimit){
            return  2;
        } else if (db.getAge() >= mLegalDrinkingAge && getDrinkCount() > 0 && getBac() >= mPerSeBacLimit){
            return 3;
        } else if(db.getAge() >= mLegalDrinkingAge && getDrinkCount() > 0 && getBac() >= mEnhancedBacLimit){
            return 4;
        } else if(db.getAge() >= mLegalDrinkingAge && getDrinkCount() > 0 && getBac() >= 3.0){
            return 5;
        } else {
            return 6;
        }
    }

    public void setWeight(int weight){
        mWeight = weight;
    }

    public void setPerSeBacLimit(double limit){
        mPerSeBacLimit = limit;
    }

    public void setUnderageBacLimit(double limit){
        mUnderageBacLimit = limit;
    }

    public void setEnhancedBacLimit(double limit){
        mEnhancedBacLimit = limit;
    }

    public void setLegalDrinkingAge(int age){
        mLegalDrinkingAge = age;
    }
}
