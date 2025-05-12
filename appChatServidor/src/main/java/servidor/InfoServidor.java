/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor;

/**
 *
 * @author Usuario
 */
public class InfoServidor {
    private String IP;
    private int puertoCliente;
    private int puertoSincronizacion;
    private int puertoMonitoreo;

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public int getPuertoCliente() {
        return puertoCliente;
    }

    public void setPuertoCliente(int puertoCliente) {
        this.puertoCliente = puertoCliente;
    }

    public int getPuertoSincronizacion() {
        return puertoSincronizacion;
    }

    public void setPuertoSincronizacion(int puertoSincronizacion) {
        this.puertoSincronizacion = puertoSincronizacion;
    }

    public int getPuertoMonitoreo() {
        return puertoMonitoreo;
    }

    public void setPuertoMonitoreo(int puertoMonitoreo) {
        this.puertoMonitoreo = puertoMonitoreo;
    }

    public InfoServidor(String IP, int puertoCliente, int puertoSincronizacion, int puertoMonitoreo) {
        this.IP = IP;
        this.puertoCliente = puertoCliente;
        this.puertoSincronizacion = puertoSincronizacion;
        this.puertoMonitoreo = puertoMonitoreo;
    }  
}
