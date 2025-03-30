/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package grupo10.messenger.backend.control;

import grupo10.messenger.backend.modelo.Contacto;
import grupo10.messenger.backend.modelo.Conversacion;
import grupo10.messenger.backend.modelo.Mensaje;
import grupo10.messenger.backend.modelo.MensajeRed;
import grupo10.messenger.backend.modelo.Usuario;
import grupo10.messenger.backend.red.Cliente;
import grupo10.messenger.backend.red.Server;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
    
    public boolean registrar(String nickname,int puerto){
        if (this.iniciarServer(puerto)){
            try {
                InetAddress localHost = InetAddress.getLocalHost();
                usuario = new Usuario(nickname, localHost.getHostAddress(),puerto);
                return true;
            } catch(UnknownHostException e) {
                System.out.println("No se pudo obtener la IP.");
                return false;
            }
        }
        return false;
    }
    
    private boolean iniciarServer(int puerto){
        
        Server server = new Server(puerto,instance);
        
        Thread hilo = new Thread(server);
        hilo.start();
        
        try {
            hilo.join(1000);//espero 1 seg para comprobar si se conecto o no el servidor al puerto
        } catch (InterruptedException ex) {
            return false;
        }
        return server.isConectado();
    }
    
    public boolean agregarContacto(String nickname, String ip, int port) {
        if (isValidIPv4(ip) && isValidPort(port)){
            Contacto newCon = usuario.agregarContacto(nickname, ip, port);
            return newCon!=null;
        }
        return false;
    }
    
    private static boolean isValidPort(int port) {
        return port >= 0 && port <= 65535;
    }
    
    private static boolean isValidIPv4(String ip) {
        String ipv4Regex = "^((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])\\.){3}" +
                           "(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])$";
        return Pattern.matches(ipv4Regex, ip);
    }
    
    public boolean agregarConversacion(String ip, int port) {
        Conversacion newCon = usuario.agregarConversacion(ip, port);
        return newCon!=null;
    }
    
    public Conversacion verConversacion(String ip, int port) {
        return usuario.buscarConversacion(ip, port);
    }

    public synchronized boolean enviarMensaje(String contenido, String IP, int puerto){
       MensajeRed msjRed = new MensajeRed(usuario.getNickname(),usuario.getIp(),usuario.getPort(),IP,puerto,contenido);
       if (instance.enviarMensaje(msjRed)){
           Mensaje mensaje = new Mensaje(contenido,true);
           usuario.buscarConversacion(IP, puerto).agregarMensaje(mensaje);
           return true;
       }
       return false;
    }
    
    private boolean enviarMensaje(MensajeRed msj){
        Cliente cliente = new Cliente(msj);
        Thread hilo = new Thread(cliente);
        hilo.start();
        try {
            hilo.join(); //hilo principal de ejecucion espera a que el hilo cliente termine
        } catch (InterruptedException ex) {
            Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(cliente.isConectado());
        return cliente.isConectado();
    }
    
    public synchronized void recibirMensaje(MensajeRed mensaje){
        Mensaje msj = new Mensaje(mensaje.getContenido(),true);
        Conversacion conversacion = usuario.buscarConversacion(mensaje.getMyIp(), mensaje.getMyPort());
        if( conversacion == null)
            conversacion = new Conversacion(new Contacto(mensaje.getMyNickname(),mensaje.getMyIp(),mensaje.getMyPort()));
        conversacion.agregarMensaje(msj);
        //llamar metodo de la vista
    }
}
