package grupo10.messenger.backend;

import java.util.ArrayList;
import java.util.List;

public class Agenda {
    private final List<Contacto> contactos;

    // Constructor
    public Agenda() {
        this.contactos = new ArrayList<>();
    }

    // Getter de la lista de contactos
    public List<Contacto> getContactos() {
        return contactos;
    }
    
    // 📌 Agregar un contacto
    public boolean agregarContacto(Contacto contacto) {
        contactos.add(contacto);
        return true;
    }

    // 📌 Eliminar un contacto por ID
    public boolean eliminarContacto(int id) {
        boolean eliminado = contactos.removeIf(contacto -> contacto.getId() == id);
        return eliminado;
    }

    // 📌 Obtener un contacto por ID (devuelve null si no existe)
    public Contacto obtenerContacto(int id) {
        return contactos.stream()
                .filter(contacto -> contacto.getId() == id)
                .findFirst()
                .orElse(null);
    }

}

