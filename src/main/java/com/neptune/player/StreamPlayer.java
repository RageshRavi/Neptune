/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptune.player;

import com.neptune.common.CampaignPlayOrder;
import com.neptune.common.DataHolder;
import com.neptune.common.EndtimeRuleEnum;
import com.neptune.comparators.ProgrammeStartdateComparator;
import com.neptune.schema.Media;
import com.neptune.schema.Media.Channels.Station;
import com.neptune.schema.Media.Channels.Station.Programme;
import com.neptune.schema.Media.Channels.Station.Programme.Playlist.Track;
import com.neptune.schema.Odpmedia.Playlist;
import com.neptune.util.DateUtil;
import static com.neptune.util.DateUtil.DATE_FORMAT_HH_MM_SS;
import static com.neptune.util.DateUtil.DATE_FORMAT_NEPTUNE;
import com.neptune.util.HelperUtility;
import com.neptune.util.NeptuneLogger;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

/**
 *
 * @author ragesh.raveendran
 */
public class StreamPlayer {

    public static void playback(boolean isCampaign, Date currentDate, Date currentTime) {
        try {
            HelperUtility helperUtility = new HelperUtility();
            if (!isCampaign) {
                if (DataHolder.currentStation == null) {
                    loadCurrentProgramme(currentDate, currentTime);
                } else if (!DataHolder.currentStation.getName().equalsIgnoreCase("OnDemandStation")) {
                    try {
                        loadCurrentProgramme(currentDate, currentTime);
                    } catch (Exception e) {
                        NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, "Exception", e);
                        if (DataHolder.currentStation == null) {
                            DataHolder.stationLabel.setText("Unavailable");
                        } else {
                            DataHolder.stationLabel.setText(DataHolder.currentStation.getName());
                        }
                        if (DataHolder.currentProgramme == null) {
                            DataHolder.programmeLabel.setText("Unavailable");
                        } else {
                            DataHolder.programmeLabel.setText(DataHolder.currentProgramme.getName());
                        }
                        if (DataHolder.currenttrack == null) {
                            DataHolder.currentTrackLabel.setText("Unavailable");
                            DataHolder.nextTrackLabel.setText("Unavailable");
                        } else {
                            DataHolder.currentTrackLabel.setText(DataHolder.currenttrack.getTitle().isEmpty() ? DataHolder.currenttrack.getName() : DataHolder.currenttrack.getTitle());
                            DataHolder.nextTrackLabel.setText(DataHolder.nextTrackPath);
                        }
                    }
                } else if (DataHolder.currentStation.getName().equalsIgnoreCase("OnDemandStation")) {
                    loadOnDemandStationPrograms();
                }
            }
            if (DataHolder.currentTrackPath != null && !DataHolder.currentStation.getName().equalsIgnoreCase("OnDemandStation")) {
                System.out.println("Current campaign path " + DataHolder.currentTrackPath);
                File campaignFile = new File(DataHolder.currentTrackPath);
                System.out.println("File exists : " + campaignFile.exists());
                if (campaignFile.exists()) {

                    DataHolder.fis = new FileInputStream(campaignFile);
                    DataHolder.bis = new BufferedInputStream(DataHolder.fis);
                    DataHolder.player = new AdvancedPlayer(DataHolder.bis);
                    DataHolder.currentTrackPath = null;
                    final AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(campaignFile);

                    DataHolder.currentTrackLength = DataHolder.fis.available();
                    DataHolder.playbackThread = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                NeptuneLogger.getInstance().getLogger().info("Resuming at : " + DataHolder.pausedOnFrame);
                                DataHolder.currentPlayerStatus = "Playing...";
                                DataHolder.playerStatusLabel.setText(DataHolder.currentPlayerStatus);
                                if (DataHolder.currentStation == null) {
                                    DataHolder.stationLabel.setText("Unavailable");
                                } else {
                                    DataHolder.stationLabel.setText(DataHolder.currentStation.getName());
                                }
                                if (DataHolder.currentProgramme == null) {
                                    DataHolder.programmeLabel.setText("Unavailable");
                                } else {
                                    DataHolder.programmeLabel.setText(DataHolder.currentProgramme.getName());
                                }
                                if (DataHolder.currenttrack == null) {
                                    DataHolder.currentTrackLabel.setText("Unavailable");
                                } else {
                                    DataHolder.currentTrackLabel.setText(DataHolder.currenttrack.getTitle().isEmpty() ? DataHolder.currenttrack.getName() : DataHolder.currenttrack.getTitle());
                                }
                                if (DataHolder.nextTrack != null) {
                                    DataHolder.nextTrackLabel.setText(DataHolder.nextTrack.getTitle().isEmpty() ? DataHolder.nextTrack.getName() : DataHolder.nextTrack.getTitle());
                                } else {
                                    DataHolder.nextTrackLabel.setText("Unavailable");
                                }

                                if (DataHolder.currentStation.getName().equalsIgnoreCase("OnDemandStation")
                                        || DataHolder.currentProgramme.getName().equalsIgnoreCase("campaign")
                                        || DataHolder.isDynamicTenmplate) {
                                    DataHolder.isDynamicTenmplate = false;
                                    DataHolder.player.play();
                                } else {
                                    int seconds = DateUtil.getInstance().getTimeDifferenceInSeconds(DateUtil.getInstance().getDate(
                                            DataHolder.currenttrack.getStarttime(), DateUtil.getInstance().DATE_FORMAT_HH_MM_SS));
                                    Float startFrame = (Float) fileFormat.properties().get("mp3.framerate.fps") * seconds;
                                    DataHolder.player.play(startFrame.intValue(), fileFormat.getFrameLength());

                                }

                                StreamPlayer.stop();

                            } catch (Exception e) {
                                NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, e);
                            }
                        }

                    });
                    DataHolder.isComplete = false;
                    DataHolder.playbackThread.start();
                    DataHolder.isPlaying = true;
                    DataHolder.isPause = false;
                } else {
                    DataHolder.playerStatusLabel.setText("Track Unavailable");
                    DataHolder.currentTrackLabel.setText("Track Unavailable in local");
                }
            } else {
                if (!DataHolder.initializationCompleted) {
                    DataHolder.playerStatusLabel.setText("Initilializing...");
                } else {
                    DataHolder.playerStatusLabel.setText("No Program to play...");
                    if (DataHolder.currentStation == null) {
                        DataHolder.stationLabel.setText("No selected station");
                    } else {
                        DataHolder.stationLabel.setText(DataHolder.currentStation.getName());
                    }
                    if (DataHolder.currentProgramme == null) {
                        DataHolder.programmeLabel.setText("Unavailable");
                    } else {
                        DataHolder.programmeLabel.setText(DataHolder.currentProgramme.getName());
                    }
                    if (DataHolder.currenttrack == null) {
                        DataHolder.currentTrackLabel.setText("Unavailable");
                        DataHolder.nextTrackLabel.setText("Unavailable");
                    } else {
                        DataHolder.currentTrackLabel.setText(DataHolder.currenttrack.getTitle().isEmpty() ? DataHolder.currenttrack.getName() : DataHolder.currenttrack.getTitle());
                        DataHolder.nextTrackLabel.setText(DataHolder.nextTrackPath);
                    }
                }
                NeptuneLogger.getInstance().getLogger().log(Level.INFO, "No Campaign file in the location {0}", DataHolder.currentTrackPath);
            }
        } catch (FileNotFoundException ex) {
            NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
        } catch (JavaLayerException ex) {
            NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
        } catch (UnsupportedAudioFileException ex) {
            DataHolder.playerStatusLabel.setText("Error - Unsupported format");
            DataHolder.currentTrackLabel.setText("Track format not supported");
            Logger.getLogger(StreamPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void playWhenOndemandOverrideIsOn() {
        if (DataHolder.currentTrackPath == null) {
            DataHolder.playerStatusLabel.setText("Select one track");
//            loadOnDemandStationPrograms();
        } else {
//            if (DataHolder.nextTrack == null || DataHolder.nextTrackLabel.getText().equalsIgnoreCase("unavailable")) {
//                DataHolder.nextTrack = fetchNextTrack(DataHolder.currentStation.getProgramme().get(0));
//                while (DataHolder.nextTrack.getName().equalsIgnoreCase(DataHolder.currentTrackPath)) {
//                    DataHolder.nextTrack = fetchNextTrack(DataHolder.currentStation.getProgramme().get(0));
//                }
//                DataHolder.nextTrackLabel.setText(DataHolder.nextTrack.getName());
//            }
        }
        if (null != DataHolder.currentTrackPath) {
            final File onDemandTrack = new File(DataHolder.currentTrackPath);
            if (onDemandTrack.exists()) {
                try {
                    DataHolder.fis = new FileInputStream(onDemandTrack);
                    DataHolder.bis = new BufferedInputStream(DataHolder.fis);
                    DataHolder.player = new AdvancedPlayer(DataHolder.bis);
                    final AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(onDemandTrack);

                    DataHolder.currentTrackLength = DataHolder.fis.available();
                    DataHolder.playbackThread = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                DataHolder.currentPlayerStatus = "Playing...";
                                DataHolder.playerStatusLabel.setText(DataHolder.currentPlayerStatus);
                                DataHolder.currentTrackLabel.setText(onDemandTrack.getName());
                                DataHolder.currentTrackPath = null;
                                DataHolder.isComplete = false;
                                HelperUtility.highlightCurrentCategoryInCategoryList();
                                HelperUtility.highlightCurrentTrackInPlayList(DataHolder.currenttrack);
                                DataHolder.player.play();
                                StreamPlayer.stop();

                            } catch (Exception e) {
                                NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, e);
                            }
                        }

                    });
                    DataHolder.playbackThread.start();
                    DataHolder.isPlaying = true;
                    DataHolder.isPause = false;
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(StreamPlayer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (JavaLayerException ex) {
                    Logger.getLogger(StreamPlayer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedAudioFileException ex) {
                    Logger.getLogger(StreamPlayer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(StreamPlayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                DataHolder.playerStatusLabel.setText("Track Unavailable");
                DataHolder.currentTrackLabel.setText("Track Unavailable in local");
            }
        }
    }

    public static void playWhenOdpPlaylistIsOn() {
        if (DataHolder.odpCurrentTrackId == DataHolder.odpTrackList.size()) {
            DataHolder.odpCurrentTrackId = 0L;
            DataHolder.isOdpPlaylistPlaying = false;
            DataHolder.nextTrackLabel.setText("Unavailable");
            DataHolder.playList.getSelectionModel().clearSelection();
            DataHolder.odpMaxTime.setText("00:00");
            DataHolder.currenttrack = null;
            DataHolder.currentTrackPath = null;
            DataHolder.playerStatusLabel.setText(DataHolder.currentPlayerStatus);
            DataHolder.currentTrackLabel.setText("Unavailable");
            DataHolder.odpTrackList.clear();
            return;
        }
        for (Playlist.Track track : DataHolder.odpTrackList) {
            if (null != DataHolder.playbackThread) {
                DataHolder.playbackThread.stop();
            }
            if (track.getId() == DataHolder.odpCurrentTrackId) {
                DataHolder.programmeLabel.setText(DataHolder.currentOdpPlaylist);
                DataHolder.currentTrackPath = DataHolder.archiveFolderPath + FileSystems.getDefault().getSeparator() + track.getName();
                DataHolder.odpCurrentTrackId = DataHolder.odpCurrentTrackId + 1;
                DataHolder.currentTrackLabel.setText(track.getLabel());
                break;
            }
        }
        if (null != DataHolder.currentTrackPath) {
            final File onDemandTrack = new File(DataHolder.currentTrackPath);
            if (onDemandTrack.exists()) {
                try {
                    DataHolder.fis = new FileInputStream(onDemandTrack);
                    DataHolder.bis = new BufferedInputStream(DataHolder.fis);
                    DataHolder.player = new AdvancedPlayer(DataHolder.bis);
                    final AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(onDemandTrack);

                    DataHolder.currentTrackLength = DataHolder.fis.available();
                    DataHolder.playbackThread = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                DataHolder.currentPlayerStatus = "Playing...";
                                DataHolder.playerStatusLabel.setText(DataHolder.currentPlayerStatus);
                                DataHolder.isComplete = false;
                                HelperUtility.highlightCurrentCategoryInCategoryList();
                                HelperUtility.highlightCurrentTrackInPlayList(DataHolder.currenttrack);
                                DataHolder.player.play();
                                DataHolder.isComplete = true;
                                StreamPlayer.stop();

                            } catch (Exception e) {
                                NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, e);
                            }
                        }

                    });
                    DataHolder.playbackThread.start();
                    DataHolder.isPlaying = true;
                    DataHolder.isPause = false;
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(StreamPlayer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (JavaLayerException ex) {
                    Logger.getLogger(StreamPlayer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedAudioFileException ex) {
                    Logger.getLogger(StreamPlayer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(StreamPlayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                DataHolder.playerStatusLabel.setText("Track Unavailable");
                DataHolder.currentTrackLabel.setText("Track Unavailable in local");
            }
        }
    }

    public static void pause() {
        try {
            if (DataHolder.player != null) {
                DataHolder.currentTrackPauseLength = DataHolder.fis.available();
                DataHolder.isPlaying = false;
                DataHolder.isPause = true;
                DataHolder.player.close();
            }
        } catch (Exception ex) {
            NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
        }
    }

    public static void fadeOut() {
        System.out.println("Fadeout..!!!");
        if (System.getProperty("os.name").toLowerCase().contains("window")) {
            fadOutInWindowsOs();
        } else {
            fadeOutInUbuntuOs();
        }
    }

    public static void fadOutInWindowsOs() {
        if (DataHolder.player != null) {
            String decreaseVolume = DataHolder.executionFolderPath + FileSystems.getDefault().getSeparator() + "nircmd.exe changesysvolume -5000";
            String increaseVolume = DataHolder.executionFolderPath + FileSystems.getDefault().getSeparator() + "nircmd.exe changesysvolume 45000";
            try {
                for (int i = 0; i < 10; i++) {
                    try {
                        Runtime.getRuntime().exec(decreaseVolume);
                        Thread.sleep(150L);
                    } catch (Exception ex) {
                        NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
                    }
                }
                NeptuneLogger.getInstance().getLogger().log(Level.INFO, "Volume fade out completed.");
            } catch (Exception ex) {
                NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
            } finally {
                try {
                    StreamPlayer.stop();
                } catch (Exception ex) {
                    NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        Runtime.getRuntime().exec(increaseVolume);
                        NeptuneLogger.getInstance().getLogger().log(Level.INFO, "Volume increased...");
                    } catch (IOException ex) {
                        Logger.getLogger(StreamPlayer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    public static void fadeOutInUbuntuOs() {
        if (DataHolder.player != null) {
            String volume = "100%";
            try {
                try {
                    Runtime rt = Runtime.getRuntime();
                    Process proc = rt.exec("amixer get Master | awk '$0~/%/{print $4}' | tr -d '[]'");
                    InputStream stdin = proc.getInputStream();
                    InputStreamReader isr = new InputStreamReader(stdin);
                    BufferedReader br = new BufferedReader(isr);
                    String line = null;
                    br.readLine();
                    br.readLine();
                    br.readLine();
                    br.readLine();
                    String op = br.readLine();
                    volume = String.valueOf(op.subSequence(op.indexOf('[') + 1, op.indexOf(']')));
                    int exitVal = proc.waitFor();
                } catch (Exception ex) {
                    Logger.getLogger(StreamPlayer.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    Runtime.getRuntime().exec("amixer sset Master 15%-,15%-");
                } catch (Exception ex) {
                    NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
                }
                Thread.sleep(300L);
                try {
                    Runtime.getRuntime().exec("amixer sset Master 15%-,15%-");
                } catch (Exception ex) {
                    NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
                }
                Thread.sleep(300L);
                try {
                    Runtime.getRuntime().exec("amixer sset Master 10%-,10%-");
                } catch (Exception ex) {
                    NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
                }
                Thread.sleep(300L);
                try {
                    Runtime.getRuntime().exec("amixer sset Master 10%-,10%-");
                } catch (Exception ex) {
                    NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
                }
                Thread.sleep(300L);
                try {
                    Runtime.getRuntime().exec("amixer sset Master 10%-,10%-");
                } catch (Exception ex) {
                    NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
                }
                Thread.sleep(300L);
                try {
                    Runtime.getRuntime().exec("amixer sset Master 10%-,10%-");
                } catch (Exception ex) {
                    NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
                }
                Thread.sleep(300L);
                try {
                    Runtime.getRuntime().exec("amixer sset Master 10%-,10%-");
                } catch (Exception ex) {
                    NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
                }
                NeptuneLogger.getInstance().getLogger().log(Level.INFO, "Volume fade out completed.");
            } catch (Exception ex) {
                NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
            } finally {
                try {
                    StreamPlayer.stop();
                } catch (Exception ex) {
                    NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        Runtime.getRuntime().exec("amixer sset Master " + volume);
                        NeptuneLogger.getInstance().getLogger().log(Level.INFO, "Volume increased...");
                    } catch (IOException ex) {
                        Logger.getLogger(StreamPlayer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    public static void stop() {
        System.out.println("STOP..!!!");
        try {
            if (DataHolder.player != null) {
                DataHolder.player.close();
                DataHolder.player = null;
                DataHolder.currentPlayerStatus = "STOP";
                DataHolder.isComplete = true;
                if (DataHolder.fis != null) {
                    DataHolder.fis.close();
                }
                if (DataHolder.bis != null) {
                    DataHolder.bis.close();
                }
                DataHolder.fis = null;
                DataHolder.bis = null;
                if (DataHolder.currentStation == null) {
                    DataHolder.playerStatusLabel.setText("Searching...");
                }
            }
        } catch (Exception ex) {
            NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
        }
    }

    public static void resumePlayback() {
        try {
            DataHolder.fis = new FileInputStream(new File(DataHolder.currentTrackPath));
            DataHolder.bis = new BufferedInputStream(DataHolder.fis);
            DataHolder.player = new AdvancedPlayer(DataHolder.bis);
//            DataHolder.fis.skip(DataHolder.currentTrackLength - DataHolder.currentTrackPauseLength);

            DataHolder.playbackThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        NeptuneLogger.getInstance().getLogger().log(Level.INFO, "Resuming at : {0}", DataHolder.pausedOnFrame);
                        DataHolder.player.play();

                    } catch (Exception e) {
                        NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, e);
                    }
                }
            });
            DataHolder.playbackThread.start();
            DataHolder.isPlaying = true;
            DataHolder.isPause = false;
        } catch (FileNotFoundException ex) {
            NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
        } catch (JavaLayerException ex) {
            NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
        }
    }

    public static boolean loadCurrentProgramme(Date currentDate, Date currentTime) {
        if (DataHolder.media == null) {
            NeptuneLogger.getInstance().getLogger().log(Level.INFO, "Media is null....so Program cannot be loaded.");
            DataHolder.playerStatusLabel.setText("Unable to update.");
            return false;
        }

        for (Media.Channels.Station station : DataHolder.media.getChannels().getStation()) {
            if (!station.getName().trim().equalsIgnoreCase("OnDemandStation")) {
                DataHolder.currentStation = station;
                fetchNextProgram(DataHolder.currentStation, currentTime);
                //temporary flag for identifying loops
                boolean loopOverFlag = Boolean.FALSE;
//                identify if any daily programme is available.
                if (!loopOverFlag) {
                    for (Media.Channels.Station.Programme programme : station.getProgramme()) {
                        if (DateUtil.getInstance().isCurrentDateInBetween(DateUtil.getInstance().getDate(programme.getStarttime(),
                                DateUtil.DATE_FORMAT_NEPTUNE),
                                DateUtil.getInstance().getDate(programme.getEndtime(),
                                        DateUtil.DATE_FORMAT_NEPTUNE))
                                && DateUtil.getInstance().isDailyProgramForToday(programme.getRepeat(), programme.getStarttime())) {//is this defaultProgram eligible?
                            loopOverFlag = Boolean.TRUE;
                            NeptuneLogger.getInstance().getLogger().log(Level.INFO, "Program selected : {0}", programme.getName());
                            DataHolder.currentProgramme = programme;
                            DataHolder.programmeLabel.setText(DataHolder.currentProgramme.getName());
                            int count = 0;
                            if ("no".equalsIgnoreCase(DataHolder.currentProgramme.getShuffle())) {
                                for (Media.Channels.Station.Programme.Playlist.Track track : DataHolder.currentProgramme.getPlaylist().getTrack()) {
                                    count++;
//                            Date startTime = DateUtil.getInstance().toDate(track.getStarttime());
//                            Date endTime = DateUtil.getInstance().toDate(track.getEndtime());
                                    if (DateUtil.getInstance().isCurrentTimeInBetween(DateUtil.getInstance().getDate(track.getStarttime(), DateUtil.getInstance().DATE_FORMAT_HH_MM_SS),
                                            DateUtil.getInstance().getDate(track.getEndtime(), DateUtil.getInstance().DATE_FORMAT_HH_MM_SS))) {//is this track eligible?
                                        int trackSelected = 0;
                                        switch (EndtimeRuleEnum.valueOf(DataHolder.currentProgramme.getEndtimerule().toUpperCase())) {
                                            case EARLY:
                                                //when will the track end if it starts playing now
                                                Date trackEndTime = DateUtil.getInstance().addTime(DateUtil.getInstance().formatDate(
                                                        DateUtil.getInstance().toDate(track.getLength()), DATE_FORMAT_HH_MM_SS));

                                                Date trackLength = DateUtil.getInstance().toDate(track.getLength());
                                                Date programmeEndDate = DateUtil.getInstance().getDate(DataHolder.currentProgramme.getEndtime(), DATE_FORMAT_NEPTUNE);
                                                programmeEndDate = DateUtil.getInstance().getDate(DateUtil.getInstance().formatDate(programmeEndDate, DATE_FORMAT_HH_MM_SS), DATE_FORMAT_HH_MM_SS);
                                                //check if the endtime of the track is after defaultProgram endtime
                                                if (null != DataHolder.nextTrackTimeForProgrammesWithEndtimeruleAsExactly
                                                        && 0 == currentTime.compareTo(DataHolder.nextTrackTimeForProgrammesWithEndtimeruleAsExactly)) {
                                                    DataHolder.currentTrackPath = DataHolder.archiveFolderPath + FileSystems.getDefault().getSeparator() + DataHolder.nextTrack.getName();
                                                    DataHolder.currentProgramme = DataHolder.currentProgramme;
                                                    DataHolder.currenttrack = DataHolder.nextTrack;
                                                    DataHolder.currentStation = station;
                                                    DataHolder.stationLabel.setText(DataHolder.currentStation.getName());
                                                    DataHolder.nextTrackLabel.setText(getNextTrackFromThisProgram(DataHolder.currentProgramme, DataHolder.nextTrack).equalsIgnoreCase("unavailable") ? "Unavailable" : DataHolder.nextTrack.getTitle());
                                                    trackSelected = 1;
                                                    DataHolder.isDynamicTenmplate = false;
                                                } else if (!trackEndTime.after(programmeEndDate)) {
                                                    DataHolder.currentTrackPath = DataHolder.archiveFolderPath + FileSystems.getDefault().getSeparator() + track.getName();
                                                    DataHolder.currentProgramme = DataHolder.currentProgramme;
                                                    DataHolder.currenttrack = track;
                                                    DataHolder.currentStation = station;
                                                    DataHolder.nextTrackLabel.setText(getNextTrackFromThisProgram(DataHolder.currentProgramme, track).equalsIgnoreCase("unavailable") ? "Unavailable" : DataHolder.nextTrack.getTitle());
                                                    NeptuneLogger.getInstance().getLogger().log(Level.INFO, "Selected track : {0}", DataHolder.currentTrackPath);
                                                    trackSelected = 1;
                                                    DataHolder.isDynamicTenmplate = false;
                                                }
                                                break;
                                            case LATE:
                                            case EXACTLY:
                                                //no problem even if the last track finish playing after the defaultProgram endtime
                                                NeptuneLogger.getInstance().getLogger().log(Level.INFO, "Next Track time : " + DataHolder.nextTrackTimeForProgrammesWithEndtimeruleAsExactly);
                                                if (null != DataHolder.nextTrackTimeForProgrammesWithEndtimeruleAsExactly
                                                        && 0 == currentTime.compareTo(DataHolder.nextTrackTimeForProgrammesWithEndtimeruleAsExactly)) {
                                                    DataHolder.currentTrackPath = DataHolder.archiveFolderPath + FileSystems.getDefault().getSeparator() + DataHolder.nextTrack.getName();
                                                    DataHolder.currentProgramme = DataHolder.currentProgramme;
                                                    DataHolder.currenttrack = DataHolder.nextTrack;
                                                    DataHolder.currentStation = station;
                                                    DataHolder.stationLabel.setText(DataHolder.currentStation.getName());
                                                    DataHolder.nextTrackLabel.setText(getNextTrackFromThisProgram(DataHolder.currentProgramme, DataHolder.nextTrack).equalsIgnoreCase("unavailable") ? "Unavailable" : DataHolder.nextTrack.getTitle());
                                                    trackSelected = 1;
                                                } else {
                                                    DataHolder.currentTrackPath = DataHolder.archiveFolderPath + FileSystems.getDefault().getSeparator() + track.getName();
                                                    DataHolder.currentProgramme = DataHolder.currentProgramme;
                                                    DataHolder.currenttrack = track;
                                                    DataHolder.currentStation = station;
                                                    DataHolder.nextTrackLabel.setText(getNextTrackFromThisProgram(DataHolder.currentProgramme, track).equalsIgnoreCase("unavailable") ? "Unavailable" : DataHolder.nextTrack.getTitle());
                                                    NeptuneLogger.getInstance().getLogger().log(Level.INFO, "Selected track : {0}", DataHolder.currentTrackPath);
                                                    trackSelected = 1;
                                                }
                                                break;
                                        }
                                        if (trackSelected == 1) {
                                            DataHolder.stationLabel.setText(DataHolder.currentStation.getName());
                                            return true;//if an eligible track is found
                                        } else if (trackSelected == 0 && (DataHolder.currentProgramme.getEndtimerule().equalsIgnoreCase(EndtimeRuleEnum.EARLY.toString()))) {
                                            return false;//if no eligible track is found
                                        }
                                    }
                                }
                            }

                            //repeat option with shuffle logic 
                            //todo rewrite the logic here
                            if (DataHolder.currentProgramme.getPlaylist().getTrack().size() > 0) {
                                if (DataHolder.nextTrack != null) {
                                    if (!DataHolder.nextTrack.getName().equalsIgnoreCase("unavailable")) {
                                        DataHolder.currenttrack = DataHolder.nextTrack;
                                    }
                                } else {
                                    DataHolder.currenttrack = fetchNextTrack(DataHolder.currentProgramme);
                                }
                                DataHolder.nextTrack = fetchNextTrack(DataHolder.currentProgramme);
                                while (DataHolder.nextTrack.getName().equalsIgnoreCase(DataHolder.currenttrack.getName())) {
                                    DataHolder.nextTrack = fetchNextTrack(DataHolder.currentProgramme);
                                }
//                            System.out.println("@@@@@@@@@Next Track :" + DataHolder.nextTrack.getName());
                                DataHolder.currentTrackPath = DataHolder.archiveFolderPath + FileSystems.getDefault().getSeparator() + DataHolder.currenttrack.getName();
                                DataHolder.nextTrackPath = DataHolder.archiveFolderPath + FileSystems.getDefault().getSeparator() + DataHolder.nextTrack.getName();
                                DataHolder.currentTrackLabel.setText(DataHolder.currenttrack.getTitle().isEmpty() ? DataHolder.currenttrack.getName() : DataHolder.currenttrack.getTitle());
                                DataHolder.nextTrackLabel.setText(DataHolder.nextTrack.getTitle().isEmpty() ? DataHolder.nextTrack.getName() : DataHolder.nextTrack.getTitle());
                                DataHolder.currentProgramme = DataHolder.currentProgramme;
                                DataHolder.isDynamicTenmplate = true;
                                NeptuneLogger.getInstance().getLogger().log(Level.INFO, "Selected track : {0}", DataHolder.currentTrackPath);
                                return true;

                            }
                            DataHolder.currentProgramme = null;
                            DataHolder.currenttrack = null;
                        }
                    }
                }
                if (!loopOverFlag) {
                    for (Media.Channels.Station.Programme programme : station.getProgramme()) {
                        if (DateUtil.getInstance().isCurrentDateInBetween(DateUtil.getInstance().getDate(programme.getStarttime(),
                                DateUtil.DATE_FORMAT_NEPTUNE),
                                DateUtil.getInstance().getDate(programme.getEndtime(),
                                        DateUtil.DATE_FORMAT_NEPTUNE))
                                && DateUtil.getInstance().isRepeat(programme.getRepeat(), programme.getStarttime())) {//is this defaultProgram eligible?
                            loopOverFlag = Boolean.TRUE;
                            NeptuneLogger.getInstance().getLogger().log(Level.INFO, "Program selected : {0}", programme.getName());
                            DataHolder.currentProgramme = programme;
                            DataHolder.programmeLabel.setText(DataHolder.currentProgramme.getName());
                            int count = 0;
                            if ("no".equalsIgnoreCase(DataHolder.currentProgramme.getShuffle())) {
                                for (Media.Channels.Station.Programme.Playlist.Track track : DataHolder.currentProgramme.getPlaylist().getTrack()) {
                                    count++;
//                            Date startTime = DateUtil.getInstance().toDate(track.getStarttime());
//                            Date endTime = DateUtil.getInstance().toDate(track.getEndtime());
                                    if (DateUtil.getInstance().isCurrentTimeInBetween(DateUtil.getInstance().getDate(track.getStarttime(), DateUtil.getInstance().DATE_FORMAT_HH_MM_SS),
                                            DateUtil.getInstance().getDate(track.getEndtime(), DateUtil.getInstance().DATE_FORMAT_HH_MM_SS))) {//is this track eligible?
                                        int trackSelected = 0;
                                        switch (EndtimeRuleEnum.valueOf(DataHolder.currentProgramme.getEndtimerule().toUpperCase())) {
                                            case EARLY:
                                                //when will the track end if it starts playing now
                                                Date trackEndTime = DateUtil.getInstance().addTime(DateUtil.getInstance().formatDate(
                                                        DateUtil.getInstance().toDate(track.getLength()), DATE_FORMAT_HH_MM_SS));

                                                Date trackLength = DateUtil.getInstance().toDate(track.getLength());
                                                Date programmeEndDate = DateUtil.getInstance().getDate(DataHolder.currentProgramme.getEndtime(), DATE_FORMAT_NEPTUNE);
                                                programmeEndDate = DateUtil.getInstance().getDate(DateUtil.getInstance().formatDate(programmeEndDate, DATE_FORMAT_HH_MM_SS), DATE_FORMAT_HH_MM_SS);
                                                //check if the endtime of the track is after defaultProgram endtime
                                                if (null != DataHolder.nextTrackTimeForProgrammesWithEndtimeruleAsExactly
                                                        && 0 == currentTime.compareTo(DataHolder.nextTrackTimeForProgrammesWithEndtimeruleAsExactly)) {
                                                    DataHolder.currentTrackPath = DataHolder.archiveFolderPath + FileSystems.getDefault().getSeparator() + DataHolder.nextTrack.getName();
                                                    DataHolder.currentProgramme = DataHolder.currentProgramme;
                                                    DataHolder.currenttrack = DataHolder.nextTrack;
                                                    DataHolder.currentStation = station;
                                                    DataHolder.stationLabel.setText(DataHolder.currentStation.getName());
                                                    DataHolder.nextTrackLabel.setText(getNextTrackFromThisProgram(DataHolder.currentProgramme, DataHolder.nextTrack));
                                                    trackSelected = 1;
                                                    DataHolder.isDynamicTenmplate = false;
                                                } else if (!trackEndTime.after(programmeEndDate)) {
                                                    DataHolder.currentTrackPath = DataHolder.archiveFolderPath + FileSystems.getDefault().getSeparator() + track.getName();
                                                    DataHolder.currentProgramme = DataHolder.currentProgramme;
                                                    DataHolder.currenttrack = track;
                                                    DataHolder.currentStation = station;
                                                    DataHolder.nextTrackLabel.setText(getNextTrackFromThisProgram(DataHolder.currentProgramme, track).equalsIgnoreCase("unavailable") ? "Unavailable" : DataHolder.nextTrack.getTitle());
                                                    NeptuneLogger.getInstance().getLogger().log(Level.INFO, "Selected track : {0}", DataHolder.currentTrackPath);
                                                    trackSelected = 1;
                                                    DataHolder.isDynamicTenmplate = false;
                                                }
                                                break;
                                            case LATE:
                                            case EXACTLY:
                                                //no problem even if the last track finish playing after the defaultProgram endtime
                                                NeptuneLogger.getInstance().getLogger().log(Level.INFO, "Next Track time : " + DataHolder.nextTrackTimeForProgrammesWithEndtimeruleAsExactly);
                                                if (null != DataHolder.nextTrackTimeForProgrammesWithEndtimeruleAsExactly
                                                        && 0 == currentTime.compareTo(DataHolder.nextTrackTimeForProgrammesWithEndtimeruleAsExactly)) {
                                                    DataHolder.currentTrackPath = DataHolder.archiveFolderPath + FileSystems.getDefault().getSeparator() + DataHolder.nextTrack.getName();
                                                    DataHolder.currentProgramme = DataHolder.currentProgramme;
                                                    DataHolder.currenttrack = DataHolder.nextTrack;
                                                    DataHolder.currentStation = station;
                                                    DataHolder.stationLabel.setText(DataHolder.currentStation.getName());
                                                    DataHolder.nextTrackLabel.setText(getNextTrackFromThisProgram(DataHolder.currentProgramme, DataHolder.nextTrack).equalsIgnoreCase("unavailable") ? "Unavailable" : DataHolder.nextTrack.getTitle());
                                                    trackSelected = 1;
                                                } else {
                                                    DataHolder.currentTrackPath = DataHolder.archiveFolderPath + FileSystems.getDefault().getSeparator() + track.getName();
                                                    DataHolder.currentProgramme = DataHolder.currentProgramme;
                                                    DataHolder.currenttrack = track;
                                                    DataHolder.currentStation = station;
                                                    DataHolder.nextTrackLabel.setText(getNextTrackFromThisProgram(DataHolder.currentProgramme, track).equalsIgnoreCase("unavailable") ? "Unavailable" : DataHolder.nextTrack.getTitle());
                                                    NeptuneLogger.getInstance().getLogger().log(Level.INFO, "Selected track : {0}", DataHolder.currentTrackPath);
                                                    trackSelected = 1;
                                                }
                                                break;
                                        }
                                        if (trackSelected == 1) {
                                            DataHolder.stationLabel.setText(DataHolder.currentStation.getName());
                                            return true;//if an eligible track is found
                                        } else if (trackSelected == 0 && (DataHolder.currentProgramme.getEndtimerule().equalsIgnoreCase(EndtimeRuleEnum.EARLY.toString()))) {
                                            return false;//if no eligible track is found
                                        }
                                    }
                                }
                            }

                            //repeat option with shuffle logic 
                            //todo rewrite the logic here
                            if (DataHolder.currentProgramme.getPlaylist().getTrack().size() > 0) {
                                if (DataHolder.nextTrack != null) {
                                    if (!DataHolder.nextTrack.getName().equalsIgnoreCase("unavailable")) {
                                        DataHolder.currenttrack = DataHolder.nextTrack;
                                    }
                                } else {
                                    DataHolder.currenttrack = fetchNextTrack(DataHolder.currentProgramme);
                                }
                                DataHolder.nextTrack = fetchNextTrack(DataHolder.currentProgramme);
                                while (DataHolder.nextTrack.getName().equalsIgnoreCase(DataHolder.currenttrack.getName())) {
                                    DataHolder.nextTrack = fetchNextTrack(DataHolder.currentProgramme);
                                }
//                            System.out.println("@@@@@@@@@Next Track :" + DataHolder.nextTrack.getName());
                                DataHolder.currentTrackPath = DataHolder.archiveFolderPath + FileSystems.getDefault().getSeparator() + DataHolder.currenttrack.getName();
                                DataHolder.nextTrackPath = DataHolder.archiveFolderPath + FileSystems.getDefault().getSeparator() + DataHolder.nextTrack.getName();
                                DataHolder.currentTrackLabel.setText(DataHolder.currenttrack.getTitle().isEmpty() ? DataHolder.currenttrack.getName() : DataHolder.currenttrack.getTitle());
                                DataHolder.nextTrackLabel.setText(DataHolder.nextTrack.getTitle().isEmpty() ? DataHolder.nextTrack.getName() : DataHolder.nextTrack.getTitle());
                                DataHolder.currentProgramme = DataHolder.currentProgramme;
                                DataHolder.isDynamicTenmplate = true;
                                NeptuneLogger.getInstance().getLogger().log(Level.INFO, "Selected track : {0}", DataHolder.currentTrackPath);
                                return true;

                            }
                            DataHolder.currentProgramme = null;
                            DataHolder.currenttrack = null;
                        }
                    }
                }
            }
        }
        NeptuneLogger.getInstance().getLogger().log(Level.INFO, "No Track Selected");
        DataHolder.currentProgramme = null;
        DataHolder.currenttrack = null;
        if (DataHolder.currentStation == null) {
            DataHolder.stationLabel.setText("Unavailable");
        } else {
            DataHolder.stationLabel.setText(DataHolder.currentStation.getName());
        }
        if (DataHolder.currentProgramme == null) {
            DataHolder.programmeLabel.setText("Unavailable");
        } else {
            DataHolder.programmeLabel.setText(DataHolder.currentProgramme.getName());
        }
        if (DataHolder.currenttrack == null) {
            DataHolder.currentTrackLabel.setText("Unavailable");
            DataHolder.nextTrackLabel.setText("Unavailable");
        } else {
            DataHolder.currentTrackLabel.setText(DataHolder.currenttrack.getTitle().isEmpty() ? DataHolder.currenttrack.getName() : DataHolder.currenttrack.getTitle());
            DataHolder.nextTrackLabel.setText(DataHolder.nextTrackPath);
        }
        return false;//no defaultProgram eligible in this station
    }

    //parse the updated xml and find if we do have a campaign that is eligible to be played @current time?
    public static boolean checkForCampaign(Date currentDate, Date currentTime) {
        if (DataHolder.initializationCompleted) {
            if (null == DataHolder.media) {
                NeptuneLogger.getInstance().getLogger().log(Level.INFO, "Media is Null....Unable to load campaigns...");
                return false;
            }
            for (Media.Channels.Station station : DataHolder.media.getChannels().getStation()) {
                if (!station.getName().trim().equalsIgnoreCase("OnDemandStation")) {
                    for (Media.Channels.Station.Schedule.Campaign campaign : station.getSchedule().getCampaign()) {
                        if (HelperUtility.checkIfCampaignIsEligible(campaign, currentTime)) {
                            switch (CampaignPlayOrder.valueOf(campaign.getPlayorder().toUpperCase())) {
                                case RANDOM:
                                    if (campaign.getTrack() != null) {
                                        if (campaign.getTrack().size() > 0) {
                                            Media.Channels.Station.Schedule.Campaign.Track campaignTrack
                                                    = campaign.getTrack().get(new Random().nextInt(campaign.getTrack().size()));
                                            setCampaignTrackAsCurrentTrack(campaignTrack);
                                            NeptuneLogger.getInstance().getLogger().log(Level.INFO, "Selected track : {0}", DataHolder.currentTrackPath);
                                            return true;
                                        }
                                    }
                                    break;
                                case SEQUENTIAL:
                                    if (campaign.getTrack() != null) {
                                        int sequenceNumber = HelperUtility.getSequenceFromTrackRepeat(campaign, currentTime);
                                        if (sequenceNumber > -1) {
                                            if (campaign.getTrack().size() > 0) {
                                                if (sequenceNumber > campaign.getTrack().size() - 1) {
                                                    Media.Channels.Station.Schedule.Campaign.Track campaignTrack
                                                            = campaign.getTrack().get(new Random().nextInt(campaign.getTrack().size()));
                                                    setCampaignTrackAsCurrentTrack(campaignTrack);
                                                } else {
                                                    Media.Channels.Station.Schedule.Campaign.Track campaignTrack
                                                            = campaign.getTrack().get(sequenceNumber);
                                                    setCampaignTrackAsCurrentTrack(campaignTrack);
                                                }
                                                NeptuneLogger.getInstance().getLogger().log(Level.INFO, "Selected track : {0}", DataHolder.currentTrackPath);
                                                return true;
                                            }
                                        }

                                    }
                                    break;

                            }

                        }
                    }
                }
            }
            NeptuneLogger.getInstance().getLogger().info("No Campaign available.");
            return false;
        } else {
            NeptuneLogger.getInstance().getLogger().info("Not initialized ");
            return false;
        }
    }

    private static void setCampaignTrackAsCurrentTrack(Station.Schedule.Campaign.Track campaignTrack) {
        Track track = new Track();
        track.setArtist(null);
        track.setEndtime(null);
        track.setGenre(null);
        track.setId(Long.valueOf(campaignTrack.getId()));
        track.setLength(null);
        track.setName(campaignTrack.getName());
        track.setStarttime(null);
        track.setSynced(null);
        track.setValue(campaignTrack.getValue());
        DataHolder.currentTrackPath = DataHolder.archiveFolderPath + FileSystems.getDefault().getSeparator() + campaignTrack.getName();
        DataHolder.currenttrack = track;
    }

    private static boolean loadOnDemandStationPrograms() {
        if (DataHolder.media == null) {
            NeptuneLogger.getInstance().getLogger().info("Media is null....so Program cannot be loaded.");
            return false;
        }
        for (Media.Channels.Station station : DataHolder.media.getChannels().getStation()) {
            if (station.getName().equalsIgnoreCase("OnDemandStation")) {
                NeptuneLogger.getInstance().getLogger().log(Level.INFO, "DataHolder.currentTrackNumber : {0}", DataHolder.currentTrackNumber);
                DataHolder.currentStation = station;
                DataHolder.stationLabel.setText(DataHolder.currentStation.getName());
//                DataHolder.currentProgramme = station.getProgramme().get(0);
                DataHolder.nextProgrammeLabel.setText("Unavailable");
                if (DataHolder.ondemandCategoryName != null) {
                    for (Programme programme : station.getProgramme()) {
                        if (programme.getName().equalsIgnoreCase(DataHolder.ondemandCategoryName)) {
                            DataHolder.currentProgramme = programme;
                            DataHolder.programmeLabel.setText(DataHolder.currentProgramme.getName());
                        }
                    }
                } else {
                    DataHolder.currentProgramme = null;
                    DataHolder.programmeLabel.setText("Select Category");
                }

                if (DataHolder.currentProgramme == null) {
                    return false;
                }
                if (DataHolder.currentProgramme.getPlaylist().getTrack().size() == 0) {
                    return false;
                }

                DataHolder.onDemandTrackList.clear();
                DataHolder.onDemandTrackList.addAll(DataHolder.currentProgramme.getPlaylist().getTrack());
                if (DataHolder.nextTrack != null) {
                    if (!DataHolder.nextTrack.getName().equalsIgnoreCase("unavailable")) {
                        if (DataHolder.onDemandTrackList.contains(DataHolder.nextTrack)) {
                            DataHolder.currenttrack = DataHolder.nextTrack;
                        } else {
                            DataHolder.currenttrack = fetchNextTrack(DataHolder.currentProgramme);
                        }
                    }
                } else {
                    DataHolder.currenttrack = fetchNextTrack(DataHolder.currentProgramme);
                }
                DataHolder.nextTrack = fetchNextTrack(DataHolder.currentProgramme);
                if (null != DataHolder.nextTrack) {
                    while (DataHolder.nextTrack.getName().equalsIgnoreCase(DataHolder.currenttrack.getName()) && DataHolder.onDemandTrackList.size() > 1) {
                        DataHolder.nextTrack = fetchNextTrack(DataHolder.currentProgramme);
                    }
                }
                DataHolder.currentTrackPath = DataHolder.archiveFolderPath + FileSystems.getDefault().getSeparator() + DataHolder.currenttrack.getName();
                DataHolder.nextTrackPath = DataHolder.archiveFolderPath + FileSystems.getDefault().getSeparator() + DataHolder.nextTrack.getName();
                DataHolder.currentTrackLabel.setText(DataHolder.currenttrack.getTitle().isEmpty() ? DataHolder.currenttrack.getName() : DataHolder.currenttrack.getTitle());
                DataHolder.nextTrackLabel.setText(DataHolder.nextTrack.getTitle().isEmpty() ? DataHolder.nextTrack.getName() : DataHolder.nextTrack.getTitle());
                NeptuneLogger.getInstance().getLogger().log(Level.INFO, "Selected track : {0}", DataHolder.currentTrackPath);
                return true;

            }
        }
        NeptuneLogger.getInstance().getLogger().info("No Ondemand Station available for Host...");
        return false;
    }

    private static String getNextTrackFromThisProgram(Programme programme, Track track) {
        for (Track nextTrack : programme.getPlaylist().getTrack()) {
            if (nextTrack.getStarttime().equalsIgnoreCase(track.getEndtime())) {
                DataHolder.nextTrack = nextTrack;
                DataHolder.nextTrackPath = nextTrack.getName();
                switch (EndtimeRuleEnum.valueOf(programme.getEndtimerule().toUpperCase())) {
                    case EXACTLY:
                    case EARLY:
                    case LATE:
                        DataHolder.nextTrackTimeForProgrammesWithEndtimeruleAsExactly = DateUtil.getInstance()
                                .getDate(DataHolder.nextTrack.getStarttime(), DATE_FORMAT_HH_MM_SS);
                        DataHolder.isStaticTrack = false;
//                        DataHolder.nextTrackTimeForProgrammesWithEndtimeruleAsExactly.setSeconds(DataHolder.nextTrackTimeForProgrammesWithEndtimeruleAsExactly.getSeconds() - 3);
//                        if (DataHolder.nextTrackTimeForProgrammesWithEndtimeruleAsExactly.getSeconds() < 0) {
//                            DataHolder.nextTrackTimeForProgrammesWithEndtimeruleAsExactly.setSeconds(0);
//                            if (DataHolder.nextTrackTimeForProgrammesWithEndtimeruleAsExactly.getMinutes() > 0) {
//                                DataHolder.nextTrackTimeForProgrammesWithEndtimeruleAsExactly.setMinutes(
//                                        DataHolder.nextTrackTimeForProgrammesWithEndtimeruleAsExactly.getMinutes() - 1);
//                            }
//                        }
                        break;
                }
                DataHolder.isStaticTrack = false;
                return nextTrack.getName();
            } else if (DateUtil.getInstance().getDate(nextTrack.getStarttime(), DATE_FORMAT_HH_MM_SS).
                    after(DateUtil.getInstance().getDate(track.getStarttime(), DATE_FORMAT_HH_MM_SS))) {
                if (DateUtil.getInstance().getDate(nextTrack.getStarttime(), DATE_FORMAT_HH_MM_SS).
                        before(DateUtil.getInstance().getDate(track.getEndtime(), DATE_FORMAT_HH_MM_SS))) {
                    DataHolder.nextTrackTimeForProgrammesWithEndtimeruleAsExactly = DateUtil.getInstance()
                            .getDate(nextTrack.getStarttime(), DATE_FORMAT_HH_MM_SS);
                    DataHolder.nextTrackTimeForProgrammesWithEndtimeruleAsExactly.setSeconds(DataHolder.nextTrackTimeForProgrammesWithEndtimeruleAsExactly.getSeconds() - 3);
                    if (DataHolder.nextTrackTimeForProgrammesWithEndtimeruleAsExactly.getSeconds() < 0) {
                        DataHolder.nextTrackTimeForProgrammesWithEndtimeruleAsExactly.setSeconds(0);
                        if (DataHolder.nextTrackTimeForProgrammesWithEndtimeruleAsExactly.getMinutes() > 0) {
                            DataHolder.nextTrackTimeForProgrammesWithEndtimeruleAsExactly.setMinutes(
                                    DataHolder.nextTrackTimeForProgrammesWithEndtimeruleAsExactly.getMinutes() - 1);
                        }
                    }
                    DataHolder.nextTrack = nextTrack;
                    DataHolder.isStaticTrack = true;
                    return nextTrack.getName();
                }
            }
        }
        DataHolder.nextTrackPath = "Unavailable";
        DataHolder.nextTrackTimeForProgrammesWithEndtimeruleAsExactly = null;
        return "Unavailable";
    }

    public static Station getNotOndemandStation() {
        if (DataHolder.media != null) {
            if (DataHolder.media.getChannels() != null) {
                if (DataHolder.media.getChannels().getStation() != null) {
                    for (Station station : DataHolder.media.getChannels().getStation()) {
                        if (!station.getName().trim().equalsIgnoreCase("OnDemandStation")) {
                            return station;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static Station getOndemandStation() {
        for (Media.Channels.Station station : DataHolder.media.getChannels().getStation()) {
            if (station.getName().trim().equalsIgnoreCase("OnDemandStation")) {
                return station;
            }
        }
        return null;
    }

    public static Programme getOndemandStationProgramme(Station station) {
        if (null != DataHolder.ondemandCategories) {
            if (DataHolder.ondemandCategories.getSelectedRow() > -1 && DataHolder.ondemandCategories.getSelectedColumn() > -1) {
                if (null != DataHolder.ondemandCategories.getValueAt(
                        DataHolder.ondemandCategories.getSelectedRow(), DataHolder.ondemandCategories.getSelectedColumn())) {
                    for (Programme programme : station.getProgramme()) {
                        if (programme.getName().equalsIgnoreCase(
                                DataHolder.ondemandCategories.getValueAt(
                                        DataHolder.ondemandCategories.getSelectedRow(),
                                        DataHolder.ondemandCategories.getSelectedColumn()).toString())) {
                            return programme;
                        }
                    }
                }
            }
        }
        if (station.getProgramme().size() > 0) {
            return station.getProgramme().get(0);
        }
        return null;
    }

    public static Track fetchNextTrack(Programme programme) {
        Random r = new Random();
        if (programme.getPlaylist().getTrack().size() == 0) {
            return null;
        }
        return programme.getPlaylist().getTrack().get(r.nextInt(programme.getPlaylist().getTrack().size()));
    }

    private static void fetchNextProgram(Station currentStation, Date currentTime) {
        List<Programme> programmes = new ArrayList<Programme>();
        programmes.addAll(currentStation.getProgramme());
        Collections.sort(programmes, new ProgrammeStartdateComparator());

        for (Programme programme : programmes) {
            Date programmeStartDate = DateUtil.getInstance().getDate(programme.getStarttime(), DATE_FORMAT_NEPTUNE);
            programmeStartDate = DateUtil.getInstance().getDate(DateUtil.getInstance().
                    formatDate(programmeStartDate, DATE_FORMAT_HH_MM_SS), DATE_FORMAT_HH_MM_SS);
            if (DateUtil.getInstance().isRepeat(programme.getRepeat(), programme.getStarttime())) {
                if (programmeStartDate.after(currentTime)) {
                    String nextTrackLabel = programme.getName() + " at " + DateUtil.getInstance().formatDate(programmeStartDate, DATE_FORMAT_HH_MM_SS);
                    DataHolder.nextProgrammeLabel.setText(nextTrackLabel);
                    return;
                }
            }
        }
        DataHolder.nextProgrammeLabel.setText("Unavailable");
    }

}
