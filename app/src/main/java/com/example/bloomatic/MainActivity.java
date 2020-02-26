package com.example.bloomatic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideToolbar();
        setContentView(R.layout.activity_main);
        ImageView about = findViewById(R.id.about_btn);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this)
                        .setView(R.layout.info_about_window);
                ad.show();
            }
        });
    }

    public void openDashboard(View view) {
        Intent i = new Intent(MainActivity.this,SeedMgmtActivity.class);
        startActivity(i);

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
