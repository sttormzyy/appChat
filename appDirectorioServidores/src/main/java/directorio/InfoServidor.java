/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package directorio;


/**
 *
 * @author Usuario
 */
public class InfoServidor {
    private String IP;
    private int puertoCliente;
    private int puertoSincronizacion;
    private int puertoParaDirectorio;
    private int puertoPing;
    private int cantUsuariosActivos = 0;
    private boolean estaListo = true;
    
    
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

    public int getPuertoPing() {
        return puertoPing;
    }

    public void setPuertoPing(int puertoPing) {
        this.puertoPing = puertoPing;
    }

    public int getPuertoParaDirectorio() {
        return puertoParaDirectorio;
    }

    public void setPuertoParaDirectorio(int puertoParaDirectorio) {
        this.puertoParaDirectorio = puertoParaDirectorio;
    }
    
    public void agregarUsuarioActivo()
    {
        this.cantUsuariosActivos += 1;
    }
    
    public void eliminarUsuarioActivo()
    {
        cantUsuariosActivos -= 1;
    }
    
    public int getCantidadUsuariosActivos()
    {
        return this.cantUsuariosActivos;
    }
    
    public boolean estaListo()
    {
        return estaListo;
    }
    
    public void setEstado(boolean estado)
    {
        this.estaListo = estado;
    }

    public InfoServidor(String IP, int puertoCliente, int puertoSincronizacion,int puertoParaDirectorio, int puertoPing, boolean estado) 
    {
        this.IP = IP;
        this.puertoCliente = puertoCliente;
        this.puertoSincronizacion = puertoSincronizacion;
        this.puertoParaDirectorio = puertoParaDirectorio;
        this.puertoPing = puertoPing;
        this.estaListo = estado;
    }  
}
