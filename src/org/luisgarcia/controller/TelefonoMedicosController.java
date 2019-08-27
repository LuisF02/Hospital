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
import org.luisgarcia.bean.Medicos;
import org.luisgarcia.bean.TelefonoMedicos;
import org.luisgarcia.db.Conexion;
import org.luisgarcia.sistema.Principal;


/**
 *
 * @author programacion
 */
public class TelefonoMedicosController implements Initializable{
    
    private enum operaciones {Nuevo, Guardar, Editar, Actualizar, Cancelar, Eliminar, Ninguno}
    private operaciones tipodeOperaciones = operaciones.Ninguno;
    private ObservableList <TelefonoMedicos> listaTelefonoMedicos;
    private ObservableList <Medicos> listaMedico;
    
    private Principal EscenarioPrincipalTelefonoMedicos;

    
    @FXML private ComboBox cmbCodTelfMedico;
    @FXML private ComboBox cmbcodigo;
    @FXML private TextField txtTelefonoPersonal;
    @FXML private TextField txtTelefonoTrabajo;
    
    @FXML private TableView tablaTelefonoMedico;
    @FXML private TableColumn codTelefonoMedico;
    @FXML private TableColumn telefonoPersonal;
    @FXML private TableColumn telefonoTrabajo;
    @FXML private TableColumn CodigoMedico;  
    @FXML private Button btnNuevo;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;        
    @FXML private Button btnReporte;
    
      
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    cmbCodTelfMedico.setItems(getTelefonoMedicos());
    cmbcodigo.setItems(getMedicos());
  
    }
    
    public void cargarDatos(){
        tablaTelefonoMedico.setItems(getTelefonoMedicos());
        codTelefonoMedico.setCellValueFactory(new PropertyValueFactory <TelefonoMedicos, Integer>("codigoTelefonoMedico"));
        telefonoPersonal.setCellValueFactory(new PropertyValueFactory <TelefonoMedicos, String>("telefonoPersonal"));
        telefonoTrabajo.setCellValueFactory(new PropertyValueFactory <TelefonoMedicos, String>("telefonoTrabajo"));
        CodigoMedico.setCellValueFactory(new PropertyValueFactory <TelefonoMedicos , Integer>("codigoMedico"));
        
    }
            
    public ObservableList <TelefonoMedicos> getTelefonoMedicos(){
        ArrayList <TelefonoMedicos> lista = new ArrayList<TelefonoMedicos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Listartelefonosmedicos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new TelefonoMedicos(resultado.getInt("codigoTelefonoMedico"),
                        resultado.getString("telefonoPersonal"),
                        resultado.getString("telefonoTrabajo"),
                        resultado.getInt("codigoMedico")));
            }           
        }catch(SQLException e) {
            e.printStackTrace(); 
        }
            return listaTelefonoMedicos = FXCollections.observableList (lista)  ;
    }
    
    
    public ObservableList <Medicos> getMedicos(){
        ArrayList <Medicos> lista = new ArrayList<Medicos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListaMedicos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Medicos(resultado.getInt("codigoMedico"),resultado.getInt("licenciaMedica"), resultado.getString("nombres"),
                            resultado.getString("apellidos"), resultado.getDate("horaEntrada"), resultado.getDate("horaSalidad"), resultado.getString("sexo") ));
            } 
                
        }catch(SQLException e) {
            e.printStackTrace();  
        } 
            return listaMedico = FXCollections.observableList (lista)  ;
    }
    
    
    public void seleccionar (){
        cmbCodTelfMedico.getSelectionModel().select(buscarTelefonoMedicos(((TelefonoMedicos)tablaTelefonoMedico.getSelectionModel().getSelectedItem()).getCodigoTelefonoMedico()));
        cmbcodigo.getSelectionModel().select(buscarMedicos(((TelefonoMedicos)tablaTelefonoMedico.getSelectionModel().getSelectedItem()).getCodigoMedico()));
        txtTelefonoPersonal.setText(((TelefonoMedicos)tablaTelefonoMedico.getSelectionModel().getSelectedItem()).getTelefonoPersonal());
        txtTelefonoTrabajo.setText(((TelefonoMedicos)tablaTelefonoMedico.getSelectionModel().getSelectedItem()).getTelefonoTrabajo());
    
    }    
    
    public TelefonoMedicos buscarTelefonoMedicos (int codigoTelefonoMedico){
        TelefonoMedicos resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscartelefonosMedicos(?)}");
            procedimiento.setInt(1, codigoTelefonoMedico);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new TelefonoMedicos(registro.getInt("codigoTelefonoMedico"),
                registro.getString("telefonoPersonal"),
                registro.getString("telefonoTrabajo"),
                registro.getInt("codigoMedico"));
                
            }
            
        }catch(SQLException e) {
            e.printStackTrace();  
        } 
         return resultado;
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
      
    public void nuevo(){
        switch(tipodeOperaciones){
            case Ninguno:
                activar();
                limpiar();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnModificar.setDisable(true);
                btnReporte.setDisable(true);
                tipodeOperaciones = operaciones.Guardar;
            break;
            
            case Guardar:
                ingresar();
                desactivar();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnModificar.setDisable(false);
                btnReporte.setDisable(false);
                tipodeOperaciones = operaciones.Ninguno;
                cargarDatos();
           
                
        }
    }
    
     public void Eliminar () {
         switch(tipodeOperaciones){
         case Guardar:
         // if(tipodeOperaciones == operaciones.Guardar )
           btnNuevo.setText("Agregar");
           btnEliminar.setText("Cancelar");
           tipodeOperaciones = operaciones.Ninguno;
           activar();
           desactivar();
           // cancelar();
           break;
            default:
        if(tablaTelefonoMedico.getSelectionModel().getSelectedItem() != null){
           int respuesta = JOptionPane.showConfirmDialog(null, "Seguro que desea eliminar el registro?","Eliminar Medico", JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE );
            if(respuesta == JOptionPane.YES_OPTION){
                try{
                    PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call eliminar_telefonosmedicos(?)}");
                    procedimiento.setInt(1, ((TelefonoMedicos) tablaTelefonoMedico.getSelectionModel().getSelectedItem()).getCodigoTelefonoMedico());
                    procedimiento.execute();
                    listaTelefonoMedicos.remove(tablaTelefonoMedico.getSelectionModel().getSelectedItem());
                    limpiar();    
                    cargarDatos();    
                }catch(SQLException e) {
                    e.printStackTrace();  
                } 
            }
        }else{
            JOptionPane.showMessageDialog(null, "¡Debe seleccionar un registro a eliminar!");
        }  
    }
   }
        
        
    public void activar(){
        
        txtTelefonoPersonal.setDisable(false);
        txtTelefonoTrabajo.setDisable(false);
        cmbcodigo.setDisable(false);
        
        txtTelefonoPersonal.setEditable(true);
        txtTelefonoTrabajo.setEditable(true);
        cmbcodigo.setEditable(true);
                
    }
    
    public void limpiar(){
       txtTelefonoPersonal.setText("");
       txtTelefonoTrabajo.setText("");
       
       
    }
    
    public void ingresar(){
        TelefonoMedicos registro = new TelefonoMedicos();
        registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
        registro.setTelefonoTrabajo(txtTelefonoTrabajo.getText());
        registro.setCodigoMedico(Integer.parseInt(cmbcodigo.getSelectionModel().getSelectedItem().toString()));
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call Agregar_telefonosmedicos(?,?,?)} ");
            procedimiento.setString(1, registro.getTelefonoPersonal());
            procedimiento.setString(2, registro.getTelefonoTrabajo());
            procedimiento.setInt(3, registro.getCodigoMedico());
            procedimiento.execute();
            listaTelefonoMedicos.add(registro);
            limpiar();
            cargarDatos();
            
        }catch(SQLException e){
            e.printStackTrace();  
        }
    }
        
    public void desactivar(){
      
        cmbCodTelfMedico.setDisable(true);
        txtTelefonoPersonal.setDisable(true);
        txtTelefonoTrabajo.setDisable(true);
        cmbcodigo.setDisable(true);
        
        cmbCodTelfMedico.setEditable(false);
        txtTelefonoPersonal.setEditable(false);
        txtTelefonoTrabajo.setEditable(false);
        cmbcodigo.setEditable(false);
        
    }
    
    public void edit() throws SQLException{
        switch(tipodeOperaciones){ 
            case Ninguno:
                if(tablaTelefonoMedico.getSelectionModel().getSelectedItem() !=null){
                    activar();   
                    btnModificar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipodeOperaciones = operaciones.Actualizar;   
                    btnNuevo.setDisable(true); 
                    btnEliminar.setDisable(true);            
                }
            break;
            
            case Actualizar:
                actualizar();
                btnModificar.setText("Actualizar");
                btnReporte.setText("Reporte");
                tipodeOperaciones = operaciones.Ninguno;
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                desactivar();
                cargarDatos();
            break;
        }
    }
    
    public void actualizar() throws SQLException{
        
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall(" {call modificar_telefonosmedicos(?,?,?)}");
            TelefonoMedicos registro = (TelefonoMedicos) tablaTelefonoMedico.getSelectionModel().getSelectedItem();

            registro.setCodigoTelefonoMedico(Integer.parseInt(cmbCodTelfMedico.getSelectionModel().getSelectedItem().toString()));
            registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
            registro.setTelefonoTrabajo(txtTelefonoTrabajo.getText());
            registro.setCodigoMedico(registro.getCodigoMedico());

            procedimiento.setInt(1, registro.getCodigoTelefonoMedico());
            procedimiento.setString(2, registro.getTelefonoPersonal());
            procedimiento.setString(3, registro.getTelefonoTrabajo());
            procedimiento.setInt(4, registro.getCodigoMedico());
            procedimiento.execute();
            
            
            listaTelefonoMedicos.add(registro);
        
    }
    
    
            
            
    public Principal getEscenarioPrincipalTelefonoMedicos() {
        return EscenarioPrincipalTelefonoMedicos;
    }

    public void setEscenarioPrincipalTelefonoMedicos(Principal EscenarioPrincipalTelefonoMedicos) {
        this.EscenarioPrincipalTelefonoMedicos = EscenarioPrincipalTelefonoMedicos;
    }
     
    
    
    public void menuPrincipalProgramador (){
        EscenarioPrincipalTelefonoMedicos.ventanaMedicos();
    }
    
    
    
}
