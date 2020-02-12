package com.example.drawingmap;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import static android.content.Context.LOCATION_SERVICE;

public class GPS{

    private Boolean flag = false;
    private LocationManager locationMangaer;
    private LocationListener locationListener;

    private static Context contxt;

    public void Initialize(Context con){
        contxt=con;

        locationMangaer = (LocationManager)
                contxt.getSystemService(LOCATION_SERVICE);
        flag = displayGpsStatus();
        if (flag) {

            locationListener = new MyLocationListener();
            Log.d("TAG", "**********************");
            Log.d("TAG", "11");
            Log.d("TAG", "**********************");
            locationMangaer.requestLocationUpdates(LocationManager
                    .GPS_PROVIDER, 5000, 10,locationListener);
            Log.d("TAG", "**********************");
            Log.d("TAG", "22");
            Log.d("TAG", "**********************");

        } else {

        }
    }


    public Boolean displayGpsStatus() {
        ContentResolver contentResolver = contxt
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            Log.d("TAG", "**********************");
            Log.d("TAG", "GPS on");
            Log.d("TAG", "**********************");
            return true;


        } else {
            /*
            new AlertDialog.Builder(con)
                    .setTitle("Wyłączona usługa lokalizacji")
                    .setMessage("W celu korzystania z lokalizacji włącz GPS w telefonie")
                    .setCancelable(false)
                    .setPositiveButton("Kontynuuj bez GPS", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNegativeButton("Włącz GPS", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            con.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
             */

            Log.d("TAG", "**********************");
            Log.d("TAG", "GPS OFF");
            Log.d("TAG", "**********************");

            return false;
        }
    }

    public class MyLocationListener implements LocationListener {


        @Override
        public void onLocationChanged(Location loc) {

            Toast.makeText(contxt,"Location changed : Lat: " +
                            loc.getLatitude()+ " Lng: " + loc.getLongitude(),
                    Toast.LENGTH_SHORT).show();
            Log.d("TAG", "ZMIANAAA");
            Log.d("TAG", "**********************");
            Log.d("TAG", "Location changed : Lat: " +
                    loc.getLatitude()+ " Lng: " + loc.getLongitude());
            Log.d("TAG", "**********************");
            String longitude = "Longitude: " +loc.getLongitude();
            String latitude = "Latitude: " +loc.getLatitude();
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged(String provider,
                                    int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }
    private boolean checkPermissions() {

        if (ActivityCompat.checkSelfPermission(contxt,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        if (ActivityCompat.checkSelfPermission(contxt,
                Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        if (ActivityCompat.checkSelfPermission(contxt,
                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }
    private void setPermissions(Activity act) {
        ActivityCompat.requestPermissions(act, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET}, 1);
    }


}
