/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WidgetUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;

/**
 *
 * @author ragesh.raveendran
 */
public class AudioSample1 {

    public static void main(String args[]) {

        try {
            //        AudioInputStream audioInputStream = null;
//        try {
//            audioInputStream = AudioSystem.getAudioInputStream(
//                    new File("D:\\Downloads\\MP3\\Favourites\\03  Pitbull - Rain Over Me (Feat. Marc Anthony).mp3"));
//            Clip clip = AudioSystem.getClip();
//            clip.open(audioInputStream);
//            FloatControl gainControl
//                    = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
//            gainControl.setValue(-10.0f); // Reduce volume by 10 decibels.
//            clip.start();
//        } catch (UnsupportedAudioFileException ex) {
//            Logger.getLogger(AudioSample1.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(AudioSample1.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (LineUnavailableException ex) {
//            Logger.getLogger(AudioSample1.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            try {
//                audioInputStream.close();
//            } catch (IOException ex) {
//                Logger.getLogger(AudioSample1.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
        
//        Mixer.Info[] infos = AudioSystem.getMixerInfo();
//        System.out.println("Size : "+infos.length);
//        for (Mixer.Info info: infos)
//        {
//           Mixer mixer = AudioSystem.getMixer(info);
//           if (mixer.isLineSupported(Port.Info.SPEAKER))    
//           {    System.out.println("Supported speaker");
//               try {
//                   Port port = (Port)mixer.getLine(Port.Info.SPEAKER);
//                   port.open();
//                   if (port.isControlSupported(FloatControl.Type.VOLUME))
//                   {
//                       System.out.println("Reducing volume");
//                       FloatControl volume =  (FloatControl)port.getControl(FloatControl.Type.VOLUME);
//                       volume.setValue(-100F);
//                   } else{
//                       System.out.println("Not supported");
//                   }   
//                   port.close();
//               } catch (LineUnavailableException ex) {
//                   Logger.getLogger(AudioSample1.class.getName()).log(Level.SEVERE, null, ex);
//               }
//           }
//         }
            int i =0;
            while(i<5){
//                Process proc = Runtime.getRuntime().exec("amixer -D pulse sset Master 10%-");
            Thread.sleep(100L);
            Thread.sleep(100L);
            Thread.sleep(100L);
            Thread.sleep(100L);
            i++;
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(AudioSample1.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(AudioSample1.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    }}
