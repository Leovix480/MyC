package com.mycompany.myc;

import com.mycompany.modelos.Ingredientes;
import com.mycompany.myc.clases.ventasSingleton;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

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
    
    ObservableList<Ingredientes> datos;
    ObservableList<Ingredientes> datosF;
    ObservableList<Ingredientes> datosBuscados;
    Ingredientes ing = new Ingredientes();
    int codIngredientes;
    int id;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
        String nom = txtNombre.getText();
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
            System.out.println("No se pudo edittar el ingrediente.");
        }
    }

    @FXML
    private void guardar(ActionEvent event) {
        String nom = txtNombre.getText();
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
            System.out.println("No se pudo guardar el ingrediente.");
        }
    }

    @FXML
    private void eliminar(ActionEvent event) {
        String nom = txtNombre.getText();
        double pre = Double.parseDouble(txtPrecio.getText());
        int sto = Integer.parseInt(txtStock.getText());
        int stom = Integer.parseInt(txtStockMin.getText());
        ing.setNombre(nom);
        ing.setPrecio(pre);
        ing.setStock(sto);
        ing.setStockMin(stom);
        ing.setIdIngredientes(id);
        
        if (ing.eliminar()) {
            System.out.println("Ingrediente eliminado correctamente.");
            mostrarDatos();
            mostrarIFaltantes();
            limpiar();
            cancelar(event);
        } else {
            System.out.println("No se pudo eliminar el ingredientes.");
        }
        id = -1;
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
        ventasSingleton.getInstance().setCodIngrediente(i.getIdIngredientes());
        codIngredientes = ventasSingleton.getInstance().getCodIngrediente();
        id = i.getIdIngredientes();
        ArrayList<Ingredientes> lista = i.consulta();
        for (Ingredientes ingrediente : lista) {
            if (ingrediente.getIdIngredientes() == codIngredientes) {
                System.out.println("Encontrado");
                txtNombre.setText(ingrediente.getNombre());
                txtPrecio.setText(String.valueOf(ingrediente.getPrecio()));
                txtStock.setText(String.valueOf(ingrediente.getStock()));
                txtStockMin.setText(String.valueOf(ingrediente.getStockMin()));
                habilitar();
                btnEditar.setDisable(false);
                btnEliminar.setDisable(false);
                btnCancelar.setDisable(false);
                btnAdd.setDisable(true);
            }
        }
    }
}