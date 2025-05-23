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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static servidor.Constantes.*;


/**
 *
 * @author Usuario
 */
public class HiloServidor implements Runnable{
    private String nickname;
    private Socket socket;
    private Servidor servidor;
    private BufferedReader in;
    private PrintWriter out;
    private String estado;
    private boolean enEjecucion = true;
    
    public HiloServidor(Socket socket, Servidor servidor) {
        this.socket = socket;
        this.servidor = servidor;
        try{
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e)
        { }
    }
    
    public void detenerHilo()
    {
        enEjecucion = false;
        try {
            socket.close();
            in.close();
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(HiloServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void conectar() 
    {
        try
        {
            this.nickname = in.readLine();
            estado = servidor.validarNickname(nickname);
            if (estado.equals(ESTADO_VERIFICADO)) 
            {
                System.out.println("estado "+estado);
            	out.println(ESTADO_VERIFICADO);
                servidor.agregarClienteActivo(nickname, this);
                while(enEjecucion){
                    String comando = in.readLine();
                    switch (comando) {
                        case ENVIAR_MENSAJE:
                            String nicknameOrigen = in.readLine();
                            String nicknameDestino = in.readLine();
                            String contenido = in.readLine();
                            String hora = in.readLine();
                            MensajeDeRed msj = new MensajeDeRed(nicknameOrigen, nicknameDestino, contenido, hora);
                            servidor.enviarMensaje(msj);
                            break;
                        case RECIBIR_CLIENTES:                     
                            enviarListaClientes();
                            break;
                        case RECIBIR_MENSAJES_PENDIENTES:                     
                            enviarMensajesPendientes();
                            break;
                    }
                    
                }
            }else {
            	out.println(estado);
                return;
            }
        }catch(IOException e){
           
        }finally{
            try {
                socket.close();
                if(estado != null && estado.equals(ESTADO_VERIFICADO)){
                   servidor.eliminarClienteActivo(nickname);}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Conexion cerrada con el servidor");
    }
    
    public void enviarMensaje(MensajeDeRed msj){
        out.println(RECIBIR_MENSAJE);
        out.println(msj.getNicknameOrigen());
        out.println(msj.getNicknameDestino());
        out.println(msj.getContenido());
        out.println(msj.getHoraEnvio());
    }
    
    public void enviarListaClientes(){
        ArrayList<String> clientes = servidor.obtenerListaClientes();
        out.println(RECIBIR_CLIENTES);
        out.println(clientes.size());
        for (int i = 0; i < clientes.size(); i++) {
            out.println(clientes.get(i));
        }
    }
    
    public void enviarMensajesPendientes()
    {
        servidor.enviarMensajesPendientes(nickname, this);
    }
    
    @Override
    public void run() {
        this.conectar();
    }
}