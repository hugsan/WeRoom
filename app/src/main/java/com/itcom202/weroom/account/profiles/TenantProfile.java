package com.itcom202.weroom.account.profiles;

import java.util.InputMismatchException;

public class TenantProfile {
    public static final int LESS_THAN_THREE_MONTH = 0;
    public static final int THREE_TO_SIX_MONTH = 1;
    public static final int SIX_TO_TWELVE_MONTH = 2;
    public static final int OVER_TWELVE_MONTH = 3;
    public static final char FEMALE = 'F';
    public static final char MALE = 'M';
    public static final String YES = "Yes";
    public static final String NO = "No";
    public static final String I_DONT_CARE = "Do not Care";

    public static class Builder{
        private String sUserUid;
        private String sNationallity;
        private int sDistanceCenter;
        private int sPeriodOfRent;
        private int sMinDeposit;
        private int sMaxDeposit;
        private int sMinRent;
        private int sMaxRent;
        private int sMinLandlordAge;
        private int sMaxLandlordAge;
        private String sLandlordGender;
        private String sChossenCityName;
        private String sChoosenCityID;
        private double sLatitudeCity;
        private double sLongitudeCity;
        private boolean sFurnished;
        private boolean sHandicap;
        private boolean sInternet;
        private boolean sLaundry;
        private String sPetFriendly;
        private String sSmokeFriendly;

        public Builder (String userUid){
            this.sUserUid = userUid;
        }
        public Builder withLandlordNationallity(String nation){
            sNationallity = nation;

            return this;
        }
        public Builder distanceFromCenter(int distance){
            if (distance < 0 )
                throw new InputMismatchException("Wrong distance");

            sDistanceCenter = distance;
            return this;
        }
        public Builder withRentingPeriod(int period){
            if (period != LESS_THAN_THREE_MONTH && period != THREE_TO_SIX_MONTH &&
            period != SIX_TO_TWELVE_MONTH && period != OVER_TWELVE_MONTH)
                throw new InputMismatchException("Wrong period time");
            sPeriodOfRent = period;
            return this;
        }
        public Builder withDepositRange(int min, int max){
            if (min > max)
                throw new InputMismatchException("wrong deposit input");
            sMinDeposit = min;
            sMaxDeposit = max;
            return this;
        }
        public Builder withRentRange(int min, int max){
            if (min > max)
                throw new InputMismatchException("Wrong rent input");
            sMinRent = min;
            sMaxRent = max;
            return this;
        }
        public Builder withLandlordAgeRange(int min, int max){
            if (min > max || min <= 15){
                throw new InputMismatchException("Wrong landlord age range");
            }
            sMinLandlordAge = min;
            sMaxLandlordAge = max;
            return this;
        }
        public Builder withLandlordGender(char gender){
            sLandlordGender = String.valueOf(gender);
            return this;
        }
        public Builder withCity(String id, String name, double latitude, double longitude){
            sChoosenCityID = id;
            sChossenCityName = name;
            sLongitudeCity = longitude;
            sLatitudeCity = latitude;
            return this;
        }
        public Builder isFurnished(boolean f){
            sFurnished = f;
            return this;
        }
        public Builder isHandicapFriendly(boolean h){
            sHandicap = h;
            return this;
        }
        public Builder hasInternet(boolean i){
            sInternet = i;
            return this;
        }
        public Builder hasLaundry(boolean l){
            sLaundry = l;
            return this;
        }
        public Builder isPetFriendly(String p){
            if (!p.equals(YES) && !p.equals(NO) && !p.equals(I_DONT_CARE))
                throw new InputMismatchException("Wrong answer");
            sPetFriendly = p;
            return this;
        }
        public Builder isSmokingFriendly(String s){
            if (!s.equals(YES) && !s.equals(NO)&& !s.equals(I_DONT_CARE) )
                throw new InputMismatchException("Wrong answer");
            sSmokeFriendly = s;
            return this;
        }
        public TenantProfile build(){
            TenantProfile t = new TenantProfile();
            t.mUserUid = this.sUserUid;
            t.mNationallity = this.sNationallity;
            t.mDistanceCenter = this.sDistanceCenter;
            t.mPeriodOfRent = this.sPeriodOfRent;
            t.mMinDeposit = this.sMinDeposit;
            t.mMAxDeposit = this.sMaxDeposit;
            t.mMinRent = this.sMinRent;
            t.mMaxRent = this.sMaxRent;
            t.mMinLandlordAge = this.sMinLandlordAge;
            t.mMaxLandlordAge = this.sMaxLandlordAge;
            t.mLandlordGender = this.sLandlordGender;
            t.mFurnished = this.sFurnished;
            t.mHandicap = this.sHandicap;
            t.mInternet = this.sInternet;
            t.mLaundry = this.sLaundry;
            t.mPetFriendly = this.sPetFriendly;
            t.mSmokeFriendly = this.sSmokeFriendly;
            t.mChoosenCityname = this.sChossenCityName;
            t.mChoosenCityId = this.sChoosenCityID;
            t.mCityLatitude = this.sLatitudeCity;
            t.mCityLongitude = this.sLongitudeCity;
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
    private String mLandlordGender;
    private String mChoosenCityname;
    private String mChoosenCityId;
    private double mCityLatitude;
    private double mCityLongitude;
    private Boolean mFurnished;
    private Boolean mHandicap;
    private Boolean mInternet;
    private Boolean mLaundry;
    private String mPetFriendly;
    private String mSmokeFriendly;





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

    public String getLandlordGender() {
        return mLandlordGender;
    }

    public void setLandlordGender(String landlordGender) {
        mLandlordGender = landlordGender;
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

    public String getPetFriendly() {
        return mPetFriendly;
    }

    public void setPetFriendly(String petFriendly) {
        mPetFriendly = petFriendly;
    }

    public String getSmokeFriendly() {
        return mSmokeFriendly;
    }

    public void setSmokeFriendly(String smokeFriendly) {
        mSmokeFriendly = smokeFriendly;
    }
    public String getChoosenCityname() {
        return mChoosenCityname;
    }

    public void setChoosenCityname(String choosenCityname) {
        mChoosenCityname = choosenCityname;
    }

    public String getChoosenCityId() {
        return mChoosenCityId;
    }

    public void setChoosenCityId(String choosenCityId) {
        mChoosenCityId = choosenCityId;
    }

    public double getCityLatitude() {
        return mCityLatitude;
    }

    public void setCityLatitude(double cityLatitude) {
        mCityLatitude = cityLatitude;
    }

    public double getCityLongitude() {
        return mCityLongitude;
    }

    public void setCityLongitude(double cityLongitude) {
        mCityLongitude = cityLongitude;
    }
}
