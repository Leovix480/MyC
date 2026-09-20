/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.modelos;

import com.mycompany.myc.clases.Conexion;
import com.mycompany.myc.clases.Sentencias;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author valin
 */
public class Productos extends Conexion implements Sentencias {

    private int idProducto;
    private String nombre;
    private double precio;
    private int idRecetas;

    public Productos() {
    }

    public Productos(int idProducto, String nombre, double precio, int idRecetas) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.idRecetas = idRecetas;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getIdRecetas() {
        return idRecetas;
    }

    public void setIdRecetas(int idRecetas) {
        this.idRecetas = idRecetas;
    }

    @Override
    public boolean insertar() {
        // idProducto es AUTO_INCREMENT en MySQL: se recupera la clave generada
        // (mismo patrón que Clientes.insertar(), Recetas.insertar() y Venta.insertar()).
        String sql = "INSERT INTO producto (nombre, precio, idRecetas) VALUES (?, ?, ?)";
        try (Connection con = getCon();
             PreparedStatement stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stm.setString(1, this.nombre);
            stm.setDouble(2, this.precio);
            stm.setInt(3, this.idRecetas);
            stm.executeUpdate();

            try (ResultSet rs = stm.getGeneratedKeys()) {
                if (rs.next()) {
                    this.idProducto = rs.getInt(1);
                }
            }
            return true;
        } catch (SQLException ex) {
            System.getLogger(Productos.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return false;
        }
    }

    @Override
    public boolean editar() {
        String sql = "UPDATE producto SET nombre=?, precio=?, idRecetas=? WHERE idProducto=?";
        try (Connection con = getCon();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, this.nombre);
            stm.setDouble(2, this.precio);
            stm.setInt(3, this.idRecetas);
            stm.setInt(4, this.idProducto);
            int filas = stm.executeUpdate();
            return filas > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar() {
        String sql = "DELETE FROM producto WHERE idProducto=?";
        try (Connection con = getCon();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, this.idProducto);
            int filas = stm.executeUpdate();
            return filas > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public ArrayList<Productos> consulta() {
        ArrayList<Productos> productos = new ArrayList<>();
        String sql = "SELECT * FROM producto";
        try (Connection con = getCon();
             Statement stm = con.createStatement();
             ResultSet rs = stm.executeQuery(sql)) {
            while (rs.next()) {
                int idProd = rs.getInt("idProducto");
                String nom = rs.getString("nombre");
                double prec = rs.getDouble("precio");
                int idRec = rs.getInt("idRecetas");
                Productos producto = new Productos(idProd, nom, prec, idRec);
                productos.add(producto);
            }
        } catch (SQLException ex) {
            System.getLogger(Productos.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return productos;
    }

    /**
     * Vuelve consecutivos los IDs de producto posteriores al recién eliminado
     * (si se borró el 2, el 3 pasa a ser 2, el 4 pasa a ser 3, etc.). Las filas de
     * detalle_venta.idProducto que referencian a los productos movidos se
     * actualizan solas gracias a ON UPDATE CASCADE en fk_Producto_has_Venta_Producto1
     * (ver migraciones/001_cascade_para_renumeracion.sql). No toca idRecetas de
     * ninguna fila. Transaccional: si algo falla no se aplica ningún cambio, y
     * nunca se propaga como error del borrado.
     */
    public boolean renumerarDespuesDeEliminar(int idEliminado) {
        String sqlSeleccionar = "SELECT idProducto FROM producto WHERE idProducto > ? ORDER BY idProducto ASC";
        String sqlActualizar = "UPDATE producto SET idProducto = ? WHERE idProducto = ?";

        try (Connection con = getCon()) {
            con.setAutoCommit(false);
            try {
                ArrayList<Integer> idsAMover = new ArrayList<>();
                try (PreparedStatement stm = con.prepareStatement(sqlSeleccionar)) {
                    stm.setInt(1, idEliminado);
                    try (ResultSet rs = stm.executeQuery()) {
                        while (rs.next()) {
                            idsAMover.add(rs.getInt("idProducto"));
                        }
                    }
                }

                // Se compacta a partir de idEliminado (no simplemente "idActual - 1"), para que también
                // se cierren los huecos que ya existieran por encima del id borrado.
                try (PreparedStatement stm = con.prepareStatement(sqlActualizar)) {
                    int nuevoId = idEliminado;
                    for (int idActual : idsAMover) {
                        stm.setInt(1, nuevoId);
                        stm.setInt(2, idActual);
                        stm.executeUpdate();
                        nuevoId++;
                    }
                }

                con.commit();
            } catch (SQLException ex) {
                con.rollback();
                System.getLogger(Productos.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                return false;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            System.getLogger(Productos.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return false;
        }

        ajustarAutoIncrement();
        return true;
    }

    // Ajuste best-effort del contador AUTO_INCREMENT tras renumerar: si falla no
    // afecta la integridad de los datos, en el peor caso el próximo ID no es el mínimo posible.
    private void ajustarAutoIncrement() {
        String sqlMax = "SELECT MAX(idProducto) AS maximo FROM producto";
        try (Connection con = getCon();
             Statement stmSelect = con.createStatement();
             ResultSet rs = stmSelect.executeQuery(sqlMax)) {
            int maximo = 0;
            if (rs.next()) {
                maximo = rs.getInt("maximo");
            }
            try (Statement stmAlter = con.createStatement()) {
                stmAlter.executeUpdate("ALTER TABLE producto AUTO_INCREMENT = " + (maximo + 1));
            }
        } catch (SQLException ex) {
            System.getLogger(Productos.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public Productos consultaPorReceta(int idRecetas) {
    String sql = "SELECT * FROM producto WHERE idRecetas=?";
    try (Connection con = getCon();
         PreparedStatement stm = con.prepareStatement(sql)) {
        stm.setInt(1, idRecetas);
        try (ResultSet rs = stm.executeQuery()) {
            if (rs.next()) {
                int idProd = rs.getInt("idProducto");
                String nom = rs.getString("nombre");
                double prec = rs.getDouble("precio");
                int idRec = rs.getInt("idRecetas");
                return new Productos(idProd, nom, prec, idRec);
            }
        }
    } catch (SQLException ex) {
        System.getLogger(Productos.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
    }
    return null;
    }
    public Productos consultaPorId(int idProducto) {
        String sql = "SELECT * FROM producto WHERE idProducto=?";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, idProducto);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    int idProd = rs.getInt("idProducto");
                    String nom = rs.getString("nombre");
                    double prec = rs.getDouble("precio");
                    int idRec = rs.getInt("idRecetas");
                    return new Productos(idProd, nom, prec, idRec);
                }
            }
        } catch (SQLException ex) {
            System.getLogger(Productos.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return null;
    }
}