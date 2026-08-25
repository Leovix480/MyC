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

public class DetalleVenta extends Conexion implements Sentencias {

    private int idVenta;
    private int idProducto;
    private int cantidad;

    public DetalleVenta() {
    }

    public DetalleVenta(int idVenta, int idProducto, int cantidad) {
        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public boolean insertar() {
        String sql = "INSERT INTO detalle_venta (idVenta, idProducto, cantidad) VALUES (?, ?, ?)";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, this.idVenta);
            stm.setInt(2, this.idProducto);
            stm.setInt(3, this.cantidad);
            stm.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.getLogger(DetalleVenta.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return false;
        }
    }

    @Override
    public boolean editar() {
        String sql = "UPDATE detalle_venta SET cantidad=? WHERE idVenta=? AND idProducto=?";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, this.cantidad);
            stm.setInt(2, this.idVenta);
            stm.setInt(3, this.idProducto);
            int filas = stm.executeUpdate();
            return filas > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar() {
        String sql = "DELETE FROM detalle_venta WHERE idVenta=? AND idProducto=?";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, this.idVenta);
            stm.setInt(2, this.idProducto);
            int filas = stm.executeUpdate();
            return filas > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public ArrayList<DetalleVenta> consulta() {
        ArrayList<DetalleVenta> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalle_venta";
        try (Connection con = getCon(); Statement stm = con.createStatement(); ResultSet rs = stm.executeQuery(sql)) {
            while (rs.next()) {
                int idV = rs.getInt("idVenta");
                int idProd = rs.getInt("idProducto");
                int cant = rs.getInt("cantidad");
                detalles.add(new DetalleVenta(idV, idProd, cant));
            }
        } catch (SQLException ex) {
            System.getLogger(DetalleVenta.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return detalles;
    }

    // Método extra: traer solo los productos vendidos en una venta específica
    public ArrayList<DetalleVenta> consultaPorVenta(int idVenta) {
        ArrayList<DetalleVenta> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalle_venta WHERE idVenta=?";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, idVenta);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    int idV = rs.getInt("idVenta");
                    int idProd = rs.getInt("idProducto");
                    int cant = rs.getInt("cantidad");
                    detalles.add(new DetalleVenta(idV, idProd, cant));
                }
            }
        } catch (SQLException ex) {
            System.getLogger(DetalleVenta.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return detalles;
    }
    
    public boolean eliminarPorVenta(int idVenta) {
    String sql = "DELETE FROM detalle_venta WHERE idVenta=?";
    try (Connection con = getCon();
         PreparedStatement stm = con.prepareStatement(sql)) {
        stm.setInt(1, idVenta);
        stm.executeUpdate(); // puede eliminar 0 o más filas, no es un error si da 0
        return true;
    } catch (SQLException ex) {
        ex.printStackTrace();
        return false;
    }
}
}
            