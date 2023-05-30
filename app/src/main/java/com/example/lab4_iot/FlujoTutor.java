package com.example.lab4_iot;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lab4_iot.Retrofit.EmployeeRepository;
import com.example.lab4_iot.databinding.ActivityFlujoTutorBinding;
import com.example.lab4_iot.entity.Employee;
import com.example.lab4_iot.entity.EmployeeDto;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FlujoTutor extends AppCompatActivity {
    private static String TAG = "flujoTutorAct-test";
    ActivityFlujoTutorBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFlujoTutorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EmployeeRepository employeeRepository = new Retrofit.Builder()
                .baseUrl("http://192.168.1.34:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(EmployeeRepository.class);

        binding.listatrabajadores.setOnClickListener(view -> {
            employeeRepository.listarEmployees().enqueue(new Callback<EmployeeDto>() {
                @Override
                public void onResponse(Call<EmployeeDto> call, Response<EmployeeDto> response) {

                    if (response.isSuccessful()) {
                        EmployeeDto employeeDto = response.body();
                        Log.d(TAG, "recepción correcta!");
                        Employee[] employees = employeeDto.get_embedded().getEmployees();
                        List<Employee> employeesSinPresidente = new ArrayList<>();
                        for (Employee e : employees) {
                            if (e.getManagerId() != null) {
                                employeesSinPresidente.add(e);
                            }
                        }
                        // Convert the list back to an array if needed
                        Employee[] employeesSinPresidenteArray = employeesSinPresidente.toArray(new Employee[0]);
                        guardarComoJson(employeesSinPresidenteArray);

                    } else {
                        Log.d(TAG, "algo salió mal");
                    }
                }

                @Override
                public void onFailure(Call<EmployeeDto> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        });

        binding.button2.setOnClickListener(view -> {
            Intent intent = new Intent(FlujoTutor.this,BuscarTrabajador.class);
            startActivity(intent);
        });

        binding.button3.setOnClickListener(view -> {
            Intent intent = new Intent(FlujoTutor.this,AsignarTutoria.class);
            startActivity(intent);
        });
    }

    public void guardarComoJson(Employee[] listaEmployees) {
        Gson gson = new Gson();
        String listaEmployeesAsJson = gson.toJson(listaEmployees);

        Log.d(TAG, listaEmployeesAsJson);

        String fileName = "listaDeTrabajadores.txt";

        try (FileOutputStream fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
             FileWriter fileWriter = new FileWriter(fileOutputStream.getFD())) {

            fileWriter.write(listaEmployeesAsJson);
            Log.d(TAG, "Guardado exitoso");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
