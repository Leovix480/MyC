package com.mycompany.myc;

import com.mycompany.myc.clases.Conexion;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class MyC extends Application{

    private static Scene escena;
    
    public static void main(String[] args) {
        launch();
    }
    
    //hola leo
    @Override
    public void start(Stage stage) throws Exception {
        Conexion conectar= new Conexion();
        if(conectar.getCon() != null){
            escena = new Scene(loadFXML("menu"), 650, 500);
            stage.setScene(escena);
            stage.setTitle("Sistema de gestion de Stock");
            stage.show();
        }else{
            Alert alerta= new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error de conexion");
            alerta.setHeaderText(null);
            alerta.setContentText("No se pudo establecer una conexion con la base de datos");
            alerta.showAndWait();
        }
    }
    
    static void setRoot(String fxml) throws IOException {
        escena.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MyC.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
}