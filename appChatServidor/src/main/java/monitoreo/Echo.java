/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monitoreo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Se encarga de devolver el ECHO a un PING recibido por el monitor
 * @author Usuario
 */
public class Echo implements Runnable {
    private int puertoPing;
    private volatile boolean enEjecucion = true, estaListo = false;
    private ServerSocket serverSocket;
    private ActionListener controlador;

    public Echo(int puertoPing, ActionListener controlador) {
        this.puertoPing = puertoPing;     
        this.controlador = controlador;
    }

    public void detener() {
        enEjecucion = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Echo detenido correctamente.");
            }
        } catch (IOException e) {
            System.err.println("Error cerrando Echo: " + e.getMessage());
        }
    }

    public boolean estaListo()
    {
        return estaListo;
    }
       
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(puertoPing);
            estaListo = true;
            System.out.println("Echo escuchando en el puerto " + puertoPing + "...");

            while (enEjecucion) {
                Socket cliente = null;
                try {
                    cliente = serverSocket.accept();

                    BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                    PrintWriter out = new PrintWriter(cliente.getOutputStream(), true);

                    String mensaje = in.readLine();
                    //System.out.println("Echo recibió: " + mensaje + " de " + cliente.getInetAddress());

                    // Siempre responde con ECHO
                    out.println("ECHO");

                } catch (IOException e) {
                    if (enEjecucion) {
                         controlador.actionPerformed(new ActionEvent("Echo caido", 100, "COMPONENTE CAIDO"));
                    }
                } finally {
                    if (cliente != null && !cliente.isClosed()) {
                        try {
                            cliente.close();
                        } catch (IOException e) {
                            System.err.println("Error cerrando cliente en Echo: " + e.getMessage());
                        }
                    }
                }
            }

        } catch (IOException e) {
           controlador.actionPerformed(new ActionEvent("Echo caido", 100, "COMPONENTE CAIDO"));
        }
    }
}


