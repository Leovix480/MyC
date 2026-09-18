/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.myc;

import com.mycompany.modelos.Ingredientes;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MenuController implements Initializable {

    // Codigo Konami: arriba arriba abajo abajo izquierda derecha izquierda derecha b a enter
    private static final KeyCode[] CODIGO_KONAMI = {
        KeyCode.UP, KeyCode.UP, KeyCode.DOWN, KeyCode.DOWN,
        KeyCode.LEFT, KeyCode.RIGHT, KeyCode.LEFT, KeyCode.RIGHT,
        KeyCode.B, KeyCode.A, KeyCode.ENTER
    };
    private int progresoKonami = 0;

    @FXML
    private AnchorPane root;
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
    @FXML
    private Label lblAlertaStock;

    Ingredientes ingredienteModelo = new Ingredientes();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblAcercaDe.setCursor(Cursor.HAND);
        btnClientes.setCursor(Cursor.HAND);
        btnPedidos.setCursor(Cursor.HAND);
        btnRecetas.setCursor(Cursor.HAND);
        btnClientes.setCursor(Cursor.HAND);
        btnIngredientes.setCursor(Cursor.HAND);
        btnCerrar.setCursor(Cursor.HAND);
        actualizarAlertaStock();

        root.sceneProperty().addListener((obs, escenaVieja, escenaNueva) -> {
            if (escenaNueva != null) {
                escenaNueva.addEventFilter(KeyEvent.KEY_PRESSED, this::detectarCodigoKonami);
            }
        });
    }

    private void detectarCodigoKonami(KeyEvent event) {
        // Evita que estas teclas disparen el comportamiento normal (ej. Enter
        // activando el boton enfocado, como "Cerrar sesion") mientras se esta
        // tipeando el codigo, aunque la secuencia se corte o este mal.
        boolean esTeclaDelCodigo = false;
        for (KeyCode tecla : CODIGO_KONAMI) {
            if (event.getCode() == tecla) {
                esTeclaDelCodigo = true;
                break;
            }
        }
        if (!esTeclaDelCodigo) {
            return;
        }
        event.consume();

        if (event.getCode() == CODIGO_KONAMI[progresoKonami]) {
            progresoKonami++;
            if (progresoKonami == CODIGO_KONAMI.length) {
                progresoKonami = 0;
                abrirFxml("configuracion_servidor.fxml", "Configuracion del servidor", false);
            }
        } else {
            // Si la tecla fallida es igual al primer paso, arranca de nuevo desde ahi
            progresoKonami = (event.getCode() == CODIGO_KONAMI[0]) ? 1 : 0;
        }
    }

    // Alerta de stock mínimo: mismo criterio que la tabla de faltantes de la vista Ingredientes (stock <= stockMin)
    public void actualizarAlertaStock() {
        int cantidad = 0;
        try {
            ArrayList<Ingredientes> ingredientes = ingredienteModelo.consulta();
            for (Ingredientes ing : ingredientes) {
                if (ing.getStock() <= ing.getStockMin()) {
                    cantidad++;
                }
            }
        } catch (Exception ex) {
            // Si no se pudo consultar la BD, no se muestra la alerta
            System.getLogger(MenuController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        if (cantidad == 0) {
            lblAlertaStock.setVisible(false);
            return;
        }

        String texto = (cantidad == 1)
                ? "Alerta: 1 ingrediente está por debajo del stock mínimo"
                : "Alerta: " + cantidad + " ingredientes están por debajo del stock mínimo";
        lblAlertaStock.setText(texto);
        lblAlertaStock.setVisible(true);
    }
    
    public void abrirFxml(String formulario, String titulo) {
        abrirFxml(formulario, titulo, true);
    }

    public void abrirFxml(String formulario, String titulo, boolean redimensionable) {
        FXMLLoader loader=new FXMLLoader(getClass().getResource(formulario));
        try {
            Parent root=loader.load();
            Stage stage=new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(redimensionable);
            if (redimensionable) {
                // La ventana puede agrandarse, pero nunca achicarse por debajo del tamaño inicial de la vista
                stage.setOnShown(e -> {
                    stage.setMinWidth(stage.getWidth());
                    stage.setMinHeight(stage.getHeight());
                });
            }
            stage.showAndWait();
            // Al cerrar la ventana modal se vuelve al menú: refrescar la alerta por si cambió el stock
            actualizarAlertaStock();
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
        abrirFxml("recetas.fxml", "Administrar recetas");
    }

    @FXML
    private void abrirIngredientes(ActionEvent event) {
        abrirFxml("ingredientes.fxml", "Administrar Ingredientes");
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