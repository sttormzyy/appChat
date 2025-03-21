/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package grupo10.messenger;

import javax.swing.JScrollBar;

/**
 *
 * @author Usuario
 */
public class ScrollBar extends JScrollBar
{
    public ScrollBar(){
    this.setUI(new ModernScrollBarUI());
    }
}
