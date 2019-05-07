package com.itcom202.weroom.account.profiles;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.InputMismatchException;
import java.util.UUID;

public class RoomPosted implements Serializable, Parcelable{
    static final long serialVersionUID = 42L;

    protected RoomPosted(Parcel in) {
        mRent = in.readInt();
        mDeposit = in.readInt();
        mPeriodOfRenting = in.readInt();
        mCompleteAddress = in.readString();
        mAddressID = in.readString();
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
        mSize = in.readInt();
        mFurnished = in.readByte() != 0;
        mInternet = in.readByte() != 0;
        mComonAreas = in.readByte() != 0;
        mLaundry = in.readByte() != 0;
        mRoomID = in.readString();
        mDescription = in.readString();
        mLandlordID = in.readString();
        mMatch = in.readParcelable(Match.class.getClassLoader());
    }

    public static final Creator<RoomPosted> CREATOR = new Creator<RoomPosted>() {
        @Override
        public RoomPosted createFromParcel(Parcel in) {
            return new RoomPosted(in);
        }

        @Override
        public RoomPosted[] newArray(int size) {
            return new RoomPosted[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mRent);
        dest.writeInt(mDeposit);
        dest.writeInt(mPeriodOfRenting);
        dest.writeString(mCompleteAddress);
        dest.writeString(mAddressID);
        dest.writeDouble(mLatitude);
        dest.writeDouble(mLongitude);
        dest.writeInt(mSize);
        dest.writeByte((byte) (mFurnished ? 1 : 0));
        dest.writeByte((byte) (mInternet ? 1 : 0));
        dest.writeByte((byte) (mComonAreas ? 1 : 0));
        dest.writeByte((byte) (mLaundry ? 1 : 0));
        dest.writeString(mRoomID);
        dest.writeString(mDescription);
        dest.writeString(mLandlordID);
        dest.writeParcelable(mMatch, flags);
    }


    public static class Builder{
        private int sRent;
        private int sDeposit;
        private int sPeriodOfRenting;
        private String sCompleteAddress;
        private String sAddressID;
        private double sLatitude;
        private double sLongitude;
        private int sSize;
        private boolean sFurnished;
        private boolean sInternet;
        private boolean sCommonAreas;
        private boolean sLaundry;
        private String sRoomID;
        private String sDescription;
        private String sLandlordID;

        public Builder(String landlordID){
            sRoomID = UUID.randomUUID().toString();
            sLandlordID = landlordID;
        }
        public Builder withRent(int rent){
            if(rent<0)
                throw new InputMismatchException("Wrong rent");
            sRent = rent;
            return this;
        }

        public Builder withDescription(String s){
            if(s.equals(""))
                throw new InputMismatchException("Wrong description");
            sDescription = s;
            return this;
        }
        public Builder withDeposit(int deposit){
            if (deposit < 0)
                throw new InputMismatchException("Wrong deposit");
            sDeposit = deposit;
            return this;
        }
        public Builder withPeriodRenting(int position){
            if(position < 0 || position > 4)
                throw new InputMismatchException("Wrong period");
            sPeriodOfRenting = position;
            return this;
        }
        public Builder withAddress(String id, String address, double latitude, double longitude ){
//            if(address.equals(""))
    //            throw new InputMismatchException("wrong address");
            sAddressID = id;
            sCompleteAddress = address;
            sLatitude = latitude;
            sLongitude = longitude;
            return this;
        }
        public Builder withSize(int size){
            if (size < 0 )
                throw new InputMismatchException("Wrong size DUDE!");
            sSize = size;
            return this;
        }
        public Builder isFurnished(boolean furnish ){
            sFurnished = furnish;
            return this;
        }
        public Builder hasInternet(boolean internet){
            sInternet = internet;
            return this;
        }
        public Builder hasCommonAreas(boolean commonAreas){
            sCommonAreas = commonAreas;
            return this;
        }
        public Builder hasLaundry(boolean laundry){
            sLaundry = laundry;
            return this;
        }
        public RoomPosted build(){
            RoomPosted r = new RoomPosted();
            r.mRent = sRent;
            r.mDeposit = sDeposit;
            r.mPeriodOfRenting = sPeriodOfRenting;
            r.mCompleteAddress = sCompleteAddress;
            r.mAddressID = sAddressID;
            r.mLatitude = sLatitude;
            r.mLongitude = sLongitude;
            r.mSize = sSize;
            r.mFurnished = sFurnished;
            r.mInternet = sInternet;
            r.mComonAreas = sCommonAreas;
            r.mLaundry = sLaundry;
            r.mRoomID = sRoomID;
            r.mDescription = sDescription;
            r.mLandlordID = sLandlordID;
            return r;
        }

    }

    private int mRent;
    private int mDeposit;
    private int mPeriodOfRenting;
    private String mCompleteAddress;
    private String mAddressID;
    private double mLatitude;
    private double mLongitude;
    private int mSize;
    private boolean mFurnished;
    private boolean mInternet;
    private boolean mComonAreas;
    private boolean mLaundry;
    private String mRoomID;
    private String mDescription;
    private String mLandlordID;
    private Match mMatch = new Match();

    //Constructor is public needed to de-serialize the object using firebase database

    public RoomPosted(){}

    public Match getMatch() {
        return mMatch;
    }

    public void setMatch(Match match) {
        mMatch = match;
    }

    public String getLandlordID() {
        return mLandlordID;
    }

    public void setLandlordID(String landlordID) {
        mLandlordID = landlordID;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public int getRent() {
        return mRent;
    }

    public void setRent(int rent) {
        mRent = rent;
    }

    public int getDeposit() {
        return mDeposit;
    }

    public void setDeposit(int deposit) {
        mDeposit = deposit;
    }

    public int getPeriodOfRenting() {
        return mPeriodOfRenting;
    }

    public void setPeriodOfRenting(int periodOfRenting) {
        mPeriodOfRenting = periodOfRenting;
    }

    public String getCompleteAddress() {
        return mCompleteAddress;
    }

    public void setCompleteAddress(String completeAddress) {
        mCompleteAddress = completeAddress;
    }

    public String getAddressID() {
        return mAddressID;
    }

    public void setAddressID(String addressID) {
        mAddressID = addressID;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public int getSize() {
        return mSize;
    }

    public void setSize(int size) {
        mSize = size;
    }

    public boolean isFurnished() {
        return mFurnished;
    }

    public void setFurnished(boolean furnished) {
        mFurnished = furnished;
    }

    public boolean isInternet() {
        return mInternet;
    }

    public void setInternet(boolean internet) {
        mInternet = internet;
    }

    public boolean isComonAreas() {
        return mComonAreas;
    }

    public void setComonAreas(boolean comonAreas) {
        mComonAreas = comonAreas;
    }

    public boolean isLaundry() {
        return mLaundry;
    }

    public void setLaundry(boolean laundry) {
        mLaundry = laundry;
    }

    public String getRoomID() {
        return mRoomID;
    }

    public void setRoomID(String roomID) {
        mRoomID = roomID;
    }

    @Override
    public boolean equals( Object obj) {
        if (!(obj instanceof RoomPosted))
            return false;
        return this.getRoomID().equals(((RoomPosted) obj).mRoomID);
    }
}
