/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package red;

/**
 *
 * @author Usuario
 */
public interface IEmisor {
    public void pedirMensajesPendientes();
    public void pedirListaClientes();
    public boolean enviarMensaje(MensajeDeRed msj);
    public String getEstado();
}
