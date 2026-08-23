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
public class Seleccionar_clienteController implements Initializable {


    @FXML
    private AnchorPane root;
    @FXML
    private TextField txtBuscar;
    @FXML
    private TableView<?> tablaClientes;
    @FXML
    private TableColumn<?, ?> columID;
    @FXML
    private TableColumn<?, ?> columNombre;
    @FXML
    private TableColumn<?, ?> columApellido;
    @FXML
    private TableColumn<?, ?> columDireccion;
    @FXML
    private TableColumn<?, ?> columCelular;
    @FXML
    private Button btnAceptar;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void buscar(KeyEvent event) {
    }

    @FXML
    private void mostrarFila(MouseEvent event) {
    }

    @FXML
    private void aceptar(ActionEvent event) {
    }

}
