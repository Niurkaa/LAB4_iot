package com.example.lab4_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.lab4_iot.Retrofit.EmployeeRepository;
import com.example.lab4_iot.databinding.ActivityBuscarTrabajadorBinding;
import com.example.lab4_iot.databinding.ActivityMainBinding;
import com.example.lab4_iot.entity.Employee;
import com.example.lab4_iot.entity.EmployeeDto;
import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BuscarTrabajador extends AppCompatActivity {

    ActivityBuscarTrabajadorBinding binding;
    private static String TAG = "flujoBuscarTrabajador-test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBuscarTrabajadorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EmployeeRepository employeeRepository = new Retrofit.Builder()
                .baseUrl("http://192.168.1.34:8080")
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

                        String names = employee.getFirstName().toString()+" "+employee.getLastName().toString();

                        binding.textView15.setText(names);
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