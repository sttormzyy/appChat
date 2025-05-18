/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public class InfoServidor implements Serializable{
    private String IP;
    private int puertoCliente;
    private int puertoSincronizacion;
    private int puertoParaDirectorio;
    private int puertoPing;
    
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

    public int getPuertoParaDirectorio() {
        return puertoParaDirectorio;
    }

    public void setPuertoParaDirectorio(int puertoParaDirectorio) {
        this.puertoParaDirectorio = puertoParaDirectorio;
    }

    public int getPuertoPing() {
        return puertoPing;
    }

    public void setPuertoPing(int puertoPing) {
        this.puertoPing = puertoPing;
    }

    public InfoServidor()
    {
        
    }
    
    public InfoServidor(String IP, int puertoCliente, int puertoSincronizacion, int puertoParaDirectorio, int puertoPing) {
        this.IP = IP;
        this.puertoCliente = puertoCliente;
        this.puertoSincronizacion = puertoSincronizacion;
        this.puertoParaDirectorio = puertoParaDirectorio;
        this.puertoPing = puertoPing;
    }  
}
