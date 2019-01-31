/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptune.scheduler;

import com.neptune.common.DataHolder;
import com.neptune.common.EndtimeRuleEnum;
import com.neptune.common.OdpEditEnum;
import com.neptune.player.StreamPlayer;
import com.neptune.util.DateUtil;
import static com.neptune.util.DateUtil.DATE_FORMAT_HH_MM_SS;
import static com.neptune.util.DateUtil.DATE_FORMAT_NEPTUNE;
import com.neptune.util.NeptuneLogger;
import java.util.Date;
import java.util.TimerTask;
import java.util.logging.Level;

/**
 *
 * @author ragesh.raveendran
 */
public class PlayBackMonitor extends TimerTask implements Runnable {

    @Override
    public void run() {
        try {
            if (DataHolder.isUpdateAvilable) {
                if (DataHolder.updateLogo.getIcon() == null) {
                    DataHolder.updateLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/neptuneupdate1.png")));
                } else {
                    DataHolder.updateLogo.setIcon(null);
                }
            }
        } catch (Exception e) {
            NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, "Exception in updating Logo", e);
        }

        try {
            Date currentTime = DateUtil.getInstance().getDate(DateUtil.getInstance().formatDate(new Date(), DATE_FORMAT_HH_MM_SS), DATE_FORMAT_HH_MM_SS);
            Date currentDate = DateUtil.getInstance().getDate(DateUtil.getInstance().formatDate(new Date(), DATE_FORMAT_NEPTUNE), DATE_FORMAT_NEPTUNE);
            //check if any campaign should run
            if (DataHolder.isOdpPlaylistPlaying
                    && !DataHolder.isComplete
                    && !DataHolder.currentTrackLabel.getText().equalsIgnoreCase("Track Unavailable in local")) {
                setOdpMaxtimeCounter();
            }

            if (DataHolder.currentStation != null) {
                if (!DataHolder.currentStation.getName().equalsIgnoreCase("ondemandstation")) {
                    playbackMonitorForOtherStation(currentDate, currentTime);
                } else if (DataHolder.player == null && DataHolder.onDemandOverride) {
                    switch (OdpEditEnum.valueOf(DataHolder.odpModeStatus.toUpperCase())) {
                        case LIST:
                            if (DataHolder.isOdpPlaylistPlaying) {
                                StreamPlayer.playWhenOdpPlaylistIsOn();
                            }
                            break;
                        default:
                            StreamPlayer.playWhenOndemandOverrideIsOn();
                            break;
                    }
                }
            } else {
                DataHolder.currentStation = StreamPlayer.getNotOndemandStation();
                if (DataHolder.currentStation == null) {
                    DataHolder.stationLabel.setText("Unavailable");
                    DataHolder.playerStatusLabel.setText("No Program available");
                    DataHolder.currentProgramme = null;
                    DataHolder.programmeLabel.setText("Unavailable");
                }
                DataHolder.currenttrack = null;
                DataHolder.currentTrackPath = null;
                DataHolder.currentTrackLabel.setText("Unavailable");
                DataHolder.nextTrackLabel.setText("Unavailable");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playbackMonitorForOtherStation(Date currentDate, Date currentTime) {
        NeptuneLogger.getInstance().getLogger().log(Level.INFO, "Next track time : " + DataHolder.nextTrackTimeForProgrammesWithEndtimeruleAsExactly);
        NeptuneLogger.getInstance().getLogger().log(Level.INFO, "Current time : " + currentTime);

        if (isTrackEndruleTimeSatisfied(currentTime)) {
            if (null != DataHolder.currentProgramme) {
                switch (EndtimeRuleEnum.valueOf(DataHolder.currentProgramme.getEndtimerule().toUpperCase())) {
                    case EARLY:
                        if (null != DataHolder.nextTrack) {
                            Date trackEndTime = DateUtil.getInstance().addTime(DateUtil.getInstance().formatDate(
                                    DateUtil.getInstance().toDate(DataHolder.nextTrack.getLength()), DATE_FORMAT_HH_MM_SS));
                            Date programmeEndDate = DateUtil.getInstance().getDate(DataHolder.currentProgramme.getEndtime(), DATE_FORMAT_NEPTUNE);
                            if (!trackEndTime.after(programmeEndDate)) {
                                if (DataHolder.isStaticTrack) {
                                    StreamPlayer.fadeOut();
                                } else {
                                    StreamPlayer.stop();
                                }
                                StreamPlayer.playback(false, currentDate, currentTime);
                            }
                        }
                        break;
                    case LATE:
                    case EXACTLY:
                        if (DataHolder.isStaticTrack) {
                            StreamPlayer.fadeOut();
                        } else {
                            StreamPlayer.stop();
                        }
                        StreamPlayer.playback(false, currentDate, currentTime);
                        break;
                }
            }
        } else if (null!= DataHolder.nextDailyProgrammeTime && 0 == currentTime.compareTo(DataHolder.nextDailyProgrammeTime)) {
            DataHolder.nextDailyProgrammeTime = null;
            StreamPlayer.fadeOut();
            StreamPlayer.playback(false, currentDate, currentTime);
        } else if (DataHolder.player != null) {
            if (DataHolder.isComplete || isProgramEndruleTimeSatisfied(currentTime)) {
                StreamPlayer.stop();
                StreamPlayer.playback(false, currentDate, currentTime);
            }
        } else {
            if (DataHolder.initializationCompleted) {
                DataHolder.downloadFileDetails.setText("");
                if (!DataHolder.playerStatusLabel.getText().equalsIgnoreCase("No Program to play...")) {
                    DataHolder.playerStatusLabel.setText("Searching...");
                }
                StreamPlayer.playback(false, currentDate, currentTime);
            }
        }
    }

    private boolean isProgramEndruleTimeSatisfied(Date currentTime) {
        int eligible = 1;
        try {
            if (DataHolder.currentStation != null) {
                if (!DataHolder.currentStation.getName().equalsIgnoreCase("OnDemandStation")) {
                    if (DataHolder.currentProgramme != null) {
                        if (!DataHolder.currentProgramme.getName().equalsIgnoreCase("Campaign")) {
                            Date programmeEndDate = DateUtil.getInstance().getDate(DataHolder.currentProgramme.getEndtime(), DATE_FORMAT_NEPTUNE);
                            programmeEndDate = DateUtil.getInstance().getDate(DateUtil.getInstance().formatDate(programmeEndDate, DATE_FORMAT_HH_MM_SS), DATE_FORMAT_HH_MM_SS);
                            switch (EndtimeRuleEnum.valueOf(DataHolder.currentProgramme.getEndtimerule().toUpperCase())) {
                                case EXACTLY:
                                    eligible = currentTime.compareTo(programmeEndDate);
                                    break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            NeptuneLogger.getInstance().getLogger().log(Level.INFO, null, e);
        }
        return eligible == 0;
    }

    private boolean isTrackEndruleTimeSatisfied(Date currentTime) {
        int eligible = 1;
        try {
            if (null != DataHolder.currentProgramme) {
                if (null != DataHolder.nextTrackTimeForProgrammesWithEndtimeruleAsExactly) {
                    eligible = currentTime.compareTo(DataHolder.nextTrackTimeForProgrammesWithEndtimeruleAsExactly);
                }
            }
        } catch (Exception e) {
            NeptuneLogger.getInstance().getLogger().log(Level.INFO, null, e);
        }
        return eligible == 0;
    }

    private void setOdpMaxtimeCounter() {
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


        if (maxSec > 0) {
            --maxSec;
        } else {
            maxSec = 59;
            if (maxMin > 0) {
                --maxMin;
            } else{
                maxMin = 59;
                if(maxHour > 0){
                    --maxHour;
                }
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
    }

}
