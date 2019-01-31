/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptune.common;

import WidgetUtils.ImageHolder;
import WidgetUtils.OdpHeader;
import WidgetUtils.OdpPanelInEditMode;
import WidgetUtils.OndemandPlaylistPanel;
import com.neptune.controller.ParentController;
import com.neptune.schema.Media;
import com.neptune.schema.Media.Channels.Station;
import com.neptune.schema.Media.Channels.Station.Programme;
import com.neptune.schema.Media.Channels.Station.Programme.Playlist.Track;
import com.neptune.schema.Odpmedia;
import com.neptune.util.DateUtil;
import com.neptune.util.HelperUtility;
import com.neptune.util.NeptuneLogger;
import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.SourceDataLine;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javazoom.jl.player.advanced.AdvancedPlayer;

/**
 *
 * @author ragesh.raveendran
 */
public class DataHolder {

    public static String httpURL;
    public static String executionFolderPath;
    public static String propFileName;
    public static String contentSyncFile;
    public static String odpFilePath;
    public static File odpFile;
    public static File odpPrimaryBackupFile;
    public static File odpSecondaryBackupFile;
    public static String neptuneLoggerFile;
    public static Properties configProperties;
    public static Media media;
    public static Odpmedia odpMedia;
    public static List<Track> trackList;
    public static String archiveFolderPath;
    public static Station currentStation;
    public static Programme currentProgramme;
    public static Track currenttrack;
    public static Track nextTrack;
    public static Date nextTrackTimeForProgrammesWithEndtimeruleAsExactly;
    public static Date nextDailyProgrammeTime;
    public static boolean isRegisteredUser;
    public static String currentPlayerStatus;
    public static InetAddress ip;
    public static String macAddress;
    public static boolean isComplete;
    public static boolean onDemandOverride;
    public static Set<Track> onDemandTrackList;
    public static List<com.neptune.schema.Odpmedia.Playlist.Track> odpTrackList;
    public static Long odpCurrentTrackId = 0L;
    public static Set<String> onDemandTrackListDisplayed;
    public static Set<String> onDemandCategoryListDisplayed;
    public static boolean isDynamicTenmplate;
    public static boolean isUpdateAvilable;

    public static javax.swing.JButton onDemandStationButton = new javax.swing.JButton();
    public static javax.swing.JButton otherStationButton = new javax.swing.JButton();
    public static javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
    public static javax.swing.JLabel playerStatusLabel = new javax.swing.JLabel();
    public static javax.swing.JLabel stationLabel = new javax.swing.JLabel();
    public static javax.swing.JLabel programmeLabel = new javax.swing.JLabel();
    public static javax.swing.JLabel nextProgrammeLabel = new javax.swing.JLabel();
    public static javax.swing.JLabel currentTrackLabel = new javax.swing.JLabel();
    public static javax.swing.JLabel nextTrackLabel = new javax.swing.JLabel();
    public static javax.swing.JLabel jLabel4 = new javax.swing.JLabel();
    public static javax.swing.JLabel registrationMsg = new javax.swing.JLabel();
    public static javax.swing.JLabel downloadFileDetails = new javax.swing.JLabel();
    public static javax.swing.JTable playList = new javax.swing.JTable();
    public static javax.swing.JTable ondemandCategories = new javax.swing.JTable();
    public static javax.swing.JLabel connectivity = new javax.swing.JLabel();
    public static javax.swing.JLabel downloadLogo = new javax.swing.JLabel();
    public static javax.swing.JLabel reloadPlaylist = new javax.swing.JLabel();
    public static javax.swing.JLabel odpBackupLabel = new javax.swing.JLabel();
    public static javax.swing.JLabel updateLogo = new javax.swing.JLabel();
    public static javax.swing.JLabel playButton = new javax.swing.JLabel();
    public static javax.swing.JScrollPane playListScrollPanel = new javax.swing.JScrollPane();
    public static javax.swing.JScrollPane ondemandCategoriesScrollPanel = new javax.swing.JScrollPane();
    public static javax.swing.JScrollPane odpScrollPanel = new javax.swing.JScrollPane();
    //this is the playlist that will display tracks for and odp playlist.
    public static javax.swing.JTable odpPlayList = new javax.swing.JTable();

    public static String username;
    public static String userPwd;

    public static FileInputStream fis;
    public static BufferedInputStream bis;
    public static SourceDataLine line = null;

    public static String musicDir = null;
    public static List<String> currentProgramPlaylist;
    public static int currentTrackNumber;

    public static AudioInputStream decodedAudioInputStream = null;
    public static AudioInputStream plainAudioInputStream = null;

    public static Thread playbackThread;

    public static boolean isPlaying = false;
    public static boolean isPause = false;

    public static String audioFilePath;
    public static String lastOpenPath;
    public int nBytesRead;
    public byte[] data = new byte[4096];

    public static AdvancedPlayer player;
    public static int pausedOnFrame = 0;

    public static long currentTrackLength;
    public static long currentTrackPauseLength;
    public static String currentTrackPath;
    public static String nextTrackPath;
    public static String playListSelectedTrackPath;

    public static boolean initializationCompleted;
    public static boolean updateInProgress = false;

    public static int onDemandPlayListLastRow = 0;
    public static int onDemandPlayListLastColumn = -1;
    public static int ondemandCategoriesLastRow = 0;
    public static int ondemandCategoriesLastColumn = -1;
    public static String ondemandCategoryName;

    public static JPanel odpHeaderPanel = new OdpHeader();
    public static JButton odpPlaylistButton = new JButton();
//    public static boolean isOdpBuildMode = false;
//    public static boolean isOdpEditMode = false;
    public static boolean isOdpEditLogoRequired = false;
//    public static boolean isOdpListMode = false;
    public static boolean isOdpPlaylistPlaying = false;
    public static int odpLastRow = 0;
    public static javax.swing.JLabel odpMaxTime;
    public static javax.swing.table.DefaultTableModel odpPlaylistModel;

    public static javax.swing.JLabel counter;
    public static javax.swing.JButton odpBuildButton;
    public static javax.swing.JButton odpEditButton;
    public static javax.swing.JButton odpDeleteButton;
    public static javax.swing.JLabel odpHeaderLabel;
    public static javax.swing.JButton odpSaveButton;

    public static String currentOdpPlaylist;

    public static String odpEditEnumStatus;
    public static String odpModeStatus;
    public static JPanel imageHolderPanel = new ImageHolder();

    public static JLabel odpSongLabel = new JLabel();
    public static JLabel odpImageEditLabel = new JLabel();
    public static JLabel imageHolderPanelLabel = new JLabel();

    public static boolean isStaticTrack = false;
    public static String odpFilePrimaryBackupPath;
    public static String odpFileSecondaryBackupPath;

    public static void initialize() {
        try {
            Constants.status.setText("Initializing Player....");
//  Test Path          
//            httpURL = "http://52.24.249.175";
//  Prod Path          
            httpURL = "http://52.10.194.92";
            executionFolderPath = new File(ParentController.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath().toString()).getParent();
            propFileName = executionFolderPath + FileSystems.getDefault().getSeparator() + "StreamPlayerSyn.config";
            contentSyncFile = executionFolderPath + FileSystems.getDefault().getSeparator() + "Content.syn";
            odpFilePath = executionFolderPath + FileSystems.getDefault().getSeparator() + "Odp.asynch";
            archiveFolderPath = executionFolderPath + FileSystems.getDefault().getSeparator() + "Archive";
            File logDir = new File(executionFolderPath + FileSystems.getDefault().getSeparator() + "Log");
            if (!logDir.exists()) {
                logDir.mkdir();
            }
            neptuneLoggerFile = logDir.getAbsolutePath() + FileSystems.getDefault().getSeparator() + "Neptune-" + DateUtil.getInstance().formatDate(new Date(), DateUtil.getInstance().DATE_FORMAT__MMDDYYHHMM) + ".log";
            initializationCompleted = false;
            media = null;
            odpMedia = new Odpmedia();
            trackList = null;
            currentStation = null;
            currentProgramme = null;
            currenttrack = null;
            nextTrack = null;
            currentTrackNumber = 0;
            isRegisteredUser = false;
            isComplete = false;
            currentPlayerStatus = "Initializing...";
            playerStatusLabel.setText(currentPlayerStatus);
            ondemandCategoryName = null;

            currentTrackLabel.setText(Constants.SEARCHING);
            nextTrackLabel.setText(Constants.SEARCHING);
            programmeLabel.setText(Constants.SEARCHING);
            nextProgrammeLabel.setText("Unavailable");
            stationLabel.setText(Constants.SEARCHING);

            registrationMsg.setForeground(new java.awt.Color(255, 51, 51));
            registrationMsg.setText("Register to complete installation");
            registrationMsg.setPreferredSize(new Dimension(300, 30));

            onDemandTrackList = new TreeSet<Track>();
            onDemandTrackListDisplayed = new TreeSet<String>();
            onDemandCategoryListDisplayed = new TreeSet<String>();

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
            playListScrollPanel.setVisible(true);

            ondemandCategories = new JTable(1, 6) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            DefaultTableCellRenderer centerRendererForOndemandCategories = new DefaultTableCellRenderer();
            centerRendererForOndemandCategories.setHorizontalAlignment(SwingConstants.CENTER);
            ondemandCategories.getColumnModel().getColumn(0).setCellRenderer(centerRendererForOndemandCategories);
            ondemandCategories.getColumnModel().getColumn(1).setCellRenderer(centerRendererForOndemandCategories);
            ondemandCategories.getColumnModel().getColumn(2).setCellRenderer(centerRendererForOndemandCategories);
            ondemandCategories.getColumnModel().getColumn(3).setCellRenderer(centerRendererForOndemandCategories);
            ondemandCategories.getColumnModel().getColumn(4).setCellRenderer(centerRendererForOndemandCategories);
            ondemandCategories.getColumnModel().getColumn(5).setCellRenderer(centerRendererForOndemandCategories);
            ondemandCategories.setTableHeader(null);
            ondemandCategories.setColumnSelectionAllowed(true);
            ondemandCategories.setRowSelectionAllowed(true);
            ondemandCategories.setRowHeight(36);
            ondemandCategories.setVisible(true);
            ondemandCategories.setBorder(BorderFactory.createEmptyBorder());
            ondemandCategories.setBackground(Color.black);
            ondemandCategories.setForeground(Color.white);
            ondemandCategories.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            ondemandCategoriesScrollPanel.setViewportView(DataHolder.ondemandCategories);
            ondemandCategoriesScrollPanel.setVisible(true);

            odpPlayList = new JTable(6, 1) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            odpPlayList.setTableHeader(null);
            odpPlayList.setColumnSelectionAllowed(true);
            odpPlayList.setRowSelectionAllowed(true);
            odpPlayList.setRowHeight(50);
            odpPlayList.setVisible(true);
            odpPlayList.setBorder(BorderFactory.createEmptyBorder());
            odpScrollPanel.setViewportView(DataHolder.odpPlayList);
            odpPlayList.setDefaultRenderer(Object.class, new OdpPanelInEditMode());
            odpPlayList.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            odpScrollPanel.setVisible(true);

            odpPlaylistButton.setText("Existing On Demand Playlist");
            odpMaxTime.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
            odpMaxTime.setText("00:00");
            onDemandOverride = false;
            isDynamicTenmplate = false;
            isUpdateAvilable = false;
            nextTrackPath = Constants.EMPTY_STRING;
            playListSelectedTrackPath = Constants.EMPTY_STRING;
            currentOdpPlaylist = Constants.EMPTY_STRING;

            downloadLogo.setVisible(false);
            odpModeStatus = OdpEditEnum.OFFLINE.toString();
            odpEditEnumStatus = OdpEditEnum.OFFLINE.toString();
            nextTrackTimeForProgrammesWithEndtimeruleAsExactly = null;
            nextDailyProgrammeTime = null;
            macAddress = HelperUtility.getMACAddress();
            odpFile = new File(DataHolder.odpFilePath);
            odpFilePrimaryBackupPath = executionFolderPath + FileSystems.getDefault().getSeparator() + "pbk" + FileSystems.getDefault().getSeparator() + "OdpPrimary.bk";
            odpFileSecondaryBackupPath = executionFolderPath + FileSystems.getDefault().getSeparator() + "sbk";
            odpPrimaryBackupFile = new File(odpFilePrimaryBackupPath);
            
            
            if (!DataHolder.odpPrimaryBackupFile.exists()) {
                DataHolder.odpPrimaryBackupFile.getParentFile().mkdirs();
                DataHolder.odpPrimaryBackupFile.createNewFile();
            }
            
        } catch (URISyntaxException ex) {
            NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
        } catch (SocketException ex) {
            NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DataHolder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
