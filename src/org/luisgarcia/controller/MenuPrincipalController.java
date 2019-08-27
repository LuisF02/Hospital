
package org.luisgarcia.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.luisgarcia.sistema.Principal;

public class MenuPrincipalController implements Initializable{
    private Principal escenarioPrincipal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
   
     public void ventanaMedico1(){
      escenarioPrincipal.ventanaMedicos(); 
    }
     
    public void ventanaProgramadores(){
        escenarioPrincipal.ventanaProgramadores();
    } 
    
    public void ventanaPacientes(){
        escenarioPrincipal.ventanaPacientes();   
    }
    
    public void ventanaTurnos(){
        escenarioPrincipal.ventanaTurno();
    }
    
    
    
    
}
