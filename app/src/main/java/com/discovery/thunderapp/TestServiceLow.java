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
import java.util.regex.Pattern;

/*
 * @author: Sasikumar Bharanikumar
 * @version: 1.0
 */

public class TestServiceLow extends JobService {


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onStartJob(JobParameters params) {
        // Perform your work here
        Log.d("MyJobService", "Job started");

        boolean isValid = true;
        long Iteration = 1000000000;

            new Thread(new Runnable() {
                public void run() {
                    while (isValid) {
                        for (int i = 0; i < Iteration; i++) {
                            System.out.println("The current thread: " + Thread.currentThread().getName().toString());
                            LoadSampler f = new LoadSampler();
                            f.start();
                            Intent intent = new Intent("stresstest-done");
                            // You can also include some extra data.
                            intent.putExtra("end", Calendar.getInstance().getTimeInMillis());
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                        }
                    }
                }
            }).start();

        final String value = "Notification";

            System.out.println("::::::::::INTENSIVE calls");
            NotificationChannel notification = new NotificationChannel(value,value, NotificationManager.IMPORTANCE_HIGH);
            getSystemService(NotificationManager.class).createNotificationChannel(notification);
            Notification.Builder notify = new Notification.Builder(this,value)
                    .setContentText(value)
                    .setContentTitle(value);
            startForeground(1, notify.build());

        // Return true to indicate that the job is still running
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        // Stop the job if necessary
        return false;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MyJobService", "Job started");
        long Iteration = 1000000000;
        MainActivity2.isValid = true;
        new Thread(new Runnable() {
            public void run() {
                while (MainActivity2.isValid) {
                    for (int i = 0; i < Iteration; i++) {
                        Pattern p = Pattern.compile("\"[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+(?:[A-Z]{2}|com|org|net|edu|gov|mil|biz|info|mobi|name|aero|asia|jobs|museum)\\b\"");
                        System.out.println(p);
                        }
                }
            }
        }).start();

        final String value = "Notification";

        System.out.println("::::::::::INTENSIVE calls");
        NotificationChannel notification = new NotificationChannel(value,value, NotificationManager.IMPORTANCE_HIGH);
        getSystemService(NotificationManager.class).createNotificationChannel(notification);
        Notification.Builder notify = new Notification.Builder(this,value)
                .setContentText(value)
                .setContentTitle(value);
        startForeground(1, notify.build());

        return super.onStartCommand(intent,flags,startId);
    }

}
