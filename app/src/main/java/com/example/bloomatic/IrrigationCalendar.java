package com.example.bloomatic;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
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

public class IrrigationCalendar extends AppCompatActivity {

    private CalendarView calendarView;
    private RecyclerView recyclerView;
    private DatabaseReference mRef;
    private ArrayList<Sample> mSamples;

    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    private ImageView addToolbar;

    // need for Firebase to listen from onStart() to onStop()
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
        setContentView(R.layout.activity_irrigations_calendar);
        hideToolbar();

        mRef = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.recycle_irrigations);
        addToolbar = findViewById(R.id.bottomToolBar);
        calendarView = findViewById(R.id.calendarView);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
        fetch();


        addToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddIrrigation();
            }
        });

    }

    public void openAddIrrigation(){
        Intent intent = new Intent(this,AddIrrigation.class);
        startActivity(intent);
    }
    private void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Irrigations");

        FirebaseRecyclerOptions<Irrigation> options =
                new FirebaseRecyclerOptions.Builder<Irrigation>()
                        .setQuery(query, new SnapshotParser<Irrigation>() {
                            @NonNull
                            @Override
                            public Irrigation parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new Irrigation(snapshot.child("status").getValue().toString(),
                                        snapshot.child("water_amount").getValue().toString(),
                                        snapshot.child("timestamp").getValue().toString());
                            }
                        })
                        .build();
        adapter = new FirebaseRecyclerAdapter<Irrigation, ViewHolder_irrigation>(options) {
            @Override
            public ViewHolder_irrigation onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_irrigation, parent, false);

                return new ViewHolder_irrigation(view);
            }


            @Override
            protected void onBindViewHolder(ViewHolder_irrigation holder, final int position, Irrigation model) {
                holder.setStatus(model.getStatus());
                holder.setTimestamp(model.getTimestamp());
                holder.setWaterAmout(model.getWater_amount());



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



    public void hideToolbar() {
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
    }
}
