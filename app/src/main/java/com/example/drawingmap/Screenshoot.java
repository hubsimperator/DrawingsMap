package com.example.drawingmap;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_WORLD_READABLE;
import static com.example.drawingmap.PaintView.DEFAULT_BG_COLOR;

public class Screenshoot extends AppCompatActivity {


    public static String sciezka="nie dziala";

    Context con1=null;
    Context context;
    String Id = "", Exception = "";
    String output="";
    GoogleMap mMap=null;



    public void TakeScreenshoot(GoogleMap googleMap, final Context con) {
        con1 = con;
        mMap = googleMap;

        final GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap bitmap) {
                Bitmap b = bitmap;
                String timeStamp = new SimpleDateFormat(
                        "yyyyMMdd_HHmmss", Locale.getDefault())
                        .format(new java.util.Date());
               String filepath = "test" + ".jpg";

                try {
                    OutputStream fout = null;
                    fout = con1.openFileOutput(filepath, MODE_WORLD_READABLE);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
                    fout.flush();
                    fout.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                File file = con1.getFileStreamPath(filepath);
                Log.d("TAG", "**********************");
                Log.d("TAG", "Sciezka pliku z asynca");
                Log.d("TAG", file.getAbsolutePath());

                Log.d("TAG", "**********************");
                sciezka = file.getAbsolutePath();
                //pathScreenshoot=file.getAbsolutePath();

                if (!filepath.equals("")) {
                    Log.d("TAG", "**********************");
                    Log.d("TAG", "Chce zapisac");

                    Log.d("TAG", "**********************");
                    final ContentValues values = new ContentValues(2);
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                    values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
                    final Uri contentUriFile = con1.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                } else {
                    System.out.println("ERROR");
                }


                Log.d("TAG", "**********************");
                Log.d("TAG", "Migawka gotowa");
                Log.d("TAG", "**********************");
        final File imgFile = new  File(file.getAbsolutePath());


                AlertDialog.Builder dialogBuilder1 = new AlertDialog.Builder(con1,android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
                dialogBuilder1.setNegativeButton("Powrót",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        })
                        .setNeutralButton("Usun", null)
                        .setPositiveButton("Wyślij", null);

                LayoutInflater inflater1 = (LayoutInflater) con1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView1 = inflater1.inflate(R.layout.paint_layout, null);
              ;


                dialogBuilder1.setView(dialogView1);


                if(imgFile.exists()){

                    Log.d("TAG", "**********************");
                    Log.d("TAG", "Istnieje");
                    Log.d("TAG", "**********************");

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getPath());

                    //ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);
                    ImageView img=(ImageView) dialogView1.findViewById(R.id.imageViewid);

                    img.setImageBitmap(myBitmap);

                }
                else{
                    Log.d("TAG", "**********************");
                    Log.d("TAG", "Brak pliku");
                    Log.d("TAG", "**********************");
                }

                AlertDialog alertDialog = dialogBuilder1.create();
                alertDialog.show();
    //Button wyslij
                Button positiveButton=alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        LatLng loc = mMap.getCameraPosition().target;
                        final String lat=Double.toString(loc.latitude);
                        final String lon=Double.toString(loc.longitude);


                        //  mMap.getCameraPosition();
                        final AlertDialog alertDialog=new AlertDialog.Builder(con1)
                                .setTitle("Opcje")
                                .setMessage("Czy chcesz dodać adnotację ?")
                                .setPositiveButton("Tak,dodaj opis",null)
                               .setNegativeButton("Nie,wyślij tylko zdjęcie",null)
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .show();

                       Button DodajOpisButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                       DodajOpisButton.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               alertDialog.dismiss();
                               Toast.makeText(con1,"Tu bedzie dodany opis",Toast.LENGTH_LONG).show();
                           }
                       });

                       Button Wyslij=alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                       Wyslij.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               alertDialog.dismiss();

                               String sciezka =imgFile.getPath();
                                CodePhotoBase64 cpb=new CodePhotoBase64();
                               cpb.encode2(con1,sciezka,"Test","screen","shoota",lat,lon);
                           }
                       });

                      //  String s=imgFile.getPath();
                       // CodePhotoBase64 cpb=new CodePhotoBase64();
                        //cpb.encode2(con1,s,"Test","screen","shoota");

                        //Log.d("sciezka dostep",s);
                    }
                });


    //Button wyczysc
                Button neutralButton=alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                neutralButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AttributeSet a = null;
                        SimpleDrawingView sdv=new SimpleDrawingView(con,a);
                        sdv.clear();


                    }
                });







                //************************************
            }
        };
        mMap.snapshot(callback);
    }




    public void saveImage(String filePath)
    {
        File file = con1.getFileStreamPath(filePath);
        Log.d("TAG", "**********************");
        Log.d("TAG", "Sciezka pliku z asynca");
        Log.d("TAG",         file.getAbsolutePath());

        Log.d("TAG", "**********************");
        //pathScreenshoot=file.getAbsolutePath();

        if(!filePath.equals(""))
        {
            final ContentValues values = new ContentValues(2);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            final Uri contentUriFile = con1.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }
        else
        {
            System.out.println("ERROR");
        }
        //openScreenshot(file);
sciezka="brawo";
    }

    private void takeScreenshot2(){
        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap bitmap) {
                Bitmap b = bitmap;
                String timeStamp = new SimpleDateFormat(
                        "yyyyMMdd_HHmmss", Locale.getDefault())
                        .format(new java.util.Date());
                String filepath = "test"+ ".jpg";

                try{
                    OutputStream fout = null;
                    fout = con1.openFileOutput(filepath,MODE_WORLD_READABLE);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
                    fout.flush();
                    fout.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                saveImage(filepath);
            }
        };
        mMap.snapshot(callback);
    }




}
