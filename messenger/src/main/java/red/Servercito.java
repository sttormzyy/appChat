/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package red;

import controlador.Control;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import modelo.MensajeRed;


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
            StringBuilder contenidoBuilder = new StringBuilder();
            String line;
        
            line = in.readLine();
            if (line != null) {
                contenidoBuilder.append(line);  // Añadimos la primera línea
            }
            // Leemos las siguientes líneas, pero agregamos un salto de línea solo si no es la última
            while ((line = in.readLine()) != null) {
                contenidoBuilder.append("\n");  
                contenidoBuilder.append(line); 
            }
            String contenido = contenidoBuilder.toString();
            MensajeRed msj = new MensajeRed(nickname, MyIp, Integer.parseInt(MyPort), DestinyIp, Integer.parseInt(DestinyPort), contenido);
            controlador.recibirMensaje(msj);
            //System.out.println("Mensaje recibido del cliente: " + contenido + " desde la IP " + MyIp);
        
        }catch(IOException e){
            e.printStackTrace(); 
        }finally{
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //System.out.println("Conexion cerrada con el servidor");
    }
}