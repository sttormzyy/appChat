/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package directorio;

import monitor.Monitor;
import gui.IConfiguracionListener;
import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public class Directorio implements IConfiguracionListener {
    private ArrayList<InfoServidor> servidores;
    public ArrayList<IDirectorioListener> observadores;
    private Monitor monitor;
    private ComunicacionClientes comunicacionClientes;
    
    public Directorio(){
        servidores = new ArrayList();
        observadores = new ArrayList();
    }
    
    @Override
    public void configuracionLista(String IPDirectorio, int puertoParaServidores, int puertoParaClientes){
        this.monitor = new Monitor(this,puertoParaServidores);
        new Thread (monitor).start();
            
        this.comunicacionClientes = new ComunicacionClientes(this,puertoParaClientes);
        new Thread (comunicacionClientes).start();
        
        notificarObservadoresListo();
    }
    
    
    public synchronized InfoServidor getServidorConMenosCarga(){
        InfoServidor servidor = null;
        int i = 0;
        int actMin = 99999;
        while(i<this.servidores.size()){
            if(actMin>this.servidores.get(i).getCantidadUsuariosActivos() && this.servidores.get(i).estaListo()){
                servidor = this.servidores.get(i);
                actMin = servidor.getCantidadUsuariosActivos();
            }
            i++;
        }
        return servidor;
    }

    public synchronized  ArrayList<InfoServidor> getServidores() {
        return this.servidores;
    }

    public synchronized ArrayList<InfoServidor> getServidoresActivos() {
        ArrayList<InfoServidor> activos = new ArrayList<>();
        for (InfoServidor servidor : this.servidores) {
             System.out.println("ESTADO "+servidor.estaListo());
            if (servidor.estaListo()) {
               
                activos.add(servidor);
            }
        }
        return activos;
    }

        
    public synchronized void agregarServidor(InfoServidor servidor) 
    {
        InfoServidor servidorYaEsta = this.buscarServidor(servidor.getIP(), servidor.getPuertoCliente());
        if(servidorYaEsta != null)
        {
            servidorYaEsta.setEstado(true);
        }
        else
        {
            servidores.add(servidor);
        }
        notificarObservadores();
    }
    
    public synchronized void eliminarServidores(ArrayList<InfoServidor> servidores) {
        InfoServidor servidorInactivo;
        for (InfoServidor servidor: servidores)
        {
            servidorInactivo = this.buscarServidor(servidor.getIP(),servidor.getPuertoCliente());
            servidorInactivo.setEstado(false);   
            servidorInactivo.setCantidadUsuariosActivos(0);
        }
        notificarObservadores();
    }
    
    public synchronized void disminuirContadorEnUno(String ip, int puerto) {
        InfoServidor servidor = buscarServidor(ip, puerto);
        servidor.eliminarUsuarioActivo();
        notificarObservadores();
    }
    
    public synchronized void incrementarContadorEnUno(String ip, int puerto) {
        InfoServidor servidor = buscarServidor(ip, puerto);
        servidor.agregarUsuarioActivo();
        notificarObservadores();
    }
    
    public void servidorListo(String ip, int puerto){
        this.buscarServidor(ip, puerto).setEstado(true);
    }
    
    public InfoServidor buscarServidor(String ip, int puerto) {
        int i = 0;
        while (i < this.servidores.size() && 
               (!this.servidores.get(i).getIP().equalsIgnoreCase(ip) || 
                this.servidores.get(i).getPuertoCliente() != puerto)) {
            i++;
        }

        if (i < this.servidores.size())
            return this.servidores.get(i);
        else
            return null;
    }

    
    public void agregarObservador(IDirectorioListener observador)
    {
        this.observadores.add(observador);
    }
    
    private void notificarObservadores()
    {
        for(IDirectorioListener observador: observadores)
        {
            observador.actualizarEstado(servidores);
        }
    }
    
    private void notificarObservadoresListo()
    {
        for(IDirectorioListener observador: observadores)
        {
            observador.directorioListo();
        }  
    }
}
