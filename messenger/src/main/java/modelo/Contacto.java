/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

public class Contacto extends Persona {
	
	private String nicknameAgendado;

    public Contacto(String nicknameAgendado,String nicknameReal, String ip, int puerto) {
        super(nicknameReal, ip, puerto);
        this.nicknameAgendado = nicknameAgendado;
    }
    
    public String getNicknameAgendado() {
    	return nicknameAgendado;
    }
}