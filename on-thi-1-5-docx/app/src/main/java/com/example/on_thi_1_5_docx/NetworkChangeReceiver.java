package com.example.on_thi_1_5_docx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            @SuppressWarnings("deprecation")
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo == null || !networkInfo.isConnected()) {
                Toast.makeText(context, "Mất kết nối mạng!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Có kết nối mạng!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


