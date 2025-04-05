/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import red.Servidor;

/**
 *
 * @author user
 */
public class ServerTest {
    
    public ServerTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of isConectado method, of class Servidor.
     */
    @Test
    public void testIsConectado() {
        System.out.println("isConectado");
        Servidor instance = null;
        boolean expResult = false;
        boolean result = instance.isConectado();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of iniciarServidor method, of class Servidor.
     */
    @Test
    public void testIniciarServidor() throws Exception {
        System.out.println("iniciarServidor");
        Servidor instance = null;
        instance.iniciarServidor();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of run method, of class Servidor.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        Servidor instance = null;
        instance.run();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
