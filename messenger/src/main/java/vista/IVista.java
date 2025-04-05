/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package vista;


import vista.VentanaPrincipal.SideBar;


public interface IVista extends IVistaContacto,IVistaConversacion,IVistaRegistro,IGestorConversacion{
   public void hacerVisible(boolean b); 
   public String getMensaje();
   public String getIPactiva();
   public int getPuertoActivo();
   public void setIPactiva(String ip);
   public void setPuertoActivo(int puerto);
   public void enableBotonAgregarContacto();
   public void enableBotonAgregarConversacion();
   public void disableBotonAgregarContacto();
   public void disableBotonAgregarConversacion();
   public void setSideBar(SideBar sideBar);
   public boolean isBarraDeMensajeClickeada();
}
