package com.example.drawingmap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.drawingmap.JSON.JSON_drawLayers;
import com.example.drawingmap.JSON.JSON_getpodparams;
import com.example.drawingmap.JSON.JSONrequest;
import com.example.drawingmap.JSON.JSONrequest_layers;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class layers_dialog_db2 extends AppCompatActivity {
    View view;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ListaWarstwAdapter adapter;
    public static ArrayList<String> listaWarstwDoWyswietlenia;
    public static HashMap<Integer, Boolean> switch_availability = new HashMap<>();

    public static HashMap<Integer, HashMap<Integer, Boolean>> parampoparam_click = new HashMap<>();
    public static HashMap<Integer, Boolean> podparam_click = new HashMap<>();


    AlertDialog alertDialog;
    public static GoogleMap gMap;
    public static Context con1;
    public static LatLng position;
    int p=0;
    public static ListView mListView;
    public static ArrayList<Obiekt_warstwa> listaWarstw=new ArrayList<>();

    public void layers_dialog(final GoogleMap mMap, final Context con, final String prom, final LatLng POIPosition, final ArrayList<Obiekt_warstwa> _listaWarstw) {
        gMap = mMap;
        con1 = con;
        position = POIPosition;
        listaWarstw = _listaWarstw;

        if(switch_availability.size()<1){
            for(int i =0;i<listaWarstw.size();i++){
                switch_availability.put(i+1,false);
            }
        }

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(con).setPositiveButton("Potwierdź", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                gMap.clear();
                LatLng rog_1 = gMap.getProjection().getVisibleRegion().farLeft;
                LatLng rog_2 = gMap.getProjection().getVisibleRegion().nearRight;
                listaWarstwDoWyswietlenia=new ArrayList<>();

                for(int i=1;i<=switch_availability.size();i++){
                    if(switch_availability.get(i))listaWarstwDoWyswietlenia.add(listaWarstw.get(i-1).getLayerId());
                }

                JSON_drawLayers json_drawLayers=new JSON_drawLayers();
                json_drawLayers.Send2(gMap,con1,rog_1,rog_2,listaWarstwDoWyswietlenia,"");
            }
        })
                .setNeutralButton("Powrót",null);

        LayoutInflater inflater = (LayoutInflater)   con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.wartswy_do_wyswietlenia,null);
        dialogBuilder.setView(view);
        adapter=new ListaWarstwAdapter(con1,R.layout.lista_warstw_adapter,listaWarstw,con1,switch_availability);
        mListView = (ListView) view.findViewById(R.id.lista_warstw_do_wyswietlenia);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int _position, long id) {
                switch_availability.put(_position+1,!(switch_availability.get(_position+1)));
                for (int i = 0; i < mListView.getChildCount(); i++) {
                    if(_position == i+mListView.getFirstVisiblePosition() ){
                        p=_position;
                        if(!switch_availability.get(_position+1)){
                            mListView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                        }else{
                            mListView.getChildAt(i).setBackgroundColor(Color.GREEN);
                        }
                    }else{
                    }
                }
            }
        });


        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<Obiekt_warstwa> a= new ArrayList<>();
                // a=listaWarstw;
                JSON_getpodparams json_getpodparams=new JSON_getpodparams();
                String param_id=listaWarstw.get(position).getLayerId();
                String param_name=listaWarstw.get(position).getLayerName();
                json_getpodparams.Send2(param_id,param_name,con,mMap);
                return true;
            }
        });



        alertDialog =dialogBuilder.create();
        alertDialog.show();
    }


    public void wyswietl_podparametry(final ArrayList<Obiekt_podparam> podparam){

        podparam_click=new HashMap<>();
        if(podparam_click.size()<1){
            for(int i =0;i<podparam.size();i++){
                podparam_click.put(i+1,false);
            }
        }


        try {
            if (parampoparam_click.get(Integer.valueOf(podparam.get(0).getRootId())).size() < 1) {
            }
        }
        catch (Exception e) {
            for (int i = 0; i < podparam.size(); i++) {
                parampoparam_click.put(Integer.valueOf(podparam.get(0).getRootId()), podparam_click);
            }
        }


        if(switch_availability.get(Integer.valueOf(podparam.get(0).getRootId()))){
            for(int i =0;i<podparam.size();i++){
                podparam_click.put(i+1,true);
            }
        }else{
            for(int i =0;i<podparam.size();i++){
                podparam_click.put(i+1,false);
            }
        }

Log.d("a",switch_availability.toString());
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(con1).setPositiveButton("Potwierdź", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        })
                .setNeutralButton("Powrót",null);

        LayoutInflater inflater = (LayoutInflater)   con1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.podwarstwy_do_wyswietlenia,null);
        dialogBuilder.setView(view);

        ListaPodwarstwAdapter adapter;
        adapter=new ListaPodwarstwAdapter(con1,R.layout.lista_podwarstw_adapter,podparam,con1,parampoparam_click.get(Integer.valueOf(podparam.get(0).getRootId())));

        TextView naglowek=(TextView) view.findViewById(R.id.naglowek_podwarstw);
        naglowek.setText(podparam.get(0).getRootName());

        final ListView mListView = (ListView) view.findViewById(R.id.lista_warstw_do_wyswietlenia);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                podparam_click.put(position+1,!(podparam_click.get(position+1)));

                for (int i = 0; i < mListView.getChildCount(); i++) {
                    if(position == i+mListView.getFirstVisiblePosition() ){
                        p=position;
                        if(!podparam_click.get(position+1)){
                            mListView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                        }else{
                            mListView.getChildAt(i).setBackgroundColor(Color.parseColor("#B0E0E6"));
                        }
                    }else{
                    }
                }

            }
        });


        alertDialog =dialogBuilder.create();
        alertDialog.show();
    }


}