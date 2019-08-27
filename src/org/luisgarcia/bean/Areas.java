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
public class Areas {
    private int codigoArea;
    private String nombreArea;

    public Areas(int codigoArea, String nombreArea) {
        this.codigoArea = codigoArea;
        this.nombreArea = nombreArea;
    }

    public Areas() {
    }

    public int getCodigoArea() {
        return codigoArea;
    }

    public void setCodigoArea(int codigoArea) {
        this.codigoArea = codigoArea;
    }

    public String getNombreArea() {
        return nombreArea;
    }

    public void setNombreArea(String nombreArea) {
        this.nombreArea = nombreArea;
    }
    
    @Override
    public String toString() {
        return ""+codigoArea + "_" + nombreArea;
    }
    
}
