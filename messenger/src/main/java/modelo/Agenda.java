/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.ArrayList;

public class Agenda {
    private final ArrayList<Contacto> contactos;

    // Constructor
    public Agenda() {
        this.contactos = new ArrayList<>();
    }

    // Getter de la lista de contactos
    public ArrayList<Contacto> getContactos() {
        return contactos;
    }
    
    // 📌 Agregar un contacto
    public boolean agregarContacto(Contacto contacto) {
        contactos.add(contacto);
        return true;
    }

    // 📌 Obtener un contacto por ID (devuelve null si no existe)
    public Contacto obtenerContactoPorIpYPuerto(String ip, int port) {
        int i=0;
        while(i<contactos.size() && (!contactos.get(i).getIp().equals(ip) || contactos.get(i).getPuerto()!=port))
            i++;
        if(i<contactos.size())
            return contactos.get(i);
        else
            return null;
    }
    
}

