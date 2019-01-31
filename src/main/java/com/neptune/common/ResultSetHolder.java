/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptune.common;

import java.util.Date;

/**
 *
 * @author ragesh.raveendran
 */
public class ResultSetHolder {
    int partner_id;
    int station_id;
    int playlist_id;
    int playlist_tracks_id;
    int track_id;
    String station_name;
    String playlist_name;
    Date playlist_starttime;
    Date playlist_endtime;
    String playlist_repeat;
    String track_name;
    String track_artist;
    String archive;
    int track_duration;

    public int getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(int partner_id) {
        this.partner_id = partner_id;
    }

    public int getStation_id() {
        return station_id;
    }

    public void setStation_id(int station_id) {
        this.station_id = station_id;
    }

    public int getPlaylist_id() {
        return playlist_id;
    }

    public void setPlaylist_id(int playlist_id) {
        this.playlist_id = playlist_id;
    }

    public int getPlaylist_tracks_id() {
        return playlist_tracks_id;
    }

    public void setPlaylist_tracks_id(int playlist_tracks_id) {
        this.playlist_tracks_id = playlist_tracks_id;
    }

    public int getTrack_id() {
        return track_id;
    }

    public void setTrack_id(int track_id) {
        this.track_id = track_id;
    }

    public String getStation_name() {
        return station_name;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    public String getPlaylist_name() {
        return playlist_name;
    }

    public void setPlaylist_name(String playlist_name) {
        this.playlist_name = playlist_name;
    }

    public Date getPlaylist_starttime() {
        return playlist_starttime;
    }

    public void setPlaylist_starttime(Date playlist_starttime) {
        this.playlist_starttime = playlist_starttime;
    }

    public Date getPlaylist_endtime() {
        return playlist_endtime;
    }

    public void setPlaylist_endtime(Date playlist_endtime) {
        this.playlist_endtime = playlist_endtime;
    }

    public String getPlaylist_repeat() {
        return playlist_repeat;
    }

    public void setPlaylist_repeat(String playlist_repeat) {
        this.playlist_repeat = playlist_repeat;
    }

    public String getTrack_name() {
        return track_name;
    }

    public void setTrack_name(String track_name) {
        this.track_name = track_name;
    }

    public String getTrack_artist() {
        return track_artist;
    }

    public void setTrack_artist(String track_artist) {
        this.track_artist = track_artist;
    }

    public String getArchive() {
        return archive;
    }

    public void setArchive(String archive) {
        this.archive = archive;
    }

    public int getTrack_duration() {
        return track_duration;
    }

    public void setTrack_duration(int track_duration) {
        this.track_duration = track_duration;
    }

}
