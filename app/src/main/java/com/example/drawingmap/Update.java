package com.example.drawingmap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.FileProvider;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


public class Update extends Activity {

    public static File saveFile;

    // Progress Dialog
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;


    // File url to download
    public static String file_url;//=extras.getString("BookingId"); //= "https://nc.sng.com.pl/index.php/s/JXfBTtXBciLK2AI/download?path=%2F&files=CarSharing.apk";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras= getIntent().getExtras();
        file_url=extras.getString("url");

        setContentView(R.layout.activity_maps);

        new HttpAsyncTask2().execute(file_url);

    }

    private class HttpAsyncTask2 extends AsyncTask<String, String, String> {
        AlertDialog alertDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }


        @Override
        protected String doInBackground(String... urls) {
            int count;
            try {
                URL url = new URL(urls[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                String ppath=Environment
                        .getExternalStorageDirectory().toString();

                Log.d("Path aktualizacji",ppath);
                File folder = Update.this.getFilesDir();
                String path = folder.getAbsolutePath() + "/" + "hubert";
                saveFile = new File(path);
                saveFile.createNewFile();
                OutputStream output = new FileOutputStream(saveFile,false);

              /*  // Output stream
                OutputStream output = new FileOutputStream(Environment
                        .getExternalStorageDirectory().toString()
                        + "/aktualizacjaCarsharing.apk");

               */

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                   publishProgress("" + Integer.toString((int)((total * 100) / lenghtOfFile)));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {

            }

            return null;
        }

        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String result) {
            dismissDialog(progress_bar_type);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                saveFile.setReadable(true, false);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(saveFile), "application/vnd.android.package-archive");
                Update.this.getApplicationContext().startActivity(intent);
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri fileUri = FileProvider.getUriForFile(Update.this,
                        "ir.mhdr.provider",
                        saveFile);
                intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
            }

            //fraszka dla daniela
            /*

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(Environment
                    .getExternalStorageDirectory().toString()
                    + "/aktualizacjaCarsharing.apk")), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

             */
        }
    }

    /**
     * Showing Dialog
     * */

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Pobieranie aktualizacji. Proszę czekać...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

}