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
public interface IVistaFormulario {
   public void abrirFormularioRegistro();
   public void cerrarFormularioRegistro();
   public void abrirFormularioAgregarConversacion(ArrayList<String> contactos);
   public void abrirFormularioAgregarContacto(ArrayList<String> contactos);
   public void abrirFormularioEditarContacto(String nickname);
   public void abrirFormularioPersistencia();
   public void cerrarFormularioAgregarConversacion();
   public void cerrarFormularioAgregarContacto();
   public void cerrarFormularioEditarContacto();
   public String getNicknameRegistro();
   public String getIPRegistro();
   public int getPuertoRegistro();
   public String getNicknameContacto();
   public String getNicknameContactoEditado();
   public String getNicknameContactoActual();
   public String getNicknameConversacion();
   public String getMetodoEncriptacion();
   public String getClaveEncriptacion();
}
