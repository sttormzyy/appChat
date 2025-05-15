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
    private ArrayList<InfoServidor> servidores;
    private Configuracion configuracion;
    private VentanaDirectorio ventana;
    private Monitor monitor;
    private ComunicacionClientes serverClientes;
 
    
    public Directorio(Configuracion vista){
        servidores = new ArrayList();
        this.configuracion = vista;
        this.configuracion.setActionListener(this);
    }
    
    public void setVista(Configuracion vista){
        this.configuracion = vista;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("INICIAR DIRECTORIO"))
        {
            String ipDirectorio = configuracion.getIpDirectorio();
            int puertoServidores = configuracion.getPuertoServidores();
            int puertoClientes = configuracion.getPuertoClientes();
            configuracion.dispose();
            
            this.monitor = new Monitor(this,puertoServidores);
            new Thread (monitor).start();
            
            this.serverClientes = new ComunicacionClientes(this,puertoClientes);
            new Thread (serverClientes).start();
            
            this.ventana = new VentanaDirectorio();
            ventana.setVisible(true);
        }
    }
    
    public InfoServidor getServidorConMenosCarga(){
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
        if (servidor != null)
            servidor.agregarUsuarioActivo();
        return servidor;
    }

    public ArrayList<InfoServidor> getServidores() {
        return this.servidores;
    }

    public synchronized void agregarServidor(String ip, int puertoClientes, int puertoSincro, int puertoMonitoreo) 
    {
        InfoServidor servidor;
        if(this.servidores.isEmpty())
            servidor = new InfoServidor(ip,puertoClientes, puertoClientes,puertoMonitoreo, true);
        else
            servidor = new InfoServidor(ip,puertoClientes, puertoClientes,puertoMonitoreo, false);
        this.servidores.add(servidor);
        this.ventana.agregarServidor(servidor);
    }
    
    public synchronized void eliminarServidores(ArrayList<InfoServidor> servidores) {
        for (InfoServidor servidor: servidores)
            this.servidores.remove(servidor);
        
        this.ventana.actualizarVista(this.servidores);
    }
    
    public synchronized void disminuirContadorEnUno(String ip, int puerto) {
        InfoServidor servidor = buscarServidor(ip, puerto);
        servidor.eliminarUsuarioActivo();
    }
    
    public void servidorListo(String ip, int puerto){
        this.buscarServidor(ip, puerto).setEstado(true);
    }
    
    public InfoServidor buscarServidor(String ip, int puerto){
        int i=0;
        while(i<this.servidores.size() && !this.servidores.get(i).getIP().equalsIgnoreCase(ip) && this.servidores.get(i).getPuertoCliente()!= puerto){
         i++;    
        }
        if(i<this.servidores.size())
            return this.servidores.get(i);
        else
            return null;
    }
    
}
