/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monitoreo;

import configuracion.VentanaError;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import servidor.InfoServidor;
import servidor.Servidor;
import sincronizador.Sincronizador;

/**
 *
 * @author Usuario
 */
public class ComunicacionDirectorio implements Runnable{
    private String IPDirectorio;
    private int puertoDelDirectorio;
    private int puertoParaDirectorio;
    private int puertoPing;    
    private Socket socket;
    private ServerSocket socketParaDirectorio;
    private BufferedReader in;
    private PrintWriter out;      
    private Sincronizador sincronizador;
    private Servidor servidor;
    private ActionListener controlador;
    private boolean enEjecucion = true, estaListo = false;

    public ComunicacionDirectorio(String IPDirectorio, int puertoDirectorio, int puertoParaDirectorio, int puertoPing, ActionListener controlador) {
        this.IPDirectorio = IPDirectorio;
        this.puertoDelDirectorio = puertoDirectorio;
        this.puertoParaDirectorio = puertoParaDirectorio;
        this.puertoPing = puertoPing;
        this.controlador = controlador;
    }
    
    public void setSincronizador(Sincronizador sincronizador)
    {
        this.sincronizador = sincronizador;
    }
    
    public void setServidor(Servidor servidor)
    {
        this.servidor = servidor;
    }
   
     
    public void detener() {
        enEjecucion = false;
        if (socketParaDirectorio != null && !socketParaDirectorio.isClosed()) try {
            socketParaDirectorio.close();
        } catch (IOException ex) {
            Logger.getLogger(ComunicacionDirectorio.class.getName()).log(Level.SEVERE, null, ex);
        }
        cerrarRecursos();
        System.out.println("Comunicacion con directorio detenida correctamente.");
    }
        
    private void cerrarRecursos() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Registra el servidor en el directorio
     * @param IPDirectorio
     * @param puertoDirectorio
     * @param servidor
     * @return 
     */
    public boolean registrarServidorEnDirectorio(String IPDirectorio, int puertoDirectorio, InfoServidor servidor) {
        String soyPrimero;
        System.out.println("Intentando registrar servidor...");

        int intentos = 0;
        final int MAX_INTENTOS = 50;

        while (intentos < MAX_INTENTOS) {
            try {
                this.socket = new Socket(IPDirectorio, puertoDirectorio);
                this.out = new PrintWriter(socket.getOutputStream(), true);
                this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out.println("REGISTRO");
                out.println(servidor.getIP());
                out.println(servidor.getPuertoCliente());
                out.println(servidor.getPuertoSincronizacion());
                out.println(puertoParaDirectorio);
                out.println(puertoPing);

                soyPrimero = in.readLine();
                if ("FRANCIA".equals(soyPrimero)) {
                    cerrarRecursos();
                } else {
                    System.out.println("servidor registrado en directorio");
                    sincronizador.agregarServidor(servidor);
                }

                return true;

            } catch (IOException ex) {
                intentos++;
                System.err.println("Intento " + intentos + " fallido al registrar servidor. Reintentando...");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    return false;
                }
            }
        }

        System.err.println("No se pudo registrar el servidor tras " + MAX_INTENTOS + " intentos.");
        return false;
    }

    
    /**
     * Acaba con el registro del servidor en el directorio en el caso que el servidor no sea el primero 
     * de todos en ser registrado ( debe dividirse en dos el registro por la sincronziacion que requiere u
     * nuevo servidor que no sea el primero) 
     */
    public void avisarEstoyListo() {
        int intentos = 0;
        final int MAX_INTENTOS = 100;

        while (intentos < MAX_INTENTOS) {
            try {
                this.socket = new Socket(IPDirectorio, puertoDelDirectorio);
                this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                this.out = new PrintWriter(this.socket.getOutputStream(), true);

                out.println("LISTO");
                out.println(servidor.getIP());
                out.println(servidor.getPuertoCliente());
                out.println(servidor.getPuertoSincronizacion());
                out.println(puertoParaDirectorio);
                out.println(puertoPing);
                System.out.println("servidor registrado en directorio");
                return; // Salir del método si fue exitoso

            } catch (IOException ex) {
                intentos++;
                Logger.getLogger(ComunicacionDirectorio.class.getName()).log(Level.SEVERE,
                        "Fallo intento " + intentos + " para avisar que estoy listo. Reintentando...", ex);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    new VentanaError(null,true,"Error registrando servidor en el directorio");
                    controlador.actionPerformed(new ActionEvent("Comunicacion con directorio caida", 80, "COMPONENTE CAIDO"));
                    return;
                }
            }
        }

       new VentanaError(null,true,"Error registrando servidor en el directorio");
       controlador.actionPerformed(new ActionEvent("Comunicacion con directorio caida", 80, "COMPONENTE CAIDO"));
    }
    
    /**
     * Comunica al directorio que acaba de desconectarse un usuario,
     * el puerto y la IP sirven para que el directorio identifique al servidor
     * @param ipServidor
     * @param puertoCliente 
     */
    public synchronized void avisarUsuarioDesconectado(String ipServidor,int puertoCliente) 
    {
        out.println("USUARIO DESCONECTADO");
        out.println(ipServidor);        
        out.println(puertoCliente);
    }
    
    /**
     * Comunica al directorio que acaba de conectarse un usuario,
     * el puerto y la IP sirven para que el directorio identifique al servidor
     * @param ipServidor
     * @param puertoCliente 
     */
    public synchronized void avisarUsuarioConectado(String ipServidor,int puertoCliente) 
    {
        out.println("USUARIO CONECTADO");
        out.println(ipServidor);        
        out.println(puertoCliente);
    }
     
    /**
     * Agrega a la lista de servidores del sincronizador un nuevo servidor que recibe por parte del directorio
     * @param in
     * @throws IOException 
     */
    private void agregarServidor(BufferedReader in) throws IOException{
        String ip = in.readLine();
        int puertoCliente = Integer.parseInt(in.readLine());
        int puertoSincronizacion = Integer.parseInt(in.readLine());
        int puertoParaDirectorio = Integer.parseInt(in.readLine());
        int puertoPing = Integer.parseInt(in.readLine());
        InfoServidor servidor = new InfoServidor(ip, puertoCliente, puertoSincronizacion, puertoParaDirectorio, puertoPing);
        
        System.out.println("Directorio me dijo agregue al servidor "+puertoSincronizacion);
        this.sincronizador.agregarServidor(servidor);
    }
    
    /**
     * Elimina de la lista de servidores del sincronizador un servidor que recibe por parte del directorio
     * @param in
     * @throws IOException 
     */
    private void eliminarServidor(BufferedReader in) throws IOException{
        String ip = in.readLine();
        int puertoCliente = Integer.parseInt(in.readLine());
        int puertoSincronizacion = Integer.parseInt(in.readLine());
        int puertoParaDirectorio = Integer.parseInt(in.readLine());
        int puertoPing = Integer.parseInt(in.readLine());
        InfoServidor servidor = new InfoServidor(ip, puertoCliente, puertoSincronizacion, puertoParaDirectorio, puertoPing);
        
        System.out.println("Directorio me dijo elimine al servidor "+puertoSincronizacion);
        this.sincronizador.eliminarServidor(servidor);
    }
    
    /**
     * Sincroniza completamente a un nuevo servidor, pasandole toda la informacion actualizada
     * @param in
     * @throws IOException 
     */
    private void sincronizarCompletamenteOtro(BufferedReader in) throws IOException{
        String ip = in.readLine();
        int puertoCliente = Integer.parseInt(in.readLine());
        int puertoSincronizacion = Integer.parseInt(in.readLine());
        int puertoParaDirectorio = Integer.parseInt(in.readLine());
        int puertoPing = Integer.parseInt(in.readLine());
        InfoServidor servidorNoSincronizado = new InfoServidor(ip, puertoCliente, puertoSincronizacion, puertoParaDirectorio, puertoPing);
        this.sincronizador.sincronizarTodo(servidorNoSincronizado);
    }

    public boolean estaListo()
    {
        return estaListo;
    }
       
    /**
     * Recibe comandos del directorio para
     *  - Agregar un nuevo servidor a la lista de servidores del sincronizador
     *  - Eliminar un servidor de la lista de servidores del sincronizador
     *  - Sincronizar un servidor recien registrado
     */
    @Override
    public void run() {
        try {
            this.socketParaDirectorio = new ServerSocket(puertoParaDirectorio);
            estaListo = true;
            Socket socketDirectorio = socketParaDirectorio.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socketDirectorio.getInputStream()));
            try {
                while(enEjecucion){
                    String comando = in.readLine();
                    if (comando == null) {
                        continue;
                    }
                    switch (comando) {
                        case "AGREGAR SERVIDOR":
                            agregarServidor(in);
                            break;
                        case "ELIMINAR SERVIDOR":
                            eliminarServidor(in);
                            break;
                        case "SINCRONIZAR SERVIDOR":
                            sincronizarCompletamenteOtro(in);
                            break;
                    }
                }
            } catch(Exception e){
                socketParaDirectorio.close();
                socket.close();
                if(enEjecucion) 
                {
                    System.out.println("Entre aca"+e);
                    controlador.actionPerformed(new ActionEvent("Comunicacion con directorio caida", 80, "COMPONENTE CAIDO"));
                }
            }finally{
                try {
                    socketParaDirectorio.close();
                    socket.close();
                } catch (IOException ex) {
                    Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch(IOException ex){
           System.out.println("Entre por abajo");
           controlador.actionPerformed(new ActionEvent("Comunicacion con directorio caida", 80, "COMPONENTE CAIDO"));
           
        }
    }      
}
