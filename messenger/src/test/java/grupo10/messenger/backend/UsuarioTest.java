/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package grupo10.messenger.backend;

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
    
    @BeforeAll
    public static void setUpClass() {}
    
    @AfterAll
    public static void tearDownClass() {}
    
    @BeforeEach
    public void setUp() {
        usuario = new Usuario("Juan", "192.168.1.1", 8080);
    }
    
    @AfterEach
    public void tearDown() {}

    /**
     * Test of getAgenda method, of class Usuario.
     */
    @Test
    public void testGetAgenda() {
        assertNotNull(usuario.getAgenda());
        assertEquals(0, usuario.getAgenda().size());
    }

    /**
     * Test of getConversaciones method, of class Usuario.
     */
    @Test
    public void testGetConversaciones() {
        assertNotNull(usuario.getConversaciones());
        assertEquals(0, usuario.getConversaciones().size());
    }

    /**
     * Test of existeContacto method, of class Usuario.
     */
    @Test
    public void testExisteContacto() {
        usuario.agregarContacto("Pedro", "192.168.1.2", 8081);
        assertTrue(usuario.existeContacto("192.168.1.2", 8081));
        assertFalse(usuario.existeContacto("192.168.1.3", 8082));
    }

    /**
     * Test of obtenerContacto method, of class Usuario.
     */
    @Test
    public void testObtenerContacto() {
        usuario.agregarContacto("Maria", "192.168.1.3", 8082);
        Contacto contacto = usuario.obtenerContacto("192.168.1.3", 8082);
        assertNotNull(contacto);
        assertEquals("Maria", contacto.getNickname());
    }

    /**
     * Test of agregarContacto method, of class Usuario.
     */
    @Test
    public void testAgregarContacto() {
        Contacto nuevo = usuario.agregarContacto("Lucas", "192.168.1.4", 8083);
        assertNotNull(nuevo);
        assertEquals(1, usuario.getAgenda().size());
    }

    /**
     * Test of existeConversacion method, of class Usuario.
     */
    @Test
    public void testExisteConversacion() {
        usuario.agregarContacto("Luis", "192.168.1.5", 8084);
        usuario.agregarConversacion("192.168.1.5", 8084);
        assertTrue(usuario.existeConversacion("192.168.1.5", 8084));
        assertFalse(usuario.existeConversacion("192.168.1.6", 8085));
    }

    /**
     * Test of obtenerConversacion method, of class Usuario.
     */
    @Test
    public void testObtenerConversacion() {
        usuario.agregarContacto("Ana", "192.168.1.6", 8086);
        usuario.agregarConversacion("192.168.1.6", 8086);
        Conversacion conversacion = usuario.obtenerConversacion("192.168.1.6", 8086);
        assertNotNull(conversacion);
        assertEquals("Ana", conversacion.getContacto().getNickname());
    }

    /**
     * Test of agregarConversacion method, of class Usuario.
     */
    @Test
    public void testAgregarConversacion() {
        usuario.agregarContacto("Carlos", "192.168.1.7", 8087);
        Conversacion nueva = usuario.agregarConversacion("192.168.1.7", 8087);
        assertNotNull(nueva);
        assertEquals(1, usuario.getConversaciones().size());
    }
    
    @Test
    public void testAgregarMensaje() {
        usuario.agregarContacto("Carlos", "192.168.1.7", 8087);
        usuario.agregarConversacion("192.168.1.7", 8087);
        
        Mensaje msg = new Mensaje("Esto es un test.",true);
                
        boolean agregado = usuario.agregarMensaje("192.168.1.7", 8087, msg);

        assertTrue(agregado, "El mensaje debería agregarse correctamente");

        Conversacion conversacion = usuario.obtenerConversacion("192.168.1.7", 8087);
        List<Mensaje> mensajes = conversacion.getMensajes();

        assertFalse(mensajes.isEmpty(), "La conversación debería tener mensajes");
        assertEquals("Esto es un test.", mensajes.get(0).getContenido(), "El contenido del mensaje no coincide");
    }
}
