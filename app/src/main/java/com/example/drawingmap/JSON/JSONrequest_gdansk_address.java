package com.example.drawingmap.JSON;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.drawingmap.AddressGdansk_dialog;
import com.example.drawingmap.MapsActivity;
import com.example.drawingmap.Obiekt_Adres;
import com.example.drawingmap.R;
import com.example.drawingmap.ZarzadcaBazy;
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

public class JSONrequest_gdansk_address {
    Context con1=null;
    GoogleMap mMap=null;
public static HttpAsyncTask2 mTask;

    String Id = "", Exception = "";
    public void Send(GoogleMap googleMap,Context con) {
        con1=con;
        mMap=googleMap;
        mTask=new HttpAsyncTask2();
      //  new JSONrequest_gdansk_address.HttpAsyncTask2().execute("https://notif2.sng.com.pl/api/MobileAppGetGdanskAddress");
        mTask.execute("https://notif2.sng.com.pl/api/MobileAppGetGdanskAddress");
    }


    public String POST(String url){
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
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

            Log.d("Test","Wchodze do input stream");

            if (inputStream != null) result = convertInputStreamToString(inputStream);
            else result = "Nie działa";
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return result;
    }

    ArrayList<String> Ulica;
    ArrayList<String> Numer;
    ArrayList<String> Latitude;
    ArrayList<String> Longitude;





    private class HttpAsyncTask2 extends AsyncTask<String, Void, String> {
        AlertDialog alertDialog;
            @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //MapsActivity ma =new MapsActivity();

           //LayoutInflater inflater = (LayoutInflater) con1.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            //View view = inflater.inflate( R.layout.activity_maps, null );


            // LayoutInflater inflater = (LayoutInflater)   con1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          //  View view =getLayoutInflater().inflate(R.layout.activity_maps,null);
           // LinearLayout lllabel = (LinearLayout) view.findViewById(R.id.linearlayoutlabelsid);

            alertDialog=new AlertDialog.Builder(con1)
                    .setTitle("Proszę czekać ")
                    .setMessage("Pobieranie danych ...")
                    .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mTask.cancel(true);
                        }
                    })
                    .setIcon(android.R.drawable.ic_input_add)
                    .setCancelable(false)
                    .show();
        }


        protected String doInBackground(String... urls) {
            if(!isCancelled()) {


                ZarzadcaBazy zb = new ZarzadcaBazy(con1);
                Obiekt_Adres adres = new Obiekt_Adres();

                if (!zb.sprawdzCzyIstniejeTabela()) {
                    zb.stworzTabeleAdresy();
                }

                Ulica = new ArrayList<String>();
                Numer = new ArrayList<String>();

                Latitude = new ArrayList<String>();
                Longitude = new ArrayList<String>();

                String result = POST(urls[0]);
                JSONArray array = null;
                try {
                    array = new JSONArray(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Log.d("Test", "Proba dodania");
                    // zb.UsunWszystkieRekordy();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        // Log.d("JSONROW",row.toString());
                        Ulica.add(row.getString("ulica"));
                        Numer.add(row.getString("numer"));
                        Latitude.add(row.getString("latitude"));
                        Longitude.add(row.getString("longitude"));
                        //baza
                        adres.setUlica(row.getString("ulica"));
                        adres.setNumer(row.getString("numer"));
                        adres.setLatitude(row.getString("latitude"));
                        adres.setLongitude(row.getString("longitude"));
                        zb.dodajAdres(adres);
                        Log.d("Test", "dodano adresy do bazy");
                    }
                    ArrayList<String> ulice = new ArrayList<>();
                    ulice = zb.dajNrMieszkania("Chodkiewicza");
                    result = "Dodano wszystko";

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException ne) {
                    result = null;
                }
                return result;
                //return POST(urls[0]);
            }
            else{
                return  null;
            }
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
            Log.d("Async","Anulowano");
        }

        @Override
        protected void onPostExecute(String result) {

            Log.d("Test","Proba dodania zakjonczna");
//            Log.d("Res",result);


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
                Log.d("TAG", Ulica.toString());
                Log.d("TAG", Numer.toString());
                Log.d("TAG", Latitude.toString());
                Log.d("TAG", Longitude.toString());
                Log.d("TAG", "**********************");

                //ldb.layers_dialog(mMap,con1,promien,Position,Layer_uniqe,ParamId_uniqe);
            }
            alertDialog.dismiss();
            AddressGdansk_dialog agd = new AddressGdansk_dialog();
            agd.WyswietlUlice();
        }
/*       Cursor k= (Cursor) zb.dajWszystkieAdres();
          while(k.moveToNext()){
              Log.d("TAG", "**********************");
              Log.d("TAG", "BAZAA DANYCH");
              Log.d("TAG", k.getString(0));
              Log.d("TAG", k.getString(1));
              Log.d("TAG", k.getString(2));

              Log.d("TAG", k.getString(3));


              Log.d("TAG", "**********************");
          }


 */
        }

//zb.dodajAdres()


    }


    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        Log.d("Test"," input stream");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null) result += line;
        inputStream.close();
        Log.d("Test res",result);

        return result;
    }

}