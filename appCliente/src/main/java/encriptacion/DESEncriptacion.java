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


public class DESEncriptacion implements IEncriptacion {
    private final SecretKey claveSecreta;
    private  Cipher cifrado = null;

    public DESEncriptacion(String clave) {
        byte[] claveBytes = clave.getBytes();
        this.claveSecreta = new SecretKeySpec(claveBytes, "DES");
        try {
            this.cifrado = Cipher.getInstance("DES");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(DESEncriptacion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(DESEncriptacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String encriptar(String contenido){
        try {
            cifrado.init(Cipher.ENCRYPT_MODE, claveSecreta);
            byte[] encrypted = null;
            try {
                encrypted = cifrado.doFinal(contenido.getBytes());
            } catch (IllegalBlockSizeException ex) {
                Logger.getLogger(DESEncriptacion.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BadPaddingException ex) {
                Logger.getLogger(DESEncriptacion.class.getName()).log(Level.SEVERE, null, ex);
            }
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(DESEncriptacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    @Override
    public String desencriptar(String contenido){
        try {
            cifrado.init(Cipher.DECRYPT_MODE, claveSecreta);
            byte[] decrypted = null;
            try {
                decrypted = cifrado.doFinal(Base64.getDecoder().decode(contenido));
            } catch (IllegalBlockSizeException ex) {
                Logger.getLogger(DESEncriptacion.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BadPaddingException ex) {
                Logger.getLogger(DESEncriptacion.class.getName()).log(Level.SEVERE, null, ex);
            }
            return new String(decrypted);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(DESEncriptacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
}
