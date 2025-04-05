package modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConversacionTest {

    @Test
    void testConstructor() {
        Contacto contacto = new Contacto("User1", "192.168.1.1", 8080);
        Conversacion conversacion = new Conversacion(contacto);
        assertEquals(contacto, conversacion.getContacto(), "El contacto debería ser el mismo.");
        assertTrue(conversacion.getMensajes().isEmpty(), "La lista de mensajes debería estar vacía.");
        
        assertThrows(IllegalArgumentException.class, () -> new Conversacion(null),
                     "No debería aceptar un contacto null.");

    }

    @Test
    void testGetters() {
        Contacto contacto = new Contacto("User1", "192.168.1.1", 8080);
        Conversacion conversacion = new Conversacion(contacto);

        // Verificar los getters
        assertEquals(contacto, conversacion.getContacto(), "El contacto debería ser el mismo.");
        assertEquals("User1", conversacion.getContacto().getNickname(), "El nickname del contacto debería ser 'User1'.");
        assertEquals("192.168.1.1", conversacion.getContacto().getIp(), "La IP del contacto debería ser '192.168.1.1'.");
        assertEquals(8080, conversacion.getContacto().getPuerto(), "El puerto del contacto debería ser 8080.");
    }

    @Test
    void testAgregarMensaje() {
        Contacto contacto = new Contacto("User1", "192.168.1.1", 8080);
        Conversacion conversacion = new Conversacion(contacto);
        Mensaje mensaje = new Mensaje("Hola, ¿cómo estás?", true);

        conversacion.agregarMensaje(mensaje);

        assertFalse(conversacion.getMensajes().isEmpty(), "La lista de mensajes no debería estar vacía.");
        assertEquals(1, conversacion.getMensajes().size(), "Debería haber un mensaje en la conversación.");
        assertEquals(mensaje, conversacion.getMensajes().get(0), "El mensaje agregado debería ser el mismo.");
        
        assertThrows(IllegalArgumentException.class, () -> conversacion.agregarMensaje(null),
                     "No debería aceptar un contacto null.");
    }

    @Test
    void testEquals() {
        Contacto contacto1 = new Contacto("User1", "192.168.1.1", 8080);
        Contacto contacto2 = new Contacto("User1", "192.168.1.1", 8080);
        Conversacion conversacion1 = new Conversacion(contacto1);
        Conversacion conversacion2 = new Conversacion(contacto2);

        Mensaje mensaje1 = new Mensaje("Hola", true);
        Mensaje mensaje2 = new Mensaje("Hola", true);
        
        conversacion1.agregarMensaje(mensaje1);
        conversacion2.agregarMensaje(mensaje2);

        assertThrows(IllegalArgumentException.class, () -> conversacion1.equals(null),
                     "No debería aceptar comparar con un null");
        
        assertTrue(conversacion1.equals(conversacion2), "Las conversaciones deberían ser iguales.");
    }

    @Test
    void testToString() {
        Contacto contacto = new Contacto("User1", "192.168.1.1", 8080);
        Conversacion conversacion = new Conversacion(contacto);

        // Verificar la representación de la conversación
        String expectedString = "Conversación con Persona{nickname='User1', ip='192.168.1.1', port=8080})\nMensajes:\n";
        assertEquals(expectedString, conversacion.toString(), "El método toString debería generar la representación correcta.");
    }
}
