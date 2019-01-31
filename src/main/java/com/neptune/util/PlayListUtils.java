/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptune.util;

import WidgetUtils.OndemandPlaylistPanel;
import com.neptune.common.DataHolder;
import com.neptune.common.OdpEditEnum;
import com.neptune.comparators.OdpTrackComparator;
import com.neptune.player.StreamPlayer;
import com.neptune.schema.Media;
import com.neptune.schema.Odpmedia;
import com.neptune.schema.Odpmedia.Playlist;
import java.awt.event.MouseEvent;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import javax.swing.JOptionPane;

/**
 *
 * @author ragesh.raveendran
 */
public class PlayListUtils {

    public static void playListMouseClickedWhenOnDemandOverrideIsOnInOdpListMode(MouseEvent evt) {
        if (!DataHolder.isOdpPlaylistPlaying) {
            remapOdpPlaylist();
        }
        if (null != evt) {
            if (evt.getClickCount() == 2) {
                remapOdpPlaylist();
                StreamPlayer.fadeOut();
                DataHolder.isOdpPlaylistPlaying = true;
                DataHolder.currentTrackPath = null;
                DataHolder.onDemandOverride = true;
                DataHolder.odpCurrentTrackId = 0L;
            }
        }
    }

    public static void remapOdpPlaylist() {
        DataHolder.currentTrackLabel.setText("Unavailable");
        HelperUtility.cleanOdpPlaylist();
        if (DataHolder.playList.getValueAt(DataHolder.playList.getSelectedRow(), DataHolder.playList.getSelectedColumn()) != null) {
            OndemandPlaylistPanel panel = (OndemandPlaylistPanel) DataHolder.playList.getValueAt(
                    DataHolder.playList.getSelectedRow(), DataHolder.playList.getSelectedColumn());
            DataHolder.currentOdpPlaylist = panel.getSongTitleLabel().getName();
            for (Odpmedia.Playlist playlist : DataHolder.odpMedia.playlist) {
                if (playlist.id.equalsIgnoreCase(panel.getSongTitleLabel().getName())) {
                    List<Odpmedia.Playlist.Track> listOfPlayListTracks = new ArrayList(playlist.getTrack());
                    Collections.sort(listOfPlayListTracks, new OdpTrackComparator());
                    if (null == DataHolder.odpMedia) {
                        HelperUtility.initializeODPMedia();
                    }
                    DataHolder.odpPlaylistModel = (javax.swing.table.DefaultTableModel) DataHolder.odpPlayList.getModel();
                    if (null == DataHolder.odpTrackList) {
                        DataHolder.odpTrackList = new LinkedList<Odpmedia.Playlist.Track>();
                    } else {
                        DataHolder.odpTrackList.clear();
                    }
                    for (Odpmedia.Playlist.Track track : listOfPlayListTracks) {
                        if (DataHolder.odpLastRow == DataHolder.odpPlayList.getRowCount()) {
                            DataHolder.odpPlaylistModel.addRow(new Object[]{});
                        }
                        DataHolder.odpTrackList.add(track);
                        HelperUtility.addOdpMaxtime(track.getTime());
                        DataHolder.odpPlaylistModel.setValueAt(track.getLabel(), DataHolder.odpLastRow, 0);
//                        DataHolder.odpPlaylistModel.setValueAt(new OdpPlaylistTableCellRenderer(track.getLabel()), DataHolder.odpLastRow, 0);
                        DataHolder.odpLastRow++;
                    }
                }
            }
        }
    }

    public static void playListMouseClickedInOdpEditModeStatus(MouseEvent evt) {
        switch (OdpEditEnum.valueOf(DataHolder.odpEditEnumStatus.toUpperCase())) {
//============================================================================================================================================================
            case REPLACE:
                OndemandPlaylistPanel panel = (OndemandPlaylistPanel) DataHolder.playList.getValueAt(
                        DataHolder.playList.getSelectedRow(), DataHolder.playList.getSelectedColumn());
                if (null != panel) {
                    DataHolder.isOdpEditLogoRequired = true;
                    for (Odpmedia.Playlist playlist : DataHolder.odpMedia.playlist) {
                        if (DataHolder.currentOdpPlaylist.equalsIgnoreCase(playlist.getId())) {
                            List<Odpmedia.Playlist.Track> listOfPlayListTrack = new ArrayList(playlist.getTrack());
//                            Collections.sort(listOfPlayListTrack, new OdpTrackComparator());
                            Iterator iter = listOfPlayListTrack.iterator();
                            while (iter.hasNext()) {
                                Odpmedia.Playlist.Track track = (Odpmedia.Playlist.Track) iter.next();
                                if (track.getLabel().equalsIgnoreCase(
                                        DataHolder.odpPlaylistModel.getValueAt(DataHolder.odpPlayList.getSelectedRow(),
                                                DataHolder.odpPlayList.getSelectedColumn()).toString())) {
                                    HelperUtility.addOdpMaxTimeWhenReplacingTrack(track.getTime(), panel.getPlayTimeLabel().getText());
                                    track.setLabel(panel.getSongTitleLabel().getText());
                                    track.setName(panel.getSongTitleLabel().getName());
                                    track.setTime(panel.getPlayTimeLabel().getText());
                                    break;
                                }
                            }
                        }
                    }
                    DataHolder.odpPlaylistModel.setValueAt(panel.getSongTitleLabel().getText(), DataHolder.odpPlayList.getSelectedRow(), DataHolder.odpPlayList.getSelectedColumn());
                }
                break;
//============================================================================================================================================================
            case BUILD:
                if (null == DataHolder.odpMedia) {
                    HelperUtility.initializeODPMedia();
                }
                DataHolder.odpPlaylistModel = (javax.swing.table.DefaultTableModel) DataHolder.odpPlayList.getModel();
                OndemandPlaylistPanel onDemandPlaylistPanel = (OndemandPlaylistPanel) DataHolder.playList.getValueAt(
                        DataHolder.playList.getSelectedRow(), DataHolder.playList.getSelectedColumn());
                if (null != onDemandPlaylistPanel) {
                    switch (HelperUtility.covertBooleanToInt(HelperUtility.addOdpMaxtime(onDemandPlaylistPanel.getPlayTimeLabel().getText()))) {
                        case 1:

                            for (Playlist playList : DataHolder.odpMedia.getPlaylist()) {
                                if (playList.getId().equalsIgnoreCase(DataHolder.currentOdpPlaylist)) {
                                    Collections.sort(new ArrayList(playList.getTrack()), new OdpTrackComparator());
                                    com.neptune.schema.Odpmedia.Playlist.Track track = new com.neptune.schema.Odpmedia.Playlist.Track();
                                    track.setName(onDemandPlaylistPanel.getSongTitleLabel().getName());
                                    track.setLabel(onDemandPlaylistPanel.getSongTitleLabel().getText());
                                    track.setId(Long.parseLong(String.valueOf(DataHolder.odpLastRow)));
                                    track.setTime(onDemandPlaylistPanel.getPlayTimeLabel().getText());

                                    if (!playList.getTrack().contains(track)) {
                                        if (DataHolder.odpLastRow == DataHolder.odpPlayList.getRowCount()) {
                                            DataHolder.odpPlaylistModel.addRow(new Object[]{});
                                        }
                                        playList.getTrack().add(track);
                                        DataHolder.odpTrackList.add(track);
                                        DataHolder.odpPlaylistModel.setValueAt(onDemandPlaylistPanel.getSongTitleLabel().getText(), DataHolder.odpLastRow, 0);
                                        DataHolder.odpLastRow++;
                                    }
                                }
                            }
                            break;
                        case 0:
                            JOptionPane.showMessageDialog(null, "Your On Demand Playlist is at maximum length. Please remove or replace content to continue.", "", JOptionPane.WARNING_MESSAGE);
                            break;
                    }
                }
                break;
//============================================================================================================================================================                
            case REMOVE:

                break;
//============================================================================================================================================================                
            case OFFLINE:
                playListMouseClicked(evt);
                if (evt.getClickCount() == 2) {
                    if (DataHolder.playListSelectedTrackPath != null && !DataHolder.playListSelectedTrackPath.isEmpty()) {
                        try {
                            if (DataHolder.playListSelectedTrackPath.equalsIgnoreCase(DataHolder.currentTrackPath) && DataHolder.isPlaying) {
                                return;
                            }
                            StreamPlayer.fadeOut();
                            if (DataHolder.playListSelectedTrackPath != null) {
                                if (!DataHolder.playListSelectedTrackPath.isEmpty()) {
                                    DataHolder.currentTrackPath = DataHolder.playListSelectedTrackPath;
                                    DataHolder.programmeLabel.setText(DataHolder.ondemandCategoryName);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            DataHolder.onDemandOverride = true;
                        }
                    }
                }
                break;
//============================================================================================================================================================                
        }
    }

    public static void playListMouseClicked(java.awt.event.MouseEvent evt) {
        if (DataHolder.playList.getValueAt(DataHolder.playList.getSelectedRow(), DataHolder.playList.getSelectedColumn()) != null) {
            OndemandPlaylistPanel panel = (OndemandPlaylistPanel) DataHolder.playList.getValueAt(
                    DataHolder.playList.getSelectedRow(), DataHolder.playList.getSelectedColumn());
            DataHolder.playListSelectedTrackPath = DataHolder.archiveFolderPath + FileSystems.getDefault().getSeparator()
                    + panel.getSongTitleLabel().getName();
            Media.Channels.Station.Programme.Playlist.Track track = new Media.Channels.Station.Programme.Playlist.Track();
            track.setName(panel.getSongTitleLabel().getName());
            DataHolder.currenttrack = track;
        }
    }

}
