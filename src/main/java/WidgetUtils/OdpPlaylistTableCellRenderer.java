/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WidgetUtils;

import com.neptune.common.DataHolder;
import com.neptune.common.OdpEditEnum;
import java.awt.Component;
import java.awt.ComponentOrientation;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author ragesh.raveendran
 */
public class OdpPlaylistTableCellRenderer implements TableCellRenderer {

    public String songName;
    
    public OdpPlaylistTableCellRenderer(String songName) {
        this.songName = songName;
    }

    
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JPanel panel = new OdpPanelInEditMode();
        switch (OdpEditEnum.valueOf(DataHolder.odpModeStatus)) {
            case EDIT:
                DataHolder.odpImageEditLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/odpEdit.png")));
                break;
            default:
                DataHolder.odpImageEditLabel.setIcon(null);
                break;
        }
        if (isSelected) {
            panel.setBackground(new java.awt.Color(153, 204, 255));
            panel.setForeground(table.getSelectionForeground());
            panel.setBackground(table.getSelectionBackground());
        } else {
            panel.setBackground(table.getBackground());
            if (row % 2 == 0) {
                panel.setBackground(new java.awt.Color(192, 192, 192));
                panel.setBackground(new java.awt.Color(192, 192, 192));
            } else {
                panel.setBackground(new java.awt.Color(160, 160, 160));
                panel.setBackground(new java.awt.Color(160, 160, 160));
            }
        }

        if (value != null) {
            OdpPlaylistTableCellRenderer cellRenderer = (OdpPlaylistTableCellRenderer)value;
            
            if ("".equalsIgnoreCase(cellRenderer.songName)) {
                DataHolder.odpSongLabel.setText("");
                DataHolder.odpImageEditLabel.setIcon(null);
            } else {
                DataHolder.odpSongLabel.setText(cellRenderer.songName);
            }
        }
        DataHolder.odpSongLabel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        DataHolder.odpSongLabel.setHorizontalAlignment(SwingConstants.LEFT);
        return panel;
    }

}
