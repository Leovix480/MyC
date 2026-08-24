package com.mycompany.myc;

import com.mycompany.modelos.Productos;
import com.mycompany.myc.clases.ventasSingleton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Seleccionar_productoController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private TextField txtBuscar;
    @FXML
    private TableView<Productos> tablaProductos;
    @FXML
    private TableColumn<Productos, Integer> columID;
    @FXML
    private TableColumn<Productos, String> columNombre;
    @FXML
    private TableColumn<Productos, Double> columPrecio;
    @FXML
    private Button btnAceptar;

    ObservableList<Productos> datos;
    ObservableList<Productos> datosBuscados;
    Productos producto = new Productos();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnAceptar.setCursor(Cursor.HAND);
        mostrarDatos();
    }

    public void mostrarDatos() {
        datos = FXCollections.observableArrayList(producto.consulta());
        columID.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        columNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        tablaProductos.setItems(datos);
    }

    @FXML
    private void buscar(KeyEvent event) {
        datosBuscados = FXCollections.observableArrayList();
        String buscar = txtBuscar.getText();
        if (buscar.isEmpty()) {
            tablaProductos.setItems(datos);
        } else {
            datosBuscados.clear();
            for (Productos p : datos) {
                if (p.getNombre().toLowerCase().contains(buscar.toLowerCase())) {
                    datosBuscados.add(p);
                }
            }
            tablaProductos.setItems(datosBuscados);
        }
    }

    @FXML
    private void mostrarFila(MouseEvent event) {
        Productos seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            return;
        }
        ventasSingleton.getInstance().setCodProducto(seleccionado.getIdProducto());
    }

    @FXML
    private void aceptar(ActionEvent event) {
        Stage stage = (Stage) btnAceptar.getScene().getWindow();
        stage.close();
    }
}
