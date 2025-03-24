/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package grupo10.messenger.backend;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author user
 */
public class Control {
    private static Control instance;
    private final Usuario usuario;
    private final Conexion conexion;

    private Control(String nickname, String ip, int port) {
        this.usuario = new Usuario(nickname, ip, port);
        this.conexion = new Conexion(nickname, ip, port);
    }

    public static synchronized Control getInstance(String nickname, String ip, int port) {
        if (instance == null) {
            instance = new Control(nickname, ip, port);
        }
        return instance;
    }
    
    public static synchronized Control getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Control no ha sido inicializado. Llama a getInstance(nickname, ip, port) primero.");
        }
        return instance;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public Conexion getConexion() {
        return conexion;
    }
    
    public synchronized boolean almacenarMensaje(int conversacionId, Mensaje msg) {
        Conversacion chat = usuario.getHistorial().obtenerConversacionPorId(conversacionId);
        if (chat == null) {
            return false;
        }
        return chat.agregarMensaje(msg);
    }
    
    public void enviarMensaje(String contenidoMensaje, int conversacionId) {
        Conversacion chat = usuario.getHistorial().obtenerConversacionPorContactoId(conversacionId);
        Contacto con = chat.getContacto();
        MensajeRed msgRed = new MensajeRed(usuario.getNickname(), usuario.getIp(), usuario.getPort(), con.getIp(), con.getPort(), contenidoMensaje);
        conexion.sendMessage(msgRed);
        Mensaje newMsg = new Mensaje(contenidoMensaje, true);
        almacenarMensaje(conversacionId, newMsg);
    }
    
    public Mensaje recibirMensaje(MensajeRed msgRed){
        Contacto con = usuario.obtenerContacto(msgRed.getMyIp(), msgRed.getMyPort());
        if (con == null) {
            con = new Contacto(msgRed.getMyNickname(),msgRed.getMyIp(),msgRed.getMyPort());
            usuario.getAgenda().agregarContacto(con);
        }
        
        Conversacion chat = usuario.getHistorial().obtenerConversacionPorContactoId(con.getId());
        if (chat == null) {
            chat = new Conversacion(con);
            usuario.getHistorial().agregarConversacion(chat);
        }
        
        Mensaje newMsg = new Mensaje(msgRed.getContenido(), false);
        almacenarMensaje(chat.getId(), newMsg);
        return newMsg;
    }
    
    public boolean agregarContacto(String nickname, String ip, int port) {
        Contacto newCon = new Contacto(nickname, ip, port);
        boolean saveResult = usuario.getAgenda().agregarContacto(newCon);
        return saveResult;
    }
    
    public boolean agregarConversacion(int contactId) {
        Contacto con = usuario.getAgenda().obtenerContacto(contactId);
        Conversacion chat = new Conversacion(con);
        boolean saveResult = usuario.getHistorial().agregarConversacion(chat);
        return saveResult;
    }
    
    public Conversacion verConversacion(int conversacionId) {
        return usuario.getHistorial().obtenerConversacionPorId(conversacionId);
    }

}
