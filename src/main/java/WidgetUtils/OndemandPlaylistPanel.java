/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WidgetUtils;

import com.neptune.schema.Media;
import com.neptune.schema.Odpmedia;
import com.neptune.util.HelperUtility;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import static javax.swing.SwingConstants.LEFT;
import static javax.swing.SwingConstants.RIGHT;
import static javax.swing.SwingConstants.TOP;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author ragesh.raveendran
 */
public class OndemandPlaylistPanel extends javax.swing.JPanel implements TableCellRenderer {

    /**
     * Creates new form NewJPanel
     */
//    public OndemandPlaylistPanel(String songName, String artist, String playTime, int rowNumber) {
//        initComponentsManually(songName, artist, playTime, rowNumber);
////        initComponents();
//    }
    
    public OndemandPlaylistPanel(Media.Channels.Station.Programme.Playlist.Track track) {
        initComponentsManually(track);
    }
    
     public OndemandPlaylistPanel(Odpmedia.Playlist playlist) {
                setBackground(new java.awt.Color(102, 255, 255));
//        setToolTipText(songName != null ? songName : "No Track");
        songTitleLabel = new javax.swing.JLabel();
        artistNameLabel = new javax.swing.JLabel();
        playTimeLabel = new javax.swing.JLabel();

        songTitleLabel.setFont(new java.awt.Font("Arial", 1, 11));
        songTitleLabel.setText(playlist.getId());
        songTitleLabel.setName(playlist.getId());
        songTitleLabel.setMaximumSize(new Dimension(76, 16));
        songTitleLabel.setMinimumSize(new Dimension(76, 16));
        songTitleLabel.setPreferredSize(new Dimension(76, 16));
        songTitleLabel.setHorizontalAlignment(LEFT);
        songTitleLabel.setVerticalAlignment(TOP);

        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(songTitleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(artistNameLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                                        .addComponent(playTimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(songTitleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(playTimeLabel)
                                .addComponent(artistNameLabel))
                        .addContainerGap())
        );
    }
    
    public OndemandPlaylistPanel(Media.Channels.Station.Programme.Playlist playlist) {
                setBackground(new java.awt.Color(102, 255, 255));
//        setToolTipText(songName != null ? songName : "No Track");
        songTitleLabel = new javax.swing.JLabel();
        artistNameLabel = new javax.swing.JLabel();
        playTimeLabel = new javax.swing.JLabel();

        songTitleLabel.setFont(new java.awt.Font("Arial", 1, 11));
        songTitleLabel.setText(playlist.getId());
        songTitleLabel.setName(playlist.getId());
        songTitleLabel.setMaximumSize(new Dimension(76, 16));
        songTitleLabel.setMinimumSize(new Dimension(76, 16));
        songTitleLabel.setPreferredSize(new Dimension(76, 16));
        songTitleLabel.setHorizontalAlignment(LEFT);
        songTitleLabel.setVerticalAlignment(TOP);
//        songTitleLabel.setToolTipText(songName != null ? songName : "No Track");

        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(songTitleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(artistNameLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                                        .addComponent(playTimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(songTitleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(playTimeLabel)
                                .addComponent(artistNameLabel))
                        .addContainerGap())
        );
    }

    public OndemandPlaylistPanel() {
    }

//    private void initComponentsManually(String songName, String artist, String playTime, int rowNumber) {
    private void initComponentsManually(Media.Channels.Station.Programme.Playlist.Track track) {
        setBackground(new java.awt.Color(102, 255, 255));
//        setToolTipText(songName != null ? songName : "No Track");
        songTitleLabel = new javax.swing.JLabel();
        artistNameLabel = new javax.swing.JLabel();
        playTimeLabel = new javax.swing.JLabel();

        songTitleLabel.setFont(new java.awt.Font("Arial", 1, 11));
        songTitleLabel.setText(track.getOndemandlabel().equalsIgnoreCase("") ? track.getName() : track.getOndemandlabel());
        songTitleLabel.setName(track.getName());
        songTitleLabel.setMaximumSize(new Dimension(76, 16));
        songTitleLabel.setMinimumSize(new Dimension(76, 16));
        songTitleLabel.setPreferredSize(new Dimension(76, 16));
        songTitleLabel.setHorizontalAlignment(LEFT);
        songTitleLabel.setVerticalAlignment(TOP);
//        songTitleLabel.setToolTipText(songName != null ? songName : "No Track");

        artistNameLabel.setText(track.getArtist());
        artistNameLabel.setFont(new java.awt.Font("Arial", 0, 10));
        artistNameLabel.setMaximumSize(new Dimension(104, 14));
        artistNameLabel.setMinimumSize(new Dimension(104, 14));
        artistNameLabel.setPreferredSize(new Dimension(104, 14));
        artistNameLabel.setHorizontalAlignment(LEFT);
//        artistNameLabel.setToolTipText(songName != null ? songName : "No Track");

        playTimeLabel.setText(HelperUtility.getTruncatedLength(track.getLength()));
        playTimeLabel.setFont(new java.awt.Font("Arial", 0, 10));
        playTimeLabel.setHorizontalAlignment(RIGHT);
        playTimeLabel.setMaximumSize(new Dimension(22, 14));
        playTimeLabel.setMinimumSize(new Dimension(22, 14));
        playTimeLabel.setPreferredSize(new Dimension(22, 14));
//        playTimeLabel.setToolTipText(songName != null ? songName : "No Track");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(songTitleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(artistNameLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                                        .addComponent(playTimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(songTitleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(playTimeLabel)
                                .addComponent(artistNameLabel))
                        .addContainerGap())
        );
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        songTitleLabel = new javax.swing.JLabel();
        artistNameLabel = new javax.swing.JLabel();
        playTimeLabel = new javax.swing.JLabel();

        setBackground(new java.awt.Color(102, 255, 255));
        setToolTipText("HAHAHAHAHAHAH");

        songTitleLabel.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        songTitleLabel.setText("My Song Title");
        songTitleLabel.setName(""); // NOI18N

        artistNameLabel.setText("Artist name Displayed");

        playTimeLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        playTimeLabel.setText("Time");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(songTitleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(artistNameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                        .addComponent(playTimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(songTitleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(playTimeLabel)
                    .addComponent(artistNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel artistNameLabel;
    private javax.swing.JLabel playTimeLabel;
    private javax.swing.JLabel songTitleLabel;
    // End of variables declaration//GEN-END:variables

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JPanel panel = new JPanel();
        if (isSelected) {
            panel.setBackground(new java.awt.Color(153, 204, 255));
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(table.getBackground());
            if (row % 2 == 0) {
                setBackground(new java.awt.Color(192, 192, 192));
                panel.setBackground(new java.awt.Color(192, 192, 192));
            } else {
                setBackground(new java.awt.Color(160, 160, 160));
                panel.setBackground(new java.awt.Color(160, 160, 160));
            }
        }
        if (null != value) {
            OndemandPlaylistPanel ondemandPlaylistPanel = (OndemandPlaylistPanel) value;
            songTitleLabel = ondemandPlaylistPanel.songTitleLabel;
            artistNameLabel = ondemandPlaylistPanel.artistNameLabel;
            playTimeLabel = ondemandPlaylistPanel.playTimeLabel;

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(panel);
            panel.setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                            .addGap(22, 22, 22)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(songTitleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                            .addComponent(artistNameLabel)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                                            .addComponent(playTimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addContainerGap())
            );
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(songTitleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(playTimeLabel)
                                    .addComponent(artistNameLabel))
                            .addContainerGap())
            );
            return panel;
        }
        return this;
    }

    public JLabel getArtistNameLabel() {
        return artistNameLabel;
    }

    public void setArtistNameLabel(JLabel artistNameLabel) {
        this.artistNameLabel = artistNameLabel;
    }

    public JLabel getPlayTimeLabel() {
        return playTimeLabel;
    }

    public void setPlayTimeLabel(JLabel playTimeLabel) {
        this.playTimeLabel = playTimeLabel;
    }

    public JLabel getSongTitleLabel() {
        return songTitleLabel;
    }

    public void setSongTitleLabel(JLabel songTitleLabel) {
        this.songTitleLabel = songTitleLabel;
    }

}
