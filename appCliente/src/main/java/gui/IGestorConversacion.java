/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gui;

import java.util.ArrayList;
import modelo.Contacto;
import modelo.Conversacion;
import modelo.Mensaje;


public interface IGestorConversacion {
   public void agregarMensaje(String contenido, boolean esMio, String hora);
   public void agregarConversacion(Conversacion conversacion);
   public void cargarConversaciones(ArrayList<Conversacion> conversaciones);
   public void mostrarConversacion(ArrayList<Mensaje> mensajes, String nickname);
   public void actualizarConversacion(String nickname, String mensaje);
   public void notificar();
}
