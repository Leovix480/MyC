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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class Venta extends Conexion implements Sentencias {

    private int idVenta;
    private LocalDateTime fecha;
    private int idCliente;
    private String tipoPago;
    private double totalVenta;

    public Venta() {
    }

    public Venta(int idVenta, LocalDateTime fecha, int idCliente, String tipoPago, double totalVenta) {
        this.idVenta = idVenta;
        this.fecha = fecha;
        this.idCliente = idCliente;
        this.tipoPago = tipoPago;
        this.totalVenta = totalVenta;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public double getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(double totalVenta) {
        this.totalVenta = totalVenta;
    }

    @Override
    public boolean insertar() {
        String sql = "INSERT INTO venta (fecha, idCliente, tipoPago, totalVenta) VALUES (?, ?, ?, ?)";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stm.setTimestamp(1, Timestamp.valueOf(this.fecha));
            stm.setInt(2, this.idCliente);
            stm.setString(3, this.tipoPago);
            stm.setDouble(4, this.totalVenta);
            stm.executeUpdate();

            try (ResultSet rs = stm.getGeneratedKeys()) {
                if (rs.next()) {
                    this.idVenta = rs.getInt(1);
                }
            }
            return true;
        } catch (SQLException ex) {
            System.getLogger(Venta.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return false;
        }
    }

    @Override
    public boolean editar() {
        String sql = "UPDATE venta SET fecha=?, idCliente=?, tipoPago=?, totalVenta=? WHERE idVenta=?";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setTimestamp(1, Timestamp.valueOf(this.fecha));
            stm.setInt(2, this.idCliente);
            stm.setString(3, this.tipoPago);
            stm.setDouble(4, this.totalVenta);
            stm.setInt(5, this.idVenta);
            int filas = stm.executeUpdate();
            return filas > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar() {
        String sql = "DELETE FROM venta WHERE idVenta=?";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, this.idVenta);
            int filas = stm.executeUpdate();
            return filas > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public ArrayList<Venta> consulta() {
        ArrayList<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM venta";
        try (Connection con = getCon(); Statement stm = con.createStatement(); ResultSet rs = stm.executeQuery(sql)) {
            while (rs.next()) {
                int idV = rs.getInt("idVenta");
                LocalDateTime fec = rs.getTimestamp("fecha").toLocalDateTime();
                int idCli = rs.getInt("idCliente");
                String tp = rs.getString("tipoPago");
                double total = rs.getDouble("totalVenta");
                Venta venta = new Venta(idV, fec, idCli, tp, total);
                ventas.add(venta);
            }
        } catch (SQLException ex) {
            System.getLogger(Venta.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return ventas;
    }

    // Método extra: traer solo las ventas de un cliente específico
    public ArrayList<Venta> consultaPorCliente(int idCliente) {
        ArrayList<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM venta WHERE idCliente=?";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, idCliente);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    int idV = rs.getInt("idVenta");
                    LocalDateTime fec = rs.getTimestamp("fecha").toLocalDateTime();
                    int idCli = rs.getInt("idCliente");
                    String tp = rs.getString("tipoPago");
                    double total = rs.getDouble("totalVenta");
                    ventas.add(new Venta(idV, fec, idCli, tp, total));
                }
            }
        } catch (SQLException ex) {
            System.getLogger(Venta.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return ventas;
    }
}
