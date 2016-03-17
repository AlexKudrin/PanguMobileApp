package com.example.aleksejs.pangumobileapp;

/**
 * Created by Aleksejs on 16/03/2016.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String> {
    ArrayList<String> modelsName;
    ArrayList<String> modelsDesc;
    Context context;
    ArrayList<Bitmap> modelsImages;
    private static LayoutInflater inflater=null;
    public CustomAdapter(MainActivity mainActivity, ArrayList<String> modelsName, ArrayList<Bitmap> modelsImages, ArrayList<String> modelsDesc) {
        // TODO Auto-generated constructor stub
        super(mainActivity, R.layout.list_item, modelsName);

        context=mainActivity;
        this.modelsName = modelsName;
        this.modelsDesc = modelsDesc;
        this.modelsImages = modelsImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return modelsName.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    public class Holder
    {
        TextView name;
        TextView desc;
        ImageView img;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.list_item, parent, false);
        holder.name=(TextView) rowView.findViewById(R.id.model_name);
        holder.img=(ImageView) rowView.findViewById(R.id.model_image);
        holder.desc=(TextView) rowView.findViewById(R.id.model_description);
        holder.name.setText(modelsName.get(position));
        holder.desc.setText(modelsDesc.get(position));
        rowView.setTag(holder);
        Log.v("name : ", modelsName.get(position));
        holder.img.setImageBitmap(modelsImages.get(position));
        return rowView;
    }

}