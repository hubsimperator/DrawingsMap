package com.example.drawingmap.JSON;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.drawingmap.Layers;
import com.example.drawingmap.MapsActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class JSONrequest_data {

public int x;
Context con1=null;
   // public JSONrequest(int jakasLiczba) { this.jakasLiczba = jakasLiczba; }
    String Id = "", Exception = "";
    String output="";
GoogleMap mMap=null;
Layers la;

/*
Wybor
0-siec wodociagowa
1- info o wodociagach

 */

public static String Mslink;
public static String Parametr;
    public static String Podparametr;
    public static String Podparametr_label;
    public static String Parametr_label;



    public void Send(GoogleMap googleMap,String mslink,Context con) {
        mMap=googleMap;
        String msl[]= mslink.split(";");
        Parametr=msl[0];
        Podparametr=msl[2];

        Mslink=msl[1];
       String ms2[]= Podparametr.split("\\+");
       Parametr_label=ms2[0];
       Podparametr_label=ms2[1];
        con1=con;
        new JSONrequest_data.HttpAsyncTask3().execute("https://notif2.sng.com.pl/api/MobileAppGetData");
    }
    /*
    promien
    1 -- 111 km
    0.1 - 11.1
     */

    public String POST(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Mslink", Mslink);
            jsonObject.put("Param", Parametr);

            //tutaj wrzucasz elementy json
            jsonObject.accumulate("Id", Id);
            jsonObject.accumulate("Exception", Exception);
            json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("Accept", "application/json");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null) result = convertInputStreamToString(inputStream);
            else result = "Nie działa";
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }


    ArrayList<ArrayList<LatLng>> MSLINK_value;
    ArrayList<String> MSLINK_label;

    ArrayList<LatLng>  punkty;
    Polyline polyline6;
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
              try {
                  deserialize_json(result);
              }catch (NullPointerException ne){
                  new AlertDialog.Builder(con1)
                          .setTitle("Błąd")
                          .setMessage("Nie można wczytać danych. Sprawdź połączenie z Internetem")
                          .setIcon(android.R.drawable.ic_dialog_alert)
                          .setCancelable(true)
                          .show();
              }
        }
    }

    public void deserialize_json(String input)
    {

        MapsActivity ma=new MapsActivity();
        ArrayList<String> Dataname=new ArrayList<String>();
        ArrayList<String> Value=new ArrayList<String>();
        String dataname="";
        String value="";
        String[] separated;
        JSONArray array = null;

            try {
                array = new JSONArray(input);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                for (int i = 0; i <array.length(); i++) {
                    JSONObject row = array.getJSONObject(i);
                    dataname = row.getString("dataname");
                    value = row.getString("value");
                    Dataname.add(dataname);
                    Value.add(value);
                }
            }
            catch (JSONException e) {                e.printStackTrace();
            }

              display_parameters(Dataname,Value,con1,Mslink);
              Log.d("TAG", "**********************");
        Log.d("TAG", "RESULT");
        Log.d("TAG", Dataname.toString());
        Log.d("TAG", Value.toString());
        Log.d("TAG", "**********************");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void display_parameters(ArrayList<String> Dataname, ArrayList<String> Value, final Context con, final String mslink){
        JSONrequest_data jd=new JSONrequest_data();
        String info="";
        Log.d("TAG", "Wyswietl param i pod");
     //   Log.d("TAG", Parametr);
       // Log.d("TAG", Parametr_label);
        //Log.d("TAG", Podparametr_label);
        Log.d("TAG", "**********************");
        for(int i=0;i<Dataname.size();i++){
            info +=( Dataname.get(i) + ": " +Value.get(i)+"\n" );
        }
        AlertDialog.Builder dialogBuilder1 = new AlertDialog.Builder(con);

        dialogBuilder1.setTitle(Parametr_label+": "+Podparametr_label)
                .setMessage("Mslink : "+mslink+"\n"+info)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
             //   .setNeutralButton("Zrób zdjęcie",null)
                .setIcon(android.R.drawable.ic_dialog_info);

        AlertDialog alertDialog = dialogBuilder1.create();
        alertDialog.show();

        // wywolanie aparatu !!
//final TakePhoto tp =new TakePhoto();

        Button neutralButton=alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        neutralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tp.TakePhoto(con,Parametr_label,Podparametr_label,mslink);
                Toast.makeText(con1,"Niedostępne w wersji Beta",Toast.LENGTH_LONG).show();
            }
        });

    }//


    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null) result += line;
        inputStream.close();
        return result;
    }


}