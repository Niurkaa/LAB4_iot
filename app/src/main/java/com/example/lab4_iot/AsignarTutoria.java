package com.example.lab4_iot;

import androidx.appcompat.app.AppCompatActivity;

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
                .baseUrl("http://192.168.1.34:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(EmployeeRepository.class);

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
                                binding.textView21.setText("Asignación de cita correcta");
                                citas++;
                            }else{
                                binding.textView21.setText("El trabajador ya tiene una cita " + "asignada. Elija otro trabajador");
                            }

                        }else{
                            binding.textView21.setText("No es manager del empleado");
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
}