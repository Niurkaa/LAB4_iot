package com.example.lab4_iot.entity;

import java.io.Serializable;

public class ListaJobs implements Serializable {
    private Job[] jobs;

    public Job[] getJobs() {
        return jobs;
    }

    public void setJobs(Job[] jobs) {
        this.jobs = jobs;
    }
}
