/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.luisgarcia.bean;

/**
 *
 * @author luisgarcia
 */
public class Horarios {
    private int codigoHorario;
    private String horarioInicio;
    private String horarioSalida;
    private int lunes;
    private int martes;
    private int miercoles;
    private int jueves;
    private int viernes;

    public Horarios(int codigoHorario, String horarioInicio, String horarioSalida, int lunes, int martes, int miercoles, int jueves, int viernes) {
        this.codigoHorario = codigoHorario;
        this.horarioInicio = horarioInicio;
        this.horarioSalida = horarioSalida;
        this.lunes = lunes;
        this.martes = martes;
        this.miercoles = miercoles;
        this.jueves = jueves;
        this.viernes = viernes;
    }

    public Horarios() {
    }

    public int getCodigoHorario() {
        return codigoHorario;
    }

    public void setCodigoHorario(int codigoHorario) {
        this.codigoHorario = codigoHorario;
    }

    public String getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(String horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public String getHorarioSalida() {
        return horarioSalida;
    }

    public void setHorarioSalida(String horarioSalida) {
        this.horarioSalida = horarioSalida;
    }

    public int getLunes() {
        return lunes;
    }

    public void setLunes(int lunes) {
        this.lunes = lunes;
    }

    public int getMartes() {
        return martes;
    }

    public void setMartes(int martes) {
        this.martes = martes;
    }

    public int getMiercoles() {
        return miercoles;
    }

    public void setMiercoles(int miercoles) {
        this.miercoles = miercoles;
    }

    public int getJueves() {
        return jueves;
    }

    public void setJueves(int jueves) {
        this.jueves = jueves;
    }

    public int getViernes() {
        return viernes;
    }

    public void setViernes(int viernes) {
        this.viernes = viernes;
    }
    
    @Override
    public String toString() {
        return "" + codigoHorario + "-" + "Inicio:"+ " " +horarioInicio + " " + "Salida:"+ " " +horarioSalida;
    }
    
}
