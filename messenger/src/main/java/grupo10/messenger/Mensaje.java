/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package grupo10.messenger;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Usuario
 */
public class Mensaje extends javax.swing.JPanel {
    
    private String msj;
    private AutoSizeTextArea txt;  // Usamos AutoSizeTextArea
    private JPanel txtPanel;
    private static final int ANCHO_FIJO = 300; // Ancho fijo para el JTextArea

    public Mensaje(boolean derecha, String mensaje) {
        msj = mensaje;

        // Crear un AutoResizeTextArea con el mensaje
        txt = new AutoSizeTextArea(msj, derecha);  // Usamos la clase personalizada

        // Crear un panel para contener el JTextArea
        txtPanel = new JPanel();
        txtPanel.setLayout(new BoxLayout(txtPanel, BoxLayout.Y_AXIS)); // Usar BoxLayout en el eje Y
        txtPanel.add(txt); // Añadir el JTextArea al panel
        txtPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Márgenes arriba y abajo (10px)

        // Configurar el panel principal con márgenes a la izquierda y derecha
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15)); // Márgenes izquierdo y derecho (15px)

        // Ajustar el tamaño del panel principal según el contenido del JTextArea
        int panelHeight = txt.getPreferredSize().height + 20;  // Margen adicional de 20px
        setPreferredSize(new Dimension(ANCHO_FIJO + 30, panelHeight)); // Ajustar el ancho y alto del panel

        // Crear un contenedor para centrar el mensaje
        JPanel panelContenedor = new JPanel();
        panelContenedor.setLayout(new GridBagLayout()); // Usar GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelContenedor.add(txtPanel, gbc);

        // Si "derecha" es true, se debe alinear el mensaje a la derecha
        if (derecha) {
            add(panelContenedor, BorderLayout.EAST); // Alineamos a la derecha
        } else {
            add(panelContenedor, BorderLayout.WEST); // Alineamos a la izquierda
        }

        // Revalidar y repintar el panel para aplicar los cambios
        revalidate();
        repaint();
    }
    
    
    /*
 private String msj;
    private AutoSizeTextArea txt;  // Usamos AutoResizeTextArea
    private JPanel txtPanel;
    private static final int ANCHO_FIJO = 300; // Ancho fijo para el JTextArea

    public Mensaje(boolean derecha, String mensaje) {
        msj = mensaje;

        // Crear un AutoResizeTextArea con el mensaje
        txt = new AutoSizeTextArea(msj,derecha);  // Usamos la clase personalizada

        // Crear un panel para contener el JTextArea
        txtPanel = new JPanel();
        txtPanel.setLayout(new BorderLayout()); // Usar BorderLayout
        txtPanel.add(txt, BorderLayout.CENTER); // Añadir el JTextArea al panel
        txtPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Márgenes arriba y abajo (10px)

        // Configurar el panel principal con márgenes a la izquierda y derecha
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15)); // Márgenes izquierdo y derecho (15px)

        // Ajustar el tamaño del panel principal según el contenido del JTextArea
        int panelHeight = txt.getPreferredSize().height + 20;  // Margen adicional de 20px
        setPreferredSize(new Dimension(ANCHO_FIJO + 30, panelHeight)); // Ajustar el ancho y alto del panel

        // Crear un contenedor para centrar el mensaje
        JPanel panelContenedor = new JPanel();
        panelContenedor.setLayout(new GridBagLayout()); // Usar GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelContenedor.add(txtPanel, gbc);

        // Si "derecha" es true, se debe alinear el mensaje a la derecha
        if (derecha) {
            add(panelContenedor, BorderLayout.EAST); // Alineamos a la derecha
        } else {
            add(panelContenedor, BorderLayout.WEST); // Alineamos a la izquierda
        }

        // Revalidar y repintar el panel para aplicar los cambios
        revalidate();
        repaint();
    }
    */
    /*
private String msj;
private JTextArea txt;
private JPanel txtPanel;
private static final int ANCHO_FIJO = 300; // Ancho fijo para el JTextArea

public Mensaje(boolean derecha, String mensaje) {
    
    
    
    msj = mensaje;

    // Crear un JTextArea y configurarlo
    txt = new JTextArea();
    txt.setText(msj);
    txt.setWrapStyleWord(true);  // Habilitar ajuste de línea sin cortar las palabras
    txt.setLineWrap(true);       // Habilitar el ajuste de línea
    txt.setFocusable(false);     // No debe recibir foco
    txt.setMargin(new Insets(5, 10, 5, 10)); // Márgenes internos
    txt.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true)); // Borde redondeado

    // Calcular cuántas líneas necesita el texto
    int numberOfLines = getNumberOfLines(msj, ANCHO_FIJO);

    // Calcular la altura necesaria para el JTextArea según el número de líneas
    int lineHeight = txt.getFontMetrics(txt.getFont()).getHeight();
    int textHeight = numberOfLines * lineHeight;
    System.out.println("n lines: "+numberOfLines);
    // Asegurar que tenga una altura mínima
    if (textHeight < 50) {
        textHeight = lineHeight+5;  // Altura mínima de 50px
    }

    // Establecer el tamaño preferido para el JTextArea
    txt.setPreferredSize(new Dimension(ANCHO_FIJO, textHeight));

    // Crear un panel para contener el JTextArea
    txtPanel = new JPanel();
    txtPanel.setLayout(new BorderLayout()); // Usar BorderLayout
    txtPanel.add(txt, BorderLayout.CENTER); // Añadir el JTextArea al panel
    txtPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Márgenes arriba y abajo (10px)

    // Configurar el panel principal con márgenes a la izquierda y derecha
    setLayout(new BorderLayout());
    setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15)); // Márgenes izquierdo y derecho (15px)

    // Ajustar el tamaño del panel principal según el contenido del JTextArea
    int panelHeight = textHeight + 20;  // Margen adicional de 20px
    setPreferredSize(new Dimension(ANCHO_FIJO + 30, panelHeight)); // Ajustar el ancho y alto del panel

    // Crear un contenedor para centrar el mensaje
    JPanel panelContenedor = new JPanel();
    panelContenedor.setLayout(new GridBagLayout()); // Usar GridBagLayout
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panelContenedor.add(txtPanel, gbc);

    // Si "derecha" es true, se debe alinear el mensaje a la derecha
    if (derecha) {
        add(panelContenedor, BorderLayout.EAST); // Alineamos a la derecha
    } else {
        add(panelContenedor, BorderLayout.WEST); // Alineamos a la izquierda
    }

    // Revalidar y repintar el panel para aplicar los cambios
    revalidate();
    repaint();
}

// Método para calcular el número de líneas necesarias según el texto y el ancho máximo
private int getNumberOfLines(String text, int maxWidth) {
    FontMetrics metrics = txt.getFontMetrics(txt.getFont());
    int lineHeight = metrics.getHeight();
    int numberOfLines = 0;

    // Dividimos el texto en líneas separadas por salto de línea
    String[] lines = text.split("\n");

    for (String lineText : lines) {
        // Dividimos cada línea en palabras
        String[] words = lineText.split(" ");
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            // Si añadir la palabra excede el ancho máximo, se agrega una nueva línea
            if (metrics.stringWidth(line.toString() + word) > maxWidth) {
                numberOfLines++;  // Contamos la línea anterior
                line.setLength(0);  // Limpiar la línea actual
            }
            line.append(word).append(" ");  // Agregar la palabra a la línea
        }

        // Al finalizar la línea, contamos la última línea
        if (line.length() > 0) {
            numberOfLines++;
        }
    }

    return numberOfLines;
}
    */
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
