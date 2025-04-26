/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.JFrame;
import static resources.Constantes.*;
import modelo.*;
import red.*;
import vista.*; 
import vista.VentanaPrincipal.SideBar;

public class Control implements ActionListener,IReceptor{
    private static Control instance = null;
    private Usuario usuario;
    private IVista vista;
    private IEmisor emisor;
    
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
     * @param evento:
     *  "REGISTRO" 
     *  "SOLICITUD AGENDAR CONTACTO" | "AGENDAR CONTACTO" | "SOLICITUD EDITAR CONTACTO" | "EDITAR CONTACTO"
     *  "SOLICITUD AGREGAR CONVERSACION" | "AGREGAR CONVERSACION" | "VER CONVERSACION" | "ENVIAR MENSAJE"
     *  "MOVER A AGENDA" |  "MOVER A CONVERSACIONES" 
     */
    @Override
    public void actionPerformed(ActionEvent evento) {
        int puerto;
        String nicknameNuevo;
        Agenda agenda;
        Conversacion conversacion;
        
        switch(evento.getActionCommand()) {
            case "REGISTRO":
                nicknameNuevo = vista.getNicknameRegistro();
                this.registrar(nicknameNuevo,"localhost",777);
                emisor.pedirMensajesPendientes();
                break;
                
            case "ENVIAR MENSAJE":
                String contenido = vista.getMensaje();
                if(hayChatSeleccionado())
                {
                    if(sePuedeEnviarMensaje(contenido))
                    {
                        nicknameNuevo = vista.getNicknameActivo();
                        nicknameNuevo = usuario.getAgenda().obtenerContactoPorNicknameAgendado(nicknameNuevo).getNickname();
                        if(this.enviarMensaje(contenido,nicknameNuevo)) 
                        {
                            usuario.agregarMensaje(nicknameNuevo,new Mensaje(contenido,true));
                            vista.agregarMensaje(contenido,true,LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
                            
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
                ArrayList<String> contactosSinConversacion = new ArrayList<String>();
                ArrayList<Contacto> contactos = usuario.getAgenda().getContactos();
                for(Contacto contacto: contactos)
                {
                    conversacion = usuario.buscarConversacionNickReal(contacto.getNickname());
                    if(conversacion == null)
                        contactosSinConversacion.add(contacto.getNicknameAgendado());
                }
                if(!contactosSinConversacion.isEmpty())
                    vista.abrirFormularioAgregarConversacion(contactosSinConversacion);
                else
                {
                    VentanaError error = new VentanaError((JFrame)vista,true,"Agregue mas contactos para iniciar nuevas conversaciones");
                }
                break;
                
            case "AGREGAR CONVERSACION":
                nicknameNuevo = vista.getNicknameConversacion();
                Contacto contacto = usuario.getAgenda().obtenerContactoPorNicknameAgendado(nicknameNuevo);
                usuario.iniciarConversacion(contacto);
                vista.setNicknameActivo(nicknameNuevo);
                vista.agregarConversacion(usuario.buscarConversacionNickAgendado(nicknameNuevo));
                vista.mostrarConversacion(null,contacto.getNicknameAgendado());
                vista.cerrarFormularioAgregarConversacion();
                break;
                
                                
            case "SOLICITUD AGENDAR CONTACTO":
                emisor.pedirListaClientes();
                break;
                
            case "AGENDAR CONTACTO":
                nicknameNuevo = vista.getNicknameContacto(); 
                contacto = new Contacto(nicknameNuevo,nicknameNuevo);
                if(this.usuario.getAgenda().agregarContacto(contacto))
                {
                    this.vista.agregarContacto(contacto);
                    vista.cerrarFormularioAgregarContacto();
                    vista.abrirFormularioEditarContacto(nicknameNuevo);
                }else
                {
                    VentanaError error = new VentanaError((JFrame)vista,true,"Contacto ya existente");
                }
                break;
                
            case "SOLICITUD EDITAR CONTACTO":
                vista.abrirFormularioEditarContacto((String)evento.getSource()); //evento.getSource() devuelve el nicknameNuevo del contacto a editar
                break;
                
            case "EDITAR CONTACTO":
                String nicknameActivo = vista.getNicknameActivo();
                String nicknameActual = vista.getNicknameContactoActual();
                nicknameNuevo = vista.getNicknameContactoEditado();
                contacto = usuario.getAgenda().obtenerContactoPorNicknameAgendado(nicknameNuevo);
                if(contacto == null || nicknameNuevo.equals(contacto.getNicknameAgendado()))
                {
                    contacto = usuario.getAgenda().obtenerContactoPorNicknameAgendado(nicknameActual);
                    contacto.setNicknameAgendado(nicknameNuevo);
                    if(nicknameActivo != null && nicknameActivo.equals(nicknameActual))
                        vista.setNicknameActivo(nicknameNuevo);
                    vista.actualizarContacto(nicknameActual, contacto);
                    vista.cerrarFormularioEditarContacto();
                }
                else
                {
                    VentanaError error = new VentanaError((JFrame)vista,true,"Ya tiene un contacto agendado con ese nickname");
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
                nicknameNuevo = (String) evento.getSource(); //evento.geSource() devuelve el nicknameNuevo del contacto de la conversacion que quiere acceder el usuario
                vista.setNicknameActivo(nicknameNuevo);
                conversacion = usuario.buscarConversacionNickAgendado(nicknameNuevo);
                conversacion.setNotificacion(false);
                contacto = conversacion.getContacto();
                vista.mostrarConversacion(conversacion.getMensajes(), contacto.getNicknameAgendado());  
        }
    }
    
    /**
     * Registra usuario activo de la app con los datos recibidos por parametro.
     * @param nickname
     * @param ip
     * @param puerto 
     */
    private void registrar(String nickname, String ip, int puerto){
    	String estado = this.iniciarEmisor(nickname);
        if (estado.equals(ESTADO_VERIFICADO))
        {
            usuario = new Usuario(nickname,ip,puerto);
            vista.cerrarFormularioRegistro();
            vista.setNicknameUsuario(nickname);
            vista.hacerVisible(true);
        }
        else{
            VentanaError error = new VentanaError((JFrame)vista,true,estado);
        }
    }
    
    /**
     * Inicia el componente de red, en caso de error informa.
     * @return 
     */
    private String iniciarEmisor(String nickname){
        this.emisor= new Cliente(nickname,this);
        Thread hilo = new Thread((Runnable)emisor);
        hilo.start();
        try {
            hilo.join(1000);
        } catch (InterruptedException ex) {
            return "FALLO POR INTERRUMPCION";
        }
        return emisor.getEstado();
    }
    
    /**
     * Delega al componente de red el envio de un mensaje. En caso de error, informa.
     * @param contenido
     * @param nicknameDestino nicknameNuevo del contacto al que se envia el mensaje
     * @return true si puedo enviar el mensaje, false caso contrario
     */
    public synchronized boolean enviarMensaje(String contenido, String nicknameDestino){
       MensajeDeRed msjRed = new MensajeDeRed(usuario.getNickname(),nicknameDestino, contenido, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
       return emisor.enviarMensaje(msjRed);
    }
    
    /**
     * Recibe un mensaje y actualiza la vista dependiendo si el mensaje pertenece o no al chat activo de la misma.
     * @param mensaje 
     */
    public synchronized void recibirMensaje(MensajeDeRed mensaje){
        Mensaje msj = new Mensaje(mensaje.getContenido(), false, mensaje.getHoraEnvio());
        Conversacion conversacion = usuario.buscarConversacionNickReal(mensaje.getNicknameOrigen());
        String nicknameActivo = vista.getNicknameActivo();
        Contacto contacto;
        String hora = mensaje.getHoraEnvio().substring(11,16);
        
        if( conversacion == null)
        {
            contacto = new Contacto(mensaje.getNicknameOrigen(),mensaje.getNicknameOrigen());
            conversacion = new Conversacion(contacto);
            conversacion.agregarMensaje(msj);
            usuario.agregarConversacion(conversacion);
            usuario.getAgenda().agregarContacto(contacto);
            conversacion.setNotificacion(true);
            vista.notificar();
        }else
        {   
            contacto = usuario.getAgenda().obtenerContactoPorNicknameReal(mensaje.getNicknameOrigen());
            conversacion.agregarMensaje(msj);
            if(contacto.getNicknameAgendado().equals(nicknameActivo))
            {
                 vista.agregarMensaje(mensaje.getContenido(),false,hora);
            }
            else
            {
                 conversacion.setNotificacion(true);
                 vista.notificar();
            }

        }
    }
    
    /**
     * Recibe la lista de clientes presentes en el directorio del servidor y hace visible el formulario para agendar contacto
     * @param clientesServidor lista de clientes presentes en el directorio del servidor
     */
    public void recibirListaClientes(ArrayList<String> clientesServidor)
    {
        clientesServidor.remove(usuario.getNickname());
        vista.abrirFormularioAgregarContacto(clientesServidor);
    }
    
    private boolean sePuedeEnviarMensaje(String mensaje)
    {
        return (!mensaje.equals("") && !(mensaje.equals("Ingrese su mensaje aqui...") && !vista.isBarraDeMensajeClickeada()));
    }
    
    private boolean hayChatSeleccionado()
    {
        return  vista.getNicknameActivo()!=null;
    }
}



