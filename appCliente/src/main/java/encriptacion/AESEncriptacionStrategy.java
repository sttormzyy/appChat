package encriptacion;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import modelo.Agenda;
import modelo.Contacto;
import modelo.Conversacion;
import modelo.Mensaje;

class AESEncriptacionStrategy implements EncriptacionStrategy {
    private final SecretKey claveSecreta;
    private final Cipher cifrado;

    public AESEncriptacionStrategy(String clave) throws Exception {
        byte[] claveBytes = clave.getBytes();

        // Validar longitud
        if (claveBytes.length != 16 && claveBytes.length != 24 && claveBytes.length != 32) {
            throw new InvalidKeyException("Clave AES inválida. Debe tener 16, 24 o 32 bytes.");
        }

        this.claveSecreta = new SecretKeySpec(claveBytes, "AES");
        this.cifrado = Cipher.getInstance("AES");
    }

    @Override
    public String encriptarString(String contenido) throws Exception {
        cifrado.init(Cipher.ENCRYPT_MODE, claveSecreta);
        byte[] contenidoEncriptado = cifrado.doFinal(contenido.getBytes());
        return Base64.getEncoder().encodeToString(contenidoEncriptado);
    }

    @Override
    public String desencriptarString(String contenido) throws Exception {
        cifrado.init(Cipher.DECRYPT_MODE, claveSecreta);
        byte[] contenidoDesencriptado = cifrado.doFinal(Base64.getDecoder().decode(contenido));
        return new String(contenidoDesencriptado);
    }
    
    @Override
    public Mensaje encriptarMensaje(Mensaje msj) throws Exception {
        String contenidoEncriptado = encriptarString(msj.getContenido());
        Mensaje msjEncriptado = new Mensaje(contenidoEncriptado, msj.isMine(), msj.getFechaHora());
        return msjEncriptado;
    }
    
    @Override
    public Mensaje desencriptarMensaje(Mensaje msj) throws Exception {
        String contenidoDesencriptado = desencriptarString(msj.getContenido());
        Mensaje msjDesencriptado = new Mensaje(contenidoDesencriptado, msj.isMine(), msj.getFechaHora());
        return msjDesencriptado;
    }
    
    @Override
    public Conversacion encriptarConversacion(Conversacion conv) throws Exception {
        Conversacion convEncriptada = new Conversacion(encriptarContacto(conv.getContacto()));
        convEncriptada.setNotificacion(conv.getNotificacion());
        
        for (Mensaje mensaje : conv.getMensajes()) {
            Mensaje msjEncriptado = encriptarMensaje(mensaje);
            String contenido = msjEncriptado.getContenido();
            Boolean esMio = msjEncriptado.isMine();
            String fechaHora = msjEncriptado.getFechaHora();
            convEncriptada.agregarMensaje(contenido, esMio, fechaHora);
        }
        return convEncriptada;
    }
    
    @Override
    public Conversacion desencriptarConversacion(Conversacion conv) throws Exception {
        Conversacion convDesencriptada = new Conversacion(desencriptarContacto(conv.getContacto()));
        convDesencriptada.setNotificacion(conv.getNotificacion());
        
        for (Mensaje mensaje : conv.getMensajes()) {
            Mensaje msjDesencriptado = desencriptarMensaje(mensaje);
            String contenido = msjDesencriptado.getContenido();
            Boolean esMio = msjDesencriptado.isMine();
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
        
        for (Contacto cont : agnd.getContactos()) {
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

        for (Contacto cont : agnd.getContactos()) {
            String nickRealDesencriptado = desencriptarString(cont.getNicknameReal());
            String nickAgendaDesencriptado = desencriptarString(cont.getNicknameAgendado());
            agndDesencriptada.agregarContacto(nickRealDesencriptado);
            agndDesencriptada.actualizarContacto(nickRealDesencriptado, nickAgendaDesencriptado);
        }
        return agndDesencriptada;
    }
}

