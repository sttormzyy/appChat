/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package red;

import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public interface IReceptor {
    void recibirMensaje(MensajeDeRed mensaje);
    void recibirListaClientes(ArrayList<String> clientes);    
}
