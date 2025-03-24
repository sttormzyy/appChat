package grupo10.messenger.backend;

import java.util.ArrayList;
import java.util.List;

public class HistorialConversaciones {
    private final List<Conversacion> conversaciones;

    // Constructor
    public HistorialConversaciones() {
        this.conversaciones = new ArrayList<>();
    }

    // 📌 Getter y setter
    public List<Conversacion> getConversaciones() {
        return conversaciones;
    }
    
    // 📌 Existe una conversación
    public boolean existeConversacionPorId(int id) {
        boolean eliminado = conversaciones.stream().anyMatch(conversacion -> conversacion.getId() == id);
        return eliminado;
    }
    
    public boolean existeConversacionPorContactoId(int contactoId) {
        boolean eliminado = conversaciones.stream().anyMatch(conversacion -> conversacion.getContacto().getId() == contactoId);
        return eliminado;
    }
    
    public boolean existeConversacionPorContactoIpYPuerto(String ip, int port) {
        boolean eliminado = conversaciones.stream().anyMatch(conversacion -> conversacion.getContacto().getIp().equals(ip) && conversacion.getContacto().getPort() == port);
        return eliminado;
    }
    
    // 📌 Agregar una conversación
    public boolean agregarConversacion(Conversacion conversacion) {
        conversaciones.add(conversacion);
        return true;
    }

    // 📌 Eliminar una conversación
    public boolean eliminarConversacionPorId(int id) {
        boolean eliminado = conversaciones.removeIf(conversacion -> conversacion.getId() == id);
        return eliminado;
    }

    public boolean eliminarConversacionPorContactoId(int contactoId) {
        boolean eliminado = conversaciones.removeIf(conversacion -> conversacion.getContacto().getId() == contactoId);
        return eliminado;
    }

    public boolean eliminarConversacionPorContactoIpYPuerto(String ip, int port) {
        boolean eliminado = conversaciones.removeIf(conversacion -> conversacion.getContacto().getIp().equals(ip) && conversacion.getContacto().getPort() == port);
        return eliminado;
    }
    
    // 📌 Obtener una conversación
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

    public Conversacion obtenerConversacionPorContactoIpYPuerto(String ip, int port) {
        return conversaciones.stream()
                .filter(conversacion -> conversacion.getContacto().getIp().equals(ip) && conversacion.getContacto().getPort() == port)
                .findFirst()
                .orElse(null);
    }

}

