/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptune.util;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
/**
 *
 * @author ragesh.raveendran
 */
public class MyCellRenderer extends JLabel implements ListCellRenderer {

//    final static ImageIcon longIcon = new ImageIcon("long.gif");
//    final static ImageIcon shortIcon = new ImageIcon("short.gif");

     // This is the only method defined by ListCellRenderer.
    // We just reconfigure the JLabel each time we're called.
    public Component getListCellRendererComponent(
            JList list, // the list
            Object value, // value to display
            int index, // cell index
            boolean isSelected, // is the cell selected
            boolean cellHasFocus) // does the cell have focus
    {
        String s = value.toString();
//        setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/playlist.png"))); 
        setText(s);
//        setIcon((s.length() > 10) ? longIcon : shortIcon);
//        if (isSelected) {
//            setBackground(list.getSelectionBackground());
//            setForeground(list.getSelectionForeground());
//        } else {
//            setBackground(list.getBackground());
//            setForeground(list.getForeground());
//        }
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setOpaque(false);
        return this;
    }
}
