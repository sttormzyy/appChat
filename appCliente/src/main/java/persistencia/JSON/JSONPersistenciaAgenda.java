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
public class JSONPersistenciaAgenda implements IPersistenciaAgenda {

    ObjectMapper mapper = new ObjectMapper();

    private static final String CARPETA = "Persistencia" + File.separator;

    @Override
    public void persistirAgenda(Agenda agenda, String nickname) {
        try {
            File directorio = new File(CARPETA);
            if (!directorio.exists()) {
                directorio.mkdirs(); // Crear la carpeta si no existe
            }

            mapper.writerWithDefaultPrettyPrinter()
                  .writeValue(new File(CARPETA + nickname + "_agenda" + Constantes.JSON), agenda);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void despersistirAgenda(Agenda agenda, String nickname) {
        try {
            File archivo = new File(CARPETA + nickname + "_agenda" + Constantes.JSON);
            if (!archivo.exists()) {
                System.err.println("Archivo no encontrado: " + archivo.getPath());
                return;
            }

            Agenda agendaRecuperada = mapper.readValue(archivo, Agenda.class);
            for (Contacto c : agendaRecuperada.getContactos()) {
                agenda.agregarContacto(c.getNicknameReal());
                agenda.actualizarContacto(c.getNicknameReal(), c.getNicknameAgendado());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
