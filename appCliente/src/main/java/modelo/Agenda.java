/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement(name = "agenda")
public class Agenda {
    private final ArrayList<Contacto> contactos;

    public Agenda() {
        this.contactos = new ArrayList<>();
    }

    public Agenda(ArrayList<Contacto> contactos) {
        this.contactos = contactos;
    }
    
    
    @XmlElementWrapper(name = "contactos") // Nombre del contenedor XML
    @XmlElement(name = "contacto")
    public ArrayList<Contacto> getContactos() {
        return contactos;
    }
    
    public boolean agregarContacto(String nickname) {
        Contacto contacto = new Contacto(nickname,nickname);
        for (Contacto c : contactos) {
            if (c.getNicknameReal().equals(contacto.getNicknameReal())) {
                return false; // Ya existe un contacto con ese nickname real
            }
        }
        contactos.add(contacto);
        return true;
    }
    
    public void actualizarContacto(String nicknameReal, String nicknameAgendado)
    {
        Contacto contacto = obtenerContactoPorNickname(nicknameReal);
        contacto.setNicknameAgendado(nicknameAgendado);
    }
    
    public Contacto obtenerContactoPorNickname(String nickname) {
        int i=0;
        while(i<contactos.size() && !(contactos.get(i).getNicknameReal().equals(nickname)))
            i++;
        if (i<contactos.size())
            return contactos.get(i);
        else
            return null;
    }
    
    // Metodos persistencia
    
    public static String toTextoPlano(Agenda a) {
        StringBuilder sb = new StringBuilder();
        for (Contacto contacto : a.contactos) {
            sb.append(Contacto.toTextoPlano(contacto)).append("\n");
        }
        return sb.toString();
    }
    
    public static Agenda fromTextoPlano(String s) {   
        String[] lineas = s.split("\\r?\\n");
        ArrayList<Contacto> contactos = new ArrayList<>();
        for (String linea : lineas) {
            if (!linea.trim().isEmpty()) {
                contactos.add(Contacto.fromTextoPlano(linea.trim()));
            }
        }
        return new Agenda(contactos);
    }
}

