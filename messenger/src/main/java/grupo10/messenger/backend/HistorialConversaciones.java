package grupo10.messenger.backend;

import java.util.ArrayList;
import java.util.List;

public class HistorialConversaciones {
    private final List<Conversacion> conversaciones;

    // Constructor
    public HistorialConversaciones() {
        this.conversaciones = new ArrayList<>();
    }

    // 📌 Obtener la lista de conversaciones
    public List<Conversacion> getConversaciones() {
        return conversaciones;
    }
    
    // 📌 Agregar una conversación
    public boolean agregarConversacion(Conversacion conversacion) {
        conversaciones.add(conversacion);
        return true;
    }

    // 📌 Eliminar una conversación por id
    public boolean eliminarConversacion(int id) {
        boolean eliminado = conversaciones.removeIf(conversacion -> conversacion.getId() == id);
        return eliminado;
    }

    // 📌 Obtener una conversación por id
    public Conversacion obtenerConversacionPorId(int id) {
        return conversaciones.stream()
                .filter(conversacion -> conversacion.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Conversacion obtenerConversacionPorContactoId(int contactoId) {
        return conversaciones.stream()
                .filter(conversacion -> conversacion.getContacto().getId() == contactoId)
                .findFirst()
                .orElse(null);
    }
}

