package servidor;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;

public class Sincronizador implements Runnable {
    private int puertoSincronizacion;
    private Servidor servidor;
    private ArrayList<InfoServidor> servidores = new ArrayList<>();
    private ServerSocket sincronizadorSocket;
    private boolean enEjecucion = true;

    public Sincronizador(int puertoSincronizacion) {
        this.puertoSincronizacion = puertoSincronizacion;
    }

    public void setServidor(Servidor s) {
        this.servidor = s;
    }

    public void detener() {
        enEjecucion = false;
        try {
            if (sincronizadorSocket != null && !sincronizadorSocket.isClosed()) {
                sincronizadorSocket.close();  // esto forzará que el accept() termine
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ENVÍO DE INFORMACIÓN A SERVIDORES
    public void sincronizarMensaje(MensajeDeRed mensaje, int puertoSincronizacion) {
        System.out.println("Estoy sincronizando mensaje");
        try (Socket socket = new Socket(servidor.getIP(), puertoSincronizacion);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

            out.writeObject("SINCRONIZAR MENSAJE");
            out.writeObject(mensaje.getNicknameOrigen());
            out.writeObject(mensaje.getNicknameDestino());
            out.writeObject(mensaje.getContenido());
            out.writeObject(mensaje.getHoraEnvio());
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sincronizarMensajePendiente(MensajeDeRed mensaje) {
        for (InfoServidor servidor : servidores) {
            if (this.servidor.getIP().equals(servidor.getIP()) && Integer.parseInt(this.servidor.getPuertoSincronizacion()) == servidor.getPuertoSincronizacion()) {
                continue;
            }
            try (Socket socket = new Socket(servidor.getIP(), servidor.getPuertoSincronizacion());
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

                out.writeObject("SINCRONIZAR MENSAJE PENDIENTE");
                out.writeObject(mensaje.getNicknameOrigen());
                out.writeObject(mensaje.getNicknameDestino());
                out.writeObject(mensaje.getContenido());
                out.writeObject(mensaje.getHoraEnvio());
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sincronizarYaSeEnvioMensajesPendientes(String nickname) {
        for (InfoServidor servidor : servidores) {
            if (this.servidor.getIP().equals(servidor.getIP()) && Integer.parseInt(this.servidor.getPuertoSincronizacion()) == servidor.getPuertoSincronizacion()) {
                continue;
            }
            try (Socket socket = new Socket(servidor.getIP(), servidor.getPuertoSincronizacion());
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
                out.writeObject("SINCRONIZAR YA RECIBIO MENSAJES PENDIENTES");
                out.writeObject(nickname);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sincronizarUsuarioRegistrado(String nickname) {
        for (InfoServidor servidor : servidores) {
            if (this.servidor.getIP().equals(servidor.getIP()) && Integer.parseInt(this.servidor.getPuertoSincronizacion()) == servidor.getPuertoSincronizacion()) {
                continue;
            }
            try (Socket socket = new Socket(servidor.getIP(), servidor.getPuertoSincronizacion());
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
                out.writeObject("SINCRONIZAR USUARIO REGISTRADO");
                out.writeObject(nickname);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sincronizarUsuarioActivo(String nickname, boolean conectado) {
        for (InfoServidor servidor : servidores) {
            if (this.servidor.getIP().equals(servidor.getIP()) && Integer.parseInt(this.servidor.getPuertoSincronizacion()) == servidor.getPuertoSincronizacion()) {
                continue;
            }
            try (Socket socket = new Socket(servidor.getIP(), servidor.getPuertoSincronizacion());
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

                out.writeObject("SINCRONIZAR USUARIO ACTIVO");
                out.writeObject(nickname);
                out.writeObject(this.servidor.getPuertoSincronizacion());
                out.writeObject(conectado ? "ACTIVO" : "INACTIVO");
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sincronizarTodo(InfoServidor servidorNoSincronizado) {
        try (Socket socket = new Socket(servidorNoSincronizado.getIP(), servidorNoSincronizado.getPuertoSincronizacion());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

            out.writeObject("SINCRONIZAR TODO");
            out.writeObject(this.servidor.getClientesRegistrados());
            out.writeObject(this.servidor.getClientesActivosGlobales());
            out.writeObject(this.servidor.getMensajesPendientes());
            out.writeObject(servidores);

            out.flush();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // RECEPCIÓN DE INFORMACIÓN DE OTROS SINCRONIZADORES

    private void recibirMensaje(ObjectInputStream in) throws IOException, ClassNotFoundException {
        String nicknameOrigen = (String) in.readObject();
        String nicknameDestino = (String) in.readObject();
        String contenido = (String) in.readObject();
        String horaEnvio = (String) in.readObject();
        MensajeDeRed mensaje = new MensajeDeRed(nicknameOrigen, nicknameDestino, contenido, horaEnvio);
        servidor.enviarMensaje(mensaje);
    }

    private void recibirMensajePendiente(ObjectInputStream in) throws IOException, ClassNotFoundException {
        String nicknameOrigen = (String) in.readObject();
        String nicknameDestino = (String) in.readObject();
        String contenido = (String) in.readObject();
        String horaEnvio = (String) in.readObject();
        MensajeDeRed mensaje = new MensajeDeRed(nicknameOrigen, nicknameDestino, contenido, horaEnvio);
        servidor.agregarMensajePendiente(nicknameDestino, mensaje);
    }

    private void recibirUsuarioRegistrado(ObjectInputStream in) throws IOException, ClassNotFoundException {
        String nickname = (String) in.readObject();
        servidor.agregarClienteRegistrado(nickname);
    }

    private void recibirUsuarioActivo(ObjectInputStream in) throws IOException, ClassNotFoundException {
        String nickname = (String) in.readObject();
        String puertoString = (String) in.readObject();
        int puertoSincronizacion = Integer.parseInt(puertoString);

        String estado = (String) in.readObject();

        if ("ACTIVO".equals(estado)) {
            servidor.agregarClienteActivoGlobal(nickname, puertoSincronizacion);
        } else {
            servidor.eliminarClienteActivoGlobal(nickname);
        }
    }

    private void recibirTodo(ObjectInputStream in) throws IOException, ClassNotFoundException {
        ArrayList<String> clientesRegistrados = (ArrayList<String>) in.readObject();
        HashMap<String, Integer> clientesActivosGlobales = (HashMap<String, Integer>) in.readObject();
        HashMap<String, ArrayList<MensajeDeRed>> mensajesPendientes = (HashMap<String, ArrayList<MensajeDeRed>>) in.readObject();
        ArrayList<InfoServidor> servidores = (ArrayList<InfoServidor>) in.readObject();
        
        this.servidores = servidores;
        servidor.sincronizarTodo(clientesRegistrados, clientesActivosGlobales, mensajesPendientes);

        // Depuración para ver los objetos recibidos
        System.out.println("Recibido 'SINCRONIZAR TODO' con los siguientes datos:");
        System.out.println("Clientes Registrados: " + clientesRegistrados);
        System.out.println("Clientes Activos Globales: " + clientesActivosGlobales);
        System.out.println("Mensajes Pendientes: " + mensajesPendientes);
        System.out.println("Servidores: " + servidores);
    }

    private void eliminarMensajesPendientes(ObjectInputStream in) {
        try {
            String nickname = (String) in.readObject();
            servidor.eliminarMensajesPendientes(nickname);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void agregarServidor(InfoServidor servidor) {
        servidores.add(servidor);
    }

    public void eliminarServidor(InfoServidor servidor) {
        servidores.removeIf(s -> s.getIP().equals(servidor.getIP()) && s.getPuertoSincronizacion() == servidor.getPuertoSincronizacion());
    }

@Override
public void run() {
    try {
        sincronizadorSocket = new ServerSocket(this.puertoSincronizacion);
        System.out.println("Sincronizador activo en " + puertoSincronizacion);

        while (enEjecucion) {
            try (Socket clienteSocket = sincronizadorSocket.accept();
                 ObjectInputStream in = new ObjectInputStream(clienteSocket.getInputStream())) {

                String comando = (String) in.readObject();
                System.out.println("Comando recibido: " + comando);

                switch (comando) {
                    case "SINCRONIZAR MENSAJE":
                        recibirMensaje(in);
                        break;
                    case "SINCRONIZAR MENSAJE PENDIENTE":
                        recibirMensajePendiente(in);
                        break;
                    case "SINCRONIZAR USUARIO REGISTRADO":
                        recibirUsuarioRegistrado(in);
                        break;
                    case "SINCRONIZAR USUARIO ACTIVO":
                        recibirUsuarioActivo(in);
                        break;
                    case "SINCRONIZAR TODO":
                        recibirTodo(in);
                        break;
                    case "SINCRONIZAR ELIMINAR MENSAJES PENDIENTES":
                        eliminarMensajesPendientes(in);
                        break;
                    default:
                        System.out.println("Comando desconocido: " + comando);
                        break;
                }

            } catch (SocketException e) {
                if (!enEjecucion) {
                    System.out.println("Sincronizador detenido correctamente.");
                } else {
                    Logger.getLogger(Sincronizador.class.getName()).log(Level.WARNING, "Socket cerrado inesperadamente", e);
                }
            } catch (IOException e) {
                Logger.getLogger(Sincronizador.class.getName()).log(Level.WARNING, "Error de E/S durante la sincronización", e);
            } catch (ClassNotFoundException e) {
                Logger.getLogger(Sincronizador.class.getName()).log(Level.WARNING, "Clase no encontrada en sincronización", e);
            }
        }

    } catch (IOException e) {
        Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, "Error iniciando el servidor sincronizador", e);
    }
}

}








/*
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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
*/

/**
 *
 * @author Usuario
 */
/*
public class Sincronizador implements Runnable{
    private int puertoSincronizacion;
    private Servidor servidor;
    private ArrayList<InfoServidor> servidores = new ArrayList<>();
    private ServerSocket sincronizadorSocket;
    private BufferedReader in;
    private PrintWriter out;   
    private boolean enEjecucion =  true;
    
    public Sincronizador(int puertoSincronizacion)
    {
        this.puertoSincronizacion = puertoSincronizacion;
    }
    
    public void setServidor(Servidor s)
    {
        this.servidor = s;
    }
    
    public void detener() {
        enEjecucion = false;
        try {
            if (sincronizadorSocket != null && !sincronizadorSocket.isClosed()) {
                sincronizadorSocket.close();  // esto forzará que el accept() termine
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // ENVIO DE INFORMACION A SERVIDORES
    public void sincronizarMensaje(MensajeDeRed mensaje, int puertoSincronizacion)
    {
        System.out.println("estoy sincroniadno mensaje3");
        try (Socket socket = new Socket(servidor.getIP(), puertoSincronizacion);
            OutputStream os = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(os, true)) {

            writer.println("SINCRONIZAR MENSAJE");
            writer.println(mensaje.getNicknameOrigen());
            writer.println(mensaje.getNicknameDestino());
            writer.println(mensaje.getContenido());
            writer.println(mensaje.getHoraEnvio());
            
            socket.close();
            } catch (Exception e) {
              e.printStackTrace();
         }   
    }
    
    public void sincronizarMensajePendiente(MensajeDeRed mensaje)
    {
        //le avisa a todos q agreguen mensaje pendiente
        for(InfoServidor servidor : servidores) {
            if(this.servidor.getIP().equals(servidor.getIP()) && Integer.parseInt(this.servidor.getPuertoSincronizacion()) == servidor.getPuertoSincronizacion())
            {
                continue;
            }
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
    
    public void sincronizarYaSeEnvioMensajesPendientes(String nickname)
    {
        for(InfoServidor servidor : servidores) {
            if(this.servidor.getIP().equals(servidor.getIP()) && Integer.parseInt(this.servidor.getPuertoSincronizacion()) == servidor.getPuertoSincronizacion())
            {
                continue;
            }
            try (Socket socket = new Socket(servidor.getIP(), servidor.getPuertoSincronizacion());
                    OutputStream os = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(os, true)) {
                    writer.println("SINCRONIZAR YA RECIBIO MENSAJES PENDIENTES");
                    writer.println(nickname);
                    socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
     
     
    public void sincronizarUsuarioRegistrado(String nickname)
    {
        for(InfoServidor servidor : servidores) {
            if(this.servidor.getIP().equals(servidor.getIP()) && Integer.parseInt(this.servidor.getPuertoSincronizacion()) == servidor.getPuertoSincronizacion())
            {
                continue;
            }
            try (Socket socket = new Socket(servidor.getIP(), servidor.getPuertoSincronizacion());
                    OutputStream os = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(os, true)) {
                    System.out.println("AVISAR Q SE REGISTRO "+nickname);
                    writer.println("SINCRONIZAR USUARIO REGISTRADO");
                    writer.println(nickname);
                    socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void sincronizarUsuarioActivo(String nickname, boolean conectado)
    {
        for(InfoServidor servidor : servidores) {
            if(this.servidor.getIP().equals(servidor.getIP()) && Integer.parseInt(this.servidor.getPuertoSincronizacion()) == servidor.getPuertoSincronizacion())
            {
                continue;
            }
            try (Socket socket = new Socket(servidor.getIP(), servidor.getPuertoSincronizacion());
                    OutputStream os = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(os, true)) {

                    writer.println("SINCRONIZAR USUARIO ACTIVO");
                    writer.println(nickname);
                    writer.println(this.servidor.getPuertoSincronizacion());
                    if(conectado)
                        writer.println("ACTIVO");
                    else
                        writer.println("INACTIVO");
                    socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
    
    public void sincronizarTodo(InfoServidor servidorNoSincronizado) {
      try (Socket socket = new Socket(servidorNoSincronizado.getIP(), servidorNoSincronizado.getPuertoSincronizacion());
          PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
          writer.println("SINCRONIZAR TODO");
          ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
          out.writeObject(this.servidor.getClientesRegistrados());
          out.writeObject(this.servidor.getClientesActivosGlobales());
          out.writeObject(this.servidor.getMensajesPendientes());
          out.writeObject(servidores);

          
          System.out.println("Enviando clientes registrados:");
          for (String cliente : this.servidor.getClientesRegistrados()) {
                  System.out.println("  - " + cliente);
          }


              System.out.println("Enviando clientes activos globales:");
              for (Map.Entry<String, Integer> entry : this.servidor.getClientesActivosGlobales().entrySet()) {
                  System.out.println("  - " + entry.getKey() + " => Puerto: " + entry.getValue());
              }


              System.out.println("Enviando mensajes pendientes:");
              for (Map.Entry<String, ArrayList<MensajeDeRed>> entry : this.servidor.getMensajesPendientes().entrySet()) {
                  System.out.println("  - " + entry.getKey() + ":");
                  for (MensajeDeRed mensaje : entry.getValue()) {
                      System.out.println("      * " + mensaje);
                  }
              }


              System.out.println("Enviando servidores conocidos:");
              for (InfoServidor s : servidores) {
                  System.out.println("  - IP: " + s.getIP() +
                                     ", Cliente: " + s.getPuertoCliente() +
                                     ", Sincronización: " + s.getPuertoSincronizacion() +
                                     ", ParaDirectorio: " + s.getPuertoParaDirectorio() +
                                     ", Ping: " + s.getPuertoPing());
              }


          out.flush();
          socket.close();
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
        System.out.println("me pidieronq  envie "+contenido+" de "+nicknameOrigen+ " para "+nicknameDestino);
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
        servidor.agregarClienteRegistrado(nickname);
    }
    
    private void recibirUsuarioActivo() throws IOException
    {
        String nickname = in.readLine();
        int puertoSincronizacion = Integer.parseInt(in.readLine());
        String estado = in.readLine();
        
        if (estado.equals("ACTIVO")) {
            servidor.agregarClienteActivoGlobal(nickname,puertoSincronizacion);
        } else {
            servidor.eliminarClienteActivoGlobal(nickname);
        }
    }
    
    private void recibirTodo(ObjectInputStream in) throws IOException, ClassNotFoundException {
        ArrayList<String> clientesRegistrados = (ArrayList<String>) in.readObject();
        HashMap<String, Integer> clientesActivosGlobales = (HashMap<String, Integer>) in.readObject();
        HashMap<String,ArrayList<MensajeDeRed>> mensajesPendientes = (HashMap<String,ArrayList<MensajeDeRed>>) in.readObject();
        ArrayList<InfoServidor> servidores = (ArrayList<InfoServidor>) in.readObject();
        
for (InfoServidor servidor : servidores) {
    System.out.println("Servidor:");
    System.out.println("  IP: " + servidor.getIP());
    System.out.println("  Puerto Cliente: " + servidor.getPuertoCliente());
    System.out.println("  Puerto Sincronización: " + servidor.getPuertoSincronizacion());
    System.out.println("  Puerto para Directorio: " + servidor.getPuertoParaDirectorio());
    System.out.println("  Puerto Ping: " + servidor.getPuertoPing());
    System.out.println("--------------------------------");
}
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
    
    private void eliminarMensajesPendientes()
    {
        String nickname = "";
        try {
            nickname = in.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, null, ex);
        }
        servidor.eliminarMensajesPendientes(nickname);
    }

@Override
public void run() {
    try {
        sincronizadorSocket = new ServerSocket(this.puertoSincronizacion);
        System.out.println("Sincronizador activo en " + puertoSincronizacion);

        while (enEjecucion) {
            Socket clienteSocket = sincronizadorSocket.accept();

            try (
                // Aquí se crean los flujos para leer la entrada de texto (BufferedReader)
                BufferedReader in = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
                // El ObjectInputStream solo se inicializa cuando es necesario
                ObjectInputStream objectIn = new ObjectInputStream(clienteSocket.getInputStream());
                PrintWriter out = new PrintWriter(clienteSocket.getOutputStream(), true)
            ) {
                String comando = in.readLine();  // Lee el comando de texto (como "SINCRONIZAR TODO")

                if ("SINCRONIZAR TODO".equals(comando)) {
                    // Después de recibir el comando, lee los objetos con ObjectInputStream
                    recibirTodo(objectIn);  
                } else {
                    this.in = in;
                    this.out = out;

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
                        case "SINCRONIZAR YA RECIBIO MENSAJES PENDIENTES":
                            eliminarMensajesPendientes();
                            break;
                        default:
                            System.out.println("Comando no reconocido: " + comando);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                Logger.getLogger(Sincronizador.class.getName()).log(Level.WARNING, "Error procesando cliente", e);
            }
        }
    } catch (IOException e) {
        if (enEjecucion) {
            Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, "Error al iniciar el socket del servidor", e);
        } else {
            System.out.println("Sincronizador detenido correctamente.");
        }
    }
}


}
*/