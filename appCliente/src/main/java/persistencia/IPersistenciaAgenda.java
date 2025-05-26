/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia;

import modelo.Agenda;

/**
 *
 * @author Windows11
 */
public interface IPersistenciaAgenda {
    public void persistirAgenda(Agenda agenda, String nickname);
    public void despersistirAgenda(Agenda agenda, String nickname);
}
