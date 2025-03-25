package grupo10.messenger.backend;

import java.util.ArrayList;
import java.util.List;

public class Conversacion {
    private final Contacto contacto;
    private final List<Mensaje> mensajes;

    // Constructor
    public Conversacion(Contacto contacto) {
        this.contacto = contacto;
        this.mensajes = new ArrayList<>();
    }
    
    // 📌 Obtener la lista de mensajes
    public List<Mensaje> getMensajes() {
        return mensajes;
    }

    // 📌 Obtener el contacto asignado a la conversación
    public Contacto getContacto() {
        return contacto;
    }
    
    // 📌 Obtener el nickname del contacto asignado a la conversación
    public String getContactoNickname() {
        return contacto.getNickname();
    }
    
    // 📌 Obtener la ip del contacto asignado a la conversación
    public String getContactoIp() {
        return contacto.getIp();
    }
    
    // 📌 Obtener el puerto del contacto asignado a la conversación
    public int getContactoPort() {
        return contacto.getPort();
    }
    
    // 📌 Agregar un mensaje a la conversación
    public boolean agregarMensaje(Mensaje mensaje) {
        mensajes.add(mensaje);
        return true;
    }

}

