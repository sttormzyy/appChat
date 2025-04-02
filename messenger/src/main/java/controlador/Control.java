/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import vista.VentanaPrincipal;
import modelo.Agenda;
import modelo.Contacto;
import modelo.Conversacion;
import modelo.Mensaje;
import modelo.MensajeRed;
import modelo.Usuario;
import red.Server;
import vista.FormularioAgregarContacto;
import vista.FormularioAgregarConversacion;
import vista.VentanaError;
import vista.VentanaPrincipal.SideBar;
import vista.VentanaRegistro;

public class Control implements ActionListener{
    private static Control instance = null;
    private Server servidor;
    private Usuario usuario;
    private VentanaPrincipal vista;
    private VentanaRegistro registro;
    private FormularioAgregarContacto agregarContacto;
    private FormularioAgregarConversacion agregarConversacion;
    private Control(){}
    
    public static Control getInstance(){      
        if(instance == null)
            instance = new Control();
        return instance;
    }
    
    public void setRegistro(VentanaRegistro registro)
    {
        this.registro = registro;
    }
   
    public void setVista(VentanaPrincipal vista)
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
                nickname = registro.getNickname();
                puerto = registro.getPuerto();
                this.registrar(nickname,"localhost",puerto);
                break;
                
            case "ENVIAR MENSAJE":
                String contenido = vista.getMensaje();
                if(sePuedeEnviarMensaje(contenido)){
                    ip = vista.getIPactiva();
                    puerto = vista.getPuertoActivo();
                    if(this.enviarMensaje(contenido,ip,puerto)) 
                    {
                        usuario.buscarConversacion(ip, puerto).agregarMensaje(new Mensaje(contenido,true));
                        vista.agregarMensaje(contenido,true);
                    }
                    else{
                        VentanaError error = new VentanaError(vista,true,"No se pudo enviar el mensaje");
                    }
                }else{
                    VentanaError error = new VentanaError(vista,true,"Por favor, seleccione o cree una conversacion para enviar mensaje");
                }
                break;
                           
            case "SOLICITUD AGREGAR CONVERSACION":
                agregarConversacion = new FormularioAgregarConversacion(vista, true, instance);
                ArrayList<Contacto> contactosSinConversacion = new ArrayList<>();
                ArrayList<Contacto> contactos = usuario.getAgenda().getContactos();
                
                for(Contacto contacto: contactos)
                {
                    conversacion = usuario.buscarConversacion(contacto.getIp(),contacto.getPuerto());
                    if(conversacion == null)
                        contactosSinConversacion.add(contacto);
                }
                agregarConversacion.agregarContactos(contactosSinConversacion);
                agregarConversacion.setVisible(true);
                break;
                
            case "SOLICITUD AGREGAR CONTACTO":
                agregarContacto = new FormularioAgregarContacto(vista, true, instance);
                agregarContacto.setVisible(true);
                break;
                
            case "AGREGAR CONVERSACION":
                ip = agregarConversacion.getIp();
                puerto = agregarConversacion.getPuerto();
                Contacto contacto = usuario.getAgenda().obtenerContactoPorIpYPuerto(ip, puerto);
                usuario.iniciarConversacion(contacto);
                vista.setIpActiva(ip);
                vista.setPuertoActivo(puerto);
                System.out.println("ip:" + ip + " puerto:" + puerto);
                vista.agregarConversacion(usuario.buscarConversacion(ip, puerto));
                vista.mostrarConversacion(null,contacto.getNickname(),ip,puerto);
                agregarConversacion.dispose();
                break;
                
            case "SUBMIT CONTACTO":
                ip = agregarContacto.getIp();
                nickname = agregarContacto.getNickname();
                puerto = agregarContacto.getPuerto();
                if(usuario.getAgenda().obtenerContactoPorIpYPuerto(ip, puerto) == null){
                    contacto = new Contacto(nickname,ip,puerto);
                    this.vista.agregarContacto(contacto);
                    this.usuario.agregarContacto(contacto);
                    agregarContacto.dispose();
                }else{
                    VentanaError error = new VentanaError(vista,true,"Contacto ya existente");
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
    
    private void registrar(String nickname,String ip,int puerto){
        if (this.iniciarServer(puerto)){
            usuario = new Usuario(nickname,ip,puerto);
            registro.dispose();
            vista.setVisible(true);
        }
        else{
            VentanaError error = new VentanaError(registro,true,"No fue posible iniciar el servidor en el puerto " + puerto);
        }
    }
    
    private boolean iniciarServer(int puerto){
        
         this.servidor = new Server(puerto,instance);
        
        Thread hilo = new Thread(servidor);
        hilo.start();
        
        try {
            hilo.join(1000);//espero 1 seg para comprobar si se conecto o no el servidor al puerto
        } catch (InterruptedException ex) {
            return false;
        }
        return servidor.isConectado();
    }
    
    public synchronized boolean enviarMensaje(String contenido, String IP, int puerto){
       MensajeRed msjRed = new MensajeRed(usuario.getNickname(),usuario.getIp(),usuario.getPuerto(),IP,puerto,contenido);
       if (servidor.enviarMensaje(msjRed)){
           Mensaje mensaje = new Mensaje(contenido,true);
           usuario.buscarConversacion(IP, puerto).agregarMensaje(mensaje);
           return true;
       }
       return false;
    }
    
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
            vista.notificar(mensaje.getContenido(),mensaje.getMiIp(),mensaje.getMiPuerto());
        }else
        {   
            conversacion.agregarMensaje(msj);
            if(mensaje.getMiIp().equals(ipActiva) && puertoActivo == mensaje.getMiPuerto())
            {
                 vista.agregarMensaje(mensaje.getContenido(),false);
                  System.out.println(" puertoAct "+puertoActivo+" puertoMensjae "+mensaje.getMiPuerto());
            }
            else
            {
                 conversacion.setNotificacion(true);
                 vista.notificar(mensaje.getContenido(),mensaje.getMiIp(),mensaje.getMiPuerto());
            }

        }
    }
    
    private boolean sePuedeEnviarMensaje(String mensaje)
    {
        return (!mensaje.equals("") && !(mensaje.equals("Ingrese un mensaje...") && !vista.isBarraDeMensajeClikeada()) ) 
                && vista.getIPactiva()!=null && vista.getPuertoActivo()!= -1;
    }
}



