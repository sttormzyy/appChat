/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import gui.VentanaError;
import gui.IVista;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.JFrame;
import static resources.Constantes.*;
import modelo.*;
import red.*;
import gui.VentanaPrincipal.SideBar;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.IPersistencia;
import persistencia.JSON.JSONFactory;
import persistencia.Persistencia;
import persistencia.TextoPlano.TextoPlanoFactory;
import persistencia.XML.XMLFactory;
import encriptacion.Encriptador;


public class Control implements ActionListener,IReceptor{
    private static Control instance = null;
    private Usuario usuario;
    private IVista vista;
    private IEmisor emisor;
    private IPersistencia persistencia;
    private Encriptador encriptador;
    private String metodoEncriptacion;
    
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
        String nickname, ip, claveEncriptacion, tipoArchivo;
        Conversacion conversacion;
        int puerto;
        
        switch(evento.getActionCommand()) {
            case "REGISTRO":
                nickname = vista.getNicknameRegistro();
                ip = vista.getIPRegistro();
                puerto = vista.getPuertoRegistro();
                claveEncriptacion = vista.getClaveEncriptacion();
                metodoEncriptacion = vista.getMetodoEncriptacion();
                tipoArchivo = getTipoArchivo(nickname);
                
                
                encriptador = new Encriptador(metodoEncriptacion, claveEncriptacion);
                this.usuario = new Usuario(nickname, ip, puerto);

                if(tipoArchivo == null)
                {
                   vista.abrirFormularioPersistencia();
                   this.registrar(nickname, ip, puerto); 
                }
                else
                   this.recuperarUsuario(nickname, ip, puerto, tipoArchivo);
                
                break;
                
            case "ENVIAR MENSAJE":
                String contenido = vista.getMensaje();
                if(hayChatSeleccionado())
                {
                    if(sePuedeEnviarMensaje(contenido))
                    {
                        nickname = vista.getNicknameRealActivo();
                        if(this.enviarMensaje(contenido,nickname)) 
                        {
                            usuario.agregarMensaje(nickname,contenido,true);
                            vista.agregarMensaje(contenido,true,LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
                            vista.actualizarConversacion(nickname, contenido); 
                        }
                        else{
                             new VentanaError((JFrame)vista,true,"No se pudo enviar el mensaje");
                        }
                    }else{
                        new VentanaError((JFrame)vista,true,"Por favor, escriba un mensaje para ser enviado");
                    }
                }else
                {
                    new VentanaError((JFrame)vista,true,"Por favor, seleccione una conversacion para enviar mensajes");
                }
                break;
                           
            case "SOLICITUD AGREGAR CONVERSACION":
                ArrayList<String> contactosSinConversacion = new ArrayList<String>();
                ArrayList<Contacto> contactos = usuario.getContactos();
                for(Contacto contacto: contactos)
                {
                    conversacion = usuario.buscarConversacionPorNickname(contacto.getNicknameReal());
                    if(conversacion == null)
                    {
                        contactosSinConversacion.add(contacto.getNicknameAgendado());
                        contactosSinConversacion.add(contacto.getNicknameReal());
                    }
                }
                if(!contactosSinConversacion.isEmpty())
                    vista.abrirFormularioAgregarConversacion(contactosSinConversacion);
                else
                {
                    new VentanaError((JFrame)vista,true,"Agregue mas contactos para iniciar nuevas conversaciones");
                }
                break;
                
            case "AGREGAR CONVERSACION":
                nickname = vista.getNicknameConversacion();
                Contacto contacto = usuario.obtenerContactoPorNickname(nickname);
                usuario.iniciarConversacion(contacto);
                vista.setNicknameRealActivo(nickname);
                vista.agregarConversacion(usuario.buscarConversacionPorNickname(nickname));
                vista.mostrarConversacion(null,contacto.getNicknameAgendado());
                vista.cerrarFormularioAgregarConversacion();
                break;
                
                                
            case "SOLICITUD AGENDAR CONTACTO":
                emisor.pedirListaClientes();
                break;
                
            case "AGENDAR CONTACTO":
                nickname = vista.getNicknameContacto(); 
                if(this.usuario.agregarContacto(nickname))
                {
                    this.vista.agregarContacto(usuario.obtenerContactoPorNickname(nickname));
                    vista.cerrarFormularioAgregarContacto();
                }else
                {
                    new VentanaError((JFrame)vista,true,"Contacto ya existente");
                }
                break;
                
            case "SOLICITUD EDITAR CONTACTO":
                vista.abrirFormularioEditarContacto((String)evento.getSource()); //evento.getSource() devuelve el nickname real del contacto a editar
                break;
                
            case "EDITAR CONTACTO":
                String nicknameConversacionActiva = vista.getNicknameRealActivo();
                String nicknameActual = vista.getNicknameContactoActual();
                contacto = usuario.obtenerContactoPorNickname(nicknameActual);
                String nicknameNuevo = vista.getNicknameContactoEditado();
                usuario.actualizarContacto(nicknameActual, nicknameNuevo);
                if(nicknameConversacionActiva != null && nicknameConversacionActiva.equals(nicknameActual))
                        vista.setNicknameConversacionActiva(nicknameNuevo);
                vista.actualizarContacto(nicknameActual, contacto);
                vista.cerrarFormularioEditarContacto();
                break;
                
            case "MOVER A AGENDA":
                vista.setSideBar(SideBar.AGENDA);
                vista.cargarContactos(usuario.getContactos());
                break;
                
            case "MOVER A CONVERSACIONES":
                vista.setSideBar(SideBar.CONVERSACIONES);
                vista.cargarConversaciones(usuario.getConversaciones());
                break;
                
            case "VER CONVERSACION":
                nickname = (String) evento.getSource(); //evento.geSource() devuelve el nickname real del contacto de la conversacion que quiere acceder el usuario
                vista.setNicknameRealActivo(nickname);
                conversacion = usuario.buscarConversacionPorNickname(nickname);
                conversacion.setNotificacion(false);
                contacto = conversacion.getContacto();
                vista.mostrarConversacion(conversacion.getMensajes(), contacto.getNicknameAgendado());  
                break;
            
            case "ELEGIR PERSISTENCIA":
                tipoArchivo = (String) evento.getSource(); //evento.geSource() devuelve el tipo de persistencia elegido por el usuario
                asignarPersistencia(tipoArchivo, usuario.getNickname());
                break;
                
            case "CIERRE":
                emisor.detener();
                if(persistencia != null) persistencia.persistir(usuario);
                break;
            
        }
    }
    
    /**
     * Registra usuario activo de la app con los datos recibidos por parametro.
     * @param nickname
     * @param ip
     * @param puerto 
     */
    private void registrar(String nickname, String ipDirectorio, int puertoDirectorio){
    	String estado = this.iniciarEmisor(nickname, ipDirectorio, puertoDirectorio);
        if (estado.equals(ESTADO_VERIFICADO))
        {
            emisor.pedirMensajesPendientes();
            vista.cerrarFormularioRegistro();
            vista.setNicknameUsuario(nickname);
            vista.hacerVisible(true);
        }
        else{
            if(estado.equals(YA_EXISTE_UNA_SESION_ACTIVA_CON_ESE_NICKNAME))
            {
                VentanaError error = new VentanaError((JFrame)vista,true,estado);
            }
         }
    }
    
    /**
     * Inicia sesion con usuario de la app a partir de los datos recibidos por parametro.
     * Ademas despersiste sus conversacioens y agenda
     * @param nickname
     * @param ipDirectorio
     * @param puertoDirectorio
     * @param tipoArchivo 
     */
    private void recuperarUsuario(String nickname, String ipDirectorio, int puertoDirectorio,String tipoArchivo){
    	String estado = this.iniciarEmisor(nickname, ipDirectorio, puertoDirectorio);
        if (estado.equals(ESTADO_VERIFICADO))
        {
            asignarPersistencia(tipoArchivo,nickname);
            persistencia.despersistir(usuario);
            emisor.pedirMensajesPendientes();
            vista.cerrarFormularioRegistro();
            vista.setNicknameUsuario(nickname);
            vista.hacerVisible(true);
        }
        else{
            if(estado.equals(YA_EXISTE_UNA_SESION_ACTIVA_CON_ESE_NICKNAME))
            {
                VentanaError error = new VentanaError((JFrame)vista,true,estado);
            }
         }
    }
    
    /**
     * Inicia el componente de red, en caso de error informa.
     * @return 
     */
    private String iniciarEmisor(String nickname, String ipDirectorio, int puertoDirectorio){
        this.emisor = new ComunicacionServidor(nickname, this, ipDirectorio, puertoDirectorio);
        Thread hilo = new Thread((Runnable)emisor);
        hilo.start();
        try {
            hilo.join(1000);
        } catch (InterruptedException ex) {
            return "FALLO POR INTERRUPCION";
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
       String contenidoEncriptado;
        try {
            contenidoEncriptado = encriptador.encriptar(contenido);
            MensajeDeRed msjRed = new MensajeDeRed(usuario.getNickname(),nicknameDestino, contenidoEncriptado, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), metodoEncriptacion);
            return emisor.enviarMensaje(msjRed);
        } catch (Exception ex) {
            Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
        }
       return false;
    }
    
    /**
     * Recibe un mensaje y actualiza la vista dependiendo si el mensaje pertenece o no al chat activo de la misma.
     * @param mensaje 
     */
    public synchronized void recibirMensaje(MensajeDeRed mensaje){
        Conversacion conversacion = usuario.buscarConversacionPorNickname(mensaje.getNicknameOrigen());
        String nicknameConversacionActiva = vista.getNicknameRealActivo();
        String nicknameNuevoContacto = mensaje.getNicknameOrigen();
        Contacto contacto;
        String hora = "";
        String horaEnvio = mensaje.getHoraEnvio();
        String contenidoDesencriptado = "";
        System.out.println("MENSAJE ENCRIPTADO: "+mensaje.getContenido());
        
        try {
            contenidoDesencriptado = encriptador.desencriptar(mensaje.getContenido(),mensaje.getMetodoEncriptacion());
            System.out.println("MENSAJE DESENCRIPTADO: "+contenidoDesencriptado);
        } catch (Exception ex) {
            Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (horaEnvio != null && horaEnvio.length() >= 16) {
            hora = horaEnvio.substring(11, 16);
        }

        
        if( conversacion == null)
        {
            usuario.agregarContacto(nicknameNuevoContacto);
            contacto = usuario.obtenerContactoPorNickname(nicknameNuevoContacto);
            usuario.iniciarConversacion(contacto);
            conversacion = usuario.buscarConversacionPorNickname(nicknameNuevoContacto);
            conversacion.agregarMensaje(contenidoDesencriptado,false,mensaje.getHoraEnvio());
            usuario.buscarConversacionPorNickname(nicknameNuevoContacto).setNotificacion(true);
            vista.notificar();
        }else
        {   
            contacto = usuario.obtenerContactoPorNickname(mensaje.getNicknameOrigen());
            conversacion.agregarMensaje(contenidoDesencriptado,false,mensaje.getHoraEnvio());
            if(contacto.getNicknameReal().equals(nicknameConversacionActiva))
            {
                 vista.agregarMensaje(contenidoDesencriptado,false,hora);
                 vista.actualizarConversacion(nicknameConversacionActiva, contenidoDesencriptado);
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
    
    public void detener()
    {
        vista.cerrarVentana();
        if(persistencia != null) persistencia.persistir(usuario);
    }
    
    private boolean sePuedeEnviarMensaje(String mensaje)
    {
        return (!mensaje.equals("") && !(mensaje.equals("Ingrese su mensaje aqui...") && !vista.isBarraDeMensajeClickeada()));
    }
    
    private boolean hayChatSeleccionado()
    {
        return  vista.getNicknameRealActivo()!=null;
    }
    
    private String getTipoArchivo(String nickname) {
        String carpeta = "Persistencia" + File.separator;

        if ((new File(carpeta + nickname + "_agenda" + XML)).exists())
            return XML;
        else if ((new File(carpeta + nickname + "_agenda" + JSON)).exists())
            return JSON;
        else if ((new File(carpeta + nickname + "_agenda" + TEXTO_PLANO)).exists())
            return TEXTO_PLANO;
        else
            return null;
    }


    public void asignarPersistencia(String tipoArchivo, String nickname)
    {
        switch(tipoArchivo) {
            case XML: 
                persistencia = new Persistencia(new XMLFactory(), nickname);
                break;
            case JSON: 
                persistencia = new Persistencia(new JSONFactory(), nickname);
                break;
            case TEXTO_PLANO: 
                persistencia = new Persistencia(new TextoPlanoFactory(), nickname);
                break;
        }
    }
}



