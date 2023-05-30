package com.example.lab4_iot.entity;

import java.io.Serializable;

public class ListaEmployees implements Serializable {
    private Employee[] employees;

    public Employee[] getEmployees() {
        return employees;
    }

    public void setEmployees(Employee[] employees) {
        this.employees = employees;
    }
}
