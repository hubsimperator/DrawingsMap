package com.example.drawingmap.JSON;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JSONrequest_insert_photo {
    Context con1=null;

public int x;
    Context context;
    String Id = "", Exception = "";
    String output="";
    public static String raw_photo=null;
    public static String parametr;
    public static String podparametr;
    public static String mslink;
    public static String latitude;
    public static String longitude;


    public void Send(Context con,String photo,String _parametr,String _podparametr,String _mslink,String _latitude,String _longitude) {
        con1=con;
        raw_photo=photo;
        parametr=_parametr;
        podparametr=_podparametr;
        mslink=_mslink;
        latitude=_latitude;
        longitude=_longitude;
        String erid = " ";
        Log.d("SEND",Integer.toString(raw_photo.length()));


        new JSONrequest_insert_photo.HttpAsyncTask2().execute("https://notif2.sng.com.pl/api/MobileAppInsertPhoto");
    }



    public String POST(String url){
        InputStream inputStream = null;
        String result = "";
        Log.d("Test przyesylania",parametr);
        Log.d("Test przyesylania",podparametr);
        Log.d("Test przyesylania",mslink);

        try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                String json = "";
                JSONObject jsonObject = new JSONObject();
                        jsonObject.put("image_raw",raw_photo);
            jsonObject.put("parametr",parametr);
            jsonObject.put("podparametr",podparametr);
            jsonObject.put("mslink",mslink);
            jsonObject.put("latitude",latitude);
            jsonObject.put("longitude",longitude);

            //tutaj wrzucasz elementy json
                jsonObject.accumulate("Id", Id);
                jsonObject.accumulate("Exception", Exception);
                json = jsonObject.toString();
                StringEntity se = new StringEntity(json);
                httpPost.setEntity(se);
                httpPost.setHeader("Content-type", "application/json;charset=utf-8");
         //       httpPost.setHeader("Content-type", "charset=utf-8");

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


    private class HttpAsyncTask2 extends AsyncTask<String, Void, String> {
        AlertDialog alertDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog=new AlertDialog.Builder(con1)
                .setTitle("Proszę czekać ")
                .setMessage("Wysyłanie danych ...")
                .setIcon(android.R.drawable.ic_menu_send)
                .setCancelable(false)
                .show();


        }
        protected String doInBackground(String... urls) {



            return POST(urls[0]);
        }
        @Override
        protected void onPostExecute(String result) {

            Log.d("TAG", "**********************");
            Log.d("TAG", "POSTexect");
            Log.d("TAG", "pa tera");

            Log.d("TAG", result);
            Log.d("TAG", "**********************");
            alertDialog.dismiss();
            Toast.makeText(con1,"Zdjęcie zostało przesłane",
                    Toast.LENGTH_LONG).show();
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