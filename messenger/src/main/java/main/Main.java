/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import controlador.Control;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import vista.IVista;
import vista.VentanaPrincipal;

/**
 *
 * @author Usuario
 */
public class Main {
    /**
     * Establece las condiciones y clases para dar inicio a la aplicacion
     * @param args 
     */
    public static void main(String[] args){
        
         try {
            // Establecer el Look and Feel predeterminado de la plataforma
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
         } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
        e.printStackTrace();
        }
        Control control = Control.getInstance();
        IVista vista = new VentanaPrincipal(control);  
        control.setVista(vista);
        vista.abrirFormularioRegistro();
    }
}

