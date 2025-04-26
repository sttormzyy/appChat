/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package vista;


import vista.VentanaPrincipal.SideBar;


public interface IVista extends IVistaFormulario, IGestorConversacion{
   public void hacerVisible(boolean b); 
   public String getMensaje();
   public String getNicknameActivo();
   public void setNicknameUsuario(String nickname);
   public void setNicknameActivo(String nickname);
   public void enableBotonAgregarContacto();
   public void enableBotonAgregarConversacion();
   public void disableBotonAgregarContacto();
   public void disableBotonAgregarConversacion();
   public void setSideBar(SideBar sideBar);
   public boolean isBarraDeMensajeClickeada();
}
