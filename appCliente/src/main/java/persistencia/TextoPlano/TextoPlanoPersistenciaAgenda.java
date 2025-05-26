/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.TextoPlano;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import modelo.Agenda;
import modelo.Contacto;
import persistencia.IPersistenciaAgenda;
import resources.Constantes;

/**
 *
 * @author Windows11
 */
public class TextoPlanoPersistenciaAgenda implements IPersistenciaAgenda {

    @Override
    public void persistirAgenda(Agenda agenda, String nickname) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nickname + "_agenda"+Constantes.TEXTO_PLANO))) {
            for (Contacto contacto : agenda.getContactos()) {
                writer.append(Contacto.toTextoPlano(contacto) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void despersistirAgenda(Agenda agenda, String nickname) {
        try (BufferedReader reader = new BufferedReader(new FileReader(nickname + "_agenda"+Constantes.TEXTO_PLANO))) {
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
