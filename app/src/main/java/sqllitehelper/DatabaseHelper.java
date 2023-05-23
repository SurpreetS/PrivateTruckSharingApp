/*
            Name        :  Surpreet Singh
            Student ID  :  218663803
            Unit No.    :  SIT305

 */

package sqllitehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE =
                "CREATE TABLE " + Util.TABLE_NAME + "(" +
                        Util.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        Util.COLUMN_FULL_NAME + " TEXT, " +
                        Util.COLUMN_PHONE_NUMBER + " TEXT, " +
                        Util.COLUMN_DESCRIPTION + " TEXT, " +
                        Util.COLUMN_DATE + " TEXT, " +
                        Util.COLUMN_LOCATION + " TEXT, " +
                        Util.COLUMN_LOCATION_LAT + " REAL, " +
                        Util.COLUMN_LOCATION_LNG + " REAL" +
                        ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing table and recreate it if the database version is updated
        db.execSQL("DROP TABLE IF EXISTS " + Util.TABLE_NAME);
        onCreate(db);
    }

    public long insertData(UserData userData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.COLUMN_FULL_NAME, userData.getFullName());
        contentValues.put(Util.COLUMN_PHONE_NUMBER, userData.getPhoneNumber());
        contentValues.put(Util.COLUMN_DESCRIPTION, userData.getDescription());
        contentValues.put(Util.COLUMN_DATE, userData.getDate());
        contentValues.put(Util.COLUMN_LOCATION, userData.getLocation());
        contentValues.put(Util.COLUMN_LOCATION_LAT, userData.getLocationLatLng().latitude);
        contentValues.put(Util.COLUMN_LOCATION_LNG, userData.getLocationLatLng().longitude);

        long rowID = db.insert(Util.TABLE_NAME, null, contentValues);

        return rowID;
    }

    public List<UserData> getAllLostFoundItems() {
        List<UserData> dataArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                Util.COLUMN_ID,
                Util.COLUMN_FULL_NAME,
                Util.COLUMN_PHONE_NUMBER,
                Util.COLUMN_DESCRIPTION,
                Util.COLUMN_DATE,
                Util.COLUMN_LOCATION,
                Util.COLUMN_LOCATION_LAT,
                Util.COLUMN_LOCATION_LNG
        };

        String sortOrder = Util.COLUMN_ID + " ASC";
        Cursor cursor = db.query(Util.TABLE_NAME, columns, null, null, null, null, sortOrder);

        while (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex(Util.COLUMN_ID);
            int fullNameIndex = cursor.getColumnIndex(Util.COLUMN_FULL_NAME);
            int phoneNumberIndex = cursor.getColumnIndex(Util.COLUMN_PHONE_NUMBER);
            int descriptionIndex = cursor.getColumnIndex(Util.COLUMN_DESCRIPTION);
            int dateIndex = cursor.getColumnIndex(Util.COLUMN_DATE);
            int locationIndex = cursor.getColumnIndex(Util.COLUMN_LOCATION);
            int locationLatIndex = cursor.getColumnIndex(Util.COLUMN_LOCATION_LAT);
            int locationLngIndex = cursor.getColumnIndex(Util.COLUMN_LOCATION_LNG);

            if (idIndex != -1 && fullNameIndex != -1 && phoneNumberIndex != -1 && descriptionIndex != -1
                    && dateIndex != -1 && locationIndex != -1 && locationLatIndex != -1 && locationLngIndex != -1) {
                int id = cursor.getInt(idIndex);
                String fullName = cursor.getString(fullNameIndex);
                String phoneNumber = cursor.getString(phoneNumberIndex);
                String description = cursor.getString(descriptionIndex);
                String date = cursor.getString(dateIndex);
                String location = cursor.getString(locationIndex);
                double locationLat = cursor.getDouble(locationLatIndex);
                double locationLng = cursor.getDouble(locationLngIndex);

                LatLng locationLatLng = new LatLng(locationLat, locationLng);
                UserData orderData = new UserData(fullName, phoneNumber, description, date, location, locationLatLng);
                dataArrayList.add(orderData);
            }
        }

        cursor.close();
        db.close();

        return dataArrayList;
    }

    public boolean deleteUserData(String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = Util.COLUMN_DESCRIPTION + " = ?";
        String[] whereArgs = {String.valueOf(desc)};
        int rowsDeleted = db.delete(Util.TABLE_NAME, whereClause, whereArgs);

        db.close();
        return rowsDeleted > 0;
    }
}

