/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

public class MensajeRed {
    private Mensaje mensajeOrg;
    private final String myNickname;
    private final String myIp;
    private final int myPort;
    private final String destinyIp;
    private final int destinyPort;
    private final String contenido;
    
    // Constructor
    public MensajeRed(String nickname, String myIp, int myPort, String destinyIp, int destinyPort, String contenido) {
        this.myNickname = nickname;
        this.myIp = myIp;
        this.myPort = myPort;
        this.destinyIp = destinyIp;
        this.destinyPort = destinyPort;
        this.contenido = contenido;
    }

    // Getter para obtener el nickname
    public String getMyNickname() {
        return myNickname;
    }
    
    // Getter para obtener el ID
    public String getMyIp() {
        return myIp;
    }

    // Getter para obtener el puerto
    public int getMyPort() {
        return myPort;
    }

    // Getter para obtener el ID
    public String getDestinyIp() {
        return destinyIp;
    }

    // Getter para obtener el puerto
    public int getDestinyPort() {
        return destinyPort;
    }
    
    // Getter para obtener el contenido
    public String getContenido() {
        return contenido;
    }
    
}
