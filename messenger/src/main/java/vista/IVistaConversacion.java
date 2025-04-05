/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package vista;

import java.util.ArrayList;
import modelo.Contacto;


public interface IVistaConversacion {
   public void abrirFormularioAgregarConversacion(ArrayList<Contacto> contactosSinConversacion);
   public void cerrarFormularioAgregarConversacion(); 
   public String getIPConversacion();   
   public int getPuertoConversacion();   
}
