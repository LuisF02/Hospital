/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.luisgarcia.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.luisgarcia.bean.Especialidad;
import org.luisgarcia.bean.Horarios;
import org.luisgarcia.bean.MedicoEspecialidad;
import org.luisgarcia.bean.Medicos;
import org.luisgarcia.db.Conexion;
import org.luisgarcia.sistema.Principal;

/**
 *
 * @author luisgarcia
 */
public class MedicoEspecialidadController implements Initializable{
    private enum operaciones{Nuevo,Guardar,Editar,Actualizar,Eliminar,Cancelar,Ninguno} 
    private operaciones tipoDeOperaciones = operaciones.Ninguno;
    private ObservableList <MedicoEspecialidad> listaMedicosEspecialidad;
    private ObservableList <Medicos> listaMedico;
    private ObservableList <Horarios> listaHorarios;
    private ObservableList <Especialidad> listaEspecialidades;
    
    @FXML private ComboBox cmbCodigo;
    @FXML private ComboBox cmbEspecialidad;
    @FXML private ComboBox cmbHorario;
    @FXML private ComboBox cmbMedico;
    @FXML private Button btnAgregar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    @FXML private TableView tblEspecialidadMedicos;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colEspecialidad;
    @FXML private TableColumn colHorario;
    @FXML private TableColumn colMedico;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {       
        cargarDatos();
        cmbCodigo.setItems(getMedicoEspecialidad());
        cmbMedico.setItems(getMedicos());
        cmbHorario.setItems(getHorario());
        cmbEspecialidad.setItems(getEspecialidad());
    }
    
    public void cargarDatos(){
        tblEspecialidadMedicos.setItems(getMedicoEspecialidad());
        colCodigo.setCellValueFactory(new PropertyValueFactory <MedicoEspecialidad, Integer>("codigoMedicoEspecialidad"));
        colEspecialidad.setCellValueFactory(new PropertyValueFactory <MedicoEspecialidad,Integer>("codigoEspecialidad"));
        colHorario.setCellValueFactory(new PropertyValueFactory <MedicoEspecialidad, Integer>("codigoHorario"));
        colMedico.setCellValueFactory(new PropertyValueFactory <MedicoEspecialidad, Integer> ("codigoMedico"));
        
    } 
    
    public ObservableList<MedicoEspecialidad> getMedicoEspecialidad(){
        ArrayList lista = new ArrayList<MedicoEspecialidad>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call Elistar_medico_especialidad}");
            ResultSet resultado =procedimiento.executeQuery();
            
            while(resultado.next()){
               lista.add(new MedicoEspecialidad(resultado.getInt("codigoMedicoEspecialidad"),
               resultado.getInt("codigoMedico"),
               resultado.getInt("codigoEspecialidad"),
               resultado.getInt("codigoHorario")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return listaMedicosEspecialidad = FXCollections.observableList(lista);
    }
    
    private MedicoEspecialidad buscarMedicoEspecialidad(int codigoMedicoEspecialidad){
        MedicoEspecialidad resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_buscarMedicoEspecialida (?)}");
            procedimiento.setInt(1,codigoMedicoEspecialidad);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado = new MedicoEspecialidad(registro.getInt("codigoMedicoEspecialidad"),
                registro.getInt("codigoMedico"),
                registro.getInt("codigoEspecialidad"),
                registro.getInt("codigoHorario"));  
            }
       }catch(SQLException e){
            e.printStackTrace();
            e.getMessage();
        }
        return resultado;
    } 
    
    public ObservableList <Medicos> getMedicos(){
        ArrayList <Medicos> lista = new ArrayList<Medicos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListaMedicos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Medicos(resultado.getInt("codigoMedico"),
                        resultado.getInt("licenciaMedica"), 
                        resultado.getString("nombres"),
                        resultado.getString("apellidos"), 
                        resultado.getDate("horaEntrada"), 
                        resultado.getDate("horaSalidad"), 
                        resultado.getString("sexo") ));
            } 
                
        }catch(SQLException e) {
            e.printStackTrace();  
        } 
            return listaMedico = FXCollections.observableList (lista)  ;
    }
    
    public Medicos buscarMedicos (int codigoMedico){
        Medicos resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscaMedico(?)}");
            procedimiento.setInt(1, codigoMedico);
            ResultSet registro = procedimiento.executeQuery();
            
            while (registro.next()){
                resultado = new Medicos (registro.getInt("codigoMedico"),
                registro.getInt("LicenciaMedica"),
                registro.getString("nombres"),
                registro.getString("apellidos"),
                registro.getDate("horaEntrada"),
                registro.getDate("horaSalidad"),
                registro.getString("sexo"));
                        
            } 
                
        }catch(SQLException e) {
            e.printStackTrace();  
        } 
        return resultado;
    }
    
    
    public ObservableList <Horarios> getHorario() {
        ArrayList<Horarios> lista = new ArrayList<Horarios>();
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Elistarhorarios}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Horarios(resultado.getInt("codigoHorario"),
                resultado.getString("horarioInicio"),
                resultado.getString("horarioSalida"),
                resultado.getInt("lunes"),
                resultado.getInt("martes"),
                resultado.getInt("miercoles"),
                resultado.getInt("jueves"),
                resultado.getInt("viernes")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
      return listaHorarios = FXCollections.observableList(lista);
    }
    
    public Horarios buscarHorario(int codigoHorario){
        Horarios resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarHorarios (?)}");
            procedimiento.setInt(1, codigoHorario);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
            resultado = new Horarios(registro.getInt("codigoHorario"),
            registro.getString("horarioInicio"),
            registro.getString("horarioSalida"),
            registro.getInt("lunes"),
            registro.getInt("martes"),
            registro.getInt("miercoles"),
            registro.getInt("jueves"),
            registro.getInt("viernes")); 
            }
        }catch(SQLException e){
            e.printStackTrace();
            e.getMessage();
        
        }
        return resultado;
    } 
    
    
    public ObservableList<Especialidad> getEspecialidad(){
        ArrayList lista = new ArrayList<Especialidad>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call Elistar_especialidades}");
            ResultSet resultado =procedimiento.executeQuery();
            
            while(resultado.next()){
               lista.add(new Especialidad(resultado.getInt("codigoEspecialidad"),
               resultado.getString("nombreEspecialidad")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return listaEspecialidades = FXCollections.observableList(lista);
    }
    
    private Especialidad buscarEspecialidad(int codigoEspecialidad){
        Especialidad resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarEspecialidades(?)}");
            procedimiento.setInt(1,codigoEspecialidad);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado = new Especialidad(registro.getInt("codigoEspecialidad"),
                registro.getString("nombreEspecialidad"));
            
            }
       }catch(SQLException e){
            e.printStackTrace();
            e.getMessage();
        }
        return resultado;
    }
    
    public void seleccionar(){
        MedicoEspecialidad medicoEspecialidadSeleccionado = (MedicoEspecialidad) tblEspecialidadMedicos.getSelectionModel().getSelectedItem();
        cmbCodigo.getSelectionModel().select(medicoEspecialidadSeleccionado.toString());
        
        int codigoEspecialidad = medicoEspecialidadSeleccionado.getCodigoEspecialidad();
        Especialidad especialidadSeleccionado = buscarEspecialidad(codigoEspecialidad);
        cmbEspecialidad.getSelectionModel().select(especialidadSeleccionado.getCodigoEspecialidad() + " - " + especialidadSeleccionado.getNombreEspecialidad());
    
        int codigoHorario= medicoEspecialidadSeleccionado.getCodigoHorario();
        Horarios horarioSeleccionado = buscarHorario(codigoHorario);
        cmbHorario.getSelectionModel().select(horarioSeleccionado.getCodigoHorario() + " - " + horarioSeleccionado.getHorarioInicio() + " " + horarioSeleccionado.getHorarioSalida());
    
        int codigoMedico = medicoEspecialidadSeleccionado.getCodigoMedico();
        Medicos medicoSeleccionado = buscarMedicos(codigoMedico);
        cmbMedico.getSelectionModel().select(medicoSeleccionado.getCodigoMedico()+ " - " + medicoSeleccionado.getNombres() + " " + medicoSeleccionado.getApellidos());
    }
    
    
    
    
    
    
    
    

    private Principal EscenarioPrincipalMedicoEspecialida;

    
    public Principal getEscenarioPrincipalMedicoEspecialida() {
        return EscenarioPrincipalMedicoEspecialida;
    }

    public void setEscenarioPrincipalMedicoEspecialida(Principal EscenarioPrincipalMedicoEspecialida) {
        this.EscenarioPrincipalMedicoEspecialida = EscenarioPrincipalMedicoEspecialida;
    }
    
    public void menuPrincipalProgramador(){
        EscenarioPrincipalMedicoEspecialida.ventanaMedicos();
    }
    
    public void ventanaEspecialidad(){
        EscenarioPrincipalMedicoEspecialida.ventanaEspecialidad();
    }
    
   
    
    
        

    
}
