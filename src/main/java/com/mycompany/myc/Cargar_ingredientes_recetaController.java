package com.mycompany.myc;

import com.mycompany.modelos.DetalleReceta;
import com.mycompany.modelos.Ingredientes;
import com.mycompany.myc.clases.ventasSingleton;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.stage.Stage;

public class Cargar_ingredientes_recetaController implements Initializable {

    @FXML
    private TextField txtBuscar;
    @FXML
    private TableView<Ingredientes> tablaIRecetas;
    @FXML
    private TableColumn<Ingredientes, String> columNombre;
    @FXML
    private TableColumn<Ingredientes, String> columCantidad;
    @FXML
    private TextField txtCantidad;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnAceptar;
    @FXML
    private Button btnEditar;

    ObservableList<Ingredientes> datos;
    ObservableList<Ingredientes> datosBuscados;
    Ingredientes ingrediente = new Ingredientes();
    DetalleReceta detalle = new DetalleReceta();
    Map<Integer, String> cantidadesPorIngrediente = new HashMap<>(); // idIngrediente -> cantUso
    int idReceta;
    int idIngredienteSeleccionado;
    boolean yaAsignado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnAceptar.setCursor(Cursor.HAND);
        btnAdd.setCursor(Cursor.HAND);
        btnEditar.setCursor(Cursor.HAND);
        idReceta = ventasSingleton.getInstance().getCodReceta();
        mostrarDatos();
    }

    public void mostrarDatos() {
        datos = FXCollections.observableArrayList(ingrediente.consulta());

        // Armamos el mapa idIngrediente -> cantUso a partir de DetalleReceta
        cantidadesPorIngrediente.clear();
        ArrayList<DetalleReceta> detalles = detalle.consultaPorReceta(idReceta);
        for (DetalleReceta d : detalles) {
            cantidadesPorIngrediente.put(d.getIdIngredientes(), String.valueOf(d.getCantUso()));
        }

        columNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columCantidad.setCellValueFactory(cellData -> {
            int idIng = cellData.getValue().getIdIngredientes();
            String cant = cantidadesPorIngrediente.getOrDefault(idIng, "");
            return new SimpleStringProperty(cant);
        });

        tablaIRecetas.setItems(datos);
    }

    public void limpiar() {
        txtCantidad.clear();
        btnAdd.setDisable(true);
        btnEditar.setDisable(true);
    }
    
    @FXML
    private void mostrarFila(MouseEvent event) {
        Ingredientes seleccionado = tablaIRecetas.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            return;
        }

        idIngredienteSeleccionado = seleccionado.getIdIngredientes();
        System.out.println("Ingrediente seleccionado: " + idIngredienteSeleccionado);
        ventasSingleton.getInstance().setCodIngrediente(idIngredienteSeleccionado);

        String cant = cantidadesPorIngrediente.getOrDefault(idIngredienteSeleccionado, "");
        txtCantidad.setText(cant);
        yaAsignado = !cant.isEmpty();

        btnAdd.setDisable(yaAsignado);
        btnEditar.setDisable(!yaAsignado);
    }

    @FXML
    private void add(ActionEvent event) {
        if (!validarCantidad()) {
            return;
        }

        int cant = Integer.parseInt(txtCantidad.getText());
        detalle.setIdReceta(idReceta);
        detalle.setIdIngrediente(idIngredienteSeleccionado);
        detalle.setCantUso(cant);

        if (detalle.insertar()) {
            System.out.println("Ingrediente asignado correctamente.");
            mostrarDatos();
            limpiar();
        } else {
            mostrarAlerta("No se pudo asignar el ingrediente.");
        }
    }

    @FXML
    private void editar(ActionEvent event) {
        if (!validarCantidad()) {
            return;
        }

        double cant = Double.parseDouble(txtCantidad.getText());
        detalle.setIdReceta(idReceta);
        detalle.setIdIngrediente(idIngredienteSeleccionado);
        detalle.setCantUso(cant);

        if (detalle.editar()) {
            System.out.println("Cantidad editada correctamente.");
            mostrarDatos();
            limpiar();
        } else {
            mostrarAlerta("No se pudo editar la cantidad.");
        }
    }

    @FXML
    private void aceptar(ActionEvent event) {
        Stage stage = (Stage) btnAceptar.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void buscar(KeyEvent event) {
        datosBuscados = FXCollections.observableArrayList();
        String buscar = txtBuscar.getText();
        if (buscar.isEmpty()) {
            tablaIRecetas.setItems(datos);
        } else {
            datosBuscados.clear();
            for (Ingredientes dato : datos) {
                if (dato.getNombre().toLowerCase().contains(buscar.toLowerCase())) {
                    datosBuscados.add(dato);
                }
            }
            tablaIRecetas.setItems(datosBuscados);
        }
    }

    private boolean validarCantidad() {
        if (idIngredienteSeleccionado <= 0) {
            mostrarAlerta("Seleccioná un ingrediente de la tabla.");
            return false;
        }
        String texto = txtCantidad.getText();
        if (texto == null || texto.isEmpty()) {
            mostrarAlerta("Ingresá una cantidad.");
            return false;
        }
        try {
            double cant = Double.parseDouble(texto);
            if (cant <= 0) {
                mostrarAlerta("La cantidad debe ser mayor a 0.");
                return false;
            }
        } catch (NumberFormatException ex) {
            mostrarAlerta("La cantidad debe ser un número válido.");
            return false;
        }
        return true;
    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    
}
