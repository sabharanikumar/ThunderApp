package com.discovery.thunderapp;

import static java.lang.Runtime.*;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.content.SharedPreferences;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/*
 * @author: Sasikumar Bharanikumar
 * @version: 1.0
 */

public class TestServiceModerate extends JobService {


    private long[] memoryBlackHole = null;
    private SharedPreferences preferences;

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
        System.out.println(MainActivity2.isValid);
        MainActivity2.isValid = true;
            new Thread(new Runnable() {
                public void run() {
                   // getRamStatus();
                   // ====> cpuFrequencyTuner();
                    MemOpUtils.malloc(100);
                    getRamStatus();
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

    public void cpuFrequencyTuner()
    {
        final int MEMORY_SIZE = 1024 * 1024;

        byte[] largeArray = new byte[MEMORY_SIZE];

        while (true) {
            for (int i = 0; i < largeArray.length; i++) {
                largeArray[i] = (byte) (i % 256);
            }

            for (int i = 0; i < largeArray.length; i++) {
                byte b = largeArray[i];
            }
        }

    }


    private void getCpuPer()throws Exception { //for single process
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        long availableMemory = memoryInfo.availMem;
        long totalMemory = memoryInfo.totalMem;
        long availableMemoryInMegabytes = availableMemory / 1048576;
        long total = totalMemory/ 1048576;

        System.out.println(availableMemory);
        System.out.println(total);

    }

    public long getRamStatus()
    {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        long totalMemory = memoryInfo.totalMem;
        long availableMemory = memoryInfo.availMem;

        long totalMemoryMB = totalMemory / 1048576;
        long availableMemoryMB = availableMemory / 1048576;

        System.out.println("Total memory: " + totalMemoryMB + "MB");
        System.out.println("Available memory: " + availableMemoryMB + "MB");

        System.out.println(totalMemoryMB+"  MB:::::::::::::::");
        System.out.println(availableMemoryMB+"  MB::::::::::::::");

        return availableMemoryMB;
    }


    private void eatAllMemory() {
        long max = getRamStatus();
        boolean retry = false;
        eatMemory(max,retry);
    }

    private void eatMemory(long numberOfMb, boolean retry) {
        System.out.println("eating memory MB = " + numberOfMb/1048576);
        // convert MB -> KB -> Bytes
        long numberOfBytes = numberOfMb * 1024 * 1024;
        System.out.println("eating memory bytes = " + numberOfBytes/1048576);

        boolean memoryAllocated = false;
        String message = (numberOfBytes / 1024 / 1024) + " MB allocated";
        while (!memoryAllocated)
        {
            try {
                System.out.println("trying to eat memory bytes = " + numberOfBytes/1048576);
                // we need to do this before we allocate all the memory :-)
                message = (numberOfBytes / 1024 / 1024) + " MB allocated";
                // char is 2 bytes in java
                memoryBlackHole = new long[ (int) (numberOfBytes / 2)];
                memoryAllocated = true;
                System.out.println("success eating memory bytes = "  + numberOfBytes/1048576);
            }
            catch (OutOfMemoryError e) {
                if (retry) {
                    // we cannot have that much, lets try 5MB less
                    numberOfBytes = numberOfBytes - (1024 * 1024 * 5);
                }
                else {
                    message = "Cannot allocate " + (numberOfBytes / 1024 / 1024) + " MB, try less memory";
                    System.out.println(message);
                    return;
                }
            }
        }

        System.out.println("after memory allocation");
        fillBlackHole(numberOfBytes / 2);
        //updateNotification(message);
        //updateNotificationToCurrentMemoryUsage();
    }

    private void fillBlackHole(long numberOfChars) {
        System.out.println("filling memory bytes = "  + numberOfChars);
        // dalvik allocate the memory when requested
        // art is clever and waits until you use the memory
        for (int index=0; index<numberOfChars; index++) {
            memoryBlackHole[index] = 1000000;
        }
    }

}
