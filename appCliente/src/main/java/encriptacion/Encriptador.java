/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
public class Encriptador {
    private EncriptacionStrategy strategy;

    public void setStrategy(EncriptacionStrategy strategy) {
        this.strategy = strategy;
    }

    public String encriptarString(String contenido) throws Exception {
        return strategy.encriptarString(contenido);
    }

    public String desencriptarString(String contenido) throws Exception {
        return strategy.desencriptarString(contenido);
    }
    
    public Mensaje encriptarMensaje(Mensaje msj) throws Exception {
        return strategy.encriptarMensaje(msj);
    }
    
    public Mensaje desencriptarMensaje(Mensaje msj) throws Exception {
        return strategy.desencriptarMensaje(msj);
    }
    
    public Conversacion encriptarConversacion(Conversacion conv) throws Exception {
        return strategy.encriptarConversacion(conv);
    }
    
    public Conversacion desencriptarConversacion(Conversacion conv) throws Exception {
        return strategy.desencriptarConversacion(conv);
    }
    
    public Contacto encriptarContacto(Contacto cont) throws Exception{
        return null;
    }
    
    public Contacto desencriptarContacto(Contacto cont) throws Exception{
        return null;
    }
    
    public Agenda encriptarAgenda(Agenda agnd) throws Exception{
        return null;
    }
    
    public Agenda desencriptarAgenda(Agenda agnd) throws Exception{
        return null;
    }
}
