/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@XmlRootElement(name = "mensaje")
public class Mensaje {
    private String contenido = null;
    private String fechaHora = null;
    private boolean mine = true;

    public Mensaje() {
    }
    
    public Mensaje(String contenido, boolean esMio) {
        this.contenido = contenido;
        this.fechaHora = obtenerFechaHoraActual();
        this.mine = esMio;
    }

    public Mensaje(String contenido, String fechaHora, boolean esMio) {
        this.contenido = contenido;
        this.fechaHora = fechaHora;
        this.mine = esMio;
    }
    
    
    
    public Mensaje(String contenido, boolean esMio, String fecha) {
        this.contenido = contenido;
        this.fechaHora = fecha;
        this.mine = esMio;
    }
    
    @XmlElement(name = "contenido")
    public String getContenido() {
        return contenido;
    }

    @XmlElement(name = "fechaHora")
    public String getFechaHora() {
        return fechaHora;
    }

    @XmlElement(name = "esMio")
    public boolean isMine() {
        return mine;
    }
    
    private String obtenerFechaHoraActual() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    // Metodos persistencia
    
    public static String toTextoPlano(Mensaje m) {
        try {
            String contenido64 = Base64.getEncoder().encodeToString(m.contenido.getBytes("UTF-8"));
            return contenido64 + "|" + m.fechaHora + "|" + m.mine;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error al codificar contenido en Base64", e);
        }
    }

    
    public static Mensaje fromTextoPlano(String s, String buffer) {
        try {
            String[] partes = s.split("\\|");

            if (partes.length != 3) {
                throw new IllegalArgumentException("Formato inválido para mensaje: " + s);
            }

            String contenidoDecodificado = new String(Base64.getDecoder().decode(partes[0]), "UTF-8");
            String contenido = buffer + contenidoDecodificado;
            String fechaHora = partes[1];
            boolean esMio = Boolean.parseBoolean(partes[2]);

            return new Mensaje(contenido, fechaHora, esMio);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error al decodificar contenido en Base64", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Error al parsear mensaje desde texto plano: " + s, e);
        }
    }

}
