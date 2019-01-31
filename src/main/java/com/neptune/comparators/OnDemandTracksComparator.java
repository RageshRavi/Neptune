/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptune.comparators;

import com.neptune.schema.Media.Channels.Station.Programme.Playlist.Track;
import com.neptune.util.DateUtil;
import java.util.Comparator;
import java.util.Date;

/**
 *
 * @author ragesh.raveendran
 */
public class OnDemandTracksComparator implements Comparator<Track>{

    public int compare(Track o1, Track o2) {
        Date track1StartTime = DateUtil.getInstance().getDate(o1.getStarttime(), DateUtil.DATE_FORMAT_HH_MM_SS);
        Date track2StartTime = DateUtil.getInstance().getDate(o2.getStarttime(), DateUtil.DATE_FORMAT_HH_MM_SS);
        if(track1StartTime.before(track2StartTime)){
            return -1;
        }
        if(track1StartTime.after(track2StartTime)){
            return 1;
        }
        
          return 0;  
    }
    
}
