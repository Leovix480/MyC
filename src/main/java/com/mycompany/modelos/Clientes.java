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
        // idCliente es AUTO_INCREMENT en MySQL: no se envía manualmente,
        // se recupera la clave generada (mismo patrón que Recetas.insertar() y Venta.insertar()).
        String sql = "INSERT INTO cliente (nombre, apellido, direccion, telefono) VALUES (?, ?, ?, ?)";
        try (Connection con = getCon();
             PreparedStatement stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stm.setString(1, this.nombre);
            stm.setString(2, this.apellido);
            stm.setString(3, this.direccion);
            stm.setString(4, this.telefono);
            stm.executeUpdate();

            try (ResultSet rs = stm.getGeneratedKeys()) {
                if (rs.next()) {
                    this.idCliente = rs.getInt(1);
                }
            }
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

    /**
     * Vuelve consecutivos los IDs de cliente posteriores al recién eliminado,
     * corriendo cada uno una posición hacia abajo (si se borró el 2, el 3 pasa
     * a ser 2, el 4 pasa a ser 3, etc.). Las filas de venta que referencian a
     * los clientes movidos se actualizan solas gracias a ON UPDATE CASCADE en
     * fk_Venta_Cliente1 (ver migraciones/001_cascade_para_renumeracion.sql).
     * Se ejecuta en una sola transacción: si algo falla no se aplica ningún cambio,
     * y nunca se propaga como error del borrado (es una mejora "best effort").
     */
    public boolean renumerarDespuesDeEliminar(int idEliminado) {
        String sqlSeleccionar = "SELECT idCliente FROM cliente WHERE idCliente > ? ORDER BY idCliente ASC";
        String sqlActualizar = "UPDATE cliente SET idCliente = ? WHERE idCliente = ?";

        try (Connection con = getCon()) {
            con.setAutoCommit(false);
            try {
                ArrayList<Integer> idsAMover = new ArrayList<>();
                try (PreparedStatement stm = con.prepareStatement(sqlSeleccionar)) {
                    stm.setInt(1, idEliminado);
                    try (ResultSet rs = stm.executeQuery()) {
                        while (rs.next()) {
                            idsAMover.add(rs.getInt("idCliente"));
                        }
                    }
                }

                try (PreparedStatement stm = con.prepareStatement(sqlActualizar)) {
                    for (int idActual : idsAMover) {
                        stm.setInt(1, idActual - 1);
                        stm.setInt(2, idActual);
                        stm.executeUpdate();
                    }
                }

                con.commit();
            } catch (SQLException ex) {
                con.rollback();
                System.getLogger(Clientes.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                return false;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            System.getLogger(Clientes.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return false;
        }

        ajustarAutoIncrement();
        return true;
    }

    // Ajuste best-effort del contador AUTO_INCREMENT tras renumerar: si falla no
    // afecta la integridad de los datos, en el peor caso el próximo ID no es el mínimo posible.
    private void ajustarAutoIncrement() {
        String sqlMax = "SELECT MAX(idCliente) AS maximo FROM cliente";
        try (Connection con = getCon();
             Statement stmSelect = con.createStatement();
             ResultSet rs = stmSelect.executeQuery(sqlMax)) {
            int maximo = 0;
            if (rs.next()) {
                maximo = rs.getInt("maximo");
            }
            try (Statement stmAlter = con.createStatement()) {
                stmAlter.executeUpdate("ALTER TABLE cliente AUTO_INCREMENT = " + (maximo + 1));
            }
        } catch (SQLException ex) {
            System.getLogger(Clientes.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}