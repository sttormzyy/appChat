/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import gui.Configuracion;
import directorio.Directorio;
import directorio.IDirectorioListener;
import gui.VentanaDirectorio;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Usuario
 */
public class Main{

    public static void main(String args[]) {
             
        try 
         {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
         } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
 
        }
        Configuracion configuracion = new Configuracion();
        Directorio directorio = new Directorio();
        configuracion.agregarConfiguracionListener(directorio);
        IDirectorioListener ventana = new VentanaDirectorio();  
        directorio.agregarObservador(ventana);
    }
}
