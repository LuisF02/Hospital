/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.luisgarcia.bean;

import java.util.Date;

/**
 *
 * @author programacion
 */
public class ControlCitas {
    private int codigoControlCita;
    private Date fecha;
    private String horaInicio;
    private String horaFin;
    private int codigoMedico;
    private int codigoPacientes;

    public ControlCitas(int codigoControlCita, Date fecha, String horaInicio, String horaFin, int codigoMedico, int codigoPacientes) {
        this.codigoControlCita = codigoControlCita;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.codigoMedico = codigoMedico;
        this.codigoPacientes = codigoPacientes;
    }

    public ControlCitas() {
    }

    public int getCodigoControlCita() {
        return codigoControlCita;
    }

    public void setCodigoControlCita(int codigoControlCita) {
        this.codigoControlCita = codigoControlCita;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public int getCodigoMedico() {
        return codigoMedico;
    }

    public void setCodigoMedico(int codigoMedico) {
        this.codigoMedico = codigoMedico;
    }

    public int getCodigoPacientes() {
        return codigoPacientes;
    }

    public void setCodigoPacientes(int codigoPacientes) {
        this.codigoPacientes = codigoPacientes;
    }
    
    @Override
    public String toString() {
        return ""+codigoControlCita;
    }
    
    
}
