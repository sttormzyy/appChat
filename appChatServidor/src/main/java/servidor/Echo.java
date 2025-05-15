/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Usuario
 */
public class Echo implements Runnable{
    private int puertoMonitoreo;
   
    public Echo(int puertoMonitoreo)
    {
        this.puertoMonitoreo = puertoMonitoreo;
    }
    
    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(puertoMonitoreo)) {
            System.out.println("Echo escuchando en el puerto " + puertoMonitoreo + "...");

            while (true) {
                try (
                    Socket cliente = serverSocket.accept();
                    PrintWriter out = new PrintWriter(cliente.getOutputStream(), true)
                ) {
                    out.println("ECHO");
                    System.out.println("Respondí ECHO a " + cliente.getInetAddress());
                } catch (IOException e) {
                    System.err.println("Error atendiendo cliente: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println("No se pudo abrir el puerto " + puertoMonitoreo + ": " + e.getMessage());
        }
    }

}
