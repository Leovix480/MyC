/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.myc;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
/**
 * FXML Controller class
 *
 * @author valin
 */
public class PedidosController implements Initializable {


    @FXML
    private AnchorPane root;
    @FXML
    private TextField txtBuscarCliente;
    @FXML
    private TableView<?> tablaClientes;
    @FXML
    private TableColumn<?, ?> columClienteID;
    @FXML
    private TableColumn<?, ?> columClienteNombre;
    @FXML
    private TextField txtNombreCliente;
    @FXML
    private Button btnAddCliente;
    @FXML
    private TextField txtNombreProducto;
    @FXML
    private Button btnAddProducto;
    @FXML
    private DatePicker dpFecha;
    @FXML
    private TextField txtCantidad;
    @FXML
    private Button btnAgregar;
    @FXML
    private TableView<?> tablaDetalle;
    @FXML
    private TableColumn<?, ?> columIDProducto;
    @FXML
    private TableColumn<?, ?> columNombreProducto;
    @FXML
    private TableColumn<?, ?> columCantidadDet;
    @FXML
    private TableColumn<?, ?> columPrecioDet;
    @FXML
    private TableColumn<?, ?> columSubtotalDet;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnImprimir;
    @FXML
    private TextField txtTotal;
    @FXML
    private ComboBox<?> cmbTipoPago;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void buscarCliente(KeyEvent event) {
    }

    @FXML
    private void mostrarCliente(MouseEvent event) {
    }

    @FXML
    private void addCliente(ActionEvent event) {
    }

    @FXML
    private void abrirProducto(ActionEvent event) {
    }

    @FXML
    private void agregar(ActionEvent event) {
    }

    @FXML
    private void guardar(ActionEvent event) {
    }

    @FXML
    private void editar(ActionEvent event) {
    }

    @FXML
    private void eliminar(ActionEvent event) {
    }

    @FXML
    private void cancelar(ActionEvent event) {
    }

    @FXML
    private void imprimir(ActionEvent event) {
    }

}
