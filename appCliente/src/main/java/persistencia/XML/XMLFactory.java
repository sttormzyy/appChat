/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.XML;

import persistencia.IFactory;
import persistencia.IPersistenciaAgenda;
import persistencia.IPersistenciaConversaciones;

/**
 *
 * @author Windows11
 */
public class XMLFactory implements IFactory {

    @Override
    public IPersistenciaConversaciones crearPersistenciaConversacion() {
        return new XMLPersitenciaConversaciones();
    }

    @Override
    public IPersistenciaAgenda crearPersistneciaAgenda() {
        return new XMLPersistenciaAgenda();
    }
}