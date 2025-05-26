/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import modelo.Usuario;

/**
 *
 * @author vanci
 */
public class Persistencia implements IPersistencia {
    private IPersistenciaAgenda persistenciaAgenda;
    private IPersistenciaConversaciones persistenciaConversaciones;
    private String nickname;

    public Persistencia(IFactory abstractFactory, String nickname) {
        this.persistenciaAgenda = abstractFactory.crearPersistneciaAgenda();
        this.persistenciaConversaciones = abstractFactory.crearPersistenciaConversacion();
        this.nickname = nickname;
    }
    
    public void despersistir(Usuario usuario) {
        persistenciaAgenda.despersistirAgenda(usuario.getAgenda(), nickname);
        persistenciaConversaciones.despersistirConversaciones(usuario, nickname);
    }
    
    public void persistir(Usuario usuario) {
        System.out.println("Persistiendo");
        persistenciaAgenda.persistirAgenda(usuario.getAgenda(), nickname);
        persistenciaConversaciones.persistirConversaciones(usuario.getConversaciones(), nickname);
    }
    
}
