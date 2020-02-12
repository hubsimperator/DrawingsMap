package com.example.drawingmap.JSON;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.drawingmap.Layers;
import com.example.drawingmap.layers_dialog_db;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

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
import java.util.LinkedHashSet;

public class JSONrequest_layers {
    Context con1=null;
    GoogleMap mMap=null;
    String promien=null;
    LatLng Position;

    String Id = "", Exception = "";
    public void Send(GoogleMap googleMap,Context con,final String prom, final LatLng POIPosition) {
        con1=con;
        mMap=googleMap;
        promien=prom;
        Position=POIPosition;
        new JSONrequest_layers.HttpAsyncTask2().execute("https://notif2.sng.com.pl/api/MobileAppGetParamsAndPodparams");
    }


    public String POST(String url){
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
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

    ArrayList<String> Layer;
    ArrayList<String> Layer_uniqe;
    ArrayList<String> ParamId;
    ArrayList<String> ParamId_uniqe;



    private class HttpAsyncTask2 extends AsyncTask<String, Void, String> {
        AlertDialog alertDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            alertDialog=new AlertDialog.Builder(con1)
                    .setTitle("Proszę czekać ")
                    .setMessage("Pobieranie danych ...")
                    .setIcon(android.R.drawable.ic_input_add)
                    .setCancelable(false)
                    .show();
        }
        Layers la =new Layers();
        protected String doInBackground(String... urls) {

            Layer=new ArrayList<String>();
            Layer_uniqe=new ArrayList<String>();

            ParamId=new ArrayList<String>();
            ParamId_uniqe=new ArrayList<String>();


            String result=POST(urls[0]);
            JSONArray array = null;
            try {
                array = new JSONArray(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                for (int i = 0; i <array.length(); i++) {
                    JSONObject row = array.getJSONObject(i);
                   // Log.d("JSONROW",row.toString());
                    Layer.add(row.getString("Param"));
                    ParamId.add(row.getString("ParamId"));
                }
                ArrayList<String> param_uniqe = new ArrayList<String>(new LinkedHashSet<String>(Layer));
                Layer_uniqe=param_uniqe;

                Layers la=new Layers();
                la.Parameters=Layer_uniqe;

                param_uniqe = new ArrayList<String>(new LinkedHashSet<String>(ParamId));
                ParamId_uniqe=param_uniqe;

            }
            catch (JSONException e) {                e.printStackTrace();
            }
            catch (NullPointerException ne){
                result=null;
            }
            return result;
            //return POST(urls[0]);
        }
        @Override
        protected void onPostExecute(String result) {

            layers_dialog_db ldb=new layers_dialog_db();

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
                Log.d("TAG", "**********************");
                Log.d("TAG", "Parametry te wykorzystam");
                Log.d("TAG", Layer_uniqe.toString());
                Log.d("TAG", ParamId_uniqe.toString());
                Log.d("TAG", "**********************");
                ldb.layers_dialog(mMap,con1,promien,Position,Layer_uniqe,ParamId_uniqe);
            }
            alertDialog.dismiss();
        }}
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