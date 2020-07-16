package com.example.bloomatic;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddIrrigation extends AppCompatActivity {
    private int hours;
    private int minutes;
    private int day;
    private int month;
    private int year;
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("UpcomingIrrigations");

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
                ((DatePickerForNewIrrigation)datePicker).setEditText(dateEditText);
                datePicker.show(getSupportFragmentManager(),"datePicker");
            }
        });
        final EditText timeEditText = findViewById(R.id.editTextTime);
        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerForNewIrrigation();
                ((TimePickerForNewIrrigation)timePicker).setEditText(timeEditText);
                timePicker.show(getSupportFragmentManager(),"timePicker");

            }
        });
        final EditText waterAmountEditText = findViewById(R.id.editTextWaterAmount);
        ImageView addIrrigationBtn = findViewById(R.id.addIrrigationButton);
        addIrrigationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dateEditText.getText().equals("") || timeEditText.getText().equals("") || waterAmountEditText.getText().equals(""))
                    Toast.makeText(getApplicationContext(),"Fulfill fields",Toast.LENGTH_SHORT).show();
                else{
                    // convert to unix epoch & push to firebase
                    int f = dateEditText.getText().toString().indexOf("/");
                    int l = dateEditText.getText().toString().lastIndexOf("/");
                    day = Integer.parseInt(dateEditText.getText().toString().substring(0,f));
                    month = Integer.parseInt(dateEditText.getText().toString().substring(f+1,l));
                    year = Integer.parseInt(dateEditText.getText().toString().substring(l+1));
                    int c = timeEditText.getText().toString().indexOf(":");
                    hours = Integer.parseInt(timeEditText.getText().toString().substring(0,c));
                    minutes = Integer.parseInt(timeEditText.getText().toString().substring(c+1));

                    System.out.println("Time: "+day+ " "+month+ " "+ year + " " + hours + " " + minutes);


                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR,year);
                    calendar.set(Calendar.MONTH,month);
                    calendar.set(Calendar.DAY_OF_MONTH,day);
                    calendar.set(Calendar.HOUR,hours);
                    calendar.set(Calendar.MINUTE,minutes);
                    calendar.set(Calendar.SECOND,0);
                    calendar.set(Calendar.MILLISECOND,0);
                    long epoch = calendar.getTimeInMillis()/1000L;

                    System.out.println(epoch);

                    Irrigation irrigation = new Irrigation("Open",waterAmountEditText.getText().toString(),
                            epoch);
                    mRef.child(String.valueOf(epoch)).setValue(irrigation);

                }
            }
        });


    }

    public void hideToolbar() {
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
    }
}