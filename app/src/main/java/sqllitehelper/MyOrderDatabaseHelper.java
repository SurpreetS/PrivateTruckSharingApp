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

public class MyOrderDatabaseHelper extends SQLiteOpenHelper {

    public MyOrderDatabaseHelper(@Nullable Context context) {
        super(context, MyOrderUtil.DATABASE_NAME, null, MyOrderUtil.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE =
                "CREATE TABLE " + MyOrderUtil.TABLE_NAME + "(" +
                        MyOrderUtil.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MyOrderUtil.COLUMN_PICKUP_TIME + " TEXT, " +
                        MyOrderUtil.COLUMN_PICKUP_DATE + " TEXT, " +
                        MyOrderUtil.COLUMN_RECEIVER_NAME + " TEXT, " +
                        MyOrderUtil.COLUMN_PICKUP_LOCATION + " TEXT, " +
                        MyOrderUtil.COLUMN_PICKUP_LATITUDE + " REAL, " +
                        MyOrderUtil.COLUMN_PICKUP_LONGITUDE + " REAL, " +
                        MyOrderUtil.COLUMN_VEHICLE_TYPE + " TEXT, " +
                        MyOrderUtil.COLUMN_GOOD_TYPE + " TEXT, " +
                        MyOrderUtil.COLUMN_WEIGHT + " REAL, " +
                        MyOrderUtil.COLUMN_LENGTH + " REAL, " +
                        MyOrderUtil.COLUMN_HEIGHT + " REAL, " +
                        MyOrderUtil.COLUMN_WIDTH + " REAL, " +
                        MyOrderUtil.COLUMN_DESCRIPTION + " TEXT, " +
                        MyOrderUtil.COLUMN_TRUCK_NAME + " TEXT, " +
                        MyOrderUtil.COLUMN_IMAGE + " INTEGER, " +
                        MyOrderUtil.COLUMN_DROPOFF_LATITUDE + " REAL, " +
                        MyOrderUtil.COLUMN_DROPOFF_LONGITUDE + " REAL, " +
                        MyOrderUtil.COLUMN_DROPOFF_LOCATION + " TEXT" +
                        ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing table and recreate it if the database version is updated
        db.execSQL("DROP TABLE IF EXISTS " + MyOrderUtil.TABLE_NAME);
        onCreate(db);
    }

    public long insertOrder(MyOrderData order) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (orderExists(db, order)) {
            return -1; // Order already exists, return -1 indicating failure
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyOrderUtil.COLUMN_PICKUP_TIME, order.getPickupTime());
        contentValues.put(MyOrderUtil.COLUMN_PICKUP_DATE, order.getPickupDate());
        contentValues.put(MyOrderUtil.COLUMN_RECEIVER_NAME, order.getReceiverName());
        contentValues.put(MyOrderUtil.COLUMN_PICKUP_LOCATION, order.getPickupLocation());
        contentValues.put(MyOrderUtil.COLUMN_PICKUP_LATITUDE, order.getPickupLatLng().latitude);
        contentValues.put(MyOrderUtil.COLUMN_PICKUP_LONGITUDE, order.getPickupLatLng().longitude);
        contentValues.put(MyOrderUtil.COLUMN_VEHICLE_TYPE, order.getVehicleType());
        contentValues.put(MyOrderUtil.COLUMN_GOOD_TYPE, order.getGoodType());
        contentValues.put(MyOrderUtil.COLUMN_WEIGHT, order.getWeight());
        contentValues.put(MyOrderUtil.COLUMN_LENGTH, order.getLength());
        contentValues.put(MyOrderUtil.COLUMN_HEIGHT, order.getHeight());
        contentValues.put(MyOrderUtil.COLUMN_WIDTH, order.getWidth());
        contentValues.put(MyOrderUtil.COLUMN_DESCRIPTION, order.getDescription());
        contentValues.put(MyOrderUtil.COLUMN_TRUCK_NAME, order.getTruckName());
        contentValues.put(MyOrderUtil.COLUMN_IMAGE, order.getImage());
        contentValues.put(MyOrderUtil.COLUMN_DROPOFF_LATITUDE, order.getDropoffLatLng().latitude);
        contentValues.put(MyOrderUtil.COLUMN_DROPOFF_LONGITUDE, order.getDropoffLatLng().longitude);
        contentValues.put(MyOrderUtil.COLUMN_DROPOFF_LOCATION, order.getDropoffLocation());

        long rowID = db.insert(MyOrderUtil.TABLE_NAME, null, contentValues);

        return rowID;
    }

    private boolean orderExists(SQLiteDatabase db, MyOrderData order) {
        String query = "SELECT * FROM " + MyOrderUtil.TABLE_NAME + " WHERE " + MyOrderUtil.COLUMN_TRUCK_NAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{order.getTruckName()});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public MyOrderData getOrderData(int position) {
        List<MyOrderData> orderList = getAllOrders();
        if (position >= 0 && position < orderList.size()) {
            return orderList.get(position);
        }
        return null;
    }


    public List<MyOrderData> getAllOrders() {
        List<MyOrderData> orderList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                MyOrderUtil.COLUMN_ID,
                MyOrderUtil.COLUMN_PICKUP_TIME,
                MyOrderUtil.COLUMN_PICKUP_DATE,
                MyOrderUtil.COLUMN_RECEIVER_NAME,
                MyOrderUtil.COLUMN_PICKUP_LOCATION,
                MyOrderUtil.COLUMN_PICKUP_LATITUDE,
                MyOrderUtil.COLUMN_PICKUP_LONGITUDE,
                MyOrderUtil.COLUMN_VEHICLE_TYPE,
                MyOrderUtil.COLUMN_GOOD_TYPE,
                MyOrderUtil.COLUMN_WEIGHT,
                MyOrderUtil.COLUMN_LENGTH,
                MyOrderUtil.COLUMN_HEIGHT,
                MyOrderUtil.COLUMN_WIDTH,
                MyOrderUtil.COLUMN_DESCRIPTION,
                MyOrderUtil.COLUMN_TRUCK_NAME,
                MyOrderUtil.COLUMN_IMAGE,
                MyOrderUtil.COLUMN_DROPOFF_LATITUDE,
                MyOrderUtil.COLUMN_DROPOFF_LONGITUDE,
                MyOrderUtil.COLUMN_DROPOFF_LOCATION
        };
        Cursor cursor = db.query(
                MyOrderUtil.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String pickupTime = cursor.getString(cursor.getColumnIndexOrThrow(MyOrderUtil.COLUMN_PICKUP_TIME));
                String pickupDate = cursor.getString(cursor.getColumnIndexOrThrow(MyOrderUtil.COLUMN_PICKUP_DATE));
                String receiverName = cursor.getString(cursor.getColumnIndexOrThrow(MyOrderUtil.COLUMN_RECEIVER_NAME));
                String pickupLocation = cursor.getString(cursor.getColumnIndexOrThrow(MyOrderUtil.COLUMN_PICKUP_LOCATION));
                double pickupLatitude = cursor.getDouble(cursor.getColumnIndexOrThrow(MyOrderUtil.COLUMN_PICKUP_LATITUDE));
                double pickupLongitude = cursor.getDouble(cursor.getColumnIndexOrThrow(MyOrderUtil.COLUMN_PICKUP_LONGITUDE));
                LatLng pickupLatLng = new LatLng(pickupLatitude, pickupLongitude);
                String vehicleType = cursor.getString(cursor.getColumnIndexOrThrow(MyOrderUtil.COLUMN_VEHICLE_TYPE));
                String goodType = cursor.getString(cursor.getColumnIndexOrThrow(MyOrderUtil.COLUMN_GOOD_TYPE));
                double weight = cursor.getDouble(cursor.getColumnIndexOrThrow(MyOrderUtil.COLUMN_WEIGHT));
                double length = cursor.getDouble(cursor.getColumnIndexOrThrow(MyOrderUtil.COLUMN_LENGTH));
                double height = cursor.getDouble(cursor.getColumnIndexOrThrow(MyOrderUtil.COLUMN_HEIGHT));
                double width = cursor.getDouble(cursor.getColumnIndexOrThrow(MyOrderUtil.COLUMN_WIDTH));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(MyOrderUtil.COLUMN_DESCRIPTION));
                String truckName = cursor.getString(cursor.getColumnIndexOrThrow(MyOrderUtil.COLUMN_TRUCK_NAME));
                int image = cursor.getInt(cursor.getColumnIndexOrThrow(MyOrderUtil.COLUMN_IMAGE));
                double dropoffLatitude = cursor.getDouble(cursor.getColumnIndexOrThrow(MyOrderUtil.COLUMN_DROPOFF_LATITUDE));
                double dropoffLongitude = cursor.getDouble(cursor.getColumnIndexOrThrow(MyOrderUtil.COLUMN_DROPOFF_LONGITUDE));
                LatLng dropoffLatLng = new LatLng(dropoffLatitude, dropoffLongitude);
                String dropoffLocation = cursor.getString(cursor.getColumnIndexOrThrow(MyOrderUtil.COLUMN_DROPOFF_LOCATION));

                MyOrderData order = new MyOrderData(pickupTime, pickupDate, receiverName, pickupLocation, pickupLatLng, vehicleType, goodType, weight, length, height, width, description, truckName, image, dropoffLatLng, dropoffLocation);
                orderList.add(order);
            }
            cursor.close();
        }

        return orderList;
    }
}
