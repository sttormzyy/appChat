/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package configuracion;

import configuracion.Configuracion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import servidor.Servidor;

/**
 *
 * @author Usuario
 */
public class Controlador implements ActionListener{
    Servidor servidor;
    Configuracion configuracion;
    
    public void Controlador(){}
    
    public void setVista(Configuracion config)
    {
        this.configuracion = config;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String ip, ipDirectorio;
        int puerto, puertoDirectorio;
        
        ip = configuracion.getIP();
        puerto = configuracion.getPuerto();
        ipDirectorio = configuracion.getIPDirectorio();
        puertoDirectorio = configuracion.getPuertoDirectorio();
        configuracion.dispose();
        this.servidor = new Servidor(ip,puerto,ipDirectorio,puertoDirectorio);
    }
}
