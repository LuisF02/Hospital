
package org.luisgarcia.sistema;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.luisgarcia.bean.MedicoEspecialidad;
import org.luisgarcia.controller.AreasController;
import org.luisgarcia.controller.CargosController;
import org.luisgarcia.controller.ContactoUrgenciaController;
import org.luisgarcia.controller.EspecialidadesController;
import org.luisgarcia.controller.HorariosController;
import org.luisgarcia.controller.MedicoEspecialidadController;
import org.luisgarcia.controller.MenuPrincipalController;
import org.luisgarcia.controller.PacientesController;
import org.luisgarcia.bean.Areas;
import org.luisgarcia.controller.ProgramadorController;
import org.luisgarcia.controller.ResponsableTurnoController;
import org.luisgarcia.controller.TelefonoMedicosController;
import org.luisgarcia.controller.TurnoController;
import org.luisgarcia.controller.medicosController;
import org.luisgarcia.db.Conexion;
import org.luisgarcia.controller.AreasController;
import org.luisgarcia.controller.ControlCitasController;
import org.luisgarcia.controller.RecetasController;

public class Principal extends Application {
    private final String PAQUETE_VISTA = "/org/luisgarcia/view/";
    private Stage escenarioPrincipal ;
    private Scene escena;
    
    @Override
    
    public void start(Stage escenarioPrincipal) throws IOException{
       this.escenarioPrincipal = escenarioPrincipal;
       escenarioPrincipal.setTitle("Hospital de Infectologia ");
       menuPrincipal();
       escenarioPrincipal.show();
       
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_BuscaMedico(?)");
                    procedimiento.setInt(1, 123);
                    ResultSet registro = procedimiento.executeQuery();
                while(registro. next ()){
                    System.out.print(registro.getInt("Codigo Medico"));
                    System.out.print(registro.getString("Licencia Medica"));
                }
        }catch (SQLException e){
                    e.printStackTrace();
                }  
    }
    
    public static void main (String [] args ){
        launch (args);
    }
     
    public void menuPrincipal() {
        try{ 
            MenuPrincipalController menuPrincipal = (MenuPrincipalController) cambiarEscena("MenuPrincipalView.fxml", 685,558);
            menuPrincipal.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
       
    }
    
    public void ventanaMedicos(){
        try{
            medicosController ventanaMedicos = (medicosController) cambiarEscena("MedicosView.fxml",760,541);
            ventanaMedicos.setEscenarioPrincipalMedicos(this);
        }catch (Exception e){
          e.printStackTrace();
        }
    }
    
    public void ventanaProgramadores(){
        try{     
            ProgramadorController ventanaProgramador = (ProgramadorController) cambiarEscena("ProgramadorView.fxml",611,432);
            ventanaProgramador.setEscenarioPrincipalProgramador(this);
        }catch (Exception e){
          e.printStackTrace();
        }
    }
    
    public void ventanaPacientes(){
        try{
            PacientesController ventanaPacientes = (PacientesController) cambiarEscena("PacientesView.fxml",695,489 );
            ventanaPacientes.setEscenarioPrincipalPacientes(this);
        }catch (Exception e){
          e.printStackTrace();
        }
    }
    
    public void ventanaContactoUrgencia (){
        try{
            ContactoUrgenciaController ventanaContactoUrgencia = (ContactoUrgenciaController) cambiarEscena("ContacoUrgenciaView.fxml",620, 434);
            ventanaContactoUrgencia.setEscenarioPrincipalContactoUrgencia(this);
        }catch (Exception e){
          e.printStackTrace();
        }
    }
    
  
    public void ventanaTelefonoMedicos (){
        try{
            TelefonoMedicosController ventanaTelefonoMedicos = (TelefonoMedicosController) cambiarEscena("TelefonoMedicoView.fxml",625, 444 );
            ventanaTelefonoMedicos.setEscenarioPrincipalTelefonoMedicos(this);
        }catch (Exception e){
          e.printStackTrace();
        }
    }
    
    public void ventanaTurno(){
        try{
            TurnoController ventanaTurno = (TurnoController) cambiarEscena("Turno.fxml" , 702, 541);
            ventanaTurno.setEscenerioPrincipalTurno(this);       
        }catch (Exception e){
          e.printStackTrace();
        }
    }
    
    public void ventanaResponsableTurno(){
        try{
            ResponsableTurnoController ventanaResponsableTurno =(ResponsableTurnoController) cambiarEscena("ResponsableTurnoView.fxml",600, 484 );
            ventanaResponsableTurno.setEscenarioPrincipalResponsableTurno(this);
        }catch (Exception e){
          e.printStackTrace();
        }
    }
    
    public void ventanaCargo(){
        try{
            CargosController ventanaCargo = (CargosController) cambiarEscena("CargosView.fxml", 576, 427);
            ventanaCargo.setEscenarioPrincipalCargos(this);            
        }catch (Exception e){
          e.printStackTrace();
        }
    }
    
    public void ventanaAreas(){
        try{
            AreasController ventanaAreas = (AreasController) cambiarEscena("AreasView.fxml", 568, 400);
            ventanaAreas.setEscenarioPrincipalAreas(this);
        }catch (Exception e){
          e.printStackTrace();
        }
    }
    
    public void ventanaMedicoEspacialidad(){
        try{
            MedicoEspecialidadController ventanaMedicoEspacialidad = (MedicoEspecialidadController) cambiarEscena("MedicoEspecialidadView.fxml",617,469 );
            ventanaMedicoEspacialidad.setEscenarioPrincipalMedicoEspecialida(this);
        }catch (Exception e){
          e.printStackTrace();
        }
    }
    
    public void ventanaHorarios(){
        try{
            HorariosController ventanaHorarios = (HorariosController) cambiarEscena("HorariosView.fxml", 661, 509);
            ventanaHorarios.setEscerarioPrincipalHorarios(this);           
        }catch (Exception e){
          e.printStackTrace();
        }
    }
    
    public void ventanaEspecialidad(){
        try{
            EspecialidadesController ventanaEspecialidad = (EspecialidadesController) cambiarEscena("EspecialidadesView.fxml",600,400);
            ventanaEspecialidad.setEscenarioPrincipalEspecialidades(this);           
        }catch (Exception e){
          e.printStackTrace();
        }
    }
    
    public void ventanaControlCitas(){
        try{
           ControlCitasController ventanaControlCitas = (ControlCitasController) cambiarEscena("ControlCitasView.fxml", 651, 494 );
           ventanaControlCitas.setEscenarioPrincipalControlCitas(this);
        }catch (Exception e){
          e.printStackTrace();
        }
    }
    
    public void ventanaRecetas(){
        try{
            RecetasController ventanaRecetas = (RecetasController) cambiarEscena("RecetasView.fxml", 533, 439);
            ventanaRecetas.setEscenarioPrincipalRecetas(this);
        }catch (Exception e){
          e.printStackTrace();
        }
    }
    
    
    
    public Initializable cambiarEscena (String fxml, int ancho, int alto ) throws Exception {
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo), ancho, alto );
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        
        return resultado;    
    }

   
    
}
    
    
    
    
    
    
    
    
    
    
    

