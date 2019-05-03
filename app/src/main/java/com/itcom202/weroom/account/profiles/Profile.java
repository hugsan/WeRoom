package com.itcom202.weroom.account.profiles;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.itcom202.weroom.cameraGallery.PictureConversion;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.util.InputMismatchException;
import java.util.List;

public class Profile implements Serializable, Parcelable {

    private String name;
    private int age;
    private String gender;
    private String country;
    private String role;
    private List<String> tags;
    private String userID;
    private TenantProfile tenant;
    private LandlordProfile landlord;
    private Match match = new Match();





    public Profile (String userID, String name, int age, String gender, String country, String role, List<String> tags){
        this.userID = userID;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.country = country;
        this.role = role;
        this.tags = tags;

    }
    //This constructor is needed to de-serialize the object from the FireBase database.
    public Profile(){}

    protected Profile(Parcel in) {
        name = in.readString();
        age = in.readInt();
        gender = in.readString();
        country = in.readString();
        role = in.readString();
        tags = in.createStringArrayList();
        userID = in.readString();
        tenant = in.readParcelable(TenantProfile.class.getClassLoader());
        landlord = in.readParcelable(LandlordProfile.class.getClassLoader());
        match = in.readParcelable(Match.class.getClassLoader());
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };


    public static class Builder{
        private String sName;
        private int sAge;
        private String sGender;
        private String sCountry;
        private String sRole;
        private List<String> sTags;
        private String sUserID;

        public Builder (String userUid){
            this.sUserID = userUid;
        }
        public Builder withName(String name){
            if(name.length()==0){
                throw new InputMismatchException("No name");
            }
            sName = name;
            return this;
        }

        public Builder withAge(int age){
            if(age<18 || age>99)
                throw new InputMismatchException("Wrong age");
            sAge = age;
            return this;
        }

        public Builder withGender(String gender){
            if(gender.equals("Select your gender"))
                throw new InputMismatchException("Gender not selected");
            sGender = gender;
            return this;
        }

        public Builder withCountry(String country){
            if(country.equals("Select a country"))
                throw new InputMismatchException("Country not selected");
            sCountry = country;

            return this;
        }

        public Builder withRole(String role){
            sRole = role;
            return this;
        }

        public Builder withTags(List<String> tags){
            sTags = tags;
            return this;
        }

        public Profile build(){
            Profile profile = new Profile();
            profile.userID = sUserID;
            profile.name = sName;
            profile.age = sAge;
            profile.gender = sGender;
            profile.country = sCountry;
            profile.role = sRole;
            profile.tags = sTags;
            return profile;
        }

    }




    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name= name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public TenantProfile getTenant() {
        return tenant;
    }

    public void setTenant(TenantProfile tenant) {
        this.tenant = tenant;
    }

    public LandlordProfile getLandlord() {
        return landlord;
    }

    public void setLandlord(LandlordProfile landlord) {
        this.landlord = landlord;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    @Override
    public boolean equals(Object o){
        return (o instanceof  Profile) && this.userID.equals( ((Profile)o).userID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
        dest.writeString(gender);
        dest.writeString(country);
        dest.writeString(role);
        dest.writeStringList(tags);
        dest.writeString(userID);
        dest.writeParcelable(tenant, flags);
        dest.writeParcelable(landlord, flags);
        dest.writeParcelable(match, flags);
    }
}
