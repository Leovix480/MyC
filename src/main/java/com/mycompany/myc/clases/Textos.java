package com.mycompany.myc.clases;

/**
 * Utilidades de normalización de texto reutilizadas por los controladores
 * que crean/editan Ingredientes, Productos (Recetas) y Clientes.
 */
public class Textos {

    private Textos() {
    }

    /**
     * Pone en mayúscula solo la primera letra del texto; el resto se conserva
     * exactamente como fue escrito. Usado para nombres de ingredientes y productos.
     * Ej.: "tomate" -> "Tomate", "salsa de tomate" -> "Salsa de tomate".
     */
    public static String capitalizarInicial(String texto) {
        if (texto == null) {
            return null;
        }
        String limpio = texto.trim();
        if (limpio.isEmpty()) {
            return limpio;
        }
        return Character.toUpperCase(limpio.charAt(0)) + limpio.substring(1);
    }

    /**
     * Normaliza un nombre o apellido de persona a formato "Nombre Propio":
     * primera letra de cada palabra en mayúscula, el resto en minúscula.
     * Reconoce espacios, guiones y apóstrofes como separadores de palabra para
     * no destruir nombres/apellidos compuestos (ej.: "de la cruz" -> "De La Cruz",
     * "garcia-lopez" -> "Garcia-Lopez", "JUAN" -> "Juan").
     */
    public static String capitalizarNombrePropio(String texto) {
        if (texto == null) {
            return null;
        }
        String limpio = texto.trim().replaceAll("\\s+", " ");
        if (limpio.isEmpty()) {
            return limpio;
        }

        StringBuilder resultado = new StringBuilder(limpio.length());
        boolean inicioDePalabra = true;
        for (int i = 0; i < limpio.length(); i++) {
            char c = limpio.charAt(i);
            if (Character.isLetter(c)) {
                resultado.append(inicioDePalabra ? Character.toUpperCase(c) : Character.toLowerCase(c));
                inicioDePalabra = false;
            } else {
                resultado.append(c);
                inicioDePalabra = (c == ' ' || c == '-' || c == '\'');
            }
        }
        return resultado.toString();
    }
}
