package com.discovery.thunderapp;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.discovery.thunderapp.databinding.ActivityMainBinding;

import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
 * @author: Sasikumar Bharanikumar
 * @version: 1.0
 */

public class MainActivity2 extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    Thread thread;
    static boolean isValid;
    Button testBtn;
    Button testBtn1;
    Button testBtn2;
    Button testBtn3;
    static final String USER_CPU_REGEX = "((?i)(sys(tem)?\\\\s+[0-9]{1,2}(\\\\.[0-9]{1,2})?\\\\%))|([0-9]{1,2}\\\\.[0-9]{1,2}\\\\%\\\\s+(?i)(sys(tem)?))";
    static String m_sTopResults = null;
    static float m_fUserCpuUsage=0F;
    static String currentLoad = "0";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(R.layout.activity_main2);

        testBtn = findViewById(R.id.buttonCPU3);
        testBtn1 = findViewById(R.id.buttonCPU1);
        testBtn2 = findViewById(R.id.buttonCPU2);

        testBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Current CPU Load : " + 80 + "%", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity2.this, TestService.class);
                startForegroundService(intent);
                forgroundManger();
            }
        });


        testBtn1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Current CPU Load : 30 %", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity2.this, TestServiceLow.class);
                startForegroundService(intent);
                forgroundManger();

            }
        });

        testBtn2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Current CPU Load : Iterative mode ", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity2.this, TestServiceModerate.class);
                startForegroundService(intent);
                forgroundManger();
            }
        });

        testBtn3 = findViewById(R.id.stop);

        testBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"The CPU Load Stopped",Toast.LENGTH_LONG).show();
                //Log.d("Stop","Stop event triggered"+thread.getState());
                isValid=false;
                if(thread != null) {
                    thread.interrupt();
                }
                //canceledJob(view);
            }
        });
    }

    public boolean forgroundManger()
    {
        ActivityManager activity = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for(ActivityManager.RunningServiceInfo service : activity.getRunningServices(Integer.MAX_VALUE))
        {
            if(TestService.class.getName().equals(service.service.getClassName()))
            {
                return true;
            }

        }
        return false;
    }


    public void scheduledJob(View view)
    {
        ComponentName component = new ComponentName(this,SchedulerTest.class);
        @SuppressLint("MissingPermission") JobInfo jobinfo= new JobInfo.Builder(123,component)
                .setPeriodic(15 * 60 * 1000)
                .setPersisted(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int result = scheduler.schedule(jobinfo);

        if(result == JobScheduler.RESULT_SUCCESS)
        {
            Log.d("Scheduler success","The job is scheduled successfully");
        }
        else
        {
            Log.d("Scheduler failed","The job is not scheduled successfully");
        }

    }

    public void canceledJob(View view)
    {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        Log.d("Canceled","Canceled job in main activity");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static boolean isReachable(String targetUrl) throws IOException, InterruptedException {
        HttpURLConnection httpUrlConnection = (HttpURLConnection) new URL(
                targetUrl).openConnection();
        httpUrlConnection.setRequestMethod("HEAD");
        try
        {
            int responseCode = httpUrlConnection.getResponseCode();
            isReachable(targetUrl);
            //System.out.println("CPU USAGE: "+100 * getCpuUsage());

            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (UnknownHostException noInternetConnection)
        {
            isReachable(targetUrl);
            // System.out.println("CPU USAGE: "+100 * getCpuUsage());
            return false;
        }
    }

    public static void eatRam()
    {
        final int MEMORY_SIZE = 1024 * 1024; // 1GB

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

    public static  String getCurrentLoad()
    {
        return currentLoad;
    }

    public static void setCurrentLoad(String value)
    {
        currentLoad = value;
    }

    private int getCpuPer() { //for single process

        int result=0;
        Context context = this.getApplicationContext();
        ActivityManager mgr = (ActivityManager)context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processes = mgr.getRunningAppProcesses();
        Log.e("DEBUG", "Running processes:");
        for(Iterator i = processes.iterator(); i.hasNext(); )
        {
            ActivityManager.RunningAppProcessInfo p = (ActivityManager.RunningAppProcessInfo)i.next();
            Log.e("DEBUG", "  process name: "+p.processName);
            Log.e("DEBUG", "     pid: "+p.pid);
            int[] pids = new int[1];
            result =p.pid;
            pids[0] = p.pid;
            android.os.Debug.MemoryInfo[] MI = mgr.getProcessMemoryInfo(pids);
            Log.e("memory","     dalvik private: " + MI[0].dalvikPrivateDirty);
            Log.e("memory","     dalvik shared: " + MI[0].dalvikSharedDirty);
            Log.e("memory","     dalvik pss: " + MI[0].dalvikPss);
            Log.e("memory","     native private: " + MI[0].nativePrivateDirty);
            Log.e("memory","     native shared: " + MI[0].nativeSharedDirty);
            Log.e("memory","     native pss: " + MI[0].nativePss);
            Log.e("memory","     other private: " + MI[0].otherPrivateDirty);
            Log.e("memory","     other shared: " + MI[0].otherSharedDirty);
            Log.e("memory","     other pss: " + MI[0].otherPss);

            Log.e("memory","     total private dirty memory (KB): " + MI[0].getTotalPrivateDirty());
            Log.e("memory","     total shared (KB): " + MI[0].getTotalSharedDirty());
            Log.e("memory","     total pss: " + MI[0].getTotalPss());

        }
        return result;
    }


    public void getRamStatus()
    {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        long totalMemory = memoryInfo.totalMem;
        long availableMemory = memoryInfo.availMem;

        System.out.println(totalMemory+" :::::::::::::::");
        System.out.println(availableMemory+" ::::::::::::::");
    }


    public static double getCpuUsage(int pid){
        try {
            int kth=3;
            Double[] usage_10_list = new Double[kth];
            for(int k=0;k<kth;k++) {
                int[] cpu_usage_list = getCpuUsageStatistic(pid);
                Double cupersent = 0.0;
                for (int i = 0; i < cpu_usage_list.length; i++) {
                    cupersent += (double) cpu_usage_list[i] / 100;
                }
                usage_10_list[k] = cupersent;
            }
            Double sum=0.0;
            for(int i=0;i<usage_10_list.length;i++)
            {
                sum+=usage_10_list[i];
            }
            return sum/kth;
        }catch (NumberFormatException e)
        {
            return -0.123;
        }
    }

    private static int[] getCpuUsageStatistic(int pid) {

        String tempString = executeTop(pid);

        tempString = tempString.replaceAll(",", "");
        tempString = tempString.replaceAll("User", "");
        tempString = tempString.replaceAll("System", "");
        tempString = tempString.replaceAll("IOW", "");
        tempString = tempString.replaceAll("IRQ", "");
        tempString = tempString.replaceAll("%", "");

        System.out.println("AFTER REMOVED : "+ tempString);
        for (int i = 0; i < 10; i++) {
            tempString = tempString.replaceAll("  ", " ");
        }
        tempString = tempString.trim();
        System.out.println("AFTER REMOVED : "+ tempString);
        String[] myString = tempString.split(" ");
        int[] cpuUsageAsInt = new int[myString.length];
        for (int i = 0; i < myString.length; i++) {
            myString[i] = myString[i].trim();
            System.out.println("AFTER REMOVED 3: "+ myString[i]);
            cpuUsageAsInt[i] = Integer.parseInt(myString[i]);
        }
        return cpuUsageAsInt;
    }

    private static String executeTop(int pid) {
        java.lang.Process p = null;
        BufferedReader in = null;
        String returnString = null;
        try {
            p = Runtime.getRuntime().exec("vmhost");
            System.out.println(p+" Runtime");
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while (returnString == null || returnString.contentEquals("")) {
                returnString = in.readLine();
            }
        } catch (IOException e) {
            Log.e("executeTop", "error in getting first line of top");
            e.printStackTrace();
        } finally {
            try {
                in.close();
                p.destroy();
            } catch (IOException e) {
                Log.e("executeTop",
                        "error in closing and destroying top process");
                e.printStackTrace();
            }
        }
        System.out.println(returnString+" TOP TOP TOP");
        return returnString;
    }

    private void startCollecting() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    ProcessBuilder builder = new ProcessBuilder("top", "-q", "-o CMDLINE,%CPU", "-s2", "-b");
                    java.lang.Process process = builder.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Log.d(TAG, line);
                        System.out.println(line);
                    }
                    reader.close();
                    BufferedReader err_reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    while ((line = err_reader.readLine()) != null) {
                        //Log.e(TAG, line);
                        System.out.println(line);
                    }
                    process.waitFor();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }


    private float readUsage() {
        try {
            RandomAccessFile reader = new RandomAccessFile(Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/proc/stat", "r");
            String load = reader.readLine();

            String[] toks = load.split(" ");

            long idle1 = Long.parseLong(toks[5]);
            long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            try {
                Thread.sleep(360);
            } catch (Exception e) {}

            reader.seek(0);
            load = reader.readLine();
            reader.close();

            toks = load.split(" ");

            long idle2 = Long.parseLong(toks[5]);
            long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            return (float)(cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    private static void getCPUInfo()
    {
        BufferedReader 		ifp = null;

        //* empty the raw results buffer

        try
        {
            //* execute the top command
            java.lang.Process process = null;
            process = Runtime.getRuntime().exec("top -n 1 -d 1");
            //* we only want to retrieve the top few lines

            //* read the output from the command
            String sLine = new String();
            ifp = new BufferedReader(new InputStreamReader(process.getInputStream()));

            //* Read all the available output and store it in the class member
            while ( (sLine = ifp.readLine()) != null)
            {
                m_sTopResults += sLine + "\n";

                System.out.println(sLine+" : Current Line ***********");
            }
        } catch (IOException exp) {
            exp.getStackTrace();

        }
        finally
        {
            //* we need to close the stream once everything is done.
            try {
                if (ifp != null)
                    ifp.close();
            }
            catch (IOException exp)
            {
                exp.getStackTrace();
            }
        }

        System.out.println("CPU:CPU"+ m_sTopResults);
    }

    public void parseTopResults() {
        String sUserCpuUsage = null;
        String sSystemCpuUsage = null;

        Pattern xRegexSearchPattern = null;
        Matcher xSearch = null;

        //* Check to make sure we have some data to parse
        if (this.m_sTopResults == null) {
            System.out.println("No top results available.");
            return;
        }

        System.out.println(this.m_sTopResults+"***********");
        //* search for the user CPU information
        xRegexSearchPattern = Pattern.compile(this.USER_CPU_REGEX);
        xSearch = xRegexSearchPattern.matcher(this.m_sTopResults);
        //* check to see if we found a match
        if (xSearch.find()) {

            sUserCpuUsage = xSearch.group(0);
            System.out.println("Found User CPU USage: " + sUserCpuUsage);

            //* we want to remove the non numeric characters in the string
            try {
                this.m_fUserCpuUsage = Float.parseFloat(sUserCpuUsage.replaceAll("[^\\d.]", "").trim());
            } catch (NumberFormatException exp) {
                this.m_fUserCpuUsage = 0.0F;
                System.out.println("An error occured while trying to parse the user cpu usage.");
                System.out.println(exp.getStackTrace());
            }
        }
    }


}
