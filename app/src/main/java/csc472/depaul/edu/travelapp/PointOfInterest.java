package csc472.depaul.edu.travelapp;


import android.os.Parcel;
import android.os.Parcelable;


public final class PointOfInterest implements Parcelable{

    public PointOfInterest(){

    }



    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public PointOfInterest createFromParcel(Parcel in) {
            return new PointOfInterest(in);
        }

        public PointOfInterest[] newArray(int size) {
            return new PointOfInterest[size];
        }
    };

    public PointOfInterest(Parcel in){
        formattedAddress = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        name = in.readString();
        photoRef = in.readString();
        photoUrl = in.readString();
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(formattedAddress);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(name);
        dest.writeString(photoRef);
        dest.writeString(photoUrl);
    }


    String formattedAddress;
    String latitude;
    String longitude;
    String name;
    String photoRef;
    String photoUrl;

    String getFormattedAddress() { return formattedAddress; }
    void setFormattedAddress(String formattedAddress) { this.formattedAddress = formattedAddress; }

    double getLatitude() { return Double.parseDouble(latitude); }
    void setLatitude(String latitude) { this.latitude = latitude; }

    double getLongitude() { return Double.parseDouble(longitude); }
    void setLongitude(String longitude) { this.longitude = longitude; }

    String getName() { return name; }
    void setName(String name) { this.name = name; }

    String getPhotoRef() { return photoRef; }
    void setPhotoRef(String photoRef) { this.photoRef = photoRef; }

    String getPhotoUrl() { return photoUrl; }
    void getPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
}
