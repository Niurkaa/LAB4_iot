package com.example.lab4_iot.Retrofit;

import com.example.lab4_iot.entity.JobDto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JobRepository {
    @GET("/api/job")
    Call<JobDto> listarJobs();
    @GET("/api/job/{id}")
    Call<JobDto> obtenerJobPorId(@Path("id") int jobId);

}
