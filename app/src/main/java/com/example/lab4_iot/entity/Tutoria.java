package com.example.lab4_iot.entity;

public class Tutoria {
    private boolean cita;
    private Integer idempleado;
    private Integer idtutor;
    private String fecha;

    public boolean isCita() {
        return cita;
    }

    public void setCita(boolean cita) {
        this.cita = cita;
    }

    public Integer getIdempleado() {
        return idempleado;
    }

    public void setIdempleado(Integer idempleado) {
        this.idempleado = idempleado;
    }

    public Integer getIdtutor() {
        return idtutor;
    }

    public void setIdtutor(Integer idtutor) {
        this.idtutor = idtutor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Tutoria(boolean cita, Integer idempleado, Integer idtutor, String fecha) {
        this.cita = cita;
        this.idempleado = idempleado;
        this.idtutor = idtutor;
        this.fecha = fecha;
    }

    public Tutoria(){}
}
