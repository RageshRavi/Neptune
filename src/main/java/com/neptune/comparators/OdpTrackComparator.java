/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptune.comparators;

import com.neptune.schema.Odpmedia.Playlist.Track;
import java.util.Comparator;

/**
 *
 * @author Chester
 */
public class OdpTrackComparator implements Comparator<Track> {

    public int compare(Track o1, Track o2) {
        return o1.getId().compareTo(o2.getId());
    }

}
