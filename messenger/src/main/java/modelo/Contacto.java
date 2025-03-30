/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

public class Contacto extends Persona {
    private static int idCounter = 1;
    private final int id;

    // Constructor
    public Contacto(String nickname, String ip, int port) {
        super(nickname, ip, port);
        this.id = idCounter++;
    }

    // Getter para obtener el ID
    public int getId() {
        return id;
    }
    
}