/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package directorio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
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
    private HashMap<Server,Socket> conexiones = new HashMap();
    
    private class HiloConexionServidor implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        private HiloConexionServidor(Socket socket) {
            this.socket = socket;
            try{
                this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e)
            { }
        }

        private void conectar(){
            String ip;
            int puerto;
            try{
                while(true){
                    String comando = in.readLine();
                    switch (comando){
                        case "ELIMINAR CLIENTE":
                            ip = in.readLine();
                            puerto = Integer.parseInt(in.readLine());
                            directorio.disminuirContadorEnUno(ip,puerto);
                            break;
                    }
                }
            }catch(IOException e){
                System.out.println("Fallo inesperado");
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
        System.out.println("Monitor iniciado en el puerto " + puerto);
        while (true) 
        {
            Socket servidorSocket = monitor.accept();
            try{
                in = new BufferedReader(new InputStreamReader(servidorSocket.getInputStream()));
                System.out.println("Cliente conectado desde: " + servidorSocket.getInetAddress());
                String ipServidor;
                int puertoServidor;
                int puertoSincronizacion;
                String comando = in.readLine();
                switch (comando){
                    case "REGISTRO":
                        ipServidor = in.readLine();
                        puertoServidor = Integer.parseInt(in.readLine());
                        puertoSincronizacion = Integer.parseInt(in.readLine());
                        // sync inicial
                        if (directorio.getServidores().size() > 1) {
                            sincronizacionTotal(ipServidor, puertoSincronizacion);
                        } else {
                            enlistarServidor(servidorSocket, ipServidor, puertoServidor, puertoSincronizacion);
                        }
                        break;
                    case "LISTO":
                        ipServidor = in.readLine();
                        puertoServidor = Integer.parseInt(in.readLine());                
                        puertoSincronizacion = Integer.parseInt(in.readLine());  
                        enlistarServidor(servidorSocket, ipServidor, puertoServidor, puertoSincronizacion);
                        break;
                }
            } catch (IOException e)
            { }

        }
        }catch(IOException e){
            System.out.println("Acordame de generar un metodo para agregar una ventana error");
        }
    }

    public void sincronizacionTotal(String ipNoSincronizado, int puertoNoSincronizado) {
        Socket socketSincronizador = conexiones.get(directorio.getServidorConMenosCarga());
        PrintWriter out;
        try{
            out = new PrintWriter(socketSincronizador.getOutputStream(), true);
            out.print("SINCRONIZACION TOTAL");
            out.print(ipNoSincronizado);
            out.print(puertoNoSincronizado);
        } catch (IOException e)
        { }
    }
    
    private void enlistarServidor(Socket servidorSocket, String ipServidor, int puertoServidor, int puertoSincronizacion) {
        // COLIMBA
        agregarConexion(new Server(ipServidor, puertoServidor), servidorSocket);
        directorio.agregarServidor(ipServidor,puertoServidor);
        directorio.servidorListo(ipServidor,puertoServidor);
        enviarNuevoServidor(ipServidor, puertoSincronizacion);
        new Thread(new HiloConexionServidor(servidorSocket)).start();
    }
    
    private void enviarNuevoServidor(String ipServidorNuevo, int puertoSincronizacionNuevo){
        PrintWriter out;
        ArrayList<Server> servidores = directorio.getServidores();
        for(Server servidor : servidores){
            try{
                Socket servidorSocket = conexiones.get(servidor);
                out = new PrintWriter(servidorSocket.getOutputStream(), true);
                out.print("AGREGAR SERVIDOR");
                out.print(ipServidorNuevo);
                out.print(puertoSincronizacionNuevo);
            } catch (IOException e)
            { }
        }
    }
    
    private synchronized void agregarConexion(Server servidor, Socket socket) {
        conexiones.put(servidor, socket);
    }
    
    
    public synchronized void eliminarConexiones(ArrayList<Server> servidores) {
        for (Server servidor: servidores)
            conexiones.remove(servidor);
    }
    
    @Override
    public void run() {
        this.iniciarMonitor();
    }
}
