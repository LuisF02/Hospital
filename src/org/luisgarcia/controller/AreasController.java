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
import org.luisgarcia.bean.Areas;
import org.luisgarcia.db.Conexion;
import org.luisgarcia.sistema.Principal;

/**
 *
 * @author luisgarcia
 */
public class AreasController implements Initializable{
    private enum operaciones{Nuevo,Guardar,Editar,Actualizar,Eliminar,Cancelar,Ninguno} 
    private operaciones tipoDeOperaciones = operaciones.Ninguno;   
    private ObservableList <Areas> listarAreas;
    
    private Principal EscenarioPrincipalAreas;
    
    @FXML private ComboBox cmbCodigoArea;
    @FXML private TextField txtNombreArea;
    @FXML private Button btnAgregar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    @FXML private TableView tblArea;
    @FXML private TableColumn colCodigoArea;
    @FXML private TableColumn colNombreArea;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {       
        cargarDatos();
        cmbCodigoArea.setItems(getAreas());
    }
    
    public void cargarDatos(){
        tblArea.setItems(getAreas());
        colCodigoArea.setCellValueFactory(new PropertyValueFactory <Areas, Integer >("codigoArea"));
        colNombreArea.setCellValueFactory(new PropertyValueFactory <Areas , String >("nombreArea"));
        
    }
    
    public ObservableList<Areas> getAreas(){
        ArrayList lista = new ArrayList<Areas>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarAreas}");
            ResultSet resultado =procedimiento.executeQuery();
            
            while(resultado.next()){
               lista.add(new Areas(resultado.getInt("codigoArea"),
               resultado.getString("nombreArea")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return listarAreas = FXCollections.observableList(lista);
    }
    
    private Areas buscarArea(int codigoArea){
        Areas resultado = null;
            try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarAreas (?)}");
            procedimiento.setInt(1,codigoArea);
            ResultSet registro = procedimiento.executeQuery();
            
                while(registro.next()){
                resultado = new Areas(registro.getInt("codigoArea"),
                registro.getString("nombreArea"));
            
                }
            }catch(SQLException e){
            e.printStackTrace();
            e.getMessage();
        
        }
        return resultado;
    }
    
    
    public void seleccionar(){
        Areas areaSeleccionada = (Areas)tblArea.getSelectionModel().getSelectedItem();
        cmbCodigoArea.getSelectionModel().select(areaSeleccionada.toString());
        txtNombreArea.setText(areaSeleccionada.getNombreArea());
    }
    
    public void nuevo(){
        switch(tipoDeOperaciones){
            case  Ninguno:
                cmbCodigoArea.setDisable(true);
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
                cancelar();
                tipoDeOperaciones = operaciones.Ninguno;
                cargarDatos();
                break;
        }
    } 
    
    private void ingresar() {
       Areas registro = new Areas();
       registro.setNombreArea(txtNombreArea.getText());
       try{
          PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Areas (?)}"); 
          procedimiento.setString(1,registro.getNombreArea());
          procedimiento.execute();
          listarAreas.add(registro);
       }catch(SQLException e){
           e.printStackTrace();   
       }
    }
    
    private void limpiar() {
       txtNombreArea.setText("");
    }        
    
    private void activar() {
       txtNombreArea.setDisable(false);
        
       cmbCodigoArea.setEditable(true);
       txtNombreArea.setEditable(true);        
    }    
    
    private void desactivar() {
        cmbCodigoArea.setDisable(true);
        txtNombreArea.setDisable(true);
        btnAgregar.setDisable(false);
        btnEliminar.setDisable(false);
        btnModificar.setDisable(false);
        btnReporte.setDisable(false);
        
        
        cmbCodigoArea.setEditable(false);
        txtNombreArea.setEditable(false);
    } 
    
    public void cancelar(){
        btnAgregar.setText("Nuevo");
        btnEliminar.setText("Eliminar");
        btnReporte.setDisable(false);
        btnModificar.setDisable(false);
    }
    
    public void editar() throws SQLException{
        switch(tipoDeOperaciones){
            case Ninguno:
                if (tblArea.getSelectionModel().getSelectedItem() != null){
                    cmbCodigoArea.setDisable(false);
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
    
    private void actualizar() throws SQLException {
       PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ModificarAreas (?,?)}");
       Areas registro= (Areas) tblArea.getSelectionModel().getSelectedItem();
        
       registro.setCodigoArea(registro.getCodigoArea());
       registro.setNombreArea((txtNombreArea.getText()));
        
       procedimiento.setInt(1,registro.getCodigoArea());
       procedimiento.setString(2,registro.getNombreArea());
       procedimiento.execute();
       listarAreas.add(registro); 
    }
    
    public void eliminar(){
        if(tipoDeOperaciones == operaciones.Guardar){
            cancelar();
            desactivar();
            tipoDeOperaciones = operaciones.Ninguno;
        }else{
            tipoDeOperaciones = operaciones.Eliminar;
            if(tblArea.getSelectionModel().getSelectedItem() != null){
                int respuesta = JOptionPane.showConfirmDialog(null,"Seguro que desea eliminar el registro?","Eliminar Medico",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if(respuesta == JOptionPane.YES_OPTION){
                 try{
                     PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarAreas (?)}");
                     procedimiento.setInt(1,((Areas)tblArea.getSelectionModel().getSelectedItem()).getCodigoArea());
                     procedimiento.execute();
                     listarAreas.remove(tblArea.getSelectionModel().getSelectedIndex());
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
    
    

    public Principal getEscenarioPrincipalAreas() {
        return EscenarioPrincipalAreas;
    }

    public void setEscenarioPrincipalAreas(Principal EscenarioPrincipalAreas) {
        this.EscenarioPrincipalAreas = EscenarioPrincipalAreas;
    }
    
    public void MenuPrincipalRturno(){
        EscenarioPrincipalAreas.ventanaResponsableTurno();
    }
    
    
    
}
