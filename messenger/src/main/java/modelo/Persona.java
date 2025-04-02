/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

public class Persona {
    private final String nickname;
    private final String ip;
    private final int puerto;

    // Constructor
    public Persona(String nickname, String ip, int puerto) {
        this.nickname = nickname;
        this.ip = ip;
        this.puerto = puerto;
    }

    // Getters
    public String getNickname() {
        return nickname;
    }

    public String getIp() {
        return ip;
    }

    public int getPuerto() {
        return puerto;
    }

}
