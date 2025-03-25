/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package grupo10.messenger.backend.red;

import grupo10.messenger.backend.control.Control;
import grupo10.messenger.backend.modelo.MensajeRed;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 *
 * @author Usuario
 */
public class Servercito implements Runnable{
    private Socket socket;
    private Control controlador;

    public Servercito(Socket socket, Control controlador) {
        this.socket = socket;
        this.controlador = controlador;
    }

    @Override
    public void run() {
        try{
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        
        String nickname = in.readLine();
        String MyIp = in.readLine();
        String MyPort = in.readLine();
        String DestinyIp = in.readLine();
        String DestinyPort = in.readLine();
        String contenido = in.readLine();
        System.out.println("Mensaje recibido del cliente: " + contenido + " desde la IP " + MyIp);
        MensajeRed msj = new MensajeRed(nickname,MyIp,Integer.parseInt(MyPort),DestinyIp,Integer.parseInt(DestinyPort),contenido);
        controlador.recibirMensaje(msj);
        
        }catch(IOException e){
            e.printStackTrace(); //nunca deberia pasar
        }finally{
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Conexion cerrada con el servidor");
    }
    
    
}
