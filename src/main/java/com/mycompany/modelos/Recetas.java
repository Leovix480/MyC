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
}