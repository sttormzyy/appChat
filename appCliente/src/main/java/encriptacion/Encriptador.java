/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package encriptacion;

import static resources.Constantes.*;

/**
 *
 * @author Usuario
 */
public class Encriptador {
    IEncriptacion strategy;
    String clave;
    
    public Encriptador(String metodoEncriptacion, String clave)
    {
        this.clave = clave;
        switch(metodoEncriptacion)
        {
            case AES:
                strategy = new AESEncriptacion(clave);
                break;
            case DES:
                strategy = new DESEncriptacion(clave);
                break;
            case XOR:
                strategy =  new XOREncriptacion(clave);
                break;
        }
    }
    
    public String encriptar(String contenido)
    {
        return strategy.encriptar(contenido);
    }
    
    public String desencriptar(String contenido, String tipo)
    {
        IEncriptacion encriptador = null;
        switch(tipo)
        {
            case AES:
                encriptador = new AESEncriptacion(clave);
                break;
            case DES:
                encriptador = new DESEncriptacion(clave);
                break;
            case XOR:
                encriptador =  new XOREncriptacion(clave);
                break;
        }
        return encriptador.desencriptar(contenido);
    }
}
