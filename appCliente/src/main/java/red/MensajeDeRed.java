/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package red;

public class MensajeDeRed {
   private String nicknameOrigen;
   private String nicknameDestino;
   private String contenido;
   private String horaEnvio;
   private String metodoEncriptacion;
    
    // Constructor
    public MensajeDeRed(String nicknameOrigen, String nicknameDestino, String contenido, String horaEnvio, String metodoEncriptacion) {
        this.nicknameOrigen = nicknameOrigen;
        this.nicknameDestino = nicknameDestino;
        this.contenido = contenido;
        this.horaEnvio = horaEnvio;
        this.metodoEncriptacion = metodoEncriptacion;
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
    
    public String getMetodoEncriptacion() {
        return metodoEncriptacion;
    }
    
}
