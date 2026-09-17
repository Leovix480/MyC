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
    private String ruc;
    private String direccion;
    private String telefono;

    public Clientes() {
    }

    public Clientes( String ruc, String nombre, String apellido, String direccion, String telefono) {
        this.ruc = ruc;
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

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
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
        // El RUC lo carga el usuario en el formulario: es la clave primaria y no se genera sola.
        String sql = "INSERT INTO cliente (ruc, nombre, apellido, direccion, telefono) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = getCon();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, this.ruc);
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
        return editar(this.ruc);
    }

    /**
     * Permite corregir también el RUC: rucOriginal identifica la fila a modificar
     * y this.ruc es el valor nuevo. Las ventas del cliente siguen el cambio gracias
     * al ON UPDATE CASCADE de fk_Venta_Cliente1 (ver migraciones/002_ruc_como_clave_cliente.sql).
     */
    public boolean editar(String rucOriginal) {
        String sql = "UPDATE cliente SET ruc=?, nombre=?, apellido=?, direccion=?, telefono=? WHERE ruc=?";

        try (Connection con = getCon();
             PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setString(1, this.ruc);
            stm.setString(2, this.nombre);
            stm.setString(3, this.apellido);
            stm.setString(4, this.direccion);
            stm.setString(5, this.telefono);
            stm.setString(6, rucOriginal);

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
        String sql = "DELETE FROM cliente WHERE ruc = ?";

        try (Connection con = getCon();
             PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setString(1, this.ruc);

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
                String cod = rs.getString("ruc");
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

    public boolean existeRuc(String ruc) {
        String sql = "SELECT 1 FROM cliente WHERE ruc = ?";
        try (Connection con = getCon();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, ruc);
            try (ResultSet rs = stm.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            System.getLogger(Clientes.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return false;
        }
    }

}