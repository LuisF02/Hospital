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
import org.luisgarcia.bean.Horarios;
import org.luisgarcia.db.Conexion;
import org.luisgarcia.sistema.Principal;

/**
 *
 * @author luisgarcia
 */
public class HorariosController implements Initializable{
    
    private enum operaciones{Nuevo,Guardar,Editar,Actualizar,Eliminar,Cancelar,Ninguno} 
    private operaciones tipoDeOperaciones = operaciones.Ninguno;
    private ObservableList <Horarios> listaHorarios;
   
    private Principal EscerarioPrincipalHorarios;
    
    @FXML private ComboBox cmbCodigoHorario;
    @FXML private TextField txtHoraInicio;
    @FXML private TextField txtHoraSalida;
    @FXML private TextField txtLunes;
    @FXML private TextField txtMartes;
    @FXML private TextField txtMiercoles;
    @FXML private TextField txtJueves;
    @FXML private TextField txtViernes;
    @FXML private TableView tblHorarios;
    @FXML private TableColumn colCodigoHorario;
    @FXML private TableColumn colInicio;
    @FXML private TableColumn colSalida;
    @FXML private TableColumn colLunes;
    @FXML private TableColumn colMartes;
    @FXML private TableColumn colMiercoles;
    @FXML private TableColumn colJueves;
    @FXML private TableColumn colViernes;
    @FXML private Button btnAgregar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;  
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {       
    cargarDatos();
        cmbCodigoHorario.setItems(getHorario());
        
    }
    
    public void cargarDatos(){
        tblHorarios.setItems(getHorario());
        colCodigoHorario.setCellValueFactory(new PropertyValueFactory <Horarios, Integer>("codigoHorario"));
        colInicio.setCellValueFactory(new PropertyValueFactory <Horarios,String>("horarioInicio"));
        colSalida.setCellValueFactory(new PropertyValueFactory <Horarios, String>("horarioSalida"));
        colLunes.setCellValueFactory(new PropertyValueFactory <Horarios, Integer>("lunes"));
        colMartes.setCellValueFactory(new PropertyValueFactory <Horarios,Integer>("martes"));
        colMiercoles.setCellValueFactory(new PropertyValueFactory <Horarios,Integer>("miercoles"));
        colJueves.setCellValueFactory(new PropertyValueFactory <Horarios, Integer>("jueves"));
        colViernes.setCellValueFactory(new PropertyValueFactory <Horarios, Integer>("viernes"));

    } 
    
    public ObservableList <Horarios> getHorario() {
        ArrayList<Horarios> lista = new ArrayList<Horarios>();
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Elistarhorarios}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Horarios(resultado.getInt("codigoHorario"),
                resultado.getString("horarioInicio"),
                resultado.getString("horarioSalida"),
                resultado.getInt("lunes"),
                resultado.getInt("martes"),
                resultado.getInt("miercoles"),
                resultado.getInt("jueves"),
                resultado.getInt("viernes")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
      return listaHorarios = FXCollections.observableList(lista);
    }
    
    public void seleccionar(){
        Horarios horarioSeleccionado = (Horarios) tblHorarios.getSelectionModel().getSelectedItem();
        cmbCodigoHorario.getSelectionModel().select(horarioSeleccionado.toString());
        txtHoraInicio.setText(horarioSeleccionado.getHorarioInicio());
        txtHoraSalida.setText(horarioSeleccionado.getHorarioSalida());
        txtLunes.setText(String.valueOf(horarioSeleccionado.getLunes()));
        txtMartes.setText(String.valueOf(horarioSeleccionado.getMartes()));
        txtMiercoles.setText(String.valueOf(horarioSeleccionado.getMiercoles()));
        txtJueves.setText(String.valueOf(horarioSeleccionado.getJueves()));
        txtViernes.setText(String.valueOf(horarioSeleccionado.getViernes()));
    }
    
    public Horarios buscarHorario(int codigoHorario){
        Horarios resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarHorarios (?)}");
            procedimiento.setInt(1, codigoHorario);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
            resultado = new Horarios(registro.getInt("codigoHorario"),
            registro.getString("horarioInicio"),
            registro.getString("horarioSalida"),
            registro.getInt("lunes"),
            registro.getInt("martes"),
            registro.getInt("miercoles"),
            registro.getInt("jueves"),
            registro.getInt("viernes")); 
            }
        }catch(SQLException e){
            e.printStackTrace();
            e.getMessage();
        
        }
        return resultado;
    } 
    
    public void nuevo(){
        switch(tipoDeOperaciones){
            case  Ninguno:
                cmbCodigoHorario.setDisable(true);
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
        txtHoraInicio.setDisable(false);
        txtHoraSalida.setDisable(false);
        txtLunes.setDisable(false);
        txtMartes.setDisable(false);
        txtMiercoles.setDisable(false);
        txtJueves.setDisable(false);
        txtViernes.setDisable(false);
        
        cmbCodigoHorario.setEditable(true);
        txtHoraInicio.setEditable(true);
        txtHoraSalida.setEditable(true);
        txtLunes.setEditable(true);
        txtMartes.setEditable(true);
        txtMiercoles.setEditable(true);
        txtJueves.setEditable(true);
        txtViernes.setEditable(true);
    }
    
    private void limpiar() {
       txtHoraInicio.setText("");
       txtHoraSalida.setText("");
       txtLunes.setText("");
       txtMartes.setText("");
       txtMiercoles.setText("");
       txtJueves.setText("");
       txtViernes.setText("");
    }
    
    private void ingresar() {
       Horarios registro = new Horarios();
       registro.setHorarioInicio(txtHoraInicio.getText());
       registro.setHorarioSalida(txtHoraSalida.getText());
       registro.setLunes(Integer.valueOf(txtLunes.getText()));
       registro.setMartes(Integer.valueOf(txtMartes.getText()));
       registro.setMiercoles(Integer.valueOf(txtMiercoles.getText()));
       registro.setJueves(Integer.valueOf(txtJueves.getText()));
       registro.setViernes(Integer.valueOf(txtViernes.getText()));
       
       try{
          PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call Agregarhorarios (?,?,?,?,?,?,?)}"); 
          procedimiento.setString(1,registro.getHorarioInicio());
          procedimiento.setString(2,registro.getHorarioSalida());
          procedimiento.setInt(3,registro.getLunes());
          procedimiento.setInt(4,registro.getMartes());
          procedimiento.setInt(5,registro.getMiercoles());
          procedimiento.setInt(6, registro.getJueves());
          procedimiento.setInt(7, registro.getViernes());
          procedimiento.execute();
          listaHorarios.add(registro);
        }catch(SQLException e){
           e.printStackTrace();
       }
    } 
    
    private void desactivar() {
        cmbCodigoHorario.setDisable(true);
        txtHoraInicio.setDisable(true);
        txtHoraSalida.setDisable(true);
        txtLunes.setDisable(true);
        txtMartes.setDisable(true);
        txtMiercoles.setDisable(true);
        txtJueves.setDisable(true);
        txtViernes.setDisable(true);
        btnAgregar.setDisable(false);
        btnEliminar.setDisable(false);
        btnModificar.setDisable(false);
        btnReporte.setDisable(false);        
        
        cmbCodigoHorario.setEditable(false);
        txtHoraInicio.setEditable(false);
        txtHoraSalida.setEditable(false);
        txtLunes.setEditable(false);
        txtMartes.setEditable(false);
        txtMiercoles.setEditable(false);
        txtJueves.setEditable(false);
        txtViernes.setEditable(false);
    }   
    
    public void editar() throws SQLException{
        switch(tipoDeOperaciones){
            case Ninguno:
                if (tblHorarios.getSelectionModel().getSelectedItem() != null){
                    cmbCodigoHorario.setDisable(false);
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
        }
    }
    
    private void actualizar() throws SQLException {
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ModficarHorarios (?,?,?,?,?,?,?,?)}");
        Horarios registro= (Horarios) tblHorarios.getSelectionModel().getSelectedItem();
            
        registro.setCodigoHorario(registro.getCodigoHorario());
        registro.setHorarioInicio(txtHoraInicio.getText());
        registro.setHorarioSalida(txtHoraSalida.getText());
        registro.setLunes(Integer.valueOf(txtLunes.getText()));
        registro.setMartes(Integer.valueOf(txtMartes.getText()));
        registro.setMiercoles(Integer.valueOf(txtMiercoles.getText()));
        registro.setJueves(Integer.valueOf(txtJueves.getText()));
        registro.setViernes(Integer.valueOf(txtViernes.getText()));
        
        procedimiento.setInt(1,registro.getCodigoHorario());
        procedimiento.setString(2,registro.getHorarioInicio());
        procedimiento.setString(3,registro.getHorarioSalida());
        procedimiento.setInt(4,registro.getLunes());
        procedimiento.setInt(5,registro.getMartes());
        procedimiento.setInt(6,registro.getMiercoles());
        procedimiento.setInt(7, registro.getJueves());
        procedimiento.setInt(8, registro.getViernes());
        procedimiento.execute();
        listaHorarios.add(registro);
    }
   
    
    public void eliminar(){
        if(tipoDeOperaciones == operaciones.Guardar){
            cancelar();
            desactivar();
            tipoDeOperaciones = operaciones.Ninguno;
           
        }else{
            tipoDeOperaciones = operaciones.Eliminar;
            if(tblHorarios.getSelectionModel().getSelectedItem() != null){
                int respuesta = JOptionPane.showConfirmDialog(null,"Seguro que desea eliminar el registro?","Eliminar Medico",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if(respuesta == JOptionPane.YES_OPTION){
                 try{
                     PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call Eliminarhorarios (?)}");
                     procedimiento.setInt(1,((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).getCodigoHorario());
                     procedimiento.execute();
                     listaHorarios.remove(tblHorarios.getSelectionModel().getSelectedIndex());
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
    
    public void cancelar(){
        btnAgregar.setText("Nuevo");
        btnEliminar.setText("Eliminar");
        btnReporte.setDisable(false);
        btnModificar.setDisable(false);
    }
    
            
            
    public Principal getEscerarioPrincipalHorarios() {
        return EscerarioPrincipalHorarios;
    }

    public void setEscerarioPrincipalHorarios(Principal EscerarioPrincipalHorarios) {
        this.EscerarioPrincipalHorarios = EscerarioPrincipalHorarios;
    }
   
    
    public void menuPrincipalMedicos(){
        EscerarioPrincipalHorarios.ventanaMedicos();
    }
    
    
    
    
}
