package com.mycompany.myc;

import com.mycompany.modelos.Clientes;
import com.mycompany.modelos.DetalleReceta;
import com.mycompany.modelos.DetalleVenta;
import com.mycompany.modelos.Ingredientes;
import com.mycompany.modelos.Productos;
import com.mycompany.modelos.Venta;
import com.mycompany.myc.clases.ventasSingleton;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PedidosController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private TextField txtBuscarCliente;
    @FXML
    private TableView<Venta> tablaClientes;
    @FXML
    private TableColumn<Venta, Integer> columClienteID;
    @FXML
    private TableColumn<Venta, String> columClienteNombre;
    @FXML
    private TextField txtNombreCliente;
    @FXML
    private Button btnAddCliente;
    @FXML
    private TextField txtNombreProducto;
    @FXML
    private Button btnAddProducto;
    @FXML
    private DatePicker dpFecha;
    @FXML
    private TextField txtCantidad;
    @FXML
    private Button btnAgregar;
    @FXML
    private TableView<Productos> tablaDetalle;
    @FXML
    private TableColumn<Productos, Integer> columIDProducto;
    @FXML
    private TableColumn<Productos, String> columNombreProducto;
    @FXML
    private TableColumn<Productos, String> columCantidadDet;
    @FXML
    private TableColumn<Productos, String> columPrecioDet;
    @FXML
    private TableColumn<Productos, String> columSubtotalDet;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnImprimir;
    @FXML
    private TextField txtTotal;
    @FXML
    private ComboBox<String> cmbTipoPago;

    Clientes cliente = new Clientes();
    Productos productoModelo = new Productos();
    Venta venta = new Venta();
    DetalleVenta detalleVenta = new DetalleVenta();
    DetalleReceta detalleReceta = new DetalleReceta();
    Ingredientes ingredienteModelo = new Ingredientes();

    ObservableList<Venta> datosVentas;
    ObservableList<Venta> datosVentasBuscadas;
    ObservableList<Productos> datosDetalle;
    Map<Integer, Integer> cantidadPorProducto = new HashMap<>();
    Map<Integer, String> nombreClientePorVenta = new HashMap<>();

    int idClienteSeleccionado;
    int idProductoSeleccionadoParaAgregar;
    int idProductoEnEdicion;
    boolean modoSoloLectura;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbTipoPago.setItems(FXCollections.observableArrayList("Efectivo", "Tarjeta"));
        datosDetalle = FXCollections.observableArrayList();
        configurarColumnasDetalle();
        mostrarVentas();
        limpiarVentaActual();
        
        btnEditar.setCursor(Cursor.HAND);
        btnEliminar.setCursor(Cursor.HAND);
        btnCancelar.setCursor(Cursor.HAND);
        btnGuardar.setCursor(Cursor.HAND);
        btnAdd.setCursor(Cursor.HAND);
        btnAddCliente.setCursor(Cursor.HAND);
        btnAddProducto.setCursor(Cursor.HAND);
        btnAgregar.setCursor(Cursor.HAND);
        btnImprimir.setCursor(Cursor.HAND);
        
    }

    // ---------- Tabla superior (ventas realizadas) ----------
    public void mostrarVentas() {
        datosVentas = FXCollections.observableArrayList(venta.consulta());

        nombreClientePorVenta.clear();
        ArrayList<Clientes> todosClientes = cliente.consulta();
        for (Venta v : datosVentas) {
            for (Clientes c : todosClientes) {
                if (c.getIdCliente() == v.getIdCliente()) {
                    nombreClientePorVenta.put(v.getIdVenta(), c.getNombre() + " " + c.getApellido());
                    break;
                }
            }
        }

        columClienteID.setCellValueFactory(new PropertyValueFactory<>("idVenta"));
        columClienteNombre.setCellValueFactory(cellData -> {
            int idV = cellData.getValue().getIdVenta();
            return new SimpleStringProperty(nombreClientePorVenta.getOrDefault(idV, ""));
        });

        tablaClientes.setItems(datosVentas);
    }

    @FXML
    private void buscarCliente(KeyEvent event) {
        datosVentasBuscadas = FXCollections.observableArrayList();
        String buscar = txtBuscarCliente.getText();
        if (buscar.isEmpty()) {
            tablaClientes.setItems(datosVentas);
        } else {
            datosVentasBuscadas.clear();
            for (Venta v : datosVentas) {
                String nom = nombreClientePorVenta.getOrDefault(v.getIdVenta(), "");
                if (nom.toLowerCase().contains(buscar.toLowerCase())) {
                    datosVentasBuscadas.add(v);
                }
            }
            tablaClientes.setItems(datosVentasBuscadas);
        }
    }

    @FXML
    private void mostrarCliente(MouseEvent event) {
        Venta seleccionada = tablaClientes.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            return;
        }
        mostrarVentaGuardada(seleccionada);
    }

    private void mostrarVentaGuardada(Venta v) {
        modoSoloLectura = true;

        Clientes c = buscarClientePorId(v.getIdCliente());
        if (c != null) {
            txtNombreCliente.setText(c.getNombre() + " " + c.getApellido());
            idClienteSeleccionado = c.getIdCliente();
        }

        cmbTipoPago.setValue(v.getTipoPago());

        cantidadPorProducto.clear();
        datosDetalle.clear();
        ArrayList<DetalleVenta> detalles = detalleVenta.consultaPorVenta(v.getIdVenta());
        for (DetalleVenta d : detalles) {
            Productos p = productoModelo.consultaPorId(d.getIdProducto());
            if (p != null) {
                cantidadPorProducto.put(p.getIdProducto(), d.getCantidad());
                datosDetalle.add(p);
            }
        }
        tablaDetalle.setItems(datosDetalle);
        actualizarTotal();

        txtNombreCliente.setDisable(true);
        cmbTipoPago.setDisable(true);
        txtCantidad.setDisable(true);
        btnAddCliente.setDisable(true);
        btnAddProducto.setDisable(true);
        btnAgregar.setDisable(true);
        btnEditar.setDisable(true);
        btnEliminar.setDisable(true);
        btnGuardar.setDisable(true);
        btnCancelar.setDisable(true);
        btnAdd.setDisable(false);
        btnImprimir.setDisable(false);
    }

    private Clientes buscarClientePorId(int idCliente) {
        ArrayList<Clientes> todos = cliente.consulta();
        for (Clientes c : todos) {
            if (c.getIdCliente() == idCliente) {
                return c;
            }
        }
        return null;
    }

    // ---------- Botón "Añadir" del panel inferior: vuelve a un pedido nuevo ----------
    @FXML
    private void add(ActionEvent event) {
        limpiarVentaActual();
    }

    // ---------- Agregar cliente ----------
    @FXML
    private void addCliente(ActionEvent event) {
        if (modoSoloLectura) {
            return;
        }
        abrirFxml("agregar_clientes.fxml", "Seleccionar cliente");

        int idSel = ventasSingleton.getInstance().getCodCliente();
        if (idSel > 0) {
            Clientes c = buscarClientePorId(idSel);
            if (c != null) {
                idClienteSeleccionado = c.getIdCliente();
                txtNombreCliente.setText(c.getNombre() + " " + c.getApellido());
            }
        }
    }

    // ---------- Agregar producto ----------
    @FXML
    private void abrirProducto(ActionEvent event) {
        if (modoSoloLectura) {
            return;
        }
        abrirFxml("agregar_producto.fxml", "Seleccionar producto");

        int idSel = ventasSingleton.getInstance().getCodProducto();
        if (idSel > 0) {
            Productos p = productoModelo.consultaPorId(idSel);
            if (p != null) {
                idProductoSeleccionadoParaAgregar = p.getIdProducto();
                txtNombreProducto.setText(p.getNombre());
            }
        }
    }

    @FXML
    private void agregar(ActionEvent event) {
        if (modoSoloLectura) {
            return;
        }

        if (idProductoSeleccionadoParaAgregar <= 0) {
            mostrarAlerta("Seleccioná un producto primero.");
            return;
        }

        int cant;
        try {
            cant = Integer.parseInt(txtCantidad.getText());
            if (cant <= 0) {
                mostrarAlerta("La cantidad debe ser mayor a 0.");
                return;
            }
        } catch (NumberFormatException ex) {
            mostrarAlerta("Ingresá una cantidad válida.");
            return;
        }

        int cantidadPrevia = cantidadPorProducto.getOrDefault(idProductoSeleccionadoParaAgregar, 0);
        int cantidadTotalNueva = cantidadPrevia + cant;

        if (!hayStockSuficiente(idProductoSeleccionadoParaAgregar, cantidadTotalNueva)) {
            mostrarAlerta("No hay suficiente stock de ingredientes para realizar esta venta.");
            return;
        }

        if (cantidadPrevia > 0) {
            cantidadPorProducto.put(idProductoSeleccionadoParaAgregar, cantidadTotalNueva);
            tablaDetalle.refresh();
        } else {
            Productos p = productoModelo.consultaPorId(idProductoSeleccionadoParaAgregar);
            if (p == null) {
                mostrarAlerta("No se pudo obtener el producto.");
                return;
            }
            cantidadPorProducto.put(idProductoSeleccionadoParaAgregar, cantidadTotalNueva);
            datosDetalle.add(p);
        }

        txtCantidad.clear();
        txtNombreProducto.clear();
        idProductoSeleccionadoParaAgregar = 0;
        actualizarTotal();
    }

    // ---------- Tabla de detalle ----------
    private void configurarColumnasDetalle() {
        columIDProducto.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        columNombreProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        columCantidadDet.setCellValueFactory(cellData -> {
            int idP = cellData.getValue().getIdProducto();
            return new SimpleStringProperty(String.valueOf(cantidadPorProducto.getOrDefault(idP, 0)));
        });

        columPrecioDet.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf(cellData.getValue().getPrecio()))
        );

        columSubtotalDet.setCellValueFactory(cellData -> {
            Productos p = cellData.getValue();
            int cant = cantidadPorProducto.getOrDefault(p.getIdProducto(), 0);
            return new SimpleStringProperty(String.valueOf(p.getPrecio() * cant));
        });

        tablaDetalle.setItems(datosDetalle);
    }

    @FXML
    private void mostrarFilaDetalle(MouseEvent event) {
        Productos seleccionado = tablaDetalle.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            return;
        }
        idProductoEnEdicion = seleccionado.getIdProducto();
        if (!modoSoloLectura) {
            txtCantidad.setText(String.valueOf(cantidadPorProducto.getOrDefault(idProductoEnEdicion, 0)));
        }
    }

    @FXML
    private void editar(ActionEvent event) {
        if (modoSoloLectura) {
            return;
        }

        if (idProductoEnEdicion <= 0) {
            mostrarAlerta("Seleccioná un producto de la tabla para editar.");
            return;
        }

        int nuevaCant;
        try {
            nuevaCant = Integer.parseInt(txtCantidad.getText());
            if (nuevaCant <= 0) {
                mostrarAlerta("La cantidad debe ser mayor a 0.");
                return;
            }
        } catch (NumberFormatException ex) {
            mostrarAlerta("Ingresá una cantidad válida.");
            return;
        }

        if (!hayStockSuficiente(idProductoEnEdicion, nuevaCant)) {
            mostrarAlerta("No hay suficiente stock de ingredientes para esa cantidad.");
            return;
        }

        cantidadPorProducto.put(idProductoEnEdicion, nuevaCant);
        tablaDetalle.refresh();
        actualizarTotal();
        txtCantidad.clear();
        idProductoEnEdicion = 0;
    }

    @FXML
    private void eliminar(ActionEvent event) {
        if (modoSoloLectura) {
            return;
        }

        if (idProductoEnEdicion <= 0) {
            mostrarAlerta("Seleccioná un producto de la tabla para eliminar.");
            return;
        }

        cantidadPorProducto.remove(idProductoEnEdicion);
        datosDetalle.removeIf(p -> p.getIdProducto() == idProductoEnEdicion);
        idProductoEnEdicion = 0;
        txtCantidad.clear();
        actualizarTotal();
    }

    private void actualizarTotal() {
        double total = 0;
        for (Productos p : datosDetalle) {
            total += p.getPrecio() * cantidadPorProducto.getOrDefault(p.getIdProducto(), 0);
        }
        txtTotal.setText(String.valueOf(total));
    }

    // ---------- Validación de stock ----------
    private boolean hayStockSuficiente(int idProductoCambiado, int cantidadNuevaDelProducto) {
        Map<Integer, Double> requeridoPorIngrediente = new HashMap<>();
        boolean yaEstaEnTabla = false;

        for (Productos p : datosDetalle) {
            int cant = (p.getIdProducto() == idProductoCambiado)
                    ? cantidadNuevaDelProducto
                    : cantidadPorProducto.getOrDefault(p.getIdProducto(), 0);
            acumularRequerimiento(requeridoPorIngrediente, p.getIdProducto(), cant);
            if (p.getIdProducto() == idProductoCambiado) {
                yaEstaEnTabla = true;
            }
        }

        if (!yaEstaEnTabla) {
            acumularRequerimiento(requeridoPorIngrediente, idProductoCambiado, cantidadNuevaDelProducto);
        }

        for (Map.Entry<Integer, Double> entry : requeridoPorIngrediente.entrySet()) {
            Ingredientes ing = ingredienteModelo.consultaPorId(entry.getKey());
            if (ing == null) {
                continue;
            }
            if (entry.getValue() > ing.getStock()) {
                return false;
            }
        }
        return true;
    }

    private void acumularRequerimiento(Map<Integer, Double> mapa, int idProducto, int cantidadProducto) {
        if (cantidadProducto <= 0) {
            return;
        }
        Productos p = productoModelo.consultaPorId(idProducto);
        if (p == null) {
            return;
        }

        ArrayList<DetalleReceta> detallesReceta = detalleReceta.consultaPorReceta(p.getIdRecetas());
        for (DetalleReceta d : detallesReceta) {
            double requerido = d.getCantUso() * cantidadProducto;
            mapa.merge(d.getIdIngredientes(), requerido, Double::sum);
        }
    }

    // ---------- Guardar venta ----------
    @FXML
    private void guardar(ActionEvent event) {
        if (modoSoloLectura) {
            return;
        }

        if (idClienteSeleccionado <= 0) {
            mostrarAlerta("Seleccioná un cliente para la venta.");
            return;
        }
        if (datosDetalle.isEmpty()) {
            mostrarAlerta("Agregá al menos un producto a la venta.");
            return;
        }
        String tipoPago = cmbTipoPago.getValue();
        if (tipoPago == null || tipoPago.isEmpty()) {
            mostrarAlerta("Seleccioná un método de pago.");
            return;
        }

        venta.setFecha(LocalDateTime.now());
        venta.setIdCliente(idClienteSeleccionado);
        venta.setTipoPago(tipoPago);
        venta.setTotalVenta(calcularTotalActual());

        if (!venta.insertar()) {
            mostrarAlerta("No se pudo guardar la venta.");
            return;
        }

        int idVentaGenerado = venta.getIdVenta();
        boolean todoOk = true;
        for (Productos p : datosDetalle) {
            int cant = cantidadPorProducto.getOrDefault(p.getIdProducto(), 0);
            detalleVenta.setIdVenta(idVentaGenerado);
            detalleVenta.setIdProducto(p.getIdProducto());
            detalleVenta.setCantidad(cant);
            if (!detalleVenta.insertar()) {
                todoOk = false;
            }
        }

        if (!todoOk) {
            mostrarAlerta("La venta se guardó, pero hubo un problema al guardar algunos productos.");
        }

        descontarIngredientes();

        mostrarAlerta("Venta guardada correctamente.");
        mostrarVentas();
        limpiarVentaActual();
    }

    private double calcularTotalActual() {
        double total = 0;
        for (Productos p : datosDetalle) {
            total += p.getPrecio() * cantidadPorProducto.getOrDefault(p.getIdProducto(), 0);
        }
        return total;
    }

    private void descontarIngredientes() {
        Map<Integer, Double> requeridoPorIngrediente = new HashMap<>();
        for (Productos p : datosDetalle) {
            int cant = cantidadPorProducto.getOrDefault(p.getIdProducto(), 0);
            acumularRequerimiento(requeridoPorIngrediente, p.getIdProducto(), cant);
        }

        for (Map.Entry<Integer, Double> entry : requeridoPorIngrediente.entrySet()) {
            Ingredientes ing = ingredienteModelo.consultaPorId(entry.getKey());
            if (ing == null) {
                continue;
            }
            ing.setStock((int) Math.round(ing.getStock() - entry.getValue()));
            ing.editar();
        }
    }

    // ---------- Cancelar ----------
    @FXML
    private void cancelar(ActionEvent event) {
        limpiarVentaActual();
    }

    private void limpiarVentaActual() {
        datosDetalle.clear();
        cantidadPorProducto.clear();
        txtCantidad.clear();
        txtNombreCliente.clear();
        txtNombreProducto.clear();
        txtTotal.clear();
        cmbTipoPago.setValue(null);
        idClienteSeleccionado = 0;
        idProductoSeleccionadoParaAgregar = 0;
        idProductoEnEdicion = 0;
        modoSoloLectura = false;

        txtNombreCliente.setDisable(false);
        cmbTipoPago.setDisable(false);
        txtCantidad.setDisable(false);
        btnAddCliente.setDisable(false);
        btnAddProducto.setDisable(false);
        btnAgregar.setDisable(false);
        btnEditar.setDisable(false);
        btnEliminar.setDisable(false);
        btnGuardar.setDisable(false);
        btnCancelar.setDisable(false);

        tablaDetalle.refresh();
        actualizarTotal();
    }

    // ---------- Utilidades ----------
    public void abrirFxml(String formulario, String titulo) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(formulario));
        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            System.getLogger(PedidosController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    private void imprimir(ActionEvent event) {
    }
}
