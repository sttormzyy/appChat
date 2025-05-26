/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement(name = "conversacion")
public class Conversacion {
    private Contacto contacto;
    private ArrayList<Mensaje> mensajes = new ArrayList<>();
    private boolean notificacion = false;

    public Conversacion() {
    }

    public Conversacion(Contacto contacto) {
        this.contacto = contacto;
    }

    public Conversacion(Contacto contacto, ArrayList<Mensaje> mensajes, boolean notificada) {
        this.contacto = contacto;
        this.mensajes = mensajes;
        this.notificacion = notificada;
    }

    @XmlElementWrapper(name = "mensajes") // Nombre del contenedor XML
    @XmlElement(name = "mensaje")
    public ArrayList<Mensaje> getMensajes() {
        return mensajes;
    }

    @XmlElement(name = "contacto")
    public Contacto getContacto() {
        return contacto;
    }
    
    public boolean agregarMensaje(String contenido, boolean esMio) {
        Mensaje mensaje = new Mensaje(contenido,esMio);
        mensajes.add(mensaje);
        return true;
    }
    
    public boolean agregarMensaje(String contenido, boolean esMio, String hora) {
        Mensaje mensaje = new Mensaje(contenido,esMio,hora);
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
        this.notificacion = estado;
    }

    public void setContacto(Contacto contacto) {
        this.contacto = contacto;
    }
    
    @XmlElement(name = "notificada")
    public boolean getNotificacion()
    {
        return this.notificacion;
    }

    public String getNicknameReal() {
	return contacto.getNicknameReal();
    }
    
    public String getNicknameAgendado()
    {
        return contacto.getNicknameAgendado();
    }

    public void setMensajes(ArrayList<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }
    
    // Metodos persistencia
    
    public static String toTextoPlano(Conversacion c) {
        StringBuilder sb = new StringBuilder();

        sb.append("Contacto:").append(c.getContacto().getNicknameReal()).append("\n");

        sb.append("Notificada:").append(c.getNotificacion()).append("\n");

        sb.append("Mensajes:\n");
        for (Mensaje m : c.getMensajes()) {
            sb.append(Mensaje.toTextoPlano(m)).append("\n");
        }

        return sb.toString();
    }
}