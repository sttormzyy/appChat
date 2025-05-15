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
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
      
    private Sincronizador sincronizador;
    private String IPDirectorio;
    private int puertoDirectorio;

    public ComunicacionDirectorio(InfoServidor servidor,String IPDirectorio, int puertoDirectorio,Sincronizador sincronizador) {
        this.IPDirectorio = IPDirectorio;
        this.puertoDirectorio = puertoDirectorio;
        
        try {
            this.socket = new Socket(IPDirectorio, puertoDirectorio);
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new PrintWriter(this.socket.getOutputStream(), true);
        } catch (IOException ex) {
            Logger.getLogger(ComunicacionDirectorio.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        registrarmeEnDirectorio(servidor);
    }
    
    // ENVIAN INFORMACION AL DIRECTORIO, LO INICIA EL SERVIDOR
    private void registrarmeEnDirectorio(InfoServidor servidor){
        out.println("REGISTRAR_SERVIDOR_NUEVO");
        out.println(servidor.getIP());
        out.println(servidor.getPuertoCliente());
        out.println(servidor.getPuertoMonitoreo());
        out.println(servidor.getPuertoSincronizacion());
    }
    
    public void avisarUsuarioDesconectado(String nickname) {
        out.println("USUARIO_DESCONECTADO");
        out.println(nickname);
    }
    
    // ENVIA INFORMACION AL DIRECTORIO, SE DISPARA POR RECEPCION
    private void pingEcho(){
        out.println("ECHO");
    }
    
    // RECIBEN INFORMACION DEL DIRECTORIO, SE DISPARAN POR RECEPCION
    private void agregarServidor() throws IOException{
        String ip = in.readLine();
        int puertoCliente = Integer.parseInt(in.readLine());
        int puertoMonitoreo = Integer.parseInt(in.readLine());
        int puertoSincronizacion = Integer.parseInt(in.readLine());
        InfoServidor servidor = new InfoServidor(ip, puertoCliente, puertoMonitoreo, puertoSincronizacion);
        this.sincronizador.agregarServidor(servidor);
    }
    
    private void eliminarServidor() throws IOException{
        String ip = in.readLine();
        int puertoCliente = Integer.parseInt(in.readLine());
        int puertoMonitoreo = Integer.parseInt(in.readLine());
        int puertoSincronizacion = Integer.parseInt(in.readLine());
        InfoServidor servidor = new InfoServidor(ip, puertoCliente, puertoMonitoreo, puertoSincronizacion);
        this.sincronizador.eliminarServidor(servidor);
    }
    
    private void sincronizarCompletamenteOtro() throws IOException{
        String ip = in.readLine();
        int puertoCliente = Integer.parseInt(in.readLine());
        int puertoMonitoreo = Integer.parseInt(in.readLine());
        int puertoSincronizacion = Integer.parseInt(in.readLine());
        InfoServidor servidor = new InfoServidor(ip, puertoCliente, puertoMonitoreo, puertoSincronizacion);
        this.sincronizador.sincronizarTodo(servidor);
    }
    
    // METODO RUN PARA ESCUCHAR CONSTANTEMENTE
    @Override
    public void run() {
        try {
            while(true){
                String comando = in.readLine();
                switch (comando) {
                    case "AGREGAR_SERVIDOR":
                        agregarServidor();
                        break;
                    case "ELIMINAR_SERVIDOR":                     
                        eliminarServidor();
                        break;
                    case "PING":                     
                        pingEcho();
                        break;
                    case "SINCRONIZACION_TOTAL":
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
    
    // ANALISIS DE IMPLEMENTACIONES DE LA CLASE

    registrarServidor(InfoServidor s)
    {
        se conecta al socket de directorio
        out.(REGISTRAR_SERVIDOR)
        le pasa la info del servidor
    }
    
    avisarDirectorioEstoyListo()
    {
        se conecta al socket
        out.(READY)
    }
    
    public void avisarUsuarioDesconectado()
    {
        abre socket y le manda BAJA_DE_USUARIO al directorio
    }
     
    
    run()
    {
        while(true)
            {
                lee del socket un comando
                switch(comando){
                    case AÑADIR_SERVIDOR
                            sincronizador.agregarServidor();
                    case ELIMINAR_SERVIDOR
                            sincronizador.eliminarServidor();          
                    case PING
                            out.(ECHO);
                    case SINCRONIZACION_TOTAL
                            sincronizador.sincronizacionTotal(el servidor al q hay q sincronizar)
                    }
                }
     }
    
}
