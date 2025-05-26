/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia;

import java.util.ArrayList;
import modelo.Agenda;
import modelo.Conversacion;
import modelo.Usuario;

/**
 *
 * @author Windows11
 */
public interface IPersistenciaConversaciones {
    public void persistirConversaciones(ArrayList<Conversacion> conversaciones, String nickname);
    public void despersistirConversaciones(Usuario usuario, String nickname);
}
