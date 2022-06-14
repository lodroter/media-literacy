package com.example.educationapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import com.bumptech.glide.Glide;

public class Utils {


    /**
     * Method converting picture from database as array of bytes
     * to bitmap
     * @return bitmap
     */
    public static Bitmap getImage(byte[] image){

        return BitmapFactory.decodeByteArray(image,0,image.length);
    }

}

