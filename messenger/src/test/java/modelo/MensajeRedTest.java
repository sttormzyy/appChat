package modelo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import red.MensajeRed;

public class MensajeRedTest {

    @Test
    void testConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new MensajeRed(null, "192.168.1.1", 5000, "192.168.1.2", 5001, "Hola"),
                    "No debería aceptar un nickname nulo.");
        
        assertThrows(IllegalArgumentException.class, () -> new MensajeRed("", "192.168.1.1", 5000, "192.168.1.2", 5001, "Hola"),
                    "No debería aceptar un nickname vacío.");
        
        assertThrows(IllegalArgumentException.class, () -> new MensajeRed("NombreDemasiadoLargoParaValidacion", "192.168.1.1", 5000, "192.168.1.2", 5001, "Hola"),
                    "No debería aceptar un nickname de más de 16 caracteres.");
        
        assertThrows(IllegalArgumentException.class, () -> new MensajeRed("Usuario", null, 5000, "192.168.1.2", 5001, "Hola"),
                     "No debería aceptar una IP nula para el remitente.");
        
        assertThrows(IllegalArgumentException.class, () -> new MensajeRed("Usuario", "192.168.1.1", 5000, null, 5001, "Hola"),
                     "No debería aceptar una IP nula para el destinatario.");
        
        assertThrows(IllegalArgumentException.class, () -> new MensajeRed("Usuario", "999.999.999.999", 5000, "192.168.1.2", 5001, "Hola"),
                     "No debería aceptar una IP fuera de rango para el remitente.");
        
        assertThrows(IllegalArgumentException.class, () -> new MensajeRed("Usuario", "192.168.1.2", 5000, "999.999.999.999", 5001, "Hola"),
                     "No debería aceptar una IP fuera de rango para el destinatario.");
        
        assertThrows(IllegalArgumentException.class, () -> new MensajeRed("Usuario", "abc.def.ghi.jkl", 5000, "192.168.1.1", 5001, "Hola"),
                     "No debería aceptar una IP con letras para el remitente.");
        
        assertThrows(IllegalArgumentException.class, () -> new MensajeRed("Usuario", "192.168.1.1", 5000, "abc.def.ghi.jkl", 5001, "Hola"),
                     "No debería aceptar una IP con letras para el destinatario.");
        
        assertThrows(IllegalArgumentException.class, () -> new MensajeRed("Usuario", "192.168.", 5000, "192.168.1.1", 5001, "Hola"),
                     "No debería aceptar una IP incompleta para el remitente.");
        
        assertThrows(IllegalArgumentException.class, () -> new MensajeRed("Usuario", "192.168.1.1", 5000, "192.168.", 5001, "Hola"),
                     "No debería aceptar una IP incompleta para el destinatario.");
        
        assertThrows(IllegalArgumentException.class, () -> new MensajeRed("Usuario", "192.168.1.1", -1, "192.168.1.2", 5001, "Hola"),
                     "No debería aceptar un puerto negativo para el remitente.");
        
        assertThrows(IllegalArgumentException.class, () -> new MensajeRed("Usuario", "192.168.1.1", 5000, "192.168.1.2", -1, "Hola"),
                     "No debería aceptar un puerto negativo para el destinatario.");
        
        assertThrows(IllegalArgumentException.class, () -> new MensajeRed("Usuario", "192.168.1.1", 70000, "192.168.1.2", 5001, "Hola"),
                     "No debería aceptar un puerto mayor a 65535 para el remitente.");
        
        assertThrows(IllegalArgumentException.class, () -> new MensajeRed("Usuario", "192.168.1.1", 5000, "192.168.1.2", 70000, "Hola"),
                     "No debería aceptar un puerto mayor a 65535 para el destinatario.");
        
        assertThrows(IllegalArgumentException.class, () -> new MensajeRed("Usuario", "192.168.1.1", 5000, "192.168.1.2", 5001, null),
                     "No debería aceptar un contenido de mensaje nulo.");
        
        assertThrows(IllegalArgumentException.class, () -> new MensajeRed("Usuario", "192.168.1.1", 5000, "192.168.1.2", 5001, ""),
                     "No debería aceptar un contenido de mensaje vacío.");
    }
    
    @Test
    void testGetters() {
        String nickname = "Usuario1";
        String myIp = "192.168.1.1";
        int myPort = 5000;
        String destinyIp = "192.168.1.2";
        int destinyPort = 5001;
        String contenido = "Hola!";
        
        MensajeRed mensaje = new MensajeRed(nickname, myIp, myPort, destinyIp, destinyPort, contenido);
        
        assertEquals(nickname, mensaje.getMyNickname());
        assertEquals(myIp, mensaje.getMiIp());
        assertEquals(myPort, mensaje.getMiPuerto());
        assertEquals(destinyIp, mensaje.getDestinyIp());
        assertEquals(destinyPort, mensaje.getDestinyPuerto());
        assertEquals(contenido, mensaje.getContenido());
    }
}
