package com.mycompany.myc.clases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private String servidor;
    private String host;
    private String puerto;
    private String usuario;
    private String contrasena;
    private Connection con;

    public Conexion() {
        this.servidor = ConfigServidor.getServidor();
        this.host = ConfigServidor.getHost();
        this.puerto = ConfigServidor.getPuerto();
        this.usuario = ConfigServidor.getUsuario();
        this.contrasena = ConfigServidor.getContrasena();
    }

    public Conexion(String servidor, String host, String usuario, String contrasena) {
        this.servidor = servidor;
        this.host = host;
        this.puerto = "3306";
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public Conexion(String servidor, String host, String puerto, String usuario, String contrasena) {
        this.servidor = servidor;
        this.host = host;
        this.puerto = puerto;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getServidor() {
        return servidor;
    }

    public void setServidor(String servidor) {
        this.servidor = servidor;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Connection getCon() {
        String url="jdbc:mysql://"+host+":"+puerto+"/"+servidor;
        try {
            this.con=DriverManager.getConnection(url, usuario, contrasena);
        } catch (SQLException ex) {
            System.getLogger(Conexion.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }   
}