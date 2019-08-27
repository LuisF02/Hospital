/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.luisgarcia.bean;

/**
 *
 * @author programacion
 */
public class ContactoUrgencia {
    private int codigoContactoUrgencia;
    private String nombres;
    private String apellidos; 
    private String numeroContacto;
    private int codigoPacientes;
    
    public ContactoUrgencia(int codigoContactoUrgencia, String nombres,String apellidos, String numeroContacto, int codigoPacientes ){
        this.codigoContactoUrgencia = codigoContactoUrgencia;
        this.nombres = nombres;
        this.apellidos = apellidos; 
        this.numeroContacto = numeroContacto;
        this.codigoPacientes = codigoPacientes;
        
    }

    public ContactoUrgencia() {
    }
    

    public int getCodigoContactoUrgencia() {
        return codigoContactoUrgencia;
    }

    public void setCodigoContactoUrgencia(int codigoContactoUrgencia) {
        this.codigoContactoUrgencia = codigoContactoUrgencia;
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

    public String getNumeroContacto() {
        return numeroContacto;
    }

    public void setNumeroContacto(String numeroContacto) {
        this.numeroContacto = numeroContacto;
    }

    public int getCodigoPacientes() {
        return codigoPacientes;
    }

    public void setCodigoPacientes(int codigoPacientes) {
        this.codigoPacientes = codigoPacientes;
    }
      
    @Override
    public String toString (){
        return "" + codigoContactoUrgencia;
    }
    
}
