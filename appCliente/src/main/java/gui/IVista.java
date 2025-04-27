/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gui;


import gui.VentanaPrincipal.SideBar;


public interface IVista extends IVistaFormulario, IGestorConversacion, IGestorContacto{
   public void hacerVisible(boolean b); 
   public String getMensaje();
   public String getNicknameRealActivo();
   public void setNicknameUsuario(String nickname);
   public void setNicknameRealActivo(String nickname);
   public void setNicknameConversacionActiva(String nickname);
   public String getNicknameConversacionActiva();
   public void setSideBar(SideBar sideBar);
   public boolean isBarraDeMensajeClickeada();
}
