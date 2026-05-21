package com.example.on_thi_1_2pdf;

import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class RingtoneService extends Service {
    public static final String ACTION_PLAY = "play";
    public static final String ACTION_STOP = "stop";
    public static final String EXTRA_ACTION = "action";

    private Ringtone ringtone;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent != null ? intent.getStringExtra(EXTRA_ACTION) : null;
        if (ACTION_PLAY.equals(action)) {
            playRingtone();
        } else if (ACTION_STOP.equals(action)) {
            stopRingtone();
            stopSelf();
        }
        return START_NOT_STICKY;
    }

    private void playRingtone() {
        if (ringtone == null) {
            Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            if (ringtoneUri == null) {
                ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }
            ringtone = RingtoneManager.getRingtone(getApplicationContext(), ringtoneUri);
        }

        if (ringtone != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ringtone.setLooping(true);
            }
            if (!ringtone.isPlaying()) {
                ringtone.play();
            }
        }
    }

    private void stopRingtone() {
        if (ringtone != null && ringtone.isPlaying()) {
            ringtone.stop();
        }
    }

    @Override
    public void onDestroy() {
        stopRingtone();
        super.onDestroy();
    }
}

