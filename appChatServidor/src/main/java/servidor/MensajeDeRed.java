/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor;

import java.io.Serializable;

public class MensajeDeRed implements Serializable{
   private String nicknameOrigen;
   private String nicknameDestino;
   private String contenido;
   private String horaEnvio;
    
    // Constructor
    public MensajeDeRed(String nicknameOrigen, String nicknameDestino, String contenido, String horaEnvio) {
        this.nicknameOrigen = nicknameOrigen;
        this.nicknameDestino = nicknameDestino;
        this.contenido = contenido;
        this.horaEnvio = horaEnvio;
    }
    
    public String getNicknameOrigen() {
        return nicknameOrigen;
    }
    
    public String getNicknameDestino() {
        return nicknameDestino;
    }
    
    public String getContenido() {
        return contenido;
    }
    
    public String getHoraEnvio() {
        return horaEnvio;
    }
    
}
