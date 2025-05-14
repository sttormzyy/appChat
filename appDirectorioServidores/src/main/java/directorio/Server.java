/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package directorio;

/**
 *
 * @author Usuario
 */
public class Server {
    private String ip;
    private int puerto;
    private boolean estado;
    private int contador;

    public Server(String ip, int puerto) {
        this.ip = ip;
        this.puerto = puerto;
        estado = false;
        contador = 0;
    }

    public String getIp() {
        return ip;
    }

    public int getPuerto() {
        return puerto;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public int getContador() {
        return contador;
    }

    public void setContador(int contador) {
        this.contador = contador;
    }
    
    
}
