package com.example.drawingmap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

public class LocalSettings {
    public void Ustawienia(final Context con){
        Log.d("Licznik","Przekroczono 15 !!");
        new AlertDialog.Builder(con)
                .setTitle("Ustawienia")
                .setMessage("Czy chcesz usunąć lokalną bazę danych ?")
                .setCancelable(false)
                .setPositiveButton("Tak, Usuń lokalną bazę", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        con.deleteDatabase("baza_adresy.db");
                        con.deleteDatabase("baza_photo.db");
                    }
                })
                .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // con.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();


    }
}
