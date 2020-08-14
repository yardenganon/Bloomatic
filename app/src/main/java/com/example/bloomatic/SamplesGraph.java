package com.example.bloomatic;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SamplesGraph extends AppCompatActivity {

    private LineChart mChart;
    private DatabaseReference mSamplesRef = FirebaseDatabase.getInstance().getReference().child("SensorSamples");
    private ImageView lastDayBtn, lastWeekBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideToolbar();
        setContentView(R.layout.activity_samples_graph);
        mChart = (LineChart) findViewById(R.id.chart1);

        final int samplesInterval = 60; // Every how many minutes samples are taken
        final int numberOfSamplesForDay = 24 * 60 / samplesInterval;
        final int numberOfSamplesForWeek = 7 * numberOfSamplesForDay;

        lastDayBtn = findViewById(R.id.dayGraphBtn);
        lastWeekBtn = findViewById(R.id.weekGraphBtn);

        setData(numberOfSamplesForDay, 20);
        mChart.animateX(1500);

        lastDayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData(numberOfSamplesForDay, 20);
                mChart.animateX(1500);
            }
        });
        lastWeekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData(numberOfSamplesForWeek, 20);
                mChart.animateX(1500);
            }
        });
    }
    private void setData(int count, int range) {
        final ArrayList<Entry> yValsHumidity = new ArrayList<>();
        final ArrayList<Entry> yValsSoilHumidity = new ArrayList<>();
        final ArrayList<Entry> yValsTemperature = new ArrayList<>();


        Query query = mSamplesRef.limitToLast(count);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Float humidity, soilHumidity, temperature;
                    humidity = (Float) child.child("hum").getValue(Float.class);
                    soilHumidity = (Float) child.child("soilhumidity").getValue(Float.class);
                    temperature = (Float) child.child("temperature").getValue(Float.class);

                    if (humidity != null && soilHumidity !=null && temperature != null) {
                        yValsHumidity.add(new Entry(i, humidity));
                        yValsSoilHumidity.add(new Entry(i, soilHumidity));
                        yValsTemperature.add(new Entry(i, temperature));
                    }
                    i++;
                }
                LineDataSet set1, set2, set3;
                set1 = new LineDataSet(yValsHumidity, "Humidity");
                set1.setColor(Color.GREEN);
                set1.setLineWidth(3f);
                set2 = new LineDataSet(yValsSoilHumidity, "Soil Humidity");
                set2.setColor(Color.BLUE);
                set2.setLineWidth(3f);
                set3 = new LineDataSet(yValsTemperature, "Temperature");
                set3.setColor(Color.CYAN);
                set3.setLineWidth(3f);
                LineData data = new LineData(set1,set2,set3);
                mChart.setData(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void hideToolbar()
    {
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
    }
}