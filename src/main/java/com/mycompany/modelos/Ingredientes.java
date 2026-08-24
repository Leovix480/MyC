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
public class Ingredientes extends Conexion implements Sentencias{
    private int idIngredientes;
    private String nombre;
    private double precio;
    private int stock;
    private int stockMin;

    public Ingredientes(int idIngredientes, String nombre, double precio, int stock, int stockMin) {
        this.idIngredientes = idIngredientes;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.stockMin = stockMin;
    }
    
    public Ingredientes(){
    }
    
        

    @Override
    public boolean insertar() {
        String sql = "INSERT INTO ingredientes (nombre, precioIngredientes, stockIngredientes, stockMinimo) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stm = getCon().prepareStatement(sql);) {
            stm.setString(1, this.nombre);
            stm.setDouble(2, this.precio);
            stm.setInt(3, this.stock);
            stm.setInt(4, this.stockMin);
            stm.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.getLogger(Ingredientes.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return false;
        }
    }

    @Override
    public boolean editar() {
        String sql = "UPDATE ingredientes SET nombre=?, precioIngredientes=?, stockIngredientes=?, stockMinimo=? WHERE idIngredientes=?";

        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setString(1, this.nombre);
            stm.setDouble(2, this.precio);
            stm.setInt(3, this.stock);
            stm.setInt(4, this.stockMin);
            stm.setInt(5, this.idIngredientes);

            int filas = stm.executeUpdate();

            System.out.println("Filas modificadas: " + filas);

            return filas > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar() {
        String sql = "DELETE FROM ingredientes WHERE idIngredientes = ?";

        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setInt(1, this.idIngredientes);

            int filas = stm.executeUpdate();

            return filas > 0;
        } catch (SQLException ex) {
            System.getLogger(Ingredientes.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return false;
        }
    }
    
    @Override
    public ArrayList consulta() {
        ArrayList<Ingredientes> ingrediente = new ArrayList<>();
        String sql = "select * from ingredientes";
        try (
                Connection con = getCon(); Statement stm = con.createStatement(); ResultSet rs = stm.executeQuery(sql)) {
            while (rs.next()) {
                int cod = rs.getInt("idIngredientes");
                String nom = rs.getString("nombre");
                double pre = rs.getDouble("precioIngredientes");
                int si = rs.getInt("stockIngredientes");
                int sim = rs.getInt("stockMinimo");
                Ingredientes ingre = new Ingredientes(cod, nom, pre, si, sim);
                ingrediente.add(ingre);
            }
        } catch (SQLException ex) {
            System.getLogger(Ingredientes.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return ingrediente;
    }
    
    public Ingredientes consultaPorId(int idIngredientes) {
        String sql = "SELECT * FROM ingredientes WHERE idIngredientes=?";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, idIngredientes);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    int cod = rs.getInt("idIngredientes");
                    String nom = rs.getString("nombre");
                    double pre = rs.getDouble("precioIngredientes");
                    int si = rs.getInt("stockIngredientes");
                    int sim = rs.getInt("stockMinimo");
                    return new Ingredientes(cod, nom, pre, si, sim);
                }
            }
        } catch (SQLException ex) {
            System.getLogger(Ingredientes.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return null;
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

    public int getIdIngredientes() {
        return idIngredientes;
    }

    public void setIdIngredientes(int idIngredientes) {
        this.idIngredientes = idIngredientes;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getStockMin() {
        return stockMin;
    }

    public void setStockMin(int stockMin) {
        this.stockMin = stockMin;
    }
    
}