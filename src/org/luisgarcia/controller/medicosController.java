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

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.luisgarcia.bean.Medicos;
import org.luisgarcia.db.Conexion;
import org.luisgarcia.report.GenerarReporte;
import org.luisgarcia.sistema.Principal;



/**
 *
 * @author luisgarcia
 */
public class medicosController implements Initializable{
    
    private enum operaciones{Nuevo, Guardar, Editar, Actualizar, Cancelar,Eliminar, Ninguno}
    private operaciones tipoDeOperaciones = operaciones.Ninguno;
    private ObservableList <Medicos> listaMedico; 
     
    
    private Principal EscenarioPrincipalMedicos; 
    
    @FXML private ComboBox cmbcodigo;
    @FXML private TextField txtnombres;
    @FXML private TextField txtapellidos;
    @FXML private TextField txtlicencia;
    @FXML private TextField txtsexo;
          private DatePicker dtpentrada;
          private DatePicker dtpsalida;
    @FXML private TableView tablamedicos;
    @FXML private TableColumn codigo;
    @FXML private TableColumn licencia;
    @FXML private TableColumn nombres;
    @FXML private TableColumn apellidos;
    @FXML private TableColumn entrada;
    @FXML private TableColumn salida;
    @FXML private TableColumn sexo;
    @FXML private Button btnagregar;
    @FXML private Button btnmodificar;
    @FXML private Button btneliminar;
    @FXML private Button btnreporte;  
    @FXML private GridPane grpentrada;
    @FXML private GridPane grpsalida;
    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    cmbcodigo.setItems(getMedicos());
    
    cmbcodigo.setItems(getMedicos());
    dtpentrada=new DatePicker (Locale.ENGLISH);
    dtpentrada.setDateFormat(new SimpleDateFormat ("yyyy-MM-dd "));
    dtpentrada.getCalendarView().todayButtonTextProperty().set("Today");
    grpentrada.add(dtpentrada,0,0);
    
    dtpsalida = new DatePicker (Locale.ENGLISH);
    dtpsalida.setDateFormat(new SimpleDateFormat ("yyyy-MM-dd "));
    dtpsalida.getCalendarView().todayButtonTextProperty().set("Today");
    grpsalida.add(dtpsalida,0,0); 
    
}
    
    
    public void cargarDatos() {
        tablamedicos.setItems(getMedicos());
        codigo.setCellValueFactory(new PropertyValueFactory <Medicos, Integer>("codigoMedico"));
        licencia.setCellValueFactory(new PropertyValueFactory <Medicos, Integer>("licenciaMedica"));
        nombres.setCellValueFactory(new PropertyValueFactory <Medicos, Integer>("nombres"));
        apellidos.setCellValueFactory(new PropertyValueFactory <Medicos, Integer>("apellidos"));
        entrada.setCellValueFactory(new PropertyValueFactory <Medicos, Integer>("horaEntrada"));
        salida.setCellValueFactory(new PropertyValueFactory <Medicos, Integer>("horaSalidad"));
        sexo.setCellValueFactory(new PropertyValueFactory <Medicos, Integer>("sexo"));
            
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
    
    
    public void seleccionar(){
       
        cmbcodigo.getSelectionModel().select(buscarMedicos(((Medicos) tablamedicos.getSelectionModel().getSelectedItem()).getCodigoMedico()));
        txtlicencia.setText(""+((Medicos) tablamedicos.getSelectionModel().getSelectedItem()).getLicenciaMedica());
        txtnombres.setText(((Medicos)tablamedicos.getSelectionModel().getSelectedItem()).getNombres());
        txtapellidos.setText(((Medicos)tablamedicos.getSelectionModel().getSelectedItem()).getApellidos());
        dtpentrada.selectedDateProperty().set(((Medicos) tablamedicos.getSelectionModel() .getSelectedItem()).getHoraEntrada()) ;
        dtpsalida.selectedDateProperty().set(((Medicos) tablamedicos.getSelectionModel().getSelectedItem()).getHoraSalidad()) ;  
        txtsexo.setText(((Medicos)tablamedicos.getSelectionModel().getSelectedItem()).getSexo());
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
        cmbcodigo.setDisable(false);
        txtlicencia.setDisable(false);
        txtnombres.setDisable(false);
        txtapellidos.setDisable(false);
        dtpentrada.setDisable(false);
        dtpsalida.setDisable(false);
        txtsexo.setDisable(false);
        
        cmbcodigo.setEditable(true);
        txtlicencia.setEditable(true);
        txtnombres.setEditable(true);
        txtapellidos.setEditable(true);
        txtsexo.setEditable(true);
          
    }
    
    public void limpiar (){
        txtlicencia.setText("");
        txtnombres.setText("");
        txtapellidos.setText("");
        txtsexo.setText("");
        
    }
    
    public void ingresar(){
        Medicos registro = new Medicos();
        registro.setLicenciaMedica(Integer.parseInt(txtlicencia.getText()));
        registro.setNombres(txtnombres.getText());
        registro.setApellidos(txtapellidos.getText());
        registro.setHoraEntrada( dtpentrada.getSelectedDate());
        registro.setHoraSalidad( dtpsalida.getSelectedDate());
        registro.setSexo(txtsexo.getText());   
        
        try{
                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call AgregarMedico (?,?,?,?,?,?) } ");
                procedimiento.setInt(1,registro.getLicenciaMedica());
                procedimiento.setString(2, registro.getNombres());
                procedimiento.setString(3, registro.getApellidos());
                procedimiento.setDate(4, new java.sql.Date(registro.getHoraEntrada().getTime()));
                procedimiento.setDate(5, new java.sql.Date(registro.getHoraSalidad().getTime()));
                procedimiento.setString(6, registro.getSexo());
                procedimiento.execute();
                listaMedico.add(registro);   
                limpiar();    
                cargarDatos();
            }catch(SQLException e){
                    e.printStackTrace();  
            }  
    }
    
        public void desactivar(){
        cmbcodigo.setDisable(true);
        txtlicencia.setDisable(true);
        txtnombres.setDisable(true);
        txtapellidos.setDisable(true);
        dtpentrada.setDisable(true);
        dtpsalida.setDisable(true);
        txtsexo.setDisable(true);
        
        cmbcodigo.setEditable(false);
        txtlicencia.setEditable(false);
        txtnombres.setEditable(false);
        txtapellidos.setEditable(false);
        txtsexo.setEditable(false);
          
    }
        
    public void edit (){
        switch (tipoDeOperaciones){
            case Ninguno :
                if(tablamedicos.getSelectionModel().getSelectedItem() != null ){
                    btnmodificar.setText("Actualizar");
                    btnreporte.setText("Cancelar");
                    tipoDeOperaciones = operaciones.Actualizar;
                    btnagregar.setDisable(true);
                    btneliminar.setDisable(true);
                    activar(); 
                }
            break;
            
            case Actualizar:
                actualizar();
                btnmodificar.setText("Actualizar");
                btnreporte.setText("Reporte");
                tipoDeOperaciones = operaciones.Ninguno;
                btnagregar.setDisable(false);
                btneliminar.setDisable(false);
                desactivar();
                cargarDatos();
            break;    
        }         
    }

    public void actualizar (){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall(" {call ModificarMedico(?,?,?,?,?,?,?)}");
            Medicos registro = (Medicos) tablamedicos.getSelectionModel().getSelectedItem();
            
            registro.setCodigoMedico(Integer.parseInt(cmbcodigo.getSelectionModel().getSelectedItem().toString()));
            registro.setLicenciaMedica(Integer.parseInt(txtlicencia.getText()));
            registro.setNombres(txtnombres.getText());
            registro.setApellidos(txtapellidos.getText());
            registro.setHoraEntrada( dtpentrada.getSelectedDate());
            registro.setHoraSalidad( dtpsalida.getSelectedDate());
            registro.setSexo(txtsexo.getText());

            procedimiento.setInt(1, registro.getCodigoMedico());
            procedimiento.setInt(2, registro.getLicenciaMedica());
            procedimiento.setString(3, registro.getNombres());
            procedimiento.setString(4, registro.getApellidos());
            procedimiento.setDate(5, new java.sql.Date (registro.getHoraEntrada().getTime()));
            procedimiento.setDate(6, new java.sql.Date(registro.getHoraSalidad().getTime()));
            procedimiento.setString(7, registro.getSexo());
            procedimiento.execute();
            limpiar();    
            cargarDatos();
        }catch(SQLException e) {
            e.printStackTrace();  
        } 
        
    }
    
    public void Eliminar () {
        if(tipoDeOperaciones == operaciones.Guardar )
            cancelar();
            
        else 
            tipoDeOperaciones = operaciones.Eliminar;
        if(tablamedicos.getSelectionModel().getSelectedItem() !=null  ){
            int respuesta = JOptionPane.showConfirmDialog(null, "Seguro que desea eliminar el registro?","Eliminar Medico", JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE );
            if(respuesta == JOptionPane.YES_OPTION){
                try{
                    PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call EliminarMedicos(?)}");
                    procedimiento.setInt(1, ((Medicos) tablamedicos.getSelectionModel().getSelectedItem()).getCodigoMedico() );
                    procedimiento.execute();
                    listaMedico.remove(tablamedicos.getSelectionModel().getSelectedItem());
                    limpiar();    
                    cargarDatos();    
                }catch(SQLException e) {
                    e.printStackTrace();  
                } 
            }
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un registro a eliminar");
        }  
    }     
   
   
    public void cancelar(){
        btnagregar.setText("Nuevo");
        btneliminar.setText("Eliminar");
        btnmodificar.setDisable(false);
        btnreporte.setDisable(false);
        
    }
    
    public void imprimirReporte (){
        
        if(tablamedicos.getSelectionModel().getSelectedItem() != null ){
            int codigoMedico = (((Medicos) tablamedicos.getSelectionModel().getSelectedItem()).getCodigoMedico());
            Map parametros = new HashMap();
            parametros.put("p_codigoMedico",codigoMedico);
            GenerarReporte.mostrarReporte("reporte_medico.jasper","Reporte Medicos" ,parametros);  
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un registro a imprimir");
        }     
    }
    
     public void menuPrincipal (){
        EscenarioPrincipalMedicos.menuPrincipal();
    }
     
     
    public Principal getEscenarioPrincipalMedicos() {
        return EscenarioPrincipalMedicos;
    }

    public void setEscenarioPrincipalMedicos(Principal EscenarioPrincipalMedicos) {
        this.EscenarioPrincipalMedicos = EscenarioPrincipalMedicos;
    }
     
    public void ventanaTelefonoMedico (){
        EscenarioPrincipalMedicos.ventanaTelefonoMedicos();
    }
    
    public void ventanaMedicoEspacialidad(){
        EscenarioPrincipalMedicos.ventanaMedicoEspacialidad();
    }
    
    public void ventanaHorarios(){
        EscenarioPrincipalMedicos.ventanaHorarios();
    }
     
    public void ventanaControlCitas(){
        EscenarioPrincipalMedicos.ventanaControlCitas();
    }
}
