/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WidgetUtils;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.net.*;

public class CreateSlider{
     JSlider slider;
     JLabel label;
     public static String strCommand = new String();
     public static String str = new String();

     public static void main(String[] args){
        CreateSlider cs = new CreateSlider();
     }

  public CreateSlider(){
     JFrame frame = new JFrame("OpenBSD Volume");
     slider = new JSlider();
     slider.setMinimum(1);
     slider.setMaximum(255);
     slider.setValue(128);
     slider.addChangeListener(new MyChangeAction());
     label = new JLabel("By Matt");
     JPanel panel = new JPanel();
     panel.add(slider);
     panel.add(label);
     frame.add(panel, BorderLayout.CENTER);
     frame.setSize(400, 90);
     frame.setVisible(true);
     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

   public class SubmitProcess {
      public SubmitProcess (String strUserCommand) {
         try {
             // Create a runtime process (our script)
             Process proc = Runtime.getRuntime().exec(strUserCommand);

             // for the scripts output:
          InputStream in = proc.getInputStream();
             InputStreamReader reader_in = new InputStreamReader( in );
             BufferedReader buffered_reader_in = new BufferedReader( reader_in );
             String        inStr;


             // read and display script output (which should be what we entered above)
             // Here's where we write the output to a log - when we decide to incorporate that functionality
             try {
                while ( (inStr = buffered_reader_in.readLine()) != null)
                   System.out.println(inStr);
                   if (proc.waitFor() != 0) // if the command failed
                      // NOTE: You might receive this if you mistakenly use a valid command and you pass it bad args
                      //       For example the GUI submitted "Replace this string with your command"
                      //       to the system. System ran Replace.exe which puked with the bad parameters.
                      System.out.println(" waitfor command failed to execute");
             }
             catch (InterruptedException e) {
                System.out.println("SubmitProcess Error reading stream, see log for more detail: " + e);
             }
         }
         catch (IOException e) {
            System.out.println("SubmitProcess Error, see log for more detail: " + e);
         }
     }
  }

  public class MyChangeAction implements ChangeListener{
     public void stateChanged(ChangeEvent ce){
        int value = slider.getValue();
        str = Integer.toString(value);
        label.setText(str);
        strCommand = new String("mixerctl -q outputs.master=" + str + "," + str );
        SubmitProcess UserCommand = new SubmitProcess(strCommand); 
     }
   }
   


}