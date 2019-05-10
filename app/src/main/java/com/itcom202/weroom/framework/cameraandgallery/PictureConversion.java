package com.itcom202.weroom.framework.cameraandgallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class PictureConversion {
    public static byte[] bitmapToByteArray(Bitmap bmp){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bmp.recycle();
        return byteArray;
    }

    public static Bitmap byteArrayToBitmap(byte[] bytearray){
        return  BitmapFactory.decodeByteArray(bytearray, 0, bytearray.length);
    }
}
