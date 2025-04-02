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
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.MensajeRed;

/**
 *
 * @author Usuario
 */
public class Cliente implements Runnable{
    
    private int puerto;
    private String IP;
    private MensajeRed msj;
    private boolean conectado = false;
    
    public Cliente(MensajeRed msj){
        this.puerto=msj.getDestinyPuerto();
        this.IP=msj.getMiIp();
        this.msj=msj;
    }
    
    public boolean isConectado() {
        return conectado;
    }
    
    private void conectar()throws IOException{
        try {
        Socket socket = new Socket(IP,puerto);
        this.conectado = true;
        // Crear un socket y conectarse al servidor
        System.out.println("Conectado al servidor en " + IP + " en el puerto " + puerto);

        // Crear flujos para leer y escribir en el socket, aunque el de entrada no se utilizaria
        BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
        
        salida.println(msj.getMyNickname());
        salida.println(msj.getMiIp());
        salida.println(Integer.toString(msj.getMiPuerto()));
        salida.println(IP);
        salida.println(Integer.toString(puerto));
        salida.println(msj.getContenido());
        // Cerrar el socket y los flujos
        socket.close();
        }catch (UnknownHostException e){
            throw new IOException("Host desconocido " + IP);
        }
        catch (IOException ex) {
            throw new IOException("Error al conectar al servidor " + IP);
        }
    }

    @Override
    public void run() {
        try {
            this.conectar();
        } catch (IOException ex) {
            System.out.println("Fallo alguno en la conexion del cliente con el servidor");
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}