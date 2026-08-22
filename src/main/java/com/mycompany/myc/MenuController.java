/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.myc;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MenuController implements Initializable {


    @FXML
    private Button btnPedidos;
    @FXML
    private Button btnRecetas;
    @FXML
    private Button btnIngredientes;
    @FXML
    private Button btnClientes;
    @FXML
    private Button btnCerrar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    public void abrirFxml(String formulario, String titulo) {
        FXMLLoader loader=new FXMLLoader(getClass().getResource(formulario));
        try {
            Parent root=loader.load();
            Stage stage=new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            System.getLogger(MenuController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            System.out.println("holaaaa");
        }
    }

    @FXML
    private void abrirCliente(ActionEvent event) {
        abrirFxml("administrar_clientes.fxml", "Administrador de Clientes");
    }

    @FXML
    private void cerrar(ActionEvent event) {
        Stage ventana = (Stage) btnCerrar.getScene().getWindow();

        ventana.close();
    }

    @FXML
    private void abrirPedidos(ActionEvent event) {
        abrirFxml("cargar_pedidos.fxml","administrar pedidos");
    }

    @FXML
    private void abrirRecetas(ActionEvent event) {
        abrirFxml("recetas.fxml", "Administrar recetas :V");
    }

    @FXML
    private void abrirIngredientes(ActionEvent event) {
        abrirFxml("ingredientes.fxml", "Administrar Ingredientes >:D");
    }
}
