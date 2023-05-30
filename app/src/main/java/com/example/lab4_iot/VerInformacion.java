package com.example.lab4_iot;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.lab4_iot.Retrofit.EmployeeRepository;
import com.example.lab4_iot.databinding.ActivityFlujoTrabajadorBinding;
import com.example.lab4_iot.databinding.ActivityVerInformacionBinding;
import com.example.lab4_iot.entity.Employee;
import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VerInformacion extends AppCompatActivity {

    ActivityVerInformacionBinding binding;

    private static String TAG = "verInfo-test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerInformacionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createNotificationChannel();

        EmployeeRepository employeeRepository = new Retrofit.Builder()
                .baseUrl("http://172.16.68.174:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(EmployeeRepository.class);

        binding.buscar.setOnClickListener(view -> {
            String textoGuardar = binding.id.getText().toString();
            int id = Integer.parseInt(textoGuardar);
            employeeRepository.obtenerEmployeePorId(id).enqueue(new Callback<Employee>() {
                @Override
                public void onResponse(Call<Employee> call, Response<Employee> response) {

                    if (response.isSuccessful()) {
                        Employee employee = response.body();
                        Log.d(TAG, "recepción correcta!");
                        employee.setId(textoGuardar);

                        binding.nombre.setText(employee.getFirstName());
                        binding.apellidos.setText(employee.getLastName());
                        binding.correo.setText(employee.getEmail());
                        binding.textView17.setText(employee.getPhoneNumber());
                        binding.textView20.setText(employee.getId()==null?"Presidente":"Empleado");
                        binding.textView16.setText(employee.getSalary().toString());

                        /*Inserta logica de notificación de clase tutoria*/
                    } else {
                        Log.d(TAG, "algo salió mal");
                    }
                }

                @Override
                public void onFailure(Call<Employee> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        });

        binding.descargarInf.setOnClickListener(view -> {
            String textoGuardar = binding.id.getText().toString();
            int id = Integer.parseInt(textoGuardar);
            employeeRepository.obtenerEmployeePorId(id).enqueue(new Callback<Employee>() {
                @Override
                public void onResponse(Call<Employee> call, Response<Employee> response) {

                    if (response.isSuccessful()) {
                        Employee employee = response.body();
                        Log.d(TAG, "recepción correcta!");
                        employee.setId(textoGuardar);
                        guardarComoJson(employee);
                    } else {
                        Log.d(TAG, "algo salió mal");
                    }
                }

                @Override
                public void onFailure(Call<Employee> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        });
    }

    String channelId = "channelDefaultPri";

    public void createNotificationChannel() {
        //android.os.Build.VERSION_CODES.O == 26
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Canal notificaciones default",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Canal para notificaciones con prioridad default");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);


            askPermission();

        }
    }

    public void askPermission(){
        //android.os.Build.VERSION_CODES.TIRAMISU == 33
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) ==
                        PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(VerInformacion.this,
                    new String[]{POST_NOTIFICATIONS},
                    101);
        }
    }


    public void lanzarNotificacion(String contenttitle) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(contenttitle)
                .setContentText("Disfruta tu instancia")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1, builder.build());
        }
    }

    public void guardarComoJson(Employee listaEmployees) {
        Gson gson = new Gson();
        String listaEmployeesAsJson = gson.toJson(listaEmployees);

        Log.d(TAG, listaEmployeesAsJson);

        String fileName = "informacionDe"+listaEmployees.getId().toString()+".txt";

        try (FileOutputStream fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
             FileWriter fileWriter = new FileWriter(fileOutputStream.getFD())) {

            fileWriter.write(listaEmployeesAsJson);
            Log.d(TAG, "Guardado exitoso");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}