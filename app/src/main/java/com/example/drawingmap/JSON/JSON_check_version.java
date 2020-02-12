package com.example.drawingmap.JSON;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;


import com.example.drawingmap.SplashScreen;
import com.example.drawingmap.Update;

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

public class JSON_check_version {

    Context con = null;
    String Wersja ="";
    TextView er;
    public void StartUpdate(String version, Context context) {
        con = context;
        Wersja = version;
        new JSON_check_version.HttpAsyncTask2().execute("https://notif2.sng.com.pl/api/MobileAppCheckVersion");
    }

    public String POST(String url) {
        InputStream inputStream = null;
        String result = "";
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("Wersja",Wersja);
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
        }
        return result;
    }
    private class HttpAsyncTask2 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }


        @Override
        protected void onPostExecute(final String result) {
            Boolean status;
            if(result.contains("True"))
            {
                status=true;
            }
            else
            {
                status=false;
                }

            SplashScreen splashScreen=new SplashScreen();
            splashScreen.goToMap(status,con);


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