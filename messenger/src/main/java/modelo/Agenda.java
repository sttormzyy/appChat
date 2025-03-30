/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.ArrayList;
import java.util.List;

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

    // 📌 Eliminar un contacto por ID
    public boolean eliminarContacto(int id) {
        boolean eliminado = contactos.removeIf(contacto -> contacto.getId() == id);
        return eliminado;
    }

    // 📌 Obtener un contacto por ID (devuelve null si no existe)
    public Contacto obtenerContactoPorId(int id) {
        return contactos.stream()
                .filter(contacto -> contacto.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // 📌 Obtener un contacto por ID (devuelve null si no existe)
    public Contacto obtenerContactoPorIpYPuerto(String ip, int port) {
        return contactos.stream()
                .filter(contacto -> contacto.getIp().equals(ip) && contacto.getPort() == port)
                .findFirst()
                .orElse(null);
    }
    
}

