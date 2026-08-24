package com.mycompany.modelos;

import com.mycompany.myc.clases.Conexion;
import com.mycompany.myc.clases.Sentencias;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Clientes extends Conexion implements Sentencias{
    private String nombre;
    private String apellido;
    private int idCliente;
    private String direccion;
    private String telefono;

    public Clientes() {
    }

    public Clientes( int idCliente, String nombre, String apellido, String direccion, String telefono) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }   
    
    @Override
    public boolean insertar() {
        String sql = "INSERT INTO cliente (idCliente, nombre, apellido, direccion, telefono) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stm = getCon().prepareStatement(sql);) {
            stm.setInt(1, this.idCliente);
            stm.setString(2, this.nombre);
            stm.setString(3, this.apellido);
            stm.setString(4, this.direccion);
            stm.setString(5, this.telefono);
            stm.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.getLogger(Clientes.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return false;
        }
    }

    @Override
    public boolean editar() {
        String sql = "UPDATE cliente SET nombre=?, apellido=?, direccion=?, telefono=? WHERE idCliente=?";

        try (Connection con = getCon();
             PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setString(1, this.nombre);
            stm.setString(2, this.apellido);
            stm.setString(3, this.direccion);
            stm.setString(4, this.telefono);
            stm.setInt(5, this.idCliente);

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
        String sql = "DELETE FROM cliente WHERE idCliente = ?";

        try (Connection con = getCon();
             PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setInt(1, this.idCliente);

            int filas = stm.executeUpdate();

            return filas > 0;
        } catch (SQLException ex) {
            System.getLogger(Clientes.class.getName()).log(System.Logger.Level.ERROR, (String)null, ex);
            return false;
        }
    }

    @Override
    public ArrayList<Clientes> consulta() {
        ArrayList<Clientes> cliente = new ArrayList<>();
        String sql = "select * from cliente";
        try (
                Connection con = getCon(); Statement stm = con.createStatement(); ResultSet rs = stm.executeQuery(sql)) {
            while (rs.next()) {
                int cod = rs.getInt("idCliente");
                String nom = rs.getString("nombre");
                String ape = rs.getString("apellido");
                String dir = rs.getString("direccion");
                String tel = rs.getString("telefono");
                Clientes clie = new Clientes(cod, nom, ape, dir, tel);
                cliente.add(clie);
            }
        } catch (SQLException ex) {
            System.getLogger(Clientes.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return cliente;
    }
}