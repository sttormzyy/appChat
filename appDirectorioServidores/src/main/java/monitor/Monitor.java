/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monitor;

import directorio.Directorio;
import directorio.InfoServidor;
import gui.VentanaError;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
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
    
    
    /**
     * Se le asigna un servidor y luego recibe mensajes de este indicando
     *  1 - Tiene un nuevo usuario conectado
     *  2 - Tiene un nuevo usuario desconectado
     * De esta forma permite mantener la informacion actualizada que se necesita para
     * saber cual es el servidor con menos carga
     */
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
                System.out.println("Error al crear el socket con servidor: " + e.getMessage());
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
                System.out.println("Error en la comunicación con un servidor: " + e.getMessage());
            } finally {
                try {
                    socket.close(); // Asegurarse de cerrar el socket
                } catch (IOException e) {
                    System.out.println("Error al cerrar el socket con servidor: " + e.getMessage());
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
    
    
    /**
     * Inicia el hilo de Ping que monitorea el estado de los servidores
     * Se encarga de registrar los servidores nuevos
     */
    public void iniciarMonitor() {
    try {
        new Thread(new Ping(directorio, this)).start();
        monitor = new ServerSocket(this.puerto);
        BufferedReader in;
        PrintWriter out;
        System.out.println("Monitor iniciado en el puerto " + puerto);

        while (true) {
            Socket servidorSocket = monitor.accept();
            try {
                in = new BufferedReader(new InputStreamReader(servidorSocket.getInputStream()));
                out = new PrintWriter(servidorSocket.getOutputStream(), true);
                String ipServidor;
                int puertoServidorParaClientes, puertoSincronizacion, puertoParaDirectorio, puertoPing;
                InfoServidor servidor;

                String comando = in.readLine();
                switch (comando) {
                    case "REGISTRO":
                        ipServidor = in.readLine();
                        puertoServidorParaClientes = Integer.parseInt(in.readLine());
                        puertoSincronizacion = Integer.parseInt(in.readLine());
                        puertoParaDirectorio = Integer.parseInt(in.readLine());
                        puertoPing = Integer.parseInt(in.readLine());

                        if (directorio.getServidoresActivos().size() >= 1) {
                            out.println("FRANCIA");
                            servidor = new InfoServidor(ipServidor, puertoServidorParaClientes, puertoSincronizacion, puertoParaDirectorio, puertoPing, false);
                            sincronizacionTotal(servidor);
                        } else {
                            out.println("PRIMERO");
                            servidor = new InfoServidor(ipServidor, puertoServidorParaClientes, puertoSincronizacion, puertoParaDirectorio, puertoPing, true);
                            enlistarServidor(servidorSocket, servidor);
                        }
                        break;

                    case "LISTO":
                        ipServidor = in.readLine();
                        puertoServidorParaClientes = Integer.parseInt(in.readLine());
                        puertoSincronizacion = Integer.parseInt(in.readLine());
                        puertoParaDirectorio = Integer.parseInt(in.readLine());
                        puertoPing = Integer.parseInt(in.readLine());
                        servidor = new InfoServidor(ipServidor, puertoServidorParaClientes, puertoSincronizacion, puertoParaDirectorio, puertoPing, true);
                        enlistarServidor(servidorSocket, servidor);
                        break;
                }
            } catch (IOException e) {
                String mensajeError = "<html>Error al registrar servidor en directorio: <br><br>" +
                      e.getClass().getSimpleName() + "<br><br>" + e.getMessage() + "</html>";
                new VentanaError(null, true, mensajeError);
            }
        }

    } catch (IOException e) {
        String mensajeError = "<html>Error al iniciar el monitor: <br><br>" +
                      e.getClass().getSimpleName() + "<br><br>" + e.getMessage() + "</html>";
        new VentanaError(null, true, mensajeError);
    }
    }

    /**
     * Intenta sincronizar un servidor recien registrado.
     * Prueba usar de sincronziadrocada uno de los servidores activos en orden de menor carga
     * Si no logra sincronizar informa
     * @param servidorNoSincronizado 
     */
    public void sincronizacionTotal(InfoServidor servidorNoSincronizado) {
       System.out.println("SINCRONIZAR A " + servidorNoSincronizado.getIP() + " " + servidorNoSincronizado.getPuertoSincronizacion());

       // Obtener servidores activos y filtrar el actual
       ArrayList<InfoServidor> servidoresActivos = directorio.getServidoresActivos();
       servidoresActivos.removeIf(s -> s.getPuertoCliente()== servidorNoSincronizado.getPuertoCliente());

       // Ordenar por cantidad de usuarios activos
       servidoresActivos.sort(Comparator.comparingInt(InfoServidor::getCantidadUsuariosActivos));

       boolean sincronizado = false;
        System.out.println("serv activos "+servidoresActivos);
       for (InfoServidor servidorSincronizador : servidoresActivos) {
           try {
               Socket socketSincronizador = this.buscarConexionPorIpYPuerto(servidorSincronizador.getIP(), servidorSincronizador.getPuertoParaDirectorio()
               );
               PrintWriter out = new PrintWriter(socketSincronizador.getOutputStream(), true);
               
               System.out.println("Servidor "+servidorSincronizador.getPuertoCliente()+ " sincronizara a " + servidorSincronizador.getPuertoCliente());
               out.println("SINCRONIZAR SERVIDOR");
               out.println(servidorNoSincronizado.getIP());
               out.println(servidorNoSincronizado.getPuertoCliente());
               out.println(servidorNoSincronizado.getPuertoSincronizacion());
               out.println(servidorNoSincronizado.getPuertoParaDirectorio());
               out.println(servidorNoSincronizado.getPuertoPing());

               sincronizado = true;
               System.out.println("Sincronización exitosa desde " + servidorSincronizador.getPuertoCliente());
               break;

           } catch (IOException e) {
               System.err.println("Fallo al sincronizar con " + servidorSincronizador.getIP() + ":" + servidorSincronizador.getPuertoParaDirectorio());
           }
       }

       if (!sincronizado) {
           String mensajeError = "No se pudo sincronizar el nuevo servidor con ninguno de los servidores activos.";
           new VentanaError(null, true, mensajeError);
       }
   }

    
    /*
    private void enlistarServidor(Socket servidorSocket,InfoServidor servidor){
        try {
            agregarConexion(servidor,  new Socket(servidor.getIP(), servidor.getPuertoParaDirectorio()));
        } catch (IOException ex) {
            Logger.getLogger(Monitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        directorio.agregarServidor(servidor);
        enviarNuevoServidor(servidor);
        new Thread(new HiloConexionServidor(servidorSocket)).start();
    }
    */
    
     /**
     * Registra el servidor en el directorio e inicia el hilo que atiende a dicho servidor
     * @param servidorSocket
     * @param servidor
     * @throws IOException 
     */
    private void enlistarServidor(Socket servidorSocket, InfoServidor servidor) {
        boolean conectado = false;
        int intentos = 0;
        final int MAX_INTENTOS = 100;

        while (!conectado && intentos < MAX_INTENTOS) {
            try {
                Socket nuevoSocket = new Socket(servidor.getIP(), servidor.getPuertoParaDirectorio());
                agregarConexion(servidor, nuevoSocket);
                conectado = true; // Salir del bucle si se conecta exitosamente
            } catch (IOException ex) {
                intentos++;
                Logger.getLogger(Monitor.class.getName()).log(Level.WARNING,
                    "Intento " + intentos + " fallido al conectar con el servidor: " + servidor.getIP(), ex);
                try {
                    Thread.sleep(500); // Esperar medio segundo antes de reintentar
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        if (conectado) {
            directorio.agregarServidor(servidor);
            enviarNuevoServidor(servidor);
            new Thread(new HiloConexionServidor(servidorSocket)).start();
        } else {
            new VentanaError(null,true,"No se pudo establecer conexion con el servidor \n"+"IP:"+servidor.getIP()+" puerto:"+servidor.getPuertoParaDirectorio());
        }
    }

    
    /**
     * Comunica a los servidores activos que se registro un nuevo servidor
     * @param servidorNuevo 
     */
    private void enviarNuevoServidor(InfoServidor servidorNuevo){
        PrintWriter out = null;
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
            System.out.println("avisar a "+infoServidor.getPuertoSincronizacion()+" que agregue a "+servidorNuevo.getIP()+" "+servidorNuevo.getPuertoSincronizacion());
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
            Socket socketServidor = this.buscarConexionPorIpYPuerto(servidor.getIP(), servidor.getPuertoParaDirectorio());
            try {
                socketServidor.close();
            } catch (IOException ex) {
                Logger.getLogger(Monitor.class.getName()).log(Level.SEVERE, null, ex);
            }
            conexiones.remove(servidor);
            avisarServidorCaido(servidor);
        }
        
    }
    /**
     * Comunica a los servidores activos que se cayo un servidor
     * @param servidorCaido 
     */
    private void avisarServidorCaido(InfoServidor servidorCaido)
    {
        PrintWriter out = null;
         for (Map.Entry<InfoServidor, Socket> entry : conexiones.entrySet()) {
            Socket socketServidor = entry.getValue();
            try {
                out = new PrintWriter(socketServidor.getOutputStream(), true);
            } catch (IOException ex) {
                Logger.getLogger(Monitor.class.getName()).log(Level.SEVERE, null, ex);
            }
            InfoServidor infoServidor = entry.getKey();
  
            System.out.println("avisar "+infoServidor.getPuertoSincronizacion()+" que elimine a "+servidorCaido.getIP()+" "+servidorCaido.getPuertoSincronizacion());
            out.println("ELIMINAR SERVIDOR");
            out.println(servidorCaido.getIP());
            out.println(servidorCaido.getPuertoCliente());
            out.println(servidorCaido.getPuertoSincronizacion());
            out.println(servidorCaido.getPuertoParaDirectorio());
            out.println(servidorCaido.getPuertoPing());
            
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
