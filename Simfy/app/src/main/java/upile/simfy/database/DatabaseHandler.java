package upile.simfy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import upile.simfy.models.UserDetails;

/**
 * Created by upile.milanzi on 7/19/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "simfyDb";

    // Contacts table name
    private static final String TABLE_USER = "userDetails";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT )";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    // Adding new user
    public void addUserDetails(UserDetails userDetails) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, userDetails.getName()); // User name

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection
    }

    // Getting  user
    public UserDetails getUserDetails(String dbfield, String fieldValue) {
        SQLiteDatabase sqldb = this.getWritableDatabase();
        String Query = "Select * from " + TABLE_USER + " where " + dbfield + " = " + fieldValue;
        Cursor cursor = sqldb.rawQuery(Query, null);
        if (cursor != null)
            cursor.moveToFirst();

        UserDetails userDetails = new UserDetails();
        userDetails.setId(cursor.getString(0));
        userDetails.setName(cursor.getString(1));
        cursor.close();
        // return userDetails
        return userDetails;
    }

    // Getting All Contacts
    public List<UserDetails> getAllUserDetails() {
        List<UserDetails> userDetailsArrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                UserDetails userDetails = new UserDetails();
                userDetails.setId(cursor.getString(0));
                userDetails.setName(cursor.getString(1));
                // Adding userDetails to list
                userDetailsArrayList.add(userDetails);
            } while (cursor.moveToNext());
        }
            cursor.close();
        // return userDetails list
        return userDetailsArrayList;
    }

    // Deleting single UserDetails
    public void deleteUser(UserDetails userDetails) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, KEY_ID + " = ?",
                new String[] { String.valueOf(userDetails.getId()) });
        db.close();
    }

    public boolean recordExits(String dbfield, String fieldValue) {
        SQLiteDatabase sqldb = this.getWritableDatabase();
        String Query = "Select * from " + TABLE_USER + " where " + dbfield + " = " + fieldValue;
        Cursor cursor = sqldb.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}
