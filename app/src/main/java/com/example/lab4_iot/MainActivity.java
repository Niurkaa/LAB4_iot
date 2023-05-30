package com.example.lab4_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.lab4_iot.databinding.ActivityFlujoTutorBinding;
import com.example.lab4_iot.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tutor.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,FlujoTutor.class);
            startActivity(intent);
        });
    }
}