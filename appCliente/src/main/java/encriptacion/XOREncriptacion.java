/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package encriptacion;

/**
 *
 * @author Usuario
 */
public class XOREncriptacion implements IEncriptacion{
    private final String claveSecreta;

    public XOREncriptacion(String clave){
        this.claveSecreta = clave;
    }
    
    private String xor(String input) {
        char[] texto = input.toCharArray();
        char[] claveChars = claveSecreta.toCharArray();
        char[] resultado = new char[texto.length];

        for (int i = 0; i < texto.length; i++) {
            resultado[i] = (char) (texto[i] ^ claveChars[i % claveChars.length]);
        }

        return new String(resultado);
    }


    public String encriptar(String contenido) {
        return xor(contenido);
    }


    public String desencriptar(String contenido) {
        return xor(contenido);
    }
}
