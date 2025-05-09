/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.mensaje to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import resources.Constantes;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;


/**
 *
 * @author Usuario
 */
public class ContenedorMensajeVista extends javax.swing.JPanel {  
    private String msj;
    private MensajeVista mensaje;  
    private JPanel mensajePanel;
    private static final int ANCHO_MAX = 300; // Ancho fijo para el JTextArea

    public ContenedorMensajeVista(String mensaje,boolean esMio, String hora) {
        msj = mensaje;
        setBackground(Constantes.COLOR_BASE);
        // Crear un AutoResizeTextArea con el mensaje
        this.mensaje = new MensajeVista(msj, esMio, hora);  // Usamos la clase personalizada

        // Crear un panel para contener el JTextArea
        mensajePanel = new JPanel();
        mensajePanel.setBackground(Constantes.COLOR_BASE);
        mensajePanel.setLayout(new BoxLayout(mensajePanel, BoxLayout.Y_AXIS)); // Usar BoxLayout en el eje Y
        mensajePanel.add(this.mensaje); // Añadir el JTextArea al panel
        mensajePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Márgenes arriba y abajo (10px)

        // Configurar el panel principal con márgenes a la izquierda y esMio
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15)); // Márgenes izquierdo y derecho (15px)

        // Ajustar el tamaño del panel principal según el contenido del JTextArea
        int panelHeight = this.mensaje.getPreferredSize().height + 20;  // Margen adicional de 20px
        setPreferredSize(new Dimension(ANCHO_MAX + 30, panelHeight)); // Ajustar el ancho y alto del panel

        // Crear un contenedor para centrar el mensaje
        JPanel panelContenedor = new JPanel();
        panelContenedor.setBackground(new Color(47,52,52 ));
        panelContenedor.setLayout(new GridBagLayout()); // Usar GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelContenedor.add(mensajePanel, gbc);

        // Si "esMio" es true, se debe alinear el mensaje a la derecha
        if (esMio) {
            add(panelContenedor, BorderLayout.EAST); 
        } else {
            add(panelContenedor, BorderLayout.WEST); 
        }
        revalidate();
        repaint();
    }
  
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 102, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        setAlignmentX(0.0F);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
