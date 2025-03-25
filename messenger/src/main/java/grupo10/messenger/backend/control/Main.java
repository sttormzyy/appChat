/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package grupo10.messenger.backend.control;

/**
 *
 * @author user
 */
public class Main {
    public static void main(String args[]) {
        Control c = Control.getInstance();
        c.registrar("Fran", "181.231.105.167", 12345);
    }
}
