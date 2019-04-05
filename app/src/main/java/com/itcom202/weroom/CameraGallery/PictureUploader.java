package com.itcom202.weroom.CameraGallery;

import android.graphics.Bitmap;



public class PictureUploader {
    private Bitmap picture;
    private String picturName;

    public PictureUploader(Bitmap image, String name){
        picture = image;
        picturName = name;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public String getPicturName() {
        return picturName;
    }

    public void setPicturName(String picturName) {
        this.picturName = picturName;
    }

}
