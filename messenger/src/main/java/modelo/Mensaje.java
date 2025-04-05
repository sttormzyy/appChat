/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mensaje {
    private static int idCounter = 1;
    private final int id;
    private final String contenido;
    private final String fechaHora;
    private final boolean esMio;
    
    public Mensaje(String contenido, boolean esMio) {
        this.id = idCounter++;
        this.contenido = contenido;
        this.fechaHora = obtenerFechaHoraActual();
        this.esMio = esMio;
    }
    
    public int getId() {
        return id;
    }
    
    public String getContenido() {
        return contenido;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public boolean esMio() {
        return esMio;
    }
    
    private String obtenerFechaHoraActual() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }

}
