package com.example.drawingmap.JSON;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.drawingmap.Layers;
import com.example.drawingmap.Obiekt_LayerToDrawing;
import com.example.drawingmap.Obiekt_ParamandPodparam;
import com.example.drawingmap.Obiekt_warstwa;
import com.example.drawingmap.layers_dialog_db;
import com.example.drawingmap.layers_dialog_db2;
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
import java.util.HashMap;
import java.util.LinkedHashSet;

public class JSON_wartswy_do_wyswietlenia {
    Context con1=null;
    GoogleMap mMap=null;
    String promien=null;
    LatLng Position;

    ArrayList<Obiekt_warstwa> lista_warstw;

    String Id = "", Exception = "";
    public void Send(GoogleMap googleMap,Context con,final String prom, final LatLng POIPosition) {
        con1=con;
        mMap=googleMap;
        promien=prom;
        Position=POIPosition;
        new JSON_wartswy_do_wyswietlenia.HttpAsyncTask2().execute("https://notif2.sng.com.pl/api/MobileAppGetParamsAndPodparams");
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

    private class HttpAsyncTask2 extends AsyncTask<String, Void, ArrayList<Obiekt_warstwa>> {
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
        protected ArrayList<Obiekt_warstwa> doInBackground(String... urls) {

            lista_warstw=new ArrayList<>();
            ArrayList<String> _parametrNazwa=new ArrayList<>();
            ArrayList<String> _parametrId=new ArrayList<>();
            String result=POST(urls[0]);
            JSONArray array = null;
            try {
                array = new JSONArray(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                HashMap<Integer,Obiekt_ParamandPodparam> mapPodparam=new HashMap<>();
                for (int i = 0; i <array.length(); i++) {
                    JSONObject row = array.getJSONObject(i);
                    String paramName=row.getString("Param");
                    String paramId=row.getString("ParamId");
                    String podparamName=row.getString("Podparam");
                    String podparamId=row.getString("PodparamId");
                    _parametrNazwa.add(paramName);
                    _parametrId.add(paramId);
                    Obiekt_ParamandPodparam obiekt_paramandPodparams=new Obiekt_ParamandPodparam(paramId,paramName,podparamId,podparamName);
                    mapPodparam.put(Integer.valueOf(paramId),obiekt_paramandPodparams);
                }

                ArrayList<Obiekt_ParamandPodparam> obiekt_paramandPodparams;

                ArrayList<String> parametrNazwa_uniqe = new ArrayList<String>(new LinkedHashSet<String>(_parametrNazwa));
                ArrayList<String> parametrId_uniqe = new ArrayList<String>(new LinkedHashSet<String>(_parametrId));

                for(int i=0;i<parametrId_uniqe.size();i++){
                    Obiekt_warstwa obiekt_warstwa=new Obiekt_warstwa(parametrNazwa_uniqe.get(i),parametrId_uniqe.get(i));
                    lista_warstw.add(obiekt_warstwa);
                }

            }
            catch (JSONException e) {                e.printStackTrace();
            }
            catch (NullPointerException ne){
                result=null;
            }
            return lista_warstw;
        }
        @Override
        protected void onPostExecute(ArrayList<Obiekt_warstwa> result) {
            layers_dialog_db2 layers_dialog_db2=new layers_dialog_db2();
            //    laye layers_dialog_db =new layers_dialog_db2();
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
                layers_dialog_db2.layers_dialog(mMap,con1,promien,Position,lista_warstw);
              //  ldb.layers_dialog(mMap,con1,promien,Position,Layer_uniqe,ParamId_uniqe);
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