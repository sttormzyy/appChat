/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author Usuario
 */
public class Sincronizador implements Runnable{
    private int puertoSincronizacion;
    private Servidor servidor;
    private ArrayList<InfoServidor> servidores;
    private BufferedReader in;
    private PrintWriter out;   
    
    public Sincronizador(int puertoSincronizacion)
    {
        this.puertoSincronizacion = puertoSincronizacion;
    }
    
    public void setServidor(Servidor servidor)
    {
        this.servidor = servidor;
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

                    writer.println("SINCRONIZAR MENSAJE");
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

                    writer.println("SINCRONIZAR MENSAJE PENDIENTE");
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

                    writer.println("SINCRONIZAR USUARIO REGISTRADO");
                    writer.println(nickname);
                    writer.println(servidor.getPuertoSincronizacion());
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

                    writer.println("SINCRONIZAR USUARIO ACTIVO");
                    writer.println(nickname);
                    
                    socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
    
    public void sincronizarTodo(InfoServidor servidorNoSincronizado) {
      try (Socket socket = new Socket(servidorNoSincronizado.getIP(), servidorNoSincronizado.getPuertoSincronizacion());
          PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
          // 1. Enviar el comando como texto
          writer.println("SINCRONIZAR TODO");

          // 3. Enviar los objetos con ObjectOutputStream
          ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

          out.writeObject(this.servidor.getClientesRegistrados());
          out.writeObject(this.servidor.getClientesActivosGlobales());
          out.writeObject(this.servidor.getMensajesPendientes());
          out.writeObject(servidores);

          out.flush();
      } catch (IOException e) {
          e.printStackTrace();
      }
  }

    
    // RECEPCION DE INFORMACION DE OTROS SINCRONIZADORES
    private void enviarMensaje() throws IOException
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
        
        servidor.agregarMensajePendiente(nicknameDestino,mensaje);
    }
     
    private void recibirUsuarioRegistrado() throws IOException
    {
        String nickname = in.readLine();     
        int puertoSincronizacion = Integer.parseInt(in.readLine());
        servidor.agregarClienteRegistrado(nickname,puertoSincronizacion);
    }
    
    private void recibirUsuarioActivo() throws IOException
    {
        String nickname = in.readLine();
        int puertoSincronizacion = Integer.parseInt(in.readLine());
        boolean activo = Boolean.getBoolean(in.readLine());
        
        if (activo) {
            servidor.agregarClienteActivoGlobal(nickname,puertoSincronizacion);
        } else {
            servidor.eliminarClienteActivo(nickname);
        }
    }
    
    private void recibirTodo(ObjectInputStream in) throws IOException, ClassNotFoundException {
        HashMap<String, Integer> clientesRegistrados = (HashMap<String, Integer>) in.readObject();
        HashMap<String, Integer> clientesActivosGlobales = (HashMap<String, Integer>) in.readObject();
        HashMap<String,ArrayList<MensajeDeRed>>mensajesPendientes = (HashMap<String,ArrayList<MensajeDeRed>>) in.readObject();
        ArrayList<InfoServidor> servidores = (ArrayList<InfoServidor>) in.readObject();
        this.servidores = servidores;
        servidor.sincronizarTodo(clientesRegistrados, clientesActivosGlobales, mensajesPendientes);
    }

    
    // RELACIONADO CON COMUNICACION DIRECTORIO
    
    public void agregarServidor(InfoServidor servidor)
    {
        servidores.add(servidor);
    }
    
    public void eliminarServidor(InfoServidor servidor)
    {
        int i = 0;
        boolean encontrado = false;
        String servidorIP = servidor.getIP();
        int servidorPuertoSincronizacion = servidor.getPuertoSincronizacion();
        
        while(i < servidores.size() && !encontrado) {
            InfoServidor srv = servidores.get(i);
            i += 1;
            if (srv.getIP().equals(servidorIP) && srv.getPuertoSincronizacion() == servidorPuertoSincronizacion) {
                servidores.remove(i);
                encontrado = true;
            }
        }
    }
    

    @Override
    public void run() {
        try (ServerSocket sincronizadorSocket = new ServerSocket(this.puertoSincronizacion)) {
            System.out.println("Sincronizador activo en "+puertoSincronizacion);
            while (true) {
                Socket clienteSocket = sincronizadorSocket.accept();
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
                    String comando = in.readLine();

                    if ("SINCRONIZAR TODO".equals(comando)) {
                        // Paso 2: si es SINCRONIZAR_TODO, pasamos a lectura binaria
                        ObjectInputStream objectIn = new ObjectInputStream(clienteSocket.getInputStream());
                        recibirTodo(objectIn);
                    } else {
                        // Para los demás comandos, usamos el BufferedReader normalmente
                        this.in = in;
                        this.out = new PrintWriter(clienteSocket.getOutputStream(), true);

                        switch (comando) {
                            case "SINCRONIZAR MENSAJE":
                                enviarMensaje();
                                break;
                            case "SINCRONIZAR MENSAJE PENDIENTE":
                                recibirMensajePendiente();
                                break;
                            case "SINCRONIZAR USUARIO REGISTRADO":
                                recibirUsuarioRegistrado();
                                break;
                            case "SINCRONIZAR USUARIO ACTIVO":
                                recibirUsuarioActivo();
                                break;
                            default:
                                System.out.println("Comando no reconocido: " + comando);
                        }
                    }

                    clienteSocket.close();
                } catch (IOException | ClassNotFoundException e) {
                    Logger.getLogger(Sincronizador.class.getName()).log(Level.WARNING, "Error procesando cliente", e);
                }
            }
        } catch (IOException e) {
            Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, "Error al iniciar el socket del servidor", e);
        }
    }


}
