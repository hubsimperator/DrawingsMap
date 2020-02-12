package com.example.blanki;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

public class JSONrequest {
    Context context;
    String Id = "", Exception = "";
    String output="";

    public void Send(void) {
      //  context = con;
        String erid = " ";
        new JSONrequest.HttpAsyncTask2().execute("https://notif2.sng.com.pl/api/MobileAppGetWodkan");
    //    return output;
    }

    public String POST(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Lati", "18.842119216918945");
            jsonObject.put("Longi", "54.339080810546875");
            jsonObject.put("Promien", "0.001");
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


    private class HttpAsyncTask2 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }
        @Override
        protected void onPostExecute(String result) {
            Log.d("TAG", "**********************");
            Log.d("TAG", "RESULT");
            Log.d("TAG", result);
            Log.d("TAG", "**********************");
            output=result;
            String Mslink="";
            String Latlong="";
            JSONArray array = null;
            try {
                array = new JSONArray(result);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject row = array.getJSONObject(i);
                   // JSONArray jArray = row.getJSONArray("Mslink");

                    Mslink = row.getString("Mslink");
                    Latlong = row.getString("LatLong");
                    Log.d("TAG", "**********************");
                    Log.d("TAG", "wyjscie");
                    Log.d("TAG", Mslink);
                    Log.d("TAG", Latlong);
                    Log.d("TAG", "**********************");
                //    JSONObject json_data = jArray.getJSONObject(i);
                }
            }
            catch (JSONException e) {                e.printStackTrace();
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