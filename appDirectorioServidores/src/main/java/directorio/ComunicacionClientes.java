/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package directorio;

import gui.VentanaError;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Usuario
 */
public class ComunicacionClientes implements Runnable{
    private Directorio directorio;
    private ServerSocket servidor;
    private int puerto;
    
    public ComunicacionClientes(Directorio direct,int puerto){
        this.puerto = puerto;
        this.directorio = direct;
    }
    
    /**
     * Escucha conexiones de clientes que requieren un servidor al cual conectarse
     * Crea un hilo para atender cada solicitud
     */
    public void iniciarServidor(){
        try{
        servidor = new ServerSocket(this.puerto);
        System.out.println("Servidor para clientes iniciado en el puerto " + puerto);
        while (true) 
        {
            Socket clienteSocket = servidor.accept();
            System.out.println("Cliente conectado desde: " + clienteSocket.getInetAddress());
            HiloConexionCliente hilo = new HiloConexionCliente(clienteSocket, directorio);
            new Thread(hilo).start();
        }
        }catch(IOException e){
             String mensajeError = "Error al iniciar el directorio, reinicie por favor:\n" + e.getClass().getSimpleName() + " - " + e.getMessage();
             new VentanaError(null, true, mensajeError);
        }
    }

    @Override
    public void run() {
        this.iniciarServidor();
    }
}
