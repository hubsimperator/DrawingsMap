package com.example.drawingmap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;

import com.example.drawingmap.JSON.JSONrequest_gdansk_address;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class AddressGdansk_dialog {
    AlertDialog alertDialog;

    public static Context con1;
    public static GoogleMap mMap;
    public ArrayList<String> Ulice = new ArrayList<>();

    public ArrayList<String> NumeryBudynkow = new ArrayList<>();
    LatLng ZoomTo;

    public void GetAddress(final GoogleMap googleMap, final Context con) {
        con1 = con;
        mMap = googleMap;

        ZarzadcaBazy zb = new ZarzadcaBazy(con);
        if (zb.sprawdzCzyIstniejeTabela()) {

            Log.d("Sprawdzabnie", "Tabela istnieje");
            Ulice = zb.dajUlice();
            Log.d("ulice", Integer.toString(Ulice.size()));

            Log.d("ulice", Ulice.toString());
            WyswietlUlice();
        } else {
            Log.d("Sprawdzabnie", "Brak tabeli");

            //tworz tabele
           // zb.stworzTabeleAdresy();

            //******

            alertDialog=new AlertDialog.Builder(con1)
                    .setTitle("Uwaga !")
                    .setMessage("Baza danych zostanie pobrana na urządzenie. Może to potrwać kilka minut, w zależności od połączenia, sugerowane połączenie z WIFI")
                    .setIcon(android.R.drawable.ic_input_add)
                    .setCancelable(true)
                    .setNegativeButton("Anuluj",null)
                    .setPositiveButton("Pobierz",null)
                    .show();

            Button PositiveButton=alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            PositiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    JSONrequest_gdansk_address json_address = new JSONrequest_gdansk_address();
                    json_address.Send(googleMap, con);
                }
            });
            Button NegativeButton=alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            NegativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });



        }
    }
    public void WyswietlUlice() {
        ZarzadcaBazy zb = new ZarzadcaBazy(con1);

        Ulice=zb.dajUlice();
        View view;
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(con1).setNegativeButton("Zamknij",null)
                .setPositiveButton("Znajdź",null);
        LayoutInflater inflater = (LayoutInflater)   con1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.layers_dialog_address,null);


        //Creating the instance of ArrayAdapter containing list of fruit names
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (con1, android.R.layout.select_dialog_item, Ulice);
        //Getting the instance of AutoCompleteTextView
        final Spinner spinner = (Spinner) view.findViewById(R.id.spinner2);

        final AutoCompleteTextView actv = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        actv.setTextColor(Color.RED);
        actv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actv.showDropDown();
            }
        });

        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, int position, long id) {
                final String ul = (String)parent.getItemAtPosition(position);
                Log.d("Element",ul);
                final ZarzadcaBazy zb= new ZarzadcaBazy(con1);
                NumeryBudynkow=zb.dajNrMieszkania(ul);

                ArrayAdapter<String> adapter1 = new ArrayAdapter<String >(con1, android.R.layout.simple_spinner_dropdown_item, NumeryBudynkow);
                spinner.setAdapter(adapter1);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        String mieszkanie = (String)parentView.getItemAtPosition(position);
                    ZarzadcaBazy zb=new ZarzadcaBazy(con1);
                       String coord=zb.dajWspolrzedneMieszkania(ul,mieszkanie);
                        String[] s= coord.split(";");
                        ZoomTo = new LatLng(Double.valueOf(s[0]), Double.valueOf(s[1]));
                        LatLng point=new LatLng(Double.valueOf(s[0]), Double.valueOf(s[1]));
                        //postaw pinezke
                        MapsActivity ma =new MapsActivity();
                        ma.PIN_Set(point,mMap);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                    }

                });


            }
        });

        dialogBuilder.setView(view);
        //dialogBuilder.show();
        alertDialog = dialogBuilder.create();
        alertDialog.show();

        Button positiveButton= (Button) alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Log.d("Trest","Bedzie wyslane");

                //  mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ZoomTo, 19));
            }
        });

    }



public void SetLocalization(String lat,String lon){
Log.d("LatLon","Odebralem");
    Log.d("LatLon",lat);
    Log.d("LatLon",lon);
    Log.d("LatLon",Integer.toString(lat.length()));
    Log.d("LatLon",Integer.toString(lon.length()));
/*
    lat.substring(0,1);
    lat.substring(lat.length()-1,1);
    Log.d("LatLon",lat);
    Log.d("LatLon",lon);
    lon.substring(0,1);
    lon.substring(lon.length()-1,1);
    Log.d("LatLon",lat);
    Log.d("LatLon",lon);

 */
    double latitude = Double.parseDouble(lat);
    double longitude = Double.parseDouble(lon);
    LatLng location = new LatLng(latitude, longitude);
    //d2.dismiss();
    //d1.dismiss();
    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 20));


}



}
