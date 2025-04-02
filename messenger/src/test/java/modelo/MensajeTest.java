package modelo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MensajeTest {

    @Test
    public void testConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new Mensaje(null, true),
                     "No debería aceptar un contenido nulo.");
        
        assertThrows(IllegalArgumentException.class, () -> new Mensaje("", true),
                     "No debería aceptar un contenido vacio.");  
    }
    
    @Test
    void testGetters() {
        String contenido = "Hola";
        boolean esMio = true;
        
        Mensaje mensaje = new Mensaje(contenido, esMio);

        assertEquals(contenido, mensaje.getContenido());

        assertTrue(mensaje.esMio());

        String expectedFecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String mensajeFecha = mensaje.getFechaHora();

        assertTrue(mensajeFecha.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }
    
    @Test
    public void testEquals() {
        String contenido = "Hola";
        boolean esMio = true;

        Mensaje mensaje1 = new Mensaje(contenido, esMio);
        Mensaje mensaje2 = new Mensaje(contenido, esMio);
        Mensaje mensaje3 = new Mensaje("Adiós", false);

        // Verificamos que dos mensajes con el mismo contenido y esMio sean iguales
        assertEquals(mensaje1, mensaje2);

        // Verificamos que dos mensajes con diferentes contenidos o diferentes esMio no sean iguales
        assertNotEquals(mensaje1, mensaje3);
    }

    @Test
    public void testToString() {
        String contenido = "Mensaje de prueba";
        boolean esMio = false;

        Mensaje mensaje = new Mensaje(contenido, esMio);

        // Verificamos que el método toString devuelva el formato esperado
        String expected = "[Mensaje: " + contenido + " | Fecha: " + mensaje.getFechaHora() + " | Es mío: " + esMio + "]";
        assertEquals(expected, mensaje.toString());
    }
}
