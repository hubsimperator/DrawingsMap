package com.example.drawingmap;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.drawingmap.JSON.JSON_check_version;
import com.example.drawingmap.JSON.JSON_drawLayers;
import com.example.drawingmap.JSON.JSON_wartswy_do_wyswietlenia;
import com.example.drawingmap.JSON.JSONrequest;
import com.example.drawingmap.JSON.JSONrequest_data;
import com.example.drawingmap.JSON.JSONrequest_get_photo;
import com.example.drawingmap.JSON.JSONrequest_layers;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;

public class SplashScreen extends AppCompatActivity{
    public static Context con;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!checkPermissions())
        {
            setPermissions();
        }
        verifyPermissions();
        con=this;

        JSON_check_version json_check_version = new JSON_check_version();
       json_check_version.StartUpdate("1.1",this);
        //Intent intent = new Intent(this,MapsActivity.class);
      // startActivity(intent);
//        finish();
    }

    public void goToMap(boolean status, final Context con1){
        if(status){//wersja jest aktualna
            Intent intent = new Intent(con1,MapsActivity.class);
              con1.startActivity(intent);
              finish();

        }else {// kiedy trzeba poobrać nową werjse
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(con);
            dlgAlert.setTitle("Dostępna jest nowa wersja aplikacji");
            dlgAlert.setMessage("Aktualna wersja może nie działać prawidłowo, zalecana jest aktualizacja.");
           dlgAlert.setCancelable(false);
           dlgAlert.setNegativeButton("Przypomnij później", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   Intent intent = new Intent(con1,MapsActivity.class);
                   con1.startActivity(intent);
                   finish();
               }
           });
            dlgAlert.setPositiveButton("Aktualizuj",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //dismiss the dialog

                            // String URL =result.replace("\"","");
                        //    Intent intent = new Intent(SplashScreen.this, Update.class);
                            // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            // intent.putExtra("url", URL);
                            //con.startActivity(intent);
                        }
                    });
            dlgAlert.show();
        }
    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }
    private void setPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET,Manifest.permission.CAMERA}, 1);
    }

    private void verifyPermissions(){
        String[] permissions={Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),permissions[0])==PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1])==PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[2])==PackageManager.PERMISSION_GRANTED){
        }else{
            ActivityCompat.requestPermissions(SplashScreen.this,permissions,1);
        }
    }

}