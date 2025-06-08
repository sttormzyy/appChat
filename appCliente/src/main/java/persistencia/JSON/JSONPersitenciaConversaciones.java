/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.JSON;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import modelo.Conversacion;
import modelo.Usuario;
import persistencia.IPersistenciaConversaciones;
import resources.Constantes;

/**
 *
 * @author Windows11
 */
public class JSONPersitenciaConversaciones implements IPersistenciaConversaciones {

    ObjectMapper mapper;
    private static final String CARPETA = "Persistencia" + File.separator;

    public JSONPersitenciaConversaciones() {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public void persistirConversaciones(ArrayList<Conversacion> conversaciones, String nickname) {
        try {
            File directorio = new File(CARPETA);
            if (!directorio.exists()) {
                directorio.mkdirs(); // Crear la carpeta si no existe
            }

            File archivo = new File(CARPETA + nickname + "_conversacion" + Constantes.JSON);
            mapper.writerWithDefaultPrettyPrinter().writeValue(archivo, conversaciones);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void despersistirConversaciones(Usuario usuario, String nickname) {
        try {
            File archivo = new File(CARPETA + nickname + "_conversacion" + Constantes.JSON);
            if (!archivo.exists()) {
                System.err.println("Archivo no encontrado: " + archivo.getPath());
                return;
            }

            ArrayList<Conversacion> conversaciones = mapper.readValue(
                archivo, new TypeReference<ArrayList<Conversacion>>() {}
            );

            for (Conversacion c : conversaciones) {
                c.setContacto(usuario.obtenerContactoPorNickname(c.getNicknameReal()));
                usuario.agregarConversacion(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

