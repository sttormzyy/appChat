/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monitor;

import directorio.Directorio;
import directorio.InfoServidor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class Ping implements Runnable {
    private Directorio directorio;
    private Monitor monitor;

    public Ping(Directorio directorio, Monitor monitor) {
        this.directorio = directorio;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        while (true) {
            ArrayList<InfoServidor> caidos = new ArrayList<>();
            ArrayList<InfoServidor> servidores = new ArrayList<>(directorio.getServidoresActivos());  // Copia de la lista
            
            
            for (InfoServidor servidor : servidores) {
                try (Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress(servidor.getIP(), servidor.getPuertoPing()), 500);
                    socket.setSoTimeout(200); // lee con timeout

                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        
                    //System.out.println("Ping a " + servidor.getPuertoCliente());
                    out.println("PING");

                    String respuesta = in.readLine();
                    //System.out.println("Respuesta de " + servidor.getPuertoCliente() + ": " + respuesta);

                    if (respuesta == null || !respuesta.equals("ECHO")) {
                        System.out.println("Servidor sin respuesta válida: " + servidor.getPuertoCliente());
                        caidos.add(servidor);
                    }

                } catch (IOException ex) {
                     System.out.println("Servidor no respondió: " + servidor.getPuertoCliente());
                     caidos.add(servidor);
                }
                

                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Ping.class.getName()).log(Level.WARNING, "Ping interrumpido", ex);
                }
            }

            if (!caidos.isEmpty()) {
                directorio.eliminarServidores(caidos);
                monitor.eliminarConexiones(caidos);
            }
        }
    }
}
