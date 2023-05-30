package com.example.lab4_iot.Retrofit;

import com.example.lab4_iot.entity.Employee;
import com.example.lab4_iot.entity.EmployeeDto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface EmployeeRepository {
    @GET("/api/employee")
    Call<EmployeeDto> listarEmployees();
    @GET("/api/employee/{id}")
    Call<Employee> obtenerEmployeePorId(@Path("id") int employeeId);
}
