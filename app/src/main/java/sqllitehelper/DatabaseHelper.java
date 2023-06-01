package sqllitehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

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
                        Util.COLUMN_USERNAME + " TEXT, " +
                        Util.COLUMN_PASSWORD + " TEXT, " +
                        Util.COLUMN_PHONE_NUMBER + " TEXT" +
                        ")";
        db.execSQL(CREATE_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop the existing table and recreate it if the database version is updated
        db.execSQL("DROP TABLE IF EXISTS " + Util.TABLE_NAME);
        onCreate(db);
    }

    public long insertUser(UserData userData){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.COLUMN_FULL_NAME, userData.getFullName());
        contentValues.put(Util.COLUMN_USERNAME, userData.getUsername());
        contentValues.put(Util.COLUMN_PASSWORD, userData.getPassword());
        contentValues.put(Util.COLUMN_PHONE_NUMBER, userData.getPhoneNumber());

        long rowID = db.insert(Util.TABLE_NAME,null ,contentValues);

        return rowID;
    }

    public boolean getUser(String userName,String password){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.TABLE_NAME, new String[]{Util.COLUMN_ID},
                Util.COLUMN_USERNAME + " =? and " +
                        Util.COLUMN_PASSWORD + " =?",
                new String[]{ userName, password}, null,null
                ,null);

        int noOfRows = cursor.getCount();
        if(noOfRows >0){
            return true;
        }
        else {
            return  false;
        }


    }
}
