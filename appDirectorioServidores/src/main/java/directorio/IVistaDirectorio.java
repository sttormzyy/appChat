/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package directorio;

import java.awt.event.ActionListener;

/**
 *
 * @author Usuario
 */
public interface IVistaDirectorio {
    public String getIpDirectorio();
    public int getPuertoServidores();
    public int getPuertoClientes();
    public void setActionListener(ActionListener c);
}
