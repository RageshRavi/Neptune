/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptune.controller;

/**
 *
 * @author ragesh.raveendran
 */
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class MarqueeTest {

    private void display() {
        JFrame f = new JFrame("MarqueeTest");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String s = "Tomorrow, and tomorrow, and tomorrow, "
        + "creeps in this petty pace from day to day, "
        + "to the last syllable of recorded time; ... "
        + "It is a tale told by an idiot, full of "
        + "sound and fury signifying nothing.";
        MarqueePanel mp = new MarqueePanel(s, 32);
        f.add(mp);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        mp.start();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new MarqueeTest().display();
            }
        });
    }
}

/** Side-scroll n characters of s. */
class MarqueePanel extends JLabel implements ActionListener {

    private static final int RATE = 12;
    private final Timer timer = new Timer(1000 / RATE, this);
    private final JLabel label = new JLabel();
    private final String s;
    private final int n;
    private int index;

    public MarqueePanel(String s, int n) {
        if (s == null || n < 1) {
            throw new IllegalArgumentException("Null string or n < 1");
        }
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            sb.append(' ');
        }
        this.s = sb + s + sb;
        this.n = n;
        label.setFont(new Font("Serif", Font.ITALIC, 36));
        label.setText(sb.toString());
        this.add(label);
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        index++;
        if (index > s.length() - n) {
            index = 0;
        }
        label.setText(s.substring(index, index + n));
    }
}