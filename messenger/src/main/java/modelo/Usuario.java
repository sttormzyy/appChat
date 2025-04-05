/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.ArrayList;


public class Usuario extends Persona {
    private ArrayList<Conversacion> conversaciones = null;
    private Agenda agenda = null;

    public Usuario(String nickname, String ip, int puerto) {
        super(nickname, ip, puerto);
        this.conversaciones = new ArrayList();
        this.agenda = new Agenda();
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public ArrayList<Conversacion> getConversaciones() {
        return conversaciones;
    }
    
    public Conversacion buscarConversacion(String ip, int puerto){
        int i=0;
        while(i<conversaciones.size() && (!conversaciones.get(i).getContacto().getIp().equals(ip) || conversaciones.get(i).getContacto().getPuerto() != puerto))
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
    
    public boolean agregarContacto(Contacto contacto) {
        return agenda.agregarContacto(contacto);
    }

    public Conversacion iniciarConversacion(Contacto contacto) {
        Conversacion nuevaConversacion = new Conversacion(contacto);

        if (this.agregarConversacion(nuevaConversacion)) { 
            return nuevaConversacion;
        }
        return null;
    }
}


