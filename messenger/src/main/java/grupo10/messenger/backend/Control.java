/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package grupo10.messenger.backend;

/**
 *
 * @author user
 */
public class Control {
    private static Control instance;
    private Usuario usuario;
    
    private Control() {}

    public static synchronized Control getInstance() {
        if (instance == null) {
            instance = new Control();
        }
        return instance;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public Usuario registrarUsuario(String nickname, String ip, int port){
        if (usuario == null){
            this.usuario = new Usuario(nickname, ip, port);
        }
        return usuario;
    }
    
    public Contacto agregarContacto(String nickname, String ip, int port) {
        Contacto newCon = usuario.agregarContacto(nickname, ip, port);
        return newCon;
    }
    
    public Conversacion agregarConversacion(String ip, int port) {
        Conversacion newCon = usuario.agregarConversacion(ip, port);
        return newCon;
    }
    
    public Conversacion verConversacion(String ip, int port) {
        return usuario.obtenerConversacion(ip, port);
    }

}
