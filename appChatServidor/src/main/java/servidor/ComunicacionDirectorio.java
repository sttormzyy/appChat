/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class ComunicacionDirectorio implements Runnable{
    private String IPDirectorio;
    private int puertoDirectorio;
    private int puertoMonitoreo;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;     
    private Sincronizador sincronizador;
    private Servidor servidor;

    public ComunicacionDirectorio(String IPDirectorio, int puertoDirectorio, int puertoMonitoreo) {
        this.IPDirectorio = IPDirectorio;
        this.puertoDirectorio = puertoDirectorio;
        this.puertoMonitoreo = puertoMonitoreo;
    }
    
    public void setSincronizador(Sincronizador sincronizador)
    {
        this.sincronizador = sincronizador;
    }
    
    public boolean registrarServidorEnDirectorio(String IPDirectorio, int puertoDirectorio, InfoServidor servidor) {
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        System.out.println("Intentando registrar servidor");
        try {
            socket = new Socket(IPDirectorio, puertoDirectorio);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
 
            out.println("REGISTRO");
            out.println(servidor.getIP());
            out.println(servidor.getPuertoCliente());
            out.println(servidor.getPuertoSincronizacion());
            out.println(this.puertoMonitoreo);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(ComunicacionDirectorio.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            // Cerramos recursos manualmente
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null && !socket.isClosed()) socket.close();
            } catch (IOException e) {
                Logger.getLogger(ComunicacionDirectorio.class.getName()).log(Level.WARNING, "Error al cerrar el socket", e);
            }
        }
    }

    
    public void avisarEstoyListo(){
        
        try {
            this.socket = new Socket(IPDirectorio, puertoDirectorio);
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new PrintWriter(this.socket.getOutputStream(), true);
        } catch (IOException ex) {
            Logger.getLogger(ComunicacionDirectorio.class.getName()).log(Level.SEVERE, null, ex);
        }
        out.println("LISTO");
        new Thread(this).start();
    }
    
    public void avisarUsuarioDesconectado(int puertoCliente) {
        out.println("USUARIO DESCONECTADO");
        out.println(puertoCliente);
    }
     
    // RECIBEN INFORMACION DEL DIRECTORIO, SE DISPARAN POR RECEPCION
    private void agregarServidor() throws IOException{
        String ip = in.readLine();
        int puertoCliente = Integer.parseInt(in.readLine());
        int puertoSincronizacion = Integer.parseInt(in.readLine());
        InfoServidor servidor = new InfoServidor(ip, puertoCliente, puertoSincronizacion);
        this.sincronizador.agregarServidor(servidor);
    }
    
    private void eliminarServidor() throws IOException{
        String ip = in.readLine();
        int puertoCliente = Integer.parseInt(in.readLine());
        int puertoMonitoreo = Integer.parseInt(in.readLine());
        int puertoSincronizacion = Integer.parseInt(in.readLine());
        InfoServidor servidor = new InfoServidor(ip, puertoCliente, puertoSincronizacion);
        this.sincronizador.eliminarServidor(servidor);
    }
    
    private void sincronizarCompletamenteOtro() throws IOException{
        String ip = in.readLine();
        int puertoCliente = Integer.parseInt(in.readLine());
        int puertoSincronizacion = Integer.parseInt(in.readLine());
        InfoServidor servidorNoSincronizado = new InfoServidor(ip, puertoCliente, puertoSincronizacion);
        this.sincronizador.sincronizarTodo(servidorNoSincronizado);
    }

    
    @Override
    public void run() {
        try {
            while(true){
                String comando = in.readLine();
                switch (comando) {
                    case "AGREGAR SERVIDOR":
                        agregarServidor();
                        break;
                    case "ELIMINAR SERVIDOR":                     
                        eliminarServidor();
                        break;
                    case "SINCRONIZAR SERVIDOR":
                        sincronizarCompletamenteOtro();
                        break;
                }
            }
        } catch(IOException e){
           
        }finally{
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }      
}
