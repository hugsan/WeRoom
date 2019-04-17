package com.itcom202.weroom.account.profiles;

import java.io.Serializable;
import java.util.InputMismatchException;
import java.util.List;

public class RoomPosted implements Serializable {

    public static class Builder{
        private int sRent;
        private int sDeposit;
        private String sPeriodOfRenting;
        private String sCompleteAddress;
        private String sAddressID;
        private double sLatitude;
        private double sLongitude;
        private int sSize;
        private boolean sFurnished;
        private boolean sInternet;
        private boolean sCommonAreas;
        private boolean sLaundry;
        private int sRoomNumber;
        private String sDescription;
        private List<String> sPictures;

        public Builder(int roomNumber){
            sRoomNumber = roomNumber;
        }
        public Builder withRent(int rent){
            if(rent<0)
                throw new InputMismatchException("Wrong rent");
            sRent = rent;
            return this;
        }
        public Builder withPictures(List<String> pictures){
//            if (pictures.size() < 3)
//                throw new IllegalArgumentException("Not enought pictures, at least 3 pictures are requiered");
            this.sPictures = pictures;
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
        public Builder withPeriodRenting(String period){
            if(period.equals("Select period of renting"))
                throw new InputMismatchException("Wrong period");
            sPeriodOfRenting = period;
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
            r.mRoomNumber = sRoomNumber;
            r.mDescription = sDescription;
            r.mPictures = sPictures;
            return r;
        }




    }

    private int mRent;
    private int mDeposit;
    private String mPeriodOfRenting;
    private String mCompleteAddress;
    private String mAddressID;
    private double mLatitude;
    private double mLongitude;
    private int mSize;
    private boolean mFurnished;
    private boolean mInternet;
    private boolean mComonAreas;
    private boolean mLaundry;
    private int mRoomNumber;
    private String mDescription;

    private List<String> mPictures;

    //Constructor is public needed to de-serialize the object using firebase database

    public RoomPosted(){}

    public List<String> getPictures() {
        return mPictures;
    }

    public void setPictures(List<String> pictures) {
        mPictures = pictures;
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

    public String getPeriodOfRenting() {
        return mPeriodOfRenting;
    }

    public void setPeriodOfRenting(String periodOfRenting) {
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

    public int getRoomNumber() {
        return mRoomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        mRoomNumber = roomNumber;
    }

}
