/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package grupo10.messenger.backend;

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
public class ControlTest {
    
    private Control control;
        
    public ControlTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        control = Control.getInstance();
        control.registrarUsuario("TestUser", "192.168.1.10", 5000);
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of SingletonInstance, of class Control.
     */
    @Test
    public void testSingletonInstance() {
        Control firstInstance = Control.getInstance();
        Control secondInstance = Control.getInstance();
        assertSame(firstInstance, secondInstance, "Control debería ser un singleton");
    }
    
    /**
     * Test of registrarUsuario method, of class Control.
     */
    @Test
    public void testRegistrarUsuario() {
        Usuario usuario = control.getUsuario();
        assertNotNull(usuario, "El usuario debería estar registrado");
        assertEquals("TestUser", usuario.getNickname(), "El nickname del usuario no coincide");
        assertEquals("192.168.1.10", usuario.getIp(), "La IP del usuario no coincide");
        assertEquals(5000, usuario.getPort(), "El puerto del usuario no coincide");
    }
    
    /**
     * Test of getUsuario method, of class Control.
     */
    @Test
    public void testGetUsuario() {
        assertNotNull(control.getUsuario());
    }

    /**
     * Test of agregarContacto method, of class Control.
     */
    @Test
    public void testAgregarContacto() {
        Contacto contacto = control.agregarContacto("Contacto1", "192.168.1.20", 5001);
        assertNotNull(contacto, "El contacto debería agregarse correctamente.");
        assertTrue(control.getUsuario().existeContacto("192.168.1.20", 5001), "El contacto debería existir en la agenda.");
    }

    /**
     * Test of agregarConversacion method, of class Control.
     */
    @Test
    public void testAgregarConversacion() {
        control.agregarContacto("Contacto1", "192.168.1.20", 5001);
        Conversacion conversacion = control.agregarConversacion("192.168.1.20", 5001);
        assertNotNull(conversacion, "La conversación debería crearse correctamente");
        assertTrue(control.getUsuario().existeConversacion("192.168.1.20", 5001), "La conversación debería existir");
    }

    /**
     * Test of verConversacion method, of class Control.
     */
    @Test
    public void testVerConversacion() {
        control.agregarContacto("Contacto1", "192.168.1.20", 5001);
        control.agregarConversacion("192.168.1.20", 5001);
        Conversacion conversacion = control.verConversacion("192.168.1.20", 5001);
        assertNotNull(conversacion, "La conversación debería existir");
    }
    
}
