/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.luisgarcia.bean;

import java.sql.Date;

/**
 *
 * @author luisgarcia
 */
public class Turno {
    private int codigoTurno;
    private Date fechaTurno;
    private Date fechaCita;
    private int valorCita;
    private int codigoMedicoEspecialidad;
    private int codigoResposableTurno;
    private int codigoPacientes;

    public Turno(int codigoTurno, Date fechaTurno, Date fechaCita, int valorCita, int codigoMedicoEspecialidad, int codigoResposableTurno, int codigoPacientes) {
        this.codigoTurno = codigoTurno;
        this.fechaTurno = fechaTurno;
        this.fechaCita = fechaCita;
        this.valorCita = valorCita;
        this.codigoMedicoEspecialidad = codigoMedicoEspecialidad;
        this.codigoResposableTurno = codigoResposableTurno;
        this.codigoPacientes = codigoPacientes;
    }

    public Turno() {
    }

    public int getCodigoTurno() {
        return codigoTurno;
    }

    public void setCodigoTurno(int codigoTurno) {
        this.codigoTurno = codigoTurno;
    }

    public Date getFechaTurno() {
        return fechaTurno;
    }

    public void setFechaTurno(Date fechaTurno) {
        this.fechaTurno = fechaTurno;
    }

    public Date getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Date fechaCita) {
        this.fechaCita = fechaCita;
    }

    public int getValorCita() {
        return valorCita;
    }

    public void setValorCita(int valorCita) {
        this.valorCita = valorCita;
    }

    public int getCodigoMedicoEspecialidad() {
        return codigoMedicoEspecialidad;
    }

    public void setCodigoMedicoEspecialidad(int codigoMedicoEspecialidad) {
        this.codigoMedicoEspecialidad = codigoMedicoEspecialidad;
    }

    public int getCodigoResposableTurno() {
        return codigoResposableTurno;
    }

    public void setCodigoResposableTurno(int codigoResposableTurno) {
        this.codigoResposableTurno = codigoResposableTurno;
    }

    public int getCodigoPacientes() {
        return codigoPacientes;
    }

    public void setCodigoPacientes(int codigoPacientes) {
        this.codigoPacientes = codigoPacientes;
    }
    
    @Override
    public String toString (){
        return "" + codigoTurno;
    }
    
}
