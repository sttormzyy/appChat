/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package directorio;

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
public class HiloConexionCliente implements Runnable{    
    private Socket socket;
    private Directorio directorio;
    private BufferedReader in;
    private PrintWriter out;

    public HiloConexionCliente(Socket socket, Directorio directorio) {
        this.socket = socket;
        this.directorio = directorio;
        try{
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e)
        { }
    }
    
    private void conexion(){
        InfoServidor servidor = directorio.getServidorConMenosCarga();
        String clienteYaRecibio;
        if(servidor != null){
            System.out.println("SERVER DEVUELTO "+servidor.getIP()+" "+servidor.getPuertoCliente());
            out.println(servidor.getIP()+":"+servidor.getPuertoCliente());
            try {
                clienteYaRecibio = in.readLine();
                socket.close();
                out.close();
                in.close();
              
            } catch (IOException ex) {
                Logger.getLogger(HiloConexionCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            out.println("NO HAY SERVIDORES");
        }    
    }
    
    @Override
    public void run() {
        this.conexion();
    }
    
}
