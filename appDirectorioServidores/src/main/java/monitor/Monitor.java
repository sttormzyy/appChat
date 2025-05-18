/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monitor;

import directorio.Directorio;
import directorio.InfoServidor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class Monitor implements Runnable{
    private ServerSocket monitor;
    private int puerto;
    private Directorio directorio;
    private HashMap<InfoServidor,Socket> conexiones = new HashMap();
    
    private class HiloConexionServidor implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        private HiloConexionServidor(Socket socket) {
            this.socket = socket;
            try {
                this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                System.out.println("Error al crear el socket: " + e.getMessage());
            }
        }

        private void conectar() {
            try {
                while (!socket.isClosed()) {
                    String comando = in.readLine();
                    if (comando == null) {
                        break; // Desconexión
                    }
                    String ip;
                    int puerto;
                    switch (comando) {
                        case "USUARIO DESCONECTADO":
                            ip = in.readLine();
                            puerto = Integer.parseInt(in.readLine());
                            directorio.disminuirContadorEnUno(ip, puerto);
                            break;
                        case "USUARIO CONECTADO":
                            ip = in.readLine();
                            puerto = Integer.parseInt(in.readLine());
                            directorio.incrementarContadorEnUno(ip, puerto);
                            break; 
                            
                        // Agregar más comandos si es necesario
                    }
                }
            } catch (IOException e) {
                System.out.println("Error en la comunicación: " + e.getMessage());
            } finally {
                try {
                    socket.close(); // Asegurarse de cerrar el socket
                } catch (IOException e) {
                    System.out.println("Error al cerrar el socket: " + e.getMessage());
                }
            }
        }

        @Override
        public void run() {
            conectar();
        }
    }

    
    public Monitor(Directorio directorio,int puerto){
        this.puerto = puerto;
        this.directorio = directorio;
    }
    
    public void iniciarMonitor(){
        try{
        new Thread(new Ping(directorio, this)).start();
        monitor = new ServerSocket(this.puerto);
        BufferedReader in;
        PrintWriter out;
        System.out.println("Monitor iniciado en el puerto " + puerto);
        while (true) 
        {
            Socket servidorSocket = monitor.accept();
            try{
                in = new BufferedReader(new InputStreamReader(servidorSocket.getInputStream()));
                out = new PrintWriter(servidorSocket.getOutputStream(), true);
                System.out.println("Servidor conectado a monitor desde: " + servidorSocket.getInetAddress());
                String ipServidor;
                int puertoServidorParaClientes,puertoSincronizacion, puertoParaDirectorio, puertoPing ;
                InfoServidor servidor;
                
                String comando = in.readLine();
                switch (comando){
                    case "REGISTRO":
                        ipServidor = in.readLine();
                        puertoServidorParaClientes = Integer.parseInt(in.readLine());
                        puertoSincronizacion = Integer.parseInt(in.readLine());
                        puertoParaDirectorio = Integer.parseInt(in.readLine());
                        puertoPing = Integer.parseInt(in.readLine());
  
                        if (directorio.getServidores().size() >= 1) {
                            out.println("FRANCIA");
                            servidor = new InfoServidor(ipServidor,puertoServidorParaClientes,puertoSincronizacion,puertoParaDirectorio,puertoPing, false);
                            sincronizacionTotal(servidor);
                        } else {
                            out.println("PRIMERO");
                            servidor = new InfoServidor(ipServidor,puertoServidorParaClientes,puertoSincronizacion,puertoParaDirectorio,puertoPing, true);
                            enlistarServidor(servidorSocket, servidor);
                        }
                        break;
                    case "LISTO":
                        ipServidor = in.readLine();
                        puertoServidorParaClientes = Integer.parseInt(in.readLine());                
                        puertoSincronizacion = Integer.parseInt(in.readLine());
                        puertoParaDirectorio = Integer.parseInt(in.readLine());
                        puertoPing = Integer.parseInt(in.readLine());
                        servidor = new InfoServidor(ipServidor,puertoServidorParaClientes,puertoSincronizacion,puertoParaDirectorio,puertoPing, true);
                        enlistarServidor(servidorSocket, servidor);
                        break;
                }
            } catch (IOException e)
            { 
                System.out.println(e);
            }

        }
        }catch(IOException e){
            System.out.println("Acordame de generar un metodo para agregar una ventana error");
        }
    }

    public void sincronizacionTotal(InfoServidor servidor) {
        System.out.println("SINCRONIZAR A "+servidor.getIP()+" "+servidor.getPuertoSincronizacion());
        InfoServidor servidorConMenosCarga = directorio.getServidorConMenosCarga();
        Socket socketSincronizador = this.buscarConexionPorIpYPuerto(servidorConMenosCarga.getIP(), servidorConMenosCarga.getPuertoParaDirectorio());
        PrintWriter out;
        try{
            out = new PrintWriter(socketSincronizador.getOutputStream(), true);
            out.println("SINCRONIZAR SERVIDOR");
            out.println(servidor.getIP());
            out.println(servidor.getPuertoCliente());
            out.println(servidor.getPuertoSincronizacion());
            out.println(servidor.getPuertoParaDirectorio());
            out.println(servidor.getPuertoPing());
        } catch (IOException e)
        { }
    }
    
    private void enlistarServidor(Socket servidorSocket,InfoServidor servidor) throws IOException {
        agregarConexion(servidor,  new Socket(servidor.getIP(), servidor.getPuertoParaDirectorio()));
        directorio.agregarServidor(servidor);
        enviarNuevoServidor(servidor);
        new Thread(new HiloConexionServidor(servidorSocket)).start();
    }
    
    private void enviarNuevoServidor(InfoServidor servidorNuevo){
        PrintWriter out = null;
        ArrayList<InfoServidor> servidores = directorio.getServidores();
        for (Map.Entry<InfoServidor, Socket> entry : conexiones.entrySet()) {
            Socket socketServidor = entry.getValue();
            try {
                out = new PrintWriter(socketServidor.getOutputStream(), true);
            } catch (IOException ex) {
                Logger.getLogger(Monitor.class.getName()).log(Level.SEVERE, null, ex);
            }
            InfoServidor infoServidor = entry.getKey();
            
            if (infoServidor.getIP().equals(servidorNuevo.getIP()) &&
                infoServidor.getPuertoSincronizacion() == servidorNuevo.getPuertoSincronizacion()) {
                continue; 
            }
            System.out.println("AVISAR A "+infoServidor.getPuertoSincronizacion()+" q AGREGUE a "+servidorNuevo.getIP()+" "+servidorNuevo.getPuertoSincronizacion());
            out.println("AGREGAR SERVIDOR");
            out.println(servidorNuevo.getIP());
            out.println(servidorNuevo.getPuertoCliente());
            out.println(servidorNuevo.getPuertoSincronizacion());
            out.println(servidorNuevo.getPuertoParaDirectorio());
            out.println(servidorNuevo.getPuertoPing());
            
        }

    }
    
    private synchronized void agregarConexion(InfoServidor servidor, Socket socket) {
        conexiones.put(servidor, socket);
    }
    
    
    public synchronized void eliminarConexiones(ArrayList<InfoServidor> servidores) {
        for (InfoServidor servidor: servidores)
        {
            conexiones.remove(servidor);
            avisarServidorCaido(servidor);
        }
        
    }
    
    private void avisarServidorCaido(InfoServidor servidorCaido)
    {
        PrintWriter out;
        ArrayList<InfoServidor> servidores = directorio.getServidores();
        for(InfoServidor servidor : servidores){
            try{
                System.out.println("AVBISAR Q ELIMINEN A "+servidorCaido.getPuertoSincronizacion());
                Socket servidorSocket = this.buscarConexionPorIpYPuerto(servidor.getIP(), servidor.getPuertoParaDirectorio());
                out = new PrintWriter(servidorSocket.getOutputStream(), true);
                out.print("ELIMINAR SERVIDOR");
                out.print(servidorCaido.getIP());
                out.println(servidorCaido.getPuertoCliente());
                out.print(servidorCaido.getPuertoSincronizacion());
                out.println(servidorCaido.getPuertoParaDirectorio());
                out.println(servidorCaido.getPuertoPing());
            } catch (IOException e)
            { }
        }  
    }
    
    public Socket buscarConexionPorIpYPuerto(String ip, int puertoParaDirectorio) {
    for (Map.Entry<InfoServidor, Socket> entry : conexiones.entrySet()) {
        InfoServidor info = entry.getKey();
        if (info.getIP().equals(ip) && info.getPuertoParaDirectorio() == puertoParaDirectorio) {
            return entry.getValue();
        }
    }
    return null; 
}
 
    @Override
    public void run() {
        this.iniciarMonitor();
    }
}
