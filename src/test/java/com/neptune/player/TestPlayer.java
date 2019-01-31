/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptune.player;

import com.neptune.util.DateUtil;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
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
public class TestPlayer {

    public static void main(String args[]) {
        try {
            File mp3 = new File("D:\\Downloads\\MP3\\07 - Surili Akhiyon Wale (Duet)-(MyMp3Singer.com).mp3");
            FileInputStream fis = new FileInputStream(mp3);
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(mp3);
            System.out.println("Properties : "+fileFormat.properties());
            Float fps = (Float)fileFormat.properties().get("mp3.framerate.fps");
            Integer lastFrame = (Integer)fileFormat.properties().get("mp3.length.frames");
            
            Date curreDate = DateUtil.getInstance().getDate(DateUtil.getInstance().formatDate(new Date(), DateUtil.DATE_FORMAT_HHMMSS), DateUtil.DATE_FORMAT_HHMMSS);
            Date startTime = DateUtil.getInstance().getDate("17:51:00", DateUtil.getInstance().DATE_FORMAT_HH_MM_SS);
//            Date length = DateUtil.getInstance().getDate("00:04:17", DateUtil.getInstance().DATE_FORMAT_HH_MM_SS);
            System.out.println("Difference"+DateUtil.getInstance().getTimeDifferenceInSeconds(
                    DateUtil.getInstance().getDate("17:48:00", DateUtil.getInstance().DATE_FORMAT_HH_MM_SS)));
            
//            long framesperSec = (mp3.length())/length.getTime();
            
//            System.out.println(curreDate.getTime());
//            System.out.println(startTime.getTime());
//            System.out.println(String.valueOf(mp3.length()));
//            Long diff =curreDate.getTime() - startTime.getTime();
//            fis.skip(400000L);
//starttime="18:00:00" length="00:03:43" endtime="18:03:43"
            BufferedInputStream bis = new BufferedInputStream(fis);
            AdvancedPlayer player = new AdvancedPlayer(bis);
//            Date startTime = DateUtil.getInstance().getDate("09:00:00", DateUtil.DATE_FORMAT_HHMMSS);
            
            System.out.println(new Date());
            Float value = fps * 300F;
            System.out.println("Value : "+value);
            
            Float end = fps * 180;
            player.play(value.intValue(), lastFrame);
            System.out.println(new Date());
//            Thread.sleep(1000);
//            player.play();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TestPlayer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JavaLayerException ex) {
            Logger.getLogger(TestPlayer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestPlayer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(TestPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
