/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Usuario
 */
public class Servidor {
    private static final int PUERTO = 10000;
    private ArrayList<String> clientes = new ArrayList<>();
    private HashMap<String, HiloServidor> clientesActivos = new HashMap<>();
    private HashMap<String, ArrayList<MensajeDeRed>> mensajesPendientes = new HashMap<>();   
    private ServerSocket serverSocket;
    private boolean ejecutando = true;

    public static void main(String[] args) {
        Servidor srv = new Servidor();
        
        // Liberar el puerto al cerrar
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Apagando servidor...");
            srv.detenerServidor();
        }));

        try {
            srv.iniciarServidor(PUERTO);
        } catch (IOException e) {
            System.err.println("No se pudo iniciar el servidor: " + e.getMessage());
        }
    }

    public void iniciarServidor(int puerto) throws IOException {
        serverSocket = new ServerSocket(puerto);
        System.out.println("Servidor iniciado en el puerto " + puerto);

        while (ejecutando) {
            try {
                Socket clienteSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde: " + clienteSocket.getInetAddress());

                HiloServidor hilo = new HiloServidor(clienteSocket, this);
                new Thread(hilo).start();
            } catch (IOException e) {
                if (ejecutando) {
                    System.err.println("Error al aceptar conexión: " + e.getMessage());
                }
            }
        }
    }

    public void detenerServidor() {
        ejecutando = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Servidor detenido y puerto liberado.");
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar el servidor: " + e.getMessage());
        }
    }
    
    public synchronized void enviarMensaje(MensajeDeRed msj) {
        String destino = msj.getNicknameDestino();
        HiloServidor socket = clientesActivos.get(destino);

        if (socket != null) {
            socket.enviarMensaje(msj);
        } else {
            mensajesPendientes
            .computeIfAbsent(destino, k -> new ArrayList<>())
            .add(msj);
        }
    }   

    public synchronized boolean verificarClienteActivo(String nickname) {
        if (clientesActivos.get(nickname) != null) {
            return false;
        }
        return true;
    }
    
    public synchronized void agregarClienteActivo(String nickname, HiloServidor srvEscucha){
        if(!clientes.contains(nickname)){
            clientes.add(nickname);
        }
        clientesActivos.put(nickname, srvEscucha);
    }
    
    public void enviarMensajesPendientes(String nickname, HiloServidor srvEscucha)
    {
        ArrayList<MensajeDeRed> msjPendientes = mensajesPendientes.get(nickname);
        if (msjPendientes != null) {
            while(!msjPendientes.isEmpty()) {
                srvEscucha.enviarMensaje(msjPendientes.get(0));
                msjPendientes.remove(0);
            }
            mensajesPendientes.remove(nickname);
        }
    }
    public synchronized void eliminarClienteActivo(String nickname) {
        clientesActivos.remove(nickname);
    }
    
    public ArrayList<String> obtenerListaClientes() {
        return clientes;
    }
}