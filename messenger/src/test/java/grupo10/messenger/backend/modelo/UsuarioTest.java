/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package grupo10.messenger.backend.modelo;

import grupo10.messenger.backend.modelo.Conversacion;
import grupo10.messenger.backend.modelo.Usuario;
import grupo10.messenger.backend.modelo.Mensaje;
import grupo10.messenger.backend.modelo.Contacto;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author user
 */
public class UsuarioTest {
    
    private static Usuario usuario;

    public UsuarioTest() {}
    
    @BeforeEach
    public void setUp() {
        usuario = new Usuario("Juan", "192.168.1.1", 8080);
    }

    @Test
    public void testGetAgenda() {
        assertNotNull(usuario.getAgenda());
        assertEquals(0, usuario.getAgenda().size(), "La agenda debería estar vacía inicialmente.");
    }

    @Test
    public void testGetConversaciones() {
        assertNotNull(usuario.getConversaciones());
        assertEquals(0, usuario.getConversaciones().size(), "Las conversaciones deberían estar vacías inicialmente.");
    }

    @Test
    public void testBuscarContacto() {
        // Caso 1: El contacto existe
        usuario.agregarContacto("Pedro", "192.168.1.2", 8081);
        Contacto contactoExistente = usuario.buscarContacto("192.168.1.2", 8081);
        assertNotNull(contactoExistente, "El contacto debería existir en la agenda.");
        assertEquals("Pedro", contactoExistente.getNickname(), "El nickname del contacto debería ser 'Pedro'.");

        // Caso 2: El contacto no existe
        Contacto contactoInexistente = usuario.buscarContacto("192.168.1.3", 8082);
        assertNull(contactoInexistente, "El contacto no debería existir en la agenda.");

        // Caso 3: IP inválida (parámetro no válido)
        assertNull(usuario.buscarContacto(null, 8081), "No debería encontrar contacto con una IP null.");
        assertNull(usuario.buscarContacto("999.999.999.999", 8081), "No debería encontrar contacto con una IP con formato inválido.");
        assertNull(usuario.buscarContacto("dasdasd", 8081), "No debería encontrar contacto con una IP con letras.");
        assertNull(usuario.buscarContacto("", 8081), "No debería encontrar contacto con una IP vacía.");
        assertNull(usuario.buscarContacto("192.168.0", 8081), "No debería encontrar contacto con una IP incompleta.");

        // Caso 4: Puerto inválido (parámetro no válido)
        assertNull(usuario.buscarContacto("192.168.1.2", 70000), "No debería encontrar contacto con un puerto inválido.");
        assertNull(usuario.buscarContacto("192.168.1.2", -1), "No debería encontrar contacto con un puerto inválido.");
    }

    @Test
    public void testAgregarContacto() {
        // Caso 1: Agregar un contacto válido
        Contacto nuevo = usuario.agregarContacto("Lucas", "192.168.1.4", 8083);
        assertNotNull(nuevo, "El contacto debería agregarse correctamente.");
        assertEquals(1, usuario.getAgenda().size(), "Debería haber 1 contacto en la agenda.");
        assertEquals("Lucas", nuevo.getNickname(), "El nickname del contacto agregado debería ser 'Lucas'.");
        
        // Caso 2: Agregar un contacto con la misma IP pero diferente puerto
        Contacto contactoDiferentePuerto = usuario.agregarContacto("Juan", "192.168.1.4", 8084);
        assertNotNull(contactoDiferentePuerto, "Debería permitirse agregar un contacto con la misma IP pero con un puerto diferente.");
        assertEquals(2, usuario.getAgenda().size(), "Debería haber 2 contactos en la agenda.");

        // Caso 3: Agregar un contacto con la misma IP y mismo puerto (debería fallar)
        Contacto contactoDuplicado = usuario.agregarContacto("Carlos", "192.168.1.4", 8083);
        assertNull(contactoDuplicado, "No debería permitir agregar un contacto con la misma IP y puerto ya existentes.");
        assertEquals(2, usuario.getAgenda().size(), "Debería haber 2 contactos en la agenda.");

        // Caso 4: Agregar un contacto con nickname invalido (parámetro no válido)
        assertNull(usuario.agregarContacto(null, "192.168.1.5", 8085), "No debería permitir agregar un contacto con una IP null.");
        assertNull(usuario.agregarContacto("", "192.168.1.5", 8085), "No debería permitir agregar un contacto con una IP con formato inválido.");
        assertNull(usuario.agregarContacto("NombreDemasiadoLargoParaValidacion", "192.168.1.5", 8085), "No debería permitir agregar un contacto con una IP con letras.");
        
        // Caso 5: Agregar un contacto con IP inválida (parámetro no válido)
        assertNull(usuario.agregarContacto("Carlos", null, 8084), "No debería permitir agregar un contacto con una IP null.");
        assertNull(usuario.agregarContacto("Carlos", "999.999.999.999", 8084), "No debería permitir agregar un contacto con una IP con formato inválido.");
        assertNull(usuario.agregarContacto("Carlos", "dasdasd", 8084), "No debería permitir agregar un contacto con una IP con letras.");
        assertNull(usuario.agregarContacto("Carlos", "", 8084), "No debería permitir agregar un contacto con una IP vacía.");
        assertNull(usuario.agregarContacto("Carlos", "192.168.0", 8084), "No debería permitir agregar un contacto con una IP incompleta.");

        // Caso 6: Agregar un contacto con puerto inválido (parámetro no válido)
        assertNull(usuario.agregarContacto("Carlos", "192.168.1.5", 70000), "No debería permitir agregar un contacto con un puerto inválido.");
        assertNull(usuario.agregarContacto("Carlos", "192.168.1.5", -1), "No debería permitir agregar un contacto con un puerto inválido.");
    }

    @Test
    public void testBuscarConversacion() {
        // Caso 1: La conversación existe
        usuario.agregarContacto("Ana", "192.168.1.6", 8086);
        usuario.agregarConversacion("192.168.1.6", 8086);
        Conversacion conversacionExistente = usuario.buscarConversacion("192.168.1.6", 8086);
        assertNotNull(conversacionExistente, "La conversación debería existir.");
        assertEquals("Ana", conversacionExistente.getContacto().getNickname(), "El nickname del contacto en la conversación debería ser 'Ana'.");

        // Caso 2: La conversación no existe
        Conversacion conversacionInexistente = usuario.buscarConversacion("192.168.1.7", 8087);
        assertNull(conversacionInexistente, "La conversación no debería existir.");

        // Caso 3: IP inválida (parámetro no válido)
        assertNull(usuario.buscarConversacion(null, 8086), "No debería encontrar conversación con una IP null.");
        assertNull(usuario.buscarConversacion("999.999.999.999", 8086), "No debería encontrar conversación con una IP con formato inválido.");
        assertNull(usuario.buscarConversacion("dasdasd", 8086), "No debería encontrar conversación con una IP con letras.");
        assertNull(usuario.buscarConversacion("", 8086), "No debería encontrar conversación con una IP vacía.");
        assertNull(usuario.buscarConversacion("192.168.0", 8086), "No debería encontrar conversación con una IP incompleta.");

        // Caso 4: Puerto inválido (parámetro no válido)
        assertNull(usuario.buscarConversacion("192.168.1.6", 70000), "No debería encontrar conversación con un puerto inválido.");
        assertNull(usuario.buscarConversacion("192.168.1.6", -1), "No debería encontrar conversación con un puerto inválido.");
    }

    @Test
    public void testAgregarConversacion() {
        // Caso 1: Agregar una conversación válida
        usuario.agregarContacto("Carlos", "192.168.1.7", 8087);
        Conversacion nueva = usuario.agregarConversacion("192.168.1.7", 8087);
        assertNotNull(nueva, "La conversación debería agregarse correctamente.");
        assertEquals(1, usuario.getConversaciones().size(), "Debería haber 1 conversación.");
        assertEquals("Carlos", nueva.getContacto().getNickname(), "El nickname del contacto en la conversación debería ser 'Carlos'.");

        // Caso 2: Agregar una conversación con la misma IP pero diferente puerto
        usuario.agregarContacto("Juan", "192.168.1.7", 8088);
        Conversacion conversacionDiferentePuerto = usuario.agregarConversacion("192.168.1.7", 8088);
        assertNotNull(conversacionDiferentePuerto, "Debería permitirse agregar una conversación con la misma IP pero con un puerto diferente.");
        assertEquals(2, usuario.getConversaciones().size(), "Debería haber 2 conversaciones.");

        // Caso 3: Agregar una conversación con la misma IP y el mismo puerto (debería fallar)
        Conversacion conversacionDuplicada = usuario.agregarConversacion("192.168.1.7", 8087);
        assertNull(conversacionDuplicada, "No debería permitir agregar una conversación con la misma IP y puerto ya existentes.");
        assertEquals(2, usuario.getConversaciones().size(), "Debería haber 2 conversaciones.");

        // Caso 4: Agregar una conversación con IP inválida (parámetro no válido)
        assertNull(usuario.agregarConversacion(null, 8087), "No debería permitir agregar una conversación con una IP null.");
        assertNull(usuario.agregarConversacion("999.999.999.999", 8087), "No debería permitir agregar una conversación con una IP con formato inválido.");
        assertNull(usuario.agregarConversacion("dasdasd", 8087), "No debería permitir agregar una conversación con una IP con letras.");
        assertNull(usuario.agregarConversacion("", 8087), "No debería permitir agregar una conversación con una IP vacía.");
        assertNull(usuario.agregarConversacion("192.168.0", 8087), "No debería permitir agregar una conversación con una IP incompleta.");

        // Caso 5: Agregar una conversación con puerto inválido (parámetro no válido)
        assertNull(usuario.agregarConversacion("192.168.1.7", 70000), "No debería permitir agregar una conversación con un puerto inválido.");
        assertNull(usuario.agregarConversacion("192.168.1.7", -1), "No debería permitir agregar una conversación con un puerto inválido.");
    }

    @Test
    public void testAgregarMensaje() {
        // Caso 1: Agregar un mensaje válido
        usuario.agregarContacto("Carlos", "192.168.1.7", 8087);
        usuario.agregarConversacion("192.168.1.7", 8087);
        Mensaje msg = new Mensaje("Esto es un test.", true);

        boolean agregado = usuario.agregarMensaje("192.168.1.7", 8087, msg);
        assertTrue(agregado, "El mensaje debería agregarse correctamente.");
        Conversacion conversacion = usuario.buscarConversacion("192.168.1.7", 8087);
        List<Mensaje> mensajes = conversacion.getMensajes();
        assertFalse(mensajes.isEmpty(), "La conversación debería tener mensajes.");
        assertEquals("Esto es un test.", mensajes.get(0).getContenido(), "El contenido del mensaje no coincide.");

        // Caso 2: El objeto mensaje es null
        boolean agregadoObjetoNull = usuario.agregarMensaje("192.168.1.7", 8087, null);
        assertFalse(agregadoObjetoNull, "No debería permitir agregar un mensaje nulo.");
        Conversacion conversacionObjetoNull = usuario.buscarConversacion("192.168.1.7", 8087);
        List<Mensaje> mensajesObjetoNull = conversacionObjetoNull.getMensajes();
        assertTrue(mensajesObjetoNull.isEmpty(), "La conversación no debería tener mensajes cuando el objeto mensaje es null.");

        // Caso 3: IP inválida (parámetro no válido)
        boolean agregadoIpNull = usuario.agregarMensaje(null, 8087, msg);
        assertFalse(agregadoIpNull, "No debería permitir agregar un mensaje con una IP null.");
        Conversacion conversacionIpNull = usuario.buscarConversacion(null, 8087);
        List<Mensaje> mensajesIpNull = conversacionIpNull.getMensajes();
        assertTrue(mensajesIpNull.isEmpty(), "La conversación con IP null no debería tener mensajes.");

        boolean agregadoIpVacia = usuario.agregarMensaje("", 8087, msg);
        assertFalse(agregadoIpVacia, "No debería permitir agregar un mensaje con una IP vacía.");
        Conversacion conversacionIpVacia = usuario.buscarConversacion("", 8087);
        List<Mensaje> mensajesIpVacia = conversacionIpVacia.getMensajes();
        assertTrue(mensajesIpVacia.isEmpty(), "La conversación con IP vacía no debería tener mensajes.");

        boolean agregadoIpLetras = usuario.agregarMensaje("test", 8087, msg);
        assertFalse(agregadoIpLetras, "No debería permitir agregar un mensaje con una IP con letras.");
        Conversacion conversacionIpLetras = usuario.buscarConversacion("test", 8087);
        List<Mensaje> mensajesIpLetras = conversacionIpLetras.getMensajes();
        assertTrue(mensajesIpLetras.isEmpty(), "La conversación con IP con letras no debería tener mensajes.");

        boolean agregadoIpFormatoIncorrecto = usuario.agregarMensaje("999.999.999.999", 8087, msg);
        assertFalse(agregadoIpFormatoIncorrecto, "No debería permitir agregar un mensaje con una IP con formato incorrecto.");
        Conversacion conversacionIpFormatoIncorrecto = usuario.buscarConversacion("999.999.999.999", 8087);
        List<Mensaje> mensajesIpFormatoIncorrecto = conversacionIpFormatoIncorrecto.getMensajes();
        assertTrue(mensajesIpFormatoIncorrecto.isEmpty(), "La conversación con IP con formato incorrecto no debería tener mensajes.");

        boolean agregadoIpIncompleta = usuario.agregarMensaje("192.168.0", 8087, msg);
        assertFalse(agregadoIpIncompleta, "No debería permitir agregar un mensaje con una IP sin terminar.");
        Conversacion conversacionIpIncompleta = usuario.buscarConversacion("192.168.0", 8087);
        List<Mensaje> mensajesIpIncompleta = conversacionIpIncompleta.getMensajes();
        assertTrue(mensajesIpIncompleta.isEmpty(), "La conversación con IP incompleta no debería tener mensajes.");

        // Caso 4: Puerto inválido (parámetro no válido)
        boolean agregadoPuertoNegativo = usuario.agregarMensaje("192.168.1.7", -1, msg);
        assertFalse(agregadoPuertoNegativo, "No debería permitir agregar un mensaje con un puerto -1.");
        Conversacion conversacionPuertoNegativo = usuario.buscarConversacion("192.168.1.7", -1);
        List<Mensaje> mensajesPuertoNegativo = conversacionPuertoNegativo.getMensajes();
        assertTrue(mensajesPuertoNegativo.isEmpty(), "La conversación con puerto -1 no debería tener mensajes.");

        boolean agregadoPuertoGrande = usuario.agregarMensaje("192.168.1.7", 70000, msg);
        assertFalse(agregadoPuertoGrande, "No debería permitir agregar un mensaje con un puerto 70000.");
        Conversacion conversacionPuertoGrande = usuario.buscarConversacion("192.168.1.7", 70000);
        List<Mensaje> mensajesPuertoGrande = conversacionPuertoGrande.getMensajes();
        assertTrue(mensajesPuertoGrande.isEmpty(), "La conversación con puerto 70000 no debería tener mensajes.");

        // Caso 5: Agregar un mensaje a una conversación inexistente pero con contacto existente
        boolean agregadoMensajeConConversacionInexistente = usuario.agregarMensaje("192.168.1.7", 8087, msg);
        assertFalse(agregadoMensajeConConversacionInexistente, "No debería permitir agregar un mensaje a una conversación inexistente.");
        Conversacion conversacionInexistente = usuario.buscarConversacion("192.168.1.7", 8087);
        assertNull(conversacionInexistente, "No debería existir una conversación para un contacto sin conversación.");

        // Caso 6: Agregar un mensaje a una conversación inexistente y contacto inexistente
        boolean agregadoMensajeConContactoYConversacionInexistentes = usuario.agregarMensaje("192.168.1.10", 8087, msg);
        assertFalse(agregadoMensajeConContactoYConversacionInexistentes, "No debería permitir agregar un mensaje cuando ni el contacto ni la conversación existen.");
        Conversacion conversacionInexistenteContactoInexistente = usuario.buscarConversacion("192.168.1.10", 8087);
        assertNull(conversacionInexistenteContactoInexistente, "No debería existir una conversación para un contacto inexistente.");
    }

}
