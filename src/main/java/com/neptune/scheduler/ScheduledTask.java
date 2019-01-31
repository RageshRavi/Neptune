/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptune.scheduler;

import com.neptune.common.DataHolder;
import com.neptune.util.DateUtil;
import com.neptune.util.HelperUtility;
import com.neptune.util.NeptuneLogger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.xml.bind.JAXBException;

/**
 *
 * @author ragesh.raveendran
 */
public class ScheduledTask extends TimerTask implements Runnable {

    @Override
    public void run() {
        HelperUtility helperUtility = new HelperUtility();
        helperUtility.getPartnerLogo();
        try {
            NeptuneLogger.getInstance().getLogger().log(Level.INFO, "#################Updating#################");
            if (DataHolder.initializationCompleted) {
                DataHolder.updateInProgress = true;
            }
            //check internet connectivity
            try {
                if (HelperUtility.checkInternetConnectivity()) {
                    DataHolder.connectivity.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/wifion.png")));
                    DataHolder.connectivity.setToolTipText("Internet connected");
                } else {
                    DataHolder.connectivity.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/wifioff.png")));
                    DataHolder.connectivity.setToolTipText("No Internet connection");
                }
            } catch (Exception ex) {
                NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
            }

            //check for player updates
            try {
                StringBuilder updateUrl = new StringBuilder(DataHolder.httpURL + "/neptune/sync/GetPlayerVersion.ashx?UserName="
                        + DataHolder.configProperties.getProperty("userName").replace(" ", "%20")
                        + "&DeviceToken=" + DataHolder.configProperties.getProperty("DeviceToken").replace(" ", "%20"));
                if (!"".equalsIgnoreCase(DataHolder.macAddress)) {
                    updateUrl.append("&MacId=" + DataHolder.macAddress);
                }
                String updateAvailable = helperUtility.getUserMediaUpdates(updateUrl.toString());
                if (updateAvailable != null) {
                    if (!"no".equalsIgnoreCase(updateAvailable)) {
                        DataHolder.isUpdateAvilable = true;
                        DataHolder.updateLogo.setToolTipText("Click here to update.");
                    } else {
                        DataHolder.isUpdateAvilable = false;
                    }
                } else {
                    DataHolder.isUpdateAvilable = false;
                }
            } catch (Exception e) {
                NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, "Exception", e);
            }

            //Synch Media for current Partner
            NeptuneLogger.getInstance().getLogger().info("Started updation....");
            System.gc();
            StringBuilder url = new StringBuilder(DataHolder.httpURL + "/neptune/sync/SyncChannels.ashx?UserName="
                    + DataHolder.configProperties.getProperty("userName").replace(" ", "%20")
                    + "&SystemName=" + DataHolder.configProperties.getProperty("SystemName").replace(" ", "%20")
                    + "&DeviceToken=" + DataHolder.configProperties.getProperty("DeviceToken").replace(" ", "%20")
                    + "&CurrentDate=" + DateUtil.getInstance().formatDate(new Date(), DateUtil.DATE_FORMAT_YYYY_MM_DD));
            if (!"".equalsIgnoreCase(DataHolder.macAddress)) {
                url.append("&MacId=" + DataHolder.macAddress);
            }
            NeptuneLogger.getInstance().getLogger().log(Level.INFO, "Url - " + url);
            String updatedXml = helperUtility.getUserMediaUpdates(url.toString());
            if (updatedXml != null) {
                try {
                    if (updatedXml.substring(0, ((updatedXml.length() < 6) ? updatedXml.length() : 5)).trim().equalsIgnoreCase("<?xml")) {
                        FileOutputStream fos;
                        try {
                            fos = new FileOutputStream(DataHolder.contentSyncFile);
                            fos.write(updatedXml.getBytes());
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(ScheduledTask.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(ScheduledTask.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    helperUtility.createMediaObjectFromXmlString(updatedXml);
                } catch (JAXBException ex) {
                    Logger.getLogger(ScheduledTask.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {//load from saved content.synch file from disk to create media
                File contentFile = new File(DataHolder.contentSyncFile);
                if (contentFile.exists()) {
                    NeptuneLogger.getInstance().getLogger().log(Level.INFO, "Loading File from disk");
                    try {
                        helperUtility.createMediaObjectFromXmlString(contentFile);
                    } catch (JAXBException ex) {
                        Logger.getLogger(ScheduledTask.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    NeptuneLogger.getInstance().getLogger().log(Level.INFO, "File does not exist : ");
                }
                contentFile = null;

            }

            if (!DataHolder.initializationCompleted) {
                DataHolder.initializationCompleted = true;
                DataHolder.reloadPlaylist.setIcon(new ImageIcon(this.getClass().getResource("/images/reloadplaylist.jpg")));
            }
            try {
                DataHolder.trackList = helperUtility.getTracksLisFromUpdatedMedia();
                helperUtility.getOnDemandTrackListFromUpdatedMediaUpgraded(false);
                HelperUtility.downloadTracksFromFtp(DataHolder.trackList);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (DataHolder.initializationCompleted) {
                DataHolder.updateInProgress = true;
            }

            helperUtility = null;
            
            NeptuneLogger.getInstance().getLogger().info("Finished");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!DataHolder.reloadPlaylist.isVisible()) {
                DataHolder.reloadPlaylist.setVisible(true);
            }
        }
    }

}
