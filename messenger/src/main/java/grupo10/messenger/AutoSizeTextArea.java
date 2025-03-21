/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package grupo10.messenger;

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
public class AutoSizeTextArea extends JTextArea{
        private static final int ANCHO_FIJO = 500; // Ancho máximo

    // Constructor que recibe el texto como parámetro
    public AutoSizeTextArea(String text, boolean derecha) {
        super(text);  // Llamamos al constructor de JTextArea con el texto inicial
        this.setWrapStyleWord(true);  // Habilitar ajuste de línea sin cortar las palabras
        this.setLineWrap(true);       // Habilitar el ajuste de línea
        this.setEditable(false);  // Hacemos que no sea editable
        this.setMargin(new Insets(5, 10, 5, 10));  // Márgenes internos
        this.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true)); // Borde redondeado
        this.setBackground(derecha ? Color.CYAN : Color.WHITE); // Fondo distinto según la alineación
        addTimestamp();  // Agregar la hora de envío
        adjustSize();  // Ajustamos el tamaño al texto recibido
    }

    // Método para agregar la hora de envío al final del texto
    private void addTimestamp() {
        // Obtener la hora actual en formato HH:mm
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String timestamp = sdf.format(new Date());
        
        // Obtener el texto actual y añadir la hora al final
        String textWithTimestamp = getText() + "\n" + timestamp;
        setText(textWithTimestamp);  // Actualizamos el texto en el JTextArea
    }

private void adjustSize() {
    // Obtener las métricas de la fuente actual
    FontMetrics metrics = getFontMetrics(getFont());

    // Definir el ancho máximo para el JTextArea
    int maxWidth = ANCHO_FIJO - 10;  // Consideramos un margen de 10 píxeles

    // Obtener el texto completo
    String text = getText();

    int currentLineWidth = 0;  // Ancho actual de la línea
    int lines = 1;  // Comenzamos con 1 línea por defecto

    // Iterar sobre el texto letra por letra
    for (int i = 0; i < text.length(); i++) {
        char c = text.charAt(i);  // Obtener el carácter actual
        int charWidth = metrics.charWidth(c);  // Medir el ancho de la letra

        // Si el ancho total excede el ancho máximo, saltamos a la siguiente línea
        if (currentLineWidth + charWidth > maxWidth) {
            lines++;  // Incrementamos el número de líneas
            currentLineWidth = charWidth;  // Reiniciamos el ancho de la línea con el nuevo carácter
        } else {
            currentLineWidth += charWidth;  // Sumamos el ancho de la letra a la línea actual
        }
    }

    // Obtener el alto necesario para las líneas
    int lineHeight = metrics.getHeight();  // Altura de la línea
    int textHeight = lineHeight * lines;  // Calcular el alto total según las líneas

    // Calcular el ancho necesario para el texto, respetando el límite de ANCHO_FIJO
    int textWidth = 0;
    for (String line : text.split("\n")) {
        textWidth = Math.max(textWidth, metrics.stringWidth(line));
    }

    // Limitar el ancho a ANCHO_FIJO
    textWidth = Math.min(textWidth, ANCHO_FIJO);

    // Establecer el tamaño del JTextArea con un tamaño adecuado
    setPreferredSize(new Dimension(textWidth + 10, textHeight + 20));  // Añadir un margen extra
    System.out.println("Number of lines: " + lines);  // Debugging: Imprimir el número de líneas
}
    
    
    /*
        private static final int ANCHO_FIJO = 500; // Ancho máximo

    // Constructor que recibe el texto como parámetro
    public AutoSizeTextArea(String text, boolean derecha) {
        super(text);  // Llamamos al constructor de JTextArea con el texto inicial
        this.setWrapStyleWord(true);  // Habilitar ajuste de línea sin cortar las palabras
        this.setLineWrap(true);       // Habilitar el ajuste de línea
        this.setEditable(false);  // Hacemos que no sea editable
        this.setMargin(new Insets(5, 10, 5, 10));  // Márgenes internos
        this.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true)); // Borde redondeado
        this.setBackground(derecha ? Color.CYAN : Color.WHITE); // Fondo distinto según la alineación
        addTimestamp();  // Agregar la hora de envío
        adjustSize();  // Ajustamos el tamaño al texto recibido
    }

    // Método para agregar la hora de envío al final del texto
    private void addTimestamp() {
        // Obtener la hora actual en formato HH:mm
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String timestamp = sdf.format(new Date());
        
        // Obtener el texto actual y añadir la hora al final
        String textWithTimestamp = getText() + "\n" + timestamp;
        setText(textWithTimestamp);  // Actualizamos el texto en el JTextArea
    }

    // Método para ajustar el tamaño basado en el texto
    private void adjustSize() {
        // Obtener las métricas de la fuente actual
        FontMetrics metrics = getFontMetrics(getFont());
        
        // Calcular la altura necesaria para el texto
        int lineHeight = metrics.getHeight(); // Altura por línea
        int lines = getLineCount();  // Número de líneas de texto
        int textHeight = lineHeight * lines; // Altura total necesaria para el texto

        // Calcular el ancho necesario para el texto, pero respetando el límite de 500 píxeles
        int textWidth = 0;
        for (String line : getText().split("\n")) {
            textWidth = Math.max(textWidth, metrics.stringWidth(line));  // Obtener el ancho de cada línea y tomar el más grande
        }

        // Limitar el ancho a un máximo de 500 píxeles
        textWidth = Math.min(textWidth, ANCHO_FIJO);

        // Si hay más de una línea, aumentamos la altura
        if (lines > 1) {
            textHeight += 10; // Agregar un margen extra si hay más de una línea
        }

        // Eliminar el tamaño máximo en altura, para que pueda seguir creciendo verticalmente
        setPreferredSize(new Dimension(textWidth + 10, textHeight + 10));  // Añadir un pequeño margen extra
    }

    @Override
    public Dimension getPreferredSize() {
        // Llamamos al método de superclase para obtener el tamaño preferido, pero asegurándonos de que el JTextArea se redimensione bien
        Dimension preferredSize = super.getPreferredSize();
        preferredSize.width = Math.min(preferredSize.width, ANCHO_FIJO); // Limitar el ancho
        return preferredSize;
    }
*/
}
