/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.JSON;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import modelo.Agenda;
import modelo.Contacto;
import persistencia.IPersistenciaAgenda;
import resources.Constantes;

/**
 *
 * @author Windows11
 */
public class JSONPersistenciaAgenda implements IPersistenciaAgenda{
    
    ObjectMapper mapper = new ObjectMapper();

    @Override
    public void persistirAgenda(Agenda agenda, String nickname) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(nickname + "_agenda" + Constantes.JSON), agenda);
        } catch (IOException e) {
            e.printStackTrace();
        }    
    }

    @Override
    public void despersistirAgenda(Agenda agenda, String nickname) {
        try {
            Agenda agendaRecuperada = mapper.readValue(new File(nickname+"_agenda.json"), Agenda.class);
            for (Contacto c : agendaRecuperada.getContactos()) {
                agenda.agregarContacto(c.getNicknameReal());
                agenda.actualizarContacto(c.getNicknameReal(), c.getNicknameAgendado());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
