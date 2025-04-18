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
import modelo.Usuario;
import red.Cliente;
import red.MensajeDeRed;
import vista.IVista;
import vista.VentanaError;
import vista.VentanaPrincipal.SideBar;

public class Control implements ActionListener{
    private static Control instance = null;
    private Usuario usuario;
    private IVista vista;
    private Cliente cliente;
    
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
                if(hayChatSeleccionado())
                {
                    if(sePuedeEnviarMensaje(contenido)){
                        nickname = vista.getNicknameActivo();
                        if(this.enviarMensaje(contenido,nickname)) 
                        {
                            usuario.buscarConversacion(nickname).agregarMensaje(new Mensaje(contenido,true));
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
                    conversacion = usuario.buscarConversacion(nickname);
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
                nickname = vista.getNicknameConversacion();
                Contacto contacto = usuario.getAgenda().obtenerContactoPorNickname(nickname);
                usuario.iniciarConversacion(contacto);
                vista.setNicknameActivo(nickname);
                vista.agregarConversacion(usuario.buscarConversacion(nickname));
                vista.mostrarConversacion(null,contacto.getNickname(),nickname);
                vista.cerrarFormularioAgregarConversacion();
                break;
                
                                
            case "SOLICITUD AGREGAR CONTACTO":
                vista.abrirFormularioAgregarContacto(cliente.pedirListaClientes());
                break;
                
            case "AGENDAR CONTACTO":
                nickname = vista.getNicknameContacto(); // nickname con el que agendo
                String nicknameReal = vista.getNicknameSeleccionado();
                
                if(usuario.getAgenda().obtenerContactoPorNickname(nickname) == null){
                    contacto = new Contacto(nickname,nicknameReal);
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
                String parametros = (String) evento.getSource();
                String[] partes = parametros.split(":"); 
                nickname = partes[0];
                vista.setNicknameActivo(nickname);
                conversacion = usuario.buscarConversacion(nickname);
                conversacion.setNotificacion(false);
                contacto = conversacion.getContacto();
                vista.mostrarConversacion(conversacion.getMensajes(), contacto.getNickname());  
        }
    }
    
    /**
     * Registra usuario activo de la app con los datos recibidos por parametro.
     * @param nickname
     * @param ip
     * @param puerto 
     */
    private void registrar(String nickname,String ip,int puerto){
    	String estado = this.iniciarCliente(nickname,this);
        if (estado.equals("VERIFICADO")){
            usuario = new Usuario(nickname,ip,puerto);
            vista.cerrarFormularioRegistro();
            vista.hacerVisible(true);
        }
        else{
            VentanaError error = new VentanaError((JFrame)vista,true,estado);
        }
    }
    
    /**
     * Inicia el servidor en el puerto indicado, en caso de error informa.
     * @param puerto
     * @return 
     */
    private String iniciarCliente(String nickname,Control controlador){
        this.cliente= new Cliente(nickname,controlador);
        Thread hilo = new Thread(cliente);
        hilo.start();
        try {
            hilo.join(1000);
        } catch (InterruptedException ex) {
            return "FALLO POR INTERRUMPCION";
        }
        return cliente.getEstado();
    }
    
    /**
     * Delega al servidor el envio de un mensaje. En caso de error, informa.
     * @param contenido
     * @param IP
     * @param puerto
     * @return 
     */
    public synchronized void enviarMensaje(String contenido, String nickname){
       MensajeDeRed msjRed = new MensajeDeRed(usuario.getNickname(),nickname,contenido);
       cliente.enviarMensaje(msjRed);
    }
    
    /**
     * Recibe un mensaje y actualiza la vista dependiendo si el mensaje pertenece o no al chat activo de la misma.
     * @param mensaje 
     */
    public synchronized void recibirMensaje(MensajeDeRed mensaje){
        Mensaje msj = new Mensaje(mensaje.getContenido(),false);
        Conversacion conversacion = usuario.buscarConversacion(mensaje.getNicknameOrigen());
        String nicknameActivo = vista.getNicknameActivo();
        Contacto contacto;
        
        if( conversacion == null)
        {
            contacto = new Contacto(mensaje.getNicknameOrigen(),mensaje.getNicknameOrigen());
            conversacion = new Conversacion(contacto);
            conversacion.agregarMensaje(msj);
            usuario.agregarConversacion(conversacion);
            usuario.agregarContacto(contacto);
            conversacion.setNotificacion(true);
            vista.notificar();
        }else
        {   
            conversacion.agregarMensaje(msj);
            if(mensaje.getNicknameOrigen().equals(nicknameActivo))
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
        return (!mensaje.equals("") && !(mensaje.equals("Ingrese su mensaje aqui...") && !vista.isBarraDeMensajeClickeada()));
    }
    
    private boolean hayChatSeleccionado()
    {
        return  vista.getNicknameActivo()!=null;
    }
}



