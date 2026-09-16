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
import javafx.scene.control.Label;
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
    private Button btnFactura;
    @FXML
    private TextField txtTotal;
    @FXML
    private Label lblTotalIva;
    @FXML
    private ComboBox<String> cmbTipoPago;

    // IVA que se agrega sobre el precio de cada producto (solo informativo en la vista; se aplica en la factura)
    private static final double IVA = 0.10;

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
    int idVentaSeleccionada;
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
        btnFactura.setCursor(Cursor.HAND);
        
        deshabilitar();
    }

    public void habilitar() {
        btnGuardar.setDisable(false);
        btnCancelar.setDisable(false);
        btnEditar.setDisable(false);
        btnEliminar.setDisable(false);
        btnAddCliente.setDisable(false);
        btnAddProducto.setDisable(false);
        btnAgregar.setDisable(false);
        txtBuscarCliente.setDisable(false);
        txtCantidad.setDisable(false);
        dpFecha.setDisable(false);
        cmbTipoPago.setDisable(false);
    }
    
    public void deshabilitar() {
        btnGuardar.setDisable(true);
        btnCancelar.setDisable(true);
        btnEditar.setDisable(true);
        btnEliminar.setDisable(true);
        btnAddCliente.setDisable(true);
        btnAddProducto.setDisable(true);
        btnAgregar.setDisable(true);
        txtBuscarCliente.setDisable(true);
        txtCantidad.setDisable(true);
        dpFecha.setDisable(true);
        dpFecha.setValue(null);
        cmbTipoPago.setDisable(true);
        cmbTipoPago.setValue(null);
        txtNombreCliente.clear();
        datosDetalle.clear();
        txtTotal.setText("");
        lblTotalIva.setText("c/IVA 10%: ");
    }
    
    //  Tabla superior (ventas realizadas) 
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
        idVentaSeleccionada = v.getIdVenta();

        Clientes c = buscarClientePorId(v.getIdCliente());
        if (c != null) {
            txtNombreCliente.setText(c.getNombre() + " " + c.getApellido());
            idClienteSeleccionado = c.getIdCliente();
            dpFecha.setValue(v.getFecha().toLocalDate());
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
        
        btnEditar.setDisable(false);
        btnEliminar.setDisable(false);
        btnCancelar.setDisable(false);
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

    //  Botón "Añadir" del panel inferior: vuelve a un pedido nuevo 
    @FXML
    private void add(ActionEvent event) {
        limpiarVentaActual();
        habilitar();
    }

    //  Agregar cliente 
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

    //  Agregar producto 
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

    // Tabla de detalle
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
            eliminarVentaSeleccionada();
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
        deshabilitar();
    }

    private void eliminarVentaSeleccionada() {
        if (idVentaSeleccionada <= 0) {
            mostrarAlerta("Seleccioná una venta de la tabla para eliminar.");
            return;
        }

        restituirIngredientes(idVentaSeleccionada); 

        if (!detalleVenta.eliminarPorVenta(idVentaSeleccionada)) {
            mostrarAlerta("No se pudo eliminar el detalle de la venta.");
            return;
        }

        venta.setIdVenta(idVentaSeleccionada);
        if (venta.eliminar()) {
            mostrarAlerta("Venta eliminada correctamente.");
            mostrarVentas();
            limpiarVentaActual();
        } else {
            mostrarAlerta("No se pudo eliminar la venta.");
        }
    }

    private void actualizarTotal() {
        double total = 0;
        for (Productos p : datosDetalle) {
            total += p.getPrecio() * cantidadPorProducto.getOrDefault(p.getIdProducto(), 0);
        }
        txtTotal.setText(String.valueOf(total));
        lblTotalIva.setText("c/IVA 10%: " + String.valueOf(redondear2(total + calcularIvaActual())));
    }

    // IVA de un importe, redondeado a 2 decimales
    private double calcularIva(double importe) {
        return redondear2(importe * IVA);
    }

    // Suma del IVA calculado producto por producto (mismo criterio que usa la factura)
    private double calcularIvaActual() {
        double iva = 0;
        for (Productos p : datosDetalle) {
            int cant = cantidadPorProducto.getOrDefault(p.getIdProducto(), 0);
            iva += calcularIva(p.getPrecio() * cant);
        }
        return redondear2(iva);
    }

    private double redondear2(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    //  Validación de stock 
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

    //  Guardar venta 
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
        if (dpFecha.getValue() == null) {
            mostrarAlerta("Seleccione una fecha.");
            return;
        }
        
        String tipoPago = cmbTipoPago.getValue();
        if (tipoPago == null || tipoPago.isEmpty()) {
            mostrarAlerta("Seleccioná un método de pago.");
            return;
        }
        
        venta.setFecha(dpFecha.getValue().atStartOfDay());
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
        deshabilitar();
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

    // Cancelar 
    @FXML
    private void cancelar(ActionEvent event) {
        deshabilitar();
    }

    private void limpiarVentaActual() {
        cantidadPorProducto.clear();
        idClienteSeleccionado = 0;
        idProductoSeleccionadoParaAgregar = 0;
        idProductoEnEdicion = 0;
        idVentaSeleccionada = 0; 
        modoSoloLectura = false;

        tablaDetalle.refresh();
        actualizarTotal();
    }

    // Utilidades
    public void abrirFxml(String formulario, String titulo) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(formulario));
        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            // La ventana puede agrandarse, pero nunca achicarse por debajo del tamaño inicial de la vista
            stage.setOnShown(e -> {
                stage.setMinWidth(stage.getWidth());
                stage.setMinHeight(stage.getHeight());
            });
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
    
    private void restituirIngredientes(int idVenta) {
        ArrayList<DetalleVenta> detalles = detalleVenta.consultaPorVenta(idVenta);
        Map<Integer, Double> requeridoPorIngrediente = new HashMap<>();

        for (DetalleVenta d : detalles) {
            acumularRequerimiento(requeridoPorIngrediente, d.getIdProducto(), d.getCantidad());
        }

        for (Map.Entry<Integer, Double> entry : requeridoPorIngrediente.entrySet()) {
            Ingredientes ing = ingredienteModelo.consultaPorId(entry.getKey());
            if (ing == null) {
                continue;
            }
            ing.setStock((int) Math.round(ing.getStock() + entry.getValue()));
            ing.editar();
        }
    }

    @FXML
    private void imprimir(ActionEvent event) {

        String rutaReporte = "/reportes/reportePedidos.jasper";

        try (java.io.InputStream streamReporte = getClass().getResourceAsStream(rutaReporte)) {

            if (streamReporte == null) {
                throw new java.io.FileNotFoundException("No se encontró el archivo en: " + rutaReporte);
            }

            // 2. Establecer la conexión física a tu Base de Datos SQL (Reemplaza con tus credenciales)
            java.sql.Connection conexion = java.sql.DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/myc", "root", "");

            // Mapa de parámetros vacío porque imprime todos los pedidos
            Map<String, Object> parametros = new HashMap<>();

            // 3. Llenar el reporte
            net.sf.jasperreports.engine.JasperPrint jasperPrint = net.sf.jasperreports.engine.JasperFillManager.fillReport(streamReporte, parametros, conexion);

            // 4. Abrir el visor en pantalla
            net.sf.jasperreports.view.JasperViewer visor = new net.sf.jasperreports.view.JasperViewer(jasperPrint, false);
            visor.setTitle("Reporte General de Pedidos");
            visor.setVisible(true);

            conexion.close();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Reporte");
            alert.setHeaderText("No se pudo cargar el reporte de los pedidos");
            alert.setContentText("Detalle: " + e.getMessage());
            alert.showAndWait();
        }
    }

    //  Factura del pedido actual (datos tomados de la vista) 
    @FXML
    private void factura(ActionEvent event) {

        if (idClienteSeleccionado <= 0 || txtNombreCliente.getText().trim().isEmpty()) {
            mostrarAlerta("Seleccioná un cliente para generar la factura.");
            return;
        }
        if (datosDetalle.isEmpty()) {
            mostrarAlerta("Agregá al menos un producto para generar la factura.");
            return;
        }
        if (dpFecha.getValue() == null) {
            mostrarAlerta("Seleccione una fecha para generar la factura.");
            return;
        }

        String rutaReporte = "/reportes/facturaMyC.jrxml";

        try (java.io.InputStream streamReporte = getClass().getResourceAsStream(rutaReporte)) {

            if (streamReporte == null) {
                throw new java.io.FileNotFoundException("No se encontró el archivo en: " + rutaReporte);
            }

            // 1. Filas del detalle: mismos datos que muestra tablaDetalle + IVA 10% por producto
            java.util.List<Map<String, ?>> filas = new ArrayList<>();
            for (Productos p : datosDetalle) {
                int cant = cantidadPorProducto.getOrDefault(p.getIdProducto(), 0);
                double subtotal = p.getPrecio() * cant;
                double iva = calcularIva(subtotal);
                Map<String, Object> fila = new HashMap<>();
                fila.put("nombre", p.getNombre());
                fila.put("cantidad", cant);
                fila.put("precio", p.getPrecio());
                fila.put("subtotal", subtotal);
                fila.put("iva", iva);
                fila.put("totalConIva", redondear2(subtotal + iva));
                filas.add(fila);
            }
            net.sf.jasperreports.engine.data.JRMapCollectionDataSource datos
                    = new net.sf.jasperreports.engine.data.JRMapCollectionDataSource(filas);

            // 2. Parámetros de cabecera tomados de la vista
            Clientes c = buscarClientePorId(idClienteSeleccionado);
            String tipoPago = cmbTipoPago.getValue();

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("cliente", txtNombreCliente.getText());
            parametros.put("direccion", c != null && c.getDireccion() != null ? c.getDireccion() : "");
            parametros.put("telefono", c != null && c.getTelefono() != null ? c.getTelefono() : "");
            parametros.put("fecha", dpFecha.getValue().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            parametros.put("numeroPedido", idVentaSeleccionada > 0 ? idVentaSeleccionada : null); // en blanco si no se guardó
            parametros.put("tipoPago", tipoPago != null && !tipoPago.isEmpty() ? tipoPago : "-");
            double total = calcularTotalActual();      // mismo cálculo que txtTotal (sin IVA)
            double totalIva = calcularIvaActual();     // suma del IVA de cada producto
            parametros.put("total", total);
            parametros.put("totalIva", totalIva);
            parametros.put("totalConIva", redondear2(total + totalIva)); // mismo valor que lblTotalIva

            // 3. Compilar la plantilla y llenar el reporte con los datos de la vista
            net.sf.jasperreports.engine.JasperReport reporte
                    = net.sf.jasperreports.engine.JasperCompileManager.compileReport(streamReporte);
            net.sf.jasperreports.engine.JasperPrint jasperPrint
                    = net.sf.jasperreports.engine.JasperFillManager.fillReport(reporte, parametros, datos);

            // 4. Abrir el visor en pantalla
            net.sf.jasperreports.view.JasperViewer visor = new net.sf.jasperreports.view.JasperViewer(jasperPrint, false);
            visor.setTitle("Factura MyC");
            visor.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Factura");
            alert.setHeaderText("No se pudo generar la factura");
            alert.setContentText("Detalle: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
