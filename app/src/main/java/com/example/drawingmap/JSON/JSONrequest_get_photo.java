package com.example.drawingmap.JSON;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.drawingmap.Obiekt_photo;
import com.example.drawingmap.ZarzadcaBazyPhoto;

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

public class JSONrequest_get_photo {
    Context con1=null;

public int x;
    Context context;
    String Id = "", Exception = "";
    String output="";
    public static String photo_id=null;

    public static ArrayList<String> Photo_id;
    public static ArrayList<String> Photo;


    public void Send(Context con,String _photo_id) {
        con1=con;
        photo_id=_photo_id;
        String erid = " ";
        new JSONrequest_get_photo.HttpAsyncTask2().execute("https://notif2.sng.com.pl/api/MobileAppGetPhoto");
    }



    public String POST(String url){
        InputStream inputStream = null;
        String result = "";
        try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                String json = "";
                JSONObject jsonObject = new JSONObject();
                        jsonObject.put("photo_id","all");

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

            Log.d("TAG","Jestem w getphoto");


            super.onPreExecute();
            alertDialog=new AlertDialog.Builder(con1)
                .setTitle("Proszę czekać ")
                .setMessage("Pobieranie z bazy ...")
                .setIcon(android.R.drawable.ic_menu_send)
                .setCancelable(false)
                .show();
        }
        protected String doInBackground(String... urls) {

            String result=POST(urls[0]);
            Photo=new ArrayList<String>();
            Photo_id=new ArrayList<String>();

            ZarzadcaBazyPhoto zbphoto=new ZarzadcaBazyPhoto(con1);
            Obiekt_photo obiekt_photo = new Obiekt_photo();
            zbphoto.sprawdzCzyIstniejeTabela();



            Log.d("TAG", "**********************");
            Log.d("TAG", "PA TERA");
            Log.d("TAG", result);

            String res=null;

            JSONArray array = null;
            try {
                array = new JSONArray(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                Log.d("Bazka","rozmiar tablicy"+Integer.toString(array.length()));
                for (int i = 0; i <array.length(); i++) {
                    JSONObject row = array.getJSONObject(i);
                    Log.d("JSONROW",row.getString("photo_id"));
                    Photo_id.add(row.getString("photo_id"));
                    Photo.add(row.getString("photo"));

                    obiekt_photo.setphoto_id(row.getString("photo_id"));
                    obiekt_photo.setphoto(row.getString("photo"));

                    zbphoto.dodajPhoto(obiekt_photo);
                    Log.d("Bazka",row.getString("photo_id")+" "+row.getString("photo"));
                    Log.d("Test","dodano zdjecie do bazy");
                }

            }catch(JSONException je){

            }

            Log.d("TAG", "**********************");
            return null;
        }
        @Override
        protected void onPostExecute(String result) {

            alertDialog.dismiss();

            /*
            byte[] decodedString = Base64.decode(res, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            AlertDialog.Builder dialogBuilder1 = new AlertDialog.Builder(con1);
            dialogBuilder1.setNegativeButton("Powrót",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    })
                    .setNeutralButton("Rysuj", null)
                    .setPositiveButton("Wyślij", null);



            LayoutInflater inflater1 = (LayoutInflater) con1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogView1 = inflater1.inflate(R.layout.layers_test_bitmap, null);
            dialogBuilder1.setView(dialogView1);

            final ImageView img=(ImageView) dialogView1.findViewById(R.id.image1);

            img.setImageBitmap(decodedByte);
           // img.setImageBitmap(reducedSizeBitmap);
            final AlertDialog alertDialog = dialogBuilder1.create();
            alertDialog.show();
*/



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