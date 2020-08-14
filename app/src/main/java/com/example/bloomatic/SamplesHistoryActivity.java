package com.example.bloomatic;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class SamplesHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference mRef;
    private ArrayList<Sample> mSamples;

    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;

    private ImageView graphBtn;


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideToolbar();
        setContentView(R.layout.activity_samples_history);
        mRef = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.recycle);
        graphBtn = findViewById(R.id.graphBtn);

        graphBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SamplesHistoryActivity.this,SamplesGraph.class);
                startActivity(intent);
            }
        });

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
        fetch();

    }

    private void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("SensorSamples").limitToLast(20);

        FirebaseRecyclerOptions<Sample> options =
                new FirebaseRecyclerOptions.Builder<Sample>()
                        .setQuery(query, new SnapshotParser<Sample>() {
                            @NonNull
                            @Override
                            public Sample parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new Sample(snapshot.child("soilhumidity").getValue().toString(),
                                        snapshot.child("hum").getValue().toString(),
                                        snapshot.child("temperature").getValue().toString(),
                                        snapshot.child("timestampSTR").getValue().toString());
                            }
                        })
                        .build();
        adapter = new FirebaseRecyclerAdapter<Sample, ViewHolder>(options) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_sample, parent, false);

                return new ViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(ViewHolder holder, final int position, Sample model) {
                holder.setHumVal(model.getHumiditySampleValue());
                holder.setSoilHumVal(model.getSoilHumiditySampleValue());
                holder.setTemperatureVal(model.getTemperature());
                holder.setTimestamp(model.getTimeStamp());


//                holder.root.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(SamplesHistoryActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
//                    }
//                });
            }

        };
        recyclerView.setAdapter(adapter);
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
