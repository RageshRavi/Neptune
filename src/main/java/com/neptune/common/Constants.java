/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptune.common;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;

/**
 *
 * @author ragesh.raveendran
 */
public final class Constants {
    public static final String PLAY = "Play";
    public static final String PAUSE = "Pause";
    public static final String PREVIOUS = "Previous";
    public static final String NEXT = "Next";
    public static final String ONDEMAND = "On Demand";
    

    public static JLabel status = new JLabel("");
    public static JLabel statusComment = new JLabel("123");
    public static JLabel labelFileName = new JLabel("Playing File:");
    public static JLabel labelTimeCounter = new JLabel("00:00:00");
    public static JLabel labelDuration = new JLabel("00:00:00");

    public static JButton buttonPrev = new JButton(Constants.PREVIOUS);

    public static JButton buttonNext = new JButton(Constants.NEXT);
    public static JButton buttonPlay = new JButton(Constants.PLAY);
    public static JButton buttonPause = new JButton(Constants.PAUSE);
    public static JButton buttonOnDemand = new JButton(Constants.ONDEMAND);

    public static JSlider sliderTime = new JSlider();
    public static final String EMPTY_STRING = "";
    public static final String SEARCHING = "Searching...";

}
