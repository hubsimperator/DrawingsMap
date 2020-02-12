package com.example.drawingmap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drawingmap.JSON.JSONrequest;
import com.example.drawingmap.JSON.JSONrequest_layers;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class layers_dialog_db {

    private static String promien_m="150";
    private static String promien=Double.toString(Double.valueOf(promien_m) * 0.00001);

    AlertDialog alertDialog;

    ArrayList<String> layer_available;
    ArrayList<String> ParamId;
    ArrayList<Switch> list_switch;
//Switch sw;

    public static Map<Integer, Boolean> switch_availability;
    public static GoogleMap gMap;
    public static Context con1;
    public static LatLng position;

    public void layers_dialog(final GoogleMap mMap, final Context con, final String prom, final LatLng POIPosition, ArrayList<String> layer,ArrayList<String> param) {
        final Layers lay = new Layers();
        final JSONrequest json= new JSONrequest();
        gMap=mMap;
        con1=con;
        position=POIPosition;

        //promien=prom;
        layer_available=layer;
        ParamId=param;
        try{
            Log.d("TAG", switch_availability.toString());
            switch_availability=switch_availability;
        }catch (NullPointerException e){
            switch_availability=new HashMap<>();
            list_switch=new ArrayList<>();
        }

        View view;
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(con).setPositiveButton("Zamknij", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        LayoutInflater inflater = (LayoutInflater)   con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.layers_layers_db,null);
        LinearLayout lllabel = (LinearLayout) view.findViewById(R.id.linearlayoutlabelsid);
        int a=1;
        dialogBuilder.setView(view);

        LinearLayout.LayoutParams param_layout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                2.0f
        );
        // wypisuje dostepne wartstwy i przyciski
        for(int i=0;i<layer_available.size();i++) {

            final TextView tv = new TextView(con);
            final String param_name=layer_available.get(i);
            tv.setText(layer_available.get(i));
            tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            lllabel.addView(tv);
            final Switch sw = new Switch(con);
            sw.setId(Integer.valueOf(ParamId.get(i)));
            try {
                sw.setChecked(switch_availability.get(sw.getId()));
                Log.d("TAG", switch_availability.toString());
            } catch (NullPointerException e) {
            }
            sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                    if (bChecked) {
                        switch_availability.put(sw.getId(), true);
                       Log.d("Korner",mMap.getProjection().getVisibleRegion().farLeft.toString());
                        Log.d("Korner",mMap.getProjection().getVisibleRegion().nearRight.toString());
                      //  json.Send2(mMap, con, promien, mMap.getProjection().getVisibleRegion().farLeft,mMap.getProjection().getVisibleRegion().nearRight, Integer.toString(sw.getId()), "",param_name);
                         json.Send(mMap, con, promien, POIPosition, Integer.toString(sw.getId()), "",param_name);
                    } else {
                        switch_availability.put(sw.getId(), false);
                        lay.Usun(sw.getId());
                    }
                }
            });
            lllabel.addView(sw);
        }
// zmiana i wyswietlanie promienia poszukiwania
        final TextView test=(TextView)  view.findViewById(R.id.tv_promien);
        test.setText(promien_m);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder1 = new AlertDialog.Builder(con);
                dialogBuilder1.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                promien = String.valueOf(Double.valueOf(promien_m) * 0.00001);
                                test.setText(promien_m);
                                if(Integer.valueOf(promien_m)>600){
                                    Toast.makeText(con,"Uwaga ! Duży obszar poszukiwania spowolni pracę aplikacji i szybkość wczytywania danych. Wymagane: Dobre połączenie z internetem",
                                            Toast.LENGTH_LONG).show();
                                }
                                lay.ClearAllObjects(mMap);
                                clear_all();
                                dialog.dismiss();
                            }
                        });   LayoutInflater inflater1 = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView1 = inflater1.inflate(R.layout.layers_promien_wyszukiwania, null);
                dialogBuilder1.setView(dialogView1);

                int minValue = 100;
                int maxValue = 2000;
                int step = 50;

                final String[] valueSet = new String[((maxValue-minValue)/step)+1 ];

                for (int i = 0;100+i*step<2001 ; i++) {
                    valueSet[i] = Integer.toString(100+i*step);
                }
                final NumberPicker np =(NumberPicker) dialogView1.findViewById(R.id.numberPicker1) ;
                String select_number;
                np.setMinValue(0);
                np.setMaxValue(38);
                np.setDisplayedValues(valueSet);
                promien_m=Integer.toString(100+np.getValue()*step);
                np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        promien_m=valueSet[newVal];
                    }
                });
                AlertDialog alertDialog = dialogBuilder1.create();
                alertDialog.show();
            }
        });
        alertDialog =dialogBuilder.create();
        alertDialog.show();


    }

    public void clear_all(){
        JSONrequest_layers json_l=new JSONrequest_layers();
        alertDialog.dismiss();
        json_l.Send(gMap,con1,promien,position);


        Log.d("TAG","tu bedzie kasowanie");
        try {
            Log.d("TAG",switch_availability.toString());

            switch_availability.clear();
        }catch (NullPointerException ne){
            Log.d("TAG","juz nie mozna");
        }
    }
}
