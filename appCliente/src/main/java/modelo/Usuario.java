/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.ArrayList;

public class Usuario {
    private ArrayList<Conversacion> conversaciones = null;
    private Agenda agenda = null;
    private String nickname; 
    private String ip;
    private int puerto;
    
    public Usuario(String nickname, String ip, int puerto) {
        this.nickname = nickname;
        this.ip = ip;
        this.puerto = puerto;
        this.conversaciones = new ArrayList();
        this.agenda = new Agenda();
    }

    public ArrayList getContactos() {
        return agenda.getContactos();
    }
    
    public Agenda getAgenda() {
        return this.agenda;
    }
    
    public ArrayList<Conversacion> getConversaciones() {
        return conversaciones;
    }
    
    public Contacto obtenerContactoPorNickname(String nickname) 
    {
        return agenda.obtenerContactoPorNickname(nickname);
    }
    
    public boolean agregarContacto(String nickname)
    {
        return agenda.agregarContacto(nickname);
    }
    
    public void actualizarContacto(String nicknameReal, String nicknameAgendado)
    {
        agenda.actualizarContacto(nicknameReal,nicknameAgendado);
    }
             
    public Conversacion buscarConversacionPorNickname(String nickname){
        int i=0;
        while(i<conversaciones.size() && !(conversaciones.get(i).getNicknameReal().equals(nickname)))
            i++;
        if (i<conversaciones.size())
            return conversaciones.get(i);
        else
            return null;
    }

    public boolean agregarConversacion(Conversacion conversacion) {
        conversaciones.add(conversacion);
        return true;
    }
    
    public void agregarMensaje(String nickname, String contenido, boolean esMio)
    {
        Conversacion conversacion = buscarConversacionPorNickname(nickname);
        conversacion.agregarMensaje(contenido,esMio);      
    }
    
    public Conversacion iniciarConversacion(Contacto contacto) {
        Conversacion nuevaConversacion = new Conversacion(contacto);
        if (this.agregarConversacion(nuevaConversacion)) { 
            return nuevaConversacion;
        }
        return null;
    }

    public String getNickname() {
        return nickname;
    }

    public String getIp() {
        return ip;
    }

    public int getPuerto() {
        return puerto;
    }  
}


