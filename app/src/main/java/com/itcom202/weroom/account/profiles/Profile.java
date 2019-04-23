package com.itcom202.weroom.account.profiles;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.itcom202.weroom.cameraGallery.PictureConversion;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.util.List;

public class Profile implements Serializable {

    private String name;
    private int age;
    private String gender;
    private String country;
    private String role;
    private List<String> tags;
    private String userID;
    private TenantProfile tenant;
    private LandlordProfile landlord;
    private Match match;





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

}
