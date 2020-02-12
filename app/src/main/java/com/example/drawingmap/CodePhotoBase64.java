package com.example.drawingmap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.example.drawingmap.JSON.JSONrequest_insert_photo;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class CodePhotoBase64 {
    public static Context con1;
    Bitmap selectedImage;
    @SuppressLint("StaticFieldLeak")
    public void encode2(Context con, String fileName, final String parametr, final String podparametr, final String mslink, final String latitude, final String longitude) {
        con1=con;
        String filePath = fileName;//"/storage/emulated/0/POST_IMAGE.jpg";
        selectedImage = BitmapFactory.decodeFile(filePath);

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                int width=selectedImage.getWidth();
                int height=selectedImage.getHeight();

                Bitmap resizedBitmap;
                if(width>height) {
                     resizedBitmap = Bitmap.createScaledBitmap(
                            selectedImage, 800, 600, false);
                }else{
                     resizedBitmap = Bitmap.createScaledBitmap(
                            selectedImage, 600, 800, false);
                }


             //   Bitmap resizedBitmap1= BitmapFactory.decodeResource(con1.getResources(), R.drawable.studzienka);
              //  resizedBitmap = Bitmap.createScaledBitmap(resizedBitmap1, 16, 16, false);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                String strBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                BufferedWriter writer = null;
                try {
                    writer = new BufferedWriter(new FileWriter("/storage/emulated/0/22.txt"));
                    writer.write(strBase64);
                    writer.close();
                    Log.d("Zapis","Zapisano pomyslnie");
                }
                 catch (IOException e) {
                     Log.d("Zapis","Blad zapisu");
                }
                return strBase64;
            }
            @Override
            protected void onPostExecute(String s) {
                Log.d("POST",Integer.toString(s.length()));
                JSONrequest_insert_photo json_photos=new JSONrequest_insert_photo();
                json_photos.Send(con1,s,parametr,podparametr,mslink,latitude,longitude);
            }
        }.execute();

    }

}
