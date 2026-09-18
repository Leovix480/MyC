package com.mycompany.myc.clases;

import java.util.prefs.Preferences;

/**
 * Datos de conexion al servidor, editables desde la vista "Configuracion del
 * servidor" (se abre con el codigo Konami desde el menu). Se guardan en las
 * preferencias del usuario de Windows, asi que persisten entre aperturas y
 * cierres de la aplicacion sin depender de que el .jar sea editable.
 *
 * Los valores por defecto son los que ya usaba el sistema (servidor remoto en
 * 186.17.107.54), para que nada cambie hasta que alguien guarde otros datos
 * desde esa vista.
 */
public class ConfigServidor {

    private static final Preferences PREFS = Preferences.userNodeForPackage(ConfigServidor.class);

    private static final String CLAVE_SERVIDOR = "servidor";
    private static final String CLAVE_HOST = "host";
    private static final String CLAVE_PUERTO = "puerto";
    private static final String CLAVE_USUARIO = "usuario";
    private static final String CLAVE_CONTRASENA = "contrasena";

    private static final String SERVIDOR_POR_DEFECTO = "myc";
    private static final String HOST_POR_DEFECTO = "186.17.107.54";
    private static final String PUERTO_POR_DEFECTO = "33063";
    private static final String USUARIO_POR_DEFECTO = "root";
    private static final String CONTRASENA_POR_DEFECTO = "m4MYC_601v904";

    private ConfigServidor() {
    }

    public static String getServidor() {
        return PREFS.get(CLAVE_SERVIDOR, SERVIDOR_POR_DEFECTO);
    }

    public static String getHost() {
        return PREFS.get(CLAVE_HOST, HOST_POR_DEFECTO);
    }

    public static String getPuerto() {
        return PREFS.get(CLAVE_PUERTO, PUERTO_POR_DEFECTO);
    }

    public static String getUsuario() {
        return PREFS.get(CLAVE_USUARIO, USUARIO_POR_DEFECTO);
    }

    public static String getContrasena() {
        return PREFS.get(CLAVE_CONTRASENA, CONTRASENA_POR_DEFECTO);
    }

    public static void guardar(String servidor, String host, String puerto, String usuario, String contrasena) {
        PREFS.put(CLAVE_SERVIDOR, servidor);
        PREFS.put(CLAVE_HOST, host);
        PREFS.put(CLAVE_PUERTO, puerto);
        PREFS.put(CLAVE_USUARIO, usuario);
        PREFS.put(CLAVE_CONTRASENA, contrasena);
    }
}
