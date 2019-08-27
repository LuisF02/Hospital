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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.luisgarcia.bean.Especialidad;
import org.luisgarcia.db.Conexion;
import org.luisgarcia.sistema.Principal;

/**
 *
 * @author luisgarcia
 */
public class EspecialidadesController implements Initializable{
    private enum operaciones{Nuevo,Guardar,Editar,Actualizar,Eliminar,Cancelar,Ninguno} 
    private operaciones tipoDeOperaciones = operaciones.Ninguno;   
    private ObservableList <Especialidad> listaEspecialidades;
   
    private Principal EscenarioPrincipalEspecialidades;
    
    @FXML private ComboBox cmbCodigoEspecialidad;
    @FXML private TextField txtNombreEspecialidad;
    @FXML private TableView tblEspecialidad;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colNombre;
    @FXML private Button btnAgregar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {       
            cargarDatos();
            cmbCodigoEspecialidad.setItems(getEspecialidad());       
    }
    
    private void cargarDatos(){
        tblEspecialidad.setItems(getEspecialidad());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Especialidad,Integer>("codigoEspecialidad"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Especialidad,String>("nombreEspecialidad"));
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
    
    public void seleccionar(){
        Especialidad especialidadSeleccionada = (Especialidad)tblEspecialidad.getSelectionModel().getSelectedItem();
        cmbCodigoEspecialidad.getSelectionModel().select(especialidadSeleccionada.toString());
        txtNombreEspecialidad.setText(especialidadSeleccionada.getNombreEspecialidad());
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
    
    private void ingresar() {
        Especialidad registro = new Especialidad();
        registro.setNombreEspecialidad(txtNombreEspecialidad.getText());
        try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Especialidades (?)}"); 
           procedimiento.setString(1,registro.getNombreEspecialidad());
           procedimiento.execute();
           listaEspecialidades.add(registro);
        }catch(SQLException e){
            e.printStackTrace();   
        }    
    }
    
    private void limpiar() {
       txtNombreEspecialidad.setText("");
    }        
    
    private void activar() {
       txtNombreEspecialidad.setDisable(false);
        
       cmbCodigoEspecialidad.setEditable(true);
       txtNombreEspecialidad.setEditable(true);        
    } 
    
    private void desactivar() {
        cmbCodigoEspecialidad.setDisable(true);
        txtNombreEspecialidad.setDisable(true);
        btnAgregar.setDisable(false);
        btnEliminar.setDisable(false);
        btnModificar.setDisable(false);
        btnReporte.setDisable(false);
        
        cmbCodigoEspecialidad.setEditable(false);
        txtNombreEspecialidad.setEditable(false);
    }  
    
    public void cancelar(){
        btnAgregar.setText("Nuevo");
        btnEliminar.setText("Eliminar");
        btnReporte.setDisable(false);
        btnModificar.setDisable(false);
    }
    
    public void nuevo(){
        switch(tipoDeOperaciones){
            case  Ninguno:
                cmbCodigoEspecialidad.setDisable(true);
                tipoDeOperaciones = operaciones.Guardar;
                activar();
                limpiar();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnModificar.setDisable(true);
                btnReporte.setDisable(true);
            break;
            case Guardar:
                ingresar();
                desactivar();
                btnAgregar.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnModificar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperaciones = operaciones.Ninguno;
                cargarDatos();
            break;
        }
    }
    
    private void actualizar() throws SQLException {
       PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call Modificar_especialidades (?,?)}");
       Especialidad registro= (Especialidad) tblEspecialidad.getSelectionModel().getSelectedItem();
        
       registro.setCodigoEspecialidad(registro.getCodigoEspecialidad());
       registro.setNombreEspecialidad((txtNombreEspecialidad.getText()));
        
       procedimiento.setInt(1,registro.getCodigoEspecialidad());
       procedimiento.setString(2,registro.getNombreEspecialidad());
       procedimiento.execute();
       listaEspecialidades.add(registro); 
    }
    
    public void edit() throws SQLException{
        switch(tipoDeOperaciones){
            case Ninguno:
                if (tblEspecialidad.getSelectionModel().getSelectedItem() != null){
                    cmbCodigoEspecialidad.setDisable(false);
                    btnModificar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperaciones = operaciones.Actualizar;
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activar();
                }
            break;
            
            case Actualizar:
                actualizar();
                btnModificar.setText("Editar");
                btnReporte.setText("Reportar");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                desactivar();
                cargarDatos();
                tipoDeOperaciones = operaciones.Ninguno;       
            break;
        }
    }
    
    public void eliminar(){
        if(tipoDeOperaciones == operaciones.Guardar){
            cancelar();
            desactivar();
            tipoDeOperaciones = operaciones.Ninguno;
        }else{
            tipoDeOperaciones = operaciones.Eliminar;
            if(tblEspecialidad.getSelectionModel().getSelectedItem() != null){
                int respuesta = JOptionPane.showConfirmDialog(null,"Seguro que desea eliminar el registro?","Eliminar Medico",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if(respuesta == JOptionPane.YES_OPTION){
                 try{
                     PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call Eliminar_especialidades (?)}");
                     procedimiento.setInt(1,((Especialidad)tblEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
                     procedimiento.execute();
                     listaEspecialidades.remove(tblEspecialidad.getSelectionModel().getSelectedIndex());
                     limpiar();
                     cargarDatos();
                     tipoDeOperaciones = operaciones.Ninguno;
                    }catch(SQLException e){
                        e.printStackTrace();
                    }
                }
            }
            else{
                JOptionPane.showMessageDialog(null,"Debe Seleccionar un registro a eliminar");
            }           
        } 
    }
    

    public Principal getEscenarioPrincipalEspecialidades() {
        return EscenarioPrincipalEspecialidades;
    }

    public void setEscenarioPrincipalEspecialidades(Principal EscenarioPrincipalEspecialidades) {
        this.EscenarioPrincipalEspecialidades = EscenarioPrincipalEspecialidades;
    }
    
    
    public void menuPrincipalMedicoEspecialidad(){
        EscenarioPrincipalEspecialidades.ventanaMedicoEspacialidad();
    }
    
    
    
    
    
}
