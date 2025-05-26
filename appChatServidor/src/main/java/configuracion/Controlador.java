/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package configuracion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import servidor.ComunicacionDirectorio;
import servidor.Echo;
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
    private Sincronizador sincronizador;
    private ComunicacionDirectorio comunicacionDirectorio;
    private Echo echo;
    
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
                sincronizador.detener();
                comunicacionDirectorio.detener();
                echo.detener();
                ventanaServidor.dispose();
                break;
            default:
                System.out.println("Comando desconocido: " + comando);
                break;
        }
    }

    public void iniciarServidor() {
        String ip, ipDirectorio;
        int puertoCliente,puertoSincronizacion, puertoParaDirectorio, puertoDirectorio, puertoPing;
        
        ip = configuracion.getIP();
        puertoCliente = configuracion.getPuertoCliente();
        puertoSincronizacion = configuracion.getPuertoSincronizacion();
        puertoParaDirectorio = configuracion.getPuertoParaDirectorio();
        puertoPing = configuracion.getPuertoPing();
        ipDirectorio = configuracion.getIPDirectorio();
        puertoDirectorio = configuracion.getPuertoDirectorio();
        
        configuracion.dispose();
        
        InfoServidor infoServidor = new InfoServidor(ip, puertoCliente, puertoSincronizacion, puertoParaDirectorio, puertoPing);
        sincronizador = new Sincronizador(puertoSincronizacion);
        comunicacionDirectorio = new ComunicacionDirectorio(ipDirectorio, puertoDirectorio, puertoParaDirectorio, puertoPing);
        servidor = new Servidor(infoServidor, sincronizador, comunicacionDirectorio);
        echo = new Echo(puertoPing);
        
        new Thread(comunicacionDirectorio).start();
        new Thread(sincronizador).start();
        new Thread(servidor).start();
        new Thread(echo).start();
        sincronizador.setServidor(servidor);
        
        comunicacionDirectorio.setSincronizador(sincronizador);
        comunicacionDirectorio.setServidor(servidor);

        if(comunicacionDirectorio.registrarServidorEnDirectorio(ipDirectorio, puertoDirectorio, infoServidor))
        {
            ventanaServidor = new VentanaServidor(this);
            ventanaServidor.setVisible(true);
            servidor.setGui(ventanaServidor);
        }
        else
        {
            servidor.detenerServidor();
            sincronizador.detener();
            comunicacionDirectorio.detener();
            echo.detener();
        };
    }
}
