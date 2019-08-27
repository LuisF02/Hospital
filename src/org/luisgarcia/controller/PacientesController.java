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
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import static javafx.collections.FXCollections.observableList;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
//import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import org.luisgarcia.bean.Pacientes;
import org.luisgarcia.db.Conexion;
import org.luisgarcia.sistema.Principal;
import java.util.Date;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;


/**
 *
 * @author Ferdell
 */
public class PacientesController implements Initializable{
    
   private enum operaciones {Nuevo, Guardar, Editar, Actualizar, Cancelar, Eliminar, Ninguno } 
   private operaciones tipodeOperaciones = operaciones.Ninguno;
   private ObservableList <Pacientes> listaPacientes;
 
    
    private Principal EscenarioPrincipalPacientes;

    @FXML private ComboBox cmbcodigoPacientes;
    @FXML private TextField txtDPI;
    @FXML private TextField txtnombres;
    @FXML private TextField txtapellidos;
    @FXML private TextField txtedad;
    @FXML private TextField txtdireccion;
    @FXML private TextField txtocupacion;
    @FXML private TextField txtsexo;
          private DatePicker dtpfechanacimiento;
    @FXML private TableView tablapacientes;
    @FXML private TableColumn codigo;
    @FXML private TableColumn DPI;
    @FXML private TableColumn nombres;
    @FXML private TableColumn apellidos;        
    @FXML private TableColumn fechanacimiento;       
    @FXML private TableColumn edad; 
    @FXML private TableColumn direccion;
    @FXML private TableColumn ocupacion;
    @FXML private TableColumn sexo;
    @FXML private Button btnagregar;
    @FXML private Button btnmodificar;
    @FXML private Button btneliminar;
    @FXML private Button btnreporte;
    @FXML private GridPane grpfechanacimiento;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    cmbcodigoPacientes.setItems(getPacientes());
    
    dtpfechanacimiento = new DatePicker (Locale.ENGLISH);
    dtpfechanacimiento.setDateFormat(new SimpleDateFormat ("yyyy-MM-dd "));
    dtpfechanacimiento.getCalendarView().todayButtonTextProperty().set("Today");
    grpfechanacimiento.add(dtpfechanacimiento,0,0);
    
        
    }
    
    public void cargarDatos (){
        tablapacientes.setItems(getPacientes());
        codigo.setCellValueFactory(new PropertyValueFactory <Pacientes, Integer> ("codigoPacientes"));
        DPI.setCellValueFactory(new PropertyValueFactory <Pacientes, String> ("DPI"));
        apellidos.setCellValueFactory(new PropertyValueFactory <Pacientes, String> ("apellidos"));
        nombres.setCellValueFactory(new PropertyValueFactory <Pacientes, String> ("nombres"));
        fechanacimiento.setCellValueFactory(new PropertyValueFactory <Pacientes, Date> ("fechaNacimiento"));
        edad.setCellValueFactory(new PropertyValueFactory <Pacientes, Integer> ("edad"));
        direccion.setCellValueFactory(new PropertyValueFactory <Pacientes, String> ("direccion"));
        ocupacion.setCellValueFactory(new PropertyValueFactory <Pacientes, String> ("ocupacion"));
        sexo.setCellValueFactory(new PropertyValueFactory <Pacientes, String> ("sexo"));
        
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
    
    public void seleccionar() {
        cmbcodigoPacientes.getSelectionModel().select(buscarPacientes(((Pacientes) tablapacientes.getSelectionModel().getSelectedItem()).getCodigoPacientes())) ;
        txtDPI.setText(""+((Pacientes) tablapacientes.getSelectionModel().getSelectedItem()).getDPI());
        txtapellidos.setText(((Pacientes) tablapacientes.getSelectionModel().getSelectedItem()).getApellidos());
        txtnombres.setText(((Pacientes) tablapacientes.getSelectionModel().getSelectedItem() ).getNombres());
        dtpfechanacimiento.selectedDateProperty().set(((Pacientes) tablapacientes.getSelectionModel().getSelectedItem()).getFechaNacimiento());
        txtedad.setText(""+((Pacientes)tablapacientes.getSelectionModel().getSelectedItem()).getEdad());
        txtdireccion.setText(((Pacientes) tablapacientes.getSelectionModel().getSelectedItem()).getDireccion());
        txtocupacion.setText(((Pacientes) tablapacientes.getSelectionModel().getSelectedItem()).getOcupacion());
        txtsexo.setText(((Pacientes) tablapacientes.getSelectionModel().getSelectedItem()).getSexo() );  
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
        switch (tipodeOperaciones){
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
                btnagregar.setText("nuevo");
                btneliminar.setText("Elimiar");
                btnmodificar.setDisable(false);
                btnreporte.setDisable(false);
                tipodeOperaciones = operaciones.Ninguno;
            break;
        }
    }
    
    public void activar() {
        cmbcodigoPacientes.setDisable(false);
        txtDPI.setDisable(false);
        txtnombres.setDisable(false);
        txtapellidos.setDisable(false);
        txtedad.setDisable(false);
        txtdireccion.setDisable(false);
        txtocupacion.setDisable(false);
        txtsexo.setDisable(false);
        dtpfechanacimiento.setDisable(false);
        
        cmbcodigoPacientes.setEditable(true);
        txtDPI.setEditable(true);
        
        
    }
    
    public void limpiar (){
        txtDPI.setText("");
        txtnombres.setText("");
        txtapellidos.setText("");
        txtedad.setText("");
        txtdireccion.setText("");
        txtocupacion.setText("");
        txtsexo.setText("");
        
    }
    
    public void ingresar (){
        Pacientes registro = new Pacientes();
        registro.setDPI(txtDPI.getText());
        registro.setApellidos(txtapellidos.getText());
        registro.setNombres(txtnombres.getText());
        registro.setFechaNacimiento(new java.sql.Date(dtpfechanacimiento.getSelectedDate().getTime()));
        registro.setEdad(Integer.parseInt (txtedad.getText()));
        registro.setDireccion(txtdireccion.getText());
        registro.setOcupacion(txtocupacion.getText());
        registro.setSexo(txtsexo.getText());
        
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{ call sp_Pacientes (?,?,?,?,?,?,?,?) } ");
            procedimiento.setString(1, registro.getDPI());
            procedimiento.setString(2, registro.getApellidos());
            procedimiento.setString(3, registro.getNombres());
            procedimiento.setDate(4, new java.sql.Date(registro.getFechaNacimiento().getTime()));
            procedimiento.setInt(5, registro.getEdad());
            procedimiento.setString(6, registro.getDireccion());
            procedimiento.setString(7, registro.getOcupacion());
            procedimiento.setString(8, registro.getSexo());
            procedimiento.execute();
            listaPacientes.add(registro);
            limpiar();
            cargarDatos();   
        }catch(SQLException e){
                    e.printStackTrace();  
        }    
    }
    
    public void desactivar(){
        cmbcodigoPacientes.setDisable(true);
        txtDPI.setDisable(true);
        txtnombres.setDisable(true);
        txtapellidos.setDisable(true);
        txtedad.setDisable(true);
        txtdireccion.setDisable(true);
        txtocupacion.setDisable(true);
        txtsexo.setDisable(true);
        
        cmbcodigoPacientes.setEditable(false);
        txtDPI.setEditable(false);
        txtnombres.setEditable(false);
        txtapellidos.setEditable(false);
        txtedad.setEditable(false);
        txtdireccion.setEditable(false);
        txtocupacion.setEditable(false);
        txtsexo.setEditable(false); 
    }
    
    public void edit (){
        switch(tipodeOperaciones){
            case Ninguno:
                if (tablapacientes.getSelectionModel().getSelectedItem() != null ){
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall(" {call sp_ModificarPacientes(?,?,?,?,?,?,?,?,?)}");
            Pacientes registro = (Pacientes) tablapacientes.getSelectionModel().getSelectedItem();

            registro.setCodigoPacientes(Integer.parseInt(cmbcodigoPacientes.getSelectionModel().getSelectedItem().toString()));
            registro.setDPI(txtDPI.getText());
            registro.setApellidos(txtapellidos.getText());
            registro.setNombres(txtnombres.getText());
            registro.setFechaNacimiento(new java.sql.Date(dtpfechanacimiento.getSelectedDate().getTime()));
            registro.setEdad(Integer.parseInt(txtedad.getText()));
            registro.setDireccion(txtdireccion.getText());
            registro.setOcupacion(txtocupacion.getText());
            registro.setSexo(txtsexo.getText()); 
            
            procedimiento.setInt(1, registro.getCodigoPacientes());
            procedimiento.setString(2, registro.getDPI());
            procedimiento.setString(3, registro.getApellidos());
            procedimiento.setString(4, registro.getNombres());
            procedimiento.setDate(5, new java.sql.Date(registro.getFechaNacimiento().getTime()));
            procedimiento.setInt(6, registro.getEdad());
            procedimiento.setString(7, registro.getDireccion());
            procedimiento.setString(8, registro.getOcupacion());
            procedimiento.setString(9, registro.getSexo());
            procedimiento.execute();
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
        if(tablapacientes.getSelectionModel().getSelectedItem() !=null){
            int respuesta = JOptionPane.showConfirmDialog(null, "Seguro que desea eliminar el registro", "Eliminar Paciente",JOptionPane.YES_OPTION , JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_OPTION){
                try{
                    PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call EliminarPacientes(?)}");
                    procedimiento.setInt(1, ((Pacientes)tablapacientes.getSelectionModel().getSelectedItem()).getCodigoPacientes());
                    procedimiento.execute();
                    listaPacientes.remove(tablapacientes.getSelectionModel().getSelectedItem());
                    limpiar();
                    cargarDatos();
                }catch(SQLException e) {
                    e.printStackTrace();  
                } 
            }
        }else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un registro a eliminar");
        }
            
    }
    
    public void cancelar(){
        btnagregar.setText("Nuevo");
        btneliminar.setText("Eliminar");
        btnmodificar.setDisable(false);
        btnreporte.setDisable(false);
    }
    
    
    
    public Principal getEscenarioPrincipalPacientes() {
        return EscenarioPrincipalPacientes;
    }

    public void setEscenarioPrincipalPacientes(Principal EscenarioPrincipalPacientes) {
        this.EscenarioPrincipalPacientes = EscenarioPrincipalPacientes;
    }
    
   
    
    
    
    public void MenuPrincipalPacientes (){
        EscenarioPrincipalPacientes.menuPrincipal();
    }
    
    public void ventanaContactoUrgencia(){
        EscenarioPrincipalPacientes.ventanaContactoUrgencia();
    }
    
     
    
    
}
