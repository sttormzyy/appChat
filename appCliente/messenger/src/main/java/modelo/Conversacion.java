/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public class Conversacion {
    private final Contacto contacto;
    private final ArrayList<Mensaje> mensajes;
    private boolean notificada = false;

    public Conversacion(Contacto contacto) {
        this.contacto = contacto;
        this.mensajes = new ArrayList<>();
    }
    
    public ArrayList<Mensaje> getMensajes() {
        return mensajes;
    }

    public Contacto getContacto() {
        return contacto;
    }
    
    public boolean agregarMensaje(Mensaje mensaje) {
        mensajes.add(mensaje);
        return true;
    }
    
    public String getUltimoMensaje() {
    // Verificar si la lista de mensajes está vacía o el último mensaje es null
    if (mensajes.isEmpty() || mensajes.getLast() == null) {
        return "";  // Retorna cadena vacía si no hay mensajes
    } else {
        return mensajes.getLast().getContenido();  // Retorna el contenido del último mensaje
    }
    }

    public void setNotificacion(boolean estado)
    {
        this.notificada = estado;
    }
    
    public boolean tieneNotificacion()
    {
        return this.notificada;
    }

    public String getNicknameReal() {
	return contacto.getNickname();
    }
    
    public String getNicknameAgendado()
    {
        return contacto.getNicknameAgendado();
    }
}