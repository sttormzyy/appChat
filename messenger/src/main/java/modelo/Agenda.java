/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.ArrayList;

public class Agenda {
    private final ArrayList<Contacto> contactos;

    public Agenda() {
        this.contactos = new ArrayList<>();
    }

    public ArrayList getContactos() {
        return contactos;
    }
    
    public boolean agregarContacto(Contacto contacto) {
    	contactos.add(contacto);
        return true;
    }

    public Contacto obtenerContactoPorNickname(String nickname) {
        int i=0;
        while(i<contactos.size() && (contactos.get(i).getNickname().equals(nickname)))
            i++;
        if (i<contactos.size())
            return contactos.get(i);
        else
            return null;
    }  
}

