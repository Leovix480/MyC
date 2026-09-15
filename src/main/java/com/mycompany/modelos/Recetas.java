package com.mycompany.modelos;

import com.mycompany.myc.clases.Conexion;
import com.mycompany.myc.clases.Sentencias;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Recetas extends Conexion implements Sentencias {

    private int idRecetas;
    private String nombre;
    private String descripcion;

    public Recetas() {
    }

    public Recetas(int idRecetas, String nombre, String descripcion) {
        this.idRecetas = idRecetas;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getIdRecetas() {
        return idRecetas;
    }

    public void setIdRecetas(int idRecetas) {
        this.idRecetas = idRecetas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean insertar() {
        String sql = "INSERT INTO recetas (nombre, descripcion) VALUES (?, ?)";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stm.setString(1, this.nombre);
            stm.setString(2, this.descripcion);
            stm.executeUpdate();

            try (ResultSet rs = stm.getGeneratedKeys()) {
                if (rs.next()) {
                    this.idRecetas = rs.getInt(1);
                }
            }
            return true;
        } catch (SQLException ex) {
            System.getLogger(Recetas.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return false;
        }
    }

    @Override
    public boolean editar() {

        String sql = "UPDATE recetas SET nombre=?, descripcion=? WHERE idRecetas=?";

        try (Connection con = getCon();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, this.nombre);
            stm.setString(2, this.descripcion);
            stm.setInt(3, this.idRecetas);
            int filas = stm.executeUpdate();
            return filas > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar() {

        String sql = "DELETE FROM recetas WHERE idRecetas=?";

        try (Connection con = getCon();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, this.idRecetas);
            int filas = stm.executeUpdate();
            return filas > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public ArrayList<Recetas> consulta() {

        ArrayList<Recetas> recetas = new ArrayList<>();

        String sql = "SELECT * FROM recetas";

        try (Connection con = getCon();
             Statement stm = con.createStatement();
             ResultSet rs = stm.executeQuery(sql)) {
            while (rs.next()) {
                int idRec = rs.getInt("idRecetas");
                String nom = rs.getString("nombre");
                String des = rs.getString("descripcion");
                Recetas receta = new Recetas(idRec, nom, des);
                recetas.add(receta);
            }
        } catch (SQLException ex) {
            System.getLogger(Recetas.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return recetas;
    }

    /**
     * Vuelve consecutivos los IDs de receta posteriores al recién eliminado
     * (si se borró el 2, el 3 pasa a ser 2, el 4 pasa a ser 3, etc.). Las filas de
     * producto.idRecetas y detalle_receta.idRecetas que referencian a las recetas
     * movidas se actualizan solas gracias a ON UPDATE CASCADE en fk_Producto_Recetas1
     * y fk_Ingredientes_has_Recetas_Recetas1 (ver migraciones/001_cascade_para_renumeracion.sql).
     * Transaccional: si algo falla no se aplica ningún cambio, y nunca se propaga
     * como error del borrado.
     */
    public boolean renumerarDespuesDeEliminar(int idEliminado) {
        String sqlSeleccionar = "SELECT idRecetas FROM recetas WHERE idRecetas > ? ORDER BY idRecetas ASC";
        String sqlActualizar = "UPDATE recetas SET idRecetas = ? WHERE idRecetas = ?";

        try (Connection con = getCon()) {
            con.setAutoCommit(false);
            try {
                ArrayList<Integer> idsAMover = new ArrayList<>();
                try (PreparedStatement stm = con.prepareStatement(sqlSeleccionar)) {
                    stm.setInt(1, idEliminado);
                    try (ResultSet rs = stm.executeQuery()) {
                        while (rs.next()) {
                            idsAMover.add(rs.getInt("idRecetas"));
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
                System.getLogger(Recetas.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                return false;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            System.getLogger(Recetas.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return false;
        }

        ajustarAutoIncrement();
        return true;
    }

    // Ajuste best-effort del contador AUTO_INCREMENT tras renumerar: si falla no
    // afecta la integridad de los datos, en el peor caso el próximo ID no es el mínimo posible.
    private void ajustarAutoIncrement() {
        String sqlMax = "SELECT MAX(idRecetas) AS maximo FROM recetas";
        try (Connection con = getCon();
             Statement stmSelect = con.createStatement();
             ResultSet rs = stmSelect.executeQuery(sqlMax)) {
            int maximo = 0;
            if (rs.next()) {
                maximo = rs.getInt("maximo");
            }
            try (Statement stmAlter = con.createStatement()) {
                stmAlter.executeUpdate("ALTER TABLE recetas AUTO_INCREMENT = " + (maximo + 1));
            }
        } catch (SQLException ex) {
            System.getLogger(Recetas.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}