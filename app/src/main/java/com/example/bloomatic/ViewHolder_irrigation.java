package com.example.bloomatic;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class ViewHolder_irrigation extends RecyclerView.ViewHolder {
    public LinearLayout root;
    public TextView status;
    public TextView water_amount;
    public TextView timestamp;

    public ViewHolder_irrigation(View itemView) {
        super(itemView);
        status = itemView.findViewById(R.id.itemIrrStatus);
        water_amount = itemView.findViewById(R.id.itemIrrWaterAmount);
        timestamp = itemView.findViewById(R.id.itemIrrDate);
    }
    public void setTimestamp(String val) {
        timestamp.setText(val);
    }
    public void setStatus(String val) {
        status.setText(val);
    }
    public void setWaterAmout(String val) {

        water_amount.setText(val);
    }
}