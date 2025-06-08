package sincronizador;
import configuracion.VentanaError;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.*;
import servidor.InfoServidor;
import servidor.Servidor;

public class Sincronizador implements Runnable {
    private int puertoSincronizacion;
    private Servidor servidor;
    private ArrayList<InfoServidor> servidores = new ArrayList<>();
    private ServerSocket sincronizadorSocket;
    private ActionListener controlador;
    private boolean enEjecucion = true, estaListo = false;

    public Sincronizador(int puertoSincronizacion, ActionListener controlador) {
        this.puertoSincronizacion = puertoSincronizacion;
        this.controlador = controlador;
    }

    public void setServidor(Servidor s) {
        this.servidor = s;
    }

    public void detener() {
        enEjecucion = false;
        try {
            if (sincronizadorSocket != null && !sincronizadorSocket.isClosed()) {
                sincronizadorSocket.close();  
                System.out.println("Sincronizador detenido correctamente.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Intenta conectarse al puerto e IP indicados por parametro una cantidad fintia de veces
     * Informa en caso de superar el limite de intentos
     * Se usa para sincronizar entre servidores y tener reintento automatico en caso de que falle alguna conexion
     * @param ip
     * @param puerto
     * @param enviarDatos 
     */
    private void enviarConReintento(String ip, int puerto, Consumer<ObjectOutputStream> enviarDatos) {
        boolean enviado = false;
        int intentos = 0;
        final int MAX_INTENTOS = 100;

        while (!enviado && intentos < MAX_INTENTOS) {
            try (Socket socket = new Socket(ip, puerto);
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

                enviarDatos.accept(out);
                enviado = true;

            } catch (IOException e) {
                intentos++;
                System.err.println("Intento " + intentos + " fallido al conectar con " + ip + ":" + puerto + ". Reintentando en 2s...");
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt(); // Restaurar estado de interrupción
                    return;
                }
            }
        }

        if (!enviado) {
           new VentanaError(null,true,"Error en sincronizacion, reinicie servidores");
        }
    }


    public void sincronizarMensaje(MensajeDeRed mensaje, int puertoSincronizacion) {
        enviarConReintento(servidor.getIP(), puertoSincronizacion, out -> {
            try {
                out.writeObject("SINCRONIZAR MENSAJE");
                out.writeObject(mensaje.getNicknameOrigen());
                out.writeObject(mensaje.getNicknameDestino());
                out.writeObject(mensaje.getContenido());
                out.writeObject(mensaje.getHoraEnvio());
                out.writeObject(mensaje.getMetodoEncriptacion());
            } catch (IOException ex) {
                Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public void sincronizarMensajePendiente(MensajeDeRed mensaje) {
        for (InfoServidor s : servidores) {
            if (esEsteServidor(s)) continue;

            enviarConReintento(s.getIP(), s.getPuertoSincronizacion(), out -> {
                try {
                    out.writeObject("SINCRONIZAR MENSAJE PENDIENTE");
                    out.writeObject(mensaje.getNicknameOrigen());
                    out.writeObject(mensaje.getNicknameDestino());
                    out.writeObject(mensaje.getContenido());
                    out.writeObject(mensaje.getHoraEnvio());
                    out.writeObject(mensaje.getMetodoEncriptacion());
                } catch (IOException ex) {
                    Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    public void sincronizarYaSeEnvioMensajesPendientes(String nickname) {
        for (InfoServidor s : servidores) {
            if (esEsteServidor(s)) continue;

            enviarConReintento(s.getIP(), s.getPuertoSincronizacion(), out -> {
                try {
                    out.writeObject("SINCRONIZAR YA RECIBIO MENSAJES PENDIENTES");
                    out.writeObject(nickname);
                } catch (IOException ex) {
                    Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    public void sincronizarUsuarioRegistrado(String nickname) {
        for (InfoServidor s : servidores) {
            if (esEsteServidor(s)) continue;

            enviarConReintento(s.getIP(), s.getPuertoSincronizacion(), out -> {
                try {
                    out.writeObject("SINCRONIZAR USUARIO REGISTRADO");
                    out.writeObject(nickname);
                } catch (IOException ex) {
                    Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    public void sincronizarUsuarioActivo(String nickname, boolean conectado) {
        for (InfoServidor s : servidores) {
            if (esEsteServidor(s)) continue;

            enviarConReintento(s.getIP(), s.getPuertoSincronizacion(), out -> {
                try {
                    System.out.println((conectado ? "avisar q se conectó " : "avisar q se desconectó ") + nickname);
                    out.writeObject("SINCRONIZAR USUARIO ACTIVO");
                    out.writeObject(nickname);
                    out.writeObject(servidor.getPuertoSincronizacion());
                    out.writeObject(conectado ? "ACTIVO" : "INACTIVO");
                } catch (IOException ex) {
                    Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    public void sincronizarTodo(InfoServidor servidorNoSincronizado) {
        enviarConReintento(servidorNoSincronizado.getIP(), servidorNoSincronizado.getPuertoSincronizacion(), out -> {
            try {
                out.writeObject("SINCRONIZAR TODO");
                out.writeObject(this.servidor.getClientesRegistrados());
                out.writeObject(this.servidor.getClientesActivosGlobales());
                out.writeObject(this.servidor.getMensajesPendientes());
                out.writeObject(servidores);
                out.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    
    private boolean esEsteServidor(InfoServidor s) {
        return this.servidor.getIP().equals(s.getIP())
                && Integer.parseInt(this.servidor.getPuertoSincronizacion()) == s.getPuertoSincronizacion();
    }
   

    private void recibirMensaje(ObjectInputStream in) throws IOException, ClassNotFoundException {
        String nicknameOrigen = (String) in.readObject();
        String nicknameDestino = (String) in.readObject();
        String contenido = (String) in.readObject();
        String horaEnvio = (String) in.readObject();
        String metodoEncriptacion = (String) in.readObject();
        MensajeDeRed mensaje = new MensajeDeRed(nicknameOrigen, nicknameDestino, contenido, horaEnvio, metodoEncriptacion);
        servidor.enviarMensaje(mensaje);
    }

    private void recibirMensajePendiente(ObjectInputStream in) throws IOException, ClassNotFoundException {
        String nicknameOrigen = (String) in.readObject();
        String nicknameDestino = (String) in.readObject();
        String contenido = (String) in.readObject();
        String horaEnvio = (String) in.readObject();
        String metodoEncriptacion = (String) in.readObject();
        MensajeDeRed mensaje = new MensajeDeRed(nicknameOrigen, nicknameDestino, contenido, horaEnvio, metodoEncriptacion);
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
        
        System.out.println("me avisan que "+nickname+" esta "+estado);
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
    
    public boolean estaListo()
    {
        return estaListo;
    }
    
    /**
     * Recibe solicitudes de otros sincronizadores y actua de acuerdo al comando recibido para mantener actualizada y consistente la informacion
    */
    @Override
    public void run() {
        try {
            sincronizadorSocket = new ServerSocket(this.puertoSincronizacion);
            estaListo = true;
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
                    if (enEjecucion)
                    {
                        controlador.actionPerformed(new ActionEvent("Sincronizador caido", 70, "COMPONENTE CAIDO"));
                    }
                       

                } catch (IOException e) {
                    Logger.getLogger(Sincronizador.class.getName()).log(Level.WARNING, "Error de E/S durante la sincronización", e);
                } catch (ClassNotFoundException e) {
                    Logger.getLogger(Sincronizador.class.getName()).log(Level.WARNING, "Clase no encontrada en sincronización", e);
                }
            }

        } catch (IOException e) {
           controlador.actionPerformed(new ActionEvent("Sincronizador caido", 70, "COMPONENTE CAIDO"));
        }
    }

}

