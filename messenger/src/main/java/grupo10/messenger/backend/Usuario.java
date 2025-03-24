package grupo10.messenger.backend;


public class Usuario extends Persona {
    private final HistorialConversaciones historial;
    private final Agenda agenda;

    // Constructor
    public Usuario(String nickname, String ip, int port) {
        super(nickname, ip, port);
        this.historial = new HistorialConversaciones();
        this.agenda = new Agenda();
    }

    // 📌 Obtener la agenda
    public Agenda getAgenda() {
        return agenda;
    }

    // 📌 Obtener el historial de conversaciones
    public HistorialConversaciones getHistorial() {
        return historial;
    }
    
    // 📌 Existe un contacto
    public boolean existeContacto(int id) {
        return agenda.existeContacto(id);
    }

    public boolean existeContacto(String ip, int port) {
        return agenda.existeContacto(ip, port);
    }
    
    // 📌 Agregar un contacto a la agenda
    public boolean agregarContacto(Contacto contacto) {
        return agenda.agregarContacto(contacto);
    }

    // 📌 Eliminar un contacto de la agenda
    public boolean eliminarContacto(int id) {
        return agenda.eliminarContacto(id);
    }

    public boolean eliminarContacto(String ip, int port) {
        return agenda.eliminarContacto(ip, port);
    }

    // 📌 Obtener un contacto de la agenda
    public Contacto obtenerContacto(int id) {
        return agenda.obtenerContacto(id);
    }

    public Contacto obtenerContacto(String ip, int port) {
        return agenda.obtenerContacto(ip, port);
    }
    
    // Existe una conversacion
    public boolean existeConversacionPorId(int id) {
        return historial.existeConversacionPorId(id);
    }

    public boolean existeConversacionPorContactoId(int contactoId) {
        return historial.existeConversacionPorContactoId(contactoId);
    }
    
    public boolean existeConversacionPorContactoIpYPuerto(String ip, int port) {
        return historial.existeConversacionPorContactoIpYPuerto(ip, port);
    }
    
    // 📌 Crear una conversacion
    public Conversacion iniciarConversacion(Contacto contacto) {
        Conversacion nuevaConversacion = new Conversacion(contacto);

        if (historial.agregarConversacion(nuevaConversacion)) { 
            return nuevaConversacion;
        }

        return null;
    }

    // 📌 Eliminar una conversación
    public boolean eliminarConversacionPorId(int id) {
        return historial.eliminarConversacionPorId(id);
    }
    
    public boolean eliminarConversacionPorContactoId(int contactId) {
        return historial.eliminarConversacionPorContactoId(contactId);
    }
    
    public boolean eliminarConversacionPorContactoIpYPuerto(String ip, int port) {
        return historial.eliminarConversacionPorContactoIpYPuerto(ip, port);
    }
    
    // Obtener una conversacion
    public Conversacion obtenerConversacionPorId(int id) {
        return historial.obtenerConversacionPorId(id);
    }
    
    public Conversacion obtenerConversacionPorContactoId(int contactId) {
        return historial.obtenerConversacionPorContactoId(contactId);
    }
    
    public Conversacion obtenerConversacionPorContactoIpYPuerto(String ip, int port) {
        return historial.obtenerConversacionPorContactoIpYPuerto(ip, port);
    }
    
    // 📌 Agregar un mensaje a conversacion
    public void agregarMensaje(int idContacto, Mensaje mensaje) {
        
    }

}

