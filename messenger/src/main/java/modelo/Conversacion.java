/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class Conversacion {
    private final Contacto contacto;
    private final ArrayList<Mensaje> mensajes;
    private boolean notificada = false;

    // Constructor
    public Conversacion(Contacto contacto) {
        this.contacto = contacto;
        this.mensajes = new ArrayList<>();
    }
    
    
    // 📌 Obtener la lista de mensajes
    public ArrayList<Mensaje> getMensajes() {
        return mensajes;
    }

    // 📌 Obtener el contacto asignado a la conversación
    public Contacto getContacto() {
        return contacto;
    }
    
    // 📌 Agregar un mensaje a la conversación
    public boolean agregarMensaje(Mensaje mensaje) {
        mensajes.add(mensaje);
        return true;
    }
    
    public void setNotificacion(boolean estado)
    {
        this.notificada = estado;
    }
    
    public boolean tieneNotificacion()
    {
        return this.notificada;
    }
}