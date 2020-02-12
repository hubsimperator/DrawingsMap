package com.example.drawingmap.JSON;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.drawingmap.Layers;
import com.example.drawingmap.Obiekt_LayerToDrawing;
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

public class JSON_drawLayers {
    Context con1=null;
     String param;
     String podparam;
    String Id = "", Exception = "";
    GoogleMap mMap=null;
    LatLng coor1;
    LatLng coor2;
    ArrayList<Obiekt_LayerToDrawing> lista_obiektowWarstw;
    ArrayList<String> listaParametrow=new ArrayList<>();

public static HttpAsyncTask2 mTask;

    public void Send2(GoogleMap googleMap,Context con,LatLng pos1,LatLng pos2, ArrayList<String> parametr,String podparametr) {
        mTask=new HttpAsyncTask2();
        coor1=pos1;
        coor2=pos2;
        param="1";
        con1=con;
        mMap=googleMap;
        listaParametrow=parametr;
        podparam=podparametr;
        mTask.execute("https://notif2.sng.com.pl/api/MobileAppGetPolilines2");
    }

    public String POST(String url,String _parametr){
        InputStream inputStream = null;
        String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                String json = "";
                JSONObject jsonObject = new JSONObject();
                        jsonObject.put("Lati", Double.toString(coor1.longitude));
                        jsonObject.put("Longi", Double.toString(coor1.latitude));
                        jsonObject.put("Lati2", Double.toString(coor2.longitude));
                        jsonObject.put("Longi2", Double.toString(coor2.latitude));
                        jsonObject.put("Param", _parametr);
                        jsonObject.put("Podparam", podparam);
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

    private class HttpAsyncTask2 extends AsyncTask<String, Void, ArrayList<Obiekt_LayerToDrawing>> {
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

        protected ArrayList<Obiekt_LayerToDrawing> doInBackground(String... urls) {
            lista_obiektowWarstw =new ArrayList<>();
            for(int j=0;j<listaParametrow.size();j++){
                String result = POST(urls[0],listaParametrow.get(j));
                JSONArray array = null;
                try {
                    array = new JSONArray(result);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {


                    for (int i = 0; i < array.length(); i++) {
                   //     if(lista_obiektowWarstw.size()<100) {
                            JSONObject row = array.getJSONObject(i);
                            String Mslink = row.getString("Mslink");
                            String Latlong = row.getString("LatLong");
                            String Kolor = row.getString("Printstyle");
                            String Podparam = row.getString("Param");
                            String Angle = row.getString("Angle");
                            String type = row.getString("Type");
                            Obiekt_LayerToDrawing obiekt_layerToDrawing = new Obiekt_LayerToDrawing(Mslink, Latlong, Kolor, Podparam, listaParametrow.get(j), Angle, type);
                            lista_obiektowWarstw.add(obiekt_layerToDrawing);
                        //}else{
                       //     break;
                       // }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException ne) {
                    result = null;
                }
            }
                return lista_obiektowWarstw;
        }

        @Override
        protected void onPostExecute(ArrayList<Obiekt_LayerToDrawing> result) {
            if(result.size()<1){
                alertDialog.dismiss();
                new AlertDialog.Builder(con1)
                        .setTitle("Błąd")
                        .setMessage("Brak obiektów do wyświetlenia")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setCancelable(true)
                        .show();
            }
            else{
                if(mMap!=null){
                   la.RysujNaMapie(result,mMap,con1);
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