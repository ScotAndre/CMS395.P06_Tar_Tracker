package edu.rollins.cms395.tartracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by kshiver on 11/26/2015.
 */
public class DatabaseManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "TarTracker";
    private static final String PROFILE_TABLE = "tblProfile";
    private static final String DRINK_TABLE = "tblDrink";
    private static final String LOCATION_TABLE = "tblLocation";
    private static final String NAME = "name";
    private static final String WEIGHT = "weight";
    private static final String AGE = "age";
    private static final String GENDER = "gender";
    private static final String LOCATION_ID = "id";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String ADDRESS = "address";
    private static final String TYPE = "type";
    private static final String TIMESTAMP = "timestamp";
    private static final int DATABASE_VERSION = 1;
    private Context mContext;

    public DatabaseManager(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreateProfile = "create table " + PROFILE_TABLE + "("
                + NAME + " text primary key, "
                + AGE + " integer, "
                + WEIGHT + " integer, "
                + GENDER + " text"
                + ")";

        String sqlCreateLocation = "create table " + LOCATION_TABLE + "("
                + LOCATION_ID + " integer primary key autoincrement,"
                + NAME + " text, "
                + LATITUDE + " integer, "
                + LONGITUDE + " integer, "
                + ADDRESS + " text"
                + ")";

        String sqlCreateDrink = "create table "
                + DRINK_TABLE + "("
                + TYPE + " text, "
                + TIMESTAMP + " integer, "
                + LOCATION_ID + " integer, "
                + "FOREIGN KEY (" + LOCATION_ID +") REFERENCES "
                + LOCATION_TABLE + "(" + LOCATION_ID + ")";

        String sqlCreateDrinkTable = "CREATE TABLE "
                + DRINK_TABLE + " (ID integer primary key autoincrement, "
                + TIMESTAMP + " INTEGER);";

        try {
//            db.execSQL(sqlCreateProfile);
//            db.execSQL(sqlCreateLocation);
            db.execSQL(sqlCreateDrinkTable);
        }catch ( SQLException se ) {
            Toast.makeText(mContext, se.getMessage(), Toast.LENGTH_LONG).show( );
        }

    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertProfile(String name, int age, int weight, String gender){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(NAME, name);
            values.put(AGE, age);
            values.put(WEIGHT, weight);
            values.put(GENDER, gender);
            long newId = db.insert(PROFILE_TABLE, null, values);

            if ( newId == - 1 ) {
                Toast.makeText(mContext, R.string.insert_error, Toast.LENGTH_LONG ).show( );
            } else {
                Toast.makeText(mContext, R.string.insert_ok, Toast.LENGTH_LONG ).show( );
            }

            db.close();
        } catch ( SQLiteException se ) {
            Toast.makeText(mContext, se.getMessage( ), Toast.LENGTH_LONG ).show( );
        }
    }

    public void insertLocation(String name, int latitude, int longitude, String address){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(NAME, name);
            values.put(LATITUDE, latitude);
            values.put(LONGITUDE, longitude);
            values.put(ADDRESS, address);
            long newId = db.insert(LOCATION_TABLE, null, values);

            if ( newId == - 1 ) {
                Toast.makeText(mContext, R.string.insert_error, Toast.LENGTH_LONG ).show( );
            } else {
                Toast.makeText(mContext, R.string.insert_ok, Toast.LENGTH_LONG ).show( );
            }
            db.close();
        } catch ( SQLiteException se ) {
            Toast.makeText(mContext, se.getMessage( ), Toast.LENGTH_LONG ).show( );
        }
    }

    public void insertDrink(long time){

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
//            values.put(TYPE, type);
            values.put(TIMESTAMP, time);
//            values.put(LOCATION_ID, locationID);
            long newId = db.insert(DRINK_TABLE, null, values);

            if ( newId == - 1 ) {
                Toast.makeText(mContext, R.string.insert_error, Toast.LENGTH_LONG ).show( );
            } else {
                Toast.makeText(mContext, R.string.insert_ok, Toast.LENGTH_LONG ).show( );
            }
            db.close();
        } catch ( SQLiteException se ) {
            Toast.makeText(mContext, se.getMessage( ), Toast.LENGTH_LONG ).show( );
        }
    }

    public void clearProfile(){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            String deleteQuery = "DELETE FROM " + PROFILE_TABLE;
            db.rawQuery(deleteQuery, null);
            db.close();
        }
        catch (SQLException se) {
            Toast.makeText(mContext, se.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public int countDrinks (){

        int numDrinks = 0;

        try{
            SQLiteDatabase db = this.getReadableDatabase();
            String countQuery = "SELECT * FROM " + DRINK_TABLE;
            Cursor cursor = db.rawQuery(countQuery, null);
            numDrinks = cursor.getCount();
            cursor.close();
            db.close();
        }
        catch (SQLException se) {
            Toast.makeText(mContext, se.getMessage(), Toast.LENGTH_LONG).show();
        }

        return numDrinks;
    }

    public void clearDrinks (){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            String deleteQuery = "DELETE FROM " + DRINK_TABLE;
            db.execSQL(deleteQuery);
            db.close();
        }
        catch (SQLException se) {
            Toast.makeText(mContext, se.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
