package com.discovery.thunderapp;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Calendar;

public class SchedulerTest extends JobService {

    private boolean jobCancelled = false;

    @Override
    public boolean onStartJob(JobParameters params) {
        backgroundRunner(params);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d("MyJobService", "Job Failed to run on background" );
        jobCancelled = true;
        return false;
    }


    private void backgroundRunner(final JobParameters params)
    {
        Log.d("MyJobService", "Job started from scheduler class" );

        boolean isValid = true;
        int Iteration = 150;
        for(int i=0;i<Iteration;i++) {
            new Thread(new Runnable() {
                public void run() {
                    while (isValid) {
                        if(jobCancelled)
                        {
                            return;
                        }
                        System.out.println("The current thread: "+Thread.currentThread().getName().toString());
                        LoadSampler f = new LoadSampler();
                        f.start();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent("stresstest-done");
                        // You can also include some extra data.
                        intent.putExtra("end", Calendar.getInstance().getTimeInMillis());
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    }
                    jobFinished(params,false);
                }
            }).start();
        }
    }
}
