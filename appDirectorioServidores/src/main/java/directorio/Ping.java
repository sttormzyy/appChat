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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class Ping implements Runnable{
    
    private Directorio directorio;
    private Monitor monitor;
    
    public Ping(Directorio directorio, Monitor monitor){
        this.directorio = directorio;
        this.monitor = monitor;
    }
    

    @Override
    public void run() {
        while(true){
            ArrayList<Server> servidores = directorio.getServidores();
            ArrayList<Server> caidos = new ArrayList();
            for(int i=0;i<servidores.size();i++){
                Server servidor = servidores.get(i);
                if(servidor.isEstado()){
                    try {
                        Socket socket = new Socket(servidor.getIp(),servidor.getPuerto());
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));;
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        
                        out.print("PING");
                        if(!in.readLine().equalsIgnoreCase("PING"))
                            caidos.add(servidor);
                    } catch (IOException ex) {
                        caidos.add(servidor);
                    }   
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Ping.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(!caidos.isEmpty()) {
                directorio.eliminarServidores(caidos);
                monitor.eliminarConexiones(caidos);
            }
                
        }
    }   
}
