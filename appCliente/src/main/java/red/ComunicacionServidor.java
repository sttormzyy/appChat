package red;

import gui.VentanaError;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static resources.Constantes.*;

public class ComunicacionServidor implements Runnable, IEmisor {
    private IReceptor receptor;
    private BufferedReader in;
    private PrintWriter out;
    private Socket socketCliente;
    private String nickname;
    private String IPDirectorio;
    private int puertoDirectorio;
    private String estado = "";
    private volatile boolean enEjecucion = true;

    public ComunicacionServidor(String nickname, IReceptor receptor, String IPDirectorio, int puertoDirectorio) {
        this.receptor = receptor;
        this.nickname = nickname;
        this.IPDirectorio = IPDirectorio;
        this.puertoDirectorio = puertoDirectorio;
    }

    private String obtenerServidor() {
        try (Socket socket = new Socket(IPDirectorio, puertoDirectorio);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String respuesta = in.readLine();
            out.println("RECIBIDO");
            System.out.println(respuesta);
            return (respuesta == null || respuesta.equals("NO HAY SERVIDORES")) ? null : respuesta;

        } catch (Exception ex) {
            System.out.println(ex + " excepcion en obtener servidor");
            return null;
        }
    }

    private void conectarServidor(String ipServidor, int puertoServidor) {
        try {
            this.socketCliente = new Socket(ipServidor, puertoServidor);
            this.in = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
            this.out = new PrintWriter(socketCliente.getOutputStream(), true);
        } catch (Exception ex) {
            System.out.println("Exepcion conectando al servidor "+ex);
            return ;
        }

        estado = verificarme(nickname);
          System.out.println("entre a un servidor");
        if (estado.equals(ESTADO_VERIFICADO)) {
            try {
                while (enEjecucion) {
   
                    String comando = in.readLine();
                    if (comando == null) break;

                    switch (comando) {
                        case RECIBIR_MENSAJE:
                            
                            String origen = in.readLine();
                            String destino = in.readLine();
                            String contenido = in.readLine();
                            String hora = in.readLine();
                            System.out.println("recibi "+contenido);
                            receptor.recibirMensaje(new MensajeDeRed(origen, destino, contenido, hora));
                            break;

                        case RECIBIR_CLIENTES:
                            ArrayList<String> clientes = new ArrayList<>();
                            int cantidad = Integer.parseInt(in.readLine());
                            for (int i = 0; i < cantidad; i++) {
                                clientes.add(in.readLine());
                            }
                            receptor.recibirListaClientes(clientes);
                            break;
                    }
                }
            } catch (IOException e) {
                if (enEjecucion) {
                    System.out.println("Conexión interrumpida inesperadamente");
                } else {
                    System.out.println("Cliente detenido intencionalmente.");
                }
            }
        }
        else
            this.detener();
            return;
}


    public String verificarme(String nickname) {
        out.println(nickname);
        try {
            return in.readLine();
        } catch (IOException ex) {
            Logger.getLogger(ComunicacionServidor.class.getName()).log(Level.SEVERE, null, ex);
            return "FALLO AL CONECTAR AL SERVIDOR";
        }
    }

    public boolean enviarMensaje(MensajeDeRed msj) {
        try {
            out.println(ENVIAR_MENSAJE);
            out.println(msj.getNicknameOrigen());
            out.println(msj.getNicknameDestino());
            out.println(msj.getContenido());
            out.println(msj.getHoraEnvio());

            if (out.checkError()) {
                new VentanaError(null, true, "No se pudo enviar el mensaje");
                return false;
            }
            return true;

        } catch (Exception e) {
            Logger.getLogger(ComunicacionServidor.class.getName()).log(Level.SEVERE, "Error al enviar mensaje", e);
            return false;
        }
    }

    public void pedirListaClientes() {
        out.println(RECIBIR_CLIENTES);
    }

    public void pedirMensajesPendientes() {
        out.println(RECIBIR_MENSAJES_PENDIENTES);
    }

    public String getEstado() {
        return estado;
    }

    @Override
    public void run() {
        int intentos = 5;
        String iPyPuerto = obtenerServidor();
        while (iPyPuerto != null && enEjecucion) {
            String[] partes = iPyPuerto.split(":");
            conectarServidor(partes[0], Integer.parseInt(partes[1]));
            if (enEjecucion) {
                intentos-=1;
                if(intentos>0)
                {
                    iPyPuerto = obtenerServidor();
                    //new VentanaError(null,true,"Reconectando");
                }else
                    receptor.detener();
            }
        }

        if (enEjecucion) {
            new VentanaError(null, true, "IMPOSIBLE CONECTAR CON SERVIDOR");
        }
    }

    public void detener() {
        enEjecucion = false;
        
        System.out.println("deteniendo hilo cleinte");
        try {
            if (in != null) in.close();
        } catch (IOException e) {
            Logger.getLogger(ComunicacionServidor.class.getName()).log(Level.WARNING, "Error al cerrar BufferedReader", e);
        }

        if (out != null) out.close();

        try {
            if (socketCliente != null && !socketCliente.isClosed()) {
                socketCliente.close();
            }
        } catch (IOException e) {
            Logger.getLogger(ComunicacionServidor.class.getName()).log(Level.WARNING, "Error al cerrar socket", e);
        }
    }
}
