package encriptacion;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class AESEncriptacion implements IEncriptacion {
    private final SecretKey claveSecreta;
    private  Cipher cifrado = null;

    public AESEncriptacion(String clave) {
        byte[] claveBytes = clave.getBytes();
        this.claveSecreta = new SecretKeySpec(claveBytes, "AES");
        try {
            this.cifrado = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AESEncriptacion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(AESEncriptacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String encriptar(String contenido){
        try {
            cifrado.init(Cipher.ENCRYPT_MODE, claveSecreta);
            byte[] contenidoEncriptado = null;
            try {
                contenidoEncriptado = cifrado.doFinal(contenido.getBytes());
            } catch (IllegalBlockSizeException ex) {
                Logger.getLogger(AESEncriptacion.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BadPaddingException ex) {
                Logger.getLogger(AESEncriptacion.class.getName()).log(Level.SEVERE, null, ex);
            }
            return Base64.getEncoder().encodeToString(contenidoEncriptado);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(AESEncriptacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    @Override
    public String desencriptar(String contenido) {
        try {
            cifrado.init(Cipher.DECRYPT_MODE, claveSecreta);
            byte[] contenidoDesencriptado = null;
            try {
                contenidoDesencriptado = cifrado.doFinal(Base64.getDecoder().decode(contenido));
            } catch (IllegalBlockSizeException ex) {
                Logger.getLogger(AESEncriptacion.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BadPaddingException ex) {
                Logger.getLogger(AESEncriptacion.class.getName()).log(Level.SEVERE, null, ex);
            }
            return new String(contenidoDesencriptado);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(AESEncriptacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
}

