/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package encriptacion;

import modelo.Agenda;
import modelo.Contacto;
import modelo.Conversacion;
import modelo.Mensaje;

/**
 *
 * @author user
 */
public interface EncriptacionStrategy {
    String encriptarString(String contenido) throws Exception;
    String desencriptarString(String contenido) throws Exception;
    
    Mensaje encriptarMensaje(Mensaje msj) throws Exception;
    Mensaje desencriptarMensaje(Mensaje msj) throws Exception;
    
    Conversacion encriptarConversacion(Conversacion conv) throws Exception;
    Conversacion desencriptarConversacion(Conversacion conv) throws Exception;
    
    Contacto encriptarContacto(Contacto cont) throws Exception;
    Contacto desencriptarContacto(Contacto cont) throws Exception;
    
    Agenda encriptarAgenda(Agenda agnd) throws Exception;
    Agenda desencriptarAgenda(Agenda agnd) throws Exception;
}
