/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Singleton.java to edit this template
 */
package com.mycompany.myc.clases;

/**
 *
 * @author Terceros A
 */
public class ventasSingleton {
    private int codReceta;
    private int codIngrediente;

    private String rucCliente;
    private int codProducto;
    
    
    private ventasSingleton() {
        
    }
    
    public static ventasSingleton getInstance() {
        return ventasSingletonHolder.INSTANCE;
    }

    public String getRucCliente() {
        return rucCliente;
    }

    public void setRucCliente(String rucCliente) {
        this.rucCliente = rucCliente;
    }

    public int getCodProducto() {
        return codProducto;
    }

    public void setCodProducto(int codProducto) {
        this.codProducto = codProducto;
    }

    public int getCodIngrediente() {
        return codIngrediente;
    }

    public void setCodIngrediente(int codIngrediente) {
        this.codIngrediente = codIngrediente;
    }

    public int getCodReceta() {
        return codReceta;
    }

    public void setCodReceta(int codReceta) {
        this.codReceta = codReceta;
    }
    
    private static class ventasSingletonHolder {

        private static final ventasSingleton INSTANCE = new ventasSingleton();
    }
}
