/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WidgetUtils;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author ragesh.raveendran
 */
public class CustomeJTable extends JFrame {

    public CustomeJTable() {
        super("Button Column Title");
    }

    public static void main(String args[]) {

    }
}

class ButtonRenderer extends JButton implements TableCellRenderer {

    public ButtonRenderer() {
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object obj, boolean selected, boolean hasFocus, int row, int column) {
        setText((obj == null) ? "" : obj.toString());
        return this;

    }

}

class ButtonEditor extends DefaultCellEditor {

    protected JButton btn;
    private String lbl;
    private Boolean clicked;

    public ButtonEditor(JTextField textField) {
        super(textField);
        btn = new JButton();
        btn.setOpaque(true);
        btn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        });

    }
    
//        @Override
//        public Component getTableCellEditorComponent(JTable table, Object obj, boolean selected, boolean hasFocus, int row, int column){
//            
//            return super.getTableCellEditorComponent(table, obj, selected, row, column);
//        }
    
}
