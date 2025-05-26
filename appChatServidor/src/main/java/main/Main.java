/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import configuracion.Configuracion;
import configuracion.Controlador;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;



/**
 *
 * @author Usuario
 */
public class Main {
    /**
     * Establece las condiciones y clases para dar inicio a la aplicacion
     * @param args 
     */
    public static void main(String[] args)
    {  
        try 
        {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {}
        
        Controlador controlador = new Controlador();
        Configuracion configuracion = new Configuracion(controlador);
        controlador.setVista(configuracion);
    }
}

