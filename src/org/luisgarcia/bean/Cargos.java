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
public class Cargos {
    private int codigoCargo;
    private String nombreCargo;

    public Cargos(int codigoCargo, String nombreCargo) {
        this.codigoCargo = codigoCargo;
        this.nombreCargo = nombreCargo;
    }

    public Cargos() {
    }

    public int getCodigoCargo() {
        return codigoCargo;
    }

    public void setCodigoCargo(int codigoCargo) {
        this.codigoCargo = codigoCargo;
    }

    public String getNombreCargo() {
        return nombreCargo;
    }

    public void setNombreCargo(String nombreCargo) {
        this.nombreCargo = nombreCargo;
    }
    
     @Override
    public String toString() {
        return "" + codigoCargo + "_" +  nombreCargo;
    }
}
