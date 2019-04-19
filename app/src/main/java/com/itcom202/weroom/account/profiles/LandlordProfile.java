package com.itcom202.weroom.account.profiles;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

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
            landlord.mTenantMaxAge = sTenatnMaxAge;
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
    private int mTenantMaxAge;
    private String mTenantGender;
    private String mTenantOccupation;
    private String mAllowTenantSmoking;
    private String mSocialTenant;
    private List<String> mPictures = new ArrayList<>();
    private List<String> mRoomsID = new ArrayList<>();

    public boolean addPicture(String s){
        List<String> pictures = getPictures();
        if (pictures.size() == 10)
            return false;
        pictures.add(s);
        return true;
    }
    public boolean addRoomID(String id){
        if (mRoomsID.size()<3){
            mRoomsID.add(id);
            return true;
        }
        return false;
    }
    public boolean removeRoomID(String id){
        if (mRoomsID.contains(id)){
            mRoomsID.remove(id);
            return true;
        }
        return false;
    }
    public void removePicture(String s){
        getPictures().remove(s);
    }

    //public constructor needed to de-serialize the object when using firebase database.
    public LandlordProfile(){}

    public List<String> getRoomsID() {
        return mRoomsID;
    }

    public void setRoomsID(List<String> roomsID) {
        mRoomsID = roomsID;
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

    public int getTenantMaxAge() {
        return mTenantMaxAge;
    }

    public void setTenantMaxAge(int tenantMaxAge) {
        mTenantMaxAge = tenantMaxAge;
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

    public List<String> getPictures() {
        return mPictures;
    }

    public void setPictures(List<String> pictures) {
        mPictures = pictures;
    }
}
