package grupo10.messenger.backend;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mensaje {
    private final String contenido;
    private final String fechaHora;
    private final boolean esMio;
    
    // Constructor
    public Mensaje(String contenido, boolean esMio) {
        this.contenido = contenido;
        this.fechaHora = obtenerFechaHoraActual();
        this.esMio = esMio;
    }
    
    public String getContenido() {
        return contenido;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public boolean getEsMio() {
        return esMio;
    }
    
    // Obtener la fecha y hora actual formateada
    private String obtenerFechaHoraActual() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }

}
