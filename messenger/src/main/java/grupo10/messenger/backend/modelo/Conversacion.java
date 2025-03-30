package grupo10.messenger.backend.modelo;

import java.util.ArrayList;
import java.util.List;

public class Conversacion {
    private final Contacto contacto;
    private final List<Mensaje> mensajes;
    private boolean notificacion;
    
    // Constructor
    public Conversacion(Contacto contacto) {
        this.contacto = contacto;
        this.mensajes = new ArrayList<>();
        this.notificacion = false;
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
    
    public boolean getNotificacion() {
        return this.notificacion;
    }
    
    public void setNotificacion(boolean notificacion) {
        this.notificacion = notificacion;
    }
    
    // 📌 Agregar un mensaje a la conversación
    public boolean agregarMensaje(Mensaje mensaje) {
        mensajes.add(mensaje);
        return true;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Conversación con ").append(contacto.toString()).append(")\n");

        sb.append("Mensajes:\n");
        for (Mensaje mensaje : mensajes) {
            sb.append("  - ").append(mensaje).append("\n");
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Conversacion otra = (Conversacion) obj;

        // Comparamos la lista de mensajes
        if (mensajes.size() != otra.mensajes.size()) return false;

        for (int i = 0; i < mensajes.size(); i++) {
            if (!mensajes.get(i).equals(otra.mensajes.get(i))) {
                return false;
            }
        }

        return true;
    }
}

