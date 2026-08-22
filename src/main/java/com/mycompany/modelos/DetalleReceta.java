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
public class DetalleReceta extends Conexion implements Sentencias {

    private int idReceta;
    private int idIngrediente;
    private double cantUso;

    public DetalleReceta() {
    }

    public DetalleReceta(int idReceta, int idIngrediente, double cantUso) {
        this.idReceta = idReceta;
        this.idIngrediente = idIngrediente;
        this.cantUso = cantUso;
    }

    public int getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(int idReceta) {
        this.idReceta = idReceta;
    }

    public int getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(int idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public double getCantUso() {
        return cantUso;
    }

    public void setCantUso(double cantUso) {
        this.cantUso = cantUso;
    }

    @Override
    public boolean insertar() {
        String sql = "INSERT INTO detalle_receta (idRecetas, idIngrediente, cantUso) VALUES (?, ?, ?)";
        try (Connection con = getCon();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, this.idReceta);
            stm.setInt(2, this.idIngrediente);
            stm.setDouble(3, this.cantUso);
            stm.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.getLogger(DetalleReceta.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return false;
        }
    }

    @Override
    public boolean editar() {
        String sql = "UPDATE detalle_receta SET cantUso=? WHERE idRecetas=? AND idIngrediente=?";
        try (Connection con = getCon();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setDouble(1, this.cantUso);
            stm.setInt(2, this.idReceta);
            stm.setInt(3, this.idIngrediente);
            int filas = stm.executeUpdate();
            return filas > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar() {
        String sql = "DELETE FROM detalle_receta WHERE idRecetas=? AND idIngrediente=?";
        try (Connection con = getCon();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, this.idReceta);
            stm.setInt(2, this.idIngrediente);
            int filas = stm.executeUpdate();
            return filas > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public ArrayList<DetalleReceta> consulta() {
        ArrayList<DetalleReceta> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalle_receta";
        try (Connection con = getCon();
             Statement stm = con.createStatement();
             ResultSet rs = stm.executeQuery(sql)) {
            while (rs.next()) {
                int idRec = rs.getInt("idRecetas");
                int idIng = rs.getInt("idIngrediente");
                double cant = rs.getDouble("cantUso");
                DetalleReceta detalle = new DetalleReceta(idRec, idIng, cant);
                detalles.add(detalle);
            }
        } catch (SQLException ex) {
            System.getLogger(DetalleReceta.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return detalles;
    }

    // Método extra: traer solo los ingredientes de una receta específica
    public ArrayList<DetalleReceta> consultaPorReceta(int idReceta) {
        ArrayList<DetalleReceta> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalle_receta WHERE idRecetas=?";
        try (Connection con = getCon();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, idReceta);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    int idRec = rs.getInt("idRecetas");
                    int idIng = rs.getInt("idIngrediente");
                    double cant = rs.getDouble("cantUso");
                    detalles.add(new DetalleReceta(idRec, idIng, cant));
                }
            }
        } catch (SQLException ex) {
            System.getLogger(DetalleReceta.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return detalles;
    }
}