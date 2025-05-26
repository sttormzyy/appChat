/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia;

import modelo.Usuario;

/**
 *
 * @author Usuario
 */
public interface IPersistencia {
    public void persistir(Usuario usuario);
    public void despersistir(Usuario usuario);
}
