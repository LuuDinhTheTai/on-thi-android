package com.example.on_thi_1_2pdf;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RingtoneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ringtone);

        Button btnPlay = findViewById(R.id.btnPlay);
        Button btnStop = findViewById(R.id.btnStop);
        Button btnBackHome = findViewById(R.id.btnBackHome);

        btnPlay.setOnClickListener(v -> {
            Intent playIntent = new Intent(this, RingtoneService.class);
            playIntent.putExtra(RingtoneService.EXTRA_ACTION, RingtoneService.ACTION_PLAY);
            startService(playIntent);
        });

        btnStop.setOnClickListener(v -> {
            Intent stopIntent = new Intent(this, RingtoneService.class);
            stopIntent.putExtra(RingtoneService.EXTRA_ACTION, RingtoneService.ACTION_STOP);
            startService(stopIntent);
        });

        btnBackHome.setOnClickListener(v -> finish());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent stopIntent = new Intent(this, RingtoneService.class);
        stopIntent.putExtra(RingtoneService.EXTRA_ACTION, RingtoneService.ACTION_STOP);
        startService(stopIntent);
    }
}

