/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptune.controller;

import com.neptune.widgetutils.PlayerForm;
import com.neptune.common.DataHolder;
import static com.neptune.common.DataHolder.propFileName;
import com.neptune.scheduler.PlayBackMonitor;
import com.neptune.scheduler.ScheduledTask;
import com.neptune.util.HelperUtility;
import com.neptune.util.NeptuneLogger;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;
import java.util.Timer;
import java.util.logging.Level;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author ragesh.raveendran
 */
public class ParentController {

    public ParentController() {

    }

    public static void main(String args[]) {
        DataHolder.initialize();
        HelperUtility helperUtility = new HelperUtility();
        while (!DataHolder.isRegisteredUser) {
            File file = new File(propFileName);
            if (!file.exists()) {
                try {
                    NeptuneLogger.getInstance().getLogger().log(Level.INFO, "Not Registered..");

                    JTextField userName = new JTextField();
                    JTextField deviceToken = new JTextField();

                    userName.setPreferredSize(new Dimension(150, 30));
                    deviceToken.setPreferredSize(new Dimension(150, 30));

                    JButton cancel = new JButton("Cancel");
                    final JComponent[] inputs = new JComponent[]{
                        DataHolder.registrationMsg,
                        new JLabel("UserName"),
                        userName,
                        new JLabel("Device Token"),
                        deviceToken
                    };
                    JOptionPane.showMessageDialog(null, inputs, "Registration", JOptionPane.ERROR_MESSAGE);

                    if (!deviceToken.getText().trim().equalsIgnoreCase("") && !userName.getText().equalsIgnoreCase("")) {
                        DataHolder.macAddress = HelperUtility.getMACAddress();
                        StringBuilder url = new StringBuilder(DataHolder.httpURL + "/neptune/Sync/GetSync.ashx?UserName=" + userName.getText().trim().replace(" ", "%20")
                                + "&DeviceToken=" + deviceToken.getText().trim());
                        if(!"".equalsIgnoreCase(DataHolder.macAddress)){
                           url.append("&MacId="+DataHolder.macAddress);
                        }
                        String result = helperUtility.getUserMediaUpdates(url.toString());
                        if (result != null) {
                            if (result.substring(0, ((result.length() < 5) ? result.length() : 4)).trim().equalsIgnoreCase("<?xml")) {
                                FileOutputStream fos = new FileOutputStream(DataHolder.contentSyncFile);
                                fos.write(result.toString().getBytes());
                            }
                            switch (Integer.parseInt(result)) {
                                case 1:
                                    file.createNewFile();
                                    FileOutputStream fileInputStream = new FileOutputStream(file);
                                    fileInputStream.write(("userName=" + userName.getText().trim()).getBytes());
                                    fileInputStream.write("\n".getBytes());
                                    fileInputStream.write("SystemName=AlphaDemoDevice".getBytes());
                                    fileInputStream.write("\n".getBytes());
                                    fileInputStream.write(("DeviceToken=" + deviceToken.getText().trim()).getBytes());
                                    fileInputStream.close();
                                    fileInputStream = null;
                                    file.setReadOnly();
                                    file = null;
                                    NeptuneLogger.getInstance().getLogger().info("Registration SuccessFully Completed");
                                    break;
                                case 2:
                                    DataHolder.registrationMsg.setText("Failed-Due to incorrect User Name");
                                    break;
                                case 3:
                                    DataHolder.registrationMsg.setText("Faled-Due to incorrect Device Token");
                                    break;
                                case 4:
                                    DataHolder.registrationMsg.setText("Failed-Due to Incorrect User Name and Device Token");
                                    break;
                            }
                        }
                    }

                } catch (IOException ex) {
                    NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
                }

            } else {
//                try {
//                    Process p = Runtime.getRuntime().exec("shutdown -r");
//                } catch (IOException ex) {
//                    Logger.getLogger(ParentController.class.getName()).log(Level.SEVERE, null, ex);
//                }
                DataHolder.isRegisteredUser = true;
                File contentSynFile = new File(DataHolder.contentSyncFile);
                DataHolder.configProperties = new Properties();
                FileInputStream inputStream = null;
                try {
                    inputStream = new FileInputStream(propFileName);
                    if (inputStream != null) {
                        DataHolder.configProperties.load(inputStream);
                    } else {
                        throw new FileNotFoundException("property file '" + DataHolder.propFileName + "' not found in the classpath");
                    }
                } catch (FileNotFoundException ex) {
                    NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                            inputStream = null;
                        }
                    } catch (IOException ex) {
                        NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
                    }
                }

                Random r = new Random();
                Timer stTime = new Timer();
                ScheduledTask st = new ScheduledTask();
                stTime.schedule(st, 0, 1000 * (r.nextInt(1200 - 600) + 600));

                Timer pbmTime = new Timer();
                PlayBackMonitor pbm = new PlayBackMonitor();
                pbmTime.schedule(pbm, 0, 1000);

                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        PlayerForm dialog = new PlayerForm(new javax.swing.JFrame(), true);
                        dialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                            @Override
                            public void windowClosing(java.awt.event.WindowEvent e) {
                                System.exit(0);
                            }
                        });
                        dialog.setVisible(true);
                    }
                });

            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
            }
        }
    }
}
