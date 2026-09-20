package com.mycompany.myc;

import com.mycompany.modelos.Ingredientes;
import com.mycompany.myc.clases.Conexion;
import com.mycompany.myc.clases.Textos;
import com.mycompany.myc.clases.ventasSingleton;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
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
import javafx.scene.layout.AnchorPane;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class IngredientesController implements Initializable {

    @FXML
    private TextField txtBuscarIngredientes;
    @FXML
    private TableView<Ingredientes> tablaIngredientes;
    @FXML
    private TableColumn<Ingredientes, Integer> columID;
    @FXML
    private TableColumn<Ingredientes, String> columNombre;
    @FXML
    private TableColumn<Ingredientes, Double> columPrecio;
    @FXML
    private TableColumn<Ingredientes, Integer> columStock;
    @FXML
    private TableView<Ingredientes> tablaFaltantes;
    @FXML
    private TableColumn<Ingredientes, String> columNombreF;
    @FXML
    private TableColumn<Ingredientes, Integer> columStockMin;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtPrecio;
    @FXML
    private TextField txtStock;
    @FXML
    private TextField txtStockMin;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnAdd;
    @FXML
    private AnchorPane root;
    @FXML
    private Button btnImprimir;
    
    ObservableList<Ingredientes> datos;
    ObservableList<Ingredientes> datosF;
    ObservableList<Ingredientes> datosBuscados;
    Ingredientes ing = new Ingredientes();
    int codIngredientes;
    int id;
    BooleanProperty verF = new SimpleBooleanProperty();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnEditar.setCursor(Cursor.HAND);
        btnEliminar.setCursor(Cursor.HAND);
        btnCancelar.setCursor(Cursor.HAND);
        btnGuardar.setCursor(Cursor.HAND);
        btnAdd.setCursor(Cursor.HAND);
        btnImprimir.setCursor(Cursor.HAND);
        mostrarDatos();
        mostrarIFaltantes();
    }    
    
    public void mostrarIFaltantes(){
        datosF = FXCollections.observableArrayList(ing.consulta());
        datosF.clear();
        for (Ingredientes registro : datos) {
           if(registro.getStock()<= registro.getStockMin()){
               datosF.add(registro);
           }
        }
        columNombreF.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columStockMin.setCellValueFactory(new PropertyValueFactory<>("stockMin"));
        tablaFaltantes.setItems(datosF);
        verF.bind(Bindings.isEmpty(datosF));
    }
    
    
    public void mostrarDatos(){
        datos=FXCollections.observableArrayList(ing.consulta());
        columID.setCellValueFactory(new PropertyValueFactory<>("idIngredientes"));
        columNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        columStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        tablaIngredientes.setItems(datos);
    }
    
    public void limpiar() {
        txtNombre.clear();
        txtPrecio.clear();
        txtStock.clear();
        txtStockMin.clear();
    }

    public void habilitar() {
        txtNombre.setDisable(false);
        txtPrecio.setDisable(false);
        txtStock.setDisable(false);
        txtStockMin.setDisable(false);
    }

    @FXML
    private void editar(ActionEvent event) {
        if (id <= 0) {
            mostrarAlerta("Alerta: Seleccioná un ingrediente de la tabla para editar.");
            return;
        }
        if (!validarDatos()) {
            return;
        }

        String nom = Textos.capitalizarInicial(txtNombre.getText());
        double pre = Double.parseDouble(txtPrecio.getText());
        int sto = Integer.parseInt(txtStock.getText());
        int stom = Integer.parseInt(txtStockMin.getText());
        ing.setNombre(nom);
        ing.setPrecio(pre);
        ing.setStock(sto);
        ing.setStockMin(stom);
        ing.setIdIngredientes(id);

        if (ing.editar()) {
            System.out.println("Ingrediente editado correctamente.");
            mostrarDatos();
            mostrarIFaltantes();
            limpiar();
            cancelar(event);
        } else {
            mostrarAlerta("No se pudo editar el ingrediente.");
        }
    }

    // Valida los campos del formulario antes de insertar o editar un ingrediente.
    // Devuelve true si todos los datos son válidos; si no, muestra una alerta y devuelve false.
    private boolean validarDatos() {
        String nom = txtNombre.getText();
        if (nom == null || nom.trim().isEmpty()) {
            mostrarAlerta("Alerta: El nombre del ingrediente no puede estar vacío.");
            return false;
        }
        if (nom.trim().length() > 100) {
            mostrarAlerta("Alerta: El nombre no puede superar los 100 caracteres.");
            return false;
        }

        double pre;
        try {
            pre = Double.parseDouble(txtPrecio.getText());
        } catch (NumberFormatException | NullPointerException ex) {
            mostrarAlerta("Alerta: El precio debe ser un número válido.");
            return false;
        }
        if (pre <= 0) {
            mostrarAlerta("Alerta: El precio debe ser mayor a 0.");
            return false;
        }

        int sto;
        try {
            sto = Integer.parseInt(txtStock.getText());
        } catch (NumberFormatException | NullPointerException ex) {
            mostrarAlerta("Alerta: El stock debe ser un número entero válido.");
            return false;
        }
        if (sto < 0) {
            mostrarAlerta("Alerta: El stock no puede ser negativo.");
            return false;
        }

        int stom;
        try {
            stom = Integer.parseInt(txtStockMin.getText());
        } catch (NumberFormatException | NullPointerException ex) {
            mostrarAlerta("Alerta: El stock mínimo debe ser un número entero válido.");
            return false;
        }
        if (stom < 0) {
            mostrarAlerta("Alerta: El stock mínimo no puede ser negativo.");
            return false;
        }

        return true;
    }

    @FXML
    private void guardar(ActionEvent event) {
        if (!validarDatos()) {
            return;
        }

        String nom = Textos.capitalizarInicial(txtNombre.getText());
        double pre = Double.parseDouble(txtPrecio.getText());
        int sto = Integer.parseInt(txtStock.getText());
        int stom = Integer.parseInt(txtStockMin.getText());
        ing.setNombre(nom);
        ing.setPrecio(pre);
        ing.setStock(sto);
        ing.setStockMin(stom);

        if (ing.insertar()) {
            System.out.println("Ingrediente guardado correctamente.");
            mostrarDatos();
            mostrarIFaltantes();
            limpiar();
            cancelar(event);
        } else {
            mostrarAlerta("No se pudo guardar el ingrediente.");
        }
    }

    @FXML
    private void eliminar(ActionEvent event) {
        if (id <= 0) {
            mostrarAlerta("Alerta: Seleccioná un ingrediente de la tabla para eliminar.");
            return;
        }

        ing.setIdIngredientes(id);

        if (ing.eliminar()) {
            System.out.println("Ingrediente eliminado correctamente.");
            ing.renumerarDespuesDeEliminar(id);
            mostrarDatos();
            mostrarIFaltantes();
            limpiar();
            cancelar(event);
            id = -1;
        } else {
            mostrarAlerta("No se pudo eliminar el ingrediente.");
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        limpiar();
        txtNombre.setDisable(true);
        txtPrecio.setDisable(true);
        txtStock.setDisable(true);
        txtStockMin.setDisable(true);
        btnCancelar.setDisable(true);
        btnGuardar.setDisable(true);
        btnAdd.setDisable(false);
        btnEditar.setDisable(true);
        btnEliminar.setDisable(true);
    }

    @FXML
    private void add(ActionEvent event) {
        limpiar();
        id = -1;
        habilitar();
        btnEliminar.setDisable(true);
        btnEditar.setDisable(true);
        btnCancelar.setDisable(false);
        btnGuardar.setDisable(false);
    }

    @FXML
    private void buscar(KeyEvent event) {
        datosBuscados = FXCollections.observableArrayList();
        String buscar = txtBuscarIngredientes.getText();
        if (buscar.isEmpty())
            tablaIngredientes.setItems(datos);
        else {
            datosBuscados.clear();
            for (Ingredientes dato : datos) {
                String aux = String.valueOf(dato.getIdIngredientes());
                if (dato.getNombre().toLowerCase().contains(buscar.toLowerCase()) || aux.toLowerCase().contains(buscar.toLowerCase())) {
                    datosBuscados.add(dato);
                }
            }
            tablaIngredientes.setItems(datosBuscados);
        }
    }

    @FXML
    private void mostrarFila(MouseEvent event) {
        Ingredientes i = tablaIngredientes.getSelectionModel().getSelectedItem();
        if (i == null) {
            return;
        }
        ventasSingleton.getInstance().setCodIngrediente(i.getIdIngredientes());
        codIngredientes = ventasSingleton.getInstance().getCodIngrediente();
        id = i.getIdIngredientes();

        txtNombre.setText(i.getNombre());
        txtPrecio.setText(String.valueOf(i.getPrecio()));
        txtStock.setText(String.valueOf(i.getStock()));
        txtStockMin.setText(String.valueOf(i.getStockMin()));
        habilitar();
        btnEditar.setDisable(false);
        btnEliminar.setDisable(false);
        btnCancelar.setDisable(false);
        btnAdd.setDisable(true);
    }

    @FXML
    private void imprimir(ActionEvent event) {
        if(!verF.get()){    
            String rutaReporte = "/reportes/ingredientesF.jasper";

            try (java.io.InputStream streamReporte = getClass().getResourceAsStream(rutaReporte)) {

                if (streamReporte == null) {
                    throw new java.io.FileNotFoundException("No se encontró el archivo en: " + rutaReporte);
                }

                // 2. Usar la misma conexión configurada para el resto de la app (respeta el
                // servidor/host actual elegido en Configuración del servidor, sea local o remoto)
                java.sql.Connection conexion = new Conexion().getCon();
                if (conexion == null) {
                    throw new java.sql.SQLException("No se pudo establecer conexión con la base de datos.");
                }

                // Mapa de parámetros vacío porque imprime todos los pedidos
                Map<String, Object> parametros = new HashMap<>();

                try {
                    // 3. Llenar el reporte
                    net.sf.jasperreports.engine.JasperPrint jasperPrint = net.sf.jasperreports.engine.JasperFillManager.fillReport(streamReporte, parametros, conexion);

                    // 4. Abrir el visor en pantalla
                    net.sf.jasperreports.view.JasperViewer visor = new net.sf.jasperreports.view.JasperViewer(jasperPrint, false);
                    visor.setTitle("Reporte de Ingredientes Faltantes");
                    visor.setVisible(true);
                } finally {
                    conexion.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error de Reporte");
                alert.setHeaderText("No se pudo cargar el reporte de los ingredientes");
                alert.setContentText("Detalle: " + e.getMessage());
                alert.showAndWait();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No se puede imprimir un reporte si no faltan ingredientes");
            alert.showAndWait();
        }
    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}