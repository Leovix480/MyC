/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.myc;

import com.mycompany.modelos.Clientes;
import com.mycompany.myc.clases.Textos;
import com.mycompany.myc.clases.ventasSingleton;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
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
        btnEditar.setCursor(Cursor.HAND);
        btnEliminar.setCursor(Cursor.HAND);
        btnCancelar.setCursor(Cursor.HAND);
        btnGuardar.setCursor(Cursor.HAND);
        btnAdd.setCursor(Cursor.HAND);
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
    }

    @FXML
    private void add(ActionEvent event) {
        codCliente = 0;
        habilitar();
        btnEliminar.setDisable(true);
        btnEditar.setDisable(true);
        btnCancelar.setDisable(false);
        btnGuardar.setDisable(false);
    }

    // Valida los campos del formulario antes de insertar o editar un cliente.
    // Devuelve true si todos los datos son válidos; si no, muestra una alerta y devuelve false.
    private boolean validarDatos() {
        String nom = txtNombre.getText();
        String ape = txtApellido.getText();
        String dir = txtDireccion.getText();
        String tel = txtCelular.getText();

        if (nom == null || nom.trim().isEmpty()) {
            mostrarAlerta("Alerta: El nombre del cliente no puede estar vacío.");
            return false;
        }
        if (nom.trim().length() > 45) {
            mostrarAlerta("Alerta: El nombre no puede superar los 45 caracteres.");
            return false;
        }
        if (ape == null || ape.trim().isEmpty()) {
            mostrarAlerta("Alerta: El apellido del cliente no puede estar vacío.");
            return false;
        }
        if (ape.trim().length() > 45) {
            mostrarAlerta("Alerta: El apellido no puede superar los 45 caracteres.");
            return false;
        }
        if (dir == null || dir.trim().isEmpty()) {
            mostrarAlerta("Alerta: La dirección del cliente no puede estar vacía.");
            return false;
        }
        if (dir.trim().length() > 100) {
            mostrarAlerta("Alerta: La dirección no puede superar los 100 caracteres.");
            return false;
        }
        if (tel == null || tel.trim().isEmpty()) {
            mostrarAlerta("Alerta: El teléfono del cliente no puede estar vacío.");
            return false;
        }
        if (tel.trim().length() > 20) {
            mostrarAlerta("Alerta: El teléfono no puede superar los 20 caracteres.");
            return false;
        }
        if (!tel.trim().matches("[0-9+\\-()\\s]+")) {
            mostrarAlerta("Alerta: El teléfono solo puede contener números y los símbolos + - ( ).");
            return false;
        }
        if (!tel.matches(".*[0-9].*")) {
            mostrarAlerta("Alerta: El teléfono debe contener al menos un número.");
            return false;
        }
        return true;
    }

    @FXML
    private void guardar(ActionEvent event) {
        if (!validarDatos()) {
            return;
        }

        clie.setNombre(Textos.capitalizarNombrePropio(txtNombre.getText()));
        clie.setDireccion(txtDireccion.getText().trim());
        clie.setApellido(Textos.capitalizarNombrePropio(txtApellido.getText()));
        clie.setTelefono(txtCelular.getText().trim());

        if (clie.insertar()) {
            System.out.println("Cliente guardado correctamente. ID generado: " + clie.getIdCliente());
            mostrarDatos();
            limpiar();
            cancelar(event);
        } else {
            mostrarAlerta("No se pudo guardar el cliente.");
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        limpiar();
        txtNombre.setDisable(true);
        txtApellido.setDisable(true);
        txtDireccion.setDisable(true);
        txtCelular.setDisable(true);
        btnCancelar.setDisable(true);
        btnGuardar.setDisable(true);
        btnAdd.setDisable(false);
        btnEditar.setDisable(true);
        btnEliminar.setDisable(true);
    }

    @FXML
    private void mostrarFila(MouseEvent event) {
        Clientes c=tablaClientes.getSelectionModel().getSelectedItem();
        if (c == null) {
            return;
        }
        ventasSingleton.getInstance().setCodCliente(c.getIdCliente());
        codCliente=ventasSingleton.getInstance().getCodCliente();

        ArrayList<Clientes> lista=c.consulta();
        for(Clientes cliente : lista){
            if(cliente.getIdCliente()==codCliente){
                txtNombre.setText(cliente.getNombre());
                txtApellido.setText(cliente.getApellido());
                txtDireccion.setText(cliente.getDireccion());
                txtCelular.setText(cliente.getTelefono());
                habilitar();
                btnEditar.setDisable(false);
                btnEliminar.setDisable(false);
                btnCancelar.setDisable(false);
                btnAdd.setDisable(true);
            }
        }
    }

    @FXML
    private void eliminar(ActionEvent event) {
        if (codCliente <= 0) {
            mostrarAlerta("Alerta: Seleccioná un cliente de la tabla para eliminar.");
            return;
        }

        clie.setIdCliente(codCliente);

        if (clie.eliminar()) {
            System.out.println("Cliente eliminado correctamente.");
            clie.renumerarDespuesDeEliminar(codCliente);
            mostrarDatos();
            limpiar();
            cancelar(event);
        } else {
            mostrarAlerta("No se pudo eliminar el cliente.");
        }
    }

    @FXML
    private void editar(ActionEvent event) {
        if (codCliente <= 0) {
            mostrarAlerta("Alerta: Seleccioná un cliente de la tabla para editar.");
            return;
        }
        if (!validarDatos()) {
            return;
        }

        clie.setNombre(Textos.capitalizarNombrePropio(txtNombre.getText()));
        clie.setDireccion(txtDireccion.getText().trim());
        clie.setApellido(Textos.capitalizarNombrePropio(txtApellido.getText()));
        clie.setTelefono(txtCelular.getText().trim());
        clie.setIdCliente(codCliente);

        if (clie.editar()) {
            System.out.println("Cliente editado correctamente.");
            mostrarDatos();
            limpiar();
            cancelar(event);
        } else {
            mostrarAlerta("No se pudo editar el cliente.");
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

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
