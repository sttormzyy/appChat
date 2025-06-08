/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.XML;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Agenda;
import modelo.Contacto;
import persistencia.IPersistenciaAgenda;
import resources.Constantes;

/**
 *
 * @author Windows11
 */
public class XMLPersistenciaAgenda implements IPersistenciaAgenda {
    
    private static final String CARPETA = "Persistencia" + File.separator;

    @Override
    public void persistirAgenda(Agenda agenda, String nickname) {
        try {
            File directorio = new File(CARPETA);
            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            JAXBContext context = JAXBContext.newInstance(Agenda.class, Contacto.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(agenda, new File(CARPETA + nickname + "_agenda" + Constantes.XML));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void despersistirAgenda(Agenda agenda, String nickname) {
        try {
            JAXBContext context = JAXBContext.newInstance(Agenda.class, Contacto.class);
            Agenda agendaRecuperada = (Agenda) context.createUnmarshaller()
                    .unmarshal(new File(CARPETA + nickname + "_agenda" + Constantes.XML));
            for (Contacto c : agendaRecuperada.getContactos()) {
                agenda.agregarContacto(c.getNicknameReal());
                agenda.actualizarContacto(c.getNicknameReal(), c.getNicknameAgendado());
            }
        } catch (JAXBException ex) {
            Logger.getLogger(XMLPersistenciaAgenda.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

