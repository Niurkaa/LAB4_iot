package com.example.lab4_iot;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.example.lab4_iot.databinding.ActivityFlujoTrabajadorBinding;
import com.example.lab4_iot.databinding.ActivityFlujoTutorBinding;

public class FlujoTrabajador extends AppCompatActivity {

    ActivityFlujoTrabajadorBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFlujoTrabajadorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button4.setOnClickListener(view -> {
            Intent intent = new Intent(FlujoTrabajador.this,VerInformacion.class);
            startActivity(intent);
        });

        binding.button5.setOnClickListener(view -> {
            Intent intent = new Intent(FlujoTrabajador.this,DescargarHorarios.class);
            startActivity(intent);
        });
    }


}