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
import org.luisgarcia.bean.ControlCitas;
import org.luisgarcia.bean.Recetas;
import org.luisgarcia.db.Conexion;
import org.luisgarcia.sistema.Principal;

/**
 *
 * @author programacion
 */
public class RecetasController implements Initializable{
    
    private enum operaciones{Nuevo, Guardar, Editar, Actualizar, Cancelar,Eliminar, Ninguno}
    private operaciones tipoDeOperaciones = operaciones.Ninguno;
    private ObservableList <Recetas> listaRecetas;
    private ObservableList <ControlCitas> listaControlCitas;
   
    private Principal EscenarioPrincipalRecetas;
    
    @FXML private ComboBox cmbCodReceta;
    @FXML private ComboBox cmbControlCitas;
    @FXML private TextField txtDescripcionReceta;
    @FXML private TableView tblRecetas;
    @FXML private TableColumn codReceta;
    @FXML private TableColumn descripcionReceta;
    @FXML private TableColumn codCita;
    @FXML private Button btnagregar;
    @FXML private Button btnmodificar;
    @FXML private Button btneliminar;
    @FXML private Button btnreporte;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {       
        cargarDatos();
        
        cmbCodReceta.setItems(getReceta());
        cmbControlCitas.setItems(getControlCitas());
    }
    
    
    public void cargarDatos(){
        tblRecetas.setItems(getReceta());
        codReceta.setCellValueFactory(new PropertyValueFactory <Recetas, Integer>("codigoReceta"));
        descripcionReceta.setCellValueFactory(new PropertyValueFactory <Recetas, String>("descripcionReceta"));
        codCita.setCellValueFactory(new PropertyValueFactory <Recetas, Integer>("codigoControlCita"));
    }
    
    
    public ObservableList <Recetas> getReceta(){
        ArrayList <Recetas> lista = new ArrayList <Recetas>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarReceta}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Recetas(resultado.getInt("codigoReceta"),
                        resultado.getString("descripcionReceta"),
                        resultado.getInt("codigoControlCita") )); 
            }
        }catch(SQLException e) {
            e.printStackTrace();  
        }
            return listaRecetas = FXCollections.observableList (lista);
    }
    
    public void seleccionar(){
        cmbCodReceta.getSelectionModel().select(buscarRecetas(((Recetas)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoControlCita()));
        txtDescripcionReceta.setText(((Recetas)tblRecetas.getSelectionModel().getSelectedItem()).getDescripcionReceta());
            
    }
    
    public Recetas buscarRecetas (int codigoReceta){
        Recetas resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_buscarReceta(?)}");
            procedimiento.setInt(1, codigoReceta);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Recetas (registro.getInt("codigoReceta"),
                registro.getString("descripcionReceta"),
                registro.getInt("codigoControlCita"));
            }
        }catch(SQLException e) {
            e.printStackTrace();  
        }
            return resultado;
    }
    
    public ObservableList <ControlCitas> getControlCitas(){
        ArrayList <ControlCitas> lista = new ArrayList <ControlCitas>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListaControlCitas}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new ControlCitas(resultado.getInt("codigoControlCita"),
                        resultado.getDate("fecha"),
                        resultado.getString("horaInicio"),
                        resultado.getString("horaFin"),
                        resultado.getInt("codigoMedico"),
                        resultado.getInt("codigoPacientes") )); 
            }
        }catch(SQLException e) {
            e.printStackTrace();  
        }
            return listaControlCitas = FXCollections.observableList (lista);
    }
    
    public ControlCitas buscarControlCitas (int codigoControlCita){
        ControlCitas resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscaControlCitas(?)}");
            procedimiento.setInt(1, codigoControlCita);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new ControlCitas (registro.getInt("codigoControlCita"),
                registro.getDate("fecha"),
                registro.getString("horaInicio"),
                registro.getString("horaFin"),
                registro.getInt("codigoMedico"),
                registro.getInt("codigoPacientes"));
            }
        }catch(SQLException e) {
            e.printStackTrace();  
        }
            return resultado;
    }
    
    
    public void nuevo(){
        switch (tipoDeOperaciones){
            case Ninguno:
                activar();
                limpiar();
                btnagregar.setText("Guardar");
                btneliminar.setText("Cancelar");
                btnmodificar.setDisable(true);
                btnreporte.setDisable(true);
                tipoDeOperaciones = operaciones.Guardar;
            break;
            
            case Guardar:
                ingresar();
                desactivar();
                btnagregar.setText("Nuevo");
                btneliminar.setText("Eliminar");
                btnmodificar.setDisable(false);
                btnreporte.setDisable(false);
                tipoDeOperaciones = operaciones.Ninguno;
            break;
        }
    }
        
    public void activar(){
        cmbCodReceta.setDisable(false);
        txtDescripcionReceta.setDisable(false);
        
        cmbCodReceta.setEditable(true);
        txtDescripcionReceta.setEditable(true);                 
    }
    
    public void limpiar (){
        txtDescripcionReceta.setText("");
    }
    
    private void ingresar() {
       Recetas registro = new Recetas();
       registro.setDescripcionReceta(txtDescripcionReceta.getText());
       registro.setCodigoControlCita(((ControlCitas) cmbControlCitas.getSelectionModel().getSelectedItem()).getCodigoControlCita());
       try{
            limpiar();
            cargarDatos();
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Recetas (?,?)}"); 
            procedimiento.setString(1,registro.getDescripcionReceta());
            procedimiento.setInt(2, registro.getCodigoReceta());
            procedimiento.execute();
            listaRecetas.add(registro);
        }catch(SQLException e){
            e.printStackTrace();   
        }
    }
    
    
    public void desactivar(){
        cmbCodReceta.setDisable(true);
        txtDescripcionReceta.setDisable(true);
        btnagregar.setDisable(true);
        btneliminar.setDisable(true);
        btnmodificar.setDisable(true);
        btnreporte.setDisable(true);
              
        cmbCodReceta.setEditable(false);
        txtDescripcionReceta.setEditable(false);
    }
        
    
    
    
    
    public Principal getEscenarioPrincipalRecetas() {
        return EscenarioPrincipalRecetas;
    }

    public void setEscenarioPrincipalRecetas(Principal EscenarioPrincipalRecetas) {
        this.EscenarioPrincipalRecetas = EscenarioPrincipalRecetas;
    }
    
    
    public void MenuPrincipalControlCitas(){
        EscenarioPrincipalRecetas.ventanaControlCitas();
    }
    
    
}
