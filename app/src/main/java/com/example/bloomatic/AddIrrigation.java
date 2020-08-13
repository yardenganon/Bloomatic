package com.example.bloomatic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;

import java.util.Calendar;

public class AddIrrigation extends AppCompatActivity {
    private int hours;
    private int minutes;
    private int day;
    private int month;
    private int year;
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("UpcomingIrrigations");
    private FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_irrigation);
        hideToolbar();

        final EditText dateEditText = findViewById(R.id.editTextDate);
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerForNewIrrigation();
                ((DatePickerForNewIrrigation) datePicker).setEditText(dateEditText);
                datePicker.show(getSupportFragmentManager(), "datePicker");
            }
        });
        final EditText timeEditText = findViewById(R.id.editTextTime);
        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerForNewIrrigation();
                ((TimePickerForNewIrrigation) timePicker).setEditText(timeEditText);
                timePicker.show(getSupportFragmentManager(), "timePicker");

            }
        });
        final EditText waterAmountEditText = findViewById(R.id.editTextWaterAmount);
        ImageView addIrrigationBtn = findViewById(R.id.addIrrigationButton);
        addIrrigationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dateEditText.getText().equals("") || timeEditText.getText().equals("") || waterAmountEditText.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(), "Fulfill fields", Toast.LENGTH_SHORT).show();
                else {
                    // convert to unix epoch & push to firebase
                    int f = dateEditText.getText().toString().indexOf("/");
                    int l = dateEditText.getText().toString().lastIndexOf("/");
                    day = Integer.parseInt(dateEditText.getText().toString().substring(0, f));
                    month = Integer.parseInt(dateEditText.getText().toString().substring(f + 1, l));
                    year = Integer.parseInt(dateEditText.getText().toString().substring(l + 1));
                    int c = timeEditText.getText().toString().indexOf(":");
                    hours = Integer.parseInt(timeEditText.getText().toString().substring(0, c));
                    minutes = Integer.parseInt(timeEditText.getText().toString().substring(c + 1));

                    System.out.println("Time: " + day + " " + month + " " + year + " " + hours + " " + minutes);


                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    calendar.set(Calendar.HOUR, hours);
                    calendar.set(Calendar.MINUTE, minutes);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    final long epoch = calendar.getTimeInMillis() / 1000L;


                    final Long[] serverTimestamp = new Long[1]; // Server timestamp for now
                    DatabaseReference timeRef = FirebaseDatabase.getInstance().getReference().child("timestamp");
                    timeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            serverTimestamp[0] = (Long) dataSnapshot.getValue(); // Server timestamp for now
                            serverTimestamp[0] /= 1000L; // To seconds
                            long unixTime = System.currentTimeMillis() / 1000L; // Local now
                            long diff = calculateTimeDiff(unixTime, serverTimestamp[0]); // Diff between now local to now server
                            System.out.println("Chosen Epoch : "+epoch);
                            long timePlusOffset = epoch + diff - 43200L; // Add diff to chosen time & add 43200L - 12 Hours back offset
                            System.out.println("Epoch + diff : " + timePlusOffset);

                            Irrigation irrigation = new Irrigation("Open", Integer.parseInt(waterAmountEditText.getText().toString()),
                                    timePlusOffset);
                            mRef.child(String.valueOf(timePlusOffset)).setValue(irrigation);

                            Toast.makeText(getApplicationContext(), "Irrigation Scheduled", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddIrrigation.this,IrrigationCalendar.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    timeRef.setValue(ServerValue.TIMESTAMP); // Posting server timestamp to get it and calc diff
                }
            }
        });
    }

    public long calculateTimeDiff(long localEpoch, long serverEpoch) {
        System.out.println("Server : " + serverEpoch);
        System.out.println("Local : " + localEpoch);
        long diff = serverEpoch - localEpoch;
        System.out.println("Diff : " + (diff));

        return diff;
    }

    public void hideToolbar() {
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
    }
}