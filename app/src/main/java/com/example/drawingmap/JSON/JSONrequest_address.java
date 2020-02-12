package com.example.drawingmap.JSON;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drawingmap.AddressGdansk_dialog;
import com.example.drawingmap.MapsActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/*
----Levels
0 - GetLocalizationStreet
1 - GetLocalizationPoint
2 - GetLocalizationPointIntakes
 */


public class JSONrequest_address {
        public static int index_id;

        Context con1=null;
        GoogleMap mMap=null;
        public static String ID=null;
        public static ArrayList<String> url_address_list;
        public static Integer Level;



        public void Send(GoogleMap googleMap,Context con,Integer level,String id) {

            Log.d("TAG", "**********************");
            Log.d("TAG", "Nowy send");
            Log.d("TAG", Integer.toString(level));
            Log.d("TAG", id);

            Log.d("TAG", "**********************");
            ID=id;
            Level=level;
            url_address_list= new ArrayList<String >();
            url_address_list.add("https://notification.sng.com.pl/api/GetLocalizationStreet"); //933016 id
            url_address_list.add("https://notification.sng.com.pl/api/GetLocalizationPoint"); //id ulicy np
            url_address_list.add("https://notification.sng.com.pl/api/GetLocalizationPointIntakes"); // id punktu 38431
            mMap=googleMap;
            con1=con;
            new HttpAsyncTask3().execute("https://notif2.sng.com.pl/api/MobileAppGetGdanskAddress");
        }

        public String POST(String url) {
            InputStream inputStream = null;
            String result = "";
            StringBuilder builder = new StringBuilder();

            //żywcem pobrane z sng twoje wodociagi

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader("Content-Type", "application/json");
                httpPost.setHeader("Accept", "application/json");
                httpPost.setEntity(new StringEntity(ID));
                HttpResponse response = httpclient.execute(httpPost);
                org.apache.http.StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                } else {
                    Log.e("==>", "Fail to dowload ");
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return builder.toString();
        }

        AlertDialog alertDialog;

        private class HttpAsyncTask3 extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                alertDialog=new AlertDialog.Builder(con1)
                        .setTitle("Proszę czekać ")
                        .setMessage("Pobieranie danych ...")
                        .setIcon(android.R.drawable.ic_input_add)
                        .show();
            }

            protected String doInBackground(String... urls) {
                return POST(urls[0]);
            }

            @Override
            protected void onPostExecute(String result) {
                alertDialog.dismiss();
                //Boolean b=deserialize_json(result);

                try {

                    Boolean b=deserialize_json(result);
                }
                catch (NullPointerException ne){
                    Log.d("aa","tu sie wbija");
                    new AlertDialog.Builder(con1)
                            .setTitle("Błąd")
                            .setMessage("Nie można wczytać danych. Sprawdź połączenie z Internetem")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setCancelable(true)
                            .show();
                }

            }
        }
        public Boolean deserialize_json(String input)
        {
            if(Level==2) {
                Log.d("Deser", "edzie input");

                Log.d("Deser", input);
            }
            String s=null;
            MapsActivity ma=new MapsActivity();
            final ArrayList<String> Dataname=new ArrayList<String>();
            final ArrayList<String> Value=new ArrayList<String>();
            String dataname="";
            String value="";
            JSONArray array = null;

            try {
                array = new JSONArray(input);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                for (int i = 0; i <array.length(); i++) {
                    JSONObject row = array.getJSONObject(i);

                    if(Level==2) {
                        Log.d("Deserializacja","-.-.-.-.-.--..--..-.--.-.-..-");

                        dataname = row.getString("latitude");
                        value = row.getString("longitude");
                        Log.d("Deserializacja",dataname);
                        Log.d("Deserializacja",value);
                    }
                    else{
                        dataname = row.getString("Name");
                        value = row.getString("Id");
                    }
                    Dataname.add(dataname);
                    Value.add(value);
                }
            }
            catch (JSONException e) {                e.printStackTrace();
            }
          //  display_parameters(Dataname,Value,con1,Mslink);
            Log.d("TAG", "**********************");
            Log.d("TAG", "RESULT");
            Log.d("TAG", Dataname.toString());
            Log.d("TAG", Value.toString());
            Log.d("TAG", "**********************");


            Log.d("TAG", "**********************");
            Log.d("TAG", "Wybor");
            Log.d("TAG", Integer.toString(Level));
            Log.d("TAG", "**********************");
            AddressGdansk_dialog adres_gdansk = new AddressGdansk_dialog();

            if(Level==0) {
               // adres_gdansk.test(Dataname, Value);
            }
            else if(Level==1) {
                Log.d("TAG", "**********************");
                Log.d("TAG", "Wybor 1");
                Log.d("TAG", Integer.toString(Level));
                Log.d("TAG", "**********************");
             //   adres_gdansk.test2(Dataname, Value);
            }
            else if(Level==2) {
                adres_gdansk.SetLocalization(Dataname.get(0), Value.get(0));
            }

            return true;
        }
}
