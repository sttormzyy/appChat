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
    private String nickname;
    private Control controlador;
    private String estado;
    
    public Cliente(String nickname, Control controlador){
    	this.controlador = controlador;
    	this.nickname = nickname;
    }
    
    private void conectar()throws IOException{
        try {
        	Socket socket = new Socket(IP_servidor, puerto_servidor);
            System.out.println("Conectado al servidor en " + IP_servidor + " en el puerto " + puerto_servidor);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
            if(this.verificarme(nickname).equals("VERIFICADO")) {
            	estado = "VERIFICADO";
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
            		}
            }
            }else estado = "Nickname repetido";
        }
        catch (UnknownHostException e){
            throw new IOException("Host desconocido " + IP_servidor);
        }
        catch (IOException ex) {
            throw new IOException("Error al conectar al servidor " + IP_servidor);
        }
    }
    
    public String verificarme(String nickname) {
        out.println(nickname);
        String cod;
		try {
			cod = in.readLine();
			return cod;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }
    
    public void enviarMensaje(MensajeDeRed msj) {
        out.println("enviar mensaje");
        out.println(msj.getNicknameOrigen());
        out.println(msj.getNicknameDestino());
        out.println(msj.getContenido());

    }
    
    public ArrayList pedirListaClientes(){
        out.println("recibir clientes");
        try {
			String comando = in.readLine();
			while(!comando.equals("recibir clientes")) {
				comando = in.readLine();
			}
			ArrayList<String> clientes = new ArrayList<String>();
	    	int cantidad_clientes = Integer.parseInt(in.readLine());
	        for (int i = 0; i < cantidad_clientes; i++) {
	        	clientes.add(in.readLine());
	        }
	        return clientes;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }
    
    public String getEstado() {
    	return estado;
    }
    
    @Override
    public void run() {
        try {
            this.conectar();
        } catch (IOException ex) {
        	estado = "IMPOSIBLE CONECTARSE CON EL SERVIDOR";
        }
    }
}