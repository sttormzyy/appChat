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
import java.util.ArrayList;
import java.util.Base64;
import modelo.Agenda;
import modelo.Contacto;
import modelo.Conversacion;
import modelo.Mensaje;
import modelo.Usuario;
import persistencia.IPersistenciaConversaciones;

/**
 *
 * @author Windows11
 */
public class TextoPlanoPersitenciaConversaciones implements IPersistenciaConversaciones {

    private static final String CARPETA = "Persistencia" + File.separator;

    @Override
    public void persistirConversaciones(ArrayList<Conversacion> conversaciones, String nickname) {
        try {
            File directorio = new File(CARPETA);
            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            try (BufferedWriter writer = new BufferedWriter(
                    new FileWriter(CARPETA + nickname + "_conversaciones.txt"))) {

                for (Conversacion c : conversaciones) {
                    writer.append("--- Conversacion ---\n");
                    writer.append("Contacto:" + Base64.getEncoder().encodeToString(c.getContacto().getNicknameReal().getBytes("UTF-8")) + "\n");
                    writer.append("Notificada:" + c.getNotificacion() + "\n");
                    for (Mensaje m : c.getMensajes()) {
                        writer.append(Mensaje.toTextoPlano(m)).append("\n");
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void despersistirConversaciones(Usuario usuario, String nickname) {
        try (BufferedReader reader = new BufferedReader(
                new FileReader(CARPETA + nickname + "_conversaciones.txt"))) {

            String linea;
            boolean notificada;
            Agenda agenda = usuario.getAgenda();

            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                while (linea != null && !linea.isEmpty() && linea.equals("--- Conversacion ---")) {
                    if ((linea = reader.readLine()) != null && !linea.isEmpty()) {
                        String nicknameDecodificado = new String(Base64.getDecoder().decode(
                                linea.substring("Contacto:".length()).trim()), "UTF-8");

                        Contacto contacto = agenda.obtenerContactoPorNickname(nicknameDecodificado);
                        Conversacion conversacion = new Conversacion(contacto);
                        usuario.agregarConversacion(conversacion);

                        if ((linea = reader.readLine()) != null && !linea.isEmpty()) {
                            notificada = Boolean.parseBoolean(linea.substring("Notificada:".length()).trim());
                            String buffer = "";

                            while ((linea = reader.readLine()) != null &&
                                   !linea.isEmpty() &&
                                   !linea.equals("--- Conversacion ---")) {

                                Mensaje m;
                                if (linea.contains("|")) {
                                    m = Mensaje.fromTextoPlano(linea, buffer);
                                    conversacion.agregarMensaje(m.getContenido(), m.isMine(), m.getFechaHora());
                                    buffer = "";
                                } else {
                                    buffer += linea;
                                }
                            }
                            conversacion.setNotificacion(notificada);
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
