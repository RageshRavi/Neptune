/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptune.util;

import WidgetUtils.OndemandPlaylistPanel;
import com.neptune.common.DataHolder;
import com.neptune.common.OdpEditEnum;
import com.neptune.player.StreamPlayer;
import com.neptune.schema.Odpmedia;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.codehaus.plexus.util.FileUtils;

/**
 *
 * @author ragesh.raveendran
 */
public class MouseClickEventMethods {

    public static void odpPlayListButtonMouseClickeEvent(MouseEvent evt) {
        try {
            if (!DataHolder.odpFile.exists()) {
                FileUtils.copyFile(DataHolder.odpPrimaryBackupFile, DataHolder.odpFile);
            }
            if (!DataHolder.odpFile.exists()) {
                JOptionPane.showMessageDialog(null, "No saved ODP", "Alert", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            DataHolder.odpMedia = (Odpmedia) HelperUtility.unMarshall(Odpmedia.class, DataHolder.odpFile);
            if (null == DataHolder.odpMedia.getPlaylist() || DataHolder.odpMedia.getPlaylist().size() < 1) {
                JOptionPane.showMessageDialog(null, "ODP Playlist is empty now", "Alert", JOptionPane.OK_OPTION);
                DataHolder.odpModeStatus = OdpEditEnum.OFFLINE.toString();
                DataHolder.odpBuildButton.setEnabled(true);
                DataHolder.odpSaveButton.setEnabled(false);
                DataHolder.odpEditButton.setEnabled(false);
                DataHolder.odpDeleteButton.setEnabled(false);
                HelperUtility.cleanOdpPlaylist();
                HelperUtility.getOnDemandTrackListFromUpdatedMediaUpgraded(false);
                return;
            }
            DataHolder.odpModeStatus = OdpEditEnum.LIST.toString();
            DataHolder.odpBuildButton.setEnabled(false);
            DataHolder.odpSaveButton.setEnabled(false);
            DataHolder.odpEditButton.setEnabled(true);
            DataHolder.odpDeleteButton.setEnabled(true);

            StreamPlayer.fadeOut();
            DataHolder.currenttrack = null;
            DataHolder.nextTrack = null;
            DataHolder.currentTrackPath = null;
            DataHolder.odpCurrentTrackId = 0L;
            DataHolder.isOdpPlaylistPlaying = false;
            DataHolder.currentOdpPlaylist = "";

            DataHolder.onDemandTrackList.clear();
            DefaultTableModel playListDataModel = (DefaultTableModel) DataHolder.playList.getModel();
//          clear playlist, remove all the ondemand tracks from ondemand playlist.
            for (int row = 0; row < playListDataModel.getRowCount(); row++) {
                for (int column = 0; column < playListDataModel.getColumnCount(); column++) {
                    if (playListDataModel.getValueAt(row, column) != null) {
                        OndemandPlaylistPanel panel = (OndemandPlaylistPanel) playListDataModel.getValueAt(row, column);
                        if (!HelperUtility.ondemandContainsTrack(panel)) {
                            playListDataModel.setValueAt(null, row, column);
                            if (null != DataHolder.onDemandTrackListDisplayed && null != panel) {
                                DataHolder.onDemandTrackListDisplayed.remove(panel.getSongTitleLabel().getName());
                            }
                        }
                    }
                }
            }

//          List all the saved ODP in ondemand playlist.
            DataHolder.onDemandPlayListLastRow = 0;
            DataHolder.onDemandPlayListLastColumn = -1;
            for (com.neptune.schema.Odpmedia.Playlist playlist : DataHolder.odpMedia.getPlaylist()) {
                DataHolder.onDemandPlayListLastRow = DataHolder.onDemandPlayListLastColumn == 3 ? DataHolder.onDemandPlayListLastRow + 1 : DataHolder.onDemandPlayListLastRow;
                DataHolder.onDemandPlayListLastColumn = DataHolder.onDemandPlayListLastColumn == 3 ? 0 : DataHolder.onDemandPlayListLastColumn + 1;
                if (playListDataModel.getRowCount() == DataHolder.onDemandPlayListLastRow) {
                    playListDataModel.addRow(new Object[]{});
                }
                playListDataModel.setValueAt(new OndemandPlaylistPanel(playlist),
                        DataHolder.onDemandPlayListLastRow, DataHolder.onDemandPlayListLastColumn);
            }
        } catch (Exception e) {
            NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "No saved ODP", "Alert", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void odpPlaylistMouseClickEvent(MouseEvent evt) {
        switch (OdpEditEnum.valueOf(DataHolder.odpModeStatus.toUpperCase())) {
            case EDIT:
            case BUILD:
                if(!"".equalsIgnoreCase((String) DataHolder.odpPlayList.getValueAt(DataHolder.odpPlayList.getSelectedRow(), DataHolder.odpPlayList.getSelectedColumn()))){
                    String[] choices = {"Remove Song", "Replace Song", "Cancel"};
                while (true) {
                    int response = JOptionPane.showOptionDialog(
                            null // Center in window.
                            , "What do you want to do?" // Message
                            , "" // Title in titlebar
                            , JOptionPane.YES_NO_OPTION // Option type
                            , JOptionPane.PLAIN_MESSAGE // messageType
                            , null // Icon (none)
                            , choices // Button text as above.
                            , "DEFAULT" // Default button's label
                    );

                    //... Use a switch statement to check which button was clicked.
                    switch (response) {
//=====================================R E M O V E=================================================================================                        
                        case 0:
                            for (Odpmedia.Playlist playlist : DataHolder.odpMedia.playlist) {
                                if (playlist.getId().equalsIgnoreCase(DataHolder.currentOdpPlaylist)) {
//                                    List<Odpmedia.Playlist.Track> listOfPlayListTrack = new ArrayList(playlist.getTrack());
//                                    Collections.sort(listOfPlayListTrack, new OdpTrackComparator());
                                    Iterator iter = playlist.getTrack().iterator();
                                    if (null == DataHolder.odpTrackList) {
                                        DataHolder.odpTrackList = new LinkedList<Odpmedia.Playlist.Track>();
                                    } else {
                                        DataHolder.odpTrackList.clear();
                                    }
                                    while (iter.hasNext()) {
                                        Odpmedia.Playlist.Track track = (Odpmedia.Playlist.Track) iter.next();
                                        if (track.getLabel().equalsIgnoreCase((String) DataHolder.odpPlayList.getValueAt(DataHolder.odpPlayList.getSelectedRow(), DataHolder.odpPlayList.getSelectedColumn()))) {
                                            iter.remove();
                                            DataHolder.isOdpEditLogoRequired = false;
                                            HelperUtility.cleanOdpPlaylist();
                                            DataHolder.isOdpEditLogoRequired = true;
                                            for (Odpmedia.Playlist.Track track1 : playlist.getTrack()) {
                                                if (DataHolder.odpLastRow == DataHolder.odpPlayList.getRowCount()) {
                                                    DataHolder.odpPlaylistModel.addRow(new Object[]{});
                                                }
                                                HelperUtility.addOdpMaxtime(track1.getTime());
                                                DataHolder.odpPlaylistModel.setValueAt(track1.getLabel(), DataHolder.odpLastRow, 0);
                                                DataHolder.odpLastRow++;
                                            }
                                            break;
                                        }
                                    }
                                    DataHolder.odpTrackList.addAll(playlist.getTrack());
                                    System.out.println("Odp Track size : " + DataHolder.odpTrackList.size());

                                }
                            }
                            break;
//=====================================R E P L A C E=================================================================================                                
                        case 1:
                            DataHolder.odpEditEnumStatus = OdpEditEnum.REPLACE.getOdpEditEnum();
                            HelperUtility.getOnDemandTrackListFromUpdatedMediaUpgraded(false);
                            break;
                        case 2:

                            break;
                        case 3:
                        case -1:
                            System.exit(0);
                        default:
                            JOptionPane.showMessageDialog(null, "Unexpected response " + response);
                    }
                    break;
                }
                }
        }
    }

    public static void ondemandCategoriesMouseClicked(MouseEvent evt) {
        switch (OdpEditEnum.valueOf(DataHolder.odpModeStatus.toUpperCase())) {
            case LIST:
                DataHolder.odpEditButton.setEnabled(true);
                DataHolder.odpDeleteButton.setEnabled(true);
                DataHolder.odpBuildButton.setEnabled(false);
                DataHolder.odpSaveButton.setEnabled(false);
                break;
            case EDIT:
            case BUILD:
            default:
                if (DataHolder.isOdpPlaylistPlaying) {
                    StreamPlayer.stop();
                }
                DataHolder.isOdpPlaylistPlaying = false;
                if (null != DataHolder.ondemandCategories.getValueAt(DataHolder.ondemandCategories.getSelectedRow(),
                        DataHolder.ondemandCategories.getSelectedColumn())) {
                    DataHolder.onDemandOverride = true;
                    DataHolder.currenttrack = null;
                    DataHolder.nextTrack = null;
                    DataHolder.currentTrackPath = null;
                    DataHolder.nextTrackPath = null;
                    DataHolder.playListSelectedTrackPath = null;
                    DataHolder.onDemandTrackListDisplayed.clear();
                    DataHolder.ondemandCategoryName = DataHolder.ondemandCategories.getValueAt(
                            DataHolder.ondemandCategories.getSelectedRow(), DataHolder.ondemandCategories.getSelectedColumn()).toString();
                    HelperUtility.getOnDemandTrackListFromUpdatedMediaUpgraded(true);
                }
                break;
        }
    }
}
