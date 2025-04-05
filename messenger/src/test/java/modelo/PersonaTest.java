package modelo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PersonaTest {

    @Test
    void testConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new Persona(null, "192.168.1.1", 5000),
                     "No debería aceptar un nickname nulo.");
        
        assertThrows(IllegalArgumentException.class, () -> new Persona("", "192.168.1.1", 5000),
                     "No debería aceptar un nickname vacío.");
        
        assertThrows(IllegalArgumentException.class, () -> new Persona("NombreDemasiadoLargoParaValidacion", "192.168.1.1", 5000),
                     "No debería aceptar un nickname de más de 16 caracteres.");
        
        assertThrows(IllegalArgumentException.class, () -> new Persona("Usuario", null, 5000),
                     "No debería aceptar una IP nula.");
        
        assertThrows(IllegalArgumentException.class, () -> new Persona("Usuario", "", 5000),
                     "No debería aceptar una IP vacía.");
        
        assertThrows(IllegalArgumentException.class, () -> new Persona("Usuario", "999.999.999.999", 5000),
                     "No debería aceptar una IP fuera de rango.");
        
        assertThrows(IllegalArgumentException.class, () -> new Persona("Usuario", "abc.def.ghi.jkl", 5000),
                     "No debería aceptar una IP con letras.");
        
        assertThrows(IllegalArgumentException.class, () -> new Persona("Usuario", "192.168.0.", 5000),
                     "No debería aceptar una IP incompleta.");
        
        assertThrows(IllegalArgumentException.class, () -> new Persona("Usuario", "192.168.1.1", -1),
                    "No debería aceptar un puerto negativo.");
        
        assertThrows(IllegalArgumentException.class, () -> new Persona("Usuario", "192.168.1.1", 70000),
                    "No debería aceptar un puerto mayor a 65535.");
    }
    
    @Test
    void testGetters() {
        String nickname = "UsuarioValido";
        String ip = "192.168.1.1";
        int port = 5000;
        
        Persona persona = new Persona(nickname, ip, port);

        assertEquals(nickname, persona.getNickname());
        assertEquals(ip,persona.getIp());
        assertEquals(port,persona.getPuerto());
    }
}
