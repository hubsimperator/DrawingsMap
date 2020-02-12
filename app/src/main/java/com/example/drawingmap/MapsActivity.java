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
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

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


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        TakePhoto.activityResult(requestCode,resultCode,data);
    }
    Context con = this;
    public GoogleMap gm;
    public GoogleMap mMap;

    ToggleButton tb_positionsource;
    Button bt_layout;
    Button bt_maptypes;
    Button bt_refresh;
    ToggleButton bt_Paint;
    Button bt_find;



    private LocationManager locationMangaer=null;
    private LocationListener locationListener=null;
    private Boolean flag = false;

    ArrayList<LatLng> punkty;

    public static String promien = "0.0015";

    public static String pathScreenshoot=null;

    public static LatLng DefaultPositon =new LatLng(54.357860, 18.656723);
    public static Location CurrentPosition;
    public static LatLng PinPosition;
    public static LatLng POIPosition;

    public static int map_counter=0;


    public void GPS_toggle(){
        
        try{
        }catch(NullPointerException ne){
            Toast.makeText(con,"Oczekiwanie na sygnał GPS...",Toast.LENGTH_LONG).show();
        }

        if(marker!=null){
            marker.remove();
        }
        mMap.setOnMapClickListener(null);
        LatLng gps;
        if(CurrentPosition!=null){
            gps = new LatLng(CurrentPosition.getLatitude(), CurrentPosition.getLongitude());
            POIPosition=gps;
            marker=  mMap.addMarker(new MarkerOptions().position(gps));
            marker.setIcon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_mylocation));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gps,18));
        }
    }

    public void PIN_Set(LatLng point, GoogleMap mMap){

                if(marker!=null){
                    marker.remove();
                }
                PinPosition=point;
                POIPosition=PinPosition;
                        marker = mMap.addMarker(new MarkerOptions().position(point));
                       marker.setIcon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_mylocation));
            }


    @Override
    protected void onStop() {
        super.onStop();
        locationMangaer.removeUpdates(locationListener);
        Log.d("Log","Zatrzymalem aplikacje 1");
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationMangaer.removeUpdates(locationListener);
        Log.d("Log","Zatrzymalem aplikacje 2");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationMangaer.removeUpdates(locationListener);
        Log.d("Log","Zatrzymalem aplikacje 3");

    }

    @Override
    protected void onResume() {
        super.onResume();
        locationMangaer.requestLocationUpdates(LocationManager
                .GPS_PROVIDER, 5000, 100,locationListener);        Log.d("Log","Rozpoczalem aplikacje 1");
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationMangaer.requestLocationUpdates(LocationManager
                .GPS_PROVIDER, 5000, 100,locationListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        POIPosition=DefaultPositon;

        bt_find=(Button) findViewById(R.id.bt_find);
        bt_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressGdansk_dialog adres_gdansk=new AddressGdansk_dialog();
                adres_gdansk.GetAddress(mMap,con);

            }
        });

        StrictMode.VmPolicy.Builder newbuilder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(newbuilder.build());

        punkty = new ArrayList<LatLng>();
        final Layers lay = new Layers();
        final JSONrequest json = new JSONrequest();
        final AlertDialog.Builder a_builder = new AlertDialog.Builder(MapsActivity.this);


        locationMangaer = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
     //   flag = displayGpsStatus();
        //if (flag) {
         // komentuj dla emulatora

            locationListener = new MyLocationListener();
            locationMangaer.requestLocationUpdates(LocationManager
                    .GPS_PROVIDER, 5000, 100,locationListener);




      //  } else {
      //  }

        //final layers_dialog lay_d=new layers_dialog();
        final JSONrequest_layers jsoNrequest_layers=new JSONrequest_layers();

      //  final JSONrequest_get_photo get_photo=new JSONrequest_get_photo();

        bt_refresh=(Button) findViewById(R.id.bt_refresh);
        bt_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> listaWarstwDoWyswietlenia=new ArrayList<>();
                 ArrayList<Obiekt_warstwa> listaWarstw=new ArrayList<>();
                listaWarstw=layers_dialog_db2.listaWarstw;
                for(int i=1;i<=layers_dialog_db2.switch_availability.size();i++){
                    if(layers_dialog_db2.switch_availability.get(i))listaWarstwDoWyswietlenia.add(listaWarstw.get(i-1).getLayerId());
                }

                LatLng rog_1 = mMap.getProjection().getVisibleRegion().farLeft;
                LatLng rog_2 = mMap.getProjection().getVisibleRegion().nearRight;
                mMap.clear();
                JSON_drawLayers json_drawLayers=new JSON_drawLayers();
                json_drawLayers.Send2(mMap,MapsActivity.this,rog_1,rog_2,listaWarstwDoWyswietlenia,"");
            }
        });

        bt_layout=(Button) findViewById(R.id.bt_layout);
        bt_layout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
           //     lay_d.layers_dialog(mMap,con,promien,POIPosition);

                ZarzadcaBazyPhoto zarzadcaBazyPhoto = new ZarzadcaBazyPhoto(con);
                if (zarzadcaBazyPhoto.sprawdzCzyIstniejeTabela()){
  Log.d("zarzadcaBazyphot","Tabela photo istnieje");
                }
                else{
                    Log.d("zarzadcaBazyphot","Tabela photo NIE ! istnieje");
                    JSONrequest_get_photo jsoNrequest_get_photo=new JSONrequest_get_photo();
                    jsoNrequest_get_photo.Send(con, "all");
                }
                JSON_wartswy_do_wyswietlenia json_wartswy_do_wyswietlenia=new JSON_wartswy_do_wyswietlenia();
                json_wartswy_do_wyswietlenia.Send(gm,con,promien,POIPosition);
            }

        });


        bt_Paint=(ToggleButton) findViewById(R.id.toggPaintBtn);
        bt_Paint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Screenshoot scr = new Screenshoot();
                scr.TakeScreenshoot(mMap, con);
            }

        });


        //wejscie do settings

        bt_maptypes=(Button) findViewById(R.id.bt_maptypes);
        bt_maptypes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Licznik",Integer.toString(map_counter));

                new CountDownTimer(5000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        Log.d("Zegar","seconds remaining: " + millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        Log.d("Zegar","done");
                        map_counter=0;
                    }

                }.start();
                if(map_counter >= 15)
                {
                    LocalSettings localSettings = new LocalSettings();
                    localSettings.Ustawienia(MapsActivity.this);
                }
                else
                {
                    if(mMap.getMapType()==GoogleMap.MAP_TYPE_HYBRID)
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    else if(mMap.getMapType()==GoogleMap.MAP_TYPE_NORMAL)
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    map_counter++;
                }
            }
        });
    }
    Polyline polyline_previous;
    Marker marker;
    View root;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (root!= null) {
            ViewGroup parent = (ViewGroup) root.getParent();
            if (parent != null)
                parent.removeView(root);
        }
        try {
            root= inflater.inflate(R.layout.activity_maps, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }
        return root;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        final JSONrequest json = new JSONrequest();
        mMap = googleMap;

        Layers l =new Layers();
        float zoomLevel = 18.0f; //This goes up to 21
        LatLng start = new LatLng(54.358106, 18.656338);
      //  mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, zoomLevel));
       gm=mMap;
        //PIN_toggle();

        mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
            JSONrequest_data jsd=new JSONrequest_data();
            public void onPolylineClick(Polyline polyline) {
                if(polyline_previous != null)
                {
                    polyline_previous.setColor(polyline_previous.getColor() ^ 0x0000CC00);
                }
                int strokeColor = polyline.getColor() ^ 0x0000CC00;
                polyline.setColor(strokeColor);
                jsd.Send(mMap,polyline.getTag().toString(),con);
                polyline_previous=polyline;
            }

        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            JSONrequest_data jsd=new JSONrequest_data();
            @Override
            public boolean onMarkerClick(Marker marker) {
                try{
                    jsd.Send(mMap,marker.getTag().toString(),con);
                }catch(NullPointerException ne){
                }
                return false;
            }
        });

    }

    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {

            return false;
        }
    }
    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {
            CurrentPosition=loc;
            Log.d("Log","Zmiana pozycji");
try {
    mMap.setMyLocationEnabled(true);
}catch (NullPointerException ne){

}
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

    public static int backButtonCount;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed()
    {
        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                backButtonCount=1;
            }
            public void onFinish() {
                Log.d("Zegar","done");
                backButtonCount=0;
            }

        }.start();
        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "W celu zamknięcia aplikacji naćiśnij POWRÓT jeszcze raz", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    }

