/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static servidor.Constantes.ENVIAR_MENSAJE;
import static servidor.Constantes.ESTADO_VERIFICADO;
import static servidor.Constantes.RECIBIR_CLIENTES;
import static servidor.Constantes.RECIBIR_MENSAJES_PENDIENTES;


/**
 *
 * @author Usuario
 */
public class Sincronizador implements Runnable{
    private Socket socket;
    
    private BufferedReader in;
    private PrintWriter out;
    
    private Servidor servidor;
    private ArrayList<InfoServidor> servidores;
    
    public Sincronizador(Socket socket, Servidor servidor)
    {
        this.socket = socket;
        this.servidor = servidor;
        try{
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e)
        { }
    }
    
    // ENVIO DE INFORMACION A SERVIDORES
    public void sincronizarMensaje(MensajeDeRed mensaje)
    {
        //le avisa al servidor q tiene al destinatario conectado q le envie el mensaje
        String clienteDestino = mensaje.getNicknameDestino();
        int index = 0;
        boolean encontrado = false;
        
        while(index < servidores.size() && !encontrado) {
            InfoServidor servidor = servidores.get(index);
            if (servidor.consultarCliente(clienteDestino)) {
                try (Socket socket = new Socket(servidor.getIP(), servidor.getPuertoSincronizacion());
                    OutputStream os = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(os, true)) {

                    writer.println("SINCRONIZAR_MENSAJE");
                    writer.println(mensaje.getNicknameOrigen());
                    writer.println(mensaje.getNicknameDestino());
                    writer.println(mensaje.getContenido());
                    writer.println(mensaje.getHoraEnvio());
                    
                    socket.close();
                    encontrado = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            index += 1;
        }
    }
    
    public void sincronizarMensajePendiente(MensajeDeRed mensaje)
    {
        //le avisa a todos q agreguen mensaje pendiente
        for(InfoServidor servidor : servidores) {
            try (Socket socket = new Socket(servidor.getIP(), servidor.getPuertoSincronizacion());
                    OutputStream os = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(os, true)) {

                    writer.println("SINCRONIZAR_MENSAJE_PENDIENTE");
                    writer.println(mensaje.getNicknameOrigen());
                    writer.println(mensaje.getNicknameDestino());
                    writer.println(mensaje.getContenido());
                    writer.println(mensaje.getHoraEnvio());
                    
                    socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
     
    public void sincronizarUsuarioRegistrado(String nickname)
    {
        //le avisa a todso q agregue usuario registrado
        
        for(InfoServidor servidor : servidores) {
            try (Socket socket = new Socket(servidor.getIP(), servidor.getPuertoSincronizacion());
                    OutputStream os = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(os, true)) {

                    writer.println("SINCRONIZAR_USUARIO_REGISTRADO");
                    writer.println(nickname);
                    
                    socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void sincronizarUsuarioActivo(String nickname, boolean conectado)
    {
        //le avisa a todso q agregue usuario activo en lista global
        
        for(InfoServidor servidor : servidores) {
            try (Socket socket = new Socket(servidor.getIP(), servidor.getPuertoSincronizacion());
                    OutputStream os = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(os, true)) {

                    writer.println("SINCRONIZAR_USUARIO_ACTIVO");
                    writer.println(nickname);
                    writer.println(conectado);
                    
                    socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
    
    public void sincronizarTodo(InfoServidor servidor)
    {
        // envia por sockets el directorio registrados, activos globales y mensajes pendientes
        
        try (Socket socket = new Socket(servidor.getIP(), servidor.getPuertoSincronizacion());
            OutputStream os = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(os, true)) {

            writer.println("SINCRONIZAR_TODO");
            // LISTA MENSAJES PENDIENTES + CANTIDAD 
            // LISTA CLIENTES REGISTRADOS + CANTIDAD 
            // LISTA CLIENTES ACTIVOS + CANTIDAD 
            // LISTA SERVIDORES CON LA INFO DE CADA UNO (CLIENTES ACTIVOS CONECTADOS A ELLOS) + CANTIDAD 

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    // RECEPCION DE INFORMACION DE SERVIDORES
    private void recibirMensaje() throws IOException
    {
        String nicknameOrigen = in.readLine();
        String nicknameDestino = in.readLine();
        String contenido = in.readLine();
        String horaEnvio = in.readLine();
        
        MensajeDeRed mensaje = new MensajeDeRed(nicknameOrigen, nicknameDestino, contenido, horaEnvio);
        
        servidor.enviarMensaje(mensaje);
    }
    
    private void recibirMensajePendiente() throws IOException
    {
        String nicknameOrigen = in.readLine();
        String nicknameDestino = in.readLine();
        String contenido = in.readLine();
        String horaEnvio = in.readLine();
        
        MensajeDeRed mensaje = new MensajeDeRed(nicknameOrigen, nicknameDestino, contenido, horaEnvio);
        
        servidor.enviarMensaje(mensaje);
    }
     
    private void recibirUsuarioRegistrado() throws IOException
    {
        String nickname = in.readLine();
        
        servidor.agregarClienteActivo(nickname);
    }
    
    private void recibirUsuarioActivo() throws IOException
    {
        String nickname = in.readLine();
        boolean activo = Boolean.getBoolean(in.readLine());
        
        if (activo) {
            servidor.agregarClienteActivo(nickname);
        } else {
            servidor.eliminarClienteActivo(nickname);
        }
    }
    
    private void recibirTodo()
    {
        // RECIBIR LISTA MENSAJES PENDIENTES + CANTIDAD 
        // RECIBIR LISTA CLIENTES REGISTRADOS + CANTIDAD 
        // RECIBIR LISTA CLIENTES ACTIVOS + CANTIDAD 
        // RECIBIR LISTA SERVIDORES CON LA INFO DE CADA UNO (CLIENTES ACTIVOS CONECTADOS A ELLOS) + CANTIDAD 
    }
    
    // RELACIONADO CON COMUNICACION DIRECTORIO
    
    public void agregarServidor(InfoServidor servidor)
    {
        servidores.add(servidor);
    }
    
    public void eliminarServidor(InfoServidor servidor)
    {
        int index = 0;
        boolean encontrado = false;
        
        String servidorIP = servidor.getIP();
        int servidorPuertoSincronizacion = servidor.getPuertoSincronizacion();
        
        while(index < servidores.size() && !encontrado) {
            InfoServidor srv = servidores.get(index);
            if (srv.getIP().equals(servidorIP) && srv.getPuertoSincronizacion() == servidorPuertoSincronizacion) {
                servidores.remove(index);
                encontrado = true;
            }
        }
    }
    
    // METODO RUN PARA ESCUCHAR CONSTANTEMENTE
    
    @Override
    public void run() {
        try {
            while(true){
                String comando = in.readLine();
                switch (comando) {
                    case "SINCRONIZAR_MENSAJE":
                        recibirMensaje();
                        break;
                    case "SINCRONIZAR_MENSAJE_PENDIENTE":                     
                        recibirMensajePendiente();
                        break;
                    case "SINCRONIZAR_USUARIO_REGISTRADO":                     
                        recibirUsuarioRegistrado();
                        break;
                    case "SINCRONIZAR_USUARIO_ACTIVO":
                        recibirUsuarioActivo();
                        break;
                    case "SINCRONIZAR_TODO":
                        recibirTodo();
                        break;
                }

            }
        } catch(IOException e){
           
        }finally{
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    // ANALISIS DE IMPLEMENTACIONES DE LA CLASE
    while(true)
    {
        lee del socket
        switch(comando)
            case sincronizarUsuario
                    actualiza en servidor la info
            case sincronizarMensaje
                    actualiza en servidor la info
            case sincronizarMensajePendiente
                    actualiza en servidor la info
            case sincronizarTodo
                    actualiza en servidor la info
                    servidor.avisarDirectorioEstoyListo()
    }
    
}
