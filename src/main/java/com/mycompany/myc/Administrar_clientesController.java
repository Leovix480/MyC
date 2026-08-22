/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.myc;

import com.mycompany.modelos.Clientes;
import com.mycompany.myc.clases.ventasSingleton;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class Administrar_clientesController implements Initializable {


    @FXML
    private TextField txtBuscar;
    @FXML
    private TableView<Clientes> tablaClientes;
    @FXML
    private TableColumn<Clientes, Integer> columID;
    @FXML
    private TableColumn<Clientes, String> columNombre;
    @FXML
    private TableColumn<Clientes, String> columApellido;
    @FXML
    private TableColumn<Clientes, String> columDireccion;
    @FXML
    private TableColumn<Clientes, String> columCelular;
    @FXML
    private Button btnAdd;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtCelular;
    @FXML
    private TextField txtID;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnGuardar;
    
    ObservableList<Clientes> datos;
    ObservableList<Clientes> datosBuscados;
    Clientes clie=new Clientes();
    int codCliente;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mostrarDatos();
    }    
    
    public void mostrarDatos(){
        datos=FXCollections.observableArrayList(clie.consulta());
        columID.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        columNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        columDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        columCelular.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        tablaClientes.setItems(datos);
    }
    
    public void limpiar(){
        txtID.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtDireccion.clear();
        txtCelular.clear();
    }
    
    public void habilitar(){
        txtNombre.setDisable(false);
        txtApellido.setDisable(false);
        txtDireccion.setDisable(false);
        txtCelular.setDisable(false);
        txtID.setDisable(false);
    }

    @FXML
    private void add(ActionEvent event) {
        habilitar();
        btnEliminar.setDisable(true);
        btnEditar.setDisable(true);
        btnCancelar.setDisable(false);
        btnGuardar.setDisable(false);
    }

    @FXML
    private void guardar(ActionEvent event) {
        String nom = txtNombre.getText();
        String dir = txtDireccion.getText();
        String ape = txtApellido.getText();
        String tel = txtCelular.getText();
        int id = Integer.parseInt(txtID.getText());
        clie.setNombre(nom);
        clie.setDireccion(dir);
        clie.setApellido(ape);
        clie.setTelefono(tel);
        clie.setIdCliente(id);
        
        if (clie.insertar()) {
            System.out.println("Cliente guardado correctamente.");
            mostrarDatos();
            limpiar();
            cancelar(event);
        } else {
            System.out.println("No se pudo guardar el cliente.");
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        limpiar();
        txtNombre.setDisable(true);
        txtApellido.setDisable(true);
        txtDireccion.setDisable(true);
        txtCelular.setDisable(true);
        txtID.setDisable(true);
        btnCancelar.setDisable(true);
        btnGuardar.setDisable(true);
        btnAdd.setDisable(false);
        btnEditar.setDisable(true);
        btnEliminar.setDisable(true);
    }

    @FXML
    private void mostrarFila(MouseEvent event) {
        Clientes c=tablaClientes.getSelectionModel().getSelectedItem();
        System.out.println(c.getIdCliente());
        ventasSingleton.getInstance().setCodCliente(c.getIdCliente());
        codCliente=ventasSingleton.getInstance().getCodCliente();
        
        ArrayList<Clientes> lista=c.consulta();
        for(Clientes cliente : lista){
            if(cliente.getIdCliente()==codCliente){
                System.out.println("Encontrado");
                txtNombre.setText(cliente.getNombre());
                txtApellido.setText(cliente.getApellido());
                txtDireccion.setText(cliente.getDireccion());
                txtCelular.setText(cliente.getTelefono());
                txtID.setText(String.valueOf(cliente.getIdCliente()));
                habilitar();
                txtID.setDisable(true);
                btnEditar.setDisable(false);
                btnEliminar.setDisable(false);
                btnCancelar.setDisable(false);
                btnAdd.setDisable(true);
            }    
        }
    }

    @FXML
    private void eliminar(ActionEvent event) {
        String nom = txtNombre.getText();
        String dir = txtDireccion.getText();
        String ape = txtApellido.getText();
        String tel = txtCelular.getText();
        int id = Integer.parseInt(txtID.getText());
        clie.setNombre(nom);
        clie.setDireccion(dir);
        clie.setApellido(ape);
        clie.setTelefono(tel);
        clie.setIdCliente(id);
        
        if (clie.eliminar()) {
            System.out.println("Cliente eliminado correctamente.");
            mostrarDatos();
            limpiar();
            cancelar(event);
        } else {
            System.out.println("No se pudo eliminar el cliente.");
        }
    }

    @FXML
    private void editar(ActionEvent event) {
        String nom = txtNombre.getText();
        String dir = txtDireccion.getText();
        String ape = txtApellido.getText();
        String tel = txtCelular.getText();
        int id = Integer.parseInt(txtID.getText());
        clie.setNombre(nom);
        clie.setDireccion(dir);
        clie.setApellido(ape);
        clie.setTelefono(tel);
        clie.setIdCliente(id);
        
        if (clie.editar()) {
            System.out.println("Cliente editado correctamente.");
            mostrarDatos();
            limpiar();
            cancelar(event);
        } else {
            System.out.println("No se pudo edittar el cliente.");
        }
    }

    @FXML
    private void buscar(KeyEvent event) {
        datosBuscados = FXCollections.observableArrayList();
        String buscar = txtBuscar.getText();
        if (buscar.isEmpty())
            tablaClientes.setItems(datos);
        else {
            datosBuscados.clear();
            for (Clientes dato : datos) {
                String aux = String.valueOf(dato.getIdCliente());
                if (dato.getNombre().toLowerCase().contains(buscar.toLowerCase()) || aux.toLowerCase().contains(buscar.toLowerCase())) {
                    datosBuscados.add(dato);
                }
            }
            tablaClientes.setItems(datosBuscados);
        }
    }
}