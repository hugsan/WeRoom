package com.itcom202.weroom.account.profiles;

import java.util.InputMismatchException;

public class TenantProfile {
    public static final int LESS_THAN_THREE_MONTH = 1;
    public static final int THREE_TO_SIX_MONTH = 2;
    public static final int SIX_TO_TWELVE_MONTH = 3;
    public static final int OVER_TWELVE_MONTH = 4;
    public static final char FEMALE = 'F';
    public static final char MALE = 'M';
    public static final byte YES = 1;
    public static final byte NO = 2;
    public static final byte I_DONT_CARE = 3;

    public static class Builder{
        private String mUserUid;
        private String mNationallity;
        private int mDistanceCenter;
        private int mPeriodOfRent;
        private int mMinDeposit;
        private int mMAxDeposit;
        private int mMinRent;
        private int mMaxRent;
        private int mMinLandlordAge;
        private int mMaxLandlordAge;
        private char mLandlordGender;
        private String mCity; //CHECK this, we might change this regading to the google API
        private boolean mFurnished;
        private boolean mHandicap;
        private boolean mInternet;
        private boolean mLaundry;
        private byte mPetFriendly;
        private byte mSmokeFriendly;

        public Builder (String userUid){
            this.mUserUid = userUid;
        }
        public Builder withNationallity(String nation){
            mNationallity = nation;
            return this;
        }
        public Builder distanceFromCenter(int distance){
            if (distance < 0 )
                throw new InputMismatchException("Wrong distance");

            mDistanceCenter = distance;
            return this;
        }
        public Builder withRentingPeriod(int period){
            if (period != LESS_THAN_THREE_MONTH && period != THREE_TO_SIX_MONTH &&
            period != SIX_TO_TWELVE_MONTH && period != OVER_TWELVE_MONTH)
                throw new InputMismatchException("Wrong period time");
            mPeriodOfRent = period;
            return this;
        }
        public Builder withDepositRange(int min, int max){
            if (min > max)
                throw new InputMismatchException("wrong deposit input");
            mMinDeposit = min;
            mMAxDeposit = max;
            return this;
        }
        public Builder withRentRange(int min, int max){
            if (min > max)
                throw new InputMismatchException("Wrong rent input");
            mMinRent = min;
            mMaxRent = max;
            return this;
        }
        public Builder withLandlordAgeRange(int min, int max){
            if (min > max || min <= 15)
                throw new InputMismatchException("Wrong landlor age range");
            mMinLandlordAge = min;
            mMaxLandlordAge = max;
            return this;
        }
        public Builder withLandlordGender(char gender){
            mLandlordGender = gender;
            return this;
        }
        public Builder withCity(String city){
            mCity = city;
            return this;
        }
        public Builder isFurnished(boolean f){
            mFurnished = f;
            return this;
        }
        public Builder isHandicapFriendly(boolean h){
            mHandicap = h;
            return this;
        }
        public Builder hasInternet(boolean i){
            mInternet = i;
            return this;
        }
        public Builder hasLaundry(boolean l){
            mLaundry = l;
            return this;
        }
        public Builder isPetFriendly(byte p){
            if (p != YES && p != NO && p != I_DONT_CARE)
                throw new InputMismatchException("Wrong answer");
            mPetFriendly = p;
            return this;
        }
        public Builder isSmokingFriendly(byte s){
            if (s != YES && s != NO && s != I_DONT_CARE)
                throw new InputMismatchException("Wrong answer");
            mSmokeFriendly = s;
            return this;
        }
        public TenantProfile build(){
            TenantProfile t = new TenantProfile();
            t.mUserUid = this.mUserUid;
            t.mNationallity = this.mNationallity;
            t.mDistanceCenter = this.mDistanceCenter;
            t.mPeriodOfRent = this.mPeriodOfRent;
            t.mMinDeposit = this.mMinDeposit;
            t.mMAxDeposit = this.mMAxDeposit;
            t.mMinRent = this.mMinRent;
            t.mMaxRent = this.mMaxRent;
            t.mMinLandlordAge = this.mMinLandlordAge;
            t.mMaxLandlordAge = this.mMaxLandlordAge;
            t.mLandlordGender = this.mLandlordGender;
            t.mCity = this.mCity;
            t.mFurnished = this.mFurnished;
            t.mHandicap = this.mHandicap;
            t.mInternet = this.mInternet;
            t.mLaundry = this.mLaundry;
            t.mPetFriendly = this.mPetFriendly;
            t.mSmokeFriendly = this.mSmokeFriendly;
            return t;
        }
    }
    private String mUserUid;
    private String mNationallity;
    private int mDistanceCenter;
    private int mPeriodOfRent;
    private int mMinDeposit;
    private int mMAxDeposit;
    private int mMinRent;
    private int mMaxRent;
    private int mMinLandlordAge;
    private int mMaxLandlordAge;
    private char mLandlordGender;
    private String mCity; //CHECK this, we might change this regading to the google API
    private Boolean mFurnished;
    private Boolean mHandicap;
    private Boolean mInternet;
    private Boolean mLaundry;
    private byte mPetFriendly;
    private byte mSmokeFriendly;

    private TenantProfile(){}

    public String getUserUid() {
        return mUserUid;
    }

    public void setUserUid(String userUid) {
        mUserUid = userUid;
    }

    public String getNationallity() {
        return mNationallity;
    }

    public void setNationallity(String nationallity) {
        mNationallity = nationallity;
    }

    public int getDistanceCenter() {
        return mDistanceCenter;
    }

    public void setDistanceCenter(int distanceCenter) {
        mDistanceCenter = distanceCenter;
    }

    public int getPeriodOfRent() {
        return mPeriodOfRent;
    }

    public void setPeriodOfRent(int periodOfRent) {
        mPeriodOfRent = periodOfRent;
    }

    public int getMinDeposit() {
        return mMinDeposit;
    }

    public void setMinDeposit(int minDeposit) {
        mMinDeposit = minDeposit;
    }

    public int getMAxDeposit() {
        return mMAxDeposit;
    }

    public void setMAxDeposit(int MAxDeposit) {
        mMAxDeposit = MAxDeposit;
    }

    public int getMinRent() {
        return mMinRent;
    }

    public void setMinRent(int minRent) {
        mMinRent = minRent;
    }

    public int getMaxRent() {
        return mMaxRent;
    }

    public void setMaxRent(int maxRent) {
        mMaxRent = maxRent;
    }

    public int getMinLandlordAge() {
        return mMinLandlordAge;
    }

    public void setMinLandlordAge(int minLandlordAge) {
        mMinLandlordAge = minLandlordAge;
    }

    public int getMaxLandlordAge() {
        return mMaxLandlordAge;
    }

    public void setMaxLandlordAge(int maxLandlordAge) {
        mMaxLandlordAge = maxLandlordAge;
    }

    public char getLandlordGender() {
        return mLandlordGender;
    }

    public void setLandlordGender(char landlordGender) {
        mLandlordGender = landlordGender;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public Boolean getFurnished() {
        return mFurnished;
    }

    public void setFurnished(Boolean furnished) {
        mFurnished = furnished;
    }

    public Boolean getHandicap() {
        return mHandicap;
    }

    public void setHandicap(Boolean handicap) {
        mHandicap = handicap;
    }

    public Boolean getInternet() {
        return mInternet;
    }

    public void setInternet(Boolean internet) {
        mInternet = internet;
    }

    public Boolean getLaundry() {
        return mLaundry;
    }

    public void setLaundry(Boolean laundry) {
        mLaundry = laundry;
    }

    public byte getPetFriendly() {
        return mPetFriendly;
    }

    public void setPetFriendly(byte petFriendly) {
        mPetFriendly = petFriendly;
    }

    public byte getSmokeFriendly() {
        return mSmokeFriendly;
    }

    public void setSmokeFriendly(byte smokeFriendly) {
        mSmokeFriendly = smokeFriendly;
    }
}
