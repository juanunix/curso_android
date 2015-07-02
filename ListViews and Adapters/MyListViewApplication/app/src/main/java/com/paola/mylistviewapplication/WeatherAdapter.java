package com.paola.mylistviewapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherAdapter extends ArrayAdapter<Weather> {

    Context context;
    int layoutResourceId;    
    Weather data[] = null;
    
    public WeatherAdapter(Context context, int layoutResourceId, Weather[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WeatherHolder holder = null;
        
        if(convertView == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new WeatherHolder();
            holder.imgIcon = (ImageView)convertView.findViewById(R.id.imageView);
            holder.txtDate = (TextView)convertView.findViewById(R.id.textView1);
            holder.temp = (TextView)convertView.findViewById(R.id.textView2);
            convertView.setTag(holder);
        }
        else
        {
            holder = (WeatherHolder)convertView.getTag();
        }
        
        Weather weather = data[position];
        holder.txtDate.setText(weather.date);
        holder.temp.setText(weather.temp);
        holder.imgIcon.setImageResource(weather.icon);
        
        return convertView;
    }
    
    static class WeatherHolder
    {
        ImageView imgIcon;
        TextView txtDate;
        TextView temp;
    }
}