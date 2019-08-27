/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.luisgarcia.bean;

import java.util.Date;     
/**
 *
 * @author luisgarcia
 */
public class Medicos {
   private int codigoMedico; 
   private int licenciaMedica ;
   private String nombres ;
   private String apellidos ;
   private Date horaEntrada ;
   private Date horaSalidad ;
   private String sexo ;



    public Medicos(int codigoMedico, int licenciaMedica, String nombres, String apellidos, Date horaEntrada, Date horaSalidad,  String sexo) {
        this.codigoMedico = codigoMedico;
        this.licenciaMedica = licenciaMedica;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.horaEntrada = horaEntrada;
        this.horaSalidad = horaSalidad;
        this.sexo = sexo;
    }

    public Medicos() {
        
    }

   
  
    public int getCodigoMedico() {
        return codigoMedico;
    }

    public void setCodigoMedico(int codigoMedico) {
        this.codigoMedico = codigoMedico;
    }

    public int getLicenciaMedica() {
        return licenciaMedica;
    }

    public void setLicenciaMedica(int licenciaMedica) {
        this.licenciaMedica = licenciaMedica;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Date getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(Date horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public Date getHoraSalidad() {
        return horaSalidad;
    }

    public void setHoraSalidad(Date horaSalidad) {
        this.horaSalidad = horaSalidad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    @Override
    public String toString() {
        return "" + codigoMedico + "_" + nombres ;
    }
   
   
   
}
