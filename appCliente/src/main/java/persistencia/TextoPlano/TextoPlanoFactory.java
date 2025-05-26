/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.TextoPlano;

import persistencia.IFactory;
import persistencia.IPersistenciaAgenda;
import persistencia.IPersistenciaConversaciones;

/**
 *
 * @author Windows11
 */
public class TextoPlanoFactory implements IFactory {

    @Override
    public IPersistenciaConversaciones crearPersistenciaConversacion() {
        return new TextoPlanoPersitenciaConversaciones();
    }

    @Override
    public IPersistenciaAgenda crearPersistneciaAgenda() {
        return new TextoPlanoPersistenciaAgenda();
    }

}
