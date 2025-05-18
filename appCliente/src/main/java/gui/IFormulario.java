/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gui;

import java.util.ArrayList;

/**
 *
 * @author vanci
 */
public interface IFormulario {
   public void abrirFormulario();
   public void abrirFormulario(ArrayList<String> contactos);
   public void cerrarFormulario(); 
   public String getNickname();
   public int getPuerto();
   public String getIP();
}
