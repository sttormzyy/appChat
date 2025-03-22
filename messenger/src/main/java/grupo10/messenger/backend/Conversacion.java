package grupo10.messenger.backend;

import java.util.ArrayList;
import java.util.List;

public class Conversacion {
    private static int idCounter = 1;
    private final int id;
    private final Contacto contacto;
    private final List<Mensaje> mensajes;

    // Constructor
    public Conversacion(Contacto contacto) {
        this.id = idCounter++;
        this.contacto = contacto;
        this.mensajes = new ArrayList<>();
    }
    
    // Getter para obtener el ID
    public int getId() {
        return id;
    }
    
    // 📌 Obtener la lista de mensajes
    public List<Mensaje> getMensajes() {
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

    // 📌 Eliminar un contacto por ID
    public boolean eliminarMensaje(int id) {
        boolean eliminado = mensajes.removeIf(mensaje -> mensaje.getId() == id);
        return eliminado;
    }

}

