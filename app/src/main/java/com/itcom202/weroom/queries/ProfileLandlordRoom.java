package com.itcom202.weroom.queries;

import android.graphics.Bitmap;

import com.itcom202.weroom.account.profiles.LandlordProfile;
import com.itcom202.weroom.account.profiles.Profile;
import com.itcom202.weroom.account.profiles.RoomPosted;

import java.util.ArrayList;

public class ProfileLandlordRoom {
    public Profile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(Profile userProfile) {
        this.userProfile = userProfile;
    }

    public LandlordProfile getLandlordProfile() {
        return landlordProfile;
    }

    public void setLandlordProfile(LandlordProfile landlordProfile) {
        this.landlordProfile = landlordProfile;
    }

    public RoomPosted getRoom() {
        return room;
    }

    public void setRoom(RoomPosted room) {
        this.room = room;
    }

    public Bitmap getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(Bitmap userPicture) {
        this.userPicture = userPicture;
    }

    public ArrayList<Bitmap> getRoomPictures() {
        return roomPictures;
    }

    public void setRoomPictures(ArrayList<Bitmap> roomPictures) {
        this.roomPictures = roomPictures;
    }

    private Profile userProfile;
    private Bitmap userPicture;
    private LandlordProfile landlordProfile;
    private RoomPosted room;
    private ArrayList<Bitmap> roomPictures;

    public ProfileLandlordRoom(Profile userProfile, LandlordProfile landlordProfile,
                               RoomPosted room, Bitmap userPicture, ArrayList<Bitmap> roomPictures){
        this.userProfile = userProfile;
        this.landlordProfile = landlordProfile;
        this.room = room;
        this.userPicture = userPicture;
        this.roomPictures = roomPictures;
    }

}
