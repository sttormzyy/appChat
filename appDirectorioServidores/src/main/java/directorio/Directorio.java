/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package directorio;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public class Directorio implements ActionListener{
/* 
    
    
    public void agregarServidor(s)
    {
    
       añade a la venetana el servidor
    }
        
    public void eliminarServidor(s)
    {
    
       elimina de la venetana el servidor
    }           
                
    */
    private ArrayList<Server> servidores;
    private IVistaDirectorio vista;
    private Monitor serverServidores;
    private ServerParaClientes serverClientes;
    private Monitor monitor;
    
    public Directorio(IVistaDirectorio vista){
        servidores = new ArrayList();
        this.vista = vista;
    }
    
    public void setVista(IVistaDirectorio vista){
        this.vista = vista;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("INICIAR_DIRECTORIO")){
            String ipDirectorio = vista.getIpDirectorio();
            int puertoServidores = vista.getPuertoServidores();
            int puertoClientes = vista.getPuertoClientes();
            
            this.serverServidores = new Monitor(this,puertoServidores);
            new Thread (serverServidores).start();
            
            this.serverClientes = new ServerParaClientes(this,puertoClientes);
            new Thread (serverClientes).start();
            
            // this.vista.cerrar();
            // this.vista.abrirVentanaDirectorio();
        }
    }
    
    public Server getServidorConMenosCarga(){
        Server servidor = null;
        int i = 0;
        int actMin = 99999;
        while(i<this.servidores.size()){
            if(actMin>this.servidores.get(i).getContador() && this.servidores.get(i).isEstado()){
                servidor = this.servidores.get(i);
                actMin = servidor.getContador();
            }
            i++;
        }
        if (servidor != null)
            servidor.setContador(servidor.getContador()+1);
        return servidor;
    }

    public ArrayList<Server> getServidores() {
        return this.servidores;
    }

    public synchronized void agregarServidor(String ip, int puerto) {
        Server server = new Server(ip,puerto);
        if(this.servidores.isEmpty())
            server.setEstado(true);
        this.servidores.add(server);
     // this.actualizarVista(this.servidores);
    }
    
    public synchronized void eliminarServidores(ArrayList<Server> servidores) {
        for (Server servidor: servidores)
            this.servidores.remove(servidor);
        
        // this.actualizarVista(this.servidores);
    }
    
    public synchronized void disminuirContadorEnUno(String ip, int puerto) {
        Server servidor = buscarServidor(ip, puerto);
        servidor.setContador(servidor.getContador() - 1);
    }
    
    public void servidorListo(String ip, int puerto){
        this.buscarServidor(ip, puerto).setEstado(true);
    }
    
    public Server buscarServidor(String ip, int puerto){
        int i=0;
        while(i<this.servidores.size() && !this.servidores.get(i).getIp().equalsIgnoreCase(ip) && this.servidores.get(i).getPuerto()!= puerto){
         i++;    
        }
        if(i<this.servidores.size())
            return this.servidores.get(i);
        else
            return null;
    }
    
}
