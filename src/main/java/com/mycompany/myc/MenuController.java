/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.myc;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
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
    @FXML
    private Label lblAcercaDe;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblAcercaDe.setCursor(Cursor.HAND);
        btnClientes.setCursor(Cursor.HAND);
        btnPedidos.setCursor(Cursor.HAND);
        btnRecetas.setCursor(Cursor.HAND);
        btnClientes.setCursor(Cursor.HAND);
        btnIngredientes.setCursor(Cursor.HAND);
        btnCerrar.setCursor(Cursor.HAND);
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

    @FXML
    private void abrirManual(MouseEvent event) {
        try {
            // Cargar el PDF desde los recursos dentro del ejecutable JAR
            InputStream pdfStream = getClass().getResourceAsStream("/Manual_MyC.pdf");

            if (pdfStream == null) {
                System.out.println("No se encontró el archivo del manual.");
                return;
            }

            // Crear un archivo temporal para extraer el PDF
            File tempFile = File.createTempFile("Manual_MyC", ".pdf");
            tempFile.deleteOnExit();

            // Copiar el contenido
            Files.copy(pdfStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Abrir el archivo con la aplicación predeterminada del sistema
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                Desktop.getDesktop().open(tempFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}