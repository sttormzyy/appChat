/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
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
    
    private HashMap<String, Integer> clientesRegistrados = new HashMap<String,Integer>();
    private HashMap<String, Integer> clientesActivosGlobales = new HashMap<String,Integer>();
    private HashMap<String, HiloServidor> clientesActivosLocales = new HashMap<>();
    private HashMap<String, ArrayList<MensajeDeRed>> mensajesPendientes = new HashMap<>();   

    
    public Servidor(InfoServidor infoServidor, Sincronizador sincronizador, ComunicacionDirectorio comunicacionDirectorio)
    {
        this.infoServidor = infoServidor;
        this.sincronizador = sincronizador;
        this.comunicacionDirectorio = comunicacionDirectorio;
    }
    
    /*
    public void iniciarServidor() throws IOException 
    {
        serverSocket = new ServerSocket(this.infoServidor.getPuertoCliente());
        System.out.println("Servidor iniciado en el puerto " + this.infoServidor.getPuertoCliente());
        while (ejecutando) 
        {
            try 
            {
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
    }
    
    
    public void detenerServidor() {
        ejecutando = false;
        try 
        {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Servidor detenido y puerto liberado.");
        }
        } catch (IOException e) {
            System.err.println("Error al cerrar el servidor: " + e.getMessage());
        }
    }
    */
    
    public synchronized void enviarMensaje(MensajeDeRed msj) {
        String destino = msj.getNicknameDestino();
        HiloServidor socket = clientesActivosLocales.get(destino);

        if (socket != null) {
            socket.enviarMensaje(msj);
        } else {
            if (!mensajesPendientes.containsKey(destino)) {
                mensajesPendientes.put(destino, new ArrayList<>());
            }
            mensajesPendientes.get(destino).add(msj);
        }
    }   

    public synchronized String validarNickname(String nickname) {
        if (!clientesRegistrados.containsKey(nickname)) {
            sincronizador.sincronizarUsuarioRegistrado(nickname);
            return ESTADO_VERIFICADO;
        } else if (clientesActivosGlobales.containsKey(nickname)) {
            return YA_EXISTE_UNA_SESION_ACTIVA_CON_ESE_NICKNAME;
        } else {
            sincronizador.sincronizarUsuarioRegistrado(nickname);
            return ESTADO_VERIFICADO;
        }
    }

    public synchronized void agregarClienteActivo(String nickname, HiloServidor hiloServidor) {
        if (!clientesRegistrados.containsKey(nickname)) {
            clientesRegistrados.put(nickname, this.infoServidor.getPuertoSincronizacion()); 
            sincronizador.sincronizarUsuarioRegistrado(nickname);
            System.out.println("Nuevo cliente registrado: " + nickname);
        }
        clientesActivosLocales.put(nickname, hiloServidor);
        clientesActivosGlobales.put(nickname, this.infoServidor.getPuertoSincronizacion()); 
        sincronizador.sincronizarUsuarioActivo(nickname, true);
        System.out.println("Nuevo cliente activo: " + nickname);
    }

    
    public void enviarMensajesPendientes(String nickname, HiloServidor hiloServidor)
    {
        ArrayList<MensajeDeRed> msjPendientes = mensajesPendientes.get(nickname);
        if (msjPendientes != null) 
        {
            while(!msjPendientes.isEmpty()) 
            {
                hiloServidor.enviarMensaje(msjPendientes.get(0));
                msjPendientes.remove(0);
            }
            mensajesPendientes.remove(nickname);
        }
    }

    public synchronized void eliminarClienteActivo(String nickname) {
        clientesActivosLocales.remove(nickname);
        System.out.println(nickname + " se desconectó");
        sincronizador.sincronizarUsuarioActivo(nickname, false);
        comunicacionDirectorio.avisarUsuarioDesconectado(this.infoServidor.getPuertoCliente());
        if (clientesActivosLocales.isEmpty()) {
            System.out.println("No hay más clientes activos localmente. Cerrando servidor...");
        }
    }
    
    
  // METODOS QUE LLAMA SINCRONIZADOR
    public void agregarClienteRegistrado(String nickname, int puertoSincronizacion)
    {
        clientesRegistrados.put(nickname,puertoSincronizacion);
        System.out.println("Nuevo cliente registrado: " + nickname);
    }
    
    public void eliminarClienteActivoGlobal(String nickname)
    {
        clientesActivosGlobales.remove(nickname); 
    }
    
    public void agregarClienteActivoGlobal(String nickname, int puertoSincronizacion)
    {
        clientesActivosGlobales.put(nickname, puertoSincronizacion); 
        System.out.println("Nuevo cliente activo: " + nickname);
    }
  
    public void agregarMensajePendiente(String nickname, MensajeDeRed mensaje) {
       mensajesPendientes.computeIfAbsent(nickname, k -> new ArrayList<>());
       mensajesPendientes.get(nickname).add(mensaje);
   }
    
    public void sincronizarTodo(HashMap<String,Integer> clientesRegistrados,HashMap<String,Integer> clientesActivosGlobales,HashMap<String,ArrayList<MensajeDeRed>> mensajesPendientes)
    {
        this.clientesRegistrados = clientesRegistrados;
        this.clientesActivosGlobales = clientesActivosGlobales;
        this.mensajesPendientes = mensajesPendientes;
        comunicacionDirectorio.avisarEstoyListo();
    }

    // FIN METODOS QUE LLAMA SINCRONIZADOR
    
    public ArrayList<String> obtenerListaClientes() {
        return new ArrayList<>(clientesRegistrados.keySet());
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

    public HashMap<String, Integer> getClientesRegistrados() {
        return clientesRegistrados;
    }

    public HashMap<String, Integer> getClientesActivosGlobales() {
        return clientesActivosGlobales;
    }

    public HashMap<String, ArrayList<MensajeDeRed>> getMensajesPendientes() {
        return mensajesPendientes;
    }

   
    public void run() {
        try {
            serverSocket = new ServerSocket(this.infoServidor.getPuertoCliente());
            System.out.println("Servidor iniciado en el puerto " + this.infoServidor.getPuertoCliente());

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
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void detenerServidor() {
        ejecutando = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Servidor detenido y puerto liberado.");
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar el servidor: " + e.getMessage());
        }
    }
    
}