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
        String sql = "INSERT INTO producto (nombre, precio, idRecetas) VALUES (?, ?, ?)";
        try (Connection con = getCon();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, this.nombre);
            stm.setDouble(2, this.precio);
            stm.setInt(3, this.idRecetas);
            stm.executeUpdate();
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
}