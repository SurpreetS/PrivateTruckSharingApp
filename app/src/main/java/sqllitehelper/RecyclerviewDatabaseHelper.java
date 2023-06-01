package sqllitehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.week9.MyDataModel;

import java.util.ArrayList;
import java.util.List;

public class RecyclerviewDatabaseHelper extends SQLiteOpenHelper {

    public RecyclerviewDatabaseHelper(@Nullable Context context) {
        super(context, RecyclerviewUtil.DATABASE_NAME, null, RecyclerviewUtil.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE =
                "CREATE TABLE " + RecyclerviewUtil.TABLE_NAME + "(" +
                        RecyclerviewUtil.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RecyclerviewUtil.COLUMN_DESCRIPTION+ " TEXT, " +
                        RecyclerviewUtil.COLUMN_TRUCK_NAME + " TEXT, " +
                        RecyclerviewUtil.COLUMN_IMAGE + " TEXT" +
                        ")";
        db.execSQL(CREATE_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop the existing table and recreate it if the database version is updated
        db.execSQL("DROP TABLE IF EXISTS " + RecyclerviewUtil.TABLE_NAME);
        onCreate(db);
    }

    public long insertTruck(MyDataModel truck){
        SQLiteDatabase db = this.getWritableDatabase();
        if (truckExists(db, truck)) {
            return -1; // Truck already exists, return -1 indicating failure
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(RecyclerviewUtil.COLUMN_DESCRIPTION, truck.getDescription());
        contentValues.put(RecyclerviewUtil.COLUMN_TRUCK_NAME, truck.getTruckName());
        contentValues.put(RecyclerviewUtil.COLUMN_IMAGE, truck.getImage());

        long rowID = db.insert(RecyclerviewUtil.TABLE_NAME,null ,contentValues);

        return rowID;
    }

    private boolean truckExists(SQLiteDatabase db, MyDataModel truck) {
        String query = "SELECT * FROM " + RecyclerviewUtil.TABLE_NAME + " WHERE " + RecyclerviewUtil.COLUMN_TRUCK_NAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{truck.getTruckName()});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }


    public List<MyDataModel> getDataFromDatabase() {
        List<MyDataModel> dataList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                RecyclerviewUtil.COLUMN_TRUCK_NAME,
                RecyclerviewUtil.COLUMN_DESCRIPTION,
                RecyclerviewUtil.COLUMN_IMAGE
        };
        Cursor cursor = db.query(
                RecyclerviewUtil.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String truckName = cursor.getString(cursor.getColumnIndexOrThrow(RecyclerviewUtil.COLUMN_TRUCK_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(RecyclerviewUtil.COLUMN_DESCRIPTION));
                int image = cursor.getInt(cursor.getColumnIndexOrThrow(RecyclerviewUtil.COLUMN_IMAGE));
                MyDataModel data = new MyDataModel(truckName, description, image);
                dataList.add(data);
            }
            cursor.close();
        }

        return dataList;
    }
}
