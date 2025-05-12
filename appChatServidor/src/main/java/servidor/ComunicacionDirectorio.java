/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor;

/**
 *
 * @author Usuario
 */
public class ComunicacionDirectorio {
    private Sincronizador sincronizador;
    private String IPDirectorio;
    private int puertoDirecotorio;

    public ComunicacionDirectorio(InfoServidor servidor,String IPDirectorio, int puertoDirecotorio,Sincronizador sincronizador) {
        this.IPDirectorio = IPDirectorio;
        this.puertoDirecotorio = puertoDirecotorio;
        registrarServidor(InfoServidor);
    }
    
    registrarServidor(InfoServidor s)
    {
        se conecta al socket de directorio
        out.(REGISTRAR_SERVIDOR)
        le pasa la info del servidor
    }
    
    avisarDirectorioEstoyListo()
    {
        se conecta al socket
        out.(READY)
    }
    
    public void avisarUsuarioDesconectado()
    {
        abre socket y le manda BAJA_DE_USUARIO al directorio
    }
     
    
    run()
    {
        while(true)
            {
                lee del socket un comando
                switch(comando){
                    case AÑADIR_SERVIDOR
                            sincronizador.agregarServidor();
                    case ELIMINAR_SERVIDOR
                            sincronizador.eliminarServidor();          
                    case PING
                            out.(ECHO);
                    case SINCRONIZACION_TOTAL
                            sincronizador.sincronizacionTotal(el servidor al q hay q sincronizar)
                    }
                }
     }
    
}
