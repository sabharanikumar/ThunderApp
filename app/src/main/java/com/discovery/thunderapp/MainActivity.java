package com.discovery.thunderapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.discovery.thunderapp.databinding.ActivityMainBinding;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/*
 * @author: Sasikumar Bharanikumar
 * @version: 1.0
 */

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;
    Button cpu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(R.layout.activity_main);

        cpu = findViewById(R.id.CPU);

            cpu.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    startActivity(intent);
                }
            });
    }



}
