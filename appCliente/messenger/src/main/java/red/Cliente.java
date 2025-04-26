/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package red;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static resources.Constantes.*;

/**
 *
 * @author Usuario
 */
public class Cliente implements Runnable,IEmisor{
    private final int PUERTO_SERVIDOR = 10001;
    private final String IP_SERVIDOR = "localhost";
    private IReceptor receptor;
    private BufferedReader in;
    private PrintWriter out;
    private String nickname;
    private String estado;
    
    public Cliente(String nickname, IReceptor receptor)
    {
    	this.receptor = receptor;
    	this.nickname = nickname;
    }
    
    private void conectar()throws IOException
    {
        try {
            Socket socket = new Socket(IP_SERVIDOR, PUERTO_SERVIDOR);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
            if(this.verificarme(nickname)) 
            {
            	estado = ESTADO_VERIFICADO;
            	while(true){
                    String comando = in.readLine();
                    System.out.println(comando);
                    switch (comando) {
                        case RECIBIR_MENSAJE:
                            String nicknameOrigen = in.readLine();
                            String nicknameDestino = in.readLine();
                            String contenido = in.readLine();
                            String hora = in.readLine();
                            MensajeDeRed msj = new MensajeDeRed(nicknameOrigen, nicknameDestino, contenido, hora);
                            receptor.recibirMensaje(msj);
                            break;  
                        case RECIBIR_CLIENTES:      
                            ArrayList<String> clientes = new ArrayList<String>();
                            int cantidadClientes = Integer.parseInt(in.readLine());
                            for (int i = 0; i < cantidadClientes; i++) {
                                clientes.add(in.readLine());
                            }
                            receptor.recibirListaClientes(clientes);
                            break;
                        }
                            }}
            else{ 
              estado = "Nickname repetido";
            }
        }catch (UnknownHostException e){
            throw new IOException("Host desconocido " + IP_SERVIDOR);
        }
        catch (IOException ex) {
            throw new IOException("Error al conectar al servidor " + IP_SERVIDOR);
        }
        
    }
    
    public boolean verificarme(String nickname) 
    {
        out.println(nickname);
        String respuestaServidor = ESTADO_RECHAZADO;
        try {
            respuestaServidor = in.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return respuestaServidor.equals(ESTADO_VERIFICADO);
    }
    
    public boolean enviarMensaje(MensajeDeRed msj) 
    {
        out.println(ENVIAR_MENSAJE);
        out.println(msj.getNicknameOrigen());
        out.println(msj.getNicknameDestino());
        out.println(msj.getContenido());
        out.println(msj.getHoraEnvio());
        return true;
    }
    
    public void pedirListaClientes()
    {
        out.println(RECIBIR_CLIENTES);
    }
    
    public void pedirMensajesPendientes()
    {
        out.println(RECIBIR_MENSAJES_PENDIENTES);
    }
        
    public String getEstado()
    {
    	return estado;
    }
    
    @Override
    public void run() {
        try {
            this.conectar();
        } catch (IOException ex) {
        	estado = "IMPOSIBLE CONECTARSE CON EL SERVIDOR";
        }
    }
}