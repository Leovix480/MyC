package com.mycompany.myc;

import com.mycompany.modelos.Clientes;
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

public class Seleccionar_clienteController implements Initializable {

    @FXML
    private AnchorPane root;
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
    private Button btnAceptar;

    ObservableList<Clientes> datos;
    ObservableList<Clientes> datosBuscados;
    Clientes cliente = new Clientes();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnAceptar.setCursor(Cursor.HAND);
        mostrarDatos();
    }

    public void mostrarDatos() {
        datos = FXCollections.observableArrayList(cliente.consulta());
        columID.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        columNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        columDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        columCelular.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        tablaClientes.setItems(datos);
    }

    @FXML
    private void buscar(KeyEvent event) {
        datosBuscados = FXCollections.observableArrayList();
        String buscar = txtBuscar.getText();
        if (buscar.isEmpty()) {
            tablaClientes.setItems(datos);
        } else {
            datosBuscados.clear();
            for (Clientes c : datos) {
                if (c.getNombre().toLowerCase().contains(buscar.toLowerCase())
                        || c.getApellido().toLowerCase().contains(buscar.toLowerCase())) {
                    datosBuscados.add(c);
                }
            }
            tablaClientes.setItems(datosBuscados);
        }
    }

    @FXML
    private void mostrarFila(MouseEvent event) {
        Clientes seleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            return;
        }
        ventasSingleton.getInstance().setCodCliente(seleccionado.getIdCliente());
    }

    @FXML
    private void aceptar(ActionEvent event) {
        Stage stage = (Stage) btnAceptar.getScene().getWindow();
        stage.close();
    }
}
