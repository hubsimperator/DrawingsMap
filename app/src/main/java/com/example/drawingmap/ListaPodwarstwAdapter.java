package com.example.drawingmap;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

class ListaPodwarstwAdapter extends ArrayAdapter<Obiekt_podparam> {

    private Context mContext;
    int mResource;
    HashMap<Integer,Boolean> podparam_click=new HashMap<>();

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String nazwa=getItem(position).getPodparamName();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView=inflater.inflate(mResource,parent,false);
        TextView warstwa_tv=(TextView) convertView.findViewById(R.id.nazwa_warstwy_tv);
        LinearLayout llayout =(LinearLayout) convertView.findViewById(R.id.warstwa_layout);
        if(podparam_click.get(position+1)) llayout.setBackgroundColor(Color.parseColor("#B0E0E6"));
        else llayout.setBackgroundColor(Color.TRANSPARENT);

//        llayout.setBackgroundColor( Color.parseColor("#B0E0E6"));
        warstwa_tv.setText(nazwa);


        return convertView;
       // return super.getView(position, convertView, parent);
    }

    public ListaPodwarstwAdapter(Context context, int resource, ArrayList<Obiekt_podparam> objects, Context Context,HashMap<Integer,Boolean> _podparam_click) {
        super(context, resource, objects);
        podparam_click=_podparam_click;
        mContext = Context;
        mResource=resource;
    }
}

