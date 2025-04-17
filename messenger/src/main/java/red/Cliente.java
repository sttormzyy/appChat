/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package red;
import controlador.Control;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.MensajeRed;
import servidor.MensajeDeRed;

/**
 *
 * @author Usuario
 */
public class Cliente implements Runnable{
    private final int puerto_servidor = 10000;
    private final String IP_servidor = "localhost";
    private BufferedReader in;
    private PrintWriter out;
    private Control controlador;
    
    public Cliente(String nickname, Control controlador) throws IOException{
        try {
            this.controlador = controlador;
            Socket socket = new Socket(IP_servidor, puerto_servidor);
            System.out.println("Conectado al servidor en " + IP_servidor + " en el puerto " + puerto_servidor);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            verificarme(nickname);
        }catch (UnknownHostException e){
            throw new IOException("Host desconocido " + IP_servidor);
        }
        catch (IOException ex) {
            throw new IOException("Error al conectar al servidor " + IP_servidor);
        }
    }
    
    private void conectar()throws IOException{
        try {
            while(true){
                String comando = in.readLine();
                switch (comando) {
                    case "recibir mensaje":
                        String nicknameOrigen = in.readLine();
                        String nicknameDestino = in.readLine();
                        String contenido = in.readLine();
                        MensajeDeRed msj = new MensajeDeRed(nicknameOrigen, nicknameDestino, contenido);
                        controlador.recibirMensaje(msj);
                        System.out.println("Mensaje recibido del cliente: " + contenido + " desde el nickname " + nicknameOrigen);
                        break;
                    case "recibir clientes":
                        ArrayList<String> clientes = new ArrayList<String>();
                        int cantidad_clientes = Integer.parseInt(in.readLine());
                        for (int i = 0; i < cantidad_clientes; i++) {
                            clientes.add(in.readLine());
                        }
                        controlador.recibirListaClientes(clientes);
                        break;
                }
            }
        }
        catch (IOException ex) {
            throw new IOException("Error al conectar al servidor " + IP_servidor);
        }
    }
    
    public void verificarme(String nickname) {
        out.println(nickname);
    }
    
    public void enviarMensaje(MensajeDeRed msj) {
        out.println("enviar mensaje");
        out.println(msj.getNicknameOrigen());
        out.println(msj.getNicknameDestino());
        out.println(msj.getContenido());

    }
    
    public void pedirListaClientes(){
        out.println("recibir clientes");
    }
    
    @Override
    public void run() {
        try {
            this.conectar();
        } catch (IOException ex) {
            //System.out.println("Fallo alguno en la conexion del cliente con el servidor");
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}