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
import org.luisgarcia.bean.Cargos;
import org.luisgarcia.db.Conexion;
import org.luisgarcia.sistema.Principal;

/**
 *
 * @author luisgarcia
 */
public class CargosController implements Initializable{
    private enum operaciones{Nuevo,Guardar,Editar,Actualizar,Eliminar,Cancelar,Ninguno} 
    private operaciones tipoDeOperaciones = operaciones.Ninguno;
    private ObservableList <Cargos> listaCargos;    
        
    private Principal EscenarioPrincipalCargos;
    
    @FXML private ComboBox cmbCodigoCargo;
    @FXML private TextField txtNombreCargo;
    @FXML private Button btnAgregar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    @FXML private TableView tblCargo;
    @FXML private TableColumn colCodigoCargo;
    @FXML private TableColumn colNombreCargo; 
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {       
        cargarDatos();
    cmbCodigoCargo.setItems(getCargo());
    
    }
    
    private void cargarDatos(){
        tblCargo.setItems(getCargo());
        colCodigoCargo.setCellValueFactory(new PropertyValueFactory<Cargos,Integer>("codigoCargo"));
        colNombreCargo.setCellValueFactory(new PropertyValueFactory<Cargos,String>("nombreCargo"));
    }
    
    
    public ObservableList<Cargos> getCargo(){
        ArrayList lista = new ArrayList<Cargos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListaCargos}");
            ResultSet resultado =procedimiento.executeQuery();
            
            while(resultado.next()){
               lista.add(new Cargos(resultado.getInt("codigoCargo"),
               resultado.getString("nombreCargo")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return listaCargos = FXCollections.observableList(lista);
    }
    
    private Cargos buscarCargo(int codigoCargo){
        Cargos resultado = null;
            try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarCargos (?)}");
            procedimiento.setInt(1,codigoCargo);
            ResultSet registro = procedimiento.executeQuery();
            
                while(registro.next()){
                resultado = new Cargos(registro.getInt("codigoCargo"),
                registro.getString("nombreCargo"));
            
                }
            }catch(SQLException e){
            e.printStackTrace();
            e.getMessage();
        
        }
        return resultado;
    }
    
    public void seleccionar(){
        Cargos cargoSeleccionado = (Cargos)tblCargo.getSelectionModel().getSelectedItem();
        cmbCodigoCargo.getSelectionModel().select(cargoSeleccionado.toString());
        txtNombreCargo.setText(cargoSeleccionado.getNombreCargo());
    }
    
    public void nuevo(){
        switch(tipoDeOperaciones){
            case  Ninguno:
                cmbCodigoCargo.setDisable(true);
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
    
    private void activar() {
       txtNombreCargo.setDisable(false);
        
       cmbCodigoCargo.setEditable(true);
       txtNombreCargo.setEditable(true);        
    } 
    
    private void limpiar() {
       txtNombreCargo.setText("");
    } 
    
    private void ingresar() {
        Cargos registro = new Cargos();
        registro.setNombreCargo(txtNombreCargo.getText());
        try{
          PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Cargos (?)}"); 
          procedimiento.setString(1,registro.getNombreCargo());
          procedimiento.execute();
          listaCargos.add(registro);
        }catch(SQLException e){
           e.printStackTrace();   
        }
    }
        
    
    private void desactivar() {
        cmbCodigoCargo.setDisable(true);
        txtNombreCargo.setDisable(true);
        btnAgregar.setDisable(false);
        btnEliminar.setDisable(false);
        btnModificar.setDisable(false);
        btnReporte.setDisable(false);

        
        cmbCodigoCargo.setEditable(false);
        txtNombreCargo.setEditable(false);
    }      

    
    public void editar() throws SQLException{
        switch(tipoDeOperaciones){
            case Ninguno:
                if (tblCargo.getSelectionModel().getSelectedItem() != null){
                    cmbCodigoCargo.setDisable(false);
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
       PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ModificarCargos (?,?)}");
       Cargos registro= (Cargos) tblCargo.getSelectionModel().getSelectedItem();
        
       registro.setCodigoCargo(registro.getCodigoCargo());
       registro.setNombreCargo(txtNombreCargo.getText());
        
       procedimiento.setInt(1,registro.getCodigoCargo());
       procedimiento.setString(2,registro.getNombreCargo());
       procedimiento.execute();
       listaCargos.add(registro); 
    }
    
    
    public void eliminar(){
        if(tipoDeOperaciones == operaciones.Guardar){
            cancelar();
            desactivar();
            tipoDeOperaciones = operaciones.Ninguno;
        }else{
            tipoDeOperaciones = operaciones.Eliminar;
            if(tblCargo.getSelectionModel().getSelectedItem() != null){
                int respuesta = JOptionPane.showConfirmDialog(null,"Seguro que desea eliminar el registro?","Eliminar Medico",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if(respuesta == JOptionPane.YES_OPTION){
                 try{
                     PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarCargos (?)}");
                     procedimiento.setInt(1,((Cargos)tblCargo.getSelectionModel().getSelectedItem()).getCodigoCargo());
                     procedimiento.execute();
                     listaCargos.remove(tblCargo.getSelectionModel().getSelectedIndex());
                     limpiar();
                     cargarDatos();
                     tipoDeOperaciones = operaciones.Ninguno;
                    }catch(SQLException e){
                        e.printStackTrace();
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null,"Debe Seleccionar un registro a eliminar");
            }           
        } 
    } 
    
    public void cancelar(){
        btnAgregar.setText("Nuevo");
        btnEliminar.setText("Eliminar");
        btnReporte.setDisable(false);
        btnModificar.setDisable(false);
    }
    

    public Principal getEscenarioPrincipalCargos() {
        return EscenarioPrincipalCargos;
    }

    public void setEscenarioPrincipalCargos(Principal EscenarioPrincipalCargos) {
        this.EscenarioPrincipalCargos = EscenarioPrincipalCargos;
    }
    
    
    
    public void menuPrincipalProgramador(){
        EscenarioPrincipalCargos.ventanaResponsableTurno();
    }
    
    
}
