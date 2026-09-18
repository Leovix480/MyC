package com.mycompany.myc;

import com.mycompany.modelos.Productos;
import com.mycompany.modelos.Recetas;
import com.mycompany.myc.clases.Textos;
import com.mycompany.myc.clases.ventasSingleton;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.mycompany.modelos.DetalleReceta;
import com.mycompany.modelos.Ingredientes;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Cursor;

public class RecetasController implements Initializable {

    @FXML
    private TextField txtBuscar;
    @FXML
    private TableView<Recetas> tablaRecetas;
    @FXML
    private TableColumn<Recetas, Integer> columID;
    @FXML
    private TableColumn<Recetas, String> columNombre;
    @FXML
    private TextField txtPrecio;
    @FXML
    private TextField txtNombre;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnElimnar;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private TextField txtDesc;
    @FXML
    private TableView<Ingredientes> tablaDI;
    @FXML
    private TableColumn<Ingredientes, String> columDINombre;
    @FXML
    private TableColumn<Ingredientes, String> columDICantidad;
    @FXML
    private Button btnDIAdd;
    @FXML
    private Button btnDIEliminar;

    ObservableList<Recetas> datos;
    ObservableList<Recetas> datosBuscados;
    Recetas receta = new Recetas();
    Productos producto= new Productos();
    int codReceta;
    int id;
    DetalleReceta detalle = new DetalleReceta();
    Ingredientes ingredienteAux = new Ingredientes();
    Map<Integer, String> cantidadesDI = new HashMap<>();
    int idIngredienteDI;

    //No anda (TODAVIA >:D)
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnEditar.setCursor(Cursor.HAND);
        btnElimnar.setCursor(Cursor.HAND);
        btnCancelar.setCursor(Cursor.HAND);
        btnGuardar.setCursor(Cursor.HAND);
        btnAdd.setCursor(Cursor.HAND);
        btnDIEliminar.setCursor(Cursor.HAND);
        btnDIAdd.setCursor(Cursor.HAND);
        mostrarDatos();
    }

    public void mostrarDatos() {
        datos = FXCollections.observableArrayList(receta.consulta());
        columID.setCellValueFactory(new PropertyValueFactory<>("idRecetas"));
        columNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tablaRecetas.setItems(datos);
    }

    public void limpiar() {
        txtNombre.clear();
        txtDesc.clear();
        txtPrecio.clear();
    }

    public void habilitar() {
        txtNombre.setDisable(false);
        txtDesc.setDisable(false);
        txtPrecio.setDisable(false);
        mostrarDI();
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
        } catch (IOException ex) {
            System.getLogger(RecetasController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            System.out.println("holaaaa");
        }
    }

    @FXML
    private void add(ActionEvent event) {
        habilitar();
        btnElimnar.setDisable(true);
        btnEditar.setDisable(true);
        btnCancelar.setDisable(false);
        btnGuardar.setDisable(false);
    }

    // Valida los campos del formulario antes de insertar o editar una receta.
    // Devuelve true si todos los datos son válidos; si no, muestra una alerta y devuelve false.
    private boolean validarDatos() {
        String nom = txtNombre.getText();
        if (nom == null || nom.trim().isEmpty()) {
            mostrarAlerta("Alerta: El nombre de la receta no puede estar vacío.");
            return false;
        }
        if (nom.trim().length() > 60) {
            mostrarAlerta("Alerta: El nombre no puede superar los 60 caracteres.");
            return false;
        }

        String desc = txtDesc.getText();
        if (desc == null || desc.trim().isEmpty()) {
            mostrarAlerta("Alerta: La descripción de la receta no puede estar vacía.");
            return false;
        }
        if (desc.trim().length() > 150) {
            mostrarAlerta("Alerta: La descripción no puede superar los 150 caracteres.");
            return false;
        }

        try {
            double pre = Double.parseDouble(txtPrecio.getText());
            if (pre <= 0) {
                mostrarAlerta("Alerta: El precio debe ser mayor a 0.");
                return false;
            }
        } catch (NumberFormatException | NullPointerException ex) {
            mostrarAlerta("Alerta: El precio debe ser un número válido.");
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
        String desc = txtDesc.getText().trim();
        double pre = Double.parseDouble(txtPrecio.getText());
        receta.setNombre(nom);
        receta.setDescripcion(desc);

        if (!receta.insertar()) {
            mostrarAlerta("No se pudo guardar la receta.");
            return;
        }
        System.out.println("Receta guardada correctamente. ID generado: " + receta.getIdRecetas());
        mostrarDatos();

        int di=receta.getIdRecetas();

        if (di <= 0) {
            mostrarAlerta("Alerta: No se obtuvo un identificador de receta válido.");
            return;
        }
        producto.setNombre(nom);
        producto.setPrecio(pre);
        producto.setIdRecetas(di);

        if (producto.insertar()) {
            System.out.println("Producto guardado correctamente.");
            limpiar();
            cancelar(event);
        } else {
            mostrarAlerta("No se pudo guardar el producto asociado a la receta.");
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        limpiar();
        id = 0;
        codReceta = 0;
        idIngredienteDI = 0;
        btnDIEliminar.setDisable(true);
        mostrarDI();
        txtNombre.setDisable(true);
        txtDesc.setDisable(true);
        btnCancelar.setDisable(true);
        btnGuardar.setDisable(true);
        btnAdd.setDisable(false);
        btnEditar.setDisable(true);
        btnElimnar.setDisable(true);
        txtPrecio.setDisable(true);
    }

    @FXML
    private void mostrarFila(MouseEvent event) {
        Recetas r = tablaRecetas.getSelectionModel().getSelectedItem();
        if (r == null) {
            return;
        }
        ventasSingleton.getInstance().setCodReceta(r.getIdRecetas());
        codReceta = ventasSingleton.getInstance().getCodReceta();
        id = r.getIdRecetas();

        ArrayList<Recetas> lista = r.consulta();
        for (Recetas rec : datos) {
            if (rec.getIdRecetas() == codReceta) {
                System.out.println("Encontrado");
                txtNombre.setText(rec.getNombre());
                txtDesc.setText(rec.getDescripcion());

                // Buscar producto relacionado con ek idReceta
                Productos prod = producto.consultaPorReceta(codReceta);
                if (prod != null) {
                    txtPrecio.setText(String.valueOf(prod.getPrecio()));
                    ventasSingleton.getInstance().setCodProducto(prod.getIdProducto());
                } else {
                    txtPrecio.clear();
                    ventasSingleton.getInstance().setCodProducto(0);
                }

                habilitar();
                btnEditar.setDisable(false);
                btnElimnar.setDisable(false);
                btnCancelar.setDisable(false);
                btnAdd.setDisable(true);
                btnGuardar.setDisable(true);
            }
        }
    }

    @FXML
    private void eliminar(ActionEvent event) {
        if (id <= 0) {
            mostrarAlerta("Alerta: Seleccioná una receta de la tabla para eliminar.");
            return;
        }

        int codProd = ventasSingleton.getInstance().getCodProducto();

        if (codProd > 0) {
            producto.setIdProducto(codProd);
            if (!producto.eliminar()) {
                mostrarAlerta("No se pudo eliminar el producto asociado.");
                return;
            }
            producto.renumerarDespuesDeEliminar(codProd);
        }

        if (!detalle.eliminarPorReceta(id)) {
            mostrarAlerta("No se pudieron eliminar los ingredientes de la receta.");
            return;
        }

        receta.setIdRecetas(id);
        if (receta.eliminar()) {
            System.out.println("Receta eliminada correctamente.");
            receta.renumerarDespuesDeEliminar(id);
            mostrarDatos();
            limpiar();
            cancelar(event);
        } else {
            mostrarAlerta("No se pudo eliminar la receta.");
        }
    }

    @FXML
    private void editar(ActionEvent event) {
        if (id <= 0) {
            mostrarAlerta("Alerta: Seleccioná una receta de la tabla para editar.");
            return;
        }
        if (!validarDatos()) {
            return;
        }

        String nom = Textos.capitalizarInicial(txtNombre.getText());
        String desc = txtDesc.getText().trim();
        double pre = Double.parseDouble(txtPrecio.getText());
        receta.setNombre(nom);
        receta.setDescripcion(desc);
        receta.setIdRecetas(id);

        if (!receta.editar()) {
            mostrarAlerta("No se pudo editar la receta.");
            return;
        }
        System.out.println("Receta editada correctamente.");

        int codProd = ventasSingleton.getInstance().getCodProducto();
        if (codProd > 0) {
            producto.setIdProducto(codProd);
            producto.setNombre(nom);
            producto.setPrecio(pre);
            producto.setIdRecetas(id);

            if (producto.editar()) {
                System.out.println("Producto editado correctamente.");
            } else {
                mostrarAlerta("No se pudo editar el producto asociado.");
            }
        }

        mostrarDatos();
        limpiar();
        cancelar(event);
    }

    @FXML
    private void buscar(KeyEvent event) {
        datosBuscados = FXCollections.observableArrayList();
        String buscar = txtBuscar.getText();
        if (buscar.isEmpty())
            tablaRecetas.setItems(datos);
        else {
            datosBuscados.clear();
            for (Recetas dato : datos) {
                String aux = String.valueOf(dato.getIdRecetas());
                if (dato.getNombre().toLowerCase().contains(buscar.toLowerCase()) || aux.toLowerCase().contains(buscar.toLowerCase())) {
                    datosBuscados.add(dato);
                }
            }
            tablaRecetas.setItems(datosBuscados);
        }
    }

    public void mostrarDI() {
        ArrayList<DetalleReceta> detalles = detalle.consultaPorReceta(id);
        ArrayList<Ingredientes> todos = ingredienteAux.consulta();

        cantidadesDI.clear();
        ObservableList<Ingredientes> datosDI = FXCollections.observableArrayList();

        for (DetalleReceta d : detalles) {
            for (Ingredientes ing : todos) {
                if (ing.getIdIngredientes() == d.getIdIngredientes()) {
                    cantidadesDI.put(ing.getIdIngredientes(), String.valueOf(d.getCantUso()));
                    datosDI.add(ing);
                    break;
                }
            }
        }

        columDINombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columDICantidad.setCellValueFactory(cellData -> {
            int idIng = cellData.getValue().getIdIngredientes();
            return new SimpleStringProperty(cantidadesDI.getOrDefault(idIng, ""));
        });

        tablaDI.setItems(datosDI);
    }

    @FXML
    private void mostrarFilaDI(MouseEvent event) {
        Ingredientes seleccionado = tablaDI.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            return;
        }
        idIngredienteDI = seleccionado.getIdIngredientes();
        btnDIEliminar.setDisable(false);
    }

    @FXML
    private void abrirCIR(ActionEvent event) {
        if (id <= 0) {
            mostrarAlerta("Alerta: Seleccioná una receta antes de agregar ingredientes.");
            return;
        }
        ventasSingleton.getInstance().setCodReceta(id);
        abrirFxml("cargar_ingredientes_receta.fxml", "administrar ingredientes receta",false);
        mostrarDI();
    }

    @FXML
    private void eliminarIngCant(ActionEvent event) {
        if (idIngredienteDI <= 0) {
            mostrarAlerta("Alerta: Seleccioná un ingrediente de la tabla para eliminar.");
            return;
        }
        detalle.setIdReceta(id);
        detalle.setIdIngrediente(idIngredienteDI);

        if (detalle.eliminar()) {
            System.out.println("Ingrediente eliminado de la receta.");
            mostrarDI();
            idIngredienteDI = 0;
            btnDIEliminar.setDisable(true);
        } else {
            mostrarAlerta("No se pudo eliminar el ingrediente de la receta.");
        }
    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
