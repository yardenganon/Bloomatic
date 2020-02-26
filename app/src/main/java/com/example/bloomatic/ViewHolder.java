package com.example.bloomatic;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    public LinearLayout root;
    public TextView soilHumVal;
    public TextView humVal;
    public TextView temperatureVal;
    public TextView timestamp;

    public ViewHolder(View itemView) {
        super(itemView);
        soilHumVal = itemView.findViewById(R.id.soilHumidityext);
        humVal = itemView.findViewById(R.id.humidityText);
        temperatureVal = itemView.findViewById(R.id.temperatureText);
        timestamp = itemView.findViewById(R.id.timeText);
    }
    public void setTimestamp(String val) {
        timestamp.setText(val);
    }
    public void setSoilHumVal(String val) {
        soilHumVal.setText(val);
    }
    public void setHumVal(String val) {
        humVal.setText(val);
    }
    public void setTemperatureVal(String val) {
        temperatureVal.setText(val);
    }
}