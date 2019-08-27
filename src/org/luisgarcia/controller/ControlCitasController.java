/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.luisgarcia.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.luisgarcia.bean.ControlCitas;
import org.luisgarcia.bean.Medicos;
import org.luisgarcia.bean.Pacientes;
import org.luisgarcia.db.Conexion;
import org.luisgarcia.report.GenerarReporte;
import org.luisgarcia.sistema.Principal;


/**
 *
 * @author programacion
 */
public class ControlCitasController implements Initializable{
    private enum operaciones{Nuevo, Guardar, Editar, Actualizar, Cancelar,Eliminar, Ninguno}
    private operaciones tipoDeOperaciones = operaciones.Ninguno;
    private ObservableList <ControlCitas> listaControlCitas;
    private ObservableList <Medicos> listaMedico;
    private ObservableList <Pacientes> listaPacientes;
    
    private Principal EscenarioPrincipalControlCitas;
    
    @FXML private ComboBox cmbControlCitas;
    @FXML private ComboBox cmbcodigo;
    @FXML private ComboBox cmbcodigoPacientes;
    @FXML private TextField txtHoraInicio;
    @FXML private TextField txtHoraFin;
          private DatePicker dtpFecha;
    @FXML private TableView tblControlCitas;
    @FXML private TableColumn codControlCitas;
    @FXML private TableColumn fecha;
    @FXML private TableColumn horaInicio;
    @FXML private TableColumn horaFin;
    @FXML private TableColumn codMedico;
    @FXML private TableColumn codPacientes;
    @FXML private Button btnagregar;
    @FXML private Button btnmodificar;
    @FXML private Button btneliminar;
    @FXML private Button btnreporte;
    @FXML private GridPane grpFecha;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {       
        cargarDatos();
        
        cmbControlCitas.setItems(getControlCitas());
        cmbcodigo.setItems(getMedicos());
        cmbcodigoPacientes.setItems(getPacientes());
        
        dtpFecha = new DatePicker (Locale.ENGLISH);
        dtpFecha.setDateFormat(new SimpleDateFormat ("yyyy-MM-dd "));
        dtpFecha.getCalendarView().todayButtonTextProperty().set("Today");
        grpFecha.add(dtpFecha, 0, 0);
        
        
    }  
    
    public void cargarDatos(){
        tblControlCitas.setItems(getControlCitas());
        codControlCitas.setCellValueFactory(new PropertyValueFactory <ControlCitas, Integer>("codigoControlCita"));
        fecha.setCellValueFactory(new PropertyValueFactory <ControlCitas, Date>("fecha"));
        horaInicio.setCellValueFactory(new PropertyValueFactory <ControlCitas, String>("horaInicio"));
        horaFin.setCellValueFactory(new PropertyValueFactory <ControlCitas, String>("horaFin"));
        codMedico.setCellValueFactory(new PropertyValueFactory <ControlCitas, Integer>("codigoMedico"));
        codPacientes.setCellValueFactory(new PropertyValueFactory <ControlCitas, Integer>("codigoPacientes"));
        
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
    
    public void seleccionar(){
        if(tblControlCitas.getSelectionModel().getSelectedItem() !=null  ){
                
        cmbControlCitas.getSelectionModel().select(buscarControlCitas(((ControlCitas) tblControlCitas.getSelectionModel().getSelectedItem()).getCodigoControlCita()));
        dtpFecha.selectedDateProperty().set(((ControlCitas) tblControlCitas.getSelectionModel().getSelectedItem()).getFecha());
        txtHoraInicio.setText(((ControlCitas) tblControlCitas.getSelectionModel().getSelectedItem()).getHoraInicio());
        txtHoraFin.setText(((ControlCitas) tblControlCitas.getSelectionModel().getSelectedItem()).getHoraFin()); 
        cmbcodigo.getSelectionModel().select(buscarMedicos(((ControlCitas)tblControlCitas.getSelectionModel().getSelectedItem()).getCodigoMedico()));
        cmbcodigoPacientes.getSelectionModel().select(buscarPacientes(((ControlCitas) tblControlCitas.getSelectionModel().getSelectedItem() ).getCodigoPacientes()));
     }
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
    
    
    public ObservableList <Pacientes> getPacientes (){
        ArrayList <Pacientes> lista = new ArrayList<Pacientes>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListaPacientes}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Pacientes(resultado.getInt("codigoPacientes"),
                        resultado.getString("DPI"),
                        resultado.getString("apellidos"), 
                        resultado.getString("nombres"),
                        resultado.getDate("fechaNacimiento"),
                        resultado.getInt("edad"),
                        resultado.getString("direccion"),
                        resultado.getString("ocupacion"),
                        resultado.getString("sexo")));
            }
        }catch(SQLException e) {
            e.printStackTrace(); 
        }
            return listaPacientes = FXCollections.observableList (lista)  ;
    }
       
    public Pacientes buscarPacientes (int codigoPacientes ){
        Pacientes resultado = null;
            try{
                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarPacientes(?)}");
                procedimiento.setInt(1, codigoPacientes);
                ResultSet registro = procedimiento.executeQuery();

                while (registro.next()){
                    resultado = new Pacientes(registro.getInt("codigoPacientes"),
                    registro.getString("DPI"),
                    registro.getString("nombres"),
                    registro.getString("apellidos"),
                    registro.getDate("fechaNacimiento"),
                    registro.getInt("edad"),
                    registro.getString("direccion"),
                    registro.getString("ocupacion"),
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
        cmbControlCitas.setDisable(false);
        txtHoraInicio.setDisable(false);
        txtHoraFin.setDisable(false);
        dtpFecha.setDisable(false);
        
   //     cmbControlCitas.setEditable(true);
        txtHoraInicio.setEditable(true);
        txtHoraFin.setEditable(true);
                       
    }
    
    public void limpiar (){
        txtHoraInicio.setText("");
        txtHoraFin.setText("");
        
    }
    
    public void ingresar(){
        ControlCitas registro = new ControlCitas();
        registro.setFecha(dtpFecha.getSelectedDate());
        registro.setHoraInicio(txtHoraInicio.getText());
        registro.setHoraFin(txtHoraFin.getText());
        registro.setCodigoMedico(((Medicos)cmbcodigo.getSelectionModel().getSelectedItem()).getCodigoMedico());
        registro.setCodigoPacientes(((Pacientes) cmbcodigoPacientes.getSelectionModel().getSelectedItem()).getCodigoPacientes()); 
        try{
            limpiar();
            cargarDatos();
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{ call sp_AgregarControlCitas (?,?,?,?,?) } ");
            procedimiento.setDate(1, new java.sql.Date(registro.getFecha().getTime()));
            procedimiento.setString(2, registro.getHoraInicio());
            procedimiento.setString(3, registro.getHoraFin());
            procedimiento.setInt(4, registro.getCodigoMedico());
            procedimiento.setInt(5, registro.getCodigoPacientes());
            procedimiento.execute();
            
            listaControlCitas.add(registro);
        }catch(SQLException e){
                    e.printStackTrace();  
        } 
        
    }
    
    public void desactivar(){
        cmbControlCitas.setDisable(true);
        txtHoraInicio.setDisable(true);
        txtHoraFin.setDisable(true);
        cmbcodigo.setDisable(true);
        cmbcodigoPacientes.setDisable(true);
        
       // cmbControlCitas.setEditable(false);
        txtHoraInicio.setDisable(false);
        txtHoraFin.setDisable(false);
        cmbcodigo.setDisable(false);
        cmbcodigoPacientes.setDisable(false);
    }
    
    public void edit(){
        switch (tipoDeOperaciones){
            case Ninguno:
                if (tblControlCitas.getSelectionModel().getSelectedItem()!=null){
                btnmodificar.setText("Guardar");
                btnreporte.setText("Cancelar");
                btnagregar.setDisable(true);
                btneliminar.setDisable(true);
                tipoDeOperaciones = operaciones.Actualizar;
                activar();
                }
            break;
    
            case Actualizar:
                actualizar (); 
                btnmodificar.setText("Editar");
                btnreporte.setText("Reporte");
                btnagregar.setDisable(false);
                btneliminar.setDisable(false);
                tipoDeOperaciones = operaciones.Ninguno;
                desactivar();
                cargarDatos();
                break;
        }
    }
     
    public void actualizar (){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall(" {call sp_ModificarControlCitas(?,?,?,?,?,?)}");
            ControlCitas registro = (ControlCitas) tblControlCitas.getSelectionModel().getSelectedItem();
            
            registro.setCodigoControlCita(Integer.parseInt(cmbControlCitas.getSelectionModel().getSelectedItem().toString()));
            registro.setFecha(dtpFecha.getSelectedDate());
            registro.setHoraInicio(txtHoraInicio.getText());
            registro.setHoraFin(txtHoraFin.getText());
            registro.setCodigoMedico(Integer.parseInt(cmbcodigo.getSelectionModel().getSelectedItem().toString()));
            registro.setCodigoPacientes(Integer.parseInt(cmbcodigoPacientes.getSelectionModel().getSelectedItem().toString()));
          

            procedimiento.setInt(1, registro.getCodigoControlCita());
            procedimiento.setDate(2, new java.sql.Date(registro.getFecha().getTime()));
            procedimiento.setString(3, registro.getHoraInicio());
            procedimiento.setString(4, registro.getHoraFin());
            procedimiento.setInt(5, registro.getCodigoMedico());
            procedimiento.setInt(6, registro.getCodigoPacientes());
            procedimiento.execute();
            limpiar();    
            cargarDatos();
        }catch(SQLException e) {
            e.printStackTrace();  
        } 
        
    }
    
 
    public void Eliminar (){
        if(tipoDeOperaciones == operaciones.Guardar)
        cancelar();
        else
        tipoDeOperaciones= operaciones.Eliminar;
            if(tblControlCitas.getSelectionModel().getSelectedItem() !=null){
            int repuesta =JOptionPane.showConfirmDialog(null," ¿Está Seguro que desea Eliminar este registro?","Eliminar Control Cita", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(repuesta==JOptionPane.YES_OPTION){
                try{
                    PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarControlCitas(?)}");
                    procedimiento.setInt(1,((ControlCitas) tblControlCitas.getSelectionModel().getSelectedItem()).getCodigoControlCita());
                    procedimiento.execute();
                    listaControlCitas.remove(tblControlCitas.getSelectionModel().getSelectedIndex());
                    limpiar();
                    cargarDatos();
                }catch(SQLException e){
                 e.printStackTrace();
                }
            }
                
                
         }else{
            JOptionPane.showConfirmDialog(null, "''seleccionar un registro a eliminar''");
            }
        }
            
        public void cancelar(){
        btnagregar.setText("Nuevo");
        btneliminar.setText("Eliminar");
        btnreporte.setDisable(false);
        btnmodificar.setDisable(false);
        tipoDeOperaciones = operaciones.Nuevo;
        }
        
        
        public void imprimirReporte (){
        
        if(tblControlCitas.getSelectionModel().getSelectedItem() != null ){
            int ControlCitas = (((ControlCitas) tblControlCitas.getSelectionModel().getSelectedItem()).getCodigoMedico());
            Map parametros = new HashMap();
            parametros.put("p_codigoControlCita",ControlCitas);
            GenerarReporte.mostrarReporte("Reporte_ControlCitas.jasper","titulo" ,parametros);  
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un registro a imprimir");
        }     
    }
        
        
        
    

    public Principal getEscenarioPrincipalControlCitas() {
        return EscenarioPrincipalControlCitas;
    }

    public void setEscenarioPrincipalControlCitas(Principal EscenarioPrincipalControlCitas) {
        this.EscenarioPrincipalControlCitas = EscenarioPrincipalControlCitas;
    }
    
    
    public void ventanaRecetas(){
        EscenarioPrincipalControlCitas.ventanaRecetas();
    }
    
    public void MenuMedicos(){
        EscenarioPrincipalControlCitas.ventanaMedicos();
    }
    
    
}
