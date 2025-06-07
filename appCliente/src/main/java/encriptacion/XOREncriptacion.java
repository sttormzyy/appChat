/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package encriptacion;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 *
 * @author Usuario
 */
public class XOREncriptacion implements IEncriptacion{
    private final String claveSecreta;

    public XOREncriptacion(String clave){
        this.claveSecreta = clave;
    }
    
    private byte[] xor(String input) {
            byte[] texto = input.getBytes(StandardCharsets.UTF_8);
            byte[] claveBytes = claveSecreta.getBytes(StandardCharsets.UTF_8);
            byte[] resultado = new byte[texto.length];

            for (int i = 0; i < texto.length; i++) {
                resultado[i] = (byte) (texto[i] ^ claveBytes[i % claveBytes.length]);
            }

            return resultado;
        }

        @Override
        public String encriptar(String contenido) {
            byte[] xorBytes = xor(contenido);
            return Base64.getEncoder().encodeToString(xorBytes);
        }

        @Override
        public String desencriptar(String contenido) {
            byte[] datosXor = Base64.getDecoder().decode(contenido);
            byte[] claveBytes = claveSecreta.getBytes(StandardCharsets.UTF_8);
            byte[] resultado = new byte[datosXor.length];

            for (int i = 0; i < datosXor.length; i++) {
                resultado[i] = (byte) (datosXor[i] ^ claveBytes[i % claveBytes.length]);
            }

            return new String(resultado, StandardCharsets.UTF_8);
        }
}
