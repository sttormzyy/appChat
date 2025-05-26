/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.XML;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Contacto;
import modelo.Conversacion;
import modelo.Mensaje;
import modelo.Usuario;
import persistencia.IPersistenciaConversaciones;
import resources.Constantes;

/**
 *
 * @author Windows11
 */
public class XMLPersitenciaConversaciones implements IPersistenciaConversaciones {
    
    @Override
    public void persistirConversaciones(ArrayList<Conversacion> conversacion, String nickname) {
        
        JAXBContext context;
        try {
            ConversacionesDTO c = new ConversacionesDTO(conversacion);
            context = JAXBContext.newInstance(ConversacionesDTO.class, Conversacion.class, Mensaje.class, Contacto.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(c, new File(nickname+"_conversacion"+Constantes.XML));
        } catch (JAXBException ex) {
            Logger.getLogger(XMLPersitenciaConversaciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void despersistirConversaciones(Usuario usuario, String nickname) {
        JAXBContext context;
        try {
            context = JAXBContext.newInstance(ConversacionesDTO.class, Conversacion.class, Mensaje.class, Contacto.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            ConversacionesDTO dto = (ConversacionesDTO) unmarshaller.unmarshal(new File(nickname+"_conversacion"+Constantes.XML));
            ArrayList<Conversacion> conversaciones = dto.getConversaciones();
            for(Conversacion c: conversaciones){
                c.setContacto(usuario.obtenerContactoPorNickname(c.getNicknameReal()));
                usuario.getConversaciones().add(c);
            }
        } catch (JAXBException ex) {
            Logger.getLogger(XMLPersitenciaConversaciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
