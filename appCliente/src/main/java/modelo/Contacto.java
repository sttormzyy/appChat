/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

public class Contacto {	
    private String nicknameAgendado;
    private String nicknameReal;

    public Contacto(String nicknameAgendado,String nicknameReal) {
        this.nicknameReal = nicknameReal;
        this.nicknameAgendado = nicknameAgendado;
    }
    
    public String getNicknameAgendado() {
    	return nicknameAgendado;
    }

    public String getNicknameReal() {
        return nicknameReal;
    }
    
    public void setNicknameAgendado(String nickname)
    {
        this.nicknameAgendado = nickname;
    }
}