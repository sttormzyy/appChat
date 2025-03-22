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
    
    // 📌 Agregar un contacto a la agenda
    public boolean agregarContacto(Contacto contacto) {
        return agenda.agregarContacto(contacto);
    }

    // 📌 Eliminar un contacto de la agenda
    public boolean eliminarContacto(int id) {
        return agenda.eliminarContacto(id);
    }

    // 📌 Obtener un contacto de la agenda
    public Contacto obtenerContacto(int id) {
        return agenda.obtenerContactoPorId(id);
    }

    // 📌 Crear una nueva conversación con un contacto
    public Conversacion iniciarConversacion(Contacto contacto) {
        Conversacion nuevaConversacion = new Conversacion(contacto);

        if (historial.agregarConversacion(nuevaConversacion)) { 
            return nuevaConversacion;
        }

        return null;
    }

    // 📌 Eliminar una conversación del historial
    public boolean eliminarConversacion(int id) {
        return historial.eliminarConversacion(id);
    }
    
    // 📌 Agregar un mensaje a la conversacion del contacto
    public void agregarMensaje(int idContacto, Mensaje mensaje) {
        
    }

}

