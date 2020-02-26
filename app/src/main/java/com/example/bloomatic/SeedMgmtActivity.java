package com.example.bloomatic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SeedMgmtActivity extends AppCompatActivity {

    private DatabaseReference mRef;
    String isIrrigating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideToolbar();
        setContentView(R.layout.activity_seed_mgmt);

        /// Set information pop-up window
        ImageView i1 = findViewById(R.id.imageView20);
        ImageView i2 = findViewById(R.id.imageView19);
        ImageView i3 = findViewById(R.id.imageView18);
        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInfoSamplesWindow(SeedMgmtActivity.this);

            }
        });
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInfoSamplesWindow(SeedMgmtActivity.this);

            }
        });
        i3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInfoSamplesWindow(SeedMgmtActivity.this);

            }
        });


        final TextView quickIrrigation_txt = findViewById(R.id.textView_quickIrr);
        mRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference lastSample = mRef.child("SensorSamples");
        lastSample.orderByKey().limitToLast(1).addChildEventListener(new ChildEventListener() {
            TextView soilH = findViewById(R.id.soilHumVal);
            TextView hum = findViewById(R.id.humVal);
            TextView temp = findViewById(R.id.tempVal);
            TextView date = findViewById(R.id.sampleDate_txt);

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                soilH.setText(dataSnapshot.child("soilhumidity").getValue().toString() + "%");
                hum.setText(dataSnapshot.child("hum").getValue().toString() + "%");
                temp.setText(dataSnapshot.child("temperature").getValue().toString() + "°");
                date.setText("Latest samples " + dataSnapshot.child("timestamp").getValue().toString());

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference lastIrr = mRef.child("Irrigations");
        lastIrr.orderByKey().limitToLast(1).addChildEventListener(new ChildEventListener() {
            TextView date = findViewById(R.id.irrDate);
            TextView status = findViewById(R.id.irrStatus);

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                date.setText(dataSnapshot.child("timestamp").getValue().toString());
                status.setText(dataSnapshot.child("status").getValue().toString());

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Button's text follows isIrrigating status from server.
        // 1. isIrrigating = 'no' - change text to "Stop irrigation", performIrrigation() (change request to 'yes'
        // and change pause to 'no'.

        // 2. isIrrigating = 'yes' - change text to Quick irrigation, change request to 'no', pause to 'yes'
        mRef.child("Waterpump").child("isIrrigating").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                isIrrigating = dataSnapshot.getValue().toString();
                TextView quickIrrigation_txt = findViewById(R.id.textView_quickIrr);
                if (isIrrigating.equals("no")){
                    quickIrrigation_txt.setText("Stop irrigation");
                    performQuickIrrigation();

            }
                else {
                    quickIrrigation_txt.setText("Quick irrigation");


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void toSamplesHistoryActivity(View view) {
        Intent i = new Intent(this, SamplesHistoryActivity.class);
        startActivity(i);

    }

    public void openInfoSamplesWindow(Context c) {
        AlertDialog.Builder ad = new AlertDialog.Builder(c)
                .setView(R.layout.info_samples_window);
        ad.show();
    }

    public void hideToolbar() {
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
    }

    public void performQuickIrrigation() {
        final ImageView quickIrrigation_btn = findViewById(R.id.imageView12);
        final TextView quickIrrigation_txt = findViewById(R.id.textView_quickIrr);
        final DatabaseReference waterpumpRef = mRef.child("Waterpump");
        final DatabaseReference isIrrigating = waterpumpRef.child("isIrrigating");
        String status;
        String request;


        if (quickIrrigation_txt.getText().toString().equals("Quick irrigation")) {

            waterpumpRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("status").getValue().equals("okay")
                            && dataSnapshot.child("isIrrigating").getValue().equals("no")) {
                        if (dataSnapshot.child("request").getValue().equals("no")) {
                            Toast.makeText(getApplicationContext(), "Performing quick irrigation", Toast.LENGTH_SHORT).show();
                        }
                    }

                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            waterpumpRef.child("request").setValue("yes");
        } else { // stopping irrigation manually.
            waterpumpRef.child("request").setValue("no");
            waterpumpRef.child("pause").setValue("yes");
            Toast.makeText(getApplicationContext(), "Terminating irrigation", Toast.LENGTH_SHORT).show();

        }


    }


    public void openIrrCalendar(View view) {
        Intent i = new Intent(this, IrrigationCalendar.class);
        startActivity(i);
    }
}
