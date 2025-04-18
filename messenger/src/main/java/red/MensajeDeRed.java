/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package red;

import modelo.*;

public class MensajeDeRed {
   private String nicknameOrigen;
   private String nicknameDestino;
   private String contenido;
    
    // Constructor
    public MensajeDeRed(String nicknameOrigen, String nicknameDestino, String contenido) {
        this.nicknameOrigen = nicknameOrigen;
        this.nicknameDestino = nicknameDestino;
        this.contenido = contenido;
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
    
}
