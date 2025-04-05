/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package control;

import controlador.Control;
import modelo.*;
import java.lang.reflect.Constructor;
import java.net.InetAddress;
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
/*
public class ControlTest {
    
    private Control control;
        
    public ControlTest() {
    }
    
    @BeforeEach
    public void setUp() {
        control = Control.getInstance();
        control.registrar("TestUser","localhost", 5000);
    }
    
    @Test
    public void testSingletonInstance() {
        Control firstInstance = Control.getInstance();
        Control secondInstance = Control.getInstance();
        assertSame(firstInstance, secondInstance, "Control debería ser un singleton");
    }
    
    @Test
    public void testRegistrarUsuario() {
        Usuario usuario = control.getUsuario();
        assertNotNull(usuario, "El usuario debería estar registrado");
        assertEquals("TestUser", usuario.getNickname(), "El nickname del usuario no coincide");
        assertEquals("192.168.1.10", usuario.getIp(), "La IP del usuario no coincide");
        assertEquals(5000, usuario.getPort(), "El puerto del usuario no coincide");
    }
    
    @Test
    public void testRegistrarParametrosInvalidos() {
        try {
            Constructor<Control> constructor = Control.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            
            Control instancia = constructor.newInstance();

            // 🔴 Puerto negativo
            boolean resultado1 = instancia.registrar("User1", "localhost", -1);
            assertFalse(resultado1, "No debería permitir un puerto negativo.");

            // 🔴 Puerto fuera del rango
            boolean resultado2 = instancia.registrar("User1", 70000);
            assertFalse(resultado2, "No debería permitir un puerto mayor a 65535.");

            // 🔴 Nickname null
            boolean resultado3 = instancia.registrar(null, 10000);
            assertFalse(resultado3, "No debería permitir un nickname null.");
            
            // 🔴 Nickname vacio
            boolean resultado4 = instancia.registrar("", 10000);
            assertFalse(resultado4, "No debería permitir un nickname vacio.");

            // 🔴 Nickname vacio
            boolean resultado5 = instancia.registrar(new String(), 10000);
            assertFalse(resultado4, "No debería permitir un nickname vacio.");
            
        } catch (Exception e) {
            fail("Excepción en la prueba: " + e.getMessage());
        }
    }
    
    @Test
    public void testNoPermitirMismoPuertoEnMultiplesInstancias() {
        try {
            Constructor<Control> constructor = Control.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            
            int puertoDuplicado = 5002;
            InetAddress localHost = InetAddress.getLocalHost();
            String IP = localHost.getHostAddress();

            // Primera instancia en el puerto 5001
            Control instancia1 = constructor.newInstance();
            boolean registrado1 = instancia1.registrar("User1", puertoDuplicado);
            assertTrue(registrado1, "La primera instancia debería poder registrarse correctamente.");

            // Segunda instancia en el mismo puerto 5001
            Control instancia2 = constructor.newInstance();
            boolean registrado2 = instancia2.registrar("User2", puertoDuplicado);
            assertFalse(registrado2, "No se debería permitir registrar una segunda instancia en el mismo puerto.");

        } catch (Exception e) {
            fail("Excepción en la prueba: " + e.getMessage());
        }
    }

    @Test
    public void testGetUsuario() {
        assertNotNull(control.getUsuario());
    }

    @Test
    public void testAgregarContacto() {
        control.agregarContacto("Contacto1", "192.168.1.20", 5001);
        Contacto contacto = control.getUsuario().buscarContacto("192.168.1.20", 5001);
        assertNotNull(contacto, "El contacto debería agregarse correctamente.");
        assertTrue(control.getUsuario().buscarContacto("192.168.1.20", 5001) !=  null, "El contacto debería existir en la agenda.");
    }

    @Test
    public void testAgregarContactoParametrosInvalidos() {
        // 🔴 Nickname inválido
        assertFalse(control.agregarContacto("nicknamede1234567", "192.168.1.10", 5001), "No debería aceptar nickname mayor a 16 caracteres.");
        assertFalse(control.agregarContacto(null, "192.168.1.10", 5001), "No debería aceptar nickname null.");
        assertFalse(control.agregarContacto("", "192.168.1.10", 5001), "No debería aceptar nickname vacío.");

        // 🔴 IP inválida
        assertFalse(control.agregarContacto("User", null, 5001), "No debería aceptar IP null.");
        assertFalse(control.agregarContacto("User", "", 5001), "No debería aceptar IP vacía.");
        assertFalse(control.agregarContacto("User", "999.999.999.999", 5001), "No debería aceptar IP inválida.");
        assertFalse(control.agregarContacto("User", "abcd", 5001), "No debería aceptar una IP con letras.");
        assertFalse(control.agregarContacto("User", "192.168.1", 5001), "No debería aceptar una IP incompleta.");

        // 🔴 Puerto inválido
        assertFalse(control.agregarContacto("User", "192.168.1.10", -1), "No debería aceptar puerto negativo.");
        assertFalse(control.agregarContacto("User", "192.168.1.10", 70000), "No debería aceptar puerto mayor a 65535.");
    }
    
    @Test
    public void testAgregarConversacion() {
        control.agregarContacto("Contacto1", "192.168.1.20", 5001);
        control.agregarConversacion("192.168.1.20", 5001);
        Conversacion conversacion = control.getUsuario().buscarConversacion("192.168.1.20", 5001);
        assertNotNull(conversacion, "La conversación debería crearse correctamente");
        assertTrue(control.getUsuario().buscarConversacion("192.168.1.20", 5001) != null, "La conversación debería existir");
    }

    @Test
    public void testVerConversacion() {
        control.agregarContacto("Contacto1", "192.168.1.20", 5001);
        control.agregarConversacion("192.168.1.20", 5001);
        Conversacion conversacion = control.verConversacion("192.168.1.20", 5001);
        assertNotNull(conversacion, "La conversación debería existir");
    }
    
    @Test
    public void testVerConversacionParametrosInvalidos() {
        // Agregar una conversación válida para la prueba
        String ipValida = "192.168.1.10";
        int puertoValido = 5002;
        control.agregarConversacion(ipValida, puertoValido);

        // ✅ Caso 1: IP y puerto válidos (Debe devolver la conversación)
        assertNotNull(control.verConversacion(ipValida, puertoValido), 
                      "La conversación debería existir.");

        // ❌ Caso 2: IP inválida
        assertNull(control.verConversacion("999.999.999.999", puertoValido), 
                   "No debería encontrar conversación con una IP inválida.");
        assertNull(control.verConversacion("", puertoValido), 
                   "No debería encontrar conversación con una IP vacia.");
        assertNull(control.verConversacion("asdasdasd", puertoValido), 
                   "No debería encontrar conversación con una IP vacia.");
        assertNull(control.verConversacion(null, puertoValido), 
                   "No debería encontrar conversación con una IP vacia.");

        // ❌ Caso 4: Puerto inválido (<0 o >65535)
        assertNull(control.verConversacion(ipValida, -1), 
                   "No debería encontrar conversación con puerto negativo.");
        assertNull(control.verConversacion(ipValida, 70000), 
                   "No debería encontrar conversación con puerto fuera de rango.");

        // ❌ Caso 5: IP válida pero sin conversación registrada
        assertNull(control.verConversacion("192.168.1.11", puertoValido), 
                   "No debería encontrar conversación si no existe.");

        // ❌ Caso 6: Ambos parámetros inválidos
        assertNull(control.verConversacion("invalid_ip", -500), 
                   "No debería encontrar conversación con parámetros inválidos.");

        // ❌ Caso 7: IP o puerto nulos
        assertThrows(NullPointerException.class, () -> control.verConversacion(null, puertoValido),
                     "Debería lanzar una excepción si la IP es null.");
    }
    
    @Test
    public void testEnviarMensajeParametrosInvalidos() {
        try {
            int puertoValido = 5002;
            InetAddress localHost = InetAddress.getLocalHost();
            String ipValida = localHost.getHostAddress();

            Constructor<Control> constructor = Control.class.getDeclaredConstructor();
            constructor.setAccessible(true);

            Control remitente = constructor.newInstance();
            remitente.registrar("OtroUser", puertoValido);

            control.agregarContacto("OtroUser", ipValida, puertoValido);
            control.agregarConversacion(ipValida, puertoValido);

            // ✅ Caso 1: Contenido vacío o nulo
            assertFalse(control.enviarMensaje("", ipValida, puertoValido), 
                        "No debería enviar un mensaje vacío.");
            assertFalse(control.enviarMensaje(null, ipValida, puertoValido), 
                        "No debería enviar un mensaje nulo.");

            // ✅ Caso 2: IP inválida
            assertFalse(control.enviarMensaje("Hola", "999.999.999.999", puertoValido), 
                        "No debería enviar un mensaje a una IP inválida.");
            assertFalse(control.enviarMensaje("Hola", "invalid_ip", puertoValido), 
                        "No debería enviar un mensaje a un formato IP incorrecto.");
            assertFalse(control.enviarMensaje("Hola", "", puertoValido), 
                        "No debería enviar un mensaje a una ip vacia.");
            assertFalse(control.enviarMensaje("Hola", null, puertoValido), 
                        "No debería enviar un mensaje a un null.");

            // ✅ Caso 3: Puerto inválido
            assertFalse(control.enviarMensaje("Hola", ipValida, -1), 
                        "No debería enviar un mensaje a un puerto negativo.");
            assertFalse(control.enviarMensaje("Hola", ipValida, 70000), 
                        "No debería enviar un mensaje a un puerto fuera de rango.");

            // ✅ Caso 4: Todos los parámetros inválidos
            assertFalse(control.enviarMensaje(null, "invalid_ip", -500), 
                        "No debería enviar un mensaje con parámetros inválidos.");
        } catch (Exception e) {
            fail("Excepción en la prueba: " + e.getMessage());
        }
    }
    
    @Test
    public void testEnviarMensaje_SenderTieneContactoNoConversacionReciverNada() {
        try {
            Constructor<Control> constructor = Control.class.getDeclaredConstructor();
            constructor.setAccessible(true);
        
            InetAddress localHost = InetAddress.getLocalHost();
            String IP = localHost.getHostAddress();

            Control remitente = constructor.newInstance();
            remitente.registrar("UserSender", 5002);
            remitente.agregarContacto("UserReceiver", IP, 5003);

            Control receptor = constructor.newInstance();
            receptor.registrar("UserReceiver", 5003);

            boolean enviado = remitente.enviarMensaje("Hola", IP, 5003);
            assertFalse(enviado, "No se debería poder enviar mensaje sin conversación.");
        } catch (Exception e) {
            fail("Excepción en la prueba: " + e.getMessage());
        }
    }

    @Test
    public void testEnviarMensaje_SenderNoTieneContactoNiConversacionReciverNada() {
        try {
            Constructor<Control> constructor = Control.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            
            InetAddress localHost = InetAddress.getLocalHost();
            String IP = localHost.getHostAddress();

            Control remitente = constructor.newInstance();
            remitente.registrar("UserSender", 5002);

            Control receptor = constructor.newInstance();
            receptor.registrar("UserReceiver", 5003);

            boolean enviado = remitente.enviarMensaje("Hola", IP, 5003);
            assertFalse(enviado, "No se debería poder enviar mensaje sin contacto ni conversación.");
        } catch (Exception e) {
            fail("Excepción en la prueba: " + e.getMessage());
        }
    }

    @Test
    public void testEnviarMensaje_SenderTodoReciverNada() {
        try {
            Constructor<Control> constructor = Control.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            
            InetAddress localHost = InetAddress.getLocalHost();
            String IP = localHost.getHostAddress();

            Control remitente = constructor.newInstance();
            remitente.registrar("UserSender", 5002);
            remitente.agregarContacto("UserReceiver", IP, 5003);
            remitente.agregarConversacion(IP, 5003);

            Control receptor = constructor.newInstance();
            receptor.registrar("UserReceiver", 5003);

            boolean enviado = remitente.enviarMensaje("Hola", IP, 5003);
            assertTrue(enviado, "No deberia afectar lo que tenga el reciver.");
        } catch (Exception e) {
            fail("Excepción en la prueba: " + e.getMessage());
        }
    }

    @Test
    public void testEnviarMensaje_SenderTodoReciverContacto() {
        try {
            Constructor<Control> constructor = Control.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            
            InetAddress localHost = InetAddress.getLocalHost();
            String IP = localHost.getHostAddress();

            Control remitente = constructor.newInstance();
            remitente.registrar("UserSender", 5002);
            remitente.agregarContacto("UserReceiver", IP, 5003);
            remitente.agregarConversacion(IP, 5003);

            Control receptor = constructor.newInstance();
            receptor.registrar("UserReceiver", 5003);
            receptor.agregarContacto("UserSender", IP, 5002);

            boolean enviado = remitente.enviarMensaje("Hola", IP, 5003);
            assertTrue(enviado, "No deberia afectar lo que tenga el reciver.");
        } catch (Exception e) {
            fail("Excepción en la prueba: " + e.getMessage());
        }
    }

    @Test
    public void testEnviarMensaje_SenderTodoReciverTodo() {
        try {
            Constructor<Control> constructor = Control.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            
            InetAddress localHost = InetAddress.getLocalHost();
            String IP = localHost.getHostAddress();

            Control remitente = constructor.newInstance();
            remitente.registrar("UserSender", 5002);
            remitente.agregarContacto("UserReceiver", IP, 5003);
            remitente.agregarConversacion(IP, 5003);

            Control receptor = constructor.newInstance();
            receptor.registrar("UserReceiver", 5002);
            receptor.agregarContacto("UserSender", IP, 5003);
            receptor.agregarConversacion(IP, 5003);

            boolean enviado = remitente.enviarMensaje("Hola", IP, 5003);
            assertTrue(enviado, "El mensaje debería enviarse correctamente.");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            Conversacion conversacionReceptor = receptor.verConversacion(IP, 5001);
            assertNotNull(conversacionReceptor, "La conversación debería existir en el receptor.");
            assertFalse(conversacionReceptor.getMensajes().isEmpty(), "La conversación debería tener mensajes.");
            Mensaje mensajeRecibido = conversacionReceptor.getMensajes().get(0);
            assertEquals("Hola", mensajeRecibido.getContenido(), "El contenido del mensaje debería coincidir.");
            assertFalse(mensajeRecibido.getEsMio(), "El mensaje recibido no debería ser del receptor.");
        } catch (Exception e) {
            fail("Excepción en la prueba: " + e.getMessage());
        }
    }
    
    @Test
    public void testRecibirMensaje_NullMensajeRed() throws Exception {
        Constructor<Control> constructor = Control.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        
        
        Control control = constructor.newInstance();
        MensajeRed mensajeRedNull = null;

        control.recibirMensaje(mensajeRedNull);
    }

    @Test
    public void testRecibirMensaje_MensajeRedEnOrden() throws Exception {
        Constructor<Control> constructor = Control.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        
        
        Control control = constructor.newInstance();
        String contenido = "Mensaje de prueba";
        MensajeRed mensajeRedValido = new MensajeRed("TestUser", "127.0.0.1", 8080, "127.0.0.2", 8081, contenido);

        control.agregarContacto("TestUser", "127.0.0.1", 8080);
        control.agregarConversacion("127.0.0.1", 8080);

        control.recibirMensaje(mensajeRedValido);

        // Verificar que el mensaje ha sido agregado correctamente
        Conversacion conversacion = control.verConversacion("127.0.0.1", 8080);
        assertNotNull(conversacion, "La conversación debe existir.");
        assertFalse(conversacion.getMensajes().isEmpty(), "La conversación debería tener mensajes.");
        assertEquals(contenido, conversacion.getMensajes().get(0).getContenido(), "El contenido del mensaje no coincide.");
    }

    @Test
    public void testRecibirMensaje_ContactoExistenteSinConversacion() throws Exception {
        Constructor<Control> constructor = Control.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        
        
        Control control = constructor.newInstance();
        String contenido = "Mensaje de prueba";
        MensajeRed mensajeRedValido = new MensajeRed("TestUser", "127.0.0.1", 8080, "127.0.0.2", 8081, contenido);

        // Agregamos el contacto pero no la conversación
        control.agregarContacto("TestUser", "127.0.0.1", 8080);

        control.recibirMensaje(mensajeRedValido);

        // Verificar que la conversación fue creada y el mensaje agregado
        Conversacion conversacion = control.verConversacion("127.0.0.1", 8080);
        assertNotNull(conversacion, "La conversación debe ser creada.");
        assertFalse(conversacion.getMensajes().isEmpty(), "La conversación debería tener mensajes.");
        assertEquals(contenido, conversacion.getMensajes().get(0).getContenido(), "El contenido del mensaje no coincide.");
    }

    @Test
    public void testRecibirMensaje_SinContactoNiConversacion() throws Exception {
        Constructor<Control> constructor = Control.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        
        
        Control control = constructor.newInstance();
        String contenido = "Mensaje de prueba";
        MensajeRed mensajeRedValido = new MensajeRed("TestUser", "127.0.0.1", 8080, "127.0.0.2", 8081, contenido);

        // En este caso, ni el contacto ni la conversación existen
        control.recibirMensaje(mensajeRedValido);

        // Verificar que la conversación fue creada y el mensaje agregado
        Conversacion conversacion = control.verConversacion("127.0.0.1", 8080);
        assertNotNull(conversacion, "La conversación debe ser creada.");
        assertFalse(conversacion.getMensajes().isEmpty(), "La conversación debería tener mensajes.");
        assertEquals(contenido, conversacion.getMensajes().get(0).getContenido(), "El contenido del mensaje no coincide.");
    }
    
    @Test
    public void testComunicacionConcurrente() {
        try {
            Constructor<Control> constructor = Control.class.getDeclaredConstructor();
            constructor.setAccessible(true);

            InetAddress localHost = InetAddress.getLocalHost();
            String IP = localHost.getHostAddress();

            // Crear usuarios y puertos
            String[] nombres = {"User 1", "User 2", "User 3", "User 4", "User 5"};
            int[] puertos = {5001, 5002, 5003, 5004, 5005};

            // Crear instancias de Control
            Control[] instancias = new Control[5];
            for (int i = 0; i < 5; i++) {
                instancias[i] = constructor.newInstance();
                instancias[i].registrar(nombres[i], puertos[i]);
            }

            // Agregar contactos y conversaciones en cada instancia
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (i != j) {
                        instancias[i].agregarContacto(nombres[j], IP, puertos[j]);
                        instancias[i].agregarConversacion(IP, puertos[j]);
                    }
                }
            }

            // Crear 5 hilos, cada usuario envía mensajes a los otros 4
            Thread[] hilos = new Thread[5];
            for (int i = 0; i < 5; i++) {
                int userIndex = i;
                hilos[i] = new Thread(() -> {
                    for (int j = 0; j < 5; j++) {
                        if (userIndex != j) {
                            String mensaje = "Hola de " + nombres[userIndex] + " a " + nombres[j];
                            instancias[userIndex].enviarMensaje(mensaje, IP, puertos[j]);
                        }
                    }
                });
                hilos[i].start();
            }

            // Esperar a que todos los hilos terminen
            for (Thread hilo : hilos) {
                hilo.join();
            }

            // Comparar las conversaciones en todas las instancias
            Conversacion referencia = instancias[0].verConversacion(IP, puertos[1]);

            for (int i = 1; i < 5; i++) {
                Conversacion comparada = instancias[i].verConversacion(IP, puertos[0]);
                assertNotNull(comparada, "Conversación no encontrada en instancia " + (i + 1));
                assertTrue(sonConversacionesIguales(referencia, comparada),
                        "Conversación en instancia " + (i + 1) + " es diferente.");
            }

        } catch (Exception e) {
            fail("Excepción en la prueba: " + e.getMessage());
        }
    }

    
    private boolean sonConversacionesIguales(Conversacion c1, Conversacion c2) {
        if (c1 == null || c2 == null) return false;
        return c1.equals(c2);
    }
}
*/