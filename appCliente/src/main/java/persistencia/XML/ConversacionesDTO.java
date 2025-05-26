/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.XML;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import modelo.Conversacion;

/**
 *
 * @author Usuario
 */
@XmlRootElement(name = "conversacionesDTO")
public class ConversacionesDTO {
    private ArrayList<Conversacion> conversaciones = new ArrayList<>();

    public ConversacionesDTO() {
    }

    public ConversacionesDTO(ArrayList conversaciones) {
        this.conversaciones = conversaciones;
    }
    
    @XmlElementWrapper(name = "conversaciones") // Nombre del contenedor XML
    @XmlElement(name = "conversacion")
    public ArrayList getConversaciones() {
        return conversaciones;
    }

    public void setConversaciones(ArrayList conversacioned) {
        this.conversaciones = conversacioned;
    }
    
    
}
