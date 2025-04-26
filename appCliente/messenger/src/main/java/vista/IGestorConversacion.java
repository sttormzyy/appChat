/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package vista;

import java.util.ArrayList;
import modelo.Contacto;
import modelo.Conversacion;
import modelo.Mensaje;


public interface IGestorConversacion {
   public void agregarMensaje(String contenido, boolean esMio, String hora);
   public void agregarContacto(Contacto contacto);
   public void actualizarContacto(String nicknameViejo,Contacto contacto);
   public void cargarContactos(ArrayList<Contacto> contactos);
   public void agregarConversacion(Conversacion conversacion);
   public void cargarConversaciones(ArrayList<Conversacion> conversaciones);
   public void mostrarConversacion(ArrayList<Mensaje> mensajes, String nickname);
   public void notificar();
}
