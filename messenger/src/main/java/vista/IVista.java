/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package vista;

import java.util.ArrayList;
import modelo.Contacto;
import modelo.Conversacion;
import modelo.Mensaje;
import vista.VentanaPrincipal.SideBar;

/**
 *
 * @author Usuario
 */
public interface IVista {
   // metodos generales, setters y getters
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
   
   
   // metodos gestion mensajes y conversaciones
   public void agregarMensaje(String contenido, boolean esMio);
   public void agregarContacto(Contacto contacto);
   public void cargarContactos(ArrayList<Contacto> contactos);
   public void agregarConversacion(Conversacion conversacion);
   public void cargarConversaciones(ArrayList<Conversacion> conversaciones);
   public void mostrarConversacion(ArrayList<Mensaje> mensajes, String nickname, String ip, int puerto);
   public void notificar();
   
   //metodos formulario registro
   public void cerrarFormularioRegistro();
   public String getNicknameRegistro();
   public int getPuertoRegistro();
   
   //metodos formulario agregar contacto
   public void abrirFormularioAgregarContacto();
   public void cerrarFormularioAgregarContacto();  
   public String getNicknameContacto();
   public String getIPContacto();
   public int getPuertoContacto();
   
   //metodos formulario agregar conversacion
   public void abrirFormularioAgregarConversacion(ArrayList<Contacto> contactosSinConversacion);
   public void cerrarFormularioAgregarConversacion();
   public String getIPConversacion();
   public int getPuertoConversacion();
}
