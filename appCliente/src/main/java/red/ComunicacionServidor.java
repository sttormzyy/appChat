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
    private int intentos=5;

    public ComunicacionServidor(String nickname, IReceptor receptor, String IPDirectorio, int puertoDirectorio) {
        this.receptor = receptor;
        this.nickname = nickname;
        this.IPDirectorio = IPDirectorio;
        this.puertoDirectorio = puertoDirectorio;
    }

    /**
     * Se conecta al directorio para obtener la IP y el puerto del servidor al que debe conectarse el cliente.
     *
     * @return IP y puerto del servidor al que se conectará el cliente, o "NO HAY SERVIDORES" en caso de error.
     */
    private String obtenerServidor() {
        try (
            Socket socket = new Socket(IPDirectorio, puertoDirectorio);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            // Leer la respuesta del directorio
            String respuesta = in.readLine();
            System.out.println("Directorio devolvió servidor: " + respuesta);

            // Enviar confirmación de recepción
            out.println("RECIBIDO");

            if (respuesta == null) {
                new VentanaError(null, true,
                    "<html>Error conectando con el servidor.</html>");
                receptor.detener();
                return "NO HAY SERVIDORES";
            }

            if ("NO HAY SERVIDORES".equals(respuesta)) {
                new VentanaError(null, true,
                    "<html>No hay servidores disponibles.<br>Por favor, reintente más tarde.</html>");
                receptor.detener();
                return "NO HAY SERVIDORES";
            }

            return respuesta;

        } catch (Exception ex) {
            new VentanaError(null, true,
                "<html>Error conectando con el servidor.</html>");
            receptor.detener();
            return "NO HAY SERVIDORES";
        }
    }


    /**
     * Se conecta al servidor de acuerdo a los parametros y la utiliza para enviar y recibir mensajes
     * @param ipServidor
     * @param puertoServidor 
     */
    private void conectarServidor(String ipServidor, int puertoServidor) {
        try {
            this.socketCliente = new Socket(ipServidor, puertoServidor);
            this.in = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
            this.out = new PrintWriter(socketCliente.getOutputStream(), true);
        } catch (Exception ex) {
            return ;
        }

        estado = verificarme(nickname);
        System.out.println("entre a un servidor con estado "+estado);
        if (estado.equals(ESTADO_VERIFICADO)) {
            intentos+=1;
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
                            String metodoEncriptacion = in.readLine();
                            receptor.recibirMensaje(new MensajeDeRed(origen, destino, contenido, hora, metodoEncriptacion));
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
            } catch (Exception e) {
                return;
            }
        }
        else
        {
            enEjecucion = false;
            return;
        }
}

   /**
    * Chequea que en el sistema no haya nadie activo con ese nombre
    * @param nickname
    * @return 
    */
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
            out.println(msj.getMetodoEncriptacion());
            
            System.out.println("ENVIO "+msj.getContenido());
            System.out.println("con metodo "+msj.getMetodoEncriptacion());
         
            if (out.checkError()) {
                new VentanaError(null, true, "Error en conexion con servidor, reinicie la aplicaicon");
                receptor.detener();
                return false;
            }
            return true;

        } catch (Exception e) {
            Logger.getLogger(ComunicacionServidor.class.getName()).log(Level.SEVERE, "Error al enviar mensaje", e);
            return false;
        }
    }

    /**
     * Pide al servidor la lista de clientes registrados para saber que contactos se puede agendar
     */
    public void pedirListaClientes() {
        out.println(RECIBIR_CLIENTES);
    }
    
    /**
     * Al iniciar la aplicacion, se le pide al servidor que envie los mensajes pendientes que el cliente que acaba de ingresar recibio
     * mientras no estaba conectado 
     */
    public void pedirMensajesPendientes() {
        out.println(RECIBIR_MENSAJES_PENDIENTES);
    }

    public String getEstado() {
        return estado;
    }

    /**
     * Se conecta al directorio para obtener un servidor al cual conectarse
     * Luego se conecta al servidor que obtenga a partir del directorio. 
     * En caso de perder la conexion, reintenta con un servidor nuevo
     * En caso de no haber servidores o que haya un usuario activo con el mismo nickname, informa y cierra la aplicacion
     */
    @Override
    public void run() {
        String iPyPuerto = obtenerServidor();
        while (enEjecucion && !iPyPuerto.equalsIgnoreCase("NO HAY SERVIDORES")) {
            String[] partes = iPyPuerto.split(":");
            conectarServidor(partes[0], Integer.parseInt(partes[1]));
            if (enEjecucion) {
                intentos-=1;
                if(intentos>0)
                {
                    System.out.println("intento "+(intentos-4));
                    iPyPuerto = obtenerServidor();
                    try {
                        Thread.sleep(2000); 
                    } catch (InterruptedException e) {}
                
                }else
                {
                    new VentanaError(null,true,"Imposible conectar con servidor, reinicie la aplicacion");
                    receptor.detener();
                }
            }
        }
        this.detener();
    }

    public void detener() {
        enEjecucion = false;
        try {
            if (socketCliente != null && !socketCliente.isClosed()) socketCliente.close(); 
            if (in != null) in.close();
            if (out != null) out.close();
        } catch (IOException e) { }
        }
}
