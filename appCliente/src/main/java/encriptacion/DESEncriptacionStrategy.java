package encriptacion;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import modelo.Agenda;
import modelo.Contacto;
import modelo.Conversacion;
import modelo.Mensaje;

public class DESEncriptacionStrategy implements EncriptacionStrategy {
    private final SecretKey claveSecreta;
    private final Cipher cifrado;

    public DESEncriptacionStrategy(String clave) throws Exception {
        byte[] claveBytes = clave.getBytes();

        // Validar longitud
        if (claveBytes.length != 8) {
            throw new InvalidKeyException("Clave DES inválida. Debe tener exactamente 8 bytes.");
        }

        this.claveSecreta = new SecretKeySpec(claveBytes, "DES");
        this.cifrado = Cipher.getInstance("DES");
    }

    @Override
    public String encriptarString(String contenido) throws Exception {
        cifrado.init(Cipher.ENCRYPT_MODE, claveSecreta);
        byte[] encrypted = cifrado.doFinal(contenido.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    @Override
    public String desencriptarString(String contenido) throws Exception {
        cifrado.init(Cipher.DECRYPT_MODE, claveSecreta);
        byte[] decrypted = cifrado.doFinal(Base64.getDecoder().decode(contenido));
        return new String(decrypted);
    }
    
    @Override
    public Mensaje encriptarMensaje(Mensaje msj) throws Exception {
        String contenidoEncriptado = encriptarString(msj.getContenido());
        Mensaje msjEncriptado = new Mensaje(contenidoEncriptado, msj.esMio(), msj.getFechaHora());
        return msjEncriptado;
    }
    
    @Override
    public Mensaje desencriptarMensaje(Mensaje msj) throws Exception {
        String contenidoDesencriptado = desencriptarString(msj.getContenido());
        Mensaje msjDesencriptado = new Mensaje(contenidoDesencriptado, msj.esMio(), msj.getFechaHora());
        return msjDesencriptado;
    }
    
    @Override
    public Conversacion encriptarConversacion(Conversacion conv) throws Exception {
        Conversacion convEncriptada = new Conversacion(conv.getContacto());
        convEncriptada.setNotificacion(conv.tieneNotificacion());
        
        for (Mensaje mensaje : conv.getMensajes()) {
            Mensaje msjEncriptado = encriptarMensaje(mensaje);
            String contenido = msjEncriptado.getContenido();
            Boolean esMio = msjEncriptado.esMio();
            String fechaHora = msjEncriptado.getFechaHora();
            convEncriptada.agregarMensaje(contenido, esMio, fechaHora);
        }
        return convEncriptada;
    }
    
    @Override
    public Conversacion desencriptarConversacion(Conversacion conv) throws Exception {
        Conversacion convDesencriptada = new Conversacion(conv.getContacto());
        convDesencriptada.setNotificacion(conv.tieneNotificacion());
        
        for (Mensaje mensaje : conv.getMensajes()) {
            Mensaje msjDesencriptado = desencriptarMensaje(mensaje);
            String contenido = msjDesencriptado.getContenido();
            Boolean esMio = msjDesencriptado.esMio();
            String fechaHora = msjDesencriptado.getFechaHora();
            convDesencriptada.agregarMensaje(contenido, esMio, fechaHora);
        }
        return convDesencriptada;
    }

    @Override
    public Contacto encriptarContacto(Contacto cont) throws Exception{
        String nickRealEncriptado = encriptarString(cont.getNicknameReal());
        String nickAgendaEncriptado = encriptarString(cont.getNicknameAgendado());
        Contacto contEncriptado = new Contacto(nickRealEncriptado, nickAgendaEncriptado);
        return contEncriptado;
    }
    
    @Override
    public Contacto desencriptarContacto(Contacto cont) throws Exception{
        String nickRealDesencriptado = desencriptarString(cont.getNicknameReal());
        String nickAgendaDesencriptado = desencriptarString(cont.getNicknameAgendado());
        Contacto contDesencriptado = new Contacto(nickRealDesencriptado, nickAgendaDesencriptado);
        return contDesencriptado;
    }
    
    @Override
    public Agenda encriptarAgenda(Agenda agnd) throws Exception{
        Agenda agndEncriptada = new Agenda();
        
        for (Object obj : agnd.getContactos()) {
            Contacto cont = (Contacto) obj;
            String nickRealEncriptado = encriptarString(cont.getNicknameReal());
            String nickAgendaEncriptado = encriptarString(cont.getNicknameAgendado());
            agndEncriptada.agregarContacto(nickRealEncriptado);
            agndEncriptada.actualizarContacto(nickRealEncriptado, nickAgendaEncriptado);
        }
        return agndEncriptada;
    }
    
    @Override
    public Agenda desencriptarAgenda(Agenda agnd) throws Exception{
        Agenda agndDesencriptada = new Agenda();
        
        for (Object obj : agnd.getContactos()) {
            Contacto cont = (Contacto) obj;
            String nickRealDesencriptado = desencriptarString(cont.getNicknameReal());
            String nickAgendaDesencriptado = desencriptarString(cont.getNicknameAgendado());
            agndDesencriptada.agregarContacto(nickRealDesencriptado);
            agndDesencriptada.actualizarContacto(nickRealDesencriptado, nickAgendaDesencriptado);
        }
        return agndDesencriptada;
    }
}
