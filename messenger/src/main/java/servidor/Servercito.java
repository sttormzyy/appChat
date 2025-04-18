/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor;

import red.*;
import controlador.Control;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;


/**
 *
 * @author Usuario
 */
public class Servercito implements Runnable{
    private String nickname;
    private Socket socket;
    private Servidor servidor;
    private BufferedReader in;
    private PrintWriter out;
    
    public Servercito(Socket socket, Servidor servidor) {
        this.socket = socket;
        try{
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e){
            e.printStackTrace(); //nunca deberia pasar
        }
    }
    
    public void conectar() {
        try{
            String nickname = in.readLine();
            if (servidor.verificarClienteActivo(nickname)) {
            	out.println("verificado");
                servidor.agregarClienteActivo(nickname, this);
                while(true){
                    String comando = in.readLine();
                    switch (comando) {
                        case "enviar mensaje":
                            String nicknameOrigen = in.readLine();
                            String nicknameDestino = in.readLine();
                            String contenido = in.readLine();
                            MensajeDeRed msj = new MensajeDeRed(nicknameOrigen, nicknameDestino, contenido);
                            servidor.enviarMensaje(msj);
                            System.out.println("Mensaje recibido del cliente: " + contenido + " desde el nickname " + nicknameOrigen);
                            break;
                        case "recibir clientes":
                            enviarListaClientes();
                            break;
                    }
                    
                }
            }else {
            	out.println("rechazado");
            }
        }catch(IOException e){
           
        }finally{
            try {
                socket.close();
                servidor.eliminarClienteActivo(nickname);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Conexion cerrada con el servidor");
    }

    @Override
    public void run() {
        this.conectar();
    }
    
    public void enviarMensaje(MensajeDeRed msj){
        out.println("recibir mensaje");
        out.println(msj.getNicknameOrigen());
        out.println(msj.getNicknameDestino());
        out.println(msj.getContenido());
    }
    
    public void enviarListaClientes(){
        ArrayList<String> clientes = servidor.obtenerListaClientes();
        out.println("recibir clientes");
        out.println(clientes.size());
        for (int i = 0; i < clientes.size(); i++) {
            out.println(clientes.get(i));
        }
    }
}