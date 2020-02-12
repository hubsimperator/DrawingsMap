package com.example.drawingmap.JSON;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.drawingmap.Layers;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

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
import java.util.Collections;

public class JSONrequest {
    Context con1=null;
    static String param;
    static String podparam;
public int x;

    Context context;
    String Id = "", Exception = "";
    String output="";
GoogleMap mMap=null;
Layers la;
String promien;
static LatLng position;
static LatLng position2;

public static HttpAsyncTask2 mTask;


    public void Send(GoogleMap googleMap,Context con,String r,LatLng pos, String parametr,String podparametr, String Parametr_name) {
Log.d("TAG",Parametr_name);

        mTask=new HttpAsyncTask2();
        position=pos;
        param=parametr;
        promien=r;
        con1=con;
        mMap=googleMap;
        podparam=podparametr;
        String erid = " ";
        mTask.execute("https://notif2.sng.com.pl/api/MobileAppGetPolilines");
    }


    public void Send2(GoogleMap googleMap,Context con,String r,LatLng pos1,LatLng pos2, String parametr,String podparametr, String Parametr_name) {
        Log.d("TAG",Parametr_name);

        mTask=new HttpAsyncTask2();
        position=pos1;
        position2=pos2;
        param=parametr;
        promien=r;
        con1=con;
        mMap=googleMap;
        podparam=podparametr;
        String erid = " ";
        // new JSONrequest.HttpAsyncTask2().execute("https://notif2.sng.com.pl/api/MobileAppGetPolilines");
        mTask.execute("https://notif2.sng.com.pl/api/MobileAppGetPolilines");
    }


    public String POST(String url){
        InputStream inputStream = null;
        String result = "";
            try {
                Log.d("TAG", "**********************");
                Log.d("TAG", "POST");
                Log.d("TAG", param);
                Log.d("TAG",podparam);
                Log.d("TAG", "**********************");

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                String json = "";
                JSONObject jsonObject = new JSONObject();
                        jsonObject.put("Lati", Double.toString(position.longitude));
                        jsonObject.put("Longi", Double.toString(position.latitude));
                        jsonObject.put("Lati2", Double.toString(position.longitude));
                        jsonObject.put("Longi2", Double.toString(position.latitude));
                        jsonObject.put("Promien", promien);
                        jsonObject.put("Param", param);
                        jsonObject.put("Podparam", podparam);
                Log.d("sprawdzam 2",param + " and "+podparam);
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
        Log.d("TAG", "**********************");
        Log.d("TAG", "res");
        Log.d("TAG", result);
        Log.d("TAG", "**********************");
            return result;
        }


    ArrayList<ArrayList<LatLng>> MSLINK_value;
    ArrayList<String> MSLINK_label;
    ArrayList<String> MSLINK_kolor;
    ArrayList<String> MSLINK_podparametr;
    ArrayList<LatLng>  punkty;
    ArrayList<String> Type;

    String type="";

    private class HttpAsyncTask2 extends AsyncTask<String, Void, String> {
        AlertDialog alertDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            alertDialog=new AlertDialog.Builder(con1)
                    .setTitle("Proszę czekać ")
                    .setMessage("Pobieranie danych ...")
                    .setIcon(android.R.drawable.ic_input_add)
                    .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mTask.cancel(true);
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
        Layers la =new Layers();
        protected String doInBackground(String... urls) {
            if(!isCancelled()) {
                Log.d("TAG", "Awaria 0");
                String result = POST(urls[0]);

                MSLINK_value = new ArrayList<ArrayList<LatLng>>();
                MSLINK_label = new ArrayList<String>();
                MSLINK_kolor = new ArrayList<String>();
                MSLINK_podparametr = new ArrayList<String>();
                Type = new ArrayList<String>();
                LatLng location;
                String Mslink = "";
                String Latlong = "";
                String Kolor = "";
                String Podparam = "";
                String[] separated;
                JSONArray array = null;

                try {
                    array = new JSONArray(result);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        Mslink = row.getString("Mslink");
                        Latlong = row.getString("LatLong");
                        Kolor = row.getString("Printstyle");
                        Podparam = row.getString("Param");
                        type = row.getString("Type");
                        separated = Latlong.split(";");
                        punkty = new ArrayList<LatLng>();

                        for (int j = 0; j < separated.length; j++) {
                            String[] sep = separated[j].split(",");
                            location = new LatLng(Double.parseDouble(sep[0]), Double.parseDouble(sep[1]));
                            punkty.add(location);
                        }
                        MSLINK_label.add(Mslink);
                        MSLINK_value.add((punkty));
                        MSLINK_kolor.add(Kolor);
                        MSLINK_podparametr.add(Podparam);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException ne) {
                    Log.d("TAG", "Awaria 1");
                    result = null;
                }
                return result;
                //return POST(urls[0]);
            }
            else{
                Log.d("Async","Proba zatrzymania");
                return null;
            }
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
            Log.d("Anulowanie","ANULOWANO ASYNCTASK !");
        }

        @Override
        protected void onPostExecute(String result) {

            if(result==null){
                alertDialog.dismiss();
                new AlertDialog.Builder(con1)
                        .setTitle("Błąd")
                        .setMessage("Nie można wczytać danych. Sprawdź połączenie z Internetem")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setCancelable(true)
                        .show();
            }
            else{
                if(mMap!=null){

                        if (type.equals("Point")) {
                            Log.d("sprawdzam",param + " and "+podparam);
                            Log.d("TAG","PRZECHODZE DO GET PHOTO");
                            la.Rysuj_punkt(mMap,con1, MSLINK_value, MSLINK_label, MSLINK_kolor, MSLINK_podparametr, param, podparam);

                        } else {
                            try{
                            Log.d("TAG", "rysuj linie");
                            Log.d("TAG", MSLINK_label.get(0) + MSLINK_kolor.get(0) + MSLINK_podparametr.get(0) + param + podparam);
                            Log.d("TAG", "**********************");

                            la.Rysuj_linie(mMap,con1, MSLINK_value, MSLINK_label, MSLINK_kolor, MSLINK_podparametr, param, podparam);}
                            catch (IndexOutOfBoundsException ne){
                                Toast.makeText(con1,"Brak obiektu w wybranym obszarze",
                                        Toast.LENGTH_LONG).show();
                            }
                        }

            }
            alertDialog.dismiss();
            }
        }
    }


    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null) result += line;
        inputStream.close();
        return result;
    }

}