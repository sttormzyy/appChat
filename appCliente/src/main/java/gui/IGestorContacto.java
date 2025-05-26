/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gui;

import java.util.ArrayList;
import modelo.Contacto;

/**
 *
 * @author Usuario
 */
public interface IGestorContacto {
   public void agregarContacto(Contacto contacto);
   public void actualizarContacto(String nicknameViejo,Contacto contacto);
   public void cargarContactos(ArrayList<Contacto> contactos);   
}
