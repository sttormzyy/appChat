/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.TextoPlano;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import modelo.Agenda;
import modelo.Contacto;
import persistencia.IPersistenciaAgenda;
import resources.Constantes;

/**
 *
 * @author Windows11
 */
public class TextoPlanoPersistenciaAgenda implements IPersistenciaAgenda {

    private static final String CARPETA = "Persistencia" + File.separator;

    @Override
    public void persistirAgenda(Agenda agenda, String nickname) {
        try {
            File directorio = new File(CARPETA);
            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            try (BufferedWriter writer = new BufferedWriter(
                    new FileWriter(CARPETA + nickname + "_agenda" + Constantes.TEXTO_PLANO))) {

                for (Contacto contacto : agenda.getContactos()) {
                    String nicknameRealBase64 = Base64.getEncoder().encodeToString(contacto.getNicknameReal().getBytes("UTF-8"));
                    String nicknameAgendadoBase64 = Base64.getEncoder().encodeToString(contacto.getNicknameAgendado().getBytes("UTF-8"));
                    writer.append(nicknameAgendadoBase64 + "|" + nicknameRealBase64 + "\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void despersistirAgenda(Agenda agenda, String nickname) {
        try (BufferedReader reader = new BufferedReader(
                new FileReader(CARPETA + nickname + "_agenda" + Constantes.TEXTO_PLANO))) {

            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    Contacto contacto = Contacto.fromTextoPlano(linea.trim());
                    agenda.agregarContacto(contacto.getNicknameReal());
                    agenda.actualizarContacto(contacto.getNicknameReal(), contacto.getNicknameAgendado());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

