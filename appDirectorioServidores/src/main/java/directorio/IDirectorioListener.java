/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package directorio;

import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public interface IDirectorioListener {
    public void directorioListo();
    public void actualizarEstado(ArrayList<InfoServidor> servidores);
}
