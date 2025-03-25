package grupo10.messenger.backend.modelo;

import java.util.ArrayList;


public class Usuario extends Persona {
    
    private ArrayList<Conversacion> conversaciones = null;
    private ArrayList<Contacto> agenda = null;

    public Usuario(String nickname, String ip, int port) {
        super(nickname, ip, port);
        this.conversaciones = new ArrayList();
        this.agenda = new ArrayList();
    }

    public ArrayList<Contacto> getAgenda() {
        return agenda;
    }

    public ArrayList<Conversacion> getConversaciones() {
        return conversaciones;
    }
    
    public Contacto agregarContacto(String nickname, String ip, int port) {
        if (buscarContacto(ip, port) == null){
            Contacto newCon = new Contacto(nickname, ip, port);
            agenda.add(newCon);
            return newCon;
        }
        return null;
    }

    public Contacto buscarContacto(String ip, int port) {
        return agenda.stream()
                .filter(contacto -> contacto.getIp().equals(ip) && contacto.getPort() == port)
                .findFirst()
                .orElse(null);
    }

    public Conversacion agregarConversacion(String ip, int port) {
        Contacto con = buscarContacto(ip, port);
        if (con != null) {
            if (buscarConversacion(ip, port) == null) {
                Conversacion newCon = new Conversacion(con);
                conversaciones.add(newCon);
                return newCon;
            }
        }
        return null;
    }

    public Conversacion buscarConversacion(String ip, int puerto){
        int i = 0;
        while(i<conversaciones.size() && noEsConversacion(conversaciones.get(i),ip,puerto))
            i++;
        if(i<conversaciones.size())
            return conversaciones.get(i);
        return null;
    }
    
    private boolean noEsConversacion(Conversacion chat, String ip, int puerto){
        return (chat.getContacto().getIp().equals(ip) && puerto == chat.getContacto().getPort());
    }

    public boolean agregarMensaje(String ip, int port, Mensaje msg) {
        Conversacion con = buscarConversacion(ip, port);
        return con.agregarMensaje(msg);
    }

}

