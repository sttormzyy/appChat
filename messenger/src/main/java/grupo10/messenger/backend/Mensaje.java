package grupo10.messenger.backend;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mensaje {
    private static int idCounter = 1;
    private final int id;
    private final String contenido;
    private final String fechaHora;
    private final boolean esMio;
    
    // Constructor
    public Mensaje(String contenido, boolean esMio) {
        this.id = idCounter++;
        this.contenido = contenido;
        this.fechaHora = obtenerFechaHoraActual();
        this.esMio = esMio;
    }
    
    // Getter para obtener el ID
    public int getId() {
        return id;
    }
    
    // Getter para obtener el contenido del mensaje
    public String getContenido() {
        return contenido;
    }

    // Getter para obtener la fecha y hora
    public String getFechaHora() {
        return fechaHora;
    }

    // Getter para obtener si es mio
    public boolean getEsMio() {
        return esMio;
    }
    
    // Método para obtener la fecha y hora actual formateada
    private String obtenerFechaHoraActual() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }

}
