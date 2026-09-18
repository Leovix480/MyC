package com.mycompany.myc;

import com.mycompany.myc.clases.ConfigServidor;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ConfiguracionServidorController implements Initializable {

    @FXML
    private TextField txtServidor;
    @FXML
    private TextField txtHost;
    @FXML
    private TextField txtPuerto;
    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtContrasena;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtServidor.setText(ConfigServidor.getServidor());
        txtHost.setText(ConfigServidor.getHost());
        txtPuerto.setText(ConfigServidor.getPuerto());
        txtUsuario.setText(ConfigServidor.getUsuario());
        txtContrasena.setText(ConfigServidor.getContrasena());
    }

    @FXML
    private void guardar(ActionEvent event) {
        String servidor = txtServidor.getText().trim();
        String host = txtHost.getText().trim();
        String puerto = txtPuerto.getText().trim();
        String usuario = txtUsuario.getText().trim();
        String contrasena = txtContrasena.getText();

        if (servidor.isEmpty() || host.isEmpty() || puerto.isEmpty() || usuario.isEmpty()) {
            mostrarAlerta("Alerta: completá base de datos, host, puerto y usuario.");
            return;
        }
        if (!puerto.matches("[0-9]+")) {
            mostrarAlerta("Alerta: el puerto solo puede contener números.");
            return;
        }

        ConfigServidor.guardar(servidor, host, puerto, usuario, contrasena);
        cerrar();
    }

    @FXML
    private void cancelar(ActionEvent event) {
        cerrar();
    }

    private void cerrar() {
        Stage ventana = (Stage) btnGuardar.getScene().getWindow();
        ventana.close();
    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
