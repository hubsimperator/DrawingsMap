package com.example.drawingmap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.datatype.Duration;

public class Layers {
    public static GoogleMap mMap;
    public static List<Polyline> polylines;
    public static List<String> mslink;



    public static List<Marker> markers_hydrant_all;
    public static List<String> mslink_hydrant_all;



    public ArrayList<String> Parameters;
    public static Map<Integer,ArrayList<Polyline>> polylines_avaibility;
    public static Map<Integer,ArrayList<Marker>> markers_avaibility;

    public static ArrayList<String> ms;

    public void Rysuj_punkt(GoogleMap gm,Context con, ArrayList<ArrayList<LatLng>> MSLINK,ArrayList<String> label,ArrayList<String> kolor,ArrayList<String> param,String parametr,String podparametr){
        try{
            Log.d("TAG", markers_avaibility.toString());
            markers_avaibility=markers_avaibility;
        }catch (NullPointerException e){
            markers_avaibility=new HashMap<>();
        }

        markers_hydrant_all=new ArrayList<Marker>();
        mslink_hydrant_all=new ArrayList<String>();
        mslink_hydrant_all=label;
        mMap=gm;
        Marker mark;
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.komora);

        if(mMap!=null) {
            for(int i=0;i<MSLINK.size();i++){
                Log.d("wyswietlam",parametr+" i "+podparametr);

              //pobiera ikone z bazy sqlite
               ZarzadcaBazyPhoto zarzadcaBazyPhoto=new ZarzadcaBazyPhoto(con);
               try {
                   String encodedImage = zarzadcaBazyPhoto.dajPhoto(kolor.get(i));
                   byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                   Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                   markers_hydrant_all.add(
                           mark = mMap.addMarker(new MarkerOptions()
                                   .position(MSLINK.get(i).get(0))
                                   .icon(BitmapDescriptorFactory.fromBitmap(decodedByte)))
                   );
               }catch (NullPointerException ne){
                   markers_hydrant_all.add(
                           mark = mMap.addMarker(new MarkerOptions()
                                   .position(MSLINK.get(i).get(0))));

               }
                markers_hydrant_all.get(i).setTag(parametr+";"+ label.get(i)+";"+ param.get(i) +";"+mslink_hydrant_all.get(i)+";"+kolor.get(i));
         //  .setTag(parametr+";"+ label.get(i)+";"+ param.get(i) +";"+mslink.get(i)+";"+kolor.get(i));
                Log.d("setTag",markers_hydrant_all.get(i).getTag().toString());
            }
            markers_avaibility.put(Integer.valueOf(parametr), (ArrayList<Marker>) markers_hydrant_all);
        }

    }

    public void Rysuj_linie(GoogleMap gm,Context con, ArrayList<ArrayList<LatLng>> MSLINK,ArrayList<String> label,ArrayList<String> kolor,ArrayList<String> param,String parametr,String podparametr){

        try{
            Log.d("TAG", polylines_avaibility.toString());
            polylines_avaibility=polylines_avaibility;
        }catch (NullPointerException e){
            polylines_avaibility=new HashMap<>();
        }

            polylines =new ArrayList<Polyline>();
            mslink=new ArrayList<String>();
            mslink=label;
            mMap=gm;

            if(mMap!=null) {

                List<String> param_uniqe = new ArrayList<String>(new HashSet<String>(param));
                List<PatternItem> pattern=null;

                int[] rgb;
                int polyline_pattern;
                for(int i=0;i<MSLINK.size();i++) {
                    rgb=Color_Parse(kolor.get(i));
                    try {
                        polyline_pattern=rgb[3];
                        if(polyline_pattern==1){
                            pattern = Arrays.<PatternItem>asList(
                                    new Dash(40), new Gap(20), new Dash(40), new Gap(20));
                        }
                    }
                    catch (Exception e) {
                        pattern=null;
                    }
                    polylines.add(
                            this.mMap.addPolyline(new PolylineOptions()
                                    .clickable(true)
                                    .width(5)
                                    .color(Color.rgb(rgb[0],rgb[1],rgb[2]))
                                    .addAll(MSLINK.get(i))
                                    .pattern(pattern)
                            )
                    );
                    Log.d("setTag",parametr+";"+ label.get(i)+";"+ param.get(i) +";"+mslink.get(i)+";"+kolor.get(i));
                    polylines.get(i).setTag(parametr+";"+ label.get(i)+";"+ param.get(i) +";"+mslink.get(i)+";"+kolor.get(i));
                }
                polylines_avaibility.put(Integer.valueOf(parametr), (ArrayList<Polyline>) polylines);
                String s;
                String[] ss;
            }

        //   }
        mMap=gm;

    }

    public void NarysujLinie(){

    }

    public void NarysujPunkt(){

    }

    public void RysujNaMapie(ArrayList<Obiekt_LayerToDrawing> listToDrawing,GoogleMap gm,Context con){
        mMap=gm;
        polylines =new ArrayList<Polyline>();
        markers_hydrant_all=new ArrayList<Marker>();
        int polylines_count=0;
        int points_count=0;
        for(int i=0;i<listToDrawing.size();i++){
            if(listToDrawing.get(i).getType().equals("Point")){
                Marker mark;
                    ZarzadcaBazyPhoto zarzadcaBazyPhoto=new ZarzadcaBazyPhoto(con);
                String ss[]=listToDrawing.get(i).getLatlong().split(",");
                Double angle_degree=Double.valueOf(listToDrawing.get(i).getAngle());
                angle_degree=Math.toDegrees(angle_degree);

                LatLng coord=new LatLng(Double.parseDouble(ss[0]), Double.parseDouble(ss[1]));
                    try {
                        String encodedImage = zarzadcaBazyPhoto.dajPhoto(listToDrawing.get(i).getKolor());
                        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        markers_hydrant_all.add(
                                mark = mMap.addMarker(new MarkerOptions()
                                        .position(coord)
                                        .rotation(360-(angle_degree.floatValue()))
                                        .flat(true)
                                        .anchor(0.5f,0.5f)
                                        .icon(BitmapDescriptorFactory.fromBitmap(decodedByte)))
                        );
                    }catch (NullPointerException ne){
                        markers_hydrant_all.add(
                                mark = mMap.addMarker(new MarkerOptions()
                                        .position(coord)));
                    }
                    markers_hydrant_all.get(points_count).setTag(listToDrawing.get(i).getParam()+";"+ listToDrawing.get(i).getMslink()+";"+ listToDrawing.get(i).getPodparam() +";"+listToDrawing.get(i).getMslink()+";"+listToDrawing.get(i).getKolor());
                    points_count++;
            }else if(listToDrawing.get(i).getType().equals("Polilines")){
                List<PatternItem> pattern=null;
                int polyline_pattern;
                ArrayList<LatLng> coordPolylines=new ArrayList<>();

                    String s[]=listToDrawing.get(i).getLatlong().split(";");
                    for(int j=0;j<s.length;j++){

                        String ss[]=s[j].split(",");
                        coordPolylines.add(new LatLng(Double.parseDouble(ss[0]), Double.parseDouble(ss[1])));


                    }


                int [] rgb=Color_Parse(listToDrawing.get(i).getKolor());
                try {
                    polyline_pattern=rgb[3];
                    if(polyline_pattern==1){
                        pattern = Arrays.<PatternItem>asList(
                                new Dash(40), new Gap(20), new Dash(40), new Gap(20));
                    }
                }
                catch (Exception e) {
                    pattern=null;
                }
                polylines.add(
                        this.mMap.addPolyline(new PolylineOptions()
                                .clickable(true)
                                .addAll(coordPolylines)
                                .color(Color.rgb(rgb[0],rgb[1],rgb[2]))
                                .pattern(pattern)
                        )
                );
                polylines.get(polylines_count).setTag(listToDrawing.get(i).getParam()+";"+ listToDrawing.get(i).getMslink()+";"+ listToDrawing.get(i).getPodparam() +";"+listToDrawing.get(i).getMslink()+";"+listToDrawing.get(i).getKolor());
                polylines_count++;
            }else{

            }
        }
    }

    public int[] Color_Parse(String rgb){
        String[] actualValue = rgb.split(";");
        int r=Integer.valueOf(actualValue[0]);
        int g=Integer.valueOf(actualValue[1]);
        int b=Integer.valueOf(actualValue[2]);
        try {
            int stripe_line=Integer.valueOf(actualValue[3]);
            return new int[] {r, g,b,stripe_line};
        }
        catch (Exception e) {
            return new int[] {r, g,b};
        }
    }

    public void Usun(int parametr) {
       try {
           Log.d("TAG","Awaria polyline");
           polylines=polylines_avaibility.get(parametr);
            try {                Log.d("TAG"," nie hahahah p");

                for (Polyline pol : polylines) {
                    pol.remove();
                }
                polylines.clear();
                polylines_avaibility.remove(parametr);
            }catch (NullPointerException nee){
                Log.d("TAG","hahahah p");
                try {  Log.d("TAG"," nie hahahah m");
                    markers_hydrant_all=markers_avaibility.get(parametr);

                    for (Marker mark : markers_hydrant_all) {
                        mark.remove();
                    }
                    markers_hydrant_all.clear();
                    markers_avaibility.remove(parametr);
                }catch (NullPointerException nees){
                    Log.d("TAG","hahahah m");
                }
            }
       }
       catch (NullPointerException ne){
           Log.d("TAG","Awaria marker");


           try {  Log.d("TAG"," nie hahahah m");
               markers_hydrant_all=markers_avaibility.get(parametr);

               for (Marker mark : markers_hydrant_all) {
                   mark.remove();
               }
               markers_hydrant_all.clear();
               markers_avaibility.remove(parametr);
           }catch (NullPointerException nee){
               Log.d("TAG","hahahah m");

           }
       }
    }

    public void ClearAllObjects(GoogleMap mMap){
        mMap.clear();
    }
}
