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
import android.util.Log;

import com.example.lab4_iot.Retrofit.EmployeeRepository;
import com.example.lab4_iot.databinding.ActivityAsignarTutoriaBinding;
import com.example.lab4_iot.databinding.ActivityBuscarTrabajadorBinding;
import com.example.lab4_iot.entity.Employee;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AsignarTutoria extends AppCompatActivity {

    ActivityAsignarTutoriaBinding binding;
    private static String TAG = "flujoAsignarTutor-test";

    int citas = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAsignarTutoriaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EmployeeRepository employeeRepository = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8081")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(EmployeeRepository.class);

        createNotificationChannel();

        binding.button.setOnClickListener(view -> {
            String texto1 = binding.idEmpleado.getText().toString();
            int idEmpleado = Integer.parseInt(texto1);

            String texto2 = binding.codigoEmpleado.getText().toString();
            employeeRepository.obtenerEmployeePorId(idEmpleado).enqueue(new Callback<Employee>() {
                @Override
                public void onResponse(Call<Employee> call, Response<Employee> response) {

                    if (response.isSuccessful()) {
                        Employee employee = response.body();
                        Log.d(TAG, "recepción correcta!");

                        if (employee.getManagerId().toString().equals(texto2)){
                            if (citas<=0){

                                lanzarNotificacion("Asignación de cita correcta");
                                citas++;
                            }else{
                                lanzarNotificacion("El trabajador ya tiene una cita asignada. Elija otro trabajador");
                            }

                        }else{
                            lanzarNotificacion("No es manager del empleado");
                        }

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
                    NotificationManager.IMPORTANCE_DEFAULT);
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

            ActivityCompat.requestPermissions(AsignarTutoria.this,
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
                .setContentText("Disfrute de su instancia")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1, builder.build());
        }
    }
}