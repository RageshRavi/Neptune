/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WidgetUtils;

import java.util.Vector;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

/**
 *
 * @author ragesh.raveendran
 */
public class AudioSystem1 {

    public static void main(String args[]) {
        Vector synthInfos = new Vector();
        MidiDevice device;
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        System.out.println(infos.length);
        for (int i = 0; i < infos.length; i++) {
            try {
                System.out.println(infos[i]);
                device = MidiSystem.getMidiDevice(infos[i]);
                if (device instanceof Synthesizer) {
                    System.out.println(device + "is a synthesizer");
                synthInfos.add(infos[i]);
            }
            } catch (MidiUnavailableException e) {
                e.printStackTrace();
            }
//            
        }
    }
}
