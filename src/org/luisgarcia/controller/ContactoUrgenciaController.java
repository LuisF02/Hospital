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
import org.luisgarcia.bean.ContactoUrgencia;
import org.luisgarcia.db.Conexion;
import org.luisgarcia.sistema.Principal;
import java.util.Date;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import org.luisgarcia.bean.Pacientes;


/**
 *
 * @author luisgarcia
 */
public class ContactoUrgenciaController implements Initializable{
    
    private enum operaciones {Nuevo, Guardar, Editar, Actualizar, Cancelar, Eliminar, Ninguno } 
    private operaciones tipodeOperaciones = operaciones.Ninguno; 
    private ObservableList <ContactoUrgencia> listaContacotUrgencia;
    private ObservableList <Pacientes> listaPacientes;
    
    private Principal EscenarioPrincipalContactoUrgencia;
    
    @FXML private ComboBox cmbcodcontactourgencia;
    @FXML private ComboBox txtcoddelpaciente;
    @FXML private TextField txtnombres;
    @FXML private TextField txtapellidos;
    @FXML private TextField txtnumdecontacto;
    @FXML private TableView tablaContacotUrgencia;
    @FXML private TableColumn codcontactourgencia;
    @FXML private TableColumn nombre;
    @FXML private TableColumn apellidos;
    @FXML private TableColumn numerocontacto;
    @FXML private TableColumn codigopacientes;
    @FXML private Button btnagregar;
    @FXML private Button btnmodificar;
    @FXML private Button btneliminar;
    @FXML private Button btnreporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    cmbcodcontactourgencia.setItems(getContactoUrgencia());
    txtcoddelpaciente.setItems(getPacientes());
       
    }
     
    public void cargarDatos(){
        tablaContacotUrgencia.setItems(getContactoUrgencia());
        codcontactourgencia.setCellValueFactory(new PropertyValueFactory <ContactoUrgencia, Integer >("codigoContactoUrgencia") );
        nombre.setCellValueFactory(new PropertyValueFactory <ContactoUrgencia, String>("nombres"));
        apellidos.setCellValueFactory(new PropertyValueFactory <ContactoUrgencia, String>("apellidos"));
        numerocontacto.setCellValueFactory(new PropertyValueFactory <ContactoUrgencia, String>("numeroContacto"));
        codigopacientes.setCellValueFactory(new PropertyValueFactory <ContactoUrgencia, Integer>("codigoPacientes"));
     
    }
    
    public ObservableList <ContactoUrgencia> getContactoUrgencia() {
        ArrayList <ContactoUrgencia> lista = new ArrayList<ContactoUrgencia>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListacontactoUrgencia }");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new ContactoUrgencia(resultado.getInt("codigoContactoUrgencia"), 
                        resultado.getString("nombres"), 
                        resultado.getString("apellidos"), 
                        resultado.getString("numeroContacto"), 
                        resultado.getInt("codigoPacientes")));
            }
        }catch(SQLException e) {
            e.printStackTrace();  
        } 
            return listaContacotUrgencia = FXCollections.observableList (lista)  ;
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
    
    public void seleccionar(){
        cmbcodcontactourgencia.getSelectionModel().select(buscarContactoUrgencia(((ContactoUrgencia) tablaContacotUrgencia.getSelectionModel().getSelectedItem()).getCodigoContactoUrgencia()));
        
        txtnombres.setText(((ContactoUrgencia)tablaContacotUrgencia.getSelectionModel().getSelectedItem() ).getNombres());
        txtapellidos.setText(((ContactoUrgencia) tablaContacotUrgencia.getSelectionModel().getSelectedItem()).getApellidos());
        txtnumdecontacto.setText(((ContactoUrgencia)tablaContacotUrgencia.getSelectionModel().getSelectedItem()).getNumeroContacto());
        txtcoddelpaciente.getSelectionModel().select(buscarPacientes(((ContactoUrgencia)tablaContacotUrgencia.getSelectionModel().getSelectedItem()).getCodigoPacientes()));
                       
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
    
    public ContactoUrgencia buscarContactoUrgencia(int codigoContactoUrgencia ){
        ContactoUrgencia resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarContactoUrgencia(?)}");
            procedimiento.setInt(1, codigoContactoUrgencia);
            ResultSet registro = procedimiento.executeQuery();
            
            while (registro.next()){
                resultado = new ContactoUrgencia(registro.getInt("codigoContactoUrgencia"),
                registro.getString("nombres"),
                registro.getString("apellidos"),
                registro.getString("numeroContacto"),
                registro.getInt("codigoPacientes"));
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
                btnagregar.setText("Guardar");
                btneliminar.setText("Cancelar");
                btnmodificar.setDisable(true);
                btnreporte.setDisable(true);
                tipodeOperaciones = operaciones.Guardar;
            break;
            
            case Guardar:
                ingresar();
                desactivar();
                btnagregar.setText("Nuevo");
                btneliminar.setText("Eliminar");
                btnmodificar.setDisable(false);
                btnreporte.setDisable(false);
                tipodeOperaciones = operaciones.Ninguno;
            break;
        }
    }
    
    public void activar(){
        cmbcodcontactourgencia.setDisable(false);
        txtnombres.setDisable(false);
        txtapellidos.setDisable(false);
        txtnumdecontacto.setDisable(false);
        txtcoddelpaciente.setDisable(false);
        
        cmbcodcontactourgencia.setEditable(true);
        txtnombres.setEditable(true);
        txtapellidos.setEditable(true);
        txtnumdecontacto.setEditable(true);
        txtcoddelpaciente.setEditable(true);
        
    }
    
    public void limpiar (){
       txtnombres.setText("");
       txtapellidos.setText("");
       txtnumdecontacto.setText("");
       
    }
    
    public void ingresar(){
        ContactoUrgencia registro = new ContactoUrgencia();
        registro.setNombres(txtnombres.getText());
        registro.setApellidos(txtapellidos.getText());
        registro.setNumeroContacto(txtnumdecontacto.getText());
        registro.setCodigoPacientes(Integer.parseInt(txtcoddelpaciente.getSelectionModel().getSelectedItem().toString()));
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_contactoUrgencia(?,?,?,?) } ");
            procedimiento.setString(1, registro.getNombres());
            procedimiento.setString(2, registro.getApellidos());
            procedimiento.setString(3, registro.getNumeroContacto());
            procedimiento.setInt(4, registro.getCodigoPacientes());
            procedimiento.execute();
            listaContacotUrgencia.add(registro);
            limpiar();
            cargarDatos();
            
        }catch(SQLException e){
            e.printStackTrace();  
        }    
    }
    
    public void desactivar(){
        cmbcodcontactourgencia.setDisable(true);
        txtnombres.setDisable(true);
        txtapellidos.setDisable(true);
        txtnumdecontacto.setDisable(true);
        txtcoddelpaciente.setDisable(true);
        
        cmbcodcontactourgencia.setEditable(false);
        txtnombres.setEditable(false);
        txtapellidos.setEditable(false);
        txtnumdecontacto.setEditable(false);
        txtcoddelpaciente.setEditable(false);
        
        
    }
    
    public void edit(){
        switch(tipodeOperaciones){
            case Ninguno:
                if(tablaContacotUrgencia.getSelectionModel().getSelectedItem() !=null){
                    activar();
                    btnmodificar.setText("Actualizar");
                    btnreporte.setText("Cancelar");
                    tipodeOperaciones = operaciones.Actualizar;
                    btnagregar.setDisable(true);
                    btneliminar.setDisable(true);
                }
            break;
            
            case Actualizar:
                actualizar();
                btnmodificar.setText("Actualizar");
                btnreporte.setText("Reporte");
                tipodeOperaciones = operaciones.Ninguno;
                btnagregar.setDisable(false);
                btneliminar.setDisable(false);
                desactivar();
                cargarDatos();
            break;       
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall(" {call sp_ModificarContactoUrgencia(?,?,?,?)}");
            ContactoUrgencia registro = (ContactoUrgencia) tablaContacotUrgencia.getSelectionModel().getSelectedItem();
            
            registro.setCodigoContactoUrgencia(Integer.parseInt(cmbcodcontactourgencia.getSelectionModel().getSelectedItem().toString()));
            registro.setNombres(txtnombres.getText());
            registro.setApellidos(txtapellidos.getText());
            registro.setNumeroContacto(txtnumdecontacto.getText());
            
            procedimiento.setInt(1, registro.getCodigoContactoUrgencia());
            procedimiento.setString(2, registro.getNombres());
            procedimiento.setString(3, registro.getApellidos());
            procedimiento.setString(4, registro.getNumeroContacto());
            limpiar();
            cargarDatos();
        }catch(SQLException e) {
            e.printStackTrace();  
        }
    }
    
     
    
    
    
    public void Eliminar(){
        if(tipodeOperaciones == operaciones.Guardar)
            cancelar();
        else
            tipodeOperaciones = operaciones.Eliminar;
        if(tablaContacotUrgencia.getSelectionModel().getSelectedItem() !=null){
            int respuesta = JOptionPane.showConfirmDialog(null, "Seguro que dese eliminar el registro", "Eliminar paciente",JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE );
            if(respuesta == JOptionPane.YES_OPTION){
                try{
                    PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call EliminarContactoUrgencia(?)}");
                    procedimiento.setInt(1, ((ContactoUrgencia) tablaContacotUrgencia.getSelectionModel().getSelectedItem()).getCodigoContactoUrgencia());
                    procedimiento.execute();
                    listaContacotUrgencia.remove(tablaContacotUrgencia.getSelectionModel().getSelectedItem());
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


      
    

    public Principal getEscenarioPrincipalContactoUrgencia() {
        return EscenarioPrincipalContactoUrgencia;
    }

    public void setEscenarioPrincipalContactoUrgencia(Principal EscenarioPrincipalContactoUrgencia) {
        this.EscenarioPrincipalContactoUrgencia = EscenarioPrincipalContactoUrgencia;
    }
    
    
    
    public void menuPrincipalProgramador (){
        EscenarioPrincipalContactoUrgencia.ventanaPacientes();
    }
}
