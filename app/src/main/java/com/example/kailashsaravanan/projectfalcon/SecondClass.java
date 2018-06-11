package com.example.kailashsaravanan.projectfalcon;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class SecondClass extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(intent.getExtras().getInt("ID"));
        Toast.makeText(context, "Alert Acknowledged", Toast.LENGTH_SHORT).show();
    }
}
