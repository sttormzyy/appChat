/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import modelo.Agenda;
import modelo.Contacto;
import modelo.Conversacion;
import modelo.Mensaje;
import modelo.MensajeRed;
import modelo.Usuario;
import red.Servidor;
import vista.IVista;
import vista.VentanaError;
import vista.VentanaPrincipal.SideBar;

public class Control implements ActionListener{
    private static Control instance = null;
    private Servidor servidor;
    private Usuario usuario;
    private IVista vista;
    private Control(){}
    
    public static Control getInstance(){      
        if(instance == null)
            instance = new Control();
        return instance;
    }
    
    public void setVista(IVista vista)
    {
        this.vista = vista;
    }
    
    public Usuario getUsuario()
    {
        return this.usuario;
    }
    
    public void setUsuario(Usuario u)
    {
        this.usuario = u;
    }
    /**
     * En base al action command del evento, ejecuta las acciones correspondientes
     * @param evento: "REGISTRAR" | "ENVIAR MENSAJE" | "SOLIICTUD AGREGAR CONTACTO" | "SOLICITUD AGREGAR CONVERSACION"|
     * | "AGREGAR CONVERSACION" | "AGREGAR CONTACTO" | "MOVER A AGENDA" |  "MOVER A CONVERSACIONES" | "VER CONVERSACION"
     */
    @Override
    public void actionPerformed(ActionEvent evento) {
        int puerto;
        String ip;
        String nickname;
        Agenda agenda;
        Conversacion conversacion;
        
        switch(evento.getActionCommand()) {
            case "REGISTRO":
                nickname = vista.getNicknameRegistro();
                puerto = vista.getPuertoRegistro();
                this.registrar(nickname,"127.0.0.1",puerto);
                break;
                
            case "ENVIAR MENSAJE":
                String contenido = vista.getMensaje();
                System.out.println(contenido);
                if(hayChatSeleccionado())
                {
                    if(sePuedeEnviarMensaje(contenido)){
                        ip = vista.getIPactiva();
                        puerto = vista.getPuertoActivo();
                        if(this.enviarMensaje(contenido,ip,puerto)) 
                        {
                            usuario.buscarConversacion(ip, puerto).agregarMensaje(new Mensaje(contenido,true));
                            vista.agregarMensaje(contenido,true);
                        }
                        else{
                            VentanaError error = new VentanaError((JFrame)vista,true,"No se pudo enviar el mensaje");
                        }
                    }else{
                        VentanaError error = new VentanaError((JFrame)vista,true,"Por favor, escriba un mensaje para ser enviado");
                    }
                }else
                {
                    VentanaError error = new VentanaError((JFrame)vista,true,"Por favor, seleccione una conversacion para enviar mensajes");
                }
                break;
                           
            case "SOLICITUD AGREGAR CONVERSACION":
                ArrayList<Contacto> contactosSinConversacion = new ArrayList<>();
                ArrayList<Contacto> contactos = usuario.getAgenda().getContactos();
                for(Contacto contacto: contactos)
                {
                    conversacion = usuario.buscarConversacion(contacto.getIp(),contacto.getPuerto());
                    if(conversacion == null)
                        contactosSinConversacion.add(contacto);
                }
                if(contactosSinConversacion.size() != 0)
                    vista.abrirFormularioAgregarConversacion(contactosSinConversacion);
                else
                {
                    VentanaError error = new VentanaError((JFrame)vista,true,"Agregue mas contactos para iniciar nuevas conversaciones");
                }
                break;
                
            case "AGREGAR CONVERSACION":
                ip = vista.getIPConversacion();
                puerto = vista.getPuertoConversacion();
                Contacto contacto = usuario.getAgenda().obtenerContactoPorIpYPuerto(ip, puerto);
                usuario.iniciarConversacion(contacto);
                vista.setIPactiva(ip);
                vista.setPuertoActivo(puerto);
                vista.agregarConversacion(usuario.buscarConversacion(ip, puerto));
                vista.mostrarConversacion(null,contacto.getNickname(),ip,puerto);
                vista.cerrarFormularioAgregarConversacion();
                break;
                
                                
            case "SOLICITUD AGREGAR CONTACTO":
                vista.abrirFormularioAgregarContacto();
                break;
                
            case "AGENDAR CONTACTO":
                ip = vista.getIPContacto();
                nickname = vista.getNicknameContacto();
                puerto = vista.getPuertoContacto();
                if(usuario.getAgenda().obtenerContactoPorIpYPuerto(ip, puerto) == null){
                    contacto = new Contacto(nickname,ip,puerto);
                    this.vista.agregarContacto(contacto);
                    this.usuario.agregarContacto(contacto);
                    vista.cerrarFormularioAgregarContacto();
                }else{
                    VentanaError error = new VentanaError((JFrame)vista,true,"Contacto ya existente");
                }
                break;
                
            case "MOVER A AGENDA":
                agenda = this.usuario.getAgenda();
                vista.setSideBar(SideBar.AGENDA);
                vista.cargarContactos(agenda.getContactos());
                vista.disableBotonAgregarConversacion();
                vista.enableBotonAgregarContacto();
                break;
                
            case "MOVER A CONVERSACIONES":
                vista.setSideBar(SideBar.CONVERSACIONES);
                vista.cargarConversaciones(this.usuario.getConversaciones());
                vista.disableBotonAgregarContacto();
                vista.enableBotonAgregarConversacion();
                break;
                
            case "VER CONVERSACION":
                ip = vista.getIPactiva();
                puerto = vista.getPuertoActivo();
                conversacion = usuario.buscarConversacion(ip, puerto);
                conversacion.setNotificacion(false);
                contacto = conversacion.getContacto();
                vista.mostrarConversacion(conversacion.getMensajes(), contacto.getNickname(), contacto.getIp(), contacto.getPuerto());  
        }
    }
    
    /**
     * Registra usuario activo de la app con los datos recibidos por parametro.
     * @param nickname
     * @param ip
     * @param puerto 
     */
    private void registrar(String nickname,String ip,int puerto){
        if (this.iniciarServer(puerto)){
            usuario = new Usuario(nickname,ip,puerto);
            vista.cerrarFormularioRegistro();
            vista.hacerVisible(true);
        }
        else{
            VentanaError error = new VentanaError((JFrame)vista,true,"No fue posible iniciar el servidor en el puerto " + puerto);
        }
    }
    
    /**
     * Inicia el servidor en el puerto indicado, en caso de error informa.
     * @param puerto
     * @return 
     */
    private boolean iniciarServer(int puerto){
        this.servidor = new Servidor(puerto,instance);
        
        Thread hilo = new Thread(servidor);
        hilo.start();
        
        try {
            hilo.join(1000);
        } catch (InterruptedException ex) {
            return false;
        }
        return servidor.isConectado();
    }
    
    /**
     * Delega al servidor el envio de un mensaje. En caso de error, informa.
     * @param contenido
     * @param IP
     * @param puerto
     * @return 
     */
    public synchronized boolean enviarMensaje(String contenido, String IP, int puerto){
       MensajeRed msjRed = new MensajeRed(usuario.getNickname(),usuario.getIp(),usuario.getPuerto(),IP,puerto,contenido);
       if (servidor.enviarMensaje(msjRed)){
           Mensaje mensaje = new Mensaje(contenido,true);
           usuario.buscarConversacion(IP, puerto).agregarMensaje(mensaje);
           return true;
       }
       return false;
    }
    
    /**
     * Recibe un mensaje y actualiza la vista dependiendo si el mensaje pertenece o no al chat activo de la misma.
     * @param mensaje 
     */
    public synchronized void recibirMensaje(MensajeRed mensaje){
        Mensaje msj = new Mensaje(mensaje.getContenido(),false);
        Conversacion conversacion = usuario.buscarConversacion(mensaje.getMiIp(), mensaje.getMiPuerto());
        String ipActiva = vista.getIPactiva();
        int puertoActivo = vista.getPuertoActivo();
        Contacto contacto;
        
        if( conversacion == null)
        {
            contacto = new Contacto(mensaje.getMyNickname(),mensaje.getMiIp(),mensaje.getMiPuerto());
            conversacion = new Conversacion(new Contacto(mensaje.getMyNickname(),mensaje.getMiIp(),mensaje.getMiPuerto()));
            conversacion.agregarMensaje(msj);
            usuario.agregarConversacion(conversacion);
            usuario.agregarContacto(contacto);
            conversacion.setNotificacion(true);
            vista.notificar();
        }else
        {   
            conversacion.agregarMensaje(msj);
            if(mensaje.getMiIp().equals(ipActiva) && puertoActivo == mensaje.getMiPuerto())
            {
                 vista.agregarMensaje(mensaje.getContenido(),false);
            }
            else
            {
                 conversacion.setNotificacion(true);
                 vista.notificar();
            }

        }
    }
    
    private boolean sePuedeEnviarMensaje(String mensaje)
    {
        return (tieneCaracteres(mensaje) && !(mensaje.equals("Ingrese su mensaje aqui...") && !vista.isBarraDeMensajeClickeada()));
    }
    
    private boolean tieneCaracteres(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c != ' ' && c != '\n') {
                return true;
            }
        }
        return false;
    }
    private boolean hayChatSeleccionado()
    {
        return  vista.getIPactiva()!=null && vista.getPuertoActivo()!= -1;
    }
    
}



