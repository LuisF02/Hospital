/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.luisgarcia.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import org.luisgarcia.sistema.Principal;


/**
 *
 * @author luisgarcia
 */
public class TurnoController implements Initializable{
      
    private Principal EscenerioPrincipalTurno;

    
    
    public Principal getEscenerioPrincipalTurno() {
        return EscenerioPrincipalTurno;
    }

    public void setEscenerioPrincipalTurno(Principal EscenerioPrincipalTurno) {
        this.EscenerioPrincipalTurno = EscenerioPrincipalTurno;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {       
    }
    
    public void menuPrincipal(){
        EscenerioPrincipalTurno.menuPrincipal();
    }
    public void ventanaResponsableTurno(){
        EscenerioPrincipalTurno.ventanaResponsableTurno();
    }
    
    
    
    
}
