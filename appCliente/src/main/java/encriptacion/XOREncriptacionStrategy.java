/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package encriptacion;

import modelo.Agenda;
import modelo.Contacto;
import modelo.Conversacion;
import modelo.Mensaje;

/**
 *
 * @author user
 */
public class XOREncriptacionStrategy implements EncriptacionStrategy{
    private final String claveSecreta;

    public XOREncriptacionStrategy(String clave) throws Exception {
        if (clave == null || clave.isEmpty()){
            throw new IllegalArgumentException("La clave XOR no puede ser nula ni vacía.");
        }
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

    @Override
    public String encriptarString(String contenido) {
        return xor(contenido);
    }

    @Override
    public String desencriptarString(String contenido) {
        return xor(contenido);
    }

    @Override
    public Mensaje encriptarMensaje(Mensaje msj) {
        String contenidoEncriptado = xor(msj.getContenido());
        return new Mensaje(contenidoEncriptado, msj.isMine(), msj.getFechaHora());
    }

    @Override
    public Mensaje desencriptarMensaje(Mensaje msj) {
        String contenidoDesencriptado = xor(msj.getContenido());
        return new Mensaje(contenidoDesencriptado, msj.isMine(), msj.getFechaHora());
    }

    @Override
    public Conversacion encriptarConversacion(Conversacion conv) throws Exception {
        Conversacion convEncriptada = new Conversacion(encriptarContacto(conv.getContacto()));
        convEncriptada.setNotificacion(conv.getNotificacion());
        for (Mensaje mensaje : conv.getMensajes()) {
            Mensaje msjEncriptado = encriptarMensaje(mensaje);
            convEncriptada.agregarMensaje(msjEncriptado.getContenido(), msjEncriptado.isMine(), msjEncriptado.getFechaHora());
        }
        return convEncriptada;
    }

    @Override
    public Conversacion desencriptarConversacion(Conversacion conv) throws Exception {
        Conversacion convDesencriptada = new Conversacion(desencriptarContacto(conv.getContacto()));
        convDesencriptada.setNotificacion(conv.getNotificacion());
        for (Mensaje mensaje : conv.getMensajes()) {
            Mensaje msjDesencriptado = desencriptarMensaje(mensaje);
            convDesencriptada.agregarMensaje(msjDesencriptado.getContenido(), msjDesencriptado.isMine(), msjDesencriptado.getFechaHora());
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
