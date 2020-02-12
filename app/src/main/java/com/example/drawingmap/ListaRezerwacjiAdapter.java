package com.example.drawingmap;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

class ListaWarstwAdapter extends ArrayAdapter<Obiekt_warstwa> {

    private Context mContext;
    int mResource;
    HashMap<Integer,Boolean> switch_avaibility=new HashMap<>();

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String nazwa=getItem(position).getLayerName();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView=inflater.inflate(mResource,parent,false);

        TextView warstwa_tv=(TextView) convertView.findViewById(R.id.nazwa_warstwy_tv);


        LinearLayout llayout =(LinearLayout) convertView.findViewById(R.id.warstwa_layout);
        if(switch_avaibility.get(position+1)) llayout.setBackgroundColor(Color.GREEN);
        else llayout.setBackgroundColor(Color.TRANSPARENT);


            warstwa_tv.setText(nazwa);

        return convertView;
       // return super.getView(position, convertView, parent);
    }

    public ListaWarstwAdapter(Context context, int resource, ArrayList<Obiekt_warstwa> objects, Context Context, HashMap<Integer,Boolean> _switch_avaibility) {
        super(context, resource, objects);
        switch_avaibility=_switch_avaibility;
        mContext = Context;
        mResource=resource;
    }
}

