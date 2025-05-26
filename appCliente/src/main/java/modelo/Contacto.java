/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "contacto")
public class Contacto {	
    
    private String nicknameAgendado;
    private String nicknameReal;

    public Contacto(String nicknameAgendado,String nicknameReal) {
        this.nicknameReal = nicknameReal;
        this.nicknameAgendado = nicknameAgendado;
    }

    public Contacto() {
    }
    
    @XmlElement(name = "nicknameAgendado")
    public String getNicknameAgendado() {
    	return nicknameAgendado;
    }
    
    @XmlElement(name = "nicknameReal")
    public String getNicknameReal() {
        return nicknameReal;
    }
    
    public void setNicknameAgendado(String nickname)
    {
        this.nicknameAgendado = nickname;
    }

    public void setNicknameReal(String nicknameReal) {
        this.nicknameReal = nicknameReal;
    }
    
    // Metodos persistencia
    
    public static String toTextoPlano(Contacto c) {
        return c.nicknameAgendado + "|" + c.nicknameReal;
    }
    
    public String toString(){
        return this.nicknameAgendado + "|" + this.nicknameReal;
    }
    
    public static Contacto fromTextoPlano(String s) {   
        String[] partes = s.split("\\|");
        String nicknameAgendado = partes[0];
        String nicknameReal = partes[1];
        return new Contacto(nicknameAgendado, nicknameReal);
    }
}