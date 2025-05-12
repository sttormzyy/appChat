/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


/**
 *
 * @author Usuario
 */
public class Sincronizador implements Runnable{
    private BufferedReader in;
    private PrintWriter out;
    private Servidor servidor;
    private ArrayList<InfoServidor> servidores;
    
    public Sincronizador(Servidor servidor)
    {
        this.servidor = servidor;
    }
    
    public void sincronizarMensaje(MensajeDeRed mensaje)
    {
        //le avisa al servidor q tiene al destinatario conectado q le envie el mensaje
    }
    
    public void sincronizarMensajePendiente(MensajeDeRed mensaje)
    {
        //le avisa a todos q agreguen mensaje pendiente
    }
     
    public void sincronizarUsuarioRegistrado(String nickname)
    {
        //le avisa a todso q agregue usuario registrado
    }
    
    public void sincronizarUsuarioActivo(String nickname, boolean conectado)
    {
        //le avisa a todso q agregue usuario activo en lista global
    }
    
    public void agregarServidor(InfoServidor servidor)
    {
        
    }
    
    public void eliminarServidor(InfoServidor servidor)
    {
        
    }
    
    public void sincronizarTodo()
    {
        // envia por sockets el directorio registrados, activos globales y mensajes pendientes
    }
    
    
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
    
    @Override
    public void run() {
        try {
            this.conectar();
        } catch (IOException ex) {
        }
    }
}
