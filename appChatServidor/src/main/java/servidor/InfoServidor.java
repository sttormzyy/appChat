/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor;

import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public class InfoServidor {
    private String IP;
    private int puertoCliente;
    private int puertoSincronizacion;
    private int puertoMonitoreo;
    
    private ArrayList<String> clientesConectados;
    
    public void agregarClienteConectado(String cliente) {
        this.clientesConectados.add(cliente);
    }
    
    public void eliminarClienteConectado(String cliente) {
        this.clientesConectados.remove(cliente);
    }
    
    public boolean consultarCliente(String cliente) {
        return this.clientesConectados.contains(cliente);
    } 
    
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
        this.clientesConectados = new ArrayList<>();
    }  
}
