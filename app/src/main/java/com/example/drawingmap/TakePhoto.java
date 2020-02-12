package com.example.drawingmap;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUriExposedException;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;
import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.app.ActivityCompat.startActivityForResult;
import static androidx.core.content.ContextCompat.startActivity;



@RequiresApi(api = Build.VERSION_CODES.N)
public class TakePhoto {

    private static final int CAMERA_PHOTO = 111;
    private static Uri imageToUploadUri;
    public static String path=null;

    private void captureCameraImage() {
        Intent chooserIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(Environment.getExternalStorageDirectory(), "POST_IMAGE.jpg");
        path=f.toString();
        chooserIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        imageToUploadUri = Uri.fromFile(f);
        if (con1 instanceof Activity) {
            try {
                ((Activity) con1).startActivityForResult(chooserIntent, CAMERA_PHOTO);
            }
            catch (FileUriExposedException fileUriExposedException){
                Log.d("Alarm","Nie mozna wlaczyc aparatu");
            }
        } else {

        }
    }
public static Context con1;
public static String parametr;
public static String podparametr;
public static String mslink;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    public void TakePhoto(Context con,String param,String popdaram, String _mslink){
        con1=con;
        parametr=param;
        podparametr=popdaram;
        mslink=_mslink;
        znajdzKamere();
/*        Intent fotkejszyn = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (con instanceof Activity) {
            ((Activity) con).startActivityForResult(fotkejszyn, 1);
        } else {

        }
 */

captureCameraImage();
    }

    private static Bitmap getBitmap(String path) {

        Uri uri = Uri.fromFile(new File(path));
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
            in = con1.getContentResolver().openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();


            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }
            Log.d("", "scale = " + scale + ", orig-width: " + o.outWidth + ", orig-height: " + o.outHeight);

            Bitmap b = null;
            in = con1.getContentResolver().openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();
                Log.d("", "1th scale operation dimenions - width: " + width + ", height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();

            Log.d("", "bitmap size - width: " + b.getWidth() + ", height: " +
                    b.getHeight());
            return b;
        } catch ( IOException e) {
            Log.e("", e.getMessage(), e);
            return null;
        }
    }

    private int znajdzKamere(){
        int cameraID= -1;
        for(int i = 0; i< Camera.getNumberOfCameras(); i++){
            Camera.CameraInfo info=new Camera.CameraInfo();
            Camera.getCameraInfo(i,info);

            if(info.facing== Camera.CameraInfo.CAMERA_FACING_BACK){
                Log.d("TAG","*********************");
                Log.d("TAG","Jest kamera tylna");
                Log.d("TAG","*********************");
                cameraID=i;
                break;
            }


        }
        return cameraID;
    }

    public static void activityResult(int requestCode, int resultCode, Intent data){

        if (requestCode == CAMERA_PHOTO && resultCode == Activity.RESULT_OK) {
            if(imageToUploadUri != null){
                Uri selectedImage = imageToUploadUri;
                con1.getContentResolver().notifyChange(selectedImage, null);
                Bitmap reducedSizeBitmap = getBitmap(imageToUploadUri.getPath());
                if(reducedSizeBitmap != null){
                    AlertDialog.Builder dialogBuilder1 = new AlertDialog.Builder(con1,android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
                    dialogBuilder1.setNegativeButton("Powrót",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            })
                            .setNeutralButton("Rysuj", null)
                            .setPositiveButton("Wyślij", null);



                    LayoutInflater inflater1 = (LayoutInflater) con1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View dialogView1 = inflater1.inflate(R.layout.paint_layout, null);
                    dialogBuilder1.setView(dialogView1);

                    final ImageView img=(ImageView) dialogView1.findViewById(R.id.imageViewid);
                  //  img.setImageBitmap(bitmap);
                    img.setImageBitmap(reducedSizeBitmap);
                    final AlertDialog alertDialog = dialogBuilder1.create();
                    alertDialog.show();

                    //wyslij button
                    Button positiveButton=alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positiveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CodePhotoBase64 cpb=new CodePhotoBase64();
                            cpb.encode2(con1,path,parametr,podparametr,mslink,null,null);
                            alertDialog.dismiss();
                        }
                    });


                }else{
                    Toast.makeText(con1,"Error while capturing Image",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(con1,"Error while capturing Image",Toast.LENGTH_LONG).show();
            }
        }



        /*
        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {
                Bundle extras=data.getExtras();


                final Bitmap bitmap =(Bitmap) extras.get("data");
                //imageView.setImageBitmap(bitmap);


                AlertDialog.Builder dialogBuilder1 = new AlertDialog.Builder(con1,android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
                dialogBuilder1.setNegativeButton("Powrót",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        })
                        .setNeutralButton("Rysuj", null)
                        .setPositiveButton("Wyślij", null);



                LayoutInflater inflater1 = (LayoutInflater) con1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView1 = inflater1.inflate(R.layout.paint_layout, null);
                dialogBuilder1.setView(dialogView1);

                final ImageView img=(ImageView) dialogView1.findViewById(R.id.imageViewid);
                img.setImageBitmap(bitmap);

                final AlertDialog alertDialog = dialogBuilder1.create();
                alertDialog.show();

                Button neutralButton=alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                neutralButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Log.d("TAG","*********************");
                        Log.d("TAG","jestem w srodku");
                        Log.d("TAG","*********************");
                        new AlertDialog.Builder(con1)
                                .setMessage("Czy chcesz dokonać adnotacji ?")
                                .setPositiveButton("Tak",null)
                                .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                    }
                                })
                                .setNeutralButton("Nie,kontynuuj wysyłanie", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                    }
                                })
                                .show();
                    }
                });

                Button positiveButton=alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }
        */
    }

public void test(String base64){
    byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
   // image.setImageBitmap(decodedByte);
    AlertDialog.Builder dialogBuilder1 = new AlertDialog.Builder(con1,android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);

    LayoutInflater inflater1 = (LayoutInflater) con1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View dialogView1 = inflater1.inflate(R.layout.paint_layout, null);
    dialogBuilder1.setView(dialogView1);

    final ImageView img=(ImageView) dialogView1.findViewById(R.id.imageViewid);
     img.setImageBitmap(decodedByte);

}

}
