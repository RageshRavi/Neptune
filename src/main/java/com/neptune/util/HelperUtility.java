/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptune.util;

import WidgetUtils.OndemandPlaylistPanel;
import com.neptune.common.DataHolder;
import static com.neptune.common.DataHolder.playList;
import static com.neptune.common.DataHolder.playListScrollPanel;
import com.neptune.schema.Media;
import com.neptune.schema.Media.Channels.Station;
import com.neptune.schema.Media.Channels.Station.Programme;
import com.neptune.schema.Media.Channels.Station.Programme.Playlist.Track;
import com.neptune.schema.Odpmedia;
import static com.neptune.util.DateUtil.DATE_FORMAT_HH_MM_SS;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author ragesh.raveendran
 */
public class HelperUtility {

    private static HelperUtility helperUtility = new HelperUtility();

    public static String getMACAddress() throws SocketException {
        StringBuilder sb = new StringBuilder("");
        try {
            if (System.getProperty("os.name").toLowerCase().contains("window")) {
                InetAddress ip = InetAddress.getLocalHost();
                NetworkInterface network = NetworkInterface.getByInetAddress(ip);
                if (null != network) {
                    byte[] mac = network.getHardwareAddress();
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                }
            } else {
                Process proc = Runtime.getRuntime().exec("ifconfig");
                InputStream in = proc.getInputStream();
                InputStreamReader reader_in = new InputStreamReader(in);
                BufferedReader buffered_reader_in = new BufferedReader(reader_in);
                String inStr;
                while ((inStr = buffered_reader_in.readLine()) != null) {
                    if (inStr.toLowerCase().contains("HWaddr".toLowerCase())) {
                        sb.append(inStr.trim().subSequence(inStr.trim().length() - 17, inStr.trim().length()));
                    }
                }
            }
        } catch (Exception ex) {
            NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
        }
        return sb.toString();
    }

    public void getPartnerLogo() {
        NeptuneLogger.getInstance().getLogger().log(Level.INFO, "Logo downloading...!!!!");
//        File logoFile = new File(DataHolder.archiveFolderPath + FileSystems.getDefault().getSeparator() + "clogo.jpg");
        File logoFile = new File(DataHolder.archiveFolderPath + FileSystems.getDefault().getSeparator() + "clogo3.png");
        if (logoFile.exists()) {
            NeptuneLogger.getInstance().getLogger().log(Level.INFO, "File exists...!!!");
            ImageIcon icon = new ImageIcon(logoFile.getAbsolutePath());
            icon.getImage().flush();
            DataHolder.imageHolderPanelLabel.setIcon(icon);
        } else {
            NeptuneLogger.getInstance().getLogger().log(Level.INFO, "File doesnot exists...!!!");
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/clogo3.png"));
            icon.getImage().flush();
            DataHolder.imageHolderPanelLabel.setIcon(icon);
        }
        try {
            StringBuilder url = new StringBuilder(DataHolder.httpURL + "/Neptune/sync/getlogo.ashx?UserName="
                    + DataHolder.configProperties.getProperty("userName").replace(" ", "%20")
                    + "&SystemName=" + DataHolder.configProperties.getProperty("SystemName").replace(" ", "%20")
                    + "&DeviceToken=" + DataHolder.configProperties.getProperty("DeviceToken").replace(" ", "%20"));
            if (!"".equalsIgnoreCase(DataHolder.macAddress)) {
                url.append("&MacId=").append(DataHolder.macAddress);
            }
            NeptuneLogger.getInstance().getLogger().log(Level.INFO, "URL : {0}", url);

            URL obj = new URL(url.toString());
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setConnectTimeout(100000);
            con.setReadTimeout(15000);
            int responseCode = con.getResponseCode();
            String contentType = con.getContentType();
            int contentLength = con.getContentLength();
            if (contentType.startsWith("text/") || contentLength == -1) {
                throw new IOException("This is not a binary file.");
            }
            InputStream raw = con.getInputStream();
            InputStream in = new BufferedInputStream(raw);
            byte[] data = new byte[contentLength];
            int bytesRead = 0;
            int offset = 0;

            while (offset < contentLength) {
                bytesRead = in.read(data, offset, data.length - offset);
                if (bytesRead == -1) {
                    break;
                }
                offset += bytesRead;
            }
            in.close();
            if (offset != contentLength) {
                throw new IOException("Only read " + offset + " bytes; Expected " + contentLength + " bytes");
            }
            if (!logoFile.getParentFile().exists()) {
                logoFile.getParentFile().mkdirs();
            }
            if (!logoFile.exists()) {
                logoFile.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(logoFile);
            out.write(data);
            out.flush();
            out.close();
            if (logoFile.exists()) {
                System.out.println("File exists...!!!");
                ImageIcon icon = new ImageIcon(logoFile.getAbsolutePath());
                icon.getImage().flush();
                DataHolder.imageHolderPanelLabel.setIcon(icon);
            }
        } catch (MalformedURLException ex) {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/clogo3.png"));
            icon.getImage().flush();
            DataHolder.imageHolderPanelLabel.setIcon(icon);
            Logger.getLogger(HelperUtility.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/clogo3.png"));
            icon.getImage().flush();
            DataHolder.imageHolderPanelLabel.setIcon(icon);
            Logger.getLogger(HelperUtility.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/clogo3.png"));
            icon.getImage().flush();
            DataHolder.imageHolderPanelLabel.setIcon(icon);
            Logger.getLogger(HelperUtility.class.getName()).log(Level.SEVERE, null, e);
        }
        NeptuneLogger.getInstance().getLogger().log(Level.INFO, "Finished downloading...{0}", "clogo3.png");
        if (logoFile.exists()) {
            System.out.println("File exists...!!!");
            ImageIcon icon = new ImageIcon(logoFile.getAbsolutePath());
            icon.getImage().flush();
            DataHolder.imageHolderPanelLabel.setIcon(icon);
        } else {
            System.out.println("File doesnot exists...!!!");
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/clogo3.png"));
            icon.getImage().flush();
            DataHolder.imageHolderPanelLabel.setIcon(icon);
        }
    }

    public static String downloadUpdateFile() {
        try {
            DataHolder.updateLogo.setVisible(false);
            StringBuilder updateUrl = new StringBuilder(DataHolder.httpURL + "/neptune/sync/GetPlayerVersion.ashx?UserName="
                    + DataHolder.configProperties.getProperty("userName").replace(" ", "%20")
                    + "&DeviceToken=" + DataHolder.configProperties.getProperty("DeviceToken").replace(" ", "%20"));
            if (!"".equalsIgnoreCase(DataHolder.macAddress)) {
                updateUrl.append("&MacId=").append(DataHolder.macAddress);
            }
            String updateSTring = helperUtility.getUserMediaUpdates(updateUrl.toString());
            String url = DataHolder.httpURL + "/Neptune/apps/" + updateSTring.split(";")[1];
            NeptuneLogger.getInstance().getLogger().log(Level.INFO, "URL : {0}", url);

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setConnectTimeout(100000);
            con.setReadTimeout(15000);
            String contentType = con.getContentType();
            int contentLength = con.getContentLength();
            if (contentType.startsWith("text/") || contentLength == -1) {
                throw new IOException("This is not a binary file.");
            }
            InputStream raw = con.getInputStream();
            InputStream in = new BufferedInputStream(raw);
            byte[] data = new byte[contentLength];
            int bytesRead = 0;
            int offset = 0;

            while (offset < contentLength) {
                bytesRead = in.read(data, offset, data.length - offset);
                if (bytesRead == -1) {
                    break;
                }
                offset += bytesRead;
            }
            in.close();
            if (offset != contentLength) {
                throw new IOException("Only read " + offset + " bytes; Expected " + contentLength + " bytes");
            }

            File updateScriptFile = new File(DataHolder.executionFolderPath + FileSystems.getDefault().getSeparator() + updateSTring.split(";")[1]);
            if (!updateScriptFile.exists()) {
                updateScriptFile.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(updateScriptFile);
            out.write(data);
            out.flush();
            out.close();
            updateScriptFile.setExecutable(true);
            NeptuneLogger.getInstance().getLogger().log(Level.INFO, "Finished downloading...{0}", updateScriptFile.getName());

            //sending updates to server
            StringBuilder sendUpdateResult = new StringBuilder(DataHolder.httpURL + "/neptune/sync/UpdatePlayerInfo.ashx?UserName="
                    + DataHolder.configProperties.getProperty("userName").replace(" ", "%20") + "&DeviceToken="
                    + DataHolder.configProperties.getProperty("DeviceToken").replace(" ", "%20") + "&PlayerID=" + updateSTring.split(";")[0]);
            if (!"".equalsIgnoreCase(DataHolder.macAddress)) {
                sendUpdateResult.append("&MacId=" + DataHolder.macAddress);
            }
            obj = new URL(sendUpdateResult.toString());
            con = (HttpURLConnection) obj.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.getResponseCode();
            BufferedReader bin = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            File contentSynFile = new File(DataHolder.contentSyncFile);
            if (!contentSynFile.exists()) {
                contentSynFile.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(contentSynFile);
            StringBuilder response = new StringBuilder();
            while ((inputLine = bin.readLine()) != null) {
                response.append(inputLine);
                fos.write(inputLine.getBytes());
            }
            if (response.toString().equalsIgnoreCase("FAILURE")) {
                return null;
            } else if (response.toString().equalsIgnoreCase("SUCCESS")) {
                return updateSTring;
            }
            return updateSTring;
        } catch (Exception e) {
            DataHolder.updateLogo.setVisible(true);
            NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, "Exception while updating player ", e);

        }
        return null;
    }

    public static void cleanOdpPlaylist() {
        for (int i = 0; i < DataHolder.odpLastRow; i++) {
            DataHolder.odpPlaylistModel.setValueAt("", i, 0);
        }
        DataHolder.odpLastRow = 0;
        if (null == DataHolder.odpPlaylistModel) {
            DataHolder.odpPlaylistModel = (DefaultTableModel) DataHolder.odpPlayList.getModel();
        }
//        DataHolder.odpPlaylistModel.setRowCount(6);
        DataHolder.odpMaxTime.setText("00:00");
    }

    public String getUserMediaUpdates(String url) {

        NeptuneLogger.getInstance().getLogger().log(Level.INFO, "Registration process, url - {0}", url);
        StringBuilder response = new StringBuilder();
        BufferedReader in;

        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            File contentSynFile = new File(DataHolder.contentSyncFile);
            if (!contentSynFile.exists()) {
                contentSynFile.createNewFile();
            }

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        } catch (MalformedURLException ex) {
            NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
        } finally {
            in = null;
        }
        return null;
    }

    public List<Track> getTracksLisFromUpdatedMedia() {
        List<Track> tracks = new ArrayList<Track>();
        try {
            if (null == DataHolder.media) {
                NeptuneLogger.getInstance().getLogger().info("Updated Media is NULL");
                return tracks;
            }
            for (Station station : DataHolder.media.getChannels().getStation()) {
                for (Programme programme : station.getProgramme()) {
                    if (programme != null) {
                        if (programme.getPlaylist() != null) {
                            if (programme.getPlaylist().getTrack() != null) {
                                tracks.addAll(programme.getPlaylist().getTrack());
                            }
                        }
                    }
                    //todo change the logic based on xml
                    for (Media.Channels.Station.Schedule.Campaign campaign : station.getSchedule().getCampaign()) {
                        for (Media.Channels.Station.Schedule.Campaign.Track campaignTrack : campaign.getTrack()) {
                            Track track = new Track();
                            track.setId(Long.valueOf(campaignTrack.getId()));
                            track.setName(campaignTrack.getName());
//                            track.setStarttime(DateUtil.getInstance().formatDate(campaignTrack.getStarttime(), DateUtil.getInstance().DATE_FORMAT_HH_MM_SS));
                            tracks.add(track);
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tracks;
    }

    public void getOnDemandTrackListFromUpdatedMedia() {
        System.out.println("getOnDemandTrackListFromUodatedMedia");
        for (Media.Channels.Station station : DataHolder.media.getChannels().getStation()) {
            if (station.getName().equalsIgnoreCase("OnDemandStation")) {
                NeptuneLogger.getInstance().getLogger().log(Level.INFO, "DataHolder.currentTrackNumber : {0}", DataHolder.currentTrackNumber);
                for (Programme programme : station.getProgramme()) {
                    if (programme.getPlaylist().getTrack().size() > 0) {

//                    javax.swing.table.DefaultTableModel tableModel = new javax.swing.table.DefaultTableModel(0, 0);
                        if (!DataHolder.playListScrollPanel.isVisible()) {
                            javax.swing.table.DefaultTableModel tableModel = new javax.swing.table.DefaultTableModel(0, 0) {
                                @Override
                                public boolean isCellEditable(int row, int column) {
                                    return false;
                                }
                            };
                            String header[] = new String[]{"Track", "Length"};
                            tableModel.setColumnIdentifiers(header);
                            DataHolder.playList.setModel(tableModel);
                            Double width = DataHolder.playListScrollPanel.getWidth() * (.75);
                            DataHolder.playList.getColumnModel().getColumn(0).setMinWidth(width.intValue());
//                    DataHolder.playList.getColumnModel().getColumn(0).setPreferredWidth(90);
//                    DataHolder.playList.getColumnModel().getColumn(1).setPreferredWidth(10);
//                    javax.swing.table.DefaultTableCellRenderer rightRenderer = new javax.swing.table.DefaultTableCellRenderer();
//                    javax.swing.table.DefaultTableCellRenderer leftRenderer = new javax.swing.table.DefaultTableCellRenderer();
//                    leftRenderer.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
//                    rightRenderer.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
//                    leftRenderer.setOpaque(false);
//                    rightRenderer.setOpaque(false);
//                    DataHolder.playList.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
//                    DataHolder.playList.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);

                            DataHolder.onDemandTrackList.clear();
                            for (Track track : programme.getPlaylist().getTrack()) {
                                DataHolder.onDemandTrackList.add(track);
                                tableModel.addRow(new Object[]{track.getName(),
                                    HelperUtility.getTruncatedLength(track.getLength())});
                            }
                        } else {
                            //update removed tracks
                            javax.swing.table.DefaultTableModel tableModel = (javax.swing.table.DefaultTableModel) DataHolder.playList.getModel();
                            DataHolder.onDemandTrackList.clear();
                            DataHolder.onDemandTrackList.addAll(programme.getPlaylist().getTrack());
                            for (int i = 0; i < DataHolder.playList.getRowCount(); i++) {
                                String name = tableModel.getValueAt(i, 0).toString();
                                if (!ondemandContainsTrack(name)) {
                                    tableModel.removeRow(i);
                                }

                            }
//update added tracks
                            for (Track track : DataHolder.onDemandTrackList) {
                                if (!trackPresentInPlayList(track, DataHolder.playList, tableModel)) {
                                    tableModel.addColumn(new Object[]{track.getName(),
                                        HelperUtility.getTruncatedLength(track.getLength())});
                                }
                            }
//                        List<Track> tracks = programme.getPlaylist().getTrack();
//                        Set<Track> tempSet = new HashSet<Track>();
//                        tempSet.addAll(DataHolder.onDemandTrackList);
//                        tempSet.removeAll(tracks);
//                        if (tempSet.size() > 0) {
//
//                            for (Track track : tempSet) {
//                                for (int row = 0; row < DataHolder.playList.getRowCount(); row++) {
//                                    if (tableModel.getValueAt(row, 0).toString().equalsIgnoreCase(track.getName())) {
//                                        tableModel.removeRow(row);
//                                    }
//                                }
//
//                            }
//                        }

                            //update added tracks
//                        tempSet.clear();
//                        tempSet.addAll(DataHolder.onDemandTrackList);
//                        for (Track track : programme.getPlaylist().getTrack()) {
//                            if (!tempSet.contains(track)) {
//                                tableModel.addRow(new Object[]{track.getName(),
//                                    HelperUtility.getTruncatedLength(track.getLength())});
//                            }
//                        }
//
//                        tempSet.clear();
//                        tracks.clear();
                            DataHolder.onDemandTrackList.clear();
                            DataHolder.onDemandTrackList.addAll(programme.getPlaylist().getTrack());
                        }
                    }
                }
            }
        }
    }

    public static void getOnDemandTrackListFromUpdatedMediaUpgraded(boolean isOndemandCategorySelected) {
        DataHolder.onDemandTrackList.clear();
        DefaultTableModel playListDataModel = (DefaultTableModel) DataHolder.playList.getModel();
        if (null != DataHolder.media) {
            for (Media.Channels.Station station : DataHolder.media.getChannels().getStation()) {
                if (station.getName().equalsIgnoreCase("OnDemandStation")) {
                    if (DataHolder.ondemandCategoryName == null) {
                        DataHolder.programmeLabel.setText("Unavailable");
                    }
                    popoulateOndemandCategories(station.getProgramme());
                    if (DataHolder.ondemandCategoryName == null) {
                        if (station.getProgramme().size() > 0) {
                            DataHolder.currentProgramme = station.getProgramme().get(0);
                            DataHolder.ondemandCategoryName = DataHolder.currentProgramme.getName();
                        }
                    }
                    for (Programme programme : station.getProgramme()) {

                        if (isOndemandCategorySelected) {
                            playListDataModel.setRowCount(0);
                            playListDataModel.setRowCount(6);
                            DataHolder.onDemandPlayListLastRow = 0;
                            DataHolder.onDemandPlayListLastColumn = 0;
                            DataHolder.onDemandTrackListDisplayed.clear();
                            DataHolder.onDemandPlayListLastRow = 0;
                            DataHolder.onDemandPlayListLastColumn = -1;
                            if (DataHolder.ondemandCategoryName.equalsIgnoreCase(programme.getName())) {
                                DataHolder.currentProgramme = programme;
                                DataHolder.onDemandTrackList.clear();
                                DataHolder.onDemandTrackList.addAll(programme.getPlaylist().getTrack());
                            }
                        } else {
                            if (DataHolder.ondemandCategoryName == null) {
                                DataHolder.onDemandTrackList.clear();
                                DataHolder.onDemandTrackList.addAll(programme.getPlaylist().getTrack());
                            } else {
                                if (DataHolder.ondemandCategoryName.equalsIgnoreCase(programme.getName())) {
                                    DataHolder.currentProgramme = programme;
                                    DataHolder.onDemandTrackList.clear();
                                    DataHolder.onDemandTrackList.addAll(programme.getPlaylist().getTrack());

                                }
                            }
                        }
                    }
                }
            }
        }

        populateOndemandPlayListForCurrentCategory(playListDataModel);
    }

    private static void popoulateOndemandCategories(List<Programme> programmes) {
        //populate Categories.
        DefaultTableModel ondemandCategoriesDataModel = (DefaultTableModel) DataHolder.ondemandCategories.getModel();
        Set<String> programmeNameList = new HashSet<String>();
        //add
        for (Programme programme : programmes) {
            programmeNameList.add(programme.getName());
            if (!DataHolder.onDemandCategoryListDisplayed.contains(programme.getName())) {
                DataHolder.ondemandCategoriesLastRow = DataHolder.ondemandCategoriesLastColumn == 5 ? DataHolder.ondemandCategoriesLastRow + 1 : DataHolder.ondemandCategoriesLastRow;
                DataHolder.ondemandCategoriesLastColumn = DataHolder.ondemandCategoriesLastColumn == 5 ? 0 : DataHolder.ondemandCategoriesLastColumn + 1;
                if (ondemandCategoriesDataModel.getRowCount() == DataHolder.ondemandCategoriesLastRow) {
                    ondemandCategoriesDataModel.addRow(new Object[]{});
                }
                ondemandCategoriesDataModel.setValueAt(programme.getName(),
                        DataHolder.ondemandCategoriesLastRow, DataHolder.ondemandCategoriesLastColumn);
                DataHolder.onDemandCategoryListDisplayed.add(programme.getName());
            }
        }
        //remove
        for (int row = 0; row < ondemandCategoriesDataModel.getRowCount(); row++) {
            for (int column = 0; column < ondemandCategoriesDataModel.getColumnCount(); column++) {
                if (ondemandCategoriesDataModel.getValueAt(row, column) != null) {
                    if (!programmeNameList.contains(ondemandCategoriesDataModel.getValueAt(row, column))) {
                        if (ondemandCategoriesDataModel.getValueAt(row, column).toString().equalsIgnoreCase(DataHolder.currentProgramme.getName())) {
                            ((DefaultTableModel) DataHolder.playList.getModel()).setRowCount(0);
                            DataHolder.currentProgramme = null;
                            initializePlaylist();
                        }
                        if (ondemandCategoriesDataModel.getValueAt(DataHolder.ondemandCategoriesLastRow, DataHolder.ondemandCategoriesLastColumn) != null) {
                            DataHolder.onDemandCategoryListDisplayed.remove(ondemandCategoriesDataModel.getValueAt(row, column));
                            ondemandCategoriesDataModel.setValueAt(ondemandCategoriesDataModel.getValueAt(
                                    DataHolder.ondemandCategoriesLastRow, DataHolder.ondemandCategoriesLastColumn), row, column);
                            ondemandCategoriesDataModel.setValueAt(null,
                                    DataHolder.ondemandCategoriesLastRow, DataHolder.ondemandCategoriesLastColumn);
                            if (DataHolder.ondemandCategoriesLastColumn == 0) {
                                if (DataHolder.ondemandCategoriesLastRow > 0) {
                                    DataHolder.ondemandCategoriesLastRow = DataHolder.ondemandCategoriesLastRow - 1;
                                    DataHolder.ondemandCategoriesLastColumn = 5;
                                }
                            } else {
                                DataHolder.ondemandCategoriesLastColumn = DataHolder.ondemandCategoriesLastColumn - 1;
                            }
                        }
                    }
                }
            }
        }

        if (ondemandCategoriesDataModel.getRowCount() > 1) {
            for (int i = (ondemandCategoriesDataModel.getRowCount() - 1); i > DataHolder.ondemandCategoriesLastRow; i--) {
                ondemandCategoriesDataModel.removeRow(i);
            }
        }
        if (DataHolder.ondemandCategoriesLastRow == 0 && DataHolder.ondemandCategoriesLastColumn == 0) {
            if (ondemandCategoriesDataModel.getValueAt(DataHolder.ondemandCategoriesLastRow, DataHolder.ondemandCategoriesLastColumn) == null) {
                DataHolder.ondemandCategoriesLastColumn = -1;
            }
        }

    }

    private static void populateOndemandPlayListForCurrentCategory(DefaultTableModel playListDataModel) {
        //fetch default table model
        if (DataHolder.onDemandTrackList.size() > 0) {
            //add
            for (Track track : DataHolder.onDemandTrackList) {
                if (!DataHolder.onDemandTrackListDisplayed.contains(track.getName())) {
                    DataHolder.onDemandPlayListLastRow = DataHolder.onDemandPlayListLastColumn == 3 ? DataHolder.onDemandPlayListLastRow + 1 : DataHolder.onDemandPlayListLastRow;
                    DataHolder.onDemandPlayListLastColumn = DataHolder.onDemandPlayListLastColumn == 3 ? 0 : DataHolder.onDemandPlayListLastColumn + 1;
                    if (playListDataModel.getRowCount() == DataHolder.onDemandPlayListLastRow) {
                        playListDataModel.addRow(new Object[]{});
                    }
                    playListDataModel.setValueAt(new OndemandPlaylistPanel(track),
                            DataHolder.onDemandPlayListLastRow, DataHolder.onDemandPlayListLastColumn);
                    DataHolder.onDemandTrackListDisplayed.add(track.getName());
                }
            }
        }

        //remove 
        for (int row = 0; row < playListDataModel.getRowCount(); row++) {
            for (int column = 0; column < playListDataModel.getColumnCount(); column++) {
                if (playListDataModel.getValueAt(row, column) != null) {
                    OndemandPlaylistPanel panel = (OndemandPlaylistPanel) playListDataModel.getValueAt(row, column);
                    if (!ondemandContainsTrack(panel)) {
                        playListDataModel.setValueAt(null, row, column);
                        DataHolder.onDemandTrackListDisplayed.remove(panel.getSongTitleLabel().getName());
                    }
                }
            }
        }

        if (DataHolder.onDemandTrackList.size() > 0) {
            //remapping all the deleted cells.
            for (int row = 0; row < DataHolder.onDemandPlayListLastRow + 1; row++) {
                if (row < DataHolder.onDemandPlayListLastRow) {
                    for (int column = 0; column < 4; column++) {
                        if (playListDataModel.getValueAt(row, column) == null) {
                            if (playListDataModel.getValueAt(DataHolder.onDemandPlayListLastRow, DataHolder.onDemandPlayListLastColumn) != null) {
                                playListDataModel.setValueAt(playListDataModel.getValueAt(DataHolder.onDemandPlayListLastRow, DataHolder.onDemandPlayListLastColumn), row, column);
                                playListDataModel.setValueAt(null, DataHolder.onDemandPlayListLastRow, DataHolder.onDemandPlayListLastColumn);
                            }
                            if (DataHolder.onDemandPlayListLastColumn == 0) {
                                DataHolder.onDemandPlayListLastColumn = 3;
                                if (DataHolder.onDemandPlayListLastRow > 0) {
                                    DataHolder.onDemandPlayListLastRow--;
                                }
                            } else {
                                DataHolder.onDemandPlayListLastColumn--;
                            }
                        }
                    }
                } else if (row == DataHolder.onDemandPlayListLastRow) {
                    for (int column = 0; column < DataHolder.onDemandPlayListLastColumn + 1; column++) {
                        if (playListDataModel.getValueAt(row, column) == null) {
                            if (playListDataModel.getValueAt(DataHolder.onDemandPlayListLastRow, DataHolder.onDemandPlayListLastColumn) != null) {
                                playListDataModel.setValueAt(playListDataModel.getValueAt(DataHolder.onDemandPlayListLastRow, DataHolder.onDemandPlayListLastColumn), row, column);
                                playListDataModel.setValueAt(null, DataHolder.onDemandPlayListLastRow, DataHolder.onDemandPlayListLastColumn);
                            }
                            if (DataHolder.onDemandPlayListLastColumn == 0) {
                                if (row > 0) {
                                    DataHolder.onDemandPlayListLastColumn = 3;
                                    DataHolder.onDemandPlayListLastRow = DataHolder.onDemandPlayListLastRow - 1;
                                }
                            } else {
                                DataHolder.onDemandPlayListLastColumn--;
                            }
                        }
                    }
                }
            }
        }
        if (playListDataModel.getRowCount() > 6) {
            for (int i = (playListDataModel.getRowCount() - 1); i > DataHolder.onDemandPlayListLastRow; i--) {
                playListDataModel.removeRow(i);
            }
        }
        if (playListDataModel.getRowCount() < 6) {
            for (int i = (playListDataModel.getRowCount()); i < 6; i++) {
                playListDataModel.addRow(new Object[]{});
            }
        }
    }

    private static boolean ondemandContainsTrack(String name) {
        for (Track track : DataHolder.onDemandTrackList) {
            if (track.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public static boolean ondemandContainsTrack(OndemandPlaylistPanel panel) {
        for (Track track : DataHolder.onDemandTrackList) {
            if (track.getName().equalsIgnoreCase(panel.getSongTitleLabel().getName())) {
                if (!"".equalsIgnoreCase(track.getOndemandlabel())) {
                    if (track.getOndemandlabel().equalsIgnoreCase(panel.getSongTitleLabel().getText())) {
                        return true;
                    } else {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private static boolean ondemandContainsCategory(String name) {
        for (Track track : DataHolder.onDemandTrackList) {
            if (track.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    private boolean ifListContainsTrack(Set<Track> tempSet, Track track) {
        for (Track tempSet1 : tempSet) {
            if (tempSet1.getName().equalsIgnoreCase(track.getName())) {
                return true;
            }
        }
        return false;
    }

    public void createMediaObjectFromXmlString(String updatedXml) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Media.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        StringReader reader = new StringReader(updatedXml);
        DataHolder.media = (Media) jaxbUnmarshaller.unmarshal(reader);
    }

    public void createMediaObjectFromXmlString(File xmlFile) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Media.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        DataHolder.media = (Media) jaxbUnmarshaller.unmarshal(xmlFile);
    }

    public static void downloadTracksFromFtp(List<Track> listTracks) throws java.net.SocketTimeoutException {
        for (Track track : listTracks) {
            try {
                if (!new File(DataHolder.archiveFolderPath).exists()) {
                    new File(DataHolder.archiveFolderPath).mkdir();
                }

                if (new File(DataHolder.archiveFolderPath + FileSystems.getDefault().getSeparator() + track.getName()).exists()) {
                    NeptuneLogger.getInstance().getLogger().log(Level.INFO, "File : {0} already preset. Skipping.....", track.getName());
                } else {
                    DataHolder.downloadLogo.setVisible(true);
                    DataHolder.downloadLogo.setToolTipText("Downloading --> " + track.getName());
                    if (!DataHolder.initializationCompleted) {
                        DataHolder.playerStatusLabel.setText("Download in Progress");
                        DataHolder.downloadFileDetails.setText("File : " + track.getName());
                        DataHolder.stationLabel.setText("Unavailable");
                        DataHolder.programmeLabel.setText("Unavailable");
                        DataHolder.currentTrackLabel.setText("Unavailable");
                        DataHolder.nextTrackLabel.setText("Unavailable");
                    }
                    StringBuilder url = new StringBuilder(DataHolder.httpURL + "/neptune/sync/MusicFile.ashx?UserName="
                            + DataHolder.configProperties.getProperty("userName").replace(" ", "%20")
                            + "&SystemName=" + DataHolder.configProperties.getProperty("SystemName").replace(" ", "%20")
                            + "&DeviceToken=" + DataHolder.configProperties.getProperty("DeviceToken").replace(" ", "%20")
                            + "&TrackId=" + track.getId());
                    NeptuneLogger.getInstance().getLogger().log(Level.INFO, "URL : {0}", url);

                    URL obj = new URL(url.toString());
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                    con.setDoOutput(true);
                    con.setRequestMethod("GET");
                    con.setRequestProperty("User-Agent", "Mozilla/5.0");
                    con.setConnectTimeout(100000);
                    con.setReadTimeout(15000);
                    int responseCode = con.getResponseCode();
                    String contentType = con.getContentType();
                    int contentLength = con.getContentLength();
                    if (contentType.startsWith("text/") || contentLength == -1) {
                        throw new IOException("This is not a binary file.");
                    }
                    InputStream raw = con.getInputStream();
                    InputStream in = new BufferedInputStream(raw);
                    byte[] data = new byte[contentLength];
                    int bytesRead = 0;
                    int offset = 0;

                    while (offset < contentLength) {
                        bytesRead = in.read(data, offset, data.length - offset);
                        if (bytesRead == -1) {
                            break;
                        }
                        offset += bytesRead;
                    }
                    in.close();
                    if (offset != contentLength) {
                        throw new IOException("Only read " + offset + " bytes; Expected " + contentLength + " bytes");
                    }

                    File trackFile = new File(DataHolder.archiveFolderPath + FileSystems.getDefault().getSeparator() + track.getName());
                    trackFile.createNewFile();
                    FileOutputStream out = new FileOutputStream(trackFile);
                    out.write(data);
                    out.flush();
                    out.close();
                    NeptuneLogger.getInstance().getLogger().log(Level.INFO, "Finished downloading...{0}", track.getName());
                }
            } catch (MalformedURLException ex) {
                DataHolder.downloadLogo.setVisible(false);
                NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                DataHolder.downloadLogo.setVisible(false);
                NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                DataHolder.downloadLogo.setVisible(false);
                NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
            }
        }
        DataHolder.downloadLogo.setVisible(false);
    }

    public static boolean checkInternetConnectivity() {
        URL url;
        try {
            url = new URL("http://www.google.com");

            System.out.println(url.getHost());
            HttpURLConnection con = (HttpURLConnection) url
                    .openConnection();
            con.connect();
            if (con.getResponseCode() == 200) {
                return true;
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(HelperUtility.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HelperUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static String getTruncatedLength(XMLGregorianCalendar lengthDate) {
        String length = DateUtil.getInstance().formatDate(lengthDate, DATE_FORMAT_HH_MM_SS);
        return length.substring(0, 2).equalsIgnoreCase("00") ? length.substring(3, length.length()) : length;
    }

    public static void highlightCurrentTrackInPlayList(Track currenttrack) {
        try {
            if (currenttrack != null) {
                DefaultTableModel tableModel = (DefaultTableModel) DataHolder.playList.getModel();
                for (int row = 0; row < DataHolder.playList.getRowCount(); row++) {
                    for (int column = 0; column < 4; column++) {
                        if (null != tableModel.getValueAt(row, column)) {
                            OndemandPlaylistPanel panel = (OndemandPlaylistPanel) tableModel.getValueAt(row, column);
                            if (panel.getSongTitleLabel().getText().equalsIgnoreCase(currenttrack.getName())) {
                                DataHolder.playList.setRowSelectionInterval(row, row);
                                DataHolder.playList.setColumnSelectionInterval(column, column);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(HelperUtility.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static void highlightCurrentCategoryInCategoryList() {
        try {
            if (DataHolder.ondemandCategoryName != null) {
                DefaultTableModel tableModel = (DefaultTableModel) DataHolder.ondemandCategories.getModel();
                for (int row = 0; row < DataHolder.ondemandCategories.getRowCount(); row++) {
                    for (int column = 0; column < 6; column++) {
                        if (null != tableModel.getValueAt(row, column)) {
                            if (DataHolder.ondemandCategoryName.equalsIgnoreCase(tableModel.getValueAt(row, column).toString())) {
                                DataHolder.ondemandCategories.setRowSelectionInterval(row, row);
                                DataHolder.ondemandCategories.setColumnSelectionInterval(column, column);
                            } else {
                                System.out.println("Not matching .....");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(HelperUtility.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static boolean checkIfCampaignIsEligible(Station.Schedule.Campaign campaign, Date currentTime) {
        if (DateUtil.getInstance().isCampaignPlayorderContainsCurrentTime(campaign.getTimerepeat(), currentTime)
                && DateUtil.getInstance().isRepeat(campaign.getRepeat(), null)) {
            return true;
        } else {
            return false;
        }
    }

    public static int getSequenceFromTrackRepeat(Station.Schedule.Campaign campaign, Date currentTime) {
        if (!campaign.getTimerepeat().contains(",")) {
            return 0;
        }
        String[] playOrderString = campaign.getTimerepeat().split(",");
        for (int i = 0; i < playOrderString.length; i++) {
            Date date = DateUtil.getInstance().getDate(playOrderString[i].trim(), DATE_FORMAT_HH_MM_SS);
            if (date.compareTo(currentTime) == 0) {
                return i;
            }
        }
        return -1;
    }

    private boolean trackPresentInPlayList(Track track, JTable playList, DefaultTableModel tableModel) {
        for (int i = 0; i < DataHolder.playList.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).toString().equalsIgnoreCase(track.getName())) {
                return true;
            }
        }
        return false;
    }

    private static String FetchNextProgramName() {

        return null;
    }

    public static void initializePlaylist() {
        playList = new JTable(6, 4) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        playList.setTableHeader(null);
        playList.setColumnSelectionAllowed(true);
        playList.setRowSelectionAllowed(true);
        playList.setRowHeight(50);
        playList.setVisible(true);
        playList.setBorder(BorderFactory.createEmptyBorder());
        playListScrollPanel.setViewportView(DataHolder.playList);
        playList.setDefaultRenderer(Object.class, new OndemandPlaylistPanel());
        playList.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }

    public static boolean addOdpMaxtime(String trackTime) {
        int maxHour = 0;
        int maxMin = 0;
        int maxSec = 0;

        if (DataHolder.odpMaxTime.getText().split("[:]").length > 2) {
            maxSec = Integer.parseInt(DataHolder.odpMaxTime.getText().split("[:]")[2]);
            maxMin = Integer.parseInt(DataHolder.odpMaxTime.getText().split("[:]")[1]);
            maxHour = Integer.parseInt(DataHolder.odpMaxTime.getText().split("[:]")[0]);
        } else {
            maxSec = Integer.parseInt(DataHolder.odpMaxTime.getText().split("[:]")[1]);
            maxMin = Integer.parseInt(DataHolder.odpMaxTime.getText().split("[:]")[0]);
        }

        int trackSec = Integer.parseInt(trackTime.split("[:]")[1]);
        int trackMin = Integer.parseInt(trackTime.split("[:]")[0]);

        maxSec = maxSec + trackSec;
        if (maxSec > 59) {
            maxSec = maxSec - 60;
            ++maxMin;
        }

        maxMin = maxMin + trackMin;
        if (maxMin > 59) {
            maxMin = maxMin - 60;
            ++maxHour;
        }
        StringBuilder maxtimeBuilder = new StringBuilder();
        if (maxHour > 0) {
            maxtimeBuilder.append("00".substring(String.valueOf(maxHour).length()))
                    .append(String.valueOf(maxHour)).append(":")
                    .append("00".substring(String.valueOf(maxMin).length()))
                    .append(String.valueOf(maxMin)).append(":")
                    .append("00".substring(String.valueOf(maxSec).length()))
                    .append(String.valueOf(maxSec));
        } else {
            maxtimeBuilder.append("00".substring(String.valueOf(maxMin).length()))
                    .append(String.valueOf(maxMin)).append(":")
                    .append("00".substring(String.valueOf(maxSec).length()))
                    .append(String.valueOf(maxSec));
        }
        DataHolder.odpMaxTime.setText(maxtimeBuilder.toString());
        return true;
    }

    public static boolean addOdpMaxTimeWhenReplacingTrack(String existingTrackTime, String newTrackTime) {
        int maxHour = 0;
        int maxMin = 0;
        int maxSec = 0;

        if (DataHolder.odpMaxTime.getText().split("[:]").length > 2) {
            maxSec = Integer.parseInt(DataHolder.odpMaxTime.getText().split("[:]")[2]);
            maxMin = Integer.parseInt(DataHolder.odpMaxTime.getText().split("[:]")[1]);
            maxHour = Integer.parseInt(DataHolder.odpMaxTime.getText().split("[:]")[0]);
        } else {
            maxSec = Integer.parseInt(DataHolder.odpMaxTime.getText().split("[:]")[1]);
            maxMin = Integer.parseInt(DataHolder.odpMaxTime.getText().split("[:]")[0]);
        }

        int existingTrackSec = Integer.parseInt(existingTrackTime.split("[:]")[1]);
        int existingTrackMin = Integer.parseInt(existingTrackTime.split("[:]")[0]);

        int newTrackSec = Integer.parseInt(newTrackTime.split("[:]")[1]);
        int newTrackMin = Integer.parseInt(newTrackTime.split("[:]")[0]);

        maxSec = maxSec + newTrackSec - existingTrackSec;
        if (maxSec > 59) {
            maxSec = maxSec - 60;
            ++maxMin;
        } else if (maxSec < 0) {
            maxSec = maxSec * -1;
            if (maxMin > 0) {
                --maxMin;
            }
        }

        maxMin = maxMin + newTrackMin - existingTrackMin;
        if (maxMin > 59) {
            maxMin = maxMin - 60;
            ++maxHour;
        } else if (maxMin < 0) {
            maxMin = maxMin * -1;
            if (maxHour > 0) {
                --maxHour;
            }
        }

        StringBuilder maxtimeBuilder = new StringBuilder();
        if (maxHour > 0) {
            maxtimeBuilder.append("00".substring(String.valueOf(maxHour).length()))
                    .append(String.valueOf(maxHour)).append(":")
                    .append("00".substring(String.valueOf(maxMin).length()))
                    .append(String.valueOf(maxMin)).append(":")
                    .append("00".substring(String.valueOf(maxSec).length()))
                    .append(String.valueOf(maxSec));
        } else {
            maxtimeBuilder.append("00".substring(String.valueOf(maxMin).length()))
                    .append(String.valueOf(maxMin)).append(":")
                    .append("00".substring(String.valueOf(maxSec).length()))
                    .append(String.valueOf(maxSec));
        }
        DataHolder.odpMaxTime.setText(maxtimeBuilder.toString());
        return true;
    }

    public static void initializeODPMedia() {
        DataHolder.odpMedia = new Odpmedia();
    }

    public static <T> T unMarshall(Class<T> className, File file)
            throws JAXBException, FileNotFoundException {
        JAXBContext jaxbContext;
        Unmarshaller unMarshaller;
        jaxbContext = JAXBContext.newInstance(className);
        unMarshaller = jaxbContext.createUnmarshaller();
        InputStream inputStream = new FileInputStream(file);
        StreamSource streamSource = new StreamSource(inputStream);
        return unMarshaller.unmarshal(streamSource, className).getValue();
    }

    public static int covertBooleanToInt(Boolean booleanValue) {
        return booleanValue == true ? 1 : 0;
    }
}
