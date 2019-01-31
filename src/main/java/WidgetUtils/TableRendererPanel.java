/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WidgetUtils;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class TableRendererPanel extends JFrame {

    public TableRendererPanel() {
        JTable table = new JTable(5, 3);
        /*
         {
         public String getToolTipText( MouseEvent e )
         {
         System.out.println(e.getPoint());
         int row = rowAtPoint( e.getPoint() );
         int column = columnAtPoint( e.getPoint() );
 
         JPanel p = (JPanel)prepareRenderer(getDefaultRenderer(String.class), row, column);
 
         if (p.getComponentCount() > 1)
         {
         System.out.println("\t" + p.getComponent(0).getBounds());
         System.out.println("\t" + p.getComponent(1).getBounds());
         }
 
         return row + " : " + column;
         }
         };
         */
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane);

        table.setValueAt(new CellContents("Ragesh Song", "Ragesh", "00:02:30"), 0, 0);
//        table.setValueAt("12345", 1, 0);
        table.setDefaultRenderer(Object.class, new MultiLabelRenderer());
    }

    public static void main(String[] args) {
        TableRendererPanel frame = new TableRendererPanel();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    class MultiLabelRenderer implements TableCellRenderer {

        private JPanel panel;
        private JLabel red;
        private JLabel blue;

        public MultiLabelRenderer() {
            panel = new JPanel(new BorderLayout());
//          panel = new JPanel();
            red = new JLabel();
            red.setForeground(Color.RED);
            blue = new JLabel();
            blue.setForeground(Color.BLUE);
        }

        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, final int row, final int column) {
            
            panel.removeAll();

            if (isSelected) {
                panel.setBackground(table.getSelectionBackground());
            } else {
                panel.setBackground(table.getBackground());
            }

            if (value == null
                    || value.toString().length() == 0) {
                return panel;
            }
            

            CellContents text = (CellContents)value;

            red.setText(text.song);
            blue.setText(text.artist);

            int columnWidth = table.getColumnModel().getColumn(column).getWidth();
            int redWidth = red.getPreferredSize().width;

                panel.add(red, BorderLayout.WEST);
                panel.add(blue);

            return panel;
        }
    }
    
    class CellContents {
        String song;
        String artist;
        String time;

        public CellContents(String song, String artist, String time) {
            this.song = song;
            this.artist = artist;
            this.time = time;
        }
    }
}
