/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package configuracion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import monitoreo.ComunicacionDirectorio;
import monitoreo.Echo;
import servidor.InfoServidor;
import servidor.Servidor;
import sincronizador.Sincronizador;

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
                apagarServidor();
                break;

            case "COMPONENTE CAIDO":
                new VentanaError(null, true, (String)e.getSource());
                apagarServidor();
                break;
        }
    }

    private void apagarServidor()
    {
        echo.detener();
        ventanaServidor.dispose();
        comunicacionDirectorio.detener();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
         }
        servidor.detener();
        sincronizador.detener();        
    }


    /**
     * Inicia un servidor nuevo en base a los parametros indicados por el administrador
     */
    public void iniciarServidor() {
        String ip, ipDirectorio;
        int puertoCliente, puertoSincronizacion, puertoParaDirectorio, puertoDirectorio, puertoPing;

        ip = configuracion.getIP();
        puertoCliente = configuracion.getPuertoCliente();
        puertoSincronizacion = configuracion.getPuertoSincronizacion();
        puertoParaDirectorio = configuracion.getPuertoParaDirectorio();
        puertoPing = configuracion.getPuertoPing();
        ipDirectorio = configuracion.getIPDirectorio();
        puertoDirectorio = configuracion.getPuertoDirectorio();

        
        // Validar IPs
        if (!esIPValida(ip) || !esIPValida(ipDirectorio)) {
            new VentanaError(null, true, "Formato de IP inválido.\n Verifique las direcciones IP.");
            return;
        }

        // Validar puertos válidos (rango)
        if (!esPuertoValido(puertoCliente) ||
            !esPuertoValido(puertoSincronizacion) ||
            !esPuertoValido(puertoParaDirectorio) ||
            !esPuertoValido(puertoPing) ||
            !esPuertoValido(puertoDirectorio)) {

            new VentanaError(null, true, "Uno o más puertos son inválidos (deben estar entre 1024 y 65535).");
            return;
        }
    
        // Verificar si los puertos están disponibles
        if (!puertoLibre(puertoCliente) ||
            !puertoLibre(puertoSincronizacion) ||
            !puertoLibre(puertoParaDirectorio) ||
            !puertoLibre(puertoPing)) {

            new VentanaError(null, true, "Uno o más puertos ya están en uso.\n No se puede iniciar el servidor.");
            configuracion.dispose(); 
            return;
        }

        configuracion.dispose();

        InfoServidor infoServidor = new InfoServidor(ip, puertoCliente, puertoSincronizacion, puertoParaDirectorio, puertoPing);
        sincronizador = new Sincronizador(puertoSincronizacion,this);
        comunicacionDirectorio = new ComunicacionDirectorio(ipDirectorio, puertoDirectorio, puertoParaDirectorio, puertoPing,this);
        servidor = new Servidor(infoServidor, sincronizador, comunicacionDirectorio, this);
        echo = new Echo(puertoPing, this);

        new Thread(comunicacionDirectorio).start();
        new Thread(sincronizador).start();
        new Thread(servidor).start();
        new Thread(echo).start();
        sincronizador.setServidor(servidor);
        comunicacionDirectorio.setSincronizador(sincronizador);
        comunicacionDirectorio.setServidor(servidor);

        if (comunicacionDirectorio.registrarServidorEnDirectorio(ipDirectorio, puertoDirectorio, infoServidor)) {
            ventanaServidor = new VentanaServidor(this);
            ventanaServidor.setVisible(true);
            servidor.setGui(ventanaServidor);
        } else {
            servidor.detener();
            sincronizador.detener();
            comunicacionDirectorio.detener();
            echo.detener();
            new VentanaError(null, true, "Error al registrar servidor");
        }
    }

    private boolean puertoLibre(int puerto) {
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            serverSocket.setReuseAddress(true);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    private boolean esPuertoValido(int puerto) {
        return puerto >= 1024 && puerto <= 65535;
    }

    private boolean esIPValida(String ip) {
        String regex = 
            "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}" +
            "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
        return ip != null && ip.matches(regex);
    }


}
