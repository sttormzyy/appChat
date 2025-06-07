/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor;

import sincronizador.MensajeDeRed;
import sincronizador.Sincronizador;
import monitoreo.ComunicacionDirectorio;
import configuracion.IServidorListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static servidor.Constantes.*;

/**
 *
 * @author Usuario
 */
public class Servidor implements Runnable{
    private InfoServidor infoServidor;
    private boolean ejecutando = true;
    private ServerSocket serverSocket;
    private Sincronizador sincronizador;
    private ComunicacionDirectorio comunicacionDirectorio;
    private ActionListener controlador;
    
    private ArrayList<String> clientesRegistrados = new ArrayList<>();
    private HashMap<String, Integer> clientesActivosGlobales = new HashMap<String,Integer>();
    private HashMap<String, HiloServidor> clientesActivosLocales = new HashMap<>();
    private HashMap<String, ArrayList<MensajeDeRed>> mensajesPendientes = new HashMap<>();   

    private IServidorListener gui;
    
    public Servidor(InfoServidor infoServidor, Sincronizador sincronizador, ComunicacionDirectorio comunicacionDirectorio, ActionListener controlador)
    {
        this.infoServidor = infoServidor;
        this.sincronizador = sincronizador;
        this.comunicacionDirectorio = comunicacionDirectorio;
        this.controlador = controlador;
    }
  
    /**
     * Se encarga de enviar mensaje a un usuario. Los pasos que sigue son:
     *  1 - Si el destinatario esta activo en este servidor, se lo envia sin avisarle nada al resto de servidores
     *  2 - Si el destinatario esta activo pero en otro servidor, se le delega a ese servido el envio del mensaje
     *  3 - Si el destinatario no esta activo en ningun servidor, se guarda el mensaje como pendiente y se sincroniza al resto de servidores
     * @param msj 
     */
    public synchronized void enviarMensaje(MensajeDeRed msj) {
        String destino = msj.getNicknameDestino();
        HiloServidor socket = clientesActivosLocales.get(destino);

        if (socket != null) {
            socket.enviarMensaje(msj);
        } else {
            if(clientesActivosGlobales.containsKey(destino))
            {
                sincronizador.sincronizarMensaje(msj,clientesActivosGlobales.get(destino));
            }
            else
            {
                sincronizador.sincronizarMensajePendiente(msj);
                if (!mensajesPendientes.containsKey(destino)) {
                    mensajesPendientes.put(destino, new ArrayList<>());
                }
                mensajesPendientes.get(destino).add(msj);
                for(MensajeDeRed mesj: mensajesPendientes.get(destino))
                {
                    System.out.println(mesj.getContenido());
                }
                }
            }
    }   

    /**
     * Verifica que no haya ningun usuario activo o registrado con el nickname recibido por parametro
     * @param nickname
     * @return 
     */
    public synchronized String validarNickname(String nickname) {
        if (!clientesRegistrados.contains(nickname)) {
            return ESTADO_VERIFICADO;
        } else if (clientesActivosGlobales.containsKey(nickname) || clientesActivosLocales.containsKey(nickname)) {
            return YA_EXISTE_UNA_SESION_ACTIVA_CON_ESE_NICKNAME;
        } else {
            return ESTADO_VERIFICADO;
        }
    }

    /**
     * Agrega un nuevo usuario a la lista de clientes activos 
     * Si es la primera vez, lo agrega a la lista de registrados tambien
     * @param nickname
     * @param hiloServidor 
     */
    public synchronized void agregarClienteActivo(String nickname, HiloServidor hiloServidor) {
        if (!clientesRegistrados.contains(nickname)) {
            clientesRegistrados.add(nickname); 
            sincronizador.sincronizarUsuarioRegistrado(nickname);
            gui.informar("Nuevo cliente registrado: " + nickname);
        }
        clientesActivosLocales.put(nickname, hiloServidor);
        clientesActivosGlobales.put(nickname, this.infoServidor.getPuertoSincronizacion()); 
        sincronizador.sincronizarUsuarioActivo(nickname, true);
        comunicacionDirectorio.avisarUsuarioConectado(infoServidor.getIP(),infoServidor.getPuertoCliente());
        gui.informar("Nuevo cliente activo: " + nickname);
    }

    
    /**
     * Envia los mensajes pendientes a un usuario en especifico
     * @param nickname
     * @param hiloServidor 
     */
    public void enviarMensajesPendientes(String nickname, HiloServidor hiloServidor)
    {
        ArrayList<MensajeDeRed> msjPendientes = mensajesPendientes.get(nickname);

        if (msjPendientes != null && !msjPendientes.isEmpty()) {
            for (MensajeDeRed mensaje : new ArrayList<>(msjPendientes)) {
                hiloServidor.enviarMensaje(mensaje);
            }
            mensajesPendientes.remove(nickname);
            sincronizador.sincronizarYaSeEnvioMensajesPendientes(nickname);
        }
    }
    
    /**
     * Elimina un usuario de la lista de clientes activos
     * @param nickname 
     */
    public synchronized void eliminarClienteActivo(String nickname) {
        clientesActivosLocales.get(nickname).detenerHilo();
        clientesActivosLocales.remove(nickname);
        clientesActivosGlobales.remove(nickname);
        gui.informar(nickname + " se desconectó");
        sincronizador.sincronizarUsuarioActivo(nickname, false);
        
        comunicacionDirectorio.avisarUsuarioDesconectado(this.infoServidor.getIP(),this.infoServidor.getPuertoCliente());
        if (clientesActivosLocales.isEmpty()) {
            gui.informar("No hay más clientes activos localmente");
        }
    }
    
    
    /**
     * Agrega un usuario,indicado por el sincronizador, a lista de clientes registrados 
     * @param nickname 
     */
    public void agregarClienteRegistrado(String nickname)
    {
         if (!clientesRegistrados.contains(nickname))
            clientesRegistrados.add(nickname);
         gui.informar("Nuevo cliente registrado sincronizado: " + nickname);
    }
    
    /**
     * Elimina un usuario, indicado por el sincronizador, a lista de clientes activos globales 
     * @param nickname 
     */
    public void eliminarClienteActivoGlobal(String nickname)
    {
        clientesActivosGlobales.remove(nickname); 
        gui.informar(nickname + " se desconectó");
    }
    
    /**
     * Agrega un usuario,indicado por el sincronizador, a lista de clientes activos 
     * @param nickname
     * @param puertoSincronizacion 
     */
    public void agregarClienteActivoGlobal(String nickname, int puertoSincronizacion)
    {
        clientesActivosGlobales.put(nickname, puertoSincronizacion); 
        gui.informar("Nuevo cliente activo sincronizado: " + nickname);
    }
  
    /**
     * Agrega un mensaje pendiente,indicado por el sincronizador, a lista de mensajes pendientes de un usuario
     * @param nickname
     * @param mensaje 
     */
    public void agregarMensajePendiente(String nickname, MensajeDeRed mensaje) {
       if (!mensajesPendientes.containsKey(nickname)) {
            mensajesPendientes.put(nickname, new ArrayList<>());
        }
        mensajesPendientes.get(nickname).add(mensaje);
    }
    
    public void eliminarMensajesPendientes(String nickname)
    {
        mensajesPendientes.remove(nickname);
    }
   
    /**
     * Delega al sincronizador que sincronice toda la informacion actual del sistema a otro servidor
     * @param clientesRegistrados
     * @param clientesActivosGlobales
     * @param mensajesPendientes 
     */
    public void sincronizarTodo(ArrayList<String> clientesRegistrados, HashMap<String,Integer> clientesActivosGlobales, HashMap<String,ArrayList<MensajeDeRed>> mensajesPendientes)
    {
        System.out.println("INFO QUE SINCRONIZA");
        System.out.println("Clientes Registrados: " + clientesRegistrados);
        System.out.println("Clientes Activos Globales: " + clientesActivosGlobales);
        System.out.println("Mensajes Pendientes: " + mensajesPendientes);
        this.clientesRegistrados = clientesRegistrados;
        this.clientesActivosGlobales = clientesActivosGlobales;
        this.mensajesPendientes = mensajesPendientes;
        sincronizador.agregarServidor(infoServidor);
        comunicacionDirectorio.avisarEstoyListo();
    }

    /**
     * Escucha conexiones de clientes 
     * Crea un hilo para atender cada solicitud
     */
    public void run() {
        try {
            serverSocket = new ServerSocket(this.infoServidor.getPuertoCliente());

            while (ejecutando) {
                try {
                    Socket clienteSocket = serverSocket.accept();
                    System.out.println("Cliente conectado desde: " + clienteSocket.getInetAddress());
                    HiloServidor hilo = new HiloServidor(clienteSocket, this);
                    new Thread(hilo).start();
                } catch (IOException e) {
                    if (ejecutando) {
                        System.err.println("Error al aceptar conexión: " + e.getMessage());
                    }
                }
            }
        } catch (IOException ex) {
           controlador.actionPerformed(new ActionEvent("Servidor caido", 90, "COMPONENTE CAIDO"));
        }
    }
    
    public void detener() {
        ejecutando = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                 if(gui!=null) gui.informar("Servidor detenido y puerto liberado.");
            }
            for (Map.Entry<String, HiloServidor> entry : new HashMap<>(clientesActivosLocales).entrySet()) {
                String nickname = entry.getKey();
                HiloServidor hilo = entry.getValue();

                System.out.println("Hilo servidor " + nickname + " apagado");
                hilo.detenerHilo(); 
            }


        } catch (IOException e) {
            System.err.println("Error al cerrar el servidor: " + e.getMessage());
        }
    }
    
      
    public ArrayList<String> obtenerListaClientes() {
        return clientesRegistrados;
    }
    
    public String getIP()
    {
        return this.infoServidor.getIP();
    }
    
    public String getPuertoCliente()
    {
        return String.valueOf(this.infoServidor.getPuertoCliente());
    }
    
    public String getPuertoSincronizacion()
    {
        return String.valueOf(this.infoServidor.getPuertoSincronizacion());
    }
    
        
    public String getPuertoParaDirectorio()
    {
        return String.valueOf(this.infoServidor.getPuertoParaDirectorio());
    }
    
        
    public String getPuertoPing()
    {
        return String.valueOf(this.infoServidor.getPuertoPing());
    }

    public ArrayList<String> getClientesRegistrados() {
        return clientesRegistrados;
    }

    public HashMap<String, Integer> getClientesActivosGlobales() {
        return clientesActivosGlobales;
    }

    public HashMap<String, ArrayList<MensajeDeRed>> getMensajesPendientes() {
        return mensajesPendientes;
    }

    public boolean consultarCliente(String nickname)
    {
        return clientesActivosLocales.containsKey(nickname);
    }
   
    public void setGui(IServidorListener s)
    {
        this.gui =s;
        gui.informar("Servidor iniciado en el puerto " + this.infoServidor.getPuertoCliente());
    }
    
}