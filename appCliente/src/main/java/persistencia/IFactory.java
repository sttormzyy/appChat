package persistencia;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

/**
 *
 * @author Windows11
 */
public interface IFactory {
    public IPersistenciaConversaciones crearPersistenciaConversacion();
    public IPersistenciaAgenda crearPersistneciaAgenda();
}
