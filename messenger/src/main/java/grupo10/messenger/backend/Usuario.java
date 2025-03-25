package grupo10.messenger.backend;

import java.util.ArrayList;
import java.util.List;


public class Usuario extends Persona {
    private final List<Conversacion> conversaciones;
    private final List<Contacto> agenda;


    // Constructor
    public Usuario(String nickname, String ip, int port) {
        super(nickname, ip, port);
        this.conversaciones = new ArrayList<>();
        this.agenda = new ArrayList<>();
    }

    // 📌 Obtener la agenda
    public List<Contacto> getAgenda() {
        return agenda;
    }

    // 📌 Obtener el historial de conversaciones
    public List<Conversacion> getConversaciones() {
        return conversaciones;
    }
    
    public boolean existeContacto(String ip, int port) {
        return agenda.stream().anyMatch(contacto -> contacto.getIp().equals(ip) && contacto.getPort() == port);
    }

    public Contacto obtenerContacto(String ip, int port) {
        return agenda.stream()
                .filter(contacto -> contacto.getIp().equals(ip) && contacto.getPort() == port)
                .findFirst()
                .orElse(null);
    }
    
    public Contacto agregarContacto(String nickname, String ip, int port) {
        if (!existeContacto(ip, port)){
            Contacto newCon = new Contacto(nickname, ip, port);
            agenda.add(newCon);
            return newCon;
        }
        return null;
    }
    
    public boolean existeConversacion(String ip, int port) {
        boolean eliminado = conversaciones.stream().anyMatch(conversacion -> conversacion.getContacto().getIp().equals(ip) && conversacion.getContacto().getPort() == port);
        return eliminado;
    }

    public Conversacion obtenerConversacion(String ip, int port) {
        return conversaciones.stream()
                .filter(conversacion -> conversacion.getContacto().getIp().equals(ip) && conversacion.getContacto().getPort() == port)
                .findFirst()
                .orElse(null);
    }

    public Conversacion agregarConversacion(String ip, int port) {
        Contacto con = obtenerContacto(ip, port);
        if (con != null) {
            if (obtenerConversacion(ip, port) == null) {
                Conversacion newCon = new Conversacion(con);
                conversaciones.add(newCon);
                return newCon;
            }
        }
        return null;
    }

    public boolean agregarMensaje(String ip, int port, Mensaje msg) {
        Conversacion con = obtenerConversacion(ip, port);
        return con.agregarMensaje(msg);
    }

}

