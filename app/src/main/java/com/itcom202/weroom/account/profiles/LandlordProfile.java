package com.itcom202.weroom.account.profiles;

import java.util.InputMismatchException;

public class LandlordProfile {
    public static final String YES = "Yes";
    public static final String NO = "No";
    public static final String I_DONT_CARE = "Do not Care";

    public static class Builder{
        private String sUserID;
        private String sTenantNation;
        private int sTenantMinAge;
        private int sTenatnMaxAge;
        private String sTenantGender;
        private String sTenantOccupation;
        private String sAllowTenantSmoking;
        private String sSocialTenant;



        public Builder(String id){sUserID = id;}
        public Builder withTenantNationallity(String nation){
            sTenantNation = nation;
            return this;
        }
        public Builder withTenantAge(int min, int max){
            if (min > max || min <= 15){
                throw new InputMismatchException("Wrong tenant age range");
            }
            sTenantMinAge = min;
            sTenatnMaxAge = max;
            return this;
        }
        public Builder withTenantGender(String gender){
            sTenantGender = gender;
            return this;
        }
        public Builder withTenantOccupation(String occupation){
            sTenantOccupation = occupation;
            return this;
        }
        public Builder canTenantSmoke(String smoke){
            if (!smoke.equals(YES) && !smoke.equals(NO) && !smoke.equals(I_DONT_CARE))
                throw new InputMismatchException("Wrong Smoke string");
            sAllowTenantSmoking = smoke;
            return this;
        }
        public Builder tenantSocial(String social){
            if (!social.equals(YES) && !social.equals(NO) && !social.equals(I_DONT_CARE))
                throw new InputMismatchException("Wrong tenant social string");
            sSocialTenant = social;
            return this;
        }
        public LandlordProfile build(){
            LandlordProfile landlord = new LandlordProfile();
            landlord.mUserID = sUserID;
            landlord.mTenantNation = sTenantNation;
            landlord.mTenantMinAge = sTenantMinAge;
            landlord.mTenatnMaxAge = sTenatnMaxAge;
            landlord.mTenantGender = sTenantGender;
            landlord.mTenantOccupation = sTenantOccupation;
            landlord.mAllowTenantSmoking = sAllowTenantSmoking;
            landlord.mSocialTenant = sSocialTenant;
            return landlord;
        }

    }



    private String mUserID;
    private String mTenantNation;
    private int mTenantMinAge;
    private int mTenatnMaxAge;
    private String mTenantGender;
    private String mTenantOccupation;
    private String mAllowTenantSmoking;
    private String mSocialTenant;
    private RoomPosted mRoomOne;
    private RoomPosted mRoomTwo;
    private RoomPosted mRoomThree;

    //public constructor needed to de-serialize the object when using firebase database.
    public LandlordProfile(){}

    public RoomPosted getRoomOne() {
        return mRoomOne;
    }

    public void setRoomOne(RoomPosted roomOne) {
        mRoomOne = roomOne;
    }

    public RoomPosted getRoomTwo() {
        return mRoomTwo;
    }

    public void setRoomTwo(RoomPosted roomTwo) {
        mRoomTwo = roomTwo;
    }

    public RoomPosted getRoomThree() {
        return mRoomThree;
    }

    public void setRoomThree(RoomPosted roomThree) {
        mRoomThree = roomThree;
    }

    public String getUserID() {
        return mUserID;
    }

    public void setUserID(String userID) {
        mUserID = userID;
    }

    public String getTenantNation() {
        return mTenantNation;
    }

    public void setTenantNation(String tenantNation) {
        mTenantNation = tenantNation;
    }

    public int getTenantMinAge() {
        return mTenantMinAge;
    }

    public void setTenantMinAge(int tenantMinAge) {
        mTenantMinAge = tenantMinAge;
    }

    public int getTenatnMaxAge() {
        return mTenatnMaxAge;
    }

    public void setTenatnMaxAge(int tenatnMaxAge) {
        mTenatnMaxAge = tenatnMaxAge;
    }

    public String getTenantGender() {
        return mTenantGender;
    }

    public void setTenantGender(String tenantGender) {
        mTenantGender = tenantGender;
    }

    public String getTenantOccupation() {
        return mTenantOccupation;
    }

    public void setTenantOccupation(String tenantOccupation) {
        mTenantOccupation = tenantOccupation;
    }

    public String getAllowTenantSmoking() {
        return mAllowTenantSmoking;
    }

    public void setAllowTenantSmoking(String allowTenantSmoking) {
        mAllowTenantSmoking = allowTenantSmoking;
    }

    public String getSocialTenant() {
        return mSocialTenant;
    }

    public void setSocialTenant(String socialTenant) {
        mSocialTenant = socialTenant;
    }
}
