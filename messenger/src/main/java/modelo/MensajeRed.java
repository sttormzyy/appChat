/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

public class MensajeRed {
    private final String myNickname;
    private final String myIp;
    private final int myPort;
    private final String destinyIp;
    private final int destinyPuerto;
    private final String contenido;
    
    public MensajeRed(String nickname, String myIp, int myPort, String destinyIp, int destinyPuerto, String contenido) {
        this.myNickname = nickname;
        this.myIp = myIp;
        this.myPort = myPort;
        this.destinyIp = destinyIp;
        this.destinyPuerto = destinyPuerto;
        this.contenido = contenido;
    }

    public String getMyNickname() {
        return myNickname;
    }
    
    public String getMiIp() {
        return myIp;
    }

    public int getMiPuerto() {
        return myPort;
    }

    public String getDestinyIp() {
        return destinyIp;
    }

    public int getDestinyPuerto() {
        return destinyPuerto;
    }
    
    public String getContenido() {
        return contenido;
    }  
}
