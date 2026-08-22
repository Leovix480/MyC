package com.mycompany.myc;

import com.mycompany.modelos.Productos;
import com.mycompany.modelos.Recetas;
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
        FXMLLoader loader=new FXMLLoader(getClass().getResource(formulario));
        try {
            Parent root=loader.load();
            Stage stage=new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
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
 
    @FXML
    private void guardar(ActionEvent event) {
        String nom = txtNombre.getText();
        String desc = txtDesc.getText();
        double pre = Double.parseDouble(txtPrecio.getText());
        receta.setNombre(nom);
        receta.setDescripcion(desc);
        
        if (receta.insertar()) {
            System.out.println("Receta guardada correctamente. ID generado: " + receta.getIdRecetas());
            mostrarDatos();
        } else {
            System.out.println("No se pudo guardar la receta.");
        }
        
        int di=receta.getIdRecetas();
        System.out.println("ID receta generado: " + di);

        if (di <= 0) {
            System.out.println("Error: no se obtuvo un idRecetas válido.");
            return;
        }
        System.out.println(di);
        producto.setNombre(nom);
        producto.setPrecio(pre);
        producto.setIdRecetas(di);
 
        if (producto.insertar()) {
            System.out.println("Producto guardada correctamente.");
            limpiar();
            cancelar(event);
        } else {
            System.out.println("No se pudo guardar el producto.");
        }
    }
 
    @FXML
    private void cancelar(ActionEvent event) {
        limpiar();
        txtNombre.setDisable(true);
        txtDesc.setDisable(true);
        btnCancelar.setDisable(true);
        btnGuardar.setDisable(true);
        btnAdd.setDisable(false);
        btnEditar.setDisable(true);
        btnElimnar.setDisable(true);
    }
 
    @FXML
    private void mostrarFila(MouseEvent event) {
        Recetas r = tablaRecetas.getSelectionModel().getSelectedItem();
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
        int codProd = ventasSingleton.getInstance().getCodProducto();

        if (codProd > 0) {
            producto.setIdProducto(codProd);
            if (!producto.eliminar()) {
                System.out.println("No se pudo eliminar el producto asociado.");
                return;
            }
        }

        receta.setIdRecetas(id);
        if (receta.eliminar()) {
            System.out.println("Receta eliminada correctamente.");
            mostrarDatos();
            limpiar();
            cancelar(event);
        } else {
            System.out.println("No se pudo eliminar la receta.");
        }
    }
 
    @FXML
    private void editar(ActionEvent event) {
        String nom = txtNombre.getText();
        String desc = txtDesc.getText();
        double pre;
        try {
            pre = Double.parseDouble(txtPrecio.getText());
        } catch (NumberFormatException ex) {
            System.out.println("Precio inválido.");
            return;
        }
        receta.setNombre(nom);
        receta.setDescripcion(desc);
        receta.setIdRecetas(id);

        if (!receta.editar()) {
            System.out.println("No se pudo editar la receta.");
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
                System.out.println("No se pudo editar el producto.");
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
            System.out.println("Seleccioná una receta antes de agregar ingredientes.");
            return;
        }
        ventasSingleton.getInstance().setCodReceta(id);
        abrirFxml("cargar_ingredientes_receta.fxml", "administrar ingredientes receta");
        mostrarDI();
    }

    @FXML
    private void eliminarIngCant(ActionEvent event) {
        if (idIngredienteDI <= 0) {
            System.out.println("Seleccioná un ingrediente de la tabla para eliminar.");
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
            System.out.println("No se pudo eliminar el ingrediente de la receta.");
        }
    }
}