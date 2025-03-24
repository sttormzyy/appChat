package grupo10.messenger.backend;

import java.util.ArrayList;
import java.util.List;

public class Agenda {
    private final List<Contacto> contactos;

    // Constructor
    public Agenda() {
        this.contactos = new ArrayList<>();
    }

    // Getter y setter
    public List<Contacto> getContactos() {
        return contactos;
    }
    
    // 📌 Existe un contacto
    public boolean existeContacto(int id) {
        return contactos.stream().anyMatch(contacto -> contacto.getId() == id);
    }

    public boolean existeContacto(String ip, int port) {
        return contactos.stream().anyMatch(contacto -> contacto.getIp().equals(ip) && contacto.getPort() == port);
    }

    // 📌 Agregar un contacto
    public boolean agregarContacto(Contacto contacto) {
        contactos.add(contacto);
        return true;
    }

    // 📌 Eliminar un contacto
    public boolean eliminarContacto(int id) {
        boolean eliminado = contactos.removeIf(contacto -> contacto.getId() == id);
        return eliminado;
    }

    public boolean eliminarContacto(String ip, int port) {
        boolean eliminado = contactos.removeIf(contacto -> contacto.getIp().equals(ip) && contacto.getPort() == port);
        return eliminado;
    }
    
    // 📌 Obtener un contacto
    public Contacto obtenerContacto(int id) {
        return contactos.stream()
                .filter(contacto -> contacto.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    public Contacto obtenerContacto(String ip, int port) {
        return contactos.stream()
                .filter(contacto -> contacto.getIp().equals(ip) && contacto.getPort() == port)
                .findFirst()
                .orElse(null);
    }
    
}
