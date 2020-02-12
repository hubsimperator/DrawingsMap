package com.example.drawingmap.JSON;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.example.drawingmap.Obiekt_LayerToDrawing;
import com.example.drawingmap.Obiekt_podparam;
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

public class JSON_getpodparams {
    Context con1=null;
     String param;
     String podparam;
    String Id = "", Exception = "";
    GoogleMap mMap=null;
    LatLng coor1;
    LatLng coor2;
    ArrayList<Obiekt_podparam> lista_podparametrow=new ArrayList<>();
    String parametr;
    String paramName;

public static HttpAsyncTask2 mTask;

    public void Send2(String _parametr,String _paramName,Context con,GoogleMap gm) {
        con1=con;
        mMap=gm;
        mTask=new HttpAsyncTask2();
        parametr=_parametr;
        paramName=_paramName;
        mTask.execute("https://notif2.sng.com.pl/api/MobileAppGetPodparams");
    }

    public String POST(String url){
        InputStream inputStream = null;
        String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                String json = "";
                JSONObject jsonObject = new JSONObject();
                        jsonObject.put("ParamId", parametr);
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

    private class HttpAsyncTask2 extends AsyncTask<String, Void, ArrayList<Obiekt_podparam>> {
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

        protected ArrayList<Obiekt_podparam> doInBackground(String... urls) {
                String result = POST(urls[0]);
                JSONArray array = null;
                try {
                    array = new JSONArray(result);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    for (int i = 0; i < array.length(); i++) {
                            JSONObject row = array.getJSONObject(i);
                            String PodparamId = row.getString("PodparamId");
                            String PodparamName = row.getString("Podparam");
                            Obiekt_podparam obiekt_podparam = new Obiekt_podparam(PodparamId,PodparamName,parametr,paramName);
                            lista_podparametrow.add(obiekt_podparam);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException ne) {
                    result = null;
                }

                return lista_podparametrow;
        }

        @Override
        protected void onPostExecute(ArrayList<Obiekt_podparam> result) {
            alertDialog.dismiss();

            if(result.size()<1){
                new AlertDialog.Builder(con1)
                        .setTitle("Błąd")
                        .setMessage("Brak obiektów do wyświetlenia")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setCancelable(true)
                        .show();
            }
            else{
                if(mMap!=null){
                    layers_dialog_db2 layers_dialog_db2=new layers_dialog_db2();
                    layers_dialog_db2.wyswietl_podparametry(result);
            }
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