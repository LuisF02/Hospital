
package org.luisgarcia.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.luisgarcia.sistema.Principal;

/**
 *
 * @author programacion
 */
public class ProgramadorController implements Initializable {
    
    
    private Principal EscenarioPrincipalProgramador;
    
    public Principal getEscenarioPrincipalProgramador() {
        return EscenarioPrincipalProgramador;
    }

    public void setEscenarioPrincipalProgramador(Principal EscenarioPrincipalProgramador) {
        this.EscenarioPrincipalProgramador = EscenarioPrincipalProgramador;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
           
    }
    
    public void menuPrincipalProgramador (){
        EscenarioPrincipalProgramador.menuPrincipal();
    }
    
    
    
    

    
}
