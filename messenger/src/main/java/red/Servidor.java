/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package red;

import controlador.Control;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.MensajeRed;

/**
 *
 * @author Usuario
 */
public class Servidor implements Runnable{
    private int puerto;
    private Control controlador;
    private boolean conectado = false;

    public Servidor(int puerto,Control controlador) {
        this.puerto = puerto;
        this.controlador = controlador;
    }
    
    public boolean isConectado(){
        return conectado;
    }
    
    public void iniciarServidor() throws IOException{
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            conectado = true;
            //System.out.println("Servidor iniciado en el puerto " + puerto);

            while (true) {
                Socket clienteSocket = serverSocket.accept();
                //System.out.println("Cliente conectado desde: " + clienteSocket.getInetAddress());

                // Crear un hilo para manejar la conexión con este cliente
                Servercito hilo = new Servercito(clienteSocket,controlador);
                new Thread(hilo).start();  // Iniciar el hilo
            }
        } catch (IOException e) {
            throw new IOException("No se puedo generar conexion con el puerto desde el servidor");
        }
    }
    
    public boolean enviarMensaje(MensajeRed msj){
        Cliente cliente = new Cliente(msj);
        Thread hilo = new Thread(cliente);
        hilo.start();
        try {
            hilo.join(); //hilo principal de ejecucion espera a que el hilo cliente termine
        } catch (InterruptedException ex) {
            Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println(cliente.isConectado());
        return cliente.isConectado();
    }
      
    @Override
    public void run() {
        try {
            this.iniciarServidor();
        } catch (IOException ex) {
            //System.out.println("Servidor no se pudo conectar");
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}