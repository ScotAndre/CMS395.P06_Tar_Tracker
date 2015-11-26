package edu.rollins.cms395.tartracker;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
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

        String sqlCreateDrink = "create table " + DRINK_TABLE + "("
                + TYPE + " text, "
                + TIMESTAMP + " integer, "
                + LOCATION_ID + " integer, "
                + "FOREIGN KEY (" + LOCATION_ID +") REFERENCES "
                + LOCATION_TABLE + "(" + LOCATION_ID + ")";

        try {
            db.execSQL(sqlCreateProfile);
            db.execSQL(sqlCreateLocation);
            db.execSQL(sqlCreateDrink);
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
}
