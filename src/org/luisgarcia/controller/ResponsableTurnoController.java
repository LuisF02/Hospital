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
import org.luisgarcia.bean.Areas;
import org.luisgarcia.bean.Cargos;
import org.luisgarcia.bean.ResponsableTurno;
import org.luisgarcia.db.Conexion;
import org.luisgarcia.sistema.Principal;

/**
 *
 * @author luisgarcia
 */
    public class ResponsableTurnoController implements Initializable{
        private enum operaciones{Nuevo,Guardar,Editar,Actualizar,Eliminar,Cancelar,Ninguno} 
        private operaciones tipoDeOperaciones = operaciones.Ninguno; 
        private ObservableList <ResponsableTurno> listaResponsableTurno1;
        private ObservableList <Cargos> listaCargos;
        private ObservableList <Areas> listarAreas;
        
        private Principal EscenarioPrincipalResponsableTurno;

        @FXML private ComboBox cmbCodigo;
        @FXML private ComboBox cmbArea;
        @FXML private ComboBox cmbCargo;
        @FXML private TextField txtNombre;
        @FXML private TextField txtApellido;
        @FXML private TextField txtTelefono;
        @FXML private TableView tblResponsable;
        @FXML private TableColumn colCodigo;
        @FXML private TableColumn colNombre;
        @FXML private TableColumn colApellido;
        @FXML private TableColumn colTelefono;
        @FXML private TableColumn colArea;
        @FXML private TableColumn colCargo;
        @FXML private Button btnAgregar;
        @FXML private Button btnModificar;
        @FXML private Button btnEliminar;
        @FXML private Button btnReporte;

        
        
        public void activar(){
          txtNombre.setDisable(false);
          txtApellido.setDisable(false);
          txtTelefono.setDisable(false);
          
          txtNombre.setEditable(true);
          txtApellido.setEditable(true);
          txtTelefono.setEditable(true);
          
     
        }

        public void desactivar(){
          txtNombre.setDisable(true);
          txtApellido.setDisable(true);
          txtTelefono.setDisable(true);
          
          txtNombre.setEditable(false);
          txtApellido.setEditable(false);
          txtTelefono.setEditable(false);
        }
        
        public void Limpiar(){
          txtNombre.setText("");
          txtApellido.setText("");
          txtTelefono.setText(""); 
        }
        
                
            
        
        @Override
        public void initialize(URL location, ResourceBundle resources) {       
            cargarDatos();
            cmbCodigo.setItems(getResponsable1());
            cmbCargo.setItems(getCargo()); 
            cmbArea.setItems(getAreas());  
        
        }
        
        public void cargarDatos(){
        tblResponsable.setItems(getResponsable1());
        colCodigo.setCellValueFactory(new PropertyValueFactory <ResponsableTurno, Integer>("codigoResposableTurno"));
        colNombre.setCellValueFactory(new PropertyValueFactory <ResponsableTurno,String>("nombreResposable"));
        colApellido.setCellValueFactory(new PropertyValueFactory <ResponsableTurno,String>("apellidoResposable"));
        colTelefono.setCellValueFactory(new PropertyValueFactory <ResponsableTurno,String>("telefonoResposable"));   
        colArea.setCellValueFactory(new PropertyValueFactory <ResponsableTurno, Integer>("codigoArea"));
        colCargo.setCellValueFactory(new PropertyValueFactory <ResponsableTurno, Integer>("codigoCargo"));
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
    
    public ObservableList<ResponsableTurno> getResponsable1(){
        ArrayList lista = new ArrayList<ResponsableTurno>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListaResposableTurno}");
            ResultSet resultado =procedimiento.executeQuery();
            while(resultado.next()){
               lista.add(new ResponsableTurno(resultado.getInt("codigoResposableTurno"),
               resultado.getString("nombreResposable"),
               resultado.getString("apellidoResposable"),
               resultado.getString("telefonoResposable"),
               resultado.getInt("codigoArea"),
               resultado.getInt("codigoCargo")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return listaResponsableTurno1 = FXCollections.observableList(lista);
    }
       
    private ResponsableTurno buscarResponsable(int codigoResposableTurno){
        ResponsableTurno resultado = null;
            try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarResposableTurno (?)}");
            procedimiento.setInt(1,codigoResposableTurno);
            ResultSet registro = procedimiento.executeQuery();
            
                while(registro.next()){
                resultado = new ResponsableTurno(registro.getInt("codigoResposableTurno"),
                registro.getString("nombreResponsable"),
                registro.getString("apellidoResponsable"),
                registro.getString("telefonResponsable"),
                registro.getInt("codigoArea"),
                registro.getInt("codigoCargo"));
                }
            }catch(SQLException e){
            e.printStackTrace();
            e.getMessage();
        
        }
        return resultado;
    }
    
    
    
    public void seleccionar(){
        ResponsableTurno responsableSeleccionado = (ResponsableTurno) tblResponsable.getSelectionModel().getSelectedItem();
        cmbCodigo.getSelectionModel().select(responsableSeleccionado.toString());
        txtNombre.setText(responsableSeleccionado.getNombreResposable());
        txtApellido.setText(responsableSeleccionado.getApellidoResposable());
        txtTelefono.setText(responsableSeleccionado.getTelefonoResposable());
        
    }
       
        public Principal getEscenarioPrincipalResponsableTurno() {
            return EscenarioPrincipalResponsableTurno;
        }

        public void setEscenarioPrincipalResponsableTurno(Principal EscenarioPrincipalResponsableTurno) {
            this.EscenarioPrincipalResponsableTurno = EscenarioPrincipalResponsableTurno;
        }

        public void menuPrincipalProgramador(){
            EscenarioPrincipalResponsableTurno.ventanaTurno();
        }


        public void ventanaCargo(){
            EscenarioPrincipalResponsableTurno.ventanaCargo();
        }

        public void ventanaAreas(){
            EscenarioPrincipalResponsableTurno.ventanaAreas();
        }
    
}
