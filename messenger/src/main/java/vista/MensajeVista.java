/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;

/**
 *
 * @author Usuario
 */
public class MensajeVista extends JTextArea{
    private static final int ANCHO_MAX = 500;

    public MensajeVista(String text, boolean esMio) {
        super(text);  
        this.setWrapStyleWord(true);  
        this.setLineWrap(true);       
        this.setEditable(false);  
        this.setMargin(new Insets(5, 10, 5, 10));  
        this.setBorder(BorderFactory.createLineBorder(Colores.COLOR_BASE, 2, true)); 
        this.setBackground(esMio ? Colores.COLOR_MENSAJE : Color.WHITE); 
        ponerFecha();  
        ajustarTamaño();  
    }

    private void ponerFecha() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String timestamp = sdf.format(new Date());
        String textWithTimestamp = getText() + "\n" + timestamp;
        setText(textWithTimestamp);  
    }

private void ajustarTamaño() {
    FontMetrics metrics = getFontMetrics(getFont());

    int maxWidth = ANCHO_MAX - 10;  // margen de 10 píxeles

    String text = getText();

    int currentLineWidth = 0;
    int lines = 1; 

    String[] lineas = text.split("\n");

    for (String line : lineas) {
        int lineWidth = metrics.stringWidth(line);
        if (lineWidth > maxWidth) {
            int currentLineWidthForThisLine = 0;
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                int charWidth = metrics.charWidth(c);

                if (currentLineWidthForThisLine + charWidth > maxWidth) {
                    lines++; 
                    currentLineWidthForThisLine = charWidth; 
                } else {
                    currentLineWidthForThisLine += charWidth;
                }
            }
        } else {
            lines++;
        }
    }

    int lineHeight = metrics.getHeight();  // Altura de la línea
    int textHeight = lineHeight * (lines);  // Calcular el alto total según las líneas

    int textWidth = 0;
    for (String line : lineas) {
        textWidth = Math.max(textWidth, metrics.stringWidth(line));
    }
    textWidth = Math.min(textWidth, ANCHO_MAX);

    setPreferredSize(new Dimension(textWidth + 10, textHeight - 5));
}
}
