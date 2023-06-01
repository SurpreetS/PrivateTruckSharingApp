package sqllitehelper;

import android.content.ContentValues;

public class DatabaseUtility {

    public static ContentValues deliveryToContentValues(MyOrderData delivery) {
        ContentValues values = new ContentValues();
        values.put("pickup_time", delivery.getPickupTime());
        values.put("pickup_date", delivery.getPickupDate());
        values.put("receiver_name", delivery.getReceiverName());
        values.put("pickup_location", delivery.getPickupLocation());
        values.put("vehicle_type", delivery.getVehicleType());
        values.put("good_type", delivery.getGoodType());
        values.put("weight", delivery.getWeight());
        values.put("length", delivery.getLength());
        values.put("height", delivery.getHeight());
        values.put("width", delivery.getWidth());
        return values;
    }
}
