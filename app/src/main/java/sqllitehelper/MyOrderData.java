package sqllitehelper;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class MyOrderData implements Parcelable {

    private int id;
    private String pickupTime;
    private String pickupDate;
    private String receiverName;
    private String pickupLocation;
    private LatLng pickupLatLng;
    private String vehicleType;
    private String goodType;
    private double weight;
    private double length;
    private double height;
    private double width;
    private String description;
    private String truckName;
    private int image;
    private LatLng dropoffLatLng;
    private String dropoffLocation;

    public MyOrderData() {
    }

    public MyOrderData(String pickupTime, String pickupDate, String receiverName, String pickupLocation, LatLng pickupLatLng, String vehicleType, String goodType, double weight, double length, double height, double width, String description, String truckName, int image, LatLng dropoffLatLng, String dropoffLocation) {
        this.pickupTime = pickupTime;
        this.pickupDate = pickupDate;
        this.receiverName = receiverName;
        this.pickupLocation = pickupLocation;
        this.pickupLatLng = pickupLatLng;
        this.vehicleType = vehicleType;
        this.goodType = goodType;
        this.weight = weight;
        this.length = length;
        this.height = height;
        this.width = width;
        this.description = description;
        this.truckName = truckName;
        this.image = image;
        this.dropoffLatLng = dropoffLatLng;
        this.dropoffLocation = dropoffLocation;
    }

    protected MyOrderData(Parcel in) {
        id = in.readInt();
        pickupTime = in.readString();
        pickupDate = in.readString();
        receiverName = in.readString();
        pickupLocation = in.readString();
        pickupLatLng = in.readParcelable(LatLng.class.getClassLoader());
        vehicleType = in.readString();
        goodType = in.readString();
        weight = in.readDouble();
        length = in.readDouble();
        height = in.readDouble();
        width = in.readDouble();
        description = in.readString();
        truckName = in.readString();
        image = in.readInt();
        dropoffLatLng = in.readParcelable(LatLng.class.getClassLoader());
        dropoffLocation = in.readString();
    }

    public static final Creator<MyOrderData> CREATOR = new Creator<MyOrderData>() {
        @Override
        public MyOrderData createFromParcel(Parcel in) {
            return new MyOrderData(in);
        }

        @Override
        public MyOrderData[] newArray(int size) {
            return new MyOrderData[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public LatLng getPickupLatLng() {
        return pickupLatLng;
    }

    public void setPickupLatLng(LatLng pickupLatLng) {
        this.pickupLatLng = pickupLatLng;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getGoodType() {
        return goodType;
    }

    public void setGoodType(String goodType) {
        this.goodType = goodType;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTruckName() {
        return truckName;
    }

    public void setTruckName(String truckName) {
        this.truckName = truckName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public LatLng getDropoffLatLng() {
        return dropoffLatLng;
    }

    public void setDropoffLatLng(LatLng dropoffLatLng) {
        this.dropoffLatLng = dropoffLatLng;
    }

    public String getDropoffLocation() {
        return dropoffLocation;
    }

    public void setDropoffLocation(String dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(pickupTime);
        dest.writeString(pickupDate);
        dest.writeString(receiverName);
        dest.writeString(pickupLocation);
        dest.writeParcelable(pickupLatLng, flags);
        dest.writeString(vehicleType);
        dest.writeString(goodType);
        dest.writeDouble(weight);
        dest.writeDouble(length);
        dest.writeDouble(height);
        dest.writeDouble(width);
        dest.writeString(description);
        dest.writeString(truckName);
        dest.writeInt(image);
        dest.writeParcelable(dropoffLatLng, flags);
        dest.writeString(dropoffLocation);
    }
}
