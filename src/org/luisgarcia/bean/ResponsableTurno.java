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
public class ResponsableTurno {
    private int codigoResposableTurno;
    private String nombreResposable; 
    private String apellidoResposable;
    private String telefonoResposable; 
    private int codigoArea;
    private int codigoCargo;

    public ResponsableTurno(int codigoResposableTurno, String nombreResposable, String apellidoResposable, String telefonoResposable, int codigoArea, int codigoCargo) {
        this.codigoResposableTurno = codigoResposableTurno;
        this.nombreResposable = nombreResposable;
        this.apellidoResposable = apellidoResposable;
        this.telefonoResposable = telefonoResposable;
        this.codigoArea = codigoArea;
        this.codigoCargo = codigoCargo;
    }

    public ResponsableTurno() {
    }
    

    public int getCodigoResposableTurno() {
        return codigoResposableTurno;
    }

    public void setCodigoResposableTurno(int codigoResposableTurno) {
        this.codigoResposableTurno = codigoResposableTurno;
    }

    public String getNombreResposable() {
        return nombreResposable;
    }

    public void setNombreResposable(String nombreResposable) {
        this.nombreResposable = nombreResposable;
    }

    public String getApellidoResposable() {
        return apellidoResposable;
    }

    public void setApellidoResposable(String apellidoResposable) {
        this.apellidoResposable = apellidoResposable;
    }

    public String getTelefonoResposable() {
        return telefonoResposable;
    }

    public void setTelefonoResposable(String telefonoResposable) {
        this.telefonoResposable = telefonoResposable;
    }

    public int getCodigoArea() {
        return codigoArea;
    }

    public void setCodigoArea(int codigoArea) {
        this.codigoArea = codigoArea;
    }

    public int getCodigoCargo() {
        return codigoCargo;
    }

    public void setCodigoCargo(int codigoCargo) {
        this.codigoCargo = codigoCargo;
    }
    
    @Override
    public String toString (){
        return "" + codigoResposableTurno + "_" + nombreResposable;
    }
    
    
}
