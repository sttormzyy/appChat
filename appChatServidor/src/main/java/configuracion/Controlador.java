/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package configuracion;

import configuracion.Configuracion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import servidor.ComunicacionDirectorio;
import servidor.InfoServidor;
import servidor.Servidor;
import servidor.Sincronizador;

/**
 *
 * @author Usuario
 */
public class Controlador implements ActionListener{
    private Configuracion configuracion;
    private VentanaServidor ventanaServidor;
    private Servidor servidor;
    
    public void Controlador(){}
    
    public void setVista(Configuracion config)
    {
        this.configuracion = config;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand(); // obtiene el comando

        switch (comando) {
            case "INICIAR SERVIDOR":
                iniciarServidor();
                break;
            case "APAGAR SERVIDOR":
                servidor.detenerServidor();
                ventanaServidor.dispose();
                break;
            default:
                System.out.println("Comando desconocido: " + comando);
                break;
        }
    }

    public void iniciarServidor() {
        String ip, ipDirectorio;
        int puertoCliente,puertoSincronizacion, puertoMonitoreo, puertoDirectorio;
        
        ip = configuracion.getIP();
        puertoCliente = configuracion.getPuertoCliente();
        puertoSincronizacion = configuracion.getPuertoSincronizacion();
        puertoMonitoreo = configuracion.getPuertoMonitoreo();
        ipDirectorio = configuracion.getIPDirectorio();
        puertoDirectorio = configuracion.getPuertoDirectorio();
        configuracion.dispose();
        
        InfoServidor infoServidor = new InfoServidor(ip, puertoCliente, puertoSincronizacion);
        Sincronizador sincronizador = new Sincronizador(puertoSincronizacion);
        ComunicacionDirectorio comunicacionDirectorio = new ComunicacionDirectorio(ipDirectorio, puertoDirectorio, puertoMonitoreo);
        this.servidor = new Servidor(infoServidor, sincronizador, comunicacionDirectorio);
        
        new Thread(sincronizador).start();
        new Thread(servidor).start();
        comunicacionDirectorio.setSincronizador(sincronizador);

        if(comunicacionDirectorio.registrarServidorEnDirectorio(ipDirectorio, puertoDirectorio, infoServidor))
        {
            ventanaServidor = new VentanaServidor(this);
            ventanaServidor.setVisible(true);
        }
        else
        {
            // no se pudo registrar en directorio
            servidor.detenerServidor();
        };
    }
}
