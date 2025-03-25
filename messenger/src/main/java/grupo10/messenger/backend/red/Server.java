/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package grupo10.messenger.backend.red;

import grupo10.messenger.backend.control.Control;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class Server implements Runnable{
    
    private int puerto;
    private Control controlador;
    private boolean conectado = false;

    public Server(int puerto,Control controlador) {
        this.puerto = puerto;
        this.controlador = controlador;
    }
    
    public boolean isConectado(){
        return conectado;
    }
    
    public void iniciarServidor() throws IOException{
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            conectado = true;
            System.out.println("Servidor iniciado en el puerto " + puerto);

            while (true) {
                // Aceptar la conexión de un cliente
                Socket clienteSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde: " + clienteSocket.getInetAddress());

                // Crear un hilo para manejar la conexión con este cliente
                Servercito hilo = new Servercito(clienteSocket,controlador);
                new Thread(hilo).start();  // Iniciar el hilo
            }
        } catch (IOException e) {
            throw new IOException("No se puedo generar conexion con el puerto desde el servidor");
        }
    }
   /* 
    public static void main(String[] args) throws IOException{
        Server servidor = new Server(puerto,Controlador);

        // Crear un hilo y ejecutar el servidor en él
        Thread hiloServidor = new Thread(servidor);
        hiloServidor.start(); // Iniciar el servidor en otro hil
    }
    */
    @Override
    public void run() {
        try {
            this.iniciarServidor();
        } catch (IOException ex) {
            System.out.println("Servidor no se pudo conectar");
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
