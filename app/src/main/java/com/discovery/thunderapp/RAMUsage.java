package com.discovery.thunderapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Calendar;


public class RAMUsage extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MyJobService", "Job started");
        System.out.println(MainActivity2.isValid);
        MainActivity2.isValid = true;
        new Thread(new Runnable() {
            public void run() {
                // getRamStatus();
                  MemOpUtils.malloc(10);
                System.out.println("INSIDE THREAD ::::");
            }
        }).start();
        final String value = "Notification";

        System.out.println("::::::::::INTENSIVE calls");
        NotificationChannel notification = new NotificationChannel(value,value, NotificationManager.IMPORTANCE_HIGH);
        getSystemService(NotificationManager.class).createNotificationChannel(notification);
        Notification.Builder notify = new Notification.Builder(this,value)
                .setContentText(value)
                .setContentTitle(value);
        startForeground(2, notify.build());

        return super.onStartCommand(intent,flags,startId);
    }

}
