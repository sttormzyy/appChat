/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
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
    private int puertoParaDirectorio;
    private int puertoPing;    
    private Socket socket;
    private ServerSocket socketParaDirectorio;
    private BufferedReader in;
    private PrintWriter out;     
    private Sincronizador sincronizador;
    private Servidor servidor;
    private boolean enEjecucion = true;

    public ComunicacionDirectorio(String IPDirectorio, int puertoDirectorio, int puertoParaDirectorio, int puertoPing) {
        this.IPDirectorio = IPDirectorio;
        this.puertoDirectorio = puertoDirectorio;
        this.puertoParaDirectorio = puertoParaDirectorio;
        this.puertoPing = puertoPing;
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
        try {
            if (socketParaDirectorio != null && !socketParaDirectorio.isClosed()) {
                socketParaDirectorio.close();  // esto fuerza que el accept() lance una excepción
                out.close();
                in.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        
    public boolean registrarServidorEnDirectorio(String IPDirectorio, int puertoDirectorio, InfoServidor servidor) {
        String soyPrimero;
        System.out.println("Intentando registrar servidor");
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
            if(soyPrimero.equals("FRANCIA"))
            {
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null && !socket.isClosed()) socket.close();
            }
            else
                sincronizador.agregarServidor(servidor);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(ComunicacionDirectorio.class.getName()).log(Level.SEVERE, null, ex);
            return false;
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
        out.println(servidor.getIP());
        out.println(servidor.getPuertoCliente());
        out.println(servidor.getPuertoSincronizacion());
        out.println(puertoParaDirectorio);
        out.println(puertoPing);

    }
    
    public synchronized void avisarUsuarioDesconectado(String ipServidor,int puertoCliente) 
    {
        out.println("USUARIO DESCONECTADO");
        out.println(ipServidor);        
        out.println(puertoCliente);
    }
    
    public synchronized void avisarUsuarioConectado(String ipServidor,int puertoCliente) 
    {
        out.println("USUARIO CONECTADO");
        out.println(ipServidor);        
        out.println(puertoCliente);
    }
     
    // RECIBEN INFORMACION DEL DIRECTORIO, SE DISPARAN POR RECEPCION
    private void agregarServidor(BufferedReader in) throws IOException{
        String ip = in.readLine();
        int puertoCliente = Integer.parseInt(in.readLine());
        int puertoSincronizacion = Integer.parseInt(in.readLine());
        int puertoParaDirectorio = Integer.parseInt(in.readLine());
        int puertoPing = Integer.parseInt(in.readLine());
        InfoServidor servidor = new InfoServidor(ip, puertoCliente, puertoSincronizacion, puertoParaDirectorio, puertoPing);
        System.out.println("DIRECTORIO ME DIJOQ  AGREGURE A "+puertoSincronizacion);
        this.sincronizador.agregarServidor(servidor);
    }
    
    private void eliminarServidor(BufferedReader in) throws IOException{
        String ip = in.readLine();
        int puertoCliente = Integer.parseInt(in.readLine());
        int puertoSincronizacion = Integer.parseInt(in.readLine());
        int puertoParaDirectorio = Integer.parseInt(in.readLine());
        int puertoPing = Integer.parseInt(in.readLine());
        InfoServidor servidor = new InfoServidor(ip, puertoCliente, puertoSincronizacion, puertoParaDirectorio, puertoPing);
        this.sincronizador.eliminarServidor(servidor);
    }
    
    private void sincronizarCompletamenteOtro(BufferedReader in) throws IOException{
        String ip = in.readLine();
        int puertoCliente = Integer.parseInt(in.readLine());
        int puertoSincronizacion = Integer.parseInt(in.readLine());
        int puertoParaDirectorio = Integer.parseInt(in.readLine());
        int puertoPing = Integer.parseInt(in.readLine());
        InfoServidor servidorNoSincronizado = new InfoServidor(ip, puertoCliente, puertoSincronizacion, puertoParaDirectorio, puertoPing);
        this.sincronizador.sincronizarTodo(servidorNoSincronizado);
    }

    
    @Override
    public void run() {
        try {
            this.socketParaDirectorio = new ServerSocket(puertoParaDirectorio);
            Socket socketDirectorio = socketParaDirectorio.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socketDirectorio.getInputStream()));
            try {
                while(enEjecucion){
                    String comando = in.readLine();
                    switch (comando) {
                        case "AGREGAR SERVIDOR":
                            agregarServidor(in);
                            break;
                        case "ELIMINAR SERVIDOR":
                            eliminarServidor(in);
                            break;
                        case "SINCRONIZAR SERVIDOR":
                            System.out.println("sincronizar compeltamtne a otro");
                            sincronizarCompletamenteOtro(in);
                            break;
                    }
                }
            } catch(IOException e){
                
            }finally{
                try {
                    socketParaDirectorio.close();
                    socket.close();
                } catch (IOException ex) {
                    Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch(IOException ex){
            Logger.getLogger(ComunicacionDirectorio.class.getName()).log(Level.SEVERE, null, ex);
           
        }
    }      
}
